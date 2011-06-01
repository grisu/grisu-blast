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
	private String cstat;
	
	private boolean softMask;
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
	
	public void append(String commandline) {
		String old = this.commandline;
		this.commandline = old + commandline;
		pcs.firePropertyChange("commandline", old, this.commandline);
	}
	
	public String getCommand() {
		return this.commandline;
	}
	
	public void setQueryLocation(String parameters) throws NumberFormatException, Exception {
		//verify input
		if(parameters.indexOf('-') < 0) {
			throw new Exception("Invalid formatting for query_loc");
		} else {
			String[] param = parameters.split("-");
			start = Integer.parseInt(param[0]);
			stop = Integer.parseInt(param[1]);
		}
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
	
	public void setCompBasedStat(String stat) throws Exception {
		//check if correct value
		if(stat.equalsIgnoreCase("d") || stat.equalsIgnoreCase("0") || 
				stat.equalsIgnoreCase("1") || stat.equalsIgnoreCase("2") || 
				stat.equalsIgnoreCase("3") || stat.equalsIgnoreCase("f")) {
			this.cstat = stat;
		} else throw new Exception ("invalid value for composition based statistics");
	}
	
	public void setSEGFilter(String seg) throws Exception {
		//make sure seg arguments are correct
		if(seg.equalsIgnoreCase("yes") ||seg.equalsIgnoreCase("no") ||
				seg.equalsIgnoreCase("window locut hicut")) {
			this.seg = seg;
		} else throw new Exception("invalid seg argument");
	}
	
	public void setSoftMasking(String boolValue) throws Exception{
		//check if boolean
		if(boolValue.equalsIgnoreCase("true") || boolValue.equalsIgnoreCase("false")) {
			this.softMask = Boolean.parseBoolean(boolValue);
		} else throw new Exception("invalid value set for soft_masking");		
	}
	
	public void setLowerCaseFilter(boolean isEnabled) {
		this.lCaseMask = isEnabled;
	}
	
	/*
	public void ifInteger(String toCheck) throws NumberFormatException {
		Integer.parseInt(toCheck);
	}*/
}