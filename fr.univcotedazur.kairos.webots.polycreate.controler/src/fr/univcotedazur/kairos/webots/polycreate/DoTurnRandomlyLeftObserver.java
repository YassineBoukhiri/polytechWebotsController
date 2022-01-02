package fr.univcotedazur.kairos.webots.polycreate;

import com.yakindu.core.rx.Observer;

public class DoTurnRandomlyLeftObserver implements Observer<Void> {
	
	PolyCreateControler controler;
	
	public DoTurnRandomlyLeftObserver(PolyCreateControler controler) {
		this.controler = controler;
	}

	@Override
	public void next(Void value) {
		System.out.println("I'M TURNING LEFT");
		controler.doTurnRandomlyLeft();
	}

}
