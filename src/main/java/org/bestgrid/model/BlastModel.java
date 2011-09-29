package org.bestgrid.model;

import grisu.control.exceptions.RemoteFileSystemException;

//import java.util.Iterator;

import org.apache.commons.cli.*;

public class BlastModel extends AbstractBlastJob{
	//private double accumulator, opr; 
	//private char op;
	
	private String db = null;
	private String blast = null;
	private String output = null;
	//private String filename = null;
	
	private String commandline;
	
	public BlastModel() { 
		
		//accumulator = 0.0;
		
	} 
	
	public void constructCommand() {		
		commandline ="";
		String old = this.commandline;
		String append;
		
		if(db != null) {
			System.out.println("setting commandline -d " + getDatabase());
			old = this.commandline;
			append = " -d " + getDatabase();
			this.commandline = old + append;
		}
		
		if(blast != null) {
			System.out.println("setting commandline -p " + getBlast());
			old = this.commandline;
			append = " -p " + getBlast();
			this.commandline = old + append;
		}
		
		try {
			if(fm.fileExists(getFastaFile())) {
				System.out.println("setting commandline -i " + getFastaFile().getName());
				old = this.commandline;
				append = " -o " + getOutput();
				this.commandline = old + append;
			}
		}
		catch (RemoteFileSystemException rfse) {
			System.out.println(rfse.getMessage());
			rfse.printStackTrace();
		}
		
		if(output != null) {
			System.out.println("setting commandline -o " + getOutput());
			old = this.commandline;
			append = " -i " + getFastaFile().getName();
			this.commandline = old + append;
		}

		pcs.firePropertyChange("commandline", old, this.commandline);		
	}
	
	public void setModel(String[] c) { 
		Options options = getOptions();
		
		//getting arguments for job specification
		CommandLineParser parser = new PosixParser();
		
		try {
			System.out.println("parsing");
			CommandLine line = parser.parse(options, c);			

			if(line.hasOption("d")) {
				setDatabase(line.getOptionValue("d"));
				System.out.println("argument -d " + line.getOptionValue("d"));
			}
			
			if(line.hasOption("i")) {
				setFastaFile(line.getOptionValue("i"));
				System.out.println("argument -i " + line.getOptionValue("i"));
			}
			
			if(line.hasOption("p")) {
				setBlast(line.getOptionValue("p"));
				System.out.println("argument -p " + line.getOptionValue("p"));

			}
			
			if(line.hasOption("o")) {
				System.out.println("argument -o " + line.getOptionValue("o"));
				setOutput(line.getOptionValue("o"));
			}
			
		} catch (Exception e) {
			System.err.println(e);
		}			
	}
	
	public String getModel() { return blast; }
	
	private Options getOptions() {
		Options opt = new Options();  
				
		opt.addOption("d", true, "the database to use");
		opt.addOption("i", true, "query file");
		opt.addOption("p", true, "blast program name");
		opt.addOption("o", true, "the output file");

		return opt;
	}
	
	private String getBlast() {
		return blast;
	}
	
	private String getDatabase() {
		return db;
	}
	
	private String getOutput() {
		return output;
	}
	
	private void setBlast(String type) {
		this.blast = type;
	}
	
	private void setDatabase(String db) {
		this.db = db;
	}
	
	private void setOutput(String fileName) {
		this.output = fileName;
	}
	
	@Override
	public String getCommandline() {
		return commandline;
	}
}