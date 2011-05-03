package org.bestgrid;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.RemoteFileSystemException;
import grisu.frontend.model.job.JobObject;
import grisu.model.FileManager;
import grisu.model.GrisuRegistryManager;
import grisu.model.dto.GridFile;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBlastJob {

	public static final String DEFAULT_BLAST_JOBNAME = "blast";

	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private int cpus = 1;
	private int walltimeInSeconds = 600;
	private GridFile fastaFile;

	private String vo;

	private ServiceInterface si;

	private String jobname = DEFAULT_BLAST_JOBNAME;

	private String submissionLocation = null;

	private final Map<String, String> inputFiles = new HashMap<String, String>();

	private FileManager fm;
	protected void addInputFile(String url) {
		addInputFile(url, null);
	}

	protected void addInputFile(String url, String remotePath) {
		inputFiles.put(url, remotePath);
		pcs.firePropertyChange("inputFiles", null, inputFiles);
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	public JobObject createJobObject() {
		if (si == null) {
			throw new RuntimeException(
			"ServiceInterface not initialized yet...");
		}
		JobObject blastJob = new JobObject(si);
		blastJob.setTimestampJobname(getJobname());
		blastJob.setWalltimeInSeconds(getWalltimeInSeconds());
		blastJob.setCpus(getCpus());
		blastJob.setSubmissionLocation(getSubmissionLocation());
		// TODO maybe we need to switch between blast and mpiblast here?
		blastJob.setApplication("mpiBLAST");
		//blastJob.setForce_mpi(true);

		blastJob.setCommandline(getCommandline());

		// input files
		for (String url : getInputFiles().keySet()) {
			blastJob.addInputFileUrl(url, getInputFiles().get(url));
		}

		return blastJob;

	}

	abstract public String getCommandline();

	public int getCpus() {
		return cpus;
	}

	public GridFile getFastaFile() {
		return fastaFile;
	}

	public Map<String, String> getInputFiles() {
		return this.inputFiles;
	}

	public String getJobname() {
		return jobname;
	}

	public ServiceInterface getServiceInterface() {
		return this.si;
	}

	public String getSubmissionLocation() {
		return submissionLocation;
	}

	public String getVo() {
		return vo;
	}

	public int getWalltimeInSeconds() {

		return walltimeInSeconds;
	}

	public void removeAllInputFiles() {

		inputFiles.clear();

	}

	protected void removeInputFile(String url) {
		inputFiles.remove(url);
		pcs.firePropertyChange("inputFiles", null, inputFiles);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}

	public void setCpus(int cpus) {
		int old = this.cpus;
		this.cpus = cpus;
		pcs.firePropertyChange("cpus", old, this.cpus);
	}

	public void setFastaFile(GridFile inputFile) {
		GridFile old = this.fastaFile;
		inputFiles.remove(inputFile.getUrl());
		this.fastaFile = inputFile;
		inputFiles.put(inputFile.getUrl(), null);
		pcs.firePropertyChange("fastaFile", old, this.fastaFile);
		pcs.firePropertyChange("inputFiles", null, inputFiles);
	}

	public void setFastaFile(String value) throws RemoteFileSystemException {

		GridFile file = fm.createGridFile(value);
		setFastaFile(file);
	}

	public void setJobname(String jobname) {
		String old = this.jobname;
		this.jobname = jobname;
		pcs.firePropertyChange("jobname", old, this.jobname);
	}

	public void setServiceInterface(ServiceInterface si) {
		this.si = si;
		this.fm = GrisuRegistryManager.getDefault(si).getFileManager();
	}

	public void setSubmissionLocation(String submissionLocation) {
		String old = this.submissionLocation;
		this.submissionLocation = submissionLocation;
		pcs.firePropertyChange("submissionLocation", old,
				this.submissionLocation);
	}

	public void setVo(String vo) {
		String old = this.vo;
		this.vo = vo;
		pcs.firePropertyChange("vo", old, this.vo);
	}

	public void setWalltimeInSeconds(int walltimeInSeconds) {
		int old = this.walltimeInSeconds;
		this.walltimeInSeconds = walltimeInSeconds;
		pcs.firePropertyChange("walltimeInSeconds", old, this.walltimeInSeconds);
	}

}
