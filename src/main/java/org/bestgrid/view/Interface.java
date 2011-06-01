package org.bestgrid.view;

//import java.util.Scanner;

import org.apache.commons.cli.ParseException;
import org.bestgrid.control.CLIController;

import grisu.backend.model.job.Job;
import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.control.exceptions.JobSubmissionException;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.JobObject;
//import grisu.jcommons.constants.Constants;
//import grisu.model.FileManager;
//import org.apache.commons.cli.*;
//import org.bestgrid.model.BlastJobCLI;


public class Interface {

	public static void main(String[] args) {

		ServiceInterface si = null;
		System.out.println("Checking arguments...");
		
		CLIController jobControl = new CLIController();

		try {
			jobControl.process(args);
		} catch(ParseException exp) {
			System.err.println("Parsing failed. Reason: " + exp.getMessage());
			System.exit(1);
			}

		System.out.println("Logging in...");
		
		try {
			si = LoginManager.loginCommandline("BeSTGRID-DEV");
		} catch (Exception e) {
			System.err.println("Could not login: " + e.getLocalizedMessage());
			System.exit(1);
		}

		jobControl.setServiceInterface(si);
		//jobControl.createJob();
		
		System.out.println("Creating job...");

		jobControl.setServiceInterface(si);
		jobControl.getJobObject().
		setCommandline(jobControl.getJobObject().getCommandline());
		JobObject theJob = jobControl.getJobObject().createJobObject();
		
		try {
			System.out.println("Creating job on backend...");
			System.out.println(jobControl.getJobObject().getCommandline());
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
