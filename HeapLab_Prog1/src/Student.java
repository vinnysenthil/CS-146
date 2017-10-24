public class Student implements Comparable<Student>
{
   private String name;
   private double gpa = 0;
   private int units = 0;
   private int index;
   
   public Student(String name)
   {
      this.name = name;
      this.index = -1;
   }
   
   public Student(String name, int units, double gpa)
   {
      this.name = name;
      this.units = units;
      this.gpa = gpa;
      this.index = -1;
   }
   
   public String getName()
   {
      return name;
   }
   
   public double gpa()
   {
      return gpa;
   }
   
   public int index()
   {
	   return this.index;
   }
   
   public void setIndex(int i)
   {
	   this.index = i;
   }
   
   public void setGPA(double newGPA)
   {
      gpa = newGPA;
   }
   
   public int units()
   {
      return units;
   }
   
   public void setUnits(int newUnits)
   {
      units = newUnits;
   }
   
   public int compareTo(Student other)
   {
      double difference = gpa - other.gpa;
      if(difference == 0) return 0;
      if(difference > 0) return 12;
      return -14;
   }
}
