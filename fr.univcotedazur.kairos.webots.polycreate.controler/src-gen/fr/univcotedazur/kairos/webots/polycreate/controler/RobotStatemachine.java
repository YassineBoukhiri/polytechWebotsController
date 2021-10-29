/** Generated by YAKINDU Statechart Tools code generator. */
package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.IStatemachine;
import com.yakindu.core.ITimed;
import com.yakindu.core.ITimerService;
import com.yakindu.core.rx.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RobotStatemachine implements IStatemachine, ITimed {
	public enum State {
		MAIN_REGION_MOVING,
		MAIN_REGION_RIGHT_BLOCKED,
		MAIN_REGION_BLOCKED,
		MAIN_REGION_LEFT_BLOCKED,
		$NULLSTATE$
	};
	
	private final State[] stateVector = new State[1];
	
	private ITimerService timerService;
	
	private final boolean[] timeEvents = new boolean[1];
	
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
		
		clearInEvents();
		
		setAngle(0.0);
		
		setDistance(0.0);
		
		isExecuting = false;
	}
	
	public synchronized void enter() {
		if (timerService == null) {
			throw new IllegalStateException("Timer service must be set.");
		}
		
		
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
		back = false;
		lTS = false;
		rTS = false;
		clear = false;
		timeEvents[0] = false;
	}
	
	private void microStep() {
		switch (stateVector[0]) {
		case MAIN_REGION_MOVING:
			main_region_Moving_react(-1);
			break;
		case MAIN_REGION_RIGHT_BLOCKED:
			main_region_RIGHT_BLOCKED_react(-1);
			break;
		case MAIN_REGION_BLOCKED:
			main_region_BLOCKED_react(-1);
			break;
		case MAIN_REGION_LEFT_BLOCKED:
			main_region_LEFT_BLOCKED_react(-1);
			break;
		default:
			break;
		}
	}
	
	private void runCycle() {
		if (timerService == null) {
			throw new IllegalStateException("Timer service must be set.");
		}
		
		
		if (getIsExecuting()) {
			return;
		}
		isExecuting = true;
		
		nextEvent();
		do { 
			microStep();
			
			clearInEvents();
			
			nextEvent();
		} while ((((((((front || frontL) || frontR) || back) || lTS) || rTS) || clear) || timeEvents[0]));
		
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
		case MAIN_REGION_RIGHT_BLOCKED:
			return stateVector[0] == State.MAIN_REGION_RIGHT_BLOCKED;
		case MAIN_REGION_BLOCKED:
			return stateVector[0] == State.MAIN_REGION_BLOCKED;
		case MAIN_REGION_LEFT_BLOCKED:
			return stateVector[0] == State.MAIN_REGION_LEFT_BLOCKED;
		default:
			return false;
		}
	}
	
	public synchronized void setTimerService(ITimerService timerService) {
		this.timerService = timerService;
	}
	
	public ITimerService getTimerService() {
		return timerService;
	}
	
	public synchronized void raiseTimeEvent(int eventID) {
		inEventQueue.add(() -> {
			timeEvents[eventID] = true;
		});
		runCycle();
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
	
	/* Entry action for state 'Moving'. */
	private void entryAction_main_region_Moving() {
		timerService.setTimer(this, 0, 100, true);
	}
	
	/* Entry action for state 'RIGHT_BLOCKED'. */
	private void entryAction_main_region_RIGHT_BLOCKED() {
		raiseDoGoBackward();
		
		raiseDoTurnRandomlyLeft();
	}
	
	/* Entry action for state 'BLOCKED'. */
	private void entryAction_main_region_BLOCKED() {
		raiseDoFullTurn();
	}
	
	/* Entry action for state 'LEFT_BLOCKED'. */
	private void entryAction_main_region_LEFT_BLOCKED() {
		raiseDoGoBackward();
		
		raiseDoTurnRandomlyRight();
	}
	
	/* Exit action for state 'Moving'. */
	private void exitAction_main_region_Moving() {
		timerService.unsetTimer(this, 0);
	}
	
	/* 'default' enter sequence for state Moving */
	private void enterSequence_main_region_Moving_default() {
		entryAction_main_region_Moving();
		stateVector[0] = State.MAIN_REGION_MOVING;
	}
	
	/* 'default' enter sequence for state RIGHT_BLOCKED */
	private void enterSequence_main_region_RIGHT_BLOCKED_default() {
		entryAction_main_region_RIGHT_BLOCKED();
		stateVector[0] = State.MAIN_REGION_RIGHT_BLOCKED;
	}
	
	/* 'default' enter sequence for state BLOCKED */
	private void enterSequence_main_region_BLOCKED_default() {
		entryAction_main_region_BLOCKED();
		stateVector[0] = State.MAIN_REGION_BLOCKED;
	}
	
	/* 'default' enter sequence for state LEFT_BLOCKED */
	private void enterSequence_main_region_LEFT_BLOCKED_default() {
		entryAction_main_region_LEFT_BLOCKED();
		stateVector[0] = State.MAIN_REGION_LEFT_BLOCKED;
	}
	
	/* 'default' enter sequence for region main region */
	private void enterSequence_main_region_default() {
		react_main_region__entry_Default();
	}
	
	/* Default exit sequence for state Moving */
	private void exitSequence_main_region_Moving() {
		stateVector[0] = State.$NULLSTATE$;
		
		exitAction_main_region_Moving();
	}
	
	/* Default exit sequence for state RIGHT_BLOCKED */
	private void exitSequence_main_region_RIGHT_BLOCKED() {
		stateVector[0] = State.$NULLSTATE$;
	}
	
	/* Default exit sequence for state BLOCKED */
	private void exitSequence_main_region_BLOCKED() {
		stateVector[0] = State.$NULLSTATE$;
	}
	
	/* Default exit sequence for state LEFT_BLOCKED */
	private void exitSequence_main_region_LEFT_BLOCKED() {
		stateVector[0] = State.$NULLSTATE$;
	}
	
	/* Default exit sequence for region main region */
	private void exitSequence_main_region() {
		switch (stateVector[0]) {
		case MAIN_REGION_MOVING:
			exitSequence_main_region_Moving();
			break;
		case MAIN_REGION_RIGHT_BLOCKED:
			exitSequence_main_region_RIGHT_BLOCKED();
			break;
		case MAIN_REGION_BLOCKED:
			exitSequence_main_region_BLOCKED();
			break;
		case MAIN_REGION_LEFT_BLOCKED:
			exitSequence_main_region_LEFT_BLOCKED();
			break;
		default:
			break;
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region__entry_Default() {
		enterSequence_main_region_Moving_default();
	}
	
	private long react(long transitioned_before) {
		return transitioned_before;
	}
	
	private long main_region_Moving_react(long transitioned_before) {
		long transitioned_after = transitioned_before;
		
		if (transitioned_after<0) {
			if (frontR) {
				exitSequence_main_region_Moving();
				enterSequence_main_region_RIGHT_BLOCKED_default();
				react(0);
				
				transitioned_after = 0;
			} else {
				if (front) {
					exitSequence_main_region_Moving();
					enterSequence_main_region_RIGHT_BLOCKED_default();
					react(0);
					
					transitioned_after = 0;
				} else {
					if (frontL) {
						exitSequence_main_region_Moving();
						enterSequence_main_region_LEFT_BLOCKED_default();
						react(0);
						
						transitioned_after = 0;
					}
				}
			}
		}
		/* If no transition was taken then execute local reactions */
		if (transitioned_after==transitioned_before) {
			if (timeEvents[0]) {
				raiseDoGoForward();
			}
			transitioned_after = react(transitioned_before);
		}
		return transitioned_after;
	}
	
	private long main_region_RIGHT_BLOCKED_react(long transitioned_before) {
		long transitioned_after = transitioned_before;
		
		if (transitioned_after<0) {
			if (frontL) {
				exitSequence_main_region_RIGHT_BLOCKED();
				enterSequence_main_region_BLOCKED_default();
				react(0);
				
				transitioned_after = 0;
			} else {
				if (clear==true) {
					exitSequence_main_region_RIGHT_BLOCKED();
					enterSequence_main_region_Moving_default();
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
	
	private long main_region_BLOCKED_react(long transitioned_before) {
		long transitioned_after = transitioned_before;
		
		if (transitioned_after<0) {
			if (clear==true) {
				exitSequence_main_region_BLOCKED();
				enterSequence_main_region_Moving_default();
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
	
	private long main_region_LEFT_BLOCKED_react(long transitioned_before) {
		long transitioned_after = transitioned_before;
		
		if (transitioned_after<0) {
			if (frontR) {
				exitSequence_main_region_LEFT_BLOCKED();
				enterSequence_main_region_BLOCKED_default();
				react(0);
				
				transitioned_after = 0;
			} else {
				if (front) {
					exitSequence_main_region_LEFT_BLOCKED();
					enterSequence_main_region_BLOCKED_default();
					react(0);
					
					transitioned_after = 0;
				} else {
					if (clear==true) {
						exitSequence_main_region_LEFT_BLOCKED();
						enterSequence_main_region_Moving_default();
						react(0);
						
						transitioned_after = 0;
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
	
}
