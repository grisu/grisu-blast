package org.bestgrid.model;

public class BlastJobCLI extends AbstractBlastJob {

	private String commandline;
	//private Options options;
	
	//query locations
	private int start;
	private int stop;
	
	private int gencode;
	private int eVal;
	private int wSize;
	private int open;
	private int extend;
	
	private String matrix;
	private String seg;
	
	private boolean lCaseMask;
	
	@Override
	public String getCommandline() {
		return commandline;
	}

	public void setCommandline(String commandline) {		
		String old = this.commandline;
		this.commandline = commandline;
		pcs.firePropertyChange("commandline", old, this.commandline);		
	}
	
	public void addCommand(String commandline) {
		this.commandline = this.commandline + commandline;
	}
	
	public String getCommand() {
		return this.commandline;
	}
	
	public void setQueryLocation(String[] parameters) {
		start = Integer.parseInt(parameters[0]);
		stop = Integer.parseInt(parameters[1]);
	}
	
	public void setGeneticCode(int gencode) {
		this.gencode = gencode;
	}
	
	public void setExpectationValue(int eVal) {
		this.eVal = eVal;
	}
	
	public void setWordSize(int wSize) {
		this.wSize = wSize;
	}
	
	public void setOpenCost(int gO) {
		open = gO;
	}
	
	public void setExtendCost(int gE) {
		extend = gE;
	}
	
	public void setMatrix(String matrix) {
		this.matrix = matrix;
	}
	
	public void setSEGFilter(String seg) {
		this.seg = seg;
	}
	
	public void setLowerCaseFilter(boolean isEnabled) {
		this.lCaseMask = isEnabled;
	}
}