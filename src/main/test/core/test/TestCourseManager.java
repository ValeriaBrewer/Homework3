package core.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.Mockito;
import org.mockito.Spy;
import static org.mockito.Mockito.times;


import core.api.ICourseManager;
import core.api.impl.Admin;
import core.api.impl.CourseManager;

/**
 * Tests course manager. Since the Admin implementation is known to be bugging. 
 * 
 * @author Vincent
 *
 */
public class TestCourseManager {

	@Spy
	private Admin admin;
	private ICourseManager courseManager;
	
	@Before
	public void setup() {
		this.admin = Mockito.spy(new Admin());
		this.courseManager = new CourseManager(this.admin);
		setupMocking();
	}

	/*
	 * Shows some initial set-up for the mocking of Admin.
	 * This includes fixing a known bug (year in past is not correctly checked) in the Admin class by Mocking its behavior.
	 * Not all fixes to Admin can be made from here, so for the more complex constraints you can simply Mock the
	 * specific calls to Admin's createClass to yield the correct behavior in the unit test itself.
	 */
	public void setupMocking() {
		Mockito.doNothing().when(this.admin).createClass(Mockito.anyString(), AdditionalMatchers.lt(2017), Mockito.anyString(), Mockito.anyInt());
		Mockito.doNothing().when(this.admin).createClass(Mockito.anyString(), AdditionalMatchers.gt(2016), Mockito.anyString(), AdditionalMatchers.lt(0));
	}

	@Test
	public void testCreateClassCorrect() {
		this.courseManager.createClass("ECS161", 2017, "Instructor", 1);
		assertTrue(this.courseManager.classExists("ECS161", 2017));
	}
	
	@Test
	public void testCreateClassInPast() {
		this.courseManager.createClass("ECS161", 2016, "Instructor", 1);
		assertFalse(this.courseManager.classExists("ECS161", 2016));
	}

	@Test
	public void testCreateClassInFuture() {
		this.courseManager.createClass("ECS161", 2018, "Instructor", 1);
		Mockito.verify(this.admin, Mockito.never()).createClass(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
	}
	
	
	/*
	 * Test: Capacity using Mocking
	 */
	@Test 
	public void testCapacityCorrect() {
		this.courseManager.createClass("ECS161", 2017, "Vincent", 10);
		assertTrue(this.courseManager.classExists("ECS161", 2017));
	}
	
	@Test
	public void testCapacityIncorrect() {
		this.courseManager.createClass("ECS161", 2017, "Vincent", -5);
		assertFalse(this.courseManager.classExists("ECS161", 2017));
	}
	
	@Test
	public void testCapacityCornerCase() {
		this.courseManager.createClass("ECS161", 2017, "Vincent", 0);
		Mockito.verify(this.admin, Mockito.times(1)).createClass(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
	}
	
	
	/*
	 * Test: No more than 2 classes per instructor per year using mocking
	 */
	@Test
	public void testLimitNotReached() {
		this.courseManager.createClass("ECS161", 2017, "Prem", 10);
		this.courseManager.createClass("ECS160", 2017, "Prem", 15);
		assertTrue(this.courseManager.classExists("ECS160", 2017) && this.admin.classExists("ECS161", 2017));
	}
	
	@Test 
	public void testLimitNotReached2() {
		this.courseManager.createClass("ECS160", 2017, "Prem", 10);
		this.courseManager.createClass("ECS161", 2017, "Vincent", 10);
		this.courseManager.createClass("ECS40", 2017, "Vincent", 10);
		assertTrue(this.admin.classExists("ECS40", 2017) && this.admin.classExists("ECS160", 2017) && this.admin.classExists("ECS161", 2017));
	}
	
	@Test
	public void testLimitReached() {
		Mockito.doCallRealMethod().doCallRealMethod().doNothing().when(this.admin).createClass(Mockito.anyString(), Mockito.eq(2017), Mockito.eq("Prem"), AdditionalMatchers.geq(0));
		this.courseManager.createClass("ECS161", 2017, "Prem", 10);
		this.courseManager.createClass("ECS160", 2017, "Prem", 10);
		this.courseManager.createClass("ECS165", 2017, "Prem", 10);
		assertFalse(this.courseManager.classExists("ECS165", 2017));
	}
	
	@Test
	public void testLimitCornerCase() {
		this.courseManager.createClass("ECS160", 2017, "Prem", 10);
		this.courseManager.createClass("ECS161", 2017, "Vincent", 10);
		this.courseManager.createClass("ECS40", 2017, "Vincent", 10);
		this.courseManager.createClass("ECS30", 2017, "Prem", 10);
		assertTrue(this.admin.classExists("ECS30", 2017) && this.admin.classExists("ECS40", 2017) && this.admin.classExists("ECS160", 2017) && this.admin.classExists("ECS161", 2017));
	}
	
	
	/*
	 * Test: No two classes with same name/year using mocking
	 */
	@Test
	public void testClassNameValid() {
		this.courseManager.createClass("ECS161", 2017, "Prem", 10);
		this.courseManager.createClass("ECS40", 2017, "Vincent", 10);
		assertTrue(this.courseManager.classExists("ECS161", 2017));
	}
	
	@Test
	public void testClassNameValid2() {
		this.courseManager.createClass("ECS161", 2017, "Prem", 10);
		this.courseManager.createClass("ECS140", 2017, "Prem", 10);
		assertTrue(this.courseManager.classExists("ECS161", 2017) && this.courseManager.classExists("ECS140", 2017));
	}
	
	@Test
	public void testClassNameInvalid() {
		Mockito.doCallRealMethod().doNothing().when(this.admin).createClass(Mockito.eq("ECS161"), Mockito.eq(2017), Mockito.anyString(), AdditionalMatchers.geq(0));
		this.courseManager.createClass("ECS161", 2017, "Prem", 20);
		this.courseManager.createClass("ECS161", 2017, "Vincent", 15);
		assertTrue(this.courseManager.getClassInstructor("ECS161", 2017) == "Prem");
	}
}
