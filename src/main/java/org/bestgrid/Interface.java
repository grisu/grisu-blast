package org.bestgrid;

import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;

import org.bestgrid.view.BlastView;
import org.bestgrid.view.BlastPanels;
import org.bestgrid.model.BlastModel;
import org.bestgrid.control.BlastController;

public class Interface {
    public static void main(String args[]) {
    	System.out.println("Logging in...");
		ServiceInterface si = null;
		try {
			si = LoginManager.loginCommandline("BeSTGRID");
		} catch (Exception e) {
			System.err.println("Could not login: " + e.getLocalizedMessage());
			System.exit(1);
		}
		
    	final BlastModel myModel = new BlastModel();
    	myModel.setServiceInterface(si);
    	//final BlastView myView = new BlastView();
    	final BlastPanels myView = new BlastPanels();
    	@SuppressWarnings("unused")
		//BlastController myController = new BlastController(myModel, myView, args);
    	BlastController myController = new BlastController(myModel, myView);
    }
}
