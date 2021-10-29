package fr.univcotedazur.kairos.webots.polycreate;

import com.yakindu.core.rx.Observer;

public class DoFullTurnObserver implements Observer<Void> {
	
	PolyCreateControler controler;
	
	public DoFullTurnObserver(PolyCreateControler controler) {
		this.controler = controler;
	}

	@Override
	public void next(Void value) {
		controler.doFullTurn();
	}

}
