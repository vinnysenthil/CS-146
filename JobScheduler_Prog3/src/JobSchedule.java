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
 * acyclic graph represented by an adjacency list and using a single-source shortest path algorithm.
 * 
 * Written By: Vinny Senthil
 * October 24th, 2017
 */

import java.util.*;

public class JobSchedule{
	
	private int v;							// Number of vertices in graph
	private int e;							// Number of edges in graph
	private int sz;							// Array Size, Dynamically Updated
	private LinkedList<Job> adjList[];		// Incoming Adjacency List 
	
	public JobSchedule(){					// No-parameter constructor
		
		v = 0;
		e = 0;
		sz = 10;							// Default size of 10, increased or decreased automatically
		adjList = new LinkedList[sz];		

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
		sizeCheck();
		adjList[v] = new LinkedList<Job>();
		adjList[v].add(newJob);
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
		
		return adjList[index].get(0);
		
	} // end JobSchedule::getJob
	
	
	/* MINCOMPLETIONTIME
	 * 
	 * Return the minimum possible completion time for the entire 
	 * JobSchedule, or -1 if it is not possible to complete it. 
	 * (It is not possible to complete if there is a prerequisite 
	 * cycle within the underlying graph).
	 */
	public int minCompletionTime(){
		
		Integer[] d = new Integer[v];
		
		if(SSSPDAG(d, -1) < 0)
			return -1;
		
		return findMin(d);
		
	} // end JobSchedule::minCompletionTime
	
	public int SSSPDAG(Integer[] d, int jobV){
		// Shortest Path Algorithm w/ Cycle Checking
		
		Boolean[] dis = new Boolean[v];
		Stack<Integer> topStack = new Stack<Integer>();
		Stack<Integer> extraStack = new Stack<Integer>();

		for(int i = 0; i < v; i++)
			dis[i] = false;
		
		topSort(dis, 0, topStack);
		
		Integer[] pi = new Integer[v];
		Boolean[] fin = new Boolean[v];
			
		// SSSP_INITIALIZE(G,s)
		for(int i = 0; i < v; i++){
			d[i] = adjList[i].getFirst().W();
			pi[i] = null;
			dis[i] = false;
			fin[i] = false;
		}
		
		// Iterating through topologically sorted stack
				
		while(!topStack.empty()){
			int u = topStack.pop();
						
			fin[u] = true;
			Iterator<Job> edges = adjList[u].iterator();
			
			if(edges.hasNext())							// Skip first vertex in adjacency list
				edges.next();
			
			while (edges.hasNext()){
				Job v = edges.next();
					
				// RELAX(G,s)
				if(d[u] < (d[v.V()] + getWeight(u))){
					if(fin[v.V()])
						return -1;						// !! Cycle Detected !!
					
					d[u] = d[v.V()] + getWeight(u);
					pi[v.V()] = u;
				}
			}	
		}
		
		return 1;
	}
	
	// Dynamically increase size of array storing adjacency list
	public void sizeCheck(){
		if (v+1 == sz){
			sz *= 2;	
			LinkedList<Job> newAdj[] = new LinkedList[sz];
			
			for (int i = 0; i < (sz/2); i++){
				newAdj[i] = adjList[i];
			}
			
			adjList = newAdj;
		}
		
		else
			return;
	}
	
	public void topSort(Boolean[] found, int u, Stack<Integer> topStack)
	{
		found[u] = true;
		Iterator<Job> iter = adjList[u].iterator();
		
		while(iter.hasNext()){
			Job temp = iter.next();

			if(!found[temp.V()])
				topSort(found, temp.vertex, topStack);
			
		}
		
		topStack.push(u);
	}
	
	public int findMin(Integer[] d){
		
		int minTime = Integer.MIN_VALUE;				// Negative Infinity
		
		for (int i = 0; i < v; i++){
			if(d[i] > minTime)
				minTime = d[i];							// Find "minimum" time in vertex list
		}
		
		return minTime;
	}
	
	public int getWeight(int vertex){
		return adjList[vertex].getFirst().W();
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
		
	public class Job{
		int vertex;
		int weight;
		
		public Job(int v, int w){
			vertex = v;
			weight = w;
		}
		
		public int V(){
			return vertex;
		}
		
		public int W(){
			return weight;
		}
		
		/* JOBS::REQUIRES
		 * 
		 * Sets up the requirement that this job requires
		 * job j to be completed before it begins.
		 */
		public void requires(Job j){
			
			adjList[vertex].add(j);
			e++;
		
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
			
			Integer[] d = new Integer[v];
			if(SSSPDAG(d, vertex) < 0)
				return -1;
			return d[vertex] - weight;
			
		} // end Job::requires
	}
	
}