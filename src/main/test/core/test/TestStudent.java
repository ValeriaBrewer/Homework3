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
 * 2 Bugs found with this test file.
 */
public class TestStudent {

	private IAdmin admin; 
	private IInstructor instructor;
    private IStudent student;
    
    @Before
    public void setupStudent() {
        this.student = new Student();
    }
    @Before
    public void setupAdmin() {
    	this.admin = new Admin();
    }
    @Before
    public void setupInstructor() {
    	this.instructor = new Instructor();
    }
    
    /*
     * Test Function:
     *     void registerForClass(String studentName, String className, int year);
     *     
     *     Student can only register for class if class exists:							OK
     *     Student can only register for class if it hasn't met its capacity limit:		BUG
     */
    
    /*
     *  Class/year pair must exist in order for student to enroll. If it doesn't exist, student can't enroll
     */
    @Test
    public void classYearExists1A() {
    	// Expected Result: PASS
    	this.admin.createClass("Dark Arts", 2017, "Moody", 20);
    	this.student.registerForClass("Dracco", "Dark Arts", 2017);
    	assertTrue(this.student.isRegisteredFor("Dracco", "Dark Arts", 2017));
    	
    }
    @Test
    public void classYearExists1B() {
    	// Expected Result: FAIL
//    	this.admin.createClass("Dark Arts", 2017, "Moody", 20);   	
    	this.student.registerForClass("Dracco", "Dark Arts", 2017);    
    	assertTrue(this.student.isRegisteredFor("Dracco", "Dark Arts", 2017));

    }
    
    /*
     *  Class has not met its enrollment capacity. If class has met capacity, student can't enroll
     */
    @Test
    public void classCapacityNotMet1() {
    	// Expected Result: PASS
    	this.admin.createClass("Dark Arts", 2017, "Moody", 5);
    	this.student.registerForClass("Dracco", "Dark Arts", 2017);
    	this.student.registerForClass("Crabb", "Dark Arts", 2017);
    	this.student.registerForClass("Goyle", "Dark Arts", 2017);
    	assertTrue(this.student.isRegisteredFor("Goyle", "Dark Arts", 2017));
    }
    @Test
    public void classCapacityNotMet2() {
    	// Expected Result: FAIL										This should fail, but it doesn't. --> BUG
    	this.admin.createClass("Dark Arts", 2017, "Moody", 2);
    	this.student.registerForClass("Dracco", "Dark Arts", 2017);
    	this.student.registerForClass("Crabb", "Dark Arts", 2017);
    	this.student.registerForClass("Goyle", "Dark Arts", 2017);
    	assertTrue(this.student.isRegisteredFor("Goyle", "Dark Arts", 2017));
    }  
    @Test
    public void classCapacityNotMetCornerCase() {
    	// Expected Result: PASS
    	this.admin.createClass("Dark Arts", 2017, "Moody", 3);		
    	this.student.registerForClass("Dracco", "Dark Arts", 2017);
    	this.student.registerForClass("Crabb", "Dark Arts", 2017);
    	this.student.registerForClass("Goyle", "Dark Arts", 2017);		// student takes last spot available
    	assertTrue(this.student.isRegisteredFor("Goyle", "Dark Arts", 2017));
    }    
  
    
    
    
    /*
     * Testing Function:
     *     void dropClass(String studentName, String className, int year);
     *     
     *     Student must be registered to class in order to drop it: 	OK
     */
      
    /*
     *  Student must be registered for specified class to drop it:			OK
     */
    @Test
    public void studentRegisteredForClass1() {
    	// Expected Result: PASS
    	this.admin.createClass("Potions", 2017, "Snape", 15);
    	this.student.registerForClass("Harry", "Potions", 2017);
    	this.student.dropClass("Harry", "Potions", 2017);
    	assertFalse(this.student.isRegisteredFor("Harry", "Potions", 2017)); 
    }
    
    
    /*
     * Testing Function:
     *     void submitHomework(String studentName, String homeworkName, String answerString, String className, int year);
     *     
     *     Specified homework exists:			OK
     *     Student is registered for class:		BUG
     *     Class taught in current year: 		OK
     */	
    
    /*
     *  Specified homework exists. If it doesn't, student can't submit homework for it:		OK
     */
    @Test
    public void homeworkExists() {
    	// Expected Result: PASS
    	this.admin.createClass("Potions", 2017, "Snape", 15);
    	this.student.registerForClass("Harry", "Potions", 2017);
    	this.instructor.addHomework("Snape", "Potions", 2017, "Hw1");
    	this.student.submitHomework("Harry", "Hw1", "Hello", "Potions", 2017);
    	assertTrue(this.student.hasSubmitted("Harry", "Hw1", "Potions", 2017));
    }    
    @Test
    public void homeworkExists2() {
    	// Expected Result: PASS
    	this.admin.createClass("Potions", 2017, "Snape", 15);
    	this.student.registerForClass("Harry", "Potions", 2017);
//    	this.instructor.addHomework("Snape", "Potions", 2017, "Hw1");			// Hw does not exist
    	this.student.submitHomework("Harry", "Hw1", "Hello", "Potions", 2017);
    	assertFalse(this.student.hasSubmitted("Harry", "Hw1", "Potions", 2017));
    }
    
    /*
     *  Student is registered for specified class. If not, student can't submit homework for class:			BUG
     */
    @Test
    public void studentRegisteredForClass3() {
    	// Expected Result: PASS
    	this.admin.createClass("Potions", 2017, "Snape", 15);
    	this.student.registerForClass("Harry", "Potions", 2017);
    	this.instructor.addHomework("Snape", "Potions", 2017, "Hw1");
    	this.student.submitHomework("Harry", "Hw1", "Hello", "Potions", 2017);
    	assertTrue(this.student.hasSubmitted("Harry", "Hw1", "Potions", 2017));
    }
    @Test
    public void studentRegisteredForClass4() {
    	// Expected Result: PASS											This should pass, but it doesn't. --> BUG
    	this.admin.createClass("Potions", 2017, "Snape", 15);
//    	this.student.registerForClass("Harry", "Potions", 2017);			// Student not registered for class
    	this.instructor.addHomework("Snape", "Potions", 2017, "Hw1");
    	this.student.submitHomework("Harry", "Hw1", "Hello", "Potions", 2017);
    	assertFalse(this.student.hasSubmitted("Harry", "Hw1", "Potions", 2017));
    }
    
    /*
     * Class is taught in current year. If not, student can't submit homework for class:		OK
     */
    @Test
    public void classTaughtCurrently() {
    	// Expected Result: PASS
    	this.admin.createClass("Potions", 2017, "Snape", 15);		// class taught in current year
    	this.student.registerForClass("Harry", "Potions", 2017);
    	this.instructor.addHomework("Snape", "Potions", 2017, "Hw1");
    	this.student.submitHomework("Harry", "Hw1", "Hello", "Potions", 2017);
    	assertTrue(this.student.hasSubmitted("Harry", "Hw1", "Potions", 2017));
    }
    @Test
    public void classTaughtCurrently2 () {
    	// Expected Result: PASS
    	this.admin.createClass("Potions", 2020, "Snape", 10);		// class taught in future
    	this.student.registerForClass("Harry", "Potions", 2020);
    	this.instructor.addHomework("Snape", "Potions", 2020, "Hw2");
    	this.student.submitHomework("Harry", "Hw1", "Test", "Potions", 2020);
    	assertFalse(this.student.hasSubmitted("Harry", "Hw1", "Potions", 2020));    	
    }
    
}
