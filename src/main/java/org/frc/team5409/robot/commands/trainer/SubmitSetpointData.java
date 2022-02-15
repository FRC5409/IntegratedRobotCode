package org.frc.team5409.robot.commands.trainer;

import java.util.concurrent.Future;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.frc.team5409.robot.training.protocol.NetworkClient;
import org.frc.team5409.robot.training.protocol.NetworkStatus;
import org.frc.team5409.robot.training.protocol.NetworkTransaction;
import org.frc.team5409.robot.training.protocol.NetworkTransactionResult;
import org.frc.team5409.robot.training.protocol.generic.KeyValueSendable;
import org.frc.team5409.robot.training.protocol.generic.StringSendable;
import org.frc.team5409.robot.training.robot.TrainerDashboard;
import org.frc.team5409.robot.training.robot.TrainingContext;
import org.frc.team5409.robot.training.robot.TrainingModel;

public class SubmitSetpointData extends CommandBase {
    private final TrainingContext _context;
    private final NetworkClient _client;
    private final TrainerDashboard _dashboard;

    private Future<NetworkTransactionResult> _request;

    public SubmitSetpointData(TrainerDashboard dashboard, NetworkClient client, TrainingContext context) {
        _context = context;
        _client = client;
        _dashboard = dashboard;
        _request = null;
    }

    @Override
    public void initialize() {
        KeyValueSendable payload = new KeyValueSendable();
            payload.putSendable("trainer.topic", new StringSendable("trainer.setpoint"));
            payload.putDouble("trainer.data.speed", _context.getSetpoint().getTarget());
            payload.putDouble("trainer.data.distance", _context.getDistance());

        _request = _client.submitTransactionAsync(
            new NetworkTransaction(payload)
        );
    }

    @Override
    public void end(boolean interrupted) {    
        try {
            NetworkTransactionResult result = _request.get();
            if (result.getStatus() == NetworkStatus.STATUS_OK) {
                KeyValueSendable payload = (KeyValueSendable) result.getSendableResult();
                StringSendable topic = (StringSendable) payload.getSendable("trainer.topic");

                if (topic.getValue().equals("trainer.update-model")) {
                    double modelA = payload.getDouble("trainer.model.parameters[0]");
                    double modelB = payload.getDouble("trainer.model.parameters[1]");
                    double modelC = payload.getDouble("trainer.model.parameters[2]");
                    double modelD = payload.getDouble("trainer.model.parameters[3]");

                    _context.setModel(new TrainingModel(modelA, modelB, modelC, modelD));
                    _dashboard.update();
                }

                System.out.println("Received payload : " + payload);
            } else {
                System.out.println("Received status : " + result.getStatus());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve result of request", e);
        }
    }

    @Override
    public boolean isFinished() {
        return _request.isDone() || _request.isCancelled();
    }
}
