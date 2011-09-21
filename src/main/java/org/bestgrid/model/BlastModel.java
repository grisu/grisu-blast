package org.bestgrid.model;

import java.util.Iterator;

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
		
		if(getInputFiles().isEmpty() == false) {
			Iterator iterator = getInputFiles().keySet().iterator();
			while (iterator.hasNext()) {
				String value = getInputFiles().toString();
				System.out.println("setting commandline -i " + value);
				old = this.commandline;
				append = " -i " + value;
				this.commandline = old + append;
			}
		}
		
		//this.commandline = commandline;
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
				addInputFile(line.getOptionValue("i"));
				System.out.println("argument -i " + line.getOptionValue("i"));
			}
			
			if(line.hasOption("p")) {
				setBlast(line.getOptionValue("p"));
				System.out.println("argument -p " + line.getOptionValue("p"));
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
		this.blast = type;
	}
	
	private void setDatabase(String db) {
		this.db = db;
	}
	/*
	public void setFastaFile(String file) {
		
		this.filename = FileManager.getFilename(file);
		this.addInputFile(file);
		
		/*
		GridFile old = this.fastaFile;
		inputFiles.remove(inputFile.getUrl());
		this.fastaFile = inputFile;
		inputFiles.put(inputFile.getUrl(), null);
		pcs.firePropertyChange("fastaFile", old, this.fastaFile);
		pcs.firePropertyChange("inputFiles", null, inputFiles);
	}*/
	@Override
	public String getCommandline() {
		return commandline;
	}
}