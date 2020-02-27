package org.frc.team5409.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frc.team5409.robot.subsystems.shooter.ShooterFlywheel;
import org.frc.team5409.robot.subsystems.shooter.ShooterTurret;
import org.frc.team5409.robot.subsystems.*;
import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.util.*;

/**
 * This command runs the turret flywheel at a speed
 * porportional to the distance of the turret from the
 * shooting target and operates the indexer once the rpm
 * reaches it's setpoint.
 * 
 * @author Keith Davies
 */
public final class SmoothSweep extends CommandBase {
    private final ShooterTurret   m_turret;

    private final SimpleEquation  m_smooth_sweep, m_smooth_sweep_inverse;

    private       double          m_timer, m_smooth_sweep_toff;

    public SmoothSweep(ShooterTurret sys_turret, ShooterFlywheel sys_flywheel) {
        m_turret = sys_turret;

        m_smooth_sweep_inverse = Constants.ShooterControl.shooter_smooth_sweep_inverse;
        m_smooth_sweep = Constants.ShooterControl.shooter_smooth_sweep_func;
    
        addRequirements(sys_flywheel, sys_turret);
    }

    @Override
    public void initialize() {
        m_turret.enable();

        m_smooth_sweep_toff = m_smooth_sweep_inverse.calculate(m_turret.getRotation());
        m_timer = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        double time = Timer.getFPGATimestamp() - m_timer;
        double target = m_smooth_sweep.calculate(time + m_smooth_sweep_toff);
        m_turret.setRotation(target + 270);

        SmartDashboard.putNumber("Smooth Sweep target", target + 270);
    }

    @Override
    public void end(boolean interrupted) {
        m_turret.disable();
    }
    
    @Override
    public boolean isFinished() {
        return !m_turret.isEnabled();
    }
}