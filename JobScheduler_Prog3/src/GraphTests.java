import static org.junit.Assert.*;

import org.junit.Test;

public class GraphTests {

	@Test
	public void insertion() {
		JobSchedule x = new JobSchedule();
		x.addJob(6);
		x.addJob(8);
		assertEquals(2, x.getV());
	}
	
	@Test
	public void insertion2(){
		JobSchedule x = new JobSchedule();
		x.addJob(8);
		JobSchedule.Job j2 = x.addJob(3);
		x.addJob(5);
		x.getJob(1).requires(j2);
		assertEquals(1, x.getE());
		assertEquals(10, x.getSize());
		x.addJob(5);
		x.addJob(5);
		x.addJob(5);
		x.addJob(5);
		x.addJob(5);
		x.addJob(5);
		x.addJob(5);
		assertEquals(10, x.getSize());
	}
	
	@Test
	public void givenExample(){
		JobSchedule schedule = new JobSchedule();
		schedule.addJob(8); //adds job 0 with time 8
		JobSchedule.Job j1 = schedule.addJob(3); //adds job 1 with time 3
		schedule.addJob(5); //adds job 2 with time 5
		assertEquals(8, schedule.minCompletionTime()); //should return 8, since job 0 takes time 8 to complete.
		
		
		// Note it is not the min completion time of any job, but the earliest the entire set can complete.
		schedule.getJob(0).requires(schedule.getJob(2)); //job 2 must precede job 0
		assertEquals(13, schedule.minCompletionTime()); //should return 13 (job 0 cannot start until time 5)
		schedule.getJob(0).requires(j1); //job 1 must precede job 0
		assertEquals(13, schedule.minCompletionTime()); //should return 13
		
		
		assertEquals(5, schedule.getJob(0).getStartTime()); //should return 5
		assertEquals(0, j1.getStartTime()); //should return 0
		assertEquals(0, schedule.getJob(2).getStartTime()); //should return 0
		j1.requires(schedule.getJob(2)); //job 2 must precede job 1
		assertEquals(16, schedule.minCompletionTime()); //should return 16
	
		assertEquals(8, schedule.getJob(0).getStartTime()); //should return 8
		assertEquals(5, schedule.getJob(1).getStartTime()); //should return 5
		assertEquals(0, schedule.getJob(2).getStartTime()); //should return 0
		schedule.getJob(1).requires(schedule.getJob(0)); //job 0 must precede job 1 (creates loop)
		assertEquals(-1, schedule.minCompletionTime()); //should return -1
		
		assertEquals(-1, schedule.getJob(0).getStartTime()); //should return -1
		assertEquals(-1, schedule.getJob(1).getStartTime()); //should return -1
		assertEquals(0, schedule.getJob(2).getStartTime()); //should return 0 (no loops in prerequisites)
		
	}

}
