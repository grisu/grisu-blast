package org.bestgrid;

public class BlastJobCLI extends AbstractBlastJob {

	private String commandline;

	@Override
	public String getCommandline() {
		return commandline;
	}

	public void setCommandline(String commandline) {
		String old = this.commandline;
		this.commandline = commandline;
		pcs.firePropertyChange("commandline", old, this.commandline);
	}


}
