package fr.univcotedazur.kairos.webots.polycreate;

import com.yakindu.core.rx.Observer;

public class DoGetUserChoiceObserver implements Observer<Void> {
	
	PolyCreateControler controler;
	
	public DoGetUserChoiceObserver(PolyCreateControler controler) {
		this.controler = controler;
	}

	@Override
	public void next(Void value) {
		System.out.println("Getting user choice");
		controler.doGetUserChoice();
	}

}
