import java.util.ArrayList;
import java.util.Collection;

public class MaxHeap
{
   private ArrayList<Student> students;
   
   public MaxHeap(int capacity)
   {
      students = new ArrayList<Student>(capacity);
   }
   
   public MaxHeap(Collection<Student> collection)
   {
      students = new ArrayList<Student>(collection);
      for(int x = 0; x < size(); x++)
      {
    	  indexCheck(students.get(x),x); // Verify that the incoming Collection has correct index values inside Student objects
      }
      for(int i = size()/2; i >= 0; i--)
      {
         maxHeapify(i);
      }
   }
   
   public Student getMax()
   {
      if(size() < 1)
      {
         throw new IndexOutOfBoundsException("No maximum value:  the heap is empty.");
      }
      return students.get(0);
   }
   
   public Student extractMax()
   {
      Student value = getMax();
      students.set(0,students.get(size()-1));
      students.remove(size()-1);
      maxHeapify(0);
      return value;
   }
   
   public void insert(Student elt)
   {
      elt.setIndex(students.size()); // Setting index to-be of Student object prior to insertion
	  students.add(elt);
	  int x = students.size()-1;
      swapParent(x);
      return;
   }

   public void changeKey(Student s, double newGPA)
   {
	  if (s.index() < 0)    // Check to ensure 's' is not un-inserted Student object
		  return;
	  int i = s.index(); // Change students.indexOf(s)
	  students.get(i).setGPA(newGPA);
	  
	  if (students.get(i).compareTo(students.get(parent(i))) <= 0)
		  maxHeapify(i);
	  else{
		  swapParent(i);
	  }
	  return;
   }
   
   private void swapParent(int i){ // Swaps Student object at index i with its parent(s) until it reaches the proper position
	   int k = i;
	   while (students.get(k).compareTo(students.get(parent(k))) > 0){
		   swap(k,parent(k));
		   k = parent(k);
	   }
   }

   private int parent(int index)
   {
      return (index - 1)/2;
   }
   
   private int left(int index)
   {
      return 2 * index + 1;
   }
   
   private int right(int index)
   {
      return 2 * index + 2;
   }
   
   private int size()
   {
      return students.size();
   }
   
   private void swap(int from, int to)
   {
      Student val = students.get(from);
      val.setIndex(to);					// Swapping Student objects' indices prior to swap
      students.get(to).setIndex(from);
      students.set(from,  students.get(to));
      students.set(to,  val);
   }
   
   private void indexCheck(Student x, int i){
	   if (x.index() != i)
		   x.setIndex(i);
   }
   
   private void maxHeapify(int index)
   {
      int left = left(index);
      int right = right(index);
      int largest = index;
      if (left <  size() && students.get(left).compareTo(students.get(largest)) > 0)
      {
         largest = left;
      }
      if (right <  size() && students.get(right).compareTo(students.get(largest)) > 0)
      {
         largest = right;
      }
      if (largest != index)
      {
         swap(index, largest);
         maxHeapify(largest);
      }  
   }   
}
