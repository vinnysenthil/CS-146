/*
 * CS 146
 * Professor David Taylor
 * San Jose State University
 * Fall 2017
 * 
 * Programming Assignment 3: JobScheduler
 * 
 * Find the "minimum completion time" for a set of given jobs, where jobs may happen in parallel, 
 * and also, each job may take different amounts of time. This is done by implementing a directed,
 * acyclic graph and using a single-source shortest path algorithm.
 * 
 * Written By: Vinny Senthil
 * October 24th, 2017
 */

import java.util.*;

public class JobSchedule{
	
	private int V;							// Number of vertices in graph
	private LinkedList<Integer> adjList;	// Adjacency List 
	
	public JobSchedule(){}					// No-parameter constructor
	
	/* ADDJOB
	 * 
	 * Adds a new job to the schedule, 
	 * where the time needed to complete the job is time. (time will never be negative.) 
	 * Jobs will be numbered, by the order in which they are added to your structure: 
	 * the first job added is (implicitly) job 0, the next 1, even though they won't be labeled.
	 */
	public Job addJob(int time){
		
		return null;
	}
	
	/* GETJOB
	 * 
	 * which returns the job by its number. 
	 * (Use the implicit number, as described in addJob. 
	 * That number has nothing to do with the time for completion.) 
	 * (I will only call this method with valid indices. 
	 * I want you to correctly code the algorithm, rather than worrying
	 * about bullet-proofing your code against bad inputs.)
	 */
	public Job getJob(int index){
		
		return null;
	}
	
	
	/* MINCOMPLETIONTIME
	 * 
	 *  return the minimum possible completion time for the entire 
	 *  JobSchedule, or -1 if it is not possible to complete it. 
	 *  (It is not possible to complete if there is a prerequisite 
	 *  cycle within the underlying graph).
	 */
	public int minCompletionTime(){
		
		return -1;
	}
	
	
	public class Job{
		
		/* JOBS::REQUIRES
		 * 
		 * sets up the requirement that this job requires
		 * job j to be completed before it begins.
		 */
		public void requires(Job j){
			
		}
		
		/* JOBS::GETSTARTTIME
		 * 
		 *  Return the earliest possible start time for the job. 
		 *  (The very first first start time, for jobs with no pre-requisites, is 0.) 
		 *  If there IS a cycle, and thus the given job can never be started 
		 *  (because it is on the cycle, or something on the cycle must be completed
		 *  before this job starts), return -1. However, if the job can be started, 
		 *  even if other jobs within the overall schedule cannot be, return a valid time.
		 *  
		 */
		public int getStartTime(){
			
			return -1;
		}
	}
	
}