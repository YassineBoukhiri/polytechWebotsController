package fr.univcotedazur.kairos.webots.polycreate;

import com.yakindu.core.rx.Observer;

public class DoOpenGripperObserver implements Observer<Void> {
	
	PolyCreateControler controler;
	
	public DoOpenGripperObserver(PolyCreateControler controler) {
		this.controler = controler;
	}

	@Override
	public void next(Void value) {
		controler.doOpenGripper();
	}

}
