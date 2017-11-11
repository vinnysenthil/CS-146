/*
 * CS 146
 * Professor David Taylor
 * San Jos� State University
 * Fall 2017
 * 
 * Programming Assignment 3: JobScheduler
 * 
 * Find the "minimum completion time" for a set of given jobs, where jobs may happen in parallel, 
 * and also, each job may take different amounts of time. This is done by implementing a directed,
 * acyclic graph represented by 2 adjacency lists and using a single-source shortest path algorithm.
 * 
 * Written By: Vinny Senthil
 * October 24th, 2017
 */

import java.util.*;

public class JobSchedule{
	
	private int v;							// Number of vertices in graph
	private int e;							// Number of edges in graph
	private int sz;							// Array Size, Dynamically Updated
	private boolean changed;				// Track whether Schedule has been changed between method calls
	private LinkedList<Job> adjIncList[];	// Incoming Adjacency List 
	private LinkedList<Job> adjOutList[];	// Outgoing Adjacency List
	
	public JobSchedule(){					// No-parameter constructor
		v = 0;
		e = 0;
		sz = 10;							// Default size of 10, increased or decreased automatically
		changed = true;
		adjIncList = new LinkedList[sz];	
		adjOutList = new LinkedList[sz];
		
	} // end JobSchedule::JobSchedule
	
	
	/* ADDJOB
	 * 
	 * Adds a new job to the schedule, 
	 * where the time needed to complete the job is time. (time will never be negative.) 
	 * Jobs will be numbered, by the order in which they are added to your structure: 
	 * the first job added is (implicitly) job 0, the next 1, even though they won't be labeled.
	 */
	public Job addJob(int time){
		
		Job newJob = new Job(v, time);
		sizeCheck();							// Check current adj lists' size and update if needed
		
		adjIncList[v] = new LinkedList<Job>();
		adjIncList[v].add(newJob);
		
		adjOutList[v] = new LinkedList<Job>();
		adjOutList[v].add(newJob);
				
		v++;
		
		return newJob;
		
	} // end JobSchedule::addJob
	
	
	/* GETJOB
	 * 
	 * Returns the job by its number. 
	 * (Use the implicit number, as described in addJob. 
	 * That number has nothing to do with the time for completion.) 
	 * (I will only call this method with valid indices. 
	 * I want you to correctly code the algorithm, rather than worrying
	 * about bullet-proofing your code against bad inputs.)
	 */
	public Job getJob(int index){
		
		return adjIncList[index].getFirst();
		
	} // end JobSchedule::getJob
	
	
	/* MINCOMPLETIONTIME
	 * 
	 * Return the minimum possible completion time for the entire 
	 * JobSchedule, or -1 if it is not possible to complete it. 
	 * (It is not possible to complete if there is a prerequisite 
	 * cycle within the underlying graph).
	 */
	public int minCompletionTime(){
		
		if(changed){
			if(SSSPDAG(-1) < 0)			// If SSSPDAG returns -1 (cycle detection)
				return -1;					// Return -1 here as well
			changed = false;
		}
		
		return findMin();
	
	} // end JobSchedule::minCompletionTime
	
	
	// Shortest Path Algorithm w/ Cycle Checking
	public int SSSPDAG(int jobV){
				
		List<Integer> vertList = topSort();				// Create List of topologically sorted Jobs from graph
		
		if(jobV < 0 && vertList.size() != v)			// If topSort failed, there's a cycle
			return -1;									// So return -1 to tell minCompletionTime
		
		Integer[] pi = new Integer[v];
			
		// SSSP_INITIALIZE(G,s)
		for(int i = 0; i < v; i++){
			updateTime(i, getTime(i));					// Set default distance as Job
			pi[i] = null;
			setFinish(i, false);						// Set all jobs as not found
		}
				
		// Iterating through topologically sorted stack
		
		Iterator<Integer> iter = vertList.iterator();
				
		while(iter.hasNext()){
			int u = iter.next();						// Get index of Job u from topSort list
			
			if(jobV == u)								// If SSSP DAG is being called for getStartTime()
				return 1;								// Single Target Destination reached
			
			setFinish(u, true);							// Mark Job u as found
			
			Iterator<Job> edges = adjOutList[u].iterator();
			
			if(edges.hasNext())							// Skip first Job in adjacency list (Job 'u' itself)
				edges.next();
			
			while (edges.hasNext()){
				int v = edges.next().V();				// Get Job index of next outgoing edge from u -> v
					
				// RELAX(G,s)
				if(getTime(u) + getWeight(v) > getTime(v)){		// Check if new time takes longer
					updateTime(v, getTime(u) + getWeight(v));	// If so, update Job's start time
					pi[v] = u;									// Update or set parent Job
				
				}
			}	
		}
		
		for (int i = 0; i < v; i++){		// Kahn's algorithm will not discover vertices in cycles
			if(!checkFinish(i)){			// Therefore if any vertex was not discovered after SSSP DAG,
				updateTime(i, -1);			// Set its start time to -1			
			}
		}
						
		return 1;
	} // end JobSchedule::SSSPDAG
	
	
	// Dynamically increase size of array storing adjacency lists
	public void sizeCheck(){
		if (v+1 == sz){											// If new Job will exceed size, double size of adjLists
			sz *= 2;	
			LinkedList<Job> newAdjI[] = new LinkedList[sz];
			LinkedList<Job> newAdjO[] = new LinkedList[sz];

			for (int i = 0; i < (sz/2); i++){
				newAdjI[i] = adjIncList[i];						// Temporarily copy and hold data while resizing
				newAdjO[i] = adjOutList[i];
			}
			
			adjIncList = newAdjI;								// Reassign temporarily held data
			adjOutList = newAdjO;
		}
		
		else
			return;												// Else if new Job fits, then do nothing
		
	} // end JobSchedule::sizeCheck
	
	
	// Non-Recursive, Non-DFS TopSort (Kahn's Algorithm)
	public ArrayList<Integer> topSort()
	{
		int[] inDegree = new int[v];
		
		for(int i = 0; i < v; i++){
			inDegree[i] = (adjIncList[i].size() - 1);			// Set inDegree for each vertex based on LinkedList size
		}														// in the incoming adjacency list
		
		ArrayList<Integer> vertList = new ArrayList<Integer>();
		
		for (int i = 0; i < v; i++){							// Add all Jobs with no required jobs to start of topSort list
			if(inDegree[i] == 0)
				vertList.add(i);
		}
				
		for(int i = 0; i < vertList.size(); i++){
			int u = vertList.get(i);							// Get Job from topSort list, in order
			
			Iterator<Job> outVertices = adjOutList[u].iterator();
			
			while(outVertices.hasNext()){						// Go through all outgoing edges from Jobs already in list
				int v = outVertices.next().V();
				--inDegree[v];									// Once found, decrement inDegree for that Job
				
				if (inDegree[v] == 0)							// If inDegree has reached zero, add to end of topSort list
					vertList.add(v);
			}
		}
		
		return vertList;										// Return ArrayList of topSort list
		
	} // end JobSchedule::topSort
	
	
	// Find Job sequence that takes largest time in JobSchedule
	public int findMin(){
		
		int minTime = Integer.MIN_VALUE;						// Negative Infinity
		
		for (int i = 0; i < v; i++){
			if(getTime(i) > minTime)
				minTime = getTime(i);			// Find "minimum" time in vertex list
		}
		
		return minTime;
	} // end JobSchedule::findMin
	
	
	public int getWeight(int vertex){
		return adjIncList[vertex].getFirst().W();
	}
	
	// Get Number of Vertices
	public int getV(){
		return v;
	}
	
	// Get Number of Edges
	public int getE(){
		return e;
	}
	
	// Get Size of Adjacency Array
	public int getSize(){
		return sz;
	}
	
	// Update startTime of specific Job in Schedule
	public void updateTime(int v, int t){
		adjIncList[v].getFirst().setT(t);
		adjOutList[v].getFirst().setT(t);
	}
	
	public int getTime(int v){
		return adjIncList[v].getFirst().startTime;
	}
	
	public void setFinish(int v, boolean f)
	{
		adjIncList[v].getFirst().finished = f;
		adjOutList[v].getFirst().finished = f;
	}
	
	public boolean checkFinish(int v){
		return adjIncList[v].getFirst().finished;
	}
	
	public void markUndiscovered(){
		for (int i = 0; i < v; i++){		// Kahn's algorithm will not discover vertices in cycles
			if(!checkFinish(i)){			// Therefore if any vertex was not discovered after SSSP DAG,
				updateTime(i, -1);			// Set its start time to -1			
			}
		}
	}
	
	public class Job{
		int vertex;							// This Job's index in adjacency lists
		int weight;							// Time of this specific Job
		int startTime;						// Minimum time to begin this Job
		boolean finished;					// Mark if found or not during SSSP DAG
		
		// Two Parameter Job Constructor
		public Job(int v, int w){
			vertex = v;
			weight = w;
			startTime = w;
		}
		
		public int V(){
			return vertex;
		}
		
		public int W(){
			return weight;
		}
		
		public int T(){
			return startTime;
		}
			
		// Edge Weight Mutator (Job Time)
		public void setW(int t){
			weight = t;
		}
		
		// Start Time Mutator
		public void setT(int t){
			startTime = t;
		}
		
		/* JOBS::REQUIRES
		 * 
		 * Sets up the requirement that this job requires
		 * job j to be completed before it begins.
		 */
		public void requires(Job j){
			
			adjIncList[vertex].add(j);			// Add required job into incoming adjacency list
			adjOutList[j.V()].add(this);		// Add this job into the required job's outgoing adjacency list
			e++;
			changed = true;						// By adding an edge, we've potentially changed the minimum start time
		
		} // end Job::requires
		
		
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
			
			if(changed){
				SSSPDAG(vertex);
				changed = false;
			}
						
			if (startTime < 0)				// If this vertex is in a cycle
				return -1;					// Then it cannot have a start time, thus return -1
			else
				return startTime - weight;	// Return the longest minimum time to *start* job
			
		} // end Job::requires
	
	} // end Job class
	
} // end JobSchedule class