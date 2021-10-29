package fr.univcotedazur.kairos.webots.polycreate;

import com.yakindu.core.rx.Observer;

public class DoGoBackwardObserver implements Observer<Void> {
	
	PolyCreateControler controler;
	
	public DoGoBackwardObserver(PolyCreateControler controler) {
		this.controler = controler;
	}

	@Override
	public void next(Void value) {
		controler.doGoBackward();
	}

}
