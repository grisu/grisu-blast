package org.bestgrid;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.control.exceptions.JobSubmissionException;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.JobObject;
import grisu.jcommons.constants.Constants;

import org.apache.commons.cli.Options;
import org.bestgrid.view.BlastView;
import org.bestgrid.model.BlastModel;
import org.bestgrid.control.BlastController;

public class Interface {
    public static void main(String a[]) {
    	System.out.println("Logging in...");
		ServiceInterface si = null;
		try {
			si = LoginManager.loginCommandline("BeSTGRID-TEST");
		} catch (Exception e) {
			System.err.println("Could not login: " + e.getLocalizedMessage());
			System.exit(1);
		}
		
    	final BlastModel myModel = new BlastModel();
    	final BlastView myView = new BlastView();
    	BlastController myController = new BlastController(myModel, myView, si);
    }
}
