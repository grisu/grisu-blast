package org.bestgrid.view;

import grisu.control.ServiceInterface;
import grisu.frontend.view.swing.DefaultFqanChangePanel;
import grisu.frontend.view.swing.jobcreation.JobCreationPanel;
import grisu.frontend.view.swing.jobcreation.widgets.Cpus;
import grisu.frontend.view.swing.jobcreation.widgets.SingleInputGridFile;
import grisu.frontend.view.swing.jobcreation.widgets.SubmissionLocationPanel;
import grisu.frontend.view.swing.jobcreation.widgets.TextCombo;
import grisu.frontend.view.swing.jobcreation.widgets.TextField;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.bestgrid.MpiBlast;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class BlastSubmissionPanel extends JPanel implements JobCreationPanel {
	private TextCombo textCombo;
	public static final String[] BLAST_TYPES = {"blastn", "blastp", "blastx", "tblastn", "tblastx"};
	private TextField fromSubrangeField;
	private JPanel panel;
	private TextField toSubrangeField;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private TextField textField;
	private SingleInputGridFile singleInputGridFile;
	private TextField textField_1;
	private TextField textField_2;
	private TextField textField_3;
	private JSeparator separator;
	private Cpus cpus;
	private SubmissionLocationPanel submissionLocationPanel;
	private JButton btnNewButton;
	private DefaultFqanChangePanel defaultFqanChangePanel;

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
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));
		add(getTextCombo(), "2, 2, 3, 1, fill, fill");
		add(getPanel_1(), "6, 2, 3, 3, fill, fill");
		add(getTextField(), "2, 4, 3, 1, fill, fill");
		add(getSingleInputGridFile(), "2, 6, 7, 1, fill, fill");
		add(getTextField_1(), "2, 8, fill, fill");
		add(getTextField_2(), "4, 8, fill, fill");
		add(getTextField_3(), "6, 8, 3, 1, fill, fill");
		add(getSeparator(), "2, 10, 7, 1");
		add(getCpus(), "2, 12, fill, fill");
		add(getSubmissionLocationPanel(), "4, 12, 5, 1, fill, fill");
		add(getDefaultFqanChangePanel(), "2, 14, 3, 1, fill, fill");
		add(getBtnNewButton(), "8, 14, right, default");

	}

	public boolean createsBatchJob() {
		return false;
	}

	public boolean createsSingleJob() {
		return true;
	}

	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("Submit");
		}
		return btnNewButton;
	}

	private Cpus getCpus() {
		if (cpus == null) {
			cpus = new Cpus();
		}
		return cpus;
	}

	private DefaultFqanChangePanel getDefaultFqanChangePanel() {
		if (defaultFqanChangePanel == null) {
			defaultFqanChangePanel = new DefaultFqanChangePanel();
		}
		return defaultFqanChangePanel;
	}

	private TextField getFromSubrangeField() {
		if (fromSubrangeField == null) {
			fromSubrangeField = new TextField();
		}
		return fromSubrangeField;
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

	public JPanel getPanel() {
		return this;
	}

	private JPanel getPanel_1() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Query Subrange", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			panel.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("default:grow"),
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("default:grow"),},
					new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,}));
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
		}
		return singleInputGridFile;
	}

	private SubmissionLocationPanel getSubmissionLocationPanel() {
		if (submissionLocationPanel == null) {
			submissionLocationPanel = new SubmissionLocationPanel();
			submissionLocationPanel.setApplication("mpiblast");
			submissionLocationPanel
					.setVersion(MpiBlast.MPI_BLAST_DEFAULT_VERSION);
		}
		return submissionLocationPanel;
	}

	public String getSupportedApplication() {
		return "mpiBlast";
	}

	private TextCombo getTextCombo() {
		if (textCombo == null) {
			textCombo = new TextCombo();
			textCombo.setTitle("Blast type");
			textCombo.setSelectionValues(BLAST_TYPES);
		}
		return textCombo;
	}

	private TextField getTextField() {
		if (textField == null) {
			textField = new TextField();
			textField.setTitle("Genetic code");
		}
		return textField;
	}

	private TextField getTextField_1() {
		if (textField_1 == null) {
			textField_1 = new TextField();
			textField_1.setTitle("Database");
		}
		return textField_1;
	}

	private TextField getTextField_2() {
		if (textField_2 == null) {
			textField_2 = new TextField();
			textField_2.setTitle("Matrix");
		}
		return textField_2;
	}

	private TextField getTextField_3() {
		if (textField_3 == null) {
			textField_3 = new TextField();
			textField_3.setTitle("Expect Threshold");
		}
		return textField_3;
	}

	private TextField getToSubrangeField() {
		if (toSubrangeField == null) {
			toSubrangeField = new TextField();
		}
		return toSubrangeField;
	}

	public void setServiceInterface(ServiceInterface si) {

		try {
			getDefaultFqanChangePanel().setServiceInterface(si);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		getSubmissionLocationPanel().setServiceInterface(si);
		getSingleInputGridFile().setServiceInterface(si);

	}
}
