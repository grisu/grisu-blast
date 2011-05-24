package control;

import grisu.control.ServiceInterface;
//import grisu.control.exceptions.JobPropertiesException;
//import grisu.control.exceptions.JobSubmissionException;
//import grisu.frontend.model.job.JobObject;
//import grisu.jcommons.constants.Constants;
import grisu.model.FileManager;
import grisu.model.GrisuRegistryManager;

import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.bestgrid.model.BlastJobCLI;

public class CLIController {
	private Options options;
	private BlastJobCLI job;
	private ServiceInterface si;
	private FileManager fm;
	
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
		} 
		catch(ParseException exp) {
			System.err.println("Parsing failed. Reason: " + exp.getMessage());
		}
		
		//getting the blast parameters
		Scanner input = new Scanner(System.in);
		System.out.println("enter blast command");
		String command = input.nextLine();
		String arguments[] = command.split("\\s");
		job.configureBlast(arguments);
		
		
	}
	
	public Options createOptions() {
		Options opt = new Options();  
				
		opt.addOption("cpu", true, "number of CPUs to use");
		opt.addOption("vo", true, "VO to use");
		opt.addOption("sl", true, "location to submit the job");
		
		return this.options = opt;
	}
	
	public ServiceInterface getServiceInterface() {
		return this.si;
	}
	
	public void setServiceInterface(ServiceInterface si) {
		this.si = si;
		this.fm = GrisuRegistryManager.getDefault(si).getFileManager();
	}
	
}
