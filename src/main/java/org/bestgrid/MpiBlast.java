package org.bestgrid;

import grisu.control.ServiceInterface;
import grisu.frontend.view.swing.GrisuApplicationWindow;
import grisu.frontend.view.swing.jobcreation.JobCreationPanel;
import grisu.frontend.view.swing.jobcreation.widgets.SubmissionLogPanel;

import java.awt.Component;
import java.awt.EventQueue;

import org.bestgrid.view.BlastSubmissionPanel;

public class MpiBlast extends GrisuApplicationWindow {

	public static final String MPI_BLAST_APP_NAME = "mpiblast";

	public static String MPI_BLAST_DEFAULT_VERSION = "1.6.0";

	public static void main(String[] args) {

		// creating the UI
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					GrisuApplicationWindow appWindow = new MpiBlast();
					appWindow.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	//public final ServiceInterface si;

	public JobCreationPanel[] panels = null;

	// a ready-made widget that, once connected to a JobObject, tracks the
	// progress of a job submission...
	private SubmissionLogPanel submissionLogPanel;

	// pretty much everything is done for us in the superclass
	public MpiBlast() {
		super();
	}

	@Override
	public boolean displayAppSpecificMonitoringItems() {
		// yes, we only want to see the jobs that were submitted with this
		// client and not the "all jobs" menu item
		return true;
	}

	@Override
	public boolean displayBatchJobsCreationPane() {
		// no, we only submit a single job
		return false;
	}

	@Override
	public boolean displaySingleJobsCreationPane() {
		// yes
		return true;
	}

	@Override
	public JobCreationPanel[] getJobCreationPanels() {
		// only one type of job submission in our case, you can have more though
		// (e.g. a basic one and an advanced)
		// return new JobCreationPanel[] { new Viewer(this) };
		if (panels == null) {
			panels = new JobCreationPanel[] { new BlastSubmissionPanel() };
		}
		return panels;
	}

	@Override
	public String getName() {
		// if you leave it the way it is, the name of your artifact will be the
		// title of the java frame of this application. You can hardcode
		// something different if you like, though.
		return "grisu-blast";
	}

	// creating a submission log panel
	public SubmissionLogPanel getSubmissionLogPanel() {
		if (submissionLogPanel == null) {
			submissionLogPanel = new SubmissionLogPanel();
			submissionLogPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		}
		return submissionLogPanel;
	}

	@Override
	protected void initOptionalStuff(ServiceInterface si) {

	}
}