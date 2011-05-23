package org.bestgrid.model;

import org.apache.commons.cli.*;

import grisu.model.FileManager;

public class BlastP extends AbstractBlastJob {

	private String commandline;
	private String db;
	private int eVal = 10;
	private int open = 0;
	private int extend = 0;
	private String matrix;
	private String stats = "2";
	private String seg = "no";

	private Options opt;
	
	public BlastP(String[] arg) {
		opt = createOptions();
		
		/*
		CommandLineParser parser = new GnuParser();
		try {
			CommandLine line = parser.parse(options, arg);
			
			if(line.hasOption("seg")) {
				this.setSEG(line.getOptionValue("seg"));
			} else if(line.hasOption("comp_based_stats")) {
				this.setStats(line.getOptionValue("stats"));
			} else if(line.hasOption("matrix")) {
				this.setStats(line.getOptionValue("matrix"));
			}
		} 
		catch(ParseException exp) {
			System.err.println("Parsing failed. Reason: " + exp.getMessage());
		}
		
		*/
		
	}
	
	private Options createOptions() {
		Option seg = OptionBuilder.create("seg");
		OptionBuilder.withArgName("seg");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("Filter query sequence with SEG " +
				"(Format: 'yes', 'window locut hicut', or 'no' to disable");
		
		Option lcase_masking = new Option("lcase_masking", 
				"Use lower case filtering in query and subject sequences");
		
		Option comp_based_stats = OptionBuilder.create("stats");
		OptionBuilder.withArgName("comp_based_stats");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("Use composition-based statistics" +
				"D or d: default (equivalent to 2)" +
				"0 or F or f: no composition-based statistics" +
				"1: Composition-based statistics as in NAR 29:2994-3005, 2001" +
				"2 or T or t: Composition-based score adjustment as in " +
				"Bioinformatics 21:902-911, 2005 conditioned on sequence properties" +
				"3: Composition-based score adjustment as in " +
				"Bioinformatics 21:902-911, 2005 unconditionally.");
		
		Option matrix = OptionBuilder.create("matrix");
		OptionBuilder.withArgName("matrix");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("Scoring matrix name");
		
		Options options = new Options();
		
		options.addOption(lcase_masking);
		options.addOption(seg);
		options.addOption(comp_based_stats);
		options.addOption(matrix);
		
		return options;
	}
	
	@Override
	public String getCommandline() {
		return commandline;
	}

	public void setCommandline(String commandline) {
		String old = this.commandline;
		this.commandline = commandline;
		pcs.firePropertyChange("commandline", old, this.commandline);
	}

	public void setDB(String db) {
		String old = this.db;
		this.db = db;
		pcs.firePropertyChange("database", old, this.db);
	}
	
	public String getDB() {
		return db;
	}
	
	public void setEValue(int eVal) {
		//int old = this.eVal;
		this.eVal = eVal;
	}
	
	public int getEvalue() {
		return eVal;
	}
	
	public int setWordSize(int s) {
		if(s < 2) {
			System.out.println("word size have to be greater than or equal to 2");
			System.out.println("word size of 2 will be used");
			this.eVal = 2;
			return eVal;
		} else {
			this.eVal = s;
			return eVal;
		}
	}
	
	public void setGapOpen(int cost) {
		this.open = cost;
	}
	
	public int getGapOpen() {
		return open;
	}
	
	public void setGapExtend(int cost) {
		this.extend = cost;
	}
	
	public int getGapExtend() {
		return extend;
	}
	
	public void setMatrix(String matrix) {
		this.matrix = matrix;
	}
	
	public String getMatrix() {
		return matrix;
	}
	
	public void setStats(String stats) {
		if(stats.equals("D") || stats.equals("F") 
				|| stats.equals("0") || stats.equals("2")) {
			this.stats = stats;
		} else {
			System.out.println("This blast type does not support this type of");
			System.out.println("composition-based statistics");
			this.stats = "2";
		}
	}
	
	public String getStats() {
		return this.stats;
	}
	
	public void setSEG(String seg) {
		if(seg.equals("yes") || seg.equals("window locut hicut") 
				|| seg.equals("no")) {
			this.seg = seg;
		} else {
			System.out.println("Option selected is not valid");
			System.out.println("Using default SEG value");
			this.seg = "no";
		}
	}
	
	public String getSEG() {
		return this.seg;
	}
	
	public void setLCaseMask() {
		return;
	}
}