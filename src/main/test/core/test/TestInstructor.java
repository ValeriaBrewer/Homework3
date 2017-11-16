package core.test;

import core.api.IAdmin;
import core.api.IInstructor;
import core.api.IStudent;
import core.api.impl.Admin;
import core.api.impl.Instructor;
import core.api.impl.Student;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/*
 *  Bugs found with this test file. 
 */

public class TestInstructor {
    private IInstructor instructor;
    private IAdmin admin;
    private IStudent student;

    @Before
    public void setupInstructor() {
        this.instructor = new Instructor();
    }
    @Before
    public void setupAdmin() {
        this.admin = new Admin();
    }
    @Before
    public void setupStudent() {
    	this.student = new Student();
    }
    
    
    /*
     * Testing Function:
     *     void addHomework(String instructorName, String className, int year, String homeworkName);
     *     
     *     Instructor must be assigned to specified class:			BUG
     *     Specified class exists:									OK
     */
    
    /*
     * Instructor must be assigned to specified class: 		BUG
     */
    @Test
    public void instructorAssignedClass() {
    	// Expected Result: PASS
    	this.admin.createClass("Potions", 2017, "Professor Snape", 15);
    	this.instructor.addHomework("Professor Snape", "Potions", 2017, "Page394");
    	assertTrue(this.instructor.homeworkExists("Potions", 2017, "Page394")); 	
    }
    @Test
    public void instructorNotAssignedClass() {
    	// Expected Result: PASS 						BUG: instructor not assigned to class is able to add homework
    	this.admin.createClass("Potions", 2017, "Professor Snape", 15);
    	this.instructor.addHomework("Professor Lupin", "Potions", 2017, "Page394"); // instructor not assigned to class
    	assertFalse(this.instructor.homeworkExists("Potions", 2017, "Page394"));	// hw should not exist
    	
    }
     
    /*
     * Specified class exists. If class does not exist, can't add homework to it:		OK
     */
    @Test
    public void classExistsToAddHomework() {
    	// Expected Result: PASS
    	this.admin.createClass("Potions", 2017, "Professor Snape", 10);
    	this.instructor.addHomework("Professor Snape", "Potions", 2017, "Page394");
    	assertTrue(this.instructor.homeworkExists("Potions", 2017, "Page394"));   	
    }    
    @Test
    public void classDoesntExistsToAddHomework() {
    	// Expected Result: PASS
    	this.instructor.addHomework("Professor Snape", "Potions", 2017, "Page394");
    	assertFalse(this.instructor.homeworkExists("Potions", 2017, "Page394"));   	
    }
   
    
    
    /*
     * Testing Function:
     *     void assignGrade(String instructorName, String className, int year, String homeworkName, String studentName, int grade);
     *     
     *     Student is enrolled in specified class:					BUG
     * 	   Instructor must be assigned to specified class:			BUG
     *     Homework has been assigned:								OK
     *     Student has submitted the homework:						BUG
     *     
     */
  
    /*
     * Test that if all conditions specified above are met, grade is successfully assigned:
     * [ (Student is enrolled in class) ^ (Instructor assigned to class) ^ 
     *   (Homework has been assigned) ^ (Student submitted the homework) ] then the test should PASS
     */
    @Test
    public void validAssignGrade() {
    	// Expected Result: PASS
    	this.admin.createClass("Potions", 2017, "Snape", 15);
    	this.student.registerForClass("Harry", "Potions", 2017);
    	this.instructor.addHomework("Snape", "Potions", 2017, "Hw1");
    	this.student.submitHomework("Harry", "Hw1", "Hello", "Potions", 2017);
    	this.instructor.assignGrade("Snape", "Potions", 2017, "Hw1", "Harry", 50);
    	Integer grade = this.instructor.getGrade("Potions", 2017, "Hw1", "Harry");
    	assertNotNull(grade);
    }
    
   
    /*
     * If student not enrolled in class, can't assign grade:			BUG
     */
    @Test
    public void studentGradedEnrolledInClass() {
    	// Expected Result: PASS    								This should pass, but it doesn't. --> BUG
      	this.admin.createClass("Potions", 2017, "Snape", 15);
 //   	this.student.registerForClass("Harry", "Potions", 2017);		Student not enrolled in class
    	this.instructor.addHomework("Snape", "Potions", 2017, "Hw1");
    	this.student.submitHomework("Harry", "Hw1", "Hello", "Potions", 2017);
    	this.instructor.assignGrade("Snape", "Potions", 2017, "Hw1", "Harry", 80);
    	Integer grade = this.instructor.getGrade("Potions", 2017, "Hw1", "Harry");
    	assertNull(grade);		// if fails, this means that grade is being assigned although student is not enrolled in class
    }
    
    
    /*
     * If instructor who is not assigned the class attempts to assign a grade, the grade is not valid:		1 BUG
     */
    @Test
    public void graderNotAssignedToClass() {
    	// Expected Result: PASS 									This should pass, but it doesn't. --> BUG
    	this.admin.createClass("Potions", 2017, "Snape", 15);
    	this.student.registerForClass("Harry", "Potions", 2017);
    	this.instructor.addHomework("Snape", "Potions", 2017, "Hw1");
    	this.student.submitHomework("Harry", "Hw1", "Hello", "Potions", 2017);
    	this.instructor.assignGrade("Lupin", "Potions", 2017, "Hw1", "Harry", 80); // wrong teacher assigning grade
    	Integer grade = this.instructor.getGrade("Potions", 2017, "Hw1", "Harry");
    	assertNull(grade);		// if fails, this means that grade is being assigned although wrong instructor is assigning it
    }
  
    /*
     *  If assignment does not exist, can't assign a grade to it: 					OK
     */
    @Test
    public void homeworkNotAssigned() {
    	// Expected Result: PASS			
      	this.admin.createClass("Potions", 2017, "Snape", 15);
    	this.student.registerForClass("Harry", "Potions", 2017);
//    	this.instructor.addHomework("Snape", "Potions", 2017, "Hw1");			Homework not assigned
    	this.student.submitHomework("Harry", "Hw1", "Hello", "Potions", 2017);
    	this.instructor.assignGrade("Snape", "Potions", 2017, "Hw1", "Harry", 80);
    	Integer grade = this.instructor.getGrade("Potions", 2017, "Hw1", "Harry");
    	assertNull(grade);
    }

    
    /*
     *  If student didn't submit assignment, can't assign grade to student: 		BUG
     */
    @Test
    public void studentSubmittedAssignment() {
    	// Expected Result: PASS									This should pass, but it doesn't. --> BUG			
      	this.admin.createClass("Potions", 2017, "Snape", 15);
    	this.student.registerForClass("Harry", "Potions", 2017);
    	this.instructor.addHomework("Snape", "Potions", 2017, "Hw1");
//    	this.student.submitHomework("Harry", "Hw1", "Hello", "Potions", 2017); 			Student didn't submit assignment
    	this.instructor.assignGrade("Snape", "Potions", 2017, "Hw1", "Harry", 80);
    	Integer grade = this.instructor.getGrade("Potions", 2017, "Hw1", "Harry");
    	assertNull(grade);		// if fails, then grade is being assigned although student didn't submit homework
    }   
    
}
