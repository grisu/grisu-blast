package org.bestgrid.view;

import grisu.control.ServiceInterface;
import grisu.frontend.view.swing.jobcreation.JobCreationPanel;

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.logging.Level;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.bestgrid.MpiBlast;
import org.bestgrid.control.Controller;
import org.bestgrid.model.GraphicalModel;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

@SuppressWarnings("serial")
public class Viewer extends JPanel implements JobCreationPanel {
	
	// the serviceinterface. gets set when the setServiceInterface method is
	// called. we need it in order to submit the job and do everything else...
	private ServiceInterface si;
	private MpiBlast client;//the client that created this
	
	private JTextField txtFileToUpload;
	private JTextField txtOutputFileName;
	private JComboBox cmbBlastApp;
	private JTextArea araShowCommand;
	
	// the button to submit a job
	private JButton btnSubmit;
	private JTextField txtDatabase;
	private JTextField txtSubrangeFrom;
	private JTextField txtSubrangeTo;
	private JLabel lblSubrangeStart;
	private JLabel lblSubrangeStop;
	private JLabel lblGenCode;
	private JTextField txtGenCode;
	private JTextField txtEValue;
	private JLabel lblEValue;
	private JTextField txtMatrix;
	private JLabel lblMatrix;
	private JCheckBox chkLowerCase;

	private JPanel blastN;
	private JPanel blastP;
	
	/**
	 * Create the panel.
	 */
	public Viewer(MpiBlast client) {
		this.client = client;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		//tabbedPane = new JTabbedPane();
		
		//tabbedPane.add("blastN", getBlastNPanel());
		//tabbedPane.add("blastP", new JPanel());
		this.add(getBlastNPanel());
		
		final GraphicalModel 	model = new GraphicalModel();
		Controller 				control = new Controller(this.client, model, this);
		
	}
	
	public JPanel getBlastPPanel() {
		blastP = new JPanel();
		blastP.setLayout(null);
		blastP.add(getBtnSubmit());
		blastP.add(getCmbBlastApp());
		blastP.add(getTxtFileToUpload());
		blastP.add(getTxtOutputFileName());
		
		JLabel lblBlast = new JLabel("Blast Type:");
		lblBlast.setBounds(10, 11, 54, 14);
		blastP.add(lblBlast);
		
		JLabel lblInputFile = new JLabel("Input File Location:");
		lblInputFile.setBounds(10, 62, 92, 14);
		blastP.add(lblInputFile);
		
		JLabel lblOutputFile = new JLabel("Output:");
		lblOutputFile.setBounds(20, 652, 38, 14);
		blastP.add(lblOutputFile);
		blastP.add(getTxtDatabase());
		
		JLabel lblDatabase = new JLabel("Database");
		lblDatabase.setBounds(10, 118, 46, 14);
		blastP.add(lblDatabase);
		
		blastP.add(getAraShowCommand());
		
		JLabel lblQrySubrange = new JLabel("Query Subrange");
		lblQrySubrange.setBounds(186, 11, 89, 14);
		blastP.add(lblQrySubrange);
		blastP.add(getTxtSubrangeFrom());
		blastP.add(getTxtSubrangeTo());
		blastP.add(getLblSubrangeStart());
		blastP.add(getLblSubrangeStop());
		blastP.add(getLblGenCode());
		blastP.add(getTxtGenCode());
		blastP.add(getTxtEValue());
		blastP.add(getLblEValue());
		blastP.add(getTxtMatrix());
		blastP.add(getLblMatrix());
		
		blastP.add(getChkLowerCase());
		
		return blastP;
	}
	public JPanel getBlastNPanel() {
		blastN = new JPanel();
		blastN.setLayout(null);
		blastN.add(getBtnSubmit());
		blastN.add(getCmbBlastApp());
		blastN.add(getTxtFileToUpload());
		blastN.add(getTxtOutputFileName());
		
		JLabel lblBlast = new JLabel("Blast Type:");
		lblBlast.setBounds(10, 11, 54, 14);
		blastN.add(lblBlast);
		
		JLabel lblInputFile = new JLabel("Input File Location:");
		lblInputFile.setBounds(10, 62, 92, 14);
		blastN.add(lblInputFile);
		
		JLabel lblOutputFile = new JLabel("Output:");
		lblOutputFile.setBounds(39, 272, 38, 14);
		blastN.add(lblOutputFile);
		blastN.add(getTxtDatabase());
		
		JLabel lblDatabase = new JLabel("Database");
		lblDatabase.setBounds(10, 118, 46, 14);
		blastN.add(lblDatabase);
		
		blastN.add(getAraShowCommand());
		
		JLabel lblQrySubrange = new JLabel("Query Subrange");
		lblQrySubrange.setBounds(186, 11, 89, 14);
		blastN.add(lblQrySubrange);
		blastN.add(getTxtSubrangeFrom());
		blastN.add(getTxtSubrangeTo());
		blastN.add(getLblSubrangeStart());
		blastN.add(getLblSubrangeStop());
		blastN.add(getLblGenCode());
		blastN.add(getTxtGenCode());
		blastN.add(getTxtEValue());
		blastN.add(getLblEValue());
		blastN.add(getTxtMatrix());
		blastN.add(getLblMatrix());
		
		blastN.add(getChkLowerCase());
		
		return blastN;
	}
	
	public JCheckBox getChkLowerCase() {
		if (chkLowerCase == null) {
			chkLowerCase = new JCheckBox("Mask Lower Case Letters");
			chkLowerCase.setBounds(10, 232, 150, 23);
		}
		return chkLowerCase;
	}
	public JTextField getTxtEValue() {
		if (txtEValue == null) {
			txtEValue = new JTextField();
			txtEValue.setBounds(285, 139, 113, 20);
			txtEValue.setColumns(10);
		}
		
		return txtEValue;
	}
	
	public JTextArea getAraShowCommand() {
		if (araShowCommand == null) {
			araShowCommand = new JTextArea();
			araShowCommand.setBounds(196, 171, 223, 84);
		}
		
		return araShowCommand;
	}
	
	public void showError(String errMessage) {
		JOptionPane.showMessageDialog(this, errMessage);
	}
	
	public void setAraShowCommand(String command) {
		araShowCommand.setText(command);
	}
	
	private JComboBox getCmbBlastApp() {
		if (cmbBlastApp == null) {
			String[] items = {"blastn", "blastp", "blastx", "tblastn", "tblastx"};
			cmbBlastApp = new JComboBox(items);
			cmbBlastApp.setBounds(20, 31, 140, 20);
		}
		
		return cmbBlastApp;
	}
	
	private JTextField getTxtFileToUpload() {
		if (txtFileToUpload == null) {
			txtFileToUpload = new JTextField();
			txtFileToUpload.setBounds(20, 87, 140, 20);
		}
		
		return txtFileToUpload;
	}
	
	private JTextField getTxtOutputFileName() {
		if (txtOutputFileName == null) {
			txtOutputFileName = new JTextField();
			txtOutputFileName.setBounds(87, 269, 140, 20);
			txtOutputFileName.setColumns(10);
		}
		
		return txtOutputFileName;
	}
	
	public String getBlastApp() {
		return cmbBlastApp.getSelectedItem().toString();
	}
	
	public String getInputFile() {
		return txtFileToUpload.getText();
	}
	
	public String getOutputFile() {
		return txtOutputFileName.getText();
	}
	
	// creating the submit button and connecting it with the submit action
	private JButton getBtnSubmit() {
		if (btnSubmit == null) {
			btnSubmit = new JButton("Submit");
			btnSubmit.setBounds(330, 266, 89, 23);
			/*
			btnSubmit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					submitJob();
				}
			});*/
		}
		return btnSubmit;
	}
	
	public boolean createsBatchJob() {
		// we are only creating a single job with this panel
		return false;
	}

	public boolean createsSingleJob() {
		// yep
		return true;
	}

	// usually you just return the object itself
	public JPanel getPanel() {
		return this;
	}

	// this is what is going to be displayed in the client on the upper left
	// side
	public String getPanelName() {
		return "mpiBlast";
	}

	// here you return the name of the application package that your client
	// submits jobs for.
	// e.g. Java, Python, MrBayes, ....
	public String getSupportedApplication() {
		return "mpiBlast";
	}

	// convenience method to disable and re-enable the button while the
	// submission is ongoing
	public void lockUI(final boolean lock) {

		SwingUtilities.invokeLater(new Thread() {

			@Override
			public void run() {
				getBtnSubmit().setEnabled(!lock);
			}

		});

	}

	// here we collect the reference to the serviceinterface. Also, usually
	// here's where you do some
	// initialization of your panel (if you need to). E.g. populating a combobox
	// with a list of submission locations or versions of an application
	// package...
	public void setServiceInterface(ServiceInterface si) {
		this.si = si;
	}
	
	public void showError(Exception e) {
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
				Viewer.this.getRootPane(), pane);
	}
	
	private JTextField getTxtDatabase() {
		if (txtDatabase == null) {
			txtDatabase = new JTextField();
			txtDatabase.setBounds(20, 139, 140, 20);
			txtDatabase.setColumns(10);
		}
		return txtDatabase;
	}
	
	public String getDatabase() {
		return txtDatabase.getText();
	}
	
	public String getQuerySubrangeFrom() {
		return txtSubrangeFrom.getText();
	}
	
	public String getQuerySubrangeTo() {
		return txtSubrangeTo.getText();
	}
	
	public String getQueryGencode() {
		return txtGenCode.getText();
	}
	
	public String getEValue() {
		return txtEValue.getText();
	}
	
	public String getMatrix() {
		return txtMatrix.getText();
	}
	
	public boolean getLCaseMask() {
		return chkLowerCase.isSelected();
	}
	public void addSubmitListener(ActionListener sl) {
		btnSubmit.addActionListener(sl);
	}
	
	public void addBlastAppListener(ItemListener bl) {
		cmbBlastApp.addItemListener(bl);
	}
	
	public void addInputFileListener(ActionListener il) {
		txtFileToUpload.addActionListener(il);
	}
	
	public void addOutputFileNameListener(ActionListener il) {
		txtOutputFileName.addActionListener(il);
	}
	
	public void addDatabaseListener(ActionListener il) {
		txtDatabase.addActionListener(il);
	}
	
	public void addQrySubrangeStartListener(ActionListener qryStart) {
		txtSubrangeFrom.addActionListener(qryStart);
	}
	
	public void addQrySubrangeStopListener(ActionListener qryStop) {
		txtSubrangeTo.addActionListener(qryStop);
	}
	
	public void addQryGencodeListener(ActionListener qryStop) {
		txtGenCode.addActionListener(qryStop);
	}
	
	public void addEValueListener(ActionListener ev) {
		txtEValue.addActionListener(ev);
	}
	
	public void addMatrixListener(ActionListener m) {
		txtMatrix.addActionListener(m);
	}
	
	public void addLowerCaseMaskListener(ItemListener lc) {
		chkLowerCase.addItemListener(lc);
	}
	private JTextField getTxtSubrangeFrom() {
		if (txtSubrangeFrom == null) {
			txtSubrangeFrom = new JTextField();
			txtSubrangeFrom.setBounds(237, 31, 161, 20);
			txtSubrangeFrom.setColumns(10);
		}
		return txtSubrangeFrom;
	}
	private JTextField getTxtSubrangeTo() {
		if (txtSubrangeTo == null) {
			txtSubrangeTo = new JTextField();
			txtSubrangeTo.setBounds(237, 59, 161, 20);
			txtSubrangeTo.setColumns(10);
		}
		return txtSubrangeTo;
	}
	private JLabel getLblSubrangeStart() {
		if (lblSubrangeStart == null) {
			lblSubrangeStart = new JLabel("From");
			lblSubrangeStart.setBounds(196, 34, 31, 14);
		}
		return lblSubrangeStart;
	}
	private JLabel getLblSubrangeStop() {
		if (lblSubrangeStop == null) {
			lblSubrangeStop = new JLabel("To");
			lblSubrangeStop.setBounds(204, 62, 23, 14);
		}
		return lblSubrangeStop;
	}
	private JLabel getLblGenCode() {
		if (lblGenCode == null) {
			lblGenCode = new JLabel("Genetic Code");
			lblGenCode.setBounds(186, 87, 89, 14);
		}
		return lblGenCode;
	}
	private JTextField getTxtGenCode() {
		if (txtGenCode == null) {
			txtGenCode = new JTextField();
			txtGenCode.setBounds(258, 87, 140, 20);
			txtGenCode.setColumns(10);
		}
		return txtGenCode;
	}
	private JLabel getLblEValue() {
		if (lblEValue == null) {
			lblEValue = new JLabel("Expect Threshold");
			lblEValue.setBounds(186, 142, 89, 14);
		}
		return lblEValue;
	}
	private JTextField getTxtMatrix() {
		if (txtMatrix == null) {
			txtMatrix = new JTextField();
			txtMatrix.setBounds(20, 195, 140, 20);
			txtMatrix.setColumns(10);
		}
		return txtMatrix;
	}
	private JLabel getLblMatrix() {
		if (lblMatrix == null) {
			lblMatrix = new JLabel("Matrix");
			lblMatrix.setBounds(10, 170, 46, 14);
		}
		return lblMatrix;
	}
}
