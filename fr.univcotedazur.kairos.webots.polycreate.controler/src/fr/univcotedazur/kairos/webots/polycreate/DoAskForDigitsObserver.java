package fr.univcotedazur.kairos.webots.polycreate;

import com.yakindu.core.rx.Observer;

public class DoAskForDigitsObserver implements Observer<Void> {
	
	PolyCreateControler controler;
	
	public DoAskForDigitsObserver(PolyCreateControler controler) {
		this.controler = controler;
	}

	@Override
	public void next(Void value) {
		controler.doAskForDigits();
	}

}