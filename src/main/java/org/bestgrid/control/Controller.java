package org.bestgrid.control;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.control.exceptions.JobSubmissionException;
import grisu.control.exceptions.NoSuchJobException;
import grisu.control.exceptions.RemoteFileSystemException;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.JobObject;
import grisu.jcommons.constants.Constants;

//import grisu.model.GrisuRegistryManager;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.logging.Level;

import javax.swing.JCheckBox;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.bestgrid.MpiBlast;
import org.bestgrid.model.BlastModel;
import org.bestgrid.model.GraphicalModel;
import org.bestgrid.view.BlastPanelsWithOptions;
import org.bestgrid.view.BlastView;
import org.bestgrid.view.GraphicalView;
import org.bestgrid.view.Viewer;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class Controller {	
	private ServiceInterface si;
	private GraphicalModel myModel;
	private Viewer myView;
	private MpiBlast client; //the client that created all this
	
	public Controller(MpiBlast myClient, final GraphicalModel myModel, final Viewer myView) {
			this.myModel = myModel;
			this.myView = myView;
			this.client = myClient;
			
			/*TODO change listeners to event changes*/
			myView.addSubmitListener(new SubmitListener());
			myView.addBlastAppListener(new BlastListener());
			//myView.addInputFileListener(new InputFileListener());
			myView.addInputQueryListener(new InputQueryListener());
			myView.addOutputFileNameListener(new OutputFileNameListener());
			myView.addDatabaseListener(new DatabaseNameListener());
			myView.addQrySubrangeStartListener(new QuerySubrangeStartListener());
			myView.addQrySubrangeStopListener(new QuerySubrangeStopListener());
			myView.addQryGencodeListener(new GencodeListener());
			myView.addEValueListener(new EValueListener());
			myView.addMatrixListener(new MatrixListener());
			myView.addLowerCaseMaskListener(new LowerCaseMaskListener());
			
			myView.setVisible(true);
	}
	
	//////////////////////////////////////////inner class Submit
	class SubmitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			submitJob();
		}
	}//end inner class SubmitListener
	
	//////////////////////////////////////////inner class BlastListener
	class BlastListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			String blast;
			blast = myView.getBlastApp(); 	
		  
			myModel.setBlast(blast);
			System.out.println("Blast app selected: " + myView.getBlastApp());  	  
		  
			myModel.createCommand();
			System.out.println(myModel.getCommandline());
			myView.setAraShowCommand(myModel.getCommandline());        
		}
	}//end inner class BlastListener
	
	//////////////////////////////////////////inner class InputQueryListener
	class InputQueryListener implements InputMethodListener {
		private void setInputFile() {
			String file;
			file = myView.getInputQuery();	
		  
			myModel.setInputFile(file);
						
			System.out.println("Location of input file: " + file);
			myModel.createCommand();

			System.out.println(myModel.getCommandline());
			myView.setAraShowCommand(myModel.getCommandline());
		}
		public void caretPositionChanged(InputMethodEvent arg0) {
			setInputFile();			
		}

		public void inputMethodTextChanged(InputMethodEvent arg0) {
			setInputFile();				
		}
	}//end inner class InputQueryListener
	
	//////////////////////////////////////////inner class InputFileListener
	class InputFileListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String location;
			
			JTextField tf = (JTextField) e.getSource();
			location = tf.getText();
			
			myModel.setInputFile(location);
			
			System.out.println("Location of input file: " + myView.getInputFile());
			myModel.createCommand();

			System.out.println(myModel.getCommandline());
			myView.setAraShowCommand(myModel.getCommandline());
		}
	}//end inner class InputFileListener
	
	//////////////////////////////////////////inner class OutputFileNameListener
	class OutputFileNameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String filename;
			
			JTextField tf = (JTextField) e.getSource();
			filename = tf.getText();
			
			myModel.setOutput(filename);
			
			System.out.println("Name of output file: " + myView.getOutputFile());
			myModel.createCommand();

			System.out.println(myModel.getCommandline());
			myView.setAraShowCommand(myModel.getCommandline());
		}
	}//end inner class OutputFileNameListener
	
	//////////////////////////////////////////inner class DatabaseNameListener
	class DatabaseNameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String db;
			
			JTextField tf = (JTextField) e.getSource();
			db = tf.getText();
			
			myModel.setDatabase(db);
			
			System.out.println("Database name: " + myView.getDatabase());
			myModel.createCommand();

			System.out.println(myModel.getCommandline());
			myView.setAraShowCommand(myModel.getCommandline());
		}
	}//end inner class DatabaseNameListener
	
	//////////////////////////////////////////inner class QuerySubrangeStartListener
	class QuerySubrangeStartListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String from;
			
			JTextField tf = (JTextField) e.getSource();
			from = tf.getText();
			
			myModel.setQueryFrom(from);
			
			System.out.println("Query subrange from " + myView.getQuerySubrangeFrom());
			myModel.createCommand();

			System.out.println(myModel.getCommandline());
			myView.setAraShowCommand(myModel.getCommandline());
		}
	}//end inner class QuerySubrangeStartListener
	
	//////////////////////////////////////////inner class QuerySubrangeStopListener
	class QuerySubrangeStopListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String to;
			
			JTextField tf = (JTextField) e.getSource();
			to = tf.getText();
			
			myModel.setQueryTo(to);
			
			System.out.println("Query subrange to " + myView.getQuerySubrangeTo());
			myModel.createCommand();

			System.out.println(myModel.getCommandline());
			myView.setAraShowCommand(myModel.getCommandline());
		}
	}//end inner class QuerySubrangeStopListener
	
	//////////////////////////////////////////inner class GencodeListener
	class GencodeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String gencode;
			
			JTextField tf = (JTextField) e.getSource();
			gencode = tf.getText();
			
			myModel.setQueryGencode(gencode);
			
			System.out.println("Gencode: " + myView.getQueryGencode());
			myModel.createCommand();

			System.out.println(myModel.getCommandline());
			myView.setAraShowCommand(myModel.getCommandline());
		}
	}//end inner class GencodeListener
	
	//////////////////////////////////////////inner class EValueListener
	class EValueListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String evalue;
			
			JTextField tf = (JTextField) e.getSource();
			evalue = tf.getText();
			
			myModel.setEValue(evalue);
			
			System.out.println("EValue: " + myView.getEValue());
			myModel.createCommand();

			System.out.println(myModel.getCommandline());
			myView.setAraShowCommand(myModel.getCommandline());
		}
	}//end inner class EValueListener
	
	//////////////////////////////////////////inner class MatrixListener
	class MatrixListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String matrix;
			
			JTextField tf = (JTextField) e.getSource();
			matrix = tf.getText();
			
			myModel.setMatrix(matrix);
			
			System.out.println("Matrix: " + myView.getMatrix());
			myModel.createCommand();

			System.out.println(myModel.getCommandline());
			myView.setAraShowCommand(myModel.getCommandline());
		}
	}//end inner class MatrixListener
	
	//////////////////////////////////////////inner class BlastListener
	class LowerCaseMaskListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			JCheckBox src = (JCheckBox) e.getSource();
			if(src.isSelected()) {
				myModel.setLCaseMask(true);
				System.out.println("Blast app selected: " + myView.getLCaseMask());  	  
				  
				myModel.createCommand();
				System.out.println(myModel.getCommandline());
				myView.setAraShowCommand(myModel.getCommandline()); 
			} else {
				myModel.setLCaseMask(false);
				System.out.println("Blast app selected: " + myView.getLCaseMask());  	  
				  
				myModel.createCommand();
				System.out.println(myModel.getCommandline());
				myView.setAraShowCommand(myModel.getCommandline());
			}
		}
	}//end inner class BlastListener
	public ServiceInterface getServiceInterface() {
		return this.si;
	}
	
	public void setServiceInterface(ServiceInterface si) {
		this.si = si;
	}
	
	// the method to actually submit the job
	private void submitJob() {

		// we submit the job in it's own thread in order to not block the swing
		// ui
		new Thread() {
			@Override
			public void run() {
				try {
					/*Done: text area for status when submitting include a jobname
					 * TODO add submit locations options*/
					
					// disable the submit button so the user can't inadvertently
					// submit 2 jobs in a row
					myView.lockUI(true);

					// now, let's create the job
					System.out.println("Creating job...");
					myView.setStatusText("Creating job...");
					myModel.setServiceInterface(client.getServiceInterface());
					myModel.constructCommand();
					JobObject job = myModel.createJobObject();
					job.setSubmissionLocation("dev8_1:ng2hpc.canterbury.ac.nz#Loadleveler");
					//job.setSubmissionLocation("route@er171.ceres.auckland.ac.nz:ng2.auckland.ac.nz"); //changed temporarily while Bluefern is down
					//job.setSubmissionLocation("grid_linux:ng1hpc.canterbury.ac.nz#Loadleveler");
					
					// ... and connect it to the submission log panel so the
					// user can see that there's something going on...
					client.getSubmissionLogPanel().setJobObject(job);
					
					//creating the job on backend
					System.out.println("Creating job on backend...");
					myView.setStatusText("Creating job on backend...");
					//job.createJob("/ARCS/BeSTGRID");
					job.createJob("/ARCS/LocalAccounts/CanterburyHPC"); //if sending to BlueFern local account
					System.out.println(job.getCommandline());
					
					//submitting the job to the grid
					System.out.println("Submitting job to the grid...");
					myView.setStatusText("Submitting job to the grid...");
					job.submitJob();
					System.out.println("Job submission finished.");
					myView.setStatusText("Job submitted as: " + job.getJobname());
				} catch (Exception e) {
					myView.showError(e);
				} finally {
					// enable the submit button again
					myView.lockUI(false);
				}

			}
		}.start();
	}
}
	
	