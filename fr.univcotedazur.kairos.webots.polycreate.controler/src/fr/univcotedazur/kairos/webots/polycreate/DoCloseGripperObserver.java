package fr.univcotedazur.kairos.webots.polycreate;

import com.yakindu.core.rx.Observer;

public class DoCloseGripperObserver implements Observer<Void> {
	
	PolyCreateControler controler;
	
	public DoCloseGripperObserver(PolyCreateControler controler) {
		this.controler = controler;
	}

	@Override
	public void next(Void value) {
		controler.doCloseGripper();
	}

}
