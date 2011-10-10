package org.bestgrid.control;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.control.exceptions.JobSubmissionException;
import grisu.control.exceptions.NoSuchJobException;
import grisu.frontend.model.job.JobObject;
import grisu.jcommons.constants.Constants;
//import grisu.model.GrisuRegistryManager;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.FilenameUtils;
import org.bestgrid.model.BlastModel;
import org.bestgrid.view.BlastView;

public class BlastController {	
	private ServiceInterface si;
	
	public BlastController(final BlastModel myModel, final BlastView myView, String[] aCommand) {
		myModel.setModel(aCommand);	
			while(true) {
					try {
						//getting the command from command line
						BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
						String line = reader.readLine();
						String[] command = line.split("\\s");
						
						//send commands to the model
						myModel.setModel(command);
						myView.setView(myModel.getModel());

						if (line.equals("showcommand")) {
							myModel.constructCommand();
							System.out.println(myModel.getCommandline());
						}
						
						if (line.equals("submit")) {
							submit(myModel);
							break;
						}
						
						/*
						char c = (char) System.in.read();
						if(c != '\n' && c != '\r') {
							myModel.setModel(c);
							myView.setView(myModel.getModel());		
						}*/
					}
					catch(Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
				}
		}

	public ServiceInterface getServiceInterface() {
		return this.si;
	}
	
	public void setServiceInterface(ServiceInterface si) {
		this.si = si;
	}
	

    /**
     * Get the relative path from one file to another, specifying the directory separator. 
     * If one of the provided resources does not exist, it is assumed to be a file unless it ends with '/' or
     * '\'.
     * 
     * @param target targetPath is calculated to this file
     * @param base basePath is calculated from this file
     * @param separator directory separator. The platform default is not assumed so that we can test Unix behaviour when running on Windows (for example)
     * @return
     */
    public static String getRelativePath(String targetPath, String basePath, String pathSeparator) {

        // Normalize the paths
        String normalizedTargetPath = FilenameUtils.normalizeNoEndSeparator(targetPath);
        String normalizedBasePath = FilenameUtils.normalizeNoEndSeparator(basePath);

        // Undo the changes to the separators made by normalization
        if (pathSeparator.equals("/")) {
            normalizedTargetPath = FilenameUtils.separatorsToUnix(normalizedTargetPath);
            normalizedBasePath = FilenameUtils.separatorsToUnix(normalizedBasePath);

        } else if (pathSeparator.equals("\\")) {
            normalizedTargetPath = FilenameUtils.separatorsToWindows(normalizedTargetPath);
            normalizedBasePath = FilenameUtils.separatorsToWindows(normalizedBasePath);

        } else {
            throw new IllegalArgumentException("Unrecognised dir separator '" + pathSeparator + "'");
        }

        String[] base = normalizedBasePath.split(Pattern.quote(pathSeparator));
        String[] target = normalizedTargetPath.split(Pattern.quote(pathSeparator));

        // First get all the common elements. Store them as a string,
        // and also count how many of them there are.
        StringBuffer common = new StringBuffer();

        int commonIndex = 0;
        while (commonIndex < target.length && commonIndex < base.length
                && target[commonIndex].equals(base[commonIndex])) {
            common.append(target[commonIndex] + pathSeparator);
            commonIndex++;
        }

        if (commonIndex == 0) {
            // No single common path element. This most
            // likely indicates differing drive letters, like C: and D:.
            // These paths cannot be relativized.
            throw new PathResolutionException("No common path element found for '" + normalizedTargetPath + "' and '" + normalizedBasePath
                    + "'");
        }   

        // The number of directories we have to backtrack depends on whether the base is a file or a dir
        // For example, the relative path from
        //
        // /foo/bar/baz/gg/ff to /foo/bar/baz
        // 
        // ".." if ff is a file
        // "../.." if ff is a directory
        //
        // The following is a heuristic to figure out if the base refers to a file or dir. It's not perfect, because
        // the resource referred to by this path may not actually exist, but it's the best I can do
        boolean baseIsFile = true;

        File baseResource = new File(normalizedBasePath);

        if (baseResource.exists()) {
            baseIsFile = baseResource.isFile();

        } else if (basePath.endsWith(pathSeparator)) {
            baseIsFile = false;
        }

        StringBuffer relative = new StringBuffer();

        if (base.length != commonIndex) {
            int numDirsUp = baseIsFile ? base.length - commonIndex - 1 : base.length - commonIndex;

            for (int i = 0; i < numDirsUp; i++) {
                relative.append(".." + pathSeparator);
            }
        }
        relative.append(normalizedTargetPath.substring(common.length()));
        return relative.toString();
    }


    @SuppressWarnings("serial")
	static class PathResolutionException extends RuntimeException {
        PathResolutionException(String msg) {
            super(msg);
        }
    }  
    
	private void submit(BlastModel myModel) {
		System.out.println("Creating job...");
		//myModel.setServiceInterface(this.si);
		myModel.constructCommand();
		JobObject job = myModel.createJobObject();
		job.setSubmissionLocation("grid_linux:ng2hpc.canterbury.ac.nz#Loadleveler");
		
		try {
			System.out.println("Creating job on backend...");
			job.createJob("/ARCS/BeSTGRID");
			System.out.println(job.getCommandline());
		} catch (JobPropertiesException e) {
			System.err.println("Could not create job: "
					+ e.getLocalizedMessage());
			System.exit(1);
		}
    	
		try {
			System.out.println("Submitting job to the grid...");
			job.submitJob();
		} catch (JobSubmissionException e) {
			System.err.println("Could not submit job: "
					+ e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			System.err.println("Jobsubmission interrupted: "
					+ e.getLocalizedMessage());
			System.exit(1);
		}
		
		System.out.println(job.getJobDirectoryUrl());
		
		System.out.println("Job submission finished.");
		System.out.println("Job submitted to: "
				+ job.getJobProperty(Constants.SUBMISSION_SITE_KEY));

		System.out.println("Waiting for job to finish...");

		// for a realy workflow, don't check every 5 seconds since that would
		// put too much load on the backend/gateways
		job.waitForJobToFinish(5);

		System.out.println("Job finished with status: "
				+ job.getStatusString(false));

		
		System.out.println("Stdout: " + job.getStdOutContent());
		System.out.println("Stderr: " + job.getStdErrContent());

		//job.downloadAndCacheOutputFile(job.getJobDirectoryUrl());
		System.out.println(job.getJobDirectoryUrl());
		
		//Retrieving the output directory
		URI uri;
		String path = null;
		try {
			uri = new URI(URIUtil.encodeQuery(job.getJobDirectoryUrl()));
			URI relative = uri.relativize(uri);
			path = relative.toString();
		} catch (URIException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		//String path = getRelativePath(job.getJobDirectoryUrl(), job.getJobDirectoryUrl(), "/");
		System.out.println(path);
		if(job.isFinished()) {
			try {
				JobObject toRetrieve = new JobObject(job.getServiceInterface(), job.getJobname());
				toRetrieve.downloadAndCacheOutputFile(path); 
				}
			catch (NoSuchJobException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				}
		}
		// it's good practise to shutdown the jvm properly. There might be some
		// executors running in the background
		// and they need to know when to shutdown.
		// Otherwise, your application might not exit.
		System.exit(0);
	}
}
