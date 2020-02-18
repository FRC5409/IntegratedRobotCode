/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.frc.team5409.robot.subsystems.Intake;

public class IntakeActive extends CommandBase {
  private final Intake m_intakeSubsystem;

  /**
   * Creates a new IntakeActive.
   */
  public IntakeActive(Intake subsystem) {
    m_intakeSubsystem = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // Call extend method and set speed to 0.5
    m_intakeSubsystem.extend();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // Call retract method an set speed to 0
    m_intakeSubsystem.retract();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}