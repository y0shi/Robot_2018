package org.usfirst.frc.team6829.robot.commands.intake;

import org.usfirst.frc.team6829.robot.subsystems.IntakeLift;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ZeroIntake extends Command {

	private IntakeLift intake;
	
	private double power = 0.2; // TODO: Make sure the sign is right
	
    public ZeroIntake(IntakeLift intake) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	this.intake = intake;
    	requires(this.intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	intake.setLiftPower(power);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return intake.isLimitSensorTripperd();
    }

    // Called once after isFinished returns true
    protected void end() {
    	intake.zeroLift();
    	intake.stopLift();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
