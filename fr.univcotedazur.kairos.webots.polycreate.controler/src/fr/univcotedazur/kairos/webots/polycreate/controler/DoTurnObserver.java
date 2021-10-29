package fr.univcotedazur.kairos.webots.polycreate;

import com.yakindu.core.rx.Observer;

public class DoTurnObserver implements Observer<Void> {
	
	PolyCreateControler controler;
	
	public DoTurnObserver(PolyCreateControler controler) {
		this.controler = controler;
	}

	@Override
	public void next(Void value) {
		controler.doTurn();
	}

}
