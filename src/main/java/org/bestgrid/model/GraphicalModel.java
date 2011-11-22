package org.bestgrid.model;

import grisu.control.exceptions.NoSuchJobException;
import grisu.control.exceptions.RemoteFileSystemException;
import grisu.frontend.model.job.JobObject;

//import java.util.Iterator;

import org.apache.commons.cli.*;

public class GraphicalModel extends AbstractBlastJob{
	//private double accumulator, opr; 
	//private char op;
	
	private String db = null;
	private String blast = null;
	private String output = null;
	private String filename = null;
	
	//Query Locations
	private String start = null;
	private String stop = null;
	
	private String gencode = null;
	private String eValue = null;
	private String matrix = null;
	private String seg = null;
	private String softMask = null;
	private boolean lCaseMask = false;
	private String wordSize = null;
	
	//gap in search query
	private String open = null;
	private String extend = null;
	
	private String stats = null;
	private String entrez = null;
	
	private String commandline;
	
	public GraphicalModel() { 
		
		//accumulator = 0.0;
		
	} 
	
	//for the gui
	public void createCommand() {
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
		
		if(filename != null) {
			System.out.println("setting commandline -i " + filename);
			old = this.commandline;
			append = " -i " + filename;
			this.commandline = old + append;
		}
		
		if(output != null) {
			System.out.println("setting commandline -o " + getOutput());
			old = this.commandline;
			append = " -o " + this.output;
			this.commandline = old + append;
		}
		
		//optional parameters
		if(start != null && stop != null) {
			System.out.println("setting commandline -query_loc start=" + start + " stop=" + stop);
			old = this.commandline;
			append = " -query_loc " + getQueryLocation();
			this.commandline = old + append;
		} 
		
		if(gencode != null) {
			System.out.println("setting commandline -query_gencode " + getQueryGencode());
			old = this.commandline;
			append = " -query_gencode " + getQueryGencode();
			this.commandline = old + append;
		}
		
		if(eValue != null) {
			System.out.println("setting commandline -evalue " + getEValue());
			old = this.commandline;
			append = " -evalue " + getEValue();
			this.commandline = old + append;
		}
		
		if(matrix != null) {
			System.out.println("setting commandline -matrix " + getMatrix());
			old = this.commandline;
			append = " -matrix " + getMatrix();
			this.commandline = old + append;
		}
		
		if(lCaseMask) {
			System.out.println("setting commandline -lcase_masking");
			old = this.commandline;
			append = " -lcase_masking ";
			this.commandline = old + append;
		}
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
		
		if(start != null && stop != null) {
			System.out.println("setting commandline -query_loc start=" + start + " stop=" + stop);
			old = this.commandline;
			append = " -query_loc " + getQueryLocation();
			this.commandline = old + append;
		}
		
		if(gencode != null) {
			System.out.println("setting commandline -query_gencode " + getQueryGencode());
			old = this.commandline;
			append = " -query_gencode " + getQueryGencode();
			this.commandline = old + append;
		}
		
		if(eValue != null) {
			System.out.println("setting commandline -evalue " + getEValue());
			old = this.commandline;
			append = " -evalue " + getEValue();
			this.commandline = old + append;
		}
		
		if(matrix != null) {
			System.out.println("setting commandline -matrix " + getMatrix());
			old = this.commandline;
			append = " -matrix " + getMatrix();
			this.commandline = old + append;
		}
		
		if(seg != null) {
			System.out.println("setting commandline -seg " + getSeg());
			old = this.commandline;
			append = " -seg " + getSeg();
			this.commandline = old + append;
		}
		
		if(softMask != null) {
			System.out.println("setting commandline -soft_masking " + getSoftMask());
			old = this.commandline;
			append = " -soft_masking " + getSoftMask();
			this.commandline = old + append;
		}
		
		if(lCaseMask) {
			System.out.println("setting commandline -lcase_masking");
			old = this.commandline;
			append = " -lcase_masking ";
			this.commandline = old + append;
		}
		
		if(wordSize != null) {
			System.out.println("setting commandline -word_size " + getWordSize());
			old = this.commandline;
			append = " -word_size " + getWordSize();
			this.commandline = old + append;
		}
		
		if(open != null) {
			System.out.println("setting commandline -gapopen " + getGapOpen());
			old = this.commandline;
			append = " -gapopen " + getGapOpen();
			this.commandline = old + append;
		}
		
		if(extend != null) {
			System.out.println("setting commandline -gapextend " + getGapExtend());
			old = this.commandline;
			append = " -gapextend " + getGapExtend();
			this.commandline = old + append;
		}
		
		if(stats != null) {
			System.out.println("setting commandline -comp_based_stats " + getCompBasedStats());
			old = this.commandline;
			append = " -comp_based_stats " + getCompBasedStats();
			this.commandline = old + append;
		}
		
		if(filename != null) {
			System.out.println("input file defined... setting up");
			try {
				System.out.println("trying...");
				setFastaFile(filename);
				if(fm.fileExists(getFastaFile())) {
					System.out.println("checking file existence...");
					System.out.println("setting commandline -i " + getFastaFile().getName());
					old = this.commandline;
					append = " -i " + getFastaFile().getName();
					this.commandline = old + append;
				}
			} catch (RemoteFileSystemException rfse) {
				System.out.println(rfse.getMessage());
				rfse.printStackTrace();
			}
		}
		
		if(output != null) {
			System.out.println("setting commandline -o " + getOutput());
			old = this.commandline;
			append = " -o " + this.output;
			this.commandline = old + append;
		}
		
		if(entrez != null) {
			System.out.println("setting commandline -entrez_query " + getEntrezQuery());
			old = this.commandline;
			append = " -entrez_query " + entrez;
			this.commandline = old + append;
		}

		pcs.firePropertyChange("commandline", old, this.commandline);		
	}
	
