package fr.univcotedazur.kairos.webots.polycreate;

import com.yakindu.core.rx.Observer;

public class DoTurnRandomlyRightObserver implements Observer<Void> {
	
	PolyCreateControler controler;
	
	public DoTurnRandomlyRightObserver(PolyCreateControler controler) {
		this.controler = controler;
	}

	@Override
	public void next(Void value) {
		controler.doTurnRandomlyRight();
	}

}
