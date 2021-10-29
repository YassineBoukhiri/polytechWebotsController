package fr.univcotedazur.kairos.webots.polycreate;

import com.yakindu.core.rx.Observer;

public class DoGoForwardObserver implements Observer<Void> {
	
	PolyCreateControler controler;
	
	public DoGoForwardObserver(PolyCreateControler controler) {
		this.controler = controler;
	}

	@Override
	public void next(Void value) {
		controler.doGoForward();
	}

}
