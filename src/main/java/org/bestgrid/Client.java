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
			si = LoginManager.loginCommandline("BeSTGRID");
		} catch (Exception e) {
			System.err.println("Could not login: " + e.getLocalizedMessage());
			System.exit(1);
		}

		System.out.println("Creating job...");
		JobObject job = new JobObject(si);
		job.setApplication("UnixCommands");
		String filename = FileManager.getFilename(args[0]);
		job.setCommandline("cat " + filename);
		job.addInputFileUrl(args[0]);
		job.setWalltimeInSeconds(60);
		job.setSubmissionLocation("route@er171.ceres.auckland.ac.nz:ng2.auckland.ac.nz");

		job.setTimestampJobname("cat_job");

		System.out.println("Set jobname to be: " + job.getJobname());

		try {
			System.out.println("Creating job on backend...");
			job.createJob("/ARCS/BeSTGRID");
		} catch (JobPropertiesException e) {
			System.err.println("Could not create job: "
					+ e.getLocalizedMessage());
			System.exit(1);
		}

		try {
			System.out.println("Submitting job to the grid...");
			job.submitJob();
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
				+ job.getJobProperty(Constants.SUBMISSION_SITE_KEY));

		System.out.println("Waiting for job to finish...");

		// for a realy workflow, don't check every 5 seconds since that would
		// put too much load on the backend/gateways
		job.waitForJobToFinish(5);

		System.out.println("Job finished with status: "
				+ job.getStatusString(false));

		System.out.println("Stdout: " + job.getStdOutContent());
		System.out.println("Stderr: " + job.getStdErrContent());

		// it's good practise to shutdown the jvm properly. There might be some
		// executors running in the background
		// and they need to know when to shutdown.
		// Otherwise, your application might not exit.
		System.exit(0);

	}

}
