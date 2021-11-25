/** Generated by YAKINDU Statechart Tools code generator. */
package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.IStatemachine;
import com.yakindu.core.rx.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RobotStatemachine implements IStatemachine {
	public enum State {
		MAIN_REGION_MOVING,
		MAIN_REGION_BLOCKED,
		MAIN_REGION_VIRTUAL_WALL,
		MAIN_REGION_PARTIALY_BLOCKED,
		MAIN_REGION_PARTIALY_BLOCKED_R1_LEFT_BLOCKED,
		MAIN_REGION_PARTIALY_BLOCKED_R1_RIGHT_BLOCKED,
		MAIN_REGION_DO_NOTHING,
		$NULLSTATE$
	};
	
	private State[] historyVector = new State[1];
	private final State[] stateVector = new State[1];
	
	private BlockingQueue<Runnable> inEventQueue = new LinkedBlockingQueue<Runnable>();
	private boolean isExecuting;
	
	protected boolean getIsExecuting() {
		synchronized(RobotStatemachine.this) {
			return isExecuting;
		}
	}
	
	protected void setIsExecuting(boolean value) {
		synchronized(RobotStatemachine.this) {
			this.isExecuting = value;
		}
	}
	public RobotStatemachine() {
		for (int i = 0; i < 1; i++) {
			stateVector[i] = State.$NULLSTATE$;
		}
		for (int i = 0; i < 1; i++) {
			historyVector[i] = State.$NULLSTATE$;
		}
		
		clearInEvents();
		
		setAngle(0.0);
		
		setDistance(0.0);
		
		isExecuting = false;
	}
	
	public synchronized void enter() {
		
		
		if (getIsExecuting()) {
			return;
		}
		isExecuting = true;
		
		enterSequence_main_region_default();
		isExecuting = false;
	}
	
	public synchronized void exit() {
		if (getIsExecuting()) {
			return;
		}
		isExecuting = true;
		
		exitSequence_main_region();
		isExecuting = false;
	}
	
	/**
	 * @see IStatemachine#isActive()
	 */
	public synchronized boolean isActive() {
		return stateVector[0] != State.$NULLSTATE$;
	}
	
	/** 
	* Always returns 'false' since this state machine can never become final.
	*
	* @see IStatemachine#isFinal()
	*/
	public synchronized boolean isFinal() {
		return false;
	}
	private void clearInEvents() {
		front = false;
		frontL = false;
		frontR = false;
		virtualWall = false;
		back = false;
		lTS = false;
		rTS = false;
		clear = false;
	}
	
	private void microStep() {
		switch (stateVector[0]) {
		case MAIN_REGION_MOVING:
			main_region_MOVING_react(-1);
			break;
		case MAIN_REGION_BLOCKED:
			main_region_BLOCKED_react(-1);
			break;
		case MAIN_REGION_VIRTUAL_WALL:
			main_region_VIRTUAL_WALL_react(-1);
			break;
		case MAIN_REGION_PARTIALY_BLOCKED_R1_LEFT_BLOCKED:
			main_region_PARTIALY_BLOCKED_r1_LEFT_BLOCKED_react(-1);
			break;
		case MAIN_REGION_PARTIALY_BLOCKED_R1_RIGHT_BLOCKED:
			main_region_PARTIALY_BLOCKED_r1_RIGHT_BLOCKED_react(-1);
			break;
		case MAIN_REGION_DO_NOTHING:
			main_region_DO_NOTHING_react(-1);
			break;
		default:
			break;
		}
	}
	
	private void runCycle() {
		
		
		if (getIsExecuting()) {
			return;
		}
		isExecuting = true;
		
		nextEvent();
		do { 
			microStep();
			
			clearInEvents();
			
			nextEvent();
		} while ((((((((front || frontL) || frontR) || virtualWall) || back) || lTS) || rTS) || clear));
		
		isExecuting = false;
	}
	
	protected void nextEvent() {
		if(!inEventQueue.isEmpty()) {
			inEventQueue.poll().run();
			return;
		}
	}
	/**
	* Returns true if the given state is currently active otherwise false.
	*/
	public synchronized boolean isStateActive(State state) {
	
		switch (state) {
		case MAIN_REGION_MOVING:
			return stateVector[0] == State.MAIN_REGION_MOVING;
		case MAIN_REGION_BLOCKED:
			return stateVector[0] == State.MAIN_REGION_BLOCKED;
		case MAIN_REGION_VIRTUAL_WALL:
			return stateVector[0] == State.MAIN_REGION_VIRTUAL_WALL;
		case MAIN_REGION_PARTIALY_BLOCKED:
			return stateVector[0].ordinal() >= State.
					MAIN_REGION_PARTIALY_BLOCKED.ordinal()&& stateVector[0].ordinal() <= State.MAIN_REGION_PARTIALY_BLOCKED_R1_RIGHT_BLOCKED.ordinal();
		case MAIN_REGION_PARTIALY_BLOCKED_R1_LEFT_BLOCKED:
			return stateVector[0] == State.MAIN_REGION_PARTIALY_BLOCKED_R1_LEFT_BLOCKED;
		case MAIN_REGION_PARTIALY_BLOCKED_R1_RIGHT_BLOCKED:
			return stateVector[0] == State.MAIN_REGION_PARTIALY_BLOCKED_R1_RIGHT_BLOCKED;
		case MAIN_REGION_DO_NOTHING:
			return stateVector[0] == State.MAIN_REGION_DO_NOTHING;
		default:
			return false;
		}
	}
	
	
	private boolean front;
	
	
	public void raiseFront() {
		synchronized(RobotStatemachine.this) {
			inEventQueue.add(() -> {
				front = true;
			});
			runCycle();
		}
	}
	
	private boolean frontL;
	
	
	public void raiseFrontL() {
		synchronized(RobotStatemachine.this) {
			inEventQueue.add(() -> {
				frontL = true;
			});
			runCycle();
		}
	}
	
	private boolean frontR;
	
	
	public void raiseFrontR() {
		synchronized(RobotStatemachine.this) {
			inEventQueue.add(() -> {
				frontR = true;
			});
			runCycle();
		}
	}
	
	private boolean virtualWall;
	
	
	public void raiseVirtualWall() {
		synchronized(RobotStatemachine.this) {
			inEventQueue.add(() -> {
				virtualWall = true;
			});
			runCycle();
		}
	}
	
	private boolean back;
	
	
	public void raiseBack() {
		synchronized(RobotStatemachine.this) {
			inEventQueue.add(() -> {
				back = true;
			});
			runCycle();
		}
	}
	
	private boolean lTS;
	
	
	public void raiseLTS() {
		synchronized(RobotStatemachine.this) {
			inEventQueue.add(() -> {
				lTS = true;
			});
			runCycle();
		}
	}
	
	private boolean rTS;
	
	
	public void raiseRTS() {
		synchronized(RobotStatemachine.this) {
			inEventQueue.add(() -> {
				rTS = true;
			});
			runCycle();
		}
	}
	
	private boolean clear;
	
	
	public void raiseClear() {
		synchronized(RobotStatemachine.this) {
			inEventQueue.add(() -> {
				clear = true;
			});
			runCycle();
		}
	}
	
	private boolean doFullTurn;
	
	
	protected void raiseDoFullTurn() {
		synchronized(RobotStatemachine.this) {
			doFullTurn = true;
			doFullTurnObservable.next(null);
		}
	}
	
	private Observable<Void> doFullTurnObservable = new Observable<Void>();
	
	public Observable<Void> getDoFullTurn() {
		return doFullTurnObservable;
	}
	
	private boolean doTurnRandomlyRight;
	
	
	protected void raiseDoTurnRandomlyRight() {
		synchronized(RobotStatemachine.this) {
			doTurnRandomlyRight = true;
			doTurnRandomlyRightObservable.next(null);
		}
	}
	
	private Observable<Void> doTurnRandomlyRightObservable = new Observable<Void>();
	
	public Observable<Void> getDoTurnRandomlyRight() {
		return doTurnRandomlyRightObservable;
	}
	
	private boolean doTurnRandomlyLeft;
	
	
	protected void raiseDoTurnRandomlyLeft() {
		synchronized(RobotStatemachine.this) {
			doTurnRandomlyLeft = true;
			doTurnRandomlyLeftObservable.next(null);
		}
	}
	
	private Observable<Void> doTurnRandomlyLeftObservable = new Observable<Void>();
	
	public Observable<Void> getDoTurnRandomlyLeft() {
		return doTurnRandomlyLeftObservable;
	}
	
	private boolean doGoForward;
	
	
	protected void raiseDoGoForward() {
		synchronized(RobotStatemachine.this) {
			doGoForward = true;
			doGoForwardObservable.next(null);
		}
	}
	
	private Observable<Void> doGoForwardObservable = new Observable<Void>();
	
	public Observable<Void> getDoGoForward() {
		return doGoForwardObservable;
	}
	
	private boolean doGoBackward;
	
	
	protected void raiseDoGoBackward() {
		synchronized(RobotStatemachine.this) {
			doGoBackward = true;
			doGoBackwardObservable.next(null);
		}
	}
	
	private Observable<Void> doGoBackwardObservable = new Observable<Void>();
	
	public Observable<Void> getDoGoBackward() {
		return doGoBackwardObservable;
	}
	
	private boolean doOpenGripper;
	
	
	protected void raiseDoOpenGripper() {
		synchronized(RobotStatemachine.this) {
			doOpenGripper = true;
			doOpenGripperObservable.next(null);
		}
	}
	
	private Observable<Void> doOpenGripperObservable = new Observable<Void>();
	
	public Observable<Void> getDoOpenGripper() {
		return doOpenGripperObservable;
	}
	
	private boolean doCloseGripper;
	
	
	protected void raiseDoCloseGripper() {
		synchronized(RobotStatemachine.this) {
			doCloseGripper = true;
			doCloseGripperObservable.next(null);
		}
	}
	
	private Observable<Void> doCloseGripperObservable = new Observable<Void>();
	
	public Observable<Void> getDoCloseGripper() {
		return doCloseGripperObservable;
	}
	
	private double angle;
	
	public synchronized double getAngle() {
		synchronized(RobotStatemachine.this) {
			return angle;
		}
	}
	
	public void setAngle(double value) {
		synchronized(RobotStatemachine.this) {
			this.angle = value;
		}
	}
	
	private double distance;
	
	public synchronized double getDistance() {
		synchronized(RobotStatemachine.this) {
			return distance;
		}
	}
	
	public void setDistance(double value) {
		synchronized(RobotStatemachine.this) {
			this.distance = value;
		}
	}
	
	public static final double pI = 3.14;
	
	public synchronized double getPI() {
		synchronized(RobotStatemachine.this) {
			return pI;
		}
	}
	
	/* Entry action for state 'MOVING'. */
	private void entryAction_main_region_MOVING() {
		raiseDoGoForward();
	}
	
	/* Entry action for state 'BLOCKED'. */
	private void entryAction_main_region_BLOCKED() {
		raiseDoFullTurn();
	}
	
	/* Entry action for state 'VIRTUAL_WALL'. */
	private void entryAction_main_region_VIRTUAL_WALL() {
		raiseDoFullTurn();
	}
	
	/* Entry action for state 'LEFT_BLOCKED'. */
	private void entryAction_main_region_PARTIALY_BLOCKED_r1_LEFT_BLOCKED() {
		raiseDoGoBackward();
		
		raiseDoTurnRandomlyRight();
	}
	
	/* Entry action for state 'RIGHT_BLOCKED'. */
	private void entryAction_main_region_PARTIALY_BLOCKED_r1_RIGHT_BLOCKED() {
		raiseDoGoBackward();
		
		raiseDoTurnRandomlyLeft();
	}
	
	/* 'default' enter sequence for state MOVING */
	private void enterSequence_main_region_MOVING_default() {
		entryAction_main_region_MOVING();
		stateVector[0] = State.MAIN_REGION_MOVING;
	}
	
	/* 'default' enter sequence for state BLOCKED */
	private void enterSequence_main_region_BLOCKED_default() {
		entryAction_main_region_BLOCKED();
		stateVector[0] = State.MAIN_REGION_BLOCKED;
	}
	
	/* 'default' enter sequence for state VIRTUAL_WALL */
	private void enterSequence_main_region_VIRTUAL_WALL_default() {
		entryAction_main_region_VIRTUAL_WALL();
		stateVector[0] = State.MAIN_REGION_VIRTUAL_WALL;
	}
	
	/* 'default' enter sequence for state PARTIALY_BLOCKED */
	private void enterSequence_main_region_PARTIALY_BLOCKED_default() {
		enterSequence_main_region_PARTIALY_BLOCKED_r1_default();
	}
	
	/* 'default' enter sequence for state LEFT_BLOCKED */
	private void enterSequence_main_region_PARTIALY_BLOCKED_r1_LEFT_BLOCKED_default() {
		entryAction_main_region_PARTIALY_BLOCKED_r1_LEFT_BLOCKED();
		stateVector[0] = State.MAIN_REGION_PARTIALY_BLOCKED_R1_LEFT_BLOCKED;
		
		historyVector[0] = stateVector[0];
	}
	
	/* 'default' enter sequence for state RIGHT_BLOCKED */
	private void enterSequence_main_region_PARTIALY_BLOCKED_r1_RIGHT_BLOCKED_default() {
		entryAction_main_region_PARTIALY_BLOCKED_r1_RIGHT_BLOCKED();
		stateVector[0] = State.MAIN_REGION_PARTIALY_BLOCKED_R1_RIGHT_BLOCKED;
		
		historyVector[0] = stateVector[0];
	}
	
	/* 'default' enter sequence for state DO_NOTHING */
	private void enterSequence_main_region_DO_NOTHING_default() {
		stateVector[0] = State.MAIN_REGION_DO_NOTHING;
	}
	
	/* 'default' enter sequence for region main region */
	private void enterSequence_main_region_default() {
		react_main_region__entry_Default();
	}
	
	/* 'default' enter sequence for region r1 */
	private void enterSequence_main_region_PARTIALY_BLOCKED_r1_default() {
		react_main_region_PARTIALY_BLOCKED_r1__entry_Default();
	}
	
	/* deep enterSequence with history in child r1 */
	private void deepEnterSequence_main_region_PARTIALY_BLOCKED_r1() {
		switch (historyVector[0]) {
		case MAIN_REGION_PARTIALY_BLOCKED_R1_LEFT_BLOCKED:
			enterSequence_main_region_PARTIALY_BLOCKED_r1_LEFT_BLOCKED_default();
			break;
		case MAIN_REGION_PARTIALY_BLOCKED_R1_RIGHT_BLOCKED:
			enterSequence_main_region_PARTIALY_BLOCKED_r1_RIGHT_BLOCKED_default();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for state MOVING */
	private void exitSequence_main_region_MOVING() {
		stateVector[0] = State.$NULLSTATE$;
	}
	
	/* Default exit sequence for state BLOCKED */
	private void exitSequence_main_region_BLOCKED() {
		stateVector[0] = State.$NULLSTATE$;
	}
	
	/* Default exit sequence for state VIRTUAL_WALL */
	private void exitSequence_main_region_VIRTUAL_WALL() {
		stateVector[0] = State.$NULLSTATE$;
	}
	
	/* Default exit sequence for state PARTIALY_BLOCKED */
	private void exitSequence_main_region_PARTIALY_BLOCKED() {
		exitSequence_main_region_PARTIALY_BLOCKED_r1();
	}
	
	/* Default exit sequence for state LEFT_BLOCKED */
	private void exitSequence_main_region_PARTIALY_BLOCKED_r1_LEFT_BLOCKED() {
		stateVector[0] = State.$NULLSTATE$;
	}
	
	/* Default exit sequence for state RIGHT_BLOCKED */
	private void exitSequence_main_region_PARTIALY_BLOCKED_r1_RIGHT_BLOCKED() {
		stateVector[0] = State.$NULLSTATE$;
	}
	
	/* Default exit sequence for state DO_NOTHING */
	private void exitSequence_main_region_DO_NOTHING() {
		stateVector[0] = State.$NULLSTATE$;
	}
	
	/* Default exit sequence for region main region */
	private void exitSequence_main_region() {
		switch (stateVector[0]) {
		case MAIN_REGION_MOVING:
			exitSequence_main_region_MOVING();
			break;
		case MAIN_REGION_BLOCKED:
			exitSequence_main_region_BLOCKED();
			break;
		case MAIN_REGION_VIRTUAL_WALL:
			exitSequence_main_region_VIRTUAL_WALL();
			break;
		case MAIN_REGION_PARTIALY_BLOCKED_R1_LEFT_BLOCKED:
			exitSequence_main_region_PARTIALY_BLOCKED_r1_LEFT_BLOCKED();
			break;
		case MAIN_REGION_PARTIALY_BLOCKED_R1_RIGHT_BLOCKED:
			exitSequence_main_region_PARTIALY_BLOCKED_r1_RIGHT_BLOCKED();
			break;
		case MAIN_REGION_DO_NOTHING:
			exitSequence_main_region_DO_NOTHING();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region r1 */
	private void exitSequence_main_region_PARTIALY_BLOCKED_r1() {
		switch (stateVector[0]) {
		case MAIN_REGION_PARTIALY_BLOCKED_R1_LEFT_BLOCKED:
			exitSequence_main_region_PARTIALY_BLOCKED_r1_LEFT_BLOCKED();
			break;
		case MAIN_REGION_PARTIALY_BLOCKED_R1_RIGHT_BLOCKED:
			exitSequence_main_region_PARTIALY_BLOCKED_r1_RIGHT_BLOCKED();
			break;
		default:
			break;
		}
	}
	
	/* Default react sequence for deep history entry  */
	private void react_main_region_PARTIALY_BLOCKED_r1__entry_Default() {
		/* Enter the region with deep history */
		if (historyVector[0] != State.$NULLSTATE$) {
			deepEnterSequence_main_region_PARTIALY_BLOCKED_r1();
		} else {
			enterSequence_main_region_PARTIALY_BLOCKED_r1_LEFT_BLOCKED_default();
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region__entry_Default() {
		enterSequence_main_region_DO_NOTHING_default();
	}
	
	private long react(long transitioned_before) {
		return transitioned_before;
	}
	
	private long main_region_MOVING_react(long transitioned_before) {
		long transitioned_after = transitioned_before;
		
		if (transitioned_after<0) {
			if (virtualWall) {
				exitSequence_main_region_MOVING();
				enterSequence_main_region_VIRTUAL_WALL_default();
				react(0);
				
				transitioned_after = 0;
			} else {
				if (clear) {
					exitSequence_main_region_MOVING();
					enterSequence_main_region_MOVING_default();
					react(0);
					
					transitioned_after = 0;
				} else {
					if (frontL) {
						exitSequence_main_region_MOVING();
						enterSequence_main_region_PARTIALY_BLOCKED_r1_LEFT_BLOCKED_default();
						react(0);
						
						transitioned_after = 0;
					} else {
						if (frontR) {
							exitSequence_main_region_MOVING();
							enterSequence_main_region_PARTIALY_BLOCKED_r1_RIGHT_BLOCKED_default();
							react(0);
							
							transitioned_after = 0;
						} else {
							if (front) {
								exitSequence_main_region_MOVING();
								enterSequence_main_region_PARTIALY_BLOCKED_r1_RIGHT_BLOCKED_default();
								react(0);
								
								transitioned_after = 0;
							}
						}
					}
				}
			}
		}
		/* If no transition was taken then execute local reactions */
		if (transitioned_after==transitioned_before) {
			transitioned_after = react(transitioned_before);
		}
		return transitioned_after;
	}
	
	private long main_region_BLOCKED_react(long transitioned_before) {
		long transitioned_after = transitioned_before;
		
		if (transitioned_after<0) {
			if (clear) {
				exitSequence_main_region_BLOCKED();
				enterSequence_main_region_MOVING_default();
				react(0);
				
				transitioned_after = 0;
			}
		}
		/* If no transition was taken then execute local reactions */
		if (transitioned_after==transitioned_before) {
			transitioned_after = react(transitioned_before);
		}
		return transitioned_after;
	}
	
	private long main_region_VIRTUAL_WALL_react(long transitioned_before) {
		long transitioned_after = transitioned_before;
		
		if (transitioned_after<0) {
			if (clear) {
				exitSequence_main_region_VIRTUAL_WALL();
				enterSequence_main_region_MOVING_default();
				react(0);
				
				transitioned_after = 0;
			} else {
				if (virtualWall) {
					exitSequence_main_region_VIRTUAL_WALL();
					enterSequence_main_region_VIRTUAL_WALL_default();
					react(0);
					
					transitioned_after = 0;
				}
			}
		}
		/* If no transition was taken then execute local reactions */
		if (transitioned_after==transitioned_before) {
			transitioned_after = react(transitioned_before);
		}
		return transitioned_after;
	}
	
	private long main_region_PARTIALY_BLOCKED_react(long transitioned_before) {
		long transitioned_after = transitioned_before;
		
		if (transitioned_after<0) {
			if (clear) {
				exitSequence_main_region_PARTIALY_BLOCKED();
				enterSequence_main_region_MOVING_default();
				react(0);
				
				transitioned_after = 0;
			} else {
				if (frontR) {
					exitSequence_main_region_PARTIALY_BLOCKED();
					enterSequence_main_region_PARTIALY_BLOCKED_default();
					react(0);
					
					transitioned_after = 0;
				} else {
					if (frontL) {
						exitSequence_main_region_PARTIALY_BLOCKED();
						enterSequence_main_region_PARTIALY_BLOCKED_default();
						react(0);
						
						transitioned_after = 0;
					} else {
						if (front) {
							exitSequence_main_region_PARTIALY_BLOCKED();
							enterSequence_main_region_PARTIALY_BLOCKED_default();
							react(0);
							
							transitioned_after = 0;
						}
					}
				}
			}
		}
		/* If no transition was taken then execute local reactions */
		if (transitioned_after==transitioned_before) {
			transitioned_after = react(transitioned_before);
		}
		return transitioned_after;
	}
	
	private long main_region_PARTIALY_BLOCKED_r1_LEFT_BLOCKED_react(long transitioned_before) {
		long transitioned_after = transitioned_before;
		
		if (transitioned_after<0) {
			if (frontR) {
				exitSequence_main_region_PARTIALY_BLOCKED();
				enterSequence_main_region_BLOCKED_default();
				react(0);
				
				transitioned_after = 0;
			} else {
				if (front) {
					exitSequence_main_region_PARTIALY_BLOCKED();
					enterSequence_main_region_BLOCKED_default();
					react(0);
					
					transitioned_after = 0;
				}
			}
		}
		/* If no transition was taken then execute local reactions */
		if (transitioned_after==transitioned_before) {
			transitioned_after = main_region_PARTIALY_BLOCKED_react(transitioned_before);
		}
		return transitioned_after;
	}
	
	private long main_region_PARTIALY_BLOCKED_r1_RIGHT_BLOCKED_react(long transitioned_before) {
		long transitioned_after = transitioned_before;
		
		if (transitioned_after<0) {
			if (frontL) {
				exitSequence_main_region_PARTIALY_BLOCKED();
				enterSequence_main_region_BLOCKED_default();
				react(0);
				
				transitioned_after = 0;
			}
		}
		/* If no transition was taken then execute local reactions */
		if (transitioned_after==transitioned_before) {
			transitioned_after = main_region_PARTIALY_BLOCKED_react(transitioned_before);
		}
		return transitioned_after;
	}
	
	private long main_region_DO_NOTHING_react(long transitioned_before) {
		long transitioned_after = transitioned_before;
		
		if (transitioned_after<0) {
		}
		/* If no transition was taken then execute local reactions */
		if (transitioned_after==transitioned_before) {
			transitioned_after = react(transitioned_before);
		}
		return transitioned_after;
	}
	
}
