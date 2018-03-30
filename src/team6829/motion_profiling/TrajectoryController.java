package team6829.motion_profiling;

import java.io.File;

import edu.wpi.first.wpilibj.Notifier;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import team6829.common.DriveTrain;

public class TrajectoryController {

	private DriveTrain driveTrain;

	private static final int TICKS_PER_REVOLUTION = 8192; 
	private static final double WHEEL_DIAMETER = 0.5; //in feet

	private static final double LEFT_KP = 1.2;
	private static final double LEFT_KI = 0; // not used
	private static final double LEFT_KD = 0.0;
	private static final double LEFT_KA = 0; // acceleration gain

	private static final double RIGHT_KP = 1.2;
	private static final double RIGHT_KI = 0; // not used
	private static final double RIGHT_KD = 0.0;
	private static final double RIGHT_KA = 0; // acceleration gain

	private static final double MAXIMUM_VELOCITY = 6; //placeholder
	
	private Trajectory leftTrajectory ;
	private Trajectory rightTrajectory;

	private EncoderFollower left;
	private EncoderFollower right;
	
	private boolean defaultDirection;
	
	public TrajectoryController (DriveTrain driveTrain, File csvLeft, File csvRight, boolean defaultDirection) {

		this.driveTrain = driveTrain;

		this.defaultDirection = defaultDirection;
		
		this.driveTrain.defaultDirection();	
		
//		if (defaultDirection) {
//			this.driveTrain.defaultDirection();	
//		} else {
//			this.driveTrain.reverseDirection();
//		}
		
		leftTrajectory = Pathfinder.readFromCSV(csvLeft);
		rightTrajectory = Pathfinder.readFromCSV(csvRight);

		left = new EncoderFollower(leftTrajectory);
		right = new EncoderFollower(rightTrajectory);

	}
	
	class PeriodicRunnable implements Runnable {

		@Override
		public void run() {
			followTrajectory();
		}
		
	}

	public Notifier trajectoryNotifier = new Notifier(new PeriodicRunnable());
	
	public void configureFollow() {

		left.configureEncoder((driveTrain.getLeftEncoderPosition()), TICKS_PER_REVOLUTION, WHEEL_DIAMETER);

		right.configureEncoder((driveTrain.getRightEncoderPosition()), TICKS_PER_REVOLUTION, WHEEL_DIAMETER);

		left.configurePIDVA(LEFT_KP, LEFT_KI, LEFT_KD, 1 / MAXIMUM_VELOCITY, LEFT_KA);
		right.configurePIDVA(RIGHT_KP, RIGHT_KI, RIGHT_KD, 1 / MAXIMUM_VELOCITY, RIGHT_KA);

	}

	private void followTrajectory() {
		
		double l = left.calculate((driveTrain.getLeftEncoderPosition()));
		double r = right.calculate((driveTrain.getRightEncoderPosition()));
		
		double currentHeading = driveTrain.getYaw();
//
//		if (!defaultDirection) {
//			currentHeading *= -1;
//		}
		
		double desiredHeading = Pathfinder.r2d(left.getHeading());

		double angleError =  Pathfinder.boundHalfDegrees(desiredHeading - currentHeading);
		double turn =  0.8 * (-1.0 / 80.0) * angleError;
		
		driveTrain.setLeftDrivePower(l);
		driveTrain.setRightDrivePower(r);

	}

	public void resetFollowers() {

		left.reset();
		right.reset();

	}

	public boolean isTrajectoryDone() {
		return left.isFinished();
	}

}