package org.frc.team5409.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.subsystems.shooter.*;
import org.frc.team5409.robot.subsystems.shooter.ShooterTurret.ResetSwitchType;

public final class CalibrateShooter extends CommandBase {
    private final ShooterTurret m_turret;

    public CalibrateShooter(ShooterTurret sys_rotation, ShooterFlywheel sys_flywheel) {
        m_turret = sys_rotation;

        addRequirements(sys_rotation, sys_flywheel);
    }

    @Override
    public void initialize() {
        m_turret.enable();
        m_turret.setSafety(false);
        
        m_turret.setRaw(Constants.ShooterControl.shooter_calibrate_speed);
    }

    @Override
    public void end(boolean interrupted) {
        m_turret.disable();
    }

    @Override
    public boolean isFinished() {
        ResetSwitchType type = m_turret.getActiveResetSwitch();

        if (type != ResetSwitchType.kNone) {
            System.out.println("SENSOR SENSEDSENSOR SENSEDSENSOR SENSEDSENSOR SENSEDSENSOR SENSEDSENSOR SENSEDSENSOR SENSEDSENSOR SENSED");
            m_turret.resetRotation(type);
            return true;
        }

        return !m_turret.isEnabled();
    }
}