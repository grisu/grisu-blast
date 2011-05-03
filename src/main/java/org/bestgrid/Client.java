package org.bestgrid;

import grisu.model.FileManager;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.control.exceptions.JobSubmissionException;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.JobObject;
import grisu.jcommons.constants.Constants;
import grisu.model.FileManager;

public class Client {

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
<<<<<<< HEAD
		/*
		JobObject job = new JobObject(si);
		job.setApplication("UnixCommands");
		String filename = FileManager.getFilename(args[0]);
		job.setCommandline("cat " + filename);
		job.addInputFileUrl(args[0]);
		job.setWalltimeInSeconds(60);
		job.setSubmissionLocation("serial_400:ng2hpc.canterbury.ac.nz#Loadleveler");
		// the following does not work. Vlad confirmed correct settings on webmds. Ask Markus?
		// job.setSubmissionLocation("dev8_1:ng2hpc.canterbury.ac.nz#Loadleveler");

		job.setTimestampJobname("cat_job");

		System.out.println("Set jobname to be: " + job.getJobname());
*/
=======

>>>>>>> ecdfe321c448326a5d95a4e0b78f60a35fbd95b6
		BlastJobCLI job = new BlastJobCLI();
		job.setVo("/ARCS/BeSTGRID");
		job.setServiceInterface(si);
<<<<<<< HEAD
		job.setSubmissionLocation("grid_aix:ng2hpc.canterbury.ac.nz#Loadleveler");
		//get the inputs
		String theCommand = new String();
		for(int i = 0; i < args.length; i++) {
			theCommand = args[i] + " ";
		}
		
		job.setCommandline(theCommand);
=======
		job.setSubmissionLocation("dev8_1:ng2hpc.canterbury.ac.nz#Loadleveler");
		job.setCommandline(args[0]);

>>>>>>> ecdfe321c448326a5d95a4e0b78f60a35fbd95b6
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
<<<<<<< HEAD
		
=======



>>>>>>> ecdfe321c448326a5d95a4e0b78f60a35fbd95b6
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