	public void constructProperties(JobObject jo) throws NoSuchJobException {
		if(db != null) {
			jo.getServiceInterface().addJobProperty(jo.getJobname(), "DATABASE_KEY", db);
		}
		
		if(blast != null) {
			jo.getServiceInterface().addJobProperty(jo.getJobname(), "APPLICATION_KEY", blast);
		}
		
		if(output != null) {
			jo.getServiceInterface().addJobProperty(jo.getJobname(), "OUTPUT_FILE_KEY", output);
			System.out.println("Adding property OUTPUT_FILE_KEY to " + 
					jo.getJobname() + " as " + output);
		}
		
		if(start != null && stop != null) {
			jo.getServiceInterface().addJobProperty(jo.getJobname(), "QUERY_LOC_KEY", "start=" + start + " stop=" + stop);
		}
	}
	
	public void setModel(String[] c) { 
		Options options = getOptions();
		
		//getting arguments for job specification
		CommandLineParser parser = new GnuParser();
		
		try {
			System.out.println("parsing");
			CommandLine line = parser.parse(options, c);			

			if(line.hasOption("d")) {
				setDatabase(line.getOptionValue("d"));
				System.out.println("argument -d " + line.getOptionValue("d"));
			}
			
			if(line.hasOption("i")) {
				setInputFile(line.getOptionValue("i"));
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
			
			//optional blast options following
			if(line.hasOption("query_loc")) {
				System.out.println("argument -query_loc " + line.getOptionValue("query_loc"));
				setQueryLocation(line.getOptionValue("query_loc"));
			}
			
			if(line.hasOption("query_gencode")) {
				System.out.println("argument -query_gencode " + line.getOptionValue("query_gencode"));
				try {
					setQueryGencode(line.getOptionValue("query_gencode"));
				} catch (NumberFormatException nfe) {
					System.out.println("-query_gencode argument failed. Reason: " + nfe.getMessage());
				}
			}
			
			if(line.hasOption("evalue")) {
				System.out.println("argument -evalue " + line.getOptionValue("evalue"));
				try {
					setEValue(line.getOptionValue("evalue"));
				} catch (NumberFormatException nfe) {
					System.out.println("-evalue argument failed. Reason: " + nfe.getMessage());
				}
			}
			
			if(line.hasOption("matrix")) {
				System.out.println("argument -matrix " + line.getOptionValue("matrix"));
				setMatrix(line.getOptionValue("matrix"));
			}
			
			if(line.hasOption("seg")) {
				System.out.println("argument -seg " + line.getOptionValue("seg"));
				setSeg(line.getOptionValue("seg"));
			}
			
			if(line.hasOption("soft_masking")) {
				System.out.println("argument -soft_masking " + line.getOptionValue("soft_masking"));
				try {
					setSoftMask(line.getOptionValue("soft_masking"));
				} catch (IllegalArgumentException iae) {
					System.out.println("-soft_masking argument failed. Reason: " + iae.getMessage());
				}
			}
			
			if(line.hasOption("lcase_masking")) {
				System.out.println("argument -lcase_masking ");
				setLCaseMask(true);
			}
			
			if(line.hasOption("word_size")) {
				System.out.println("argument -word_size " + line.getOptionValue("word_size"));
				try {
					setWordSize(line.getOptionValue("word_size"));
				} catch (Exception e) {
					System.out.println("-word_size argument failed. Reason: " + e.getMessage());
				}
			}
			
			if(line.hasOption("gapopen")) {
				System.out.println("argument -gapopen " + line.getOptionValue("gapopen"));
				try {
					setGapOpen(line.getOptionValue("gapopen"));
				} catch (NumberFormatException nfe) {
					System.out.println("-gapopen argument failed. Reason: " + nfe.getMessage());
				}
			}
			
			if(line.hasOption("gapextend")) {
				System.out.println("argument -gapextend " + line.getOptionValue("gapextend"));
				try {
					setGapExtend(line.getOptionValue("gapextend"));
				} catch (NumberFormatException nfe) {
					System.out.println("-gapextend argument failed. Reason: " + nfe.getMessage());
				}
			}
			
			if(line.hasOption("entrez_query")) {
				System.out.println("argument -entrez_query " + line.getOptionValue("entrez_query"));
				setEntrezQuery(line.getOptionValue("entrez_query"));
			}
			
			if(line.hasOption("comp_based_stats")) {
				System.out.println("argument -comp_based_stats " + line.getOptionValue("comp_based_stats"));
				setCompBasedStats(line.getOptionValue("comp_based_stats"));
			}
		} catch (Exception e) {
			System.err.println(e);
		}			
	}
	
	public String getModel() { return blast; }
	
	private Options getOptions() {
		Options opt = new Options();  
		
		/* TODO is extension options in the manual correlate to Scoring parameters in UI for blastn*/
		
		opt.addOption("d", true, "the database to use");
		opt.addOption("i", true, "query file");
		opt.addOption("p", true, "blast program name");
		opt.addOption("o", true, "the output file");
		opt.addOption("query_loc", true, "location on the query sequence in 1-based offsets"); //tblastx, blastp, blastn, blastx, tblastn
		opt.addOption("query_gencode", true, "genetic code to use to translate query"); //tblastx, blastx
		opt.addOption("evalue", true, "expectation value (E) threshold for saving hits"); //tblastx, blastp, blastn, blastx, tblastn
		opt.addOption("matrix", true, "scoring matrix name"); //tblastx, blastp, blastx, tblastn
		opt.addOption("seg", true, "filter query sequence with SEG"); //tblastx, blastp, blastx, tblastn
		opt.addOption("soft_masking", true, "apply filtering locations as soft masks"); //tblastx, tblastn
		opt.addOption("lcase_masking", false, "enable/disable lower case filtering in query and subject sequences"); //tblastx, blastp, blastn, blastx, tblastn
		opt.addOption("word_size", true, "word size for wordfinder algorithm"); //blastp, blastn, blastx, tblastn
		opt.addOption("gapopen", true, "cost to open a gap"); //blastp, blastn, blastx, tblastn
		opt.addOption("gapextend", true, "cost to extend a gap"); //blastp, blastn, blastx, tblastn
		opt.addOption("comp_based_stats", true, "use composition-based statistics for blastp/tblastn"); //blastp, tblastn
		opt.addOption("entrez_query", true, "restrict search with the given Entrez query"); //blastn
		
		return opt;
	}
	
	public void setInputFile(String name) {
		this.filename = name;
	}
	
	private String getInputFile() {
		return this.filename;
	}
	
	private void setEntrezQuery(String entrez) {
		this.entrez = entrez;
	}
	
	private String getEntrezQuery() {
		return this.entrez;
	}
	
	private void setCompBasedStats(String stats) {
		this.stats = stats;
		/*TODO check data being passed */
	}
	
	private String getCompBasedStats() {
		return this.stats;
	}
	
	private void setGapOpen(String open) throws NumberFormatException {
		Integer.parseInt(open); //check if integer
		this.open = open;
	}
	
	private void setGapExtend(String extend) throws NumberFormatException {
		Integer.parseInt(extend);
		this.extend = extend;
	}
	
	private String getGapOpen() {
		return open;
	}
	
	private String getGapExtend() {
		return extend;
	}
	
	private void setWordSize(String size) throws NumberFormatException, IllegalArgumentException {
		Integer.parseInt(size);
		if(Integer.parseInt(size) >= 2) {
			this.wordSize = size;
		}
		else throw new IllegalArgumentException("word size is less than 2");
	}
	
	private String getWordSize() {
		return this.wordSize;
	}

	public void setLCaseMask(boolean b) {
		lCaseMask = b;
	}

	private void setSoftMask(String bool) throws IllegalArgumentException {
		if (bool.equalsIgnoreCase("true") || bool.equalsIgnoreCase("false")) {
		    softMask = Boolean.valueOf(bool).toString();  
		} else {
		    throw new IllegalArgumentException("the string is not boolean");
		}
	}

	private String getSoftMask() {
		return softMask;
	}

	private void setSeg(String seg) {
			this.seg = seg;
	}

	private String getSeg() {
		return seg;
	}

	public void setMatrix(String matrix) {
		this.matrix = matrix;
	}

	private String getMatrix() {
		return matrix;
	}
	
	public void setEValue(String value) throws NumberFormatException {
		Integer.parseInt(value);
		eValue = value;
	}
	
	private String getEValue() {
		return eValue;
	}
	
	public void setQueryGencode(String gencode) throws NumberFormatException {
		Integer.parseInt(gencode);
		this.gencode = gencode;
	}
	
	private String getQueryGencode() {
		return gencode;
	}
	
	//if from gui
	public void setQueryFrom(String from) {
		start = from;
	}
	public void setQueryTo(String to) {
		stop = to;
	}
	
	private void setQueryLocation(String startstop) {
		String delims = "[-]";
		String[] qLoc = startstop.split(delims);
		
		try {
			start = qLoc[0];
			stop = qLoc[1];
		} catch (Exception e) {
			System.out.println("-query_loc argument failed. Reason: " + e.getMessage());
			System.exit(1);
		}
	}
	
	private String getQueryLocation() {
		return start + "-" + stop;
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
	
	public void setBlast(String type) {
		this.blast = type;
	}
	
	public void setDatabase(String db) {
		this.db = db;
	}
	
	public void setOutput(String fileName) {
		this.output = fileName;
	}
	
	@Override
	public String getCommandline() {
		return commandline;
	}
}