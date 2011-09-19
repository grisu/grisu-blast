package org.bestgrid.model;

import grisu.control.ServiceInterface;
import grisu.frontend.model.job.JobObject;
import grisu.model.dto.GridFile;
import grisu.model.FileManager;

import org.apache.commons.cli.*;

public class BlastModel extends AbstractBlastJob{
	private double accumulator, opr; 
	private char op;
	
	private String db = null;
	private String blast = null;
	private String filename = null;
	
	private String commandline;
	
	public BlastModel() { 
		
		//accumulator = 0.0;
		
	} 
	
	public void setCommandline() {		
		String old = this.commandline;
		String commandline = null;
		
		if(db != null) {
			commandline = " -d " + getDatabase();
			this.commandline = old + commandline;
		}
		
		if(blast != null) {
			commandline = " -p " + getBlast();
			this.commandline = old + commandline;
		}
		
		if(filename != null) {
			commandline = " -i " + getFastaFile().getName();
			this.commandline = old + commandline;
		}
		
		this.commandline = commandline;
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
			}
			
			if(line.hasOption("i")) {
				setFastaFile(line.getOptionValue("i"));
			}
			
			if(line.hasOption("p")) {
				setBlast(line.getOptionValue("p"));
			}
			
		} catch (Exception e) {
			System.err.println(e);
		}			
	}
	
	public String getModel() { return accumulator+""; }
	
	private Options getOptions() {
		Options opt = new Options();  
				
		opt.addOption("d", true, "the database to use");
		opt.addOption("i", true, "query file");
		opt.addOption("p", true, "blast program name");

		return opt;
	}
	
	private String getBlast() {
		return blast;
	}
	
	private String getDatabase() {
		return db;
	}
	
	private void setBlast(String type) {
		blast = type;
	}
	
	private void setDatabase(String db) {
		this.db = db;
	}
	
	public void setFastaFile(String file) {
		
		this.filename = FileManager.getFilename(file);
		this.addInputFile(file);
		
		/*
		GridFile old = this.fastaFile;
		inputFiles.remove(inputFile.getUrl());
		this.fastaFile = inputFile;
		inputFiles.put(inputFile.getUrl(), null);
		pcs.firePropertyChange("fastaFile", old, this.fastaFile);
		pcs.firePropertyChange("inputFiles", null, inputFiles);*/
	}
	@Override
	public String getCommandline() {
		return commandline;
	}
}