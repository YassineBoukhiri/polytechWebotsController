package fr.univcotedazur.kairos.webots.polycreate;

import com.yakindu.core.rx.Observer;

public class DoCatchObserver implements Observer<Void> {
	
	PolyCreateControler controler;
	
	public DoCatchObserver(PolyCreateControler controler) {
		this.controler = controler;
	}

	@Override
	public void next(Void value) {
		controler.doCatch();
	}

}
