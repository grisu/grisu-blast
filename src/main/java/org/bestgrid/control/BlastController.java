package org.bestgrid.control;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.control.exceptions.JobSubmissionException;
import grisu.control.exceptions.NoSuchJobException;
import grisu.frontend.model.job.JobObject;
import grisu.jcommons.constants.Constants;

//import grisu.model.GrisuRegistryManager;

import java.awt.Desktop;
import java.io.*;

import org.bestgrid.model.BlastModel;
import org.bestgrid.view.BlastView;

public class BlastController {	
	private ServiceInterface si;
	
	public BlastController(final BlastModel myModel, final BlastView myView, String[] aCommand) {
		myModel.setModel(aCommand);	
			while(true) {
					try {
						//getting the command from command line
						BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
						String line = reader.readLine();
						String[] command = line.split("\\s");
						
						//send commands to the model
						myModel.setModel(command);
						myView.setView(myModel.getModel());

						if (line.equals("showcommand")) {
							myModel.constructCommand();
							System.out.println(myModel.getCommandline());
						}
						
						if (line.equals("submit")) {
							submit(myModel);
							break;
						}
						
						/*
						char c = (char) System.in.read();
						if(c != '\n' && c != '\r') {
							myModel.setModel(c);
							myView.setView(myModel.getModel());		
						}*/
					}
					catch(Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
				}
		}

	public ServiceInterface getServiceInterface() {
		return this.si;
	}
	
	public void setServiceInterface(ServiceInterface si) {
		this.si = si;
	}
    
	private void submit(BlastModel myModel) {
		System.out.println("Creating job...");
		//myModel.setServiceInterface(this.si);
		myModel.constructCommand();
		JobObject job = myModel.createJobObject();
		//job.setSubmissionLocation("grid_linux:ng1hpc.canterbury.ac.nz#Loadleveler");
		job.setSubmissionLocation("dev8_1:ng2hpc.canterbury.ac.nz#Loadleveler");
		//job.setSubmissionLocation("route@er171.ceres.auckland.ac.nz:ng2.auckland.ac.nz"); //changed temporarily while Bluefern is down
		
		try {
			System.out.println("Creating job on backend...");
			//job.createJob("/ARCS/BeSTGRID");
			job.createJob("/ARCS/LocalAccounts/CanterburyHPC"); //if sending to BlueFern local account
			System.out.println(job.getCommandline());
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
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			System.err.println("Jobsubmission interrupted: "
					+ e.getLocalizedMessage());
			System.exit(1);
		}
		
		System.out.println(job.getJobDirectoryUrl());
		
		System.out.println("Job submission finished.");
		System.out.println("Job submitted to: "
				+ job.getJobProperty(Constants.SUBMISSION_SITE_KEY));

		System.out.println("Waiting for job to finish...");

		// for a realy workflow, don't check every 5 seconds since that would
		// put too much load on the backend/gateways
		job.waitForJobToFinish(5);

		System.out.println("Job finished with status: "
				+ job.getStatusString(false));

		try {
			System.out.println("Constructing job properties...");
			myModel.constructProperties(job);
		} catch (NoSuchJobException nsje) {
			System.out.println(nsje.getMessage());
			nsje.printStackTrace();
		}
		System.out.println("Stdout: " + job.getStdOutContent());
		System.out.println("Stderr: " + job.getStdErrContent());
		
		System.out.println("Output File: " + job.getJobProperty("OUTPUT_FILE_KEY",true));
		
		//printing the content of output file
		System.out.println("Output content: " + job.getFileContent(job.getJobProperty("OUTPUT_FILE_KEY", true)));
		
		String url = job.getJobDirectoryUrl() + "/" + job.getJobProperty("OUTPUT_FILE_KEY", true);
		System.out.println("URL: " + url);
		File file = job.downloadAndCacheOutputFile(url);
		try {
			Desktop.getDesktop().open(file);
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			ioe.printStackTrace();
		}
		
		
		// it's good practise to shutdown the jvm properly. There might be some
		// executors running in the background
		// and they need to know when to shutdown.
		// Otherwise, your application might not exit.
		System.exit(0);
	}
}
