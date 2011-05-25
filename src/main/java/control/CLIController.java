package control;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.RemoteFileSystemException;
//import grisu.control.exceptions.JobPropertiesException;
//import grisu.control.exceptions.JobSubmissionException;
//import grisu.frontend.model.job.JobObject;
//import grisu.jcommons.constants.Constants;
import grisu.model.FileManager;
import grisu.model.GrisuRegistryManager;

//import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
//import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.bestgrid.model.BlastJobCLI;

public class CLIController {
	private Options options;
	private BlastJobCLI job;
	private ServiceInterface si;
	private FileManager fm;
	//private String command;
	
	public void createJob() {
		job = new BlastJobCLI();
	}
	
	public BlastJobCLI getJobObject(){
		return job;
	}
	
	public void process(String[] args) {
		createOptions();

		System.out.println("Creating job...");
		createJob();
		
		int cpu = 3;
		String submitLoc = "dev8_1:ng2hpc.canterbury.ac.nz#Loadleveler";
		String vo = "/ARCS/LocalAccounts/CanterburyHPC";
		
		job.setServiceInterface(getServiceInterface());
		job.setCpus(cpu);
		job.setSubmissionLocation(submitLoc);
		job.setVo(vo);
		
		//getting arguments for job specification
		CommandLineParser parser = new GnuParser();
		
		try {
			CommandLine line = parser.parse(options, args);			

			if(line.hasOption("cpu")) {
				System.out.println("cpu " + line.getOptionValue("cpu"));
				try {
					//int cpu;
					Integer.parseInt(line.getOptionValue("cpu"));
					if(Integer.parseInt(line.getOptionValue("cpu")) < 3) {
						System.out.println("Minimum CPU to be used is 3");
						System.out.println("using 3 as default CPU number to be used");
						job.setCpus(cpu);
					} else {
						job.setCpus(Integer.parseInt(line.getOptionValue("cpu")));
					}
				} catch(Exception e) {
					System.out.println("Please enter the value as integer");
					System.exit(1);
				}
			} 
			
			if(line.hasOption("vo")) {
				System.out.println("vo " + line.getOptionValue("vo"));
				job.setVo(line.getOptionValue("vo"));
			} 
			if(line.hasOption("sl")) {
				System.out.println("sl " + line.getOptionValue("sl"));
				job.setSubmissionLocation(line.getOptionValue("sl"));
			}
			
			if(line.hasOption("type")) {
				if(line.getOptionValue("type").equals("blastn")) setBlastNOptions();
				if(line.getOptionValue("type").equals("blastp")) setBlastPOptions();
				if(line.getOptionValue("type").equals("blastx")) {
					setBlastXOptions();
					job.setCommandline("blastx ");
					processBlastX(args);
					
				}
				if(line.getOptionValue("type").equals("tblastn")) setTBlastNOptions();
				if(line.getOptionValue("type").equals("tblastx")) setTBlastXOptions();
			}
		} 
		catch(ParseException exp) {
			System.err.println("Parsing failed. Reason: " + exp.getMessage());
		}
	}
	
	public Options createOptions() {
		Options opt = new Options();  
				
		opt.addOption("cpu", true, "number of CPUs to use");
		opt.addOption("vo", true, "VO to use");
		opt.addOption("sl", true, "location to submit the job");
		
		//add options for blast types
		opt.addOption("type", true, "blast application to use");
		
		return this.options = opt;
	}
	
	public void setBlastNOptions() {
		
	}
	
	public void setBlastPOptions() {
		
	}

	public void setBlastXOptions() {
		options.addOption("query", true, "Input file name");
		options.addOption("query_loc", true, "Location on the query sequence " +
				"in 1-based offsets (format:start-stop)");
		options.addOption("query-gencode", true, "Genetic code to use to " +
				"translate query. Default = '1'");
		options.addOption("evalue", true, "Expectation value (E) threshold for " +
				"saving hits. Default ='10'");
		options.addOption("word_size", true, "Word size for wordfinder algorithm");
		options.addOption("gapopen", true, "Cost to open a gap");
		options.addOption("gapextend", true, "Cost to extend a gap");
		options.addOption("matrix", true, "Scoring matrix name (normally BLOSUM62)");
		options.addOption("seg", true, "Filter query sequence with SEG " +
				"(Format: 'yes', 'window locut hicut', or 'no' to disable). " +
				"Default = '12 2.2 2.5'");
		options.addOption("lcase_masking", false, "Use lower case filtering in query" +
				" and subject sequence(s)?");
	}

	public void setTBlastNOptions() {
	
	}
	
	public void setTBlastXOptions() {
		
	}
	
	public void processBlastX(String[] theArgs){
		CommandLineParser parser = new GnuParser();
		
		try {
			CommandLine line = parser.parse(options, theArgs);			

			if(line.hasOption("query")) {
				try {
					job.setFastaFile(line.getOptionValue("query"));
					job.addCommand("-query " + line.getOptionValue("query"));
					job.setCommandline(job.getCommand());
				} catch (RemoteFileSystemException e) {
					System.out.println("Invalid argument for query");
					System.exit(1);
				}
			} 
			
			if(line.hasOption("query_loc")) {
				String parameters = line.getOptionValue("query_loc");
				
				//verify input
				if(parameters.indexOf('-') < 0) {
					System.out.println("Invalid formatting for query_loc");
					System.exit(1);
				}
				
				String[] param = parameters.split("-");
				
				if(param.length > 1) {
					System.out.println("Invalid formatting for query_loc");
					System.exit(1);
				} else {
					for(int i = 0; i < param.length; i++) {
						try {
							Integer.parseInt(param[i]);
						} catch(NumberFormatException e) {
							System.out.println("Invalid formatting for query_loc");
							System.exit(1);
						}
						job.setQueryLocation(param);
					}
				}
				
				job.addCommand("-query_loc " + line.getOptionValue("query_loc"));
				job.setCommandline(job.getCommand());
			} 
			
			if(line.hasOption("query_gencode")) {
				System.out.println("sl " + line.getOptionValue("sl"));
				job.setSubmissionLocation(line.getOptionValue("sl"));
			}
			
			if(line.hasOption("evalue")) {
			
			}
			
			if(line.hasOption("word_size")) {
				
			}
			
			if(line.hasOption("gapopen")) {
				
			}
			
			if(line.hasOption("gapextend")) {
				
			}
			
			if(line.hasOption("matrix")) {
				
			}
			
			if(line.hasOption("seg")) {
				
			}
			
			if(line.hasOption("lcase_masking")) {
				
			}
		}catch(ParseException exp) {
			System.err.println("Parsing failed. Reason: " + exp.getMessage());
		}
	}
	
	public ServiceInterface getServiceInterface() {
		return this.si;
	}
	
	public void setServiceInterface(ServiceInterface si) {
		this.si = si;
		this.fm = GrisuRegistryManager.getDefault(si).getFileManager();
	}
	
}
