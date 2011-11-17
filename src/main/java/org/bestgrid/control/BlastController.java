package org.bestgrid.control;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.control.exceptions.JobSubmissionException;
import grisu.control.exceptions.NoSuchJobException;
import grisu.frontend.model.job.JobObject;
import grisu.jcommons.constants.Constants;

//import grisu.model.GrisuRegistryManager;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.logging.Level;

import org.bestgrid.model.BlastModel;
import org.bestgrid.view.BlastView;
import org.bestgrid.view.BlastPanels;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class BlastController {	
	private ServiceInterface si;
	private BlastModel myModel;
	private BlastPanels myView;
	
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

	public BlastController(final BlastModel myModel, final BlastPanels myView) {
		while (true) {
			this.myModel = myModel;
			this.myView = myView;
			
			myView.addSubmitListener(new SubmitListener());
			myView.setVisible(true);
		}
	}
	
	//////////////////////////////////////////inner class Submit
	class SubmitListener implements ActionListener {
	  public void actionPerformed(ActionEvent e) {
		  String blast;
		  String inputFile;
		  String outputFile;
		  String database;
	      try {
	    	  blast = myView.getBlastApp();
	    	  inputFile = myView.getInputFile();
	    	  outputFile = myView.getOutputFile();
	    	  database = myView.getDatabase();
	    	  
	    	  myModel.setBlast(blast);
	    	  myModel.setDatabase(database);
	    	  myModel.setOutput(outputFile);
	    	  myModel.setFastaFile(inputFile);
	    	  
	    	  myModel.constructCommand();
	    	  myView.setTxtShowCommand(myModel.getCommandline());
	    	  
	    	  submitJob();
	          
	      } catch (Exception ex) {
	          myView.showError("Bad input: " + ex.getMessage());
	          ex.printStackTrace();
	      }
	  }
	}//end inner class SubmitListener
	
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

		try {
			System.out.println("Constructing job properties...");
			myModel.constructProperties(job);
		} catch (NoSuchJobException nsje) {
			System.out.println(nsje.getMessage());
			nsje.printStackTrace();
		}
		
		System.out.println("Waiting for job to finish...");

		// for a realy workflow, don't check every 5 seconds since that would
		// put too much load on the backend/gateways
		job.waitForJobToFinish(5);

		System.out.println("Job finished with status: "
				+ job.getStatusString(false));

<<<<<<< HEAD
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
		String url = job.getJobDirectoryUrl() + "/" + job.getJobProperty("OUTPUT_FILE_KEY", true);
=======
		System.out.println("Stdout: " + job.getStdOutContent());
		System.out.println("Stderr: " + job.getStdErrContent());
		
		String url = job.getJobDirectoryUrl() + "/" + job.getJobProperty("OUTPUT_FILE_KEY", true);
		System.out.println("Output File: " + job.getJobProperty("OUTPUT_FILE_KEY"));
>>>>>>> fb4e628c4037ea13b36195dcd3d001fb4ef656f6
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
	
	// the method to actually submit the job
	private void submitJob() {

		// we submit the job in it's own thread in order to not block the swing
		// ui
		new Thread() {
			@Override
			public void run() {
				try {
					// disable the submit button so the user can't inadvertently
					// submit 2 jobs in a row
					myView.lockUI(true);

					// now, let's create the job
					//JobObject job = new JobObject(si);
					
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

					try {
						System.out.println("Constructing job properties...");
						myModel.constructProperties(job);
					} catch (NoSuchJobException nsje) {
						System.out.println(nsje.getMessage());
						nsje.printStackTrace();
					}
					
					System.out.println("Waiting for job to finish...");

					// for a realy workflow, don't check every 5 seconds since that would
					// put too much load on the backend/gateways
					job.waitForJobToFinish(5);

					System.out.println("Job finished with status: "
							+ job.getStatusString(false));

					System.out.println("Stdout: " + job.getStdOutContent());
					System.out.println("Stderr: " + job.getStdErrContent());
					
					String url = job.getJobDirectoryUrl() + "/" + job.getJobProperty("OUTPUT_FILE_KEY", true);
					System.out.println("Output File: " + job.getJobProperty("OUTPUT_FILE_KEY"));
					System.out.println("URL: " + url);
					File file = job.downloadAndCacheOutputFile(url);
					try {
						Desktop.getDesktop().open(file);
					} catch (IOException ioe) {
						System.out.println(ioe.getMessage());
						ioe.printStackTrace();
					}
					

				} catch (Exception e) {
					// if something goes wrong, we want to show the user
					ErrorInfo info = new ErrorInfo("Job submission error",
							"Can't submit job:\n\n" + e.getLocalizedMessage(),
							null, "Error", e, Level.SEVERE, null);

					JXErrorPane pane = new JXErrorPane();
					pane.setErrorInfo(info);
					// the following line could be used to show a button to
					// submit
					// a bug/ticket to a bug tracking system
					// pane.setErrorReporter(new GrisuErrorReporter());

					JXErrorPane.showDialog(
							myView.getRootPane(), pane);
				} finally {
					// enable the submit button again
					myView.lockUI(false);
				}

			}
		}.start();
	}
}
