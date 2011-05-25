package org.bestgrid.model;

public class BlastJobCLI extends AbstractBlastJob {

	private String commandline;
	//private Options options;
	
	//query locations
	private int start;
	private int stop;
	
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
	
}
