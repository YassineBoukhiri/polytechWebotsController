package fr.univcotedazur.kairos.webots.polycreate;

import com.yakindu.core.rx.Observer;

public class DoGoToObserver implements Observer<Void> {
	
	PolyCreateControler controler;
	
	public DoGoToObserver(PolyCreateControler controler) {
		this.controler = controler;
	}

	@Override
	public void next(Void value) {
		controler.doGoTo();
	}

}
