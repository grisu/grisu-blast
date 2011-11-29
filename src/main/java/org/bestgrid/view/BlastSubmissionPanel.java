package org.bestgrid.view;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.frontend.control.jobMonitoring.RunningJobManager;
import grisu.frontend.model.job.JobObject;
import grisu.frontend.view.swing.DefaultFqanChangePanel;
import grisu.frontend.view.swing.jobcreation.JobCreationPanel;
import grisu.frontend.view.swing.jobcreation.widgets.AbstractWidget;
import grisu.frontend.view.swing.jobcreation.widgets.Cpus;
import grisu.frontend.view.swing.jobcreation.widgets.SingleInputGridFile;
import grisu.frontend.view.swing.jobcreation.widgets.SubmissionLocationPanel;
import grisu.frontend.view.swing.jobcreation.widgets.SubmissionLogPanel;
import grisu.frontend.view.swing.jobcreation.widgets.TextCombo;
import grisu.frontend.view.swing.jobcreation.widgets.TextField;
import grisu.model.GrisuRegistryManager;
import grisu.model.dto.GridFile;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.bestgrid.MpiBlast;
import org.bestgrid.model.GraphicalModel;
import org.python.google.common.collect.Lists;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class BlastSubmissionPanel extends JPanel implements JobCreationPanel,
PropertyChangeListener {
	private TextCombo blastType;
	public static final String[] BLAST_TYPES = { "blastn", "blastp", "blastx",
		"tblastn", "tblastx" };
	private TextField fromSubrangeField;
	private JPanel panel;
	private TextField toSubrangeField;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private TextField geneticCode;
	private SingleInputGridFile singleInputGridFile;
	private TextField database;
	private TextField matrix;
	private TextField expectThreshold;
	private JSeparator separator;
	private Cpus cpus;
	private SubmissionLocationPanel submissionLocationPanel;
	private JButton btnNewButton;
	private DefaultFqanChangePanel defaultFqanChangePanel;

	private final GraphicalModel blastModel = new GraphicalModel();
	private TextField outputFile;

	private ServiceInterface si;
	private SubmissionLogPanel submissionLogPanel;

	private final List<AbstractWidget> widgets = Lists.newArrayList();

	public BlastSubmissionPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(43dlu;pref)"),
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC, }));
		add(getBlastType(), "2, 2, 3, 1, fill, fill");
		add(getPanel_1(), "6, 2, 3, 3, fill, fill");
		add(getGeneticCode(), "2, 4, 3, 1, fill, fill");
		add(getSingleInputGridFile(), "2, 6, 7, 1, fill, fill");
		add(getDatabase(), "2, 8, 3, 1, fill, fill");
		add(getMatrix(), "6, 8, 3, 1, fill, fill");
		add(getExpectThreshold(), "2, 10, 3, 1, fill, fill");
		add(getOutputFile(), "6, 10, 3, 1, fill, fill");
		add(getSeparator(), "2, 12, 7, 1");
		add(getCpus(), "2, 14, fill, fill");
		add(getSubmissionLocationPanel(), "4, 14, 5, 1, fill, fill");
		add(getDefaultFqanChangePanel(), "2, 16, 5, 1, fill, center");
		add(getBtnNewButton(), "8, 16, right, center");
		add(getSubmissionLogPanel(), "2, 18, 7, 1, fill, fill");

	}

	public boolean createsBatchJob() {
		return false;
	}

	public boolean createsSingleJob() {
		return true;
	}

	private TextCombo getBlastType() {
		if (blastType == null) {
			blastType = new TextCombo();
			widgets.add(blastType);
			blastType.setTitle("Blast type");
			blastType.setSelectionValues(BLAST_TYPES);
			blastType.addWidgetListener(this);
		}
		return blastType;
	}

	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("Submit");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionevent) {

					submitJob();

				}
			});
		}
		return btnNewButton;
	}

	private Cpus getCpus() {
		if (cpus == null) {
			cpus = new Cpus();
			widgets.add(cpus);
			cpus.addWidgetListener(this);
		}
		return cpus;
	}

	private TextField getDatabase() {
		if (database == null) {
			database = new TextField();
			widgets.add(database);
			database.setTitle("Database");
			database.addWidgetListener(this);
		}
		return database;
	}

	private DefaultFqanChangePanel getDefaultFqanChangePanel() {
		if (defaultFqanChangePanel == null) {
			defaultFqanChangePanel = new DefaultFqanChangePanel();
		}
		return defaultFqanChangePanel;
	}

	private TextField getExpectThreshold() {
		if (expectThreshold == null) {
			expectThreshold = new TextField();
			widgets.add(expectThreshold);
			expectThreshold.setTitle("Expect Threshold");
			expectThreshold.addWidgetListener(this);
		}
		return expectThreshold;
	}

	private TextField getFromSubrangeField() {
		if (fromSubrangeField == null) {
			fromSubrangeField = new TextField();
			widgets.add(fromSubrangeField);
			fromSubrangeField.addWidgetListener(this);
		}
		return fromSubrangeField;
	}

	private TextField getGeneticCode() {
		if (geneticCode == null) {
			geneticCode = new TextField();
			widgets.add(geneticCode);
			geneticCode.setTitle("Genetic code");
			geneticCode.addWidgetListener(this);
		}
		return geneticCode;
	}

	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("from:");
		}
		return lblNewLabel;
	}

	private JLabel getLblNewLabel_1() {
		if (lblNewLabel_1 == null) {
			lblNewLabel_1 = new JLabel("to:");
		}
		return lblNewLabel_1;
	}

	private TextField getMatrix() {
		if (matrix == null) {
			matrix = new TextField();
			widgets.add(matrix);
			matrix.setTitle("Matrix");
			matrix.addWidgetListener(this);
		}
		return matrix;
	}

	private TextField getOutputFile() {
		if (outputFile == null) {
			outputFile = new TextField();
			widgets.add(outputFile);
			outputFile.setTitle("Output file");
		}
		return outputFile;
	}

	public JPanel getPanel() {
		return this;
	}

	private JPanel getPanel_1() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207,
					229)), "Query Subrange", TitledBorder.LEADING,
					TitledBorder.TOP, null, new Color(51, 51, 51)));
			panel.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("default:grow"),
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("default:grow"), }, new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC, }));
			panel.add(getLblNewLabel(), "2, 2");
			panel.add(getFromSubrangeField(), "4, 2");
			panel.add(getLblNewLabel_1(), "2, 4");
			panel.add(getToSubrangeField(), "4, 4, fill, fill");
		}
		return panel;
	}

	public String getPanelName() {
		return "MPI Blast";
	}

	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
		}
		return separator;
	}

	private SingleInputGridFile getSingleInputGridFile() {
		if (singleInputGridFile == null) {
			singleInputGridFile = new SingleInputGridFile();
			widgets.add(singleInputGridFile);
			singleInputGridFile.addWidgetListener(this);
		}
		return singleInputGridFile;
	}

	private SubmissionLocationPanel getSubmissionLocationPanel() {
		if (submissionLocationPanel == null) {
			submissionLocationPanel = new SubmissionLocationPanel();
			widgets.add(submissionLocationPanel);
			submissionLocationPanel.setApplication(MpiBlast.MPI_BLAST_APP_NAME);
			submissionLocationPanel
			.setVersion(MpiBlast.MPI_BLAST_DEFAULT_VERSION);
			submissionLocationPanel.addWidgetListener(this);
		}
		return submissionLocationPanel;
	}

	private SubmissionLogPanel getSubmissionLogPanel() {
		if (submissionLogPanel == null) {
			submissionLogPanel = new SubmissionLogPanel();
		}
		return submissionLogPanel;
	}

	public String getSupportedApplication() {
		return MpiBlast.MPI_BLAST_APP_NAME;
	}

	private TextField getToSubrangeField() {
		if (toSubrangeField == null) {
			toSubrangeField = new TextField();
			widgets.add(toSubrangeField);
			toSubrangeField.addWidgetListener(this);
		}
		return toSubrangeField;
	}

	private void lockUI(final boolean lock) {

		SwingUtilities.invokeLater(new Thread() {
			@Override
			public void run() {
				getBtnNewButton().setEnabled(!lock);
			}
		});

		getDefaultFqanChangePanel().lockUI(lock);
		for (AbstractWidget w : widgets) {
			w.lockUI(lock);
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {

		// System.out.println(evt.getPropertyName() + ": " + evt.getNewValue());

		Object source = evt.getSource();

		// TODO make sure that is all correct
		if (source == getGeneticCode()) {
			// TODO not sure what to set here
		} else if (source == getBlastType()) {
			blastModel.setBlast((String) evt.getNewValue());
		} else if (source == getFromSubrangeField()) {
			blastModel.setQueryFrom((String) evt.getNewValue());
		} else if (source == getToSubrangeField()) {
			blastModel.setQueryTo((String) evt.getNewValue());
		} else if (source == getSingleInputGridFile()) {
			blastModel.setFastaFile((GridFile) evt.getNewValue());
		} else if (source == getDatabase()) {
			blastModel.setDatabase((String) evt.getNewValue());
		} else if (source == getMatrix()) {
			blastModel.setMatrix((String) evt.getNewValue());
		} else if (source == getExpectThreshold()) {
			blastModel.setEValue((String) evt.getNewValue());
		} else if (source == getOutputFile()) {
			blastModel.setOutput((String) evt.getNewValue());
		}
		blastModel.constructCommand();
		System.out.println("Command: " + blastModel.getCommandline());

		getSubmissionLogPanel().setText(
				"Current command: " + blastModel.getCommandline());

	}

	public void setServiceInterface(ServiceInterface si) {

		this.si = si;
		try {
			getDefaultFqanChangePanel().setServiceInterface(si);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		getSubmissionLocationPanel().setServiceInterface(si);
		getSingleInputGridFile().setServiceInterface(si);

	}

	private void submitJob() {

		// TODO validate inputs
		final String blastType = getBlastType().getValue();
		final String genCode = getGeneticCode().getValue();

		final String from = getFromSubrangeField().getValue();
		final String to = getToSubrangeField().getValue();

		final GridFile inputfile = getSingleInputGridFile().getInputFile();
		final String database = getDatabase().getValue();
		final String matrix = getMatrix().getValue();
		final String expectThreshold = getExpectThreshold().getValue();
		final String outputFile = getOutputFile().getValue();

		final int cpus = getCpus().getCpus();
		final String subLoc = getSubmissionLocationPanel().getValue();

		final String fqan = GrisuRegistryManager.getDefault(si)
				.getUserEnvironmentManager().getCurrentFqan();

		final GraphicalModel submitModel = new GraphicalModel();
		submitModel.setBlast(blastType);
		// TODO set gencode, not sure
		submitModel.setQueryFrom(from);
		submitModel.setQueryTo(to);
		submitModel.setFastaFile(inputfile);
		submitModel.setDatabase(database);
		submitModel.setMatrix(matrix);
		submitModel.setEValue(expectThreshold); // TODO ????
		submitModel.setOutput(outputFile);

		// TODO fastafilename & timestamp is used for jobname, add input field
		// if you want to be able to have the user choose that....
		final String fastaFileName = inputfile.getName();
		int i = fastaFileName.lastIndexOf(".");
		final String jobname = fastaFileName.substring(0, i);

		final JobObject job = new JobObject(si);

		getSubmissionLogPanel().setJobObject(job);

		Thread st = new Thread() {
			@Override
			public void run() {

				lockUI(true);

				try {

					job.setTimestampJobname(jobname);
					job.setCpus(cpus);
					job.setApplication(MpiBlast.MPI_BLAST_APP_NAME);
					job.setApplicationVersion(MpiBlast.MPI_BLAST_DEFAULT_VERSION);
					job.setSubmissionLocation(subLoc);

					// mpiblast specific
					submitModel.constructCommand();
					job.setCommandline(submitModel.getCommandline());
					job.addInputFileUrl(inputfile.getUrl());

					try {
						// this is needed in order for the job to instantly show
						// up in the
						// job list
						RunningJobManager.getDefault(si).createJob(job, fqan);
					} catch (JobPropertiesException e) {
						e.printStackTrace();
					}

					try {
						job.submitJob(submitModel.getExtraJobProperties(), true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} finally {
					lockUI(false);
				}
			}
		};
		st.setName("Submission Thread for job " + job.getJobname());
		st.start();

	}
}
