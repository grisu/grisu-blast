package control;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.control.exceptions.JobSubmissionException;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.JobObject;
import grisu.jcommons.constants.Constants;

import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.bestgrid.model.BlastJobCLI;

public class CLIController {
	private Options options;

	public CLIController(String[] args) {
		createOptions();
		
		System.out.println("Logging in...");
		ServiceInterface si = null;
		try {
			si = LoginManager.loginCommandline("BeSTGRID");
		} catch (Exception e) {
			System.err.println("Could not login: " + e.getLocalizedMessage());
			System.exit(1);
		}

		System.out.println("Creating job...");
		
		BlastJobCLI job = new BlastJobCLI();
		
		//getting arguments for job specification
		int cpu = 3;
		String submitLoc = "dev8_1:ng2hpc.canterbury.ac.nz#Loadleveler";
		String vo = "/ARCS/LocalAccounts/CanterburyHPC";
		
		if(args != null || args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				if(args[i] == "cpu") {
					cpu = Integer.parseInt(args[i+1]);
				} else if(args[i] == "vo") {
					vo = args[i+1];
				} else if(args[i] == "sl") {
					submitLoc = args[i+1];
				}
				System.out.print(args[i]);
			} 
			job.setServiceInterface(si);
			job.setCpus(cpu);
			job.setVo(vo);
			job.setSubmissionLocation(submitLoc);
		} else {
			job.setServiceInterface(si);
			job.setCpus(cpu);
			job.setVo(vo);
			job.setSubmissionLocation(submitLoc);
		}
		
		Scanner input = new Scanner(System.in);
		System.out.println("enter blast command");
		String command = input.nextLine();
		String arguments[] = command.split("\\s");
		//job.setCommandline(command);

		JobObject theJob = job.createJobObject();

		
		// theJob.setApplication("UnixCommands");
		try {
			System.out.println("Creating job on backend...");
			//job.createJob("/ARCS/LocalAccounts/CanterburyHPC");
			theJob.createJob();
		} catch (JobPropertiesException e) {
			System.err.println("Could not create job: "
					+ e.getLocalizedMessage());
			System.exit(1);
		}

		try {
			System.out.println("Submitting job to the grid...");
			//job.submitJob();
			theJob.submitJob();
		} catch (JobSubmissionException e) {
			System.err.println("Could not submit job: "
					+ e.getLocalizedMessage());
			System.exit(1);
		} catch (InterruptedException e) {
			System.err.println("Jobsubmission interrupted: "
					+ e.getLocalizedMessage());
			System.exit(1);
		}

		System.out.println("Job submission finished.");
		System.out.println("Job submitted to: "
				+ theJob.getJobProperty(Constants.SUBMISSION_SITE_KEY));
		/*+ job.getJobProperty(Constants.SUBMISSION_SITE_KEY));*/
		
		System.out.println("Waiting for job to finish...");

		// for a realy workflow, don't check every 5 seconds since that would
		// put too much load on the backend/gateways
		//job.waitForJobToFinish(5);
		theJob.waitForJobToFinish(5);

		System.out.println("Job finished with status: "
				/*+ job.getStatusString(false));*/
				+ theJob.getStatusString(false));

		System.out.println("Stdout: " + theJob.getStdOutContent());
		System.out.println("Stderr: " + theJob.getStdErrContent());
		
		CommandLineParser parser = new GnuParser();
		
		//testing the contents of args
		for(int i = 0; i < args.length; i++) {
			System.out.println(args[i] + " ");
		}
		
		//test whether the options have been specified
		try {
			CommandLine line = parser.parse(options, args);			
			
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
	
	public Options createOptions() {
		Options opt = new Options();  
		
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
		
		return options = opt;
	}
}
