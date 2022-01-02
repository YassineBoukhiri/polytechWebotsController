package fr.univcotedazur.kairos.webots.polycreate;

import com.yakindu.core.rx.Observer;

public class DoDrawDigitsObserver implements Observer<Void> {
	
	PolyCreateControler controler;
	
	public DoDrawDigitsObserver(PolyCreateControler controler) {
		this.controler = controler;
	}

	@Override
	public void next(Void value) {
		controler.doDrawDigits();
	}

}