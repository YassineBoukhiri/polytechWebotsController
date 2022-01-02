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

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

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

	static int EDGE = 1; 
	
	static double WHEEL_RADIUS = 0.031;
	static double AXLE_LENGTH = 0.271756;
	static double ENCODER_RESOLUTION = 507.9188;
	static double turnPrecision= 0.025;
	
	static int choice = -1;
	static String digits = "";
 
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
		fsm.getDoGetUserChoice().subscribe(new DoGetUserChoiceObserver(this));
		fsm.getDoAskForDigits().subscribe(new DoAskForDigitsObserver(this));
		fsm.getDoDrawDigits().subscribe(new DoDrawDigitsObserver(this));
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
		if (targetOrientation < 0) targetOrientation += 2*Math.PI;
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
	
	
	public void doGetUserChoice() {
		System.out.println("Please choose a feature from the following :");
		System.out.println("1 : Cleaning and putting away objects");
		System.out.println("2 : Cleaning without putting away objects");
		System.out.println("3 : Drawing digits on the floor");
		Scanner sc = new Scanner(System.in);
		choice = sc.nextInt();
		while(choice != 1 && choice != 2 && choice != 3) {
			System.out.println("Please enter a valid choice : ");
			choice = sc.nextInt();
		}
		fsm.setUserChoice(choice);
		fsm.setGrabActivated(choice==1);
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
		
	}
	
	
	public void doAskForDigits() {
		
		System.out.println("Please enter the digits : ");
		Scanner sc = new Scanner(System.in);
		digits = sc.nextLine();  
		while(digits.length()<=0 || digits.length()>4) {
			System.out.println("WARNING : The number of the digits must be between 1 and 4");
			System.out.println("Please enter the digits : ");
			digits = sc.nextLine();  
		}
		fsm.raiseDraw();
	}
	
	public void doDrawDigits() {
		for(int i=0; i<digits.length();i++) {
			draw(digits.charAt(i));
			draw(' ');
		}
		fsm.raiseGoMainMenu();
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
	
	
	/* turnings */ 
    
    public void turnLeft() {
        System.out.println("Turning left");
        turn(Math.PI/2 );
        passiveWait(0.5);
    }

    public void turnRight() {
        System.out.println("Turning right");
        turn(-Math.PI/2 );
        passiveWait(0.5);
    }
     

    /* draw feature */
    
    public void goLine(int length) {
        long t= System.currentTimeMillis();
        long end = t+ 1000 * length;
        System.out.println("Going without drawing");
        
        while(System.currentTimeMillis() < end) {
            goForward();
            passiveWait(0.5);
            System.out.println("Going in process");
        }
        stop();
        passiveWait(0.5);
    }
        

    public void drawLine(int length) {
    	pen.write(true);
        long t= System.currentTimeMillis();
        long end = t+ 1000 * length;
        System.out.println("Drawing a line");
        
        while(System.currentTimeMillis() < end) {
            goForward();
            passiveWait(0.5);
            System.out.println("Drawing a line in process");
        }
        stop();
        pen.write(false);
        passiveWait(0.5);
    }

    public void write0() {
        drawLine(2*EDGE);
        turnRight();
        drawLine(EDGE);
        turnRight();
        drawLine(2*EDGE);
        turnRight();
        drawLine(EDGE);
        doFullTurn();
        goLine(EDGE);
    }
    
    public void write1() {
    	turnRight();
    	goLine(EDGE);
    	turnLeft();
    	drawLine(2*EDGE);
    	doFullTurn();
    	goLine(2*EDGE);
    	turnLeft();
    }
	
    public void write2() {
    	goLine(2*EDGE);
    	turnRight();
    	drawLine(EDGE);
    	turnRight();
    	drawLine(EDGE);
    	turnRight();
    	drawLine(EDGE);
    	turnLeft();
    	drawLine(EDGE);
    	turnLeft();
    	drawLine(EDGE);
    }
    
	
    public void write3() {
    	goLine(2*EDGE);
    	turnRight();
    	drawLine(EDGE);
    	turnRight();
    	drawLine(EDGE);
    	turnRight();
    	drawLine(EDGE);
    	doFullTurn();
    	goLine(EDGE);
    	turnRight();
    	drawLine(EDGE);
    	turnRight();
    	drawLine(EDGE);
    	doFullTurn();
    	goLine(EDGE);
    }
	
	
	public void write4() {
		goLine(EDGE);
		drawLine(EDGE);
		doFullTurn();
		goLine(EDGE);
		turnLeft();
		drawLine(EDGE);
		turnLeft();
		drawLine(EDGE);
		doFullTurn();
		goLine(EDGE);
		drawLine(EDGE);
	}
	
	public void write5() {
		turnRight();
		drawLine(EDGE);
		turnLeft();
		drawLine(EDGE);
		turnLeft();
		drawLine(EDGE);
		turnRight();
		drawLine(EDGE);
		turnRight();
		drawLine(EDGE);
		turnRight();
		goLine(2*EDGE);
		turnLeft();
	}
	
	public void write6() {
		turnRight();
		drawLine(EDGE);
		turnLeft();
		drawLine(EDGE);
		turnLeft();
		drawLine(EDGE);
		turnRight();
		drawLine(EDGE);
		turnRight();
		drawLine(EDGE);
		doFullTurn();
		goLine(EDGE);
		turnLeft();
		goLine(EDGE);
		drawLine(EDGE);
		turnLeft();
		goLine(EDGE);
	}
	
	
	public void write7() {
		goLine(2*EDGE);
		turnRight();
		drawLine(EDGE);
		turnRight();
		drawLine(2*EDGE);
		turnLeft();
	}
	
	public void write8() {
		drawLine(2*EDGE);
		turnRight();
		drawLine(EDGE);
		turnRight();
		drawLine(EDGE);
		turnRight();
		drawLine(EDGE);
		doFullTurn();
		goLine(EDGE);
		turnRight();
		drawLine(EDGE);
		turnRight();
		drawLine(EDGE);
		doFullTurn();
		goLine(EDGE);	
	}
	
	public void write9() {
		turnRight();
		drawLine(EDGE);
		turnLeft();
		drawLine(2*EDGE);
		turnLeft();
		drawLine(EDGE);
		turnLeft();
		drawLine(EDGE);
		turnLeft();
		drawLine(EDGE);
		turnRight();
		goLine(EDGE);
		turnLeft();
	}
	
	public void writeSpace() {
		goLine(EDGE);
		turnLeft();
	}
	
	public void draw(char digit) {
	    if (digit == '0') write0();
	    if (digit == '1') write1();
	    if (digit == '2') write2();
	    if (digit == '3') write3();
	    if (digit == '4') write4();
	    if (digit == '5') write5();
	    if (digit == '6') write6();
	    if (digit == '7') write7();
	    if (digit == '8') write8();
	    if (digit == '9') write9();
	    if (digit == ' ') writeSpace();
	}
	
	
	
	

	public static void main(String[] args) {
		PolyCreateControler controler = new PolyCreateControler();
		controler.openGripper();
		if (choice != -1) { // When user choose an option
			while(true) {
				controler.passiveWait(0.1);
				controler.listen();
			}
		}
		
	}


	@Override
	protected void finalize() {
		this.delete();
		super.finalize();
	}

}
