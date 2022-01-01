package fr.univcotedazur.kairos.webots.polycreate;

import com.yakindu.core.rx.Observer;

public class DoFlushReceiverObserver implements Observer<Void> {
	
	PolyCreateControler controler;
	
	public DoFlushReceiverObserver(PolyCreateControler controler) {
		this.controler = controler;
	}

	@Override
	public void next(Void value) {
		System.out.println("I'M Flushing");
		controler.doFlushIRReceiver();;
	}

}
