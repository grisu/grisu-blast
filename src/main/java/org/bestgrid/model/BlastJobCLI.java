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
	
	public void setQueryLocation(String[] parameters) throws NumberFormatException {
		start = Integer.parseInt(parameters[0]);
		stop = Integer.parseInt(parameters[1]);
	}
	
	public void setGeneticCode(String gencode) throws NumberFormatException {
		this.gencode = Integer.parseInt(gencode);
	}
	
	public void setExpectationValue(String eVal) throws NumberFormatException {
		this.eVal = Integer.parseInt(eVal);
		if(this.eVal < 0) throw new NumberFormatException ("Value specified not valid");
	}
	
	public void setWordSize(String wSize) throws NumberFormatException {
		this.wSize = Integer.parseInt(wSize);
		if(this.wSize < 2) throw new NumberFormatException("word size must be more than 2");
	}
	
	public void setOpenCost(String gO) throws NumberFormatException {
		open = Integer.parseInt(gO);
	}
	
	public void setExtendCost(String gE) throws NumberFormatException {
		extend = Integer.parseInt(gE);
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
	
	public void ifInteger(String toCheck) throws NumberFormatException {
		Integer.parseInt(toCheck);
	}
}