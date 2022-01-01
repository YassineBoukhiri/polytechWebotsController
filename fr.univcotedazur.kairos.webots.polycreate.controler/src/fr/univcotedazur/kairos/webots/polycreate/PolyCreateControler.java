/*******************************************************************************
 * Copyright (c) 2017 I3S and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     UCA - I3S Laboratory - julien.deantoni@univ-cotedazur.fr -> initial API
 *******************************************************************************/

package fr.univcotedazur.kairos.webots.polycreate;

import java.util.Random;

//import org.eclipse.january.dataset.Dataset;
//import org.eclipse.january.dataset.DatasetFactory;

import com.cyberbotics.webots.controller.Camera;
import com.cyberbotics.webots.controller.CameraRecognitionObject;
import com.cyberbotics.webots.controller.DistanceSensor;
import com.cyberbotics.webots.controller.GPS;
import com.cyberbotics.webots.controller.LED;
import com.cyberbotics.webots.controller.Motor;
import com.cyberbotics.webots.controller.Node;
import com.cyberbotics.webots.controller.Pen;
import com.cyberbotics.webots.controller.PositionSensor;
import com.cyberbotics.webots.controller.Receiver;
import com.cyberbotics.webots.controller.Robot;
import com.cyberbotics.webots.controller.Supervisor;
import com.cyberbotics.webots.controller.TouchSensor;
import com.yakindu.core.TimerService;
import com.yakindu.core.rx.Observable;

import fr.univcotedazur.kairos.webots.polycreate.controler.RobotStatemachine;


public class PolyCreateControler extends Supervisor {

	public RobotStatemachine fsm;

	static int MAX_SPEED = 16;
	static int NULL_SPEED = 0;
	static int HALF_SPEED = 8;
	static int MIN_SPEED = -16;

	static double WHEEL_RADIUS = 0.031;
	static double AXLE_LENGTH = 0.271756;
	static double ENCODER_RESOLUTION = 507.9188;
	static double turnPrecision= 0.05;

	/**
	 * the inkEvaporation parameter in the WorldInfo element of the robot scene may
	 * be interesting to access
	 */
	public Pen pen = null;

	public Pen getPen() {
		return pen;
	}

	public Motor[] gripMotors = new Motor[2];
	public DistanceSensor gripperSensor = null;

	public Motor leftMotor = null;
	public Motor rightMotor = null;

	public PositionSensor leftSensor = null;
	public PositionSensor rightSensor = null;

	public LED ledOn = null;
	public LED ledPlay = null;
	public LED ledStep = null;

	public TouchSensor leftBumper = null;
	public TouchSensor rightBumper = null;

	public DistanceSensor leftCliffSensor = null;
	public DistanceSensor rightCliffSensor = null;
	public DistanceSensor frontLeftCliffSensor = null;
	public DistanceSensor frontRightCliffSensor = null;

	public DistanceSensor frontDistanceSensor = null;
	public DistanceSensor frontLeftDistanceSensor = null;
	public DistanceSensor frontRightDistanceSensor = null;

	public Camera frontCamera = null;
	public Camera backCamera = null;

	public Receiver receiver = null;

	public GPS gps = null;

	public int timestep = Integer.MAX_VALUE;
	public Random random = new Random();
	
	private boolean isTurning = false;

	public PolyCreateControler() {

		timestep = (int) Math.round(this.getBasicTimeStep());

		pen = createPen("pen");

		gripMotors[0] = createMotor("motor 7");
		gripMotors[1] = createMotor("motor 7 left");
		gripperSensor = createDistanceSensor("gripper middle distance sensor");
		gripperSensor.enable(timestep);

		leftMotor = createMotor("left wheel motor");
		rightMotor = createMotor("right wheel motor");
		leftMotor.setPosition(Double.POSITIVE_INFINITY);
		rightMotor.setPosition(Double.POSITIVE_INFINITY);
		leftMotor.setVelocity(0.0);
		rightMotor.setVelocity(0.0);

		leftSensor = createPositionSensor("left wheel sensor");
		rightSensor = createPositionSensor("right wheel sensor");
		leftSensor.enable(timestep);
		rightSensor.enable(timestep);

		ledOn = createLED("led_on");
		ledPlay = createLED("led_play");
		ledStep = createLED("led_step");

		leftBumper = createTouchSensor("bumper_left");
		rightBumper = createTouchSensor("bumper_right");
		leftBumper.enable(timestep);
		rightBumper.enable(timestep);

		leftCliffSensor = createDistanceSensor("cliff_left");
		rightCliffSensor = createDistanceSensor("cliff_right");
		frontLeftCliffSensor = createDistanceSensor("cliff_front_left");
		frontRightCliffSensor = createDistanceSensor("cliff_front_right");
		leftCliffSensor.enable(timestep);
		rightCliffSensor.enable(timestep);
		frontLeftCliffSensor.enable(timestep);
		frontRightCliffSensor.enable(timestep);

		frontDistanceSensor = createDistanceSensor("front distance sensor");
		frontDistanceSensor.enable(timestep);
		frontLeftDistanceSensor = createDistanceSensor("front left distance sensor");
		frontLeftDistanceSensor.enable(timestep);
		frontRightDistanceSensor = createDistanceSensor("front right distance sensor");
		frontRightDistanceSensor.enable(timestep);

		frontCamera = createCamera("frontCamera");
		frontCamera.enable(timestep);
		frontCamera.recognitionEnable(timestep);

		backCamera = createCamera("backCamera");
		backCamera.enable(timestep);
		backCamera.recognitionEnable(timestep);

		receiver = createReceiver("receiver");
		receiver.enable(timestep);

		gps = createGPS("gps");
		gps.enable(timestep);

		PolyCreateControler ctrl = this;
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("Shutdown hook ran!");
				ctrl.delete();
			}
		});

		/**
		 * Initialize the robot
		 */
		fsm = new RobotStatemachine();

		/*
		 * Subscribe to the events
		 */

		fsm.getDoTurnRandomlyLeft().subscribe(new DoTurnRandomlyLeftObserver(this));
		fsm.getDoTurnRandomlyRight().subscribe(new DoTurnRandomlyRightObserver(this));
		fsm.getDoFullTurn().subscribe(new DoFullTurnObserver(this));
		fsm.getDoOpenGripper().subscribe(new DoOpenGripperObserver(this));
		fsm.getDoCloseGripper().subscribe(new DoCloseGripperObserver(this));
		fsm.getDoGoForward().subscribe(new DoGoForwardObserver(this));
		fsm.getDoGoBackward().subscribe(new DoGoBackwardObserver(this));
		fsm.getDoGoTo().subscribe(new DoGoToObserver(this));
		fsm.getDoCatch().subscribe(new DoCatchObserver(this));
		fsm.getDoFlushIRReceiver().subscribe(new DoFlushReceiverObserver(this));

		/*
		 * Start the robot
		 */
		/*TimerService timer = new TimerService();
		fsm.setTimerService(timer);
		*/
		fsm.enter();
		
	}

	public void listen(){
		CameraRecognitionObject[] frontObjs = this.frontCamera.getRecognitionObjects();
		//CameraRecognitionObject[] backObjs = this.backCamera.getRecognitionObjects();
		if (isThereVirtualwall()) {
			System.out.println("Virtual wall detected\n");
			fsm.raiseVirtualWall();
			this.flushIRReceiver();
		} else  if (isThereCollisionAtLeft() || frontLeftDistanceSensor.getValue() < 250) {
			System.out.println("          Left obstacle detected\n");
			fsm.raiseFrontL();
			passiveWait(0.5);
		} else if (isThereCollisionAtRight() || frontRightDistanceSensor.getValue() < 250 ||frontDistanceSensor.getValue() < 250) {
			System.out.println("          Right obstacle detected\n");
			fsm.raiseFrontR();
			fsm.raiseFront();
			passiveWait(0.5);
		} else {
			fsm.raiseClear();
		}
		if (frontObjs.length > 0 ) {
			fsm.raiseGoTo();
		}
		if(this.getObjectDistanceToGripper() < 120) {
			fsm.raiseCatch();
		}
	}

	public void doOpenGripper() {
		openGripper();
	}

	public void doCloseGripper() {
		closeGripper();
	}

	public void doTurnRandomlyLeft() {
		turn(Math.PI * this.randdouble() + 0.6);
	}

	public void doTurnRandomlyRight() {
		turn(-Math.PI * this.randdouble() - 0.6);
	}

	public void doFullTurn() {
		turn(Math.PI);
	}

	public void doGoForward() {
		goForward();
	}

	public void doGoBackward() {
		goBackward();
	}

	public void openGripper() {
		gripMotors[0].setPosition(0.5);
		gripMotors[1].setPosition(0.5);
	}

	public void closeGripper() {
		gripMotors[0].setPosition(-0.2);
		gripMotors[1].setPosition(-0.2);
	}

	/**
	 * give the obstacle distance from the gripper sensor. max distance (i.e., no
	 * obstacle detected) is 1500
	 * 
	 * @return
	 */
	public double getObjectDistanceToGripper() {
		return gripperSensor.getValue();
	}

	public boolean isThereCollisionAtLeft() {
		return (leftBumper.getValue() != 0.0);
	}

	public boolean isThereCollisionAtRight() {
		return (rightBumper.getValue() != 0.0);
	}

	public void flushIRReceiver() {
		while (receiver.getQueueLength() > 0)
			receiver.nextPacket();
	}

	public boolean isThereVirtualwall() {
		return (receiver.getQueueLength() > 0);
	}

	public void goForward() {
		leftMotor.setVelocity(MAX_SPEED);
		rightMotor.setVelocity(MAX_SPEED);
	}

	public void goBackward() {
		leftMotor.setVelocity(-HALF_SPEED);
		rightMotor.setVelocity(-HALF_SPEED);
	}

	public void stop() {
		leftMotor.setVelocity(NULL_SPEED);
		rightMotor.setVelocity(NULL_SPEED);
	}

	synchronized void passiveWait(double sec) {
		double start_time = getTime();
		do {
			if (!isTurning ) {
				doStep();
			}
		} while (start_time + sec > getTime());
	}
	
	synchronized void doStep() {
		step(timestep);
	}
	
	/**
	 * 
	 * @return the orientation wrt the north in [0; 2PI[
	 */
	public double getOrientation() {
		double res = Math.acos(this.getSelf().getOrientation()[0]);
		if(this.getSelf().getOrientation()[1] < 0) {
			return (2*Math.PI) - res;
		}else {
			return res;
		}
	}
	
	

	public double randdouble() {
		return random.nextDouble();
	}

	
	/**
	 * turn the robot to getOrientation()+angle
	 * @param angle: the angle to apply
	 */
	void turn(double angle) {
		this.isTurning=true;
		stop();
		doStep();
		double direction = (angle < 0.0) ? -1.0 : 1.0;
		leftMotor.setVelocity(direction * HALF_SPEED);
		rightMotor.setVelocity(-direction * HALF_SPEED);
		double targetOrientation = (this.getOrientation()+angle)%(2*Math.PI);
		double actualOrientation;
		System.out.println("do");
		do {
			doStep();
			actualOrientation = this.getOrientation();
		} while (!(actualOrientation > (targetOrientation -turnPrecision) && actualOrientation < (targetOrientation + turnPrecision)));
		stop();
		doStep();
		this.isTurning=false;
	}
	
	public void doFlushIRReceiver() {
		System.out.println("flushReceiver");
		this.flushIRReceiver();
		
	}
	
	
	public void doCatch() {
		if(this.getObjectDistanceToGripper() > 115) {
			goBackward();
		}
		else {
			System.out.println("rear distance : "+ this.getObjectDistanceToGripper());
			stop();
			this.closeGripper();
			this.passiveWait(0.2);
			fsm.raiseGotIt();
		}		
	}
	
	
	
	public void doGoTo() {
		CameraRecognitionObject[] frontObjs = this.frontCamera.getRecognitionObjects();
		CameraRecognitionObject obj = frontObjs[0];
		System.out.println(frontObjs.length);
		int oid = obj.getId();
		Node obj2 = this.getFromId(oid);
		double[] frontObjPos = obj.getPosition();
		double x = frontObjPos[0];
		double y = frontObjPos[1];
		double angle = Math.atan(x/y*Math.PI/180);
		System.out.println(angle);
		if(angle >= turnPrecision) {
			turn(angle);
		}
		goForward();
		System.out.println("value   "+frontDistanceSensor.getValue());
		if(frontDistanceSensor.getValue() < 400) {
			stop();
			turn(Math.PI);
			this.passiveWait(0.6);
			fsm.raiseCatch();
			System.out.println("heeeey");
		}
		System.out.println("rear distance : "+ this.getObjectDistanceToGripper());
		
		/*if(this.getObjectDistanceToGripper() < 300) {
			System.out.println("rear distancedazdazdaz : "+ this.getObjectDistanceToGripper());
			stop();
			this.closeGripper();
			this.passiveWait(0.2);
			fsm.raiseGotIt();
		}*/
			
		/*if(this.getObjectDistanceToGripper() > 129) {
			goBackward();
			/*
			/* The position and orientation are expressed relatively to the camera (the
			relative position is the one of the center of the object which can differ
			from its origin) and the units are meter and radian.
			*/
		/*
			System.out.println(" I saw an object on back Camera at :"+backObjPos[0]*100+","+backObjPos[1]*100);
			System.out.println(" gripper distance sensor is "+this.getObjectDistanceToGripper());
			System.out.println("-> the orientation of the robot is "+Math.atan2(this.getSelf().getOrientation()[0],this.getSelf().getOrientation()[8]));
			System.out.println("->  the orientation of the robot is " +controler.getOrientation());
			System.out.println(" the position of the robot is "+Math.round(this.getSelf().getPosition()[0]*100)+";"+Math.round(this.getSelf().getPosition()[2]*100));
		}
		else {
			this.stop();
			this.closeGripper();
			this.passiveWait(0.2);
			fsm.raiseGotIt();
			//backCamera.recognitionDisable();
		}*/
	}

	/**
	 * The values are returned as a 3D-vector, therefore only the indices 0, 1, and
	 * 2 are valid for accessing the vector. The returned vector indicates the
	 * absolute position of the GPS device. This position can either be expressed in
	 * the cartesian coordinate system of Webots or using
	 * latitude-longitude-altitude, depending on the value of the
	 * gpsCoordinateSystem field of the WorldInfo node. The gpsReference field of
	 * the WorldInfo node can be used to define the reference point of the GPS. The
	 * wb_gps_get_speed function returns the current GPS speed in meters per second.
	 * 
	 * @return
	 */
	public double[] getPosition() {
		return gps.getValues();
	}

	public static void main(String[] args) {
		PolyCreateControler controler = new PolyCreateControler();
		controler.openGripper();
		controler.fsm.setGrabActivated(false);
		
		
		while (true) {
			controler.passiveWait(0.1);
			controler.listen();
		}
		
	}

	// public static void main(String[] args) {
	// PolyCreateControler controler = new PolyCreateControler();
	
	// try {
	// controler.openGripper();
	// controler.pen.write(true);
	// controler.ledOn.set(1);
	// controler.passiveWait(0.5);
	// System.out.println("let's start");
	// while (true) {
	// /**
	// * The position and orientation are expressed relatively to the camera (the
	// relative position is the one of the center of the object which can differ
	// from its origin) and the units are meter and radian.
	// *
	// https://www.cyberbotics.com/doc/reference/camera?tab-language=python#wb_camera_has_recognition
	// */
	// Node anObj = controler.getFromDef("can"); //should not be there, only to have
	// another orientation for testing...
	// controler.passiveWait(0.1);

	// // System.out.println("the orientation of the can is "
	// +controler.computeRelativeObjectOrientation(anObj.getPosition(),anObj.getOrientation()));

	// System.out.println("-> the orientation of the robot is "
	// +Math.atan2(controler.getSelf().getOrientation()[0],
	// controler.getSelf().getOrientation()[8]));
	// System.out.println(" the position of the robot is "
	// +Math.round(controler.getSelf().getPosition()[0]*100)+";"+Math.round(controler.getSelf().getPosition()[2]*100));

	// System.out.println(" front distance:
	// "+controler.frontDistanceSensor.getValue());

	// CameraRecognitionObject[] backObjs =
	// controler.backCamera.getRecognitionObjects();
	// if (backObjs.length > 0) {
	// CameraRecognitionObject obj = backObjs[0];
	// int oid = obj.getId();
	// // Node obj2 = controler.getFromId(oid);
	// double[] backObjPos = obj.getPosition();
	// /**
	// * The position and orientation are expressed relatively to the camera (the
	// relative position is the one of the center of the object which can differ
	// from its origin) and the units are meter and radian.
	// */
	// System.out.println(" I saw an object on back Camera at :
	// "+backObjPos[0]+","+backObjPos[1]);
	// }
	// CameraRecognitionObject[] frontObjs =
	// controler.frontCamera.getRecognitionObjects();
	// if (frontObjs.length > 0) {
	// for(CameraRecognitionObject obj : frontObjs) {
	// double[] frontObjPos = obj.getPosition();
	// System.out.println(" I saw "+obj.getModel()+" on front Camera at :
	// "+((double)Math.round(frontObjPos[1]*1000))/10+";
	// "+Math.round(frontObjPos[0]*180/Math.PI));
	// }
	// }
	// System.out.println(" gripper distance sensor is
	// "+controler.getObjectDistanceToGripper());
	// if (controler.isThereVirtualwall()) {
	// System.out.println("Virtual wall detected\n");
	// controler.turn(Math.PI);
	// } else if (controler.isThereCollisionAtLeft() ||
	// controler.frontLeftDistanceSensor.getValue() < 250) {
	// System.out.println(" Left obstacle detected\n");
	// controler.goBackward();
	// controler.passiveWait(0.5);
	// controler.turn(Math.PI * controler.randdouble()+0.6);
	// } else if (controler.isThereCollisionAtRight()||
	// controler.frontRightDistanceSensor.getValue() < 250 ||
	// controler.frontDistanceSensor.getValue() < 250) {
	// System.out.println(" Right obstacle detected\n");
	// controler.goBackward();
	// controler.passiveWait(0.5);
	// controler.turn(-Math.PI * controler.randdouble()+0.6);
	// } else {
	// controler.goForward();
	// }
	// controler.flushIRReceiver();

	// }

	// }catch (Exception e) {
	// controler.delete();
	// }

	// }

	@Override
	protected void finalize() {
		this.delete();
		super.finalize();
	}

}
