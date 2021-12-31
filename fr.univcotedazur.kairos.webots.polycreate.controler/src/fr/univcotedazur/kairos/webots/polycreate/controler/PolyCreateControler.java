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

package fr.univcotedazur.kairos.webots.polycreate.controler;

import java.util.Random;

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
import com.cyberbotics.webots.controller.Supervisor;
import com.cyberbotics.webots.controller.TouchSensor;


public class PolyCreateControler extends Supervisor {

	static int MAX_SPEED = 16;
	static int NULL_SPEED = 0;
	static int HALF_SPEED = 8;
	static int MIN_SPEED = -16;

	static double WHEEL_RADIUS = 0.031;
	static double AXLE_LENGTH = 0.271756;
	static double ENCODER_RESOLUTION = 507.9188;
	static double turnPrecision= 0.05;

	static int EDGE = 2;

	/**
	 * the inkEvaporation parameter in the WorldInfo element of the robot scene may be interesting to access
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

	public 	int timestep = Integer.MAX_VALUE;
	public 	Random random = new Random();

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
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				System.out.println("Shutdown hook ran!");
				ctrl.delete();
			}
		});
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
	 * give the obstacle distance from the gripper sensor. max distance (i.e., no obstacle detected) is 1500
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

	public double randdouble() {
		return  random.nextDouble();
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


	/**
	 * The values are returned as a 3D-vector, therefore only the indices 0, 1, and 2 are valid for accessing the vector.
	 * The returned vector indicates the absolute position of the GPS device. This position can either be expressed in the
	 * cartesian coordinate system of Webots or using latitude-longitude-altitude, depending on the value of the
	 * gpsCoordinateSystem field of the WorldInfo node. The gpsReference field of the WorldInfo node can be used to define
	 * the reference point of the GPS. The wb_gps_get_speed function returns the current GPS speed in meters per second.
	 * @return
	 */
	public double[] getPosition() {
		return gps.getValues();
	}
	
	
	/* turnings */ 
	
	public void turnLeft() {
	    System.out.println("Turning left");
	    turn(Math.PI/2 );
	    
	}

	public void turnRight() {
	    System.out.println("Turning right");
	    turn(-Math.PI/2 );
	    passiveWait(0.5);
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
	    long t= System.currentTimeMillis();
	    long end = t+ 1000 * length;
	    System.out.println("Drawing a line");
	    
	    while(System.currentTimeMillis() < end) {
	        goForward();
	        passiveWait(0.5);
	        System.out.println("Drawing a line in process");
	    }
	    stop();
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
	    // going to bottom right corner
	    doFullTurn();
	    goLine(EDGE);
	}
	
	/* Main */


	public static void main(String[] args) {
		PolyCreateControler controler = new PolyCreateControler();
		controler.passiveWait(0.5);
		controler.goLine(1);
		controler.passiveWait(0.5);
		controler.turnRight();
		controler.passiveWait(0.5);
		controler.turnRight();
	}


	@Override
	protected void finalize() {
		this.delete();
		super.finalize();
	}

}
