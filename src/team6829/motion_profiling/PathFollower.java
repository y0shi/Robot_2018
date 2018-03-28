package team6829.motion_profiling;

import java.io.File;

import edu.wpi.first.wpilibj.command.Command;
import team6829.common.DriveTrain;

/**
 *
 */
public class PathFollower extends Command {

	private DriveTrain driveTrain;
	private TrajectoryController trajectoryController;
	
	private double timestep = 0.005;

	
	public PathFollower(DriveTrain driveTrain, File csvLeft, File csvRight, boolean direction) {

		this.driveTrain = driveTrain;

		
		trajectoryController = new TrajectoryController(driveTrain, csvLeft, csvRight, direction);

	}


	protected void initialize() {

		driveTrain.zeroEncoders();
		driveTrain.zeroAngle();
		trajectoryController.resetFollowers();
		trajectoryController.configureFollow();
		
		trajectoryController.trajectoryNotifier.startPeriodic(timestep);

	}

	protected void execute() {
		
	}

	protected boolean isFinished() {
		return trajectoryController.isTrajectoryDone();
	}

	// Called once after isFinished returns true
	protected void end() {

		driveTrain.stop();
		trajectoryController.trajectoryNotifier.stop();

		trajectoryController.resetFollowers();
		driveTrain.zeroEncoders();
		driveTrain.zeroAngle();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {

		end();

	}
}
