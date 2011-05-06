package org.bestgrid;

import java.util.Scanner;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.control.exceptions.JobSubmissionException;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.JobObject;
import grisu.jcommons.constants.Constants;
//import grisu.model.FileManager;
import org.apache.commons.cli.*;

public class Client {

	public static void main(String[] args) {

		System.out.println("Logging in...");
		ServiceInterface si = null;
		try {
			si = LoginManager.loginCommandline("BeSTGRID");
		} catch (Exception e) {
			System.err.println("Could not login: " + e.getLocalizedMessage());
			System.exit(1);
		}

		System.out.println("Creating job...");

		

		BlastJobCLI job = new BlastJobCLI();
		
		//getting arguments for job specification
		int cpu = 1;
		String submitLoc = "dev8_1:ng2hpc.canterbury.ac.nz#Loadleveler";
		String vo = "/ARCS/LocalAccounts/CanterburyHPC";
		
		if(args != null || args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				if(args[i] == "cpu") {
					cpu = Integer.parseInt(args[i+1]);
				} else if(args[i] == "vo") {
					vo = args[i+1];
				} else if(args[i] == "sl") {
					submitLoc = args[i+1];
				}
			} 
			job.setServiceInterface(si);
			job.setCpus(cpu);
			job.setVo(vo);
			job.setSubmissionLocation(submitLoc);
		} else {
			job.setServiceInterface(si);
			job.setCpus(cpu);
			job.setVo(vo);
			job.setSubmissionLocation(submitLoc);
		}
		
		Scanner input = new Scanner(System.in);
		System.out.println("enter blast command");
		String command = input.nextLine();
		job.setCommandline(command);

		JobObject theJob = job.createJobObject();

		// theJob.setApplication("UnixCommands");
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

		System.out.println("Job submission finished.");
		System.out.println("Job submitted to: "
				+ theJob.getJobProperty(Constants.SUBMISSION_SITE_KEY));
		/*+ job.getJobProperty(Constants.SUBMISSION_SITE_KEY));*/

		System.out.println("Waiting for job to finish...");

		// for a realy workflow, don't check every 5 seconds since that would
		// put too much load on the backend/gateways
		//job.waitForJobToFinish(5);
		theJob.waitForJobToFinish(5);

		System.out.println("Job finished with status: "
				/*+ job.getStatusString(false));*/
				+ theJob.getStatusString(false));

		System.out.println("Stdout: " + theJob.getStdOutContent());
		System.out.println("Stderr: " + theJob.getStdErrContent());

		// it's good practise to shutdown the jvm properly. There might be some
		// executors running in the background
		// and they need to know when to shutdown.
		// Otherwise, your application might not exit.
		System.exit(0);

	}

}
