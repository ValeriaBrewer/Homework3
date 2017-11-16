package core.test;

import core.api.IAdmin;
import core.api.IStudent;
import core.api.impl.Admin;
import core.api.impl.Student;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/*
 * 4 Bugs found with this test file
 */

public class TestAdmin {

    private IAdmin admin;
    private IStudent student;

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
     *     void createClass(String className, int year, String instructorName, int capacity)
     *     
     *     Class name and year pair must be unique:								BUG
     *     No instructor can be assigned to more that 2 courses in a year:		BUG
     *     Capacity must be greater than 0:										BUG
     */
    
    /*
     *  Class name and year pair must be unique:							1 BUG
     */
    @Test
    public void classYearUnique1() {    
    	// Expected Result: PASS				
        this.admin.createClass("Potions", 2017, "Snape", 10);	
        this.admin.createClass("Potions", 2018, "Slughorn", 10);	// unique class is created, doesn't overwrite first class
        String professor = this.admin.getClassInstructor("Potions", 2017);
        assertTrue(professor.equals("Snape"));
    }
    @Test
    public void classYearUnique2() {
    	// Expected Result: PASS						This should pass, but it fails instead. --> BUG
        this.admin.createClass("Potions", 2017, "Snape", 10);	
        this.admin.createClass("Potions", 2017, "Slughorn", 10);// this should not create class since name & year not unique
        String professor = this.admin.getClassInstructor("Potions", 2017);
        assertTrue(professor.equals("Snape"));  // Fail means that second class was created although it should not have been
    }
    
    /*
     * Year is not in the past:						1 BUG
     */
    @Test
    public void yearNotPast() {
    	// Expected Result: PASS
    	this.admin.createClass("Potions", 2017, "Snape", 10);
    	assertTrue(this.admin.classExists("Potions", 2017));
    }
    @Test
    public void yearPast() {
    	// Expected Result: PASS						This should pass, but it fails instead. --> BUG
    	this.admin.createClass("Potions", 2010, "Snape", 10);
    	assertFalse(this.admin.classExists("Potions", 2010));
    }
    
    /*
     * No instructor can be assigned to more that 2 courses in a year: 						1 BUG
     */
    @Test
    public void instructorCourseLimitNotExceeded() {
    	// Expected Result: PASS
        this.admin.createClass("Test", 2017, "Professor Snape", 15);
        assertTrue(this.admin.classExists("Test", 2017));
    }
    @Test
    public void instructorCourseLimitCornerCase() {
    	// Corner-case with an instructor being assigned to 2 classes
    	// Expected Result: PASS
        this.admin.createClass("Test", 2017, "Professor Snape", 15);
        this.admin.createClass("Test2", 2017, "Professor Snape", 15);
        assertTrue(this.admin.classExists("Test2", 2017) && this.admin.classExists("Test", 2017));
    }
    @Test
    public void instructorCourseLimitNotExceeded2() {
    	// Expected Result: PASS									This should pass, but it fails instead. --> BUG
        this.admin.createClass("Test", 2017, "Professor Snape", 15);
        this.admin.createClass("Test2", 2017, "Professor Snape", 15);
        this.admin.createClass("Test3", 2017, "Professor Snape", 15);
        assertFalse(this.admin.classExists("Test3", 2017));			// Fail means that Test3 exists although it shouldn't
    }
  
    
    /* 
     * Capacity must be greater than 0:							1 BUG				
     */
    @Test
    public void capacityValid() {
    	// Expected Result: PASS
        this.admin.createClass("Test", 2017, "Professor Snape", 15);
        assertTrue(this.admin.classExists("Test", 2017));        
    }
    @Test
    public void capacityValidCornerCase() {
    	// Corner-case where capacity = 0
    	// Expected Result: PASS
        this.admin.createClass("Test", 2017, "Professor Snape", 0);
        assertTrue(this.admin.classExists("Test", 2017));     
    }      
    @Test
    public void capacityValid2() {
    	// Expected Result: PASS									This should pass, but it fails instead. --> BUG
        this.admin.createClass("Test", 2017, "Professor Snape", -1);
        assertFalse(this.admin.classExists("Test", 2017));        // Fail means that class exists although it shouldn't
    }    
    
    
    
    
    /*
     * Testing Function: 
     *     void changeCapacity(String className, int year, int capacity);
     *     
     *     New Capacity should be greater than or equal to the number of enrolled students: 	1 BUG	
     */

    @Test
    public void capacityChangeValid() {
    	// Expected Result: PASS
    	int newCapacity = 10; 
    	this.admin.createClass("Potions", 2017, "Professor Snape", 15);
    	this.student.registerForClass("Harry Potter", "Potions", 2017);
    	this.student.registerForClass("Dracco Malfoy", "Potions", 2017);
    	this.student.registerForClass("Hermione Granger", "Potions", 2017);
    	this.admin.changeCapacity("Potions", 2017, newCapacity);    	
    	int Capacity = this.admin.getClassCapacity("Potions", 2017);
    	assertEquals(Capacity, newCapacity);
    }
    @Test
    public void capacityChangeValidCorner1() {
    	// Corner-case where new capacity == # of enrolled students
    	// Expected Result: PASS
    	int newCapacity = 3; 
    	this.admin.createClass("Potions", 2017, "Professor Snape", 15);
    	this.student.registerForClass("Harry Potter", "Potions", 2017);
    	this.student.registerForClass("Dracco Malfoy", "Potions", 2017);
    	this.student.registerForClass("Hermione Granger", "Potions", 2017);
    	this.admin.changeCapacity("Potions", 2017, newCapacity); 
    	int Capacity = this.admin.getClassCapacity("Potions", 2017);
    	assertEquals(Capacity, newCapacity);
    }
    @Test
    public void capacityChangeValidCorner2() {
    	// Corner-case where no students enrolled in class
    	// Expected Result: PASS
    	int newCapacity = 10; 
    	this.admin.createClass("Potions", 2017, "Professor Snape", 15);
    	this.admin.changeCapacity("Potions", 2017, newCapacity);    
    	int Capacity = this.admin.getClassCapacity("Potions", 2017);
    	assertEquals(Capacity, newCapacity);
    }
    @Test
    public void capacityChangeValidCorner3() {
    	// Corner-case where initial capacity is 0 
    	// Expected Result: PASS
    	int newCapacity = 10; 
    	this.admin.createClass("Potions", 2017, "Professor Snape", 0);
    	this.admin.changeCapacity("Potions", 2017, newCapacity);   
    	int Capacity = this.admin.getClassCapacity("Potions", 2017);
    	assertEquals(Capacity, newCapacity);
    }
    @Test
    public void capacityChangeValid2() {
    	// Expected Result: PASS 					This should pass, but it fails instead. --> BUG
    	int newCapacity = 2; 
    	this.admin.createClass("Potions", 2017, "Professor Snape", 5);
    	this.student.registerForClass("Harry Potter", "Potions", 2017);
    	this.student.registerForClass("Dracco Malfoy", "Potions", 2017);
    	this.student.registerForClass("Hermione Granger", "Potions", 2017);
    	this.admin.changeCapacity("Potions", 2017, newCapacity);  
    	int Capacity = this.admin.getClassCapacity("Potions", 2017);
    	assertTrue(Capacity == 5);			// Capacity change should not take affect since its an invalid change
    }

}
