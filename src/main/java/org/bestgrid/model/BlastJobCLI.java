package org.bestgrid.model;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class BlastJobCLI extends AbstractBlastJob {

	private String commandline;
	private Options options;
	
	@Override
	public String getCommandline() {
		return commandline;
	}

	public void setCommandline(String commandline) {		
		String old = this.commandline;
		this.commandline = commandline;
		pcs.firePropertyChange("commandline", old, this.commandline);		
	}

	public void configureBlast(String[] args) {
		createOptions();
		
		//getting arguments for job specification
		CommandLineParser parser = new GnuParser();
		
		try {
			CommandLine line = parser.parse(options, args);			
			
			if(line.hasOption("blastp")) setApplication("blastp");
			else if(line.hasOption("blastn")) setApplication("blastn");
			else if(line.hasOption("blastx")) setApplication("blastx");
			else if(line.hasOption("tblastn")) setApplication("tblastn");
			else if(line.hasOption("tblastx")) setApplication("tblastx");
		
			if(line.hasOption("seg")) {
				System.out.println("seg " + line.getOptionValue("seg"));
			} 
			
			if(line.hasOption("comp_based_stats")) {
				System.out.println("comp_based_stats " + line.getOptionValue("stats"));
			} 
			if(line.hasOption("matrix")) {
				System.out.println("matrix " + line.getOptionValue("matrix"));
			}	
		} 
		catch(ParseException exp) {
			System.err.println("Parsing failed. Reason: " + exp.getMessage());
		}
		
	}
	
	public void setApplication(String app) {
		//if(app.equalsIgnoreCase("blastp")) new BlastP;
		/* TODO other applications */
	}
	public Options createOptions() {
		Options opt = new Options(); 
		
		opt.addOption("blastp", false, "use blastp for this query");
		opt.addOption("blastn", false, "use blastn for this query");
		opt.addOption("blastx", false, "use blastx for this query");
		opt.addOption("tblastn", false, "use tblastp for this query");
		opt.addOption("tblastx", false, "use tblastx for this query");
		opt.addOption("seg", true, "Filter query sequence with SEG " +
				"(Format: 'yes', 'window locut hicut', or 'no' to disable");  
		opt.addOption("lcase_masking", false, 
				"Use lower case filtering in query and subject sequences");
		opt.addOption("comp_based_stats", true, 
				"Use composition-based statistics" +
				"D or d: default (equivalent to 2)" +
				"0 or F or f: no composition-based statistics" +
				"1: Composition-based statistics as in NAR 29:2994-3005, 2001" +
				"2 or T or t: Composition-based score adjustment as in " +
				"Bioinformatics 21:902-911, 2005 conditioned on sequence properties" +
				"3: Composition-based score adjustment as in " +
				"Bioinformatics 21:902-911, 2005 unconditionally.");
		opt.addOption("matrix", true, "Scoring matrix name");
		
		return this.options = opt;
	}
}
