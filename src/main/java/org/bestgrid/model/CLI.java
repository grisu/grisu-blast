package org.bestgrid.model;

import org.apache.commons.cli.*;

public class CLI extends AbstractBlastJob {

	private String commandline;

	@Override
	public String getCommandline() {
		return commandline;
	}

	public void setCommandline(String commandline) {
		String old = this.commandline;
		this.commandline = commandline;
		pcs.firePropertyChange("commandline", old, this.commandline);
		
		Options options = new Options();
	}


}