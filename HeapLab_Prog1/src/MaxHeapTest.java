import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;


public class MaxHeapTest
{
   private MaxHeap heap, heap2, heap3;
   private Student ant, vin, jason, james;
   
   @Before
   public void setUp() throws Exception
   {
      heap = new MaxHeap(10);
      heap.insert(new Student("Susan", 60, 3.5));
      heap.insert(new Student("Ben", 70, 3.4));
      heap.insert(new Student("Reed", 120, 4.0));
      heap.insert(new Student("Johnny", 50, 1.2));
      
      ant = new Student("Antonio"); // Creating Student with name-only constructor
      vin = new Student("Vinny", 12, 4.0); // Creating Student with equivalent GPA for compareTo
      
      ArrayList<Student> x = new ArrayList<Student>();
      Student susan = new Student("Susan", 60, 3.5);
      Student jonny = new Student("Johnny", 50, 1.2);
      
      x.add(new Student("Ben", 70, 3.4));
      x.add(new Student("Reed", 120, 4.0));
      x.add(jonny);
      x.add(susan);
      
      heap2 = new MaxHeap(x); // Using second constructor
      
      heap3 = new MaxHeap(10);
      jason = new Student("Jason", 30, 2.1);
      james = new Student("James", 40, 3.9);
   }

   @Test
   public void test()
   {
      
      //  ==================================
      //     Testing MaxHeap class methods
      //  ==================================
	  assertEquals(4.0, heap.extractMax().gpa(), .000001);
	  assertEquals(3.5, heap.extractMax().gpa(), .000001);
	  assertEquals(4.0, heap2.extractMax().gpa(), .000001);
	   
      heap3.insert(james); // GPA = 3.9
      heap3.insert(jason); // GPA = 2.1
      
      assertEquals(3.9, heap3.getMax().gpa(), .000001);
      
      heap3.changeKey(jason, 4.1); // Test changeKey for non-max node
      assertEquals(4.1, heap3.getMax().gpa(), .000001); // Confirming changeKey worked
      
      heap3.changeKey(jason, 4.0); // Test changeKey for max node
      assertEquals(4.0, heap3.getMax().gpa(), .000001); // Confirming changeKey worked
      
      //  ======================
      //  Testing Student class
      //  ======================
      ant.setGPA(4.0); //Testing remaining accessor and mutator methods
      
      assertEquals(4.0, ant.gpa(), .000001);
      assertEquals("Antonio", ant.getName());
            
      ant.setUnits(15);
      
      assertEquals(15, ant.units(), .000001);
      
      assertEquals(0, ant.compareTo(vin)); // compareTo test case for equal GPA
      
   }

}
