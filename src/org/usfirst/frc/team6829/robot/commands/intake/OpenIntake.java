package org.usfirst.frc.team6829.robot.commands.intake;

import org.usfirst.frc.team6829.robot.subsystems.IntakeClaw;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class OpenIntake extends Command {

	private IntakeClaw intake;
    public OpenIntake(IntakeClaw intake) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	this.intake = intake;
    	requires(this.intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	intake.openClaw();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	intake.stopClaw();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
