package org.bestgrid.view;

//import java.util.Scanner;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.control.exceptions.JobSubmissionException;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.JobObject;
//import grisu.jcommons.constants.Constants;
//import grisu.model.FileManager;
//import org.apache.commons.cli.*;
//import org.bestgrid.model.BlastJobCLI;

import control.CLIController;

public class Interface {

	public static void main(String[] args) {

		System.out.println("Logging in...");
		ServiceInterface si = null;
		try {
			si = LoginManager.loginCommandline("Local");
		} catch (Exception e) {
			System.err.println("Could not login: " + e.getLocalizedMessage());
			System.exit(1);
		}

		System.out.println("Creating job...");
		
		CLIController jobControl = new CLIController();
		
		jobControl.setServiceInterface(si);
		jobControl.createJob();
		jobControl.process(args);

		JobObject theJob = jobControl.getJobObject().createJobObject();
		
		try {
			System.out.println("Creating job on backend...");
			//job.createJob("/ARCS/LocalAccounts/CanterburyHPC");
			theJob.createJob();
		} catch (JobPropertiesException e) {
			System.err.println("Could not create job: "
					+ e.getLocalizedMessage());
			System.exit(1);
		}

		try {
			System.out.println("Submitting job to the grid...");
			//job.submitJob();
			theJob.submitJob();
		} catch (JobSubmissionException e) {
			System.err.println("Could not submit job: "
					+ e.getLocalizedMessage());
			System.exit(1);
		} catch (InterruptedException e) {
			System.err.println("Jobsubmission interrupted: "
					+ e.getLocalizedMessage());
			System.exit(1);
		}

		// it's good practise to shutdown the jvm properly. There might be some
		// executors running in the background
		// and they need to know when to shutdown.
		// Otherwise, your application might not exit.
		
		System.exit(0);

	}

}
