package com.vplus.maven.vplus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.vplus.dao.*;
import com.vplus.service.*;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.contrib.java.lang.system.SystemOutRule;
import java.io.FileNotFoundException;
import com.vplus.Application;
import com.vplus.controller.IMasterController;
import com.vplus.models.CourseModel;
import org.junit.Before; // for @Before
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest
{
	@Autowired
	private IMasterController masterController;
	@Autowired
	private ICourseService courseService;
	@Autowired
	private ICourseDAO courseDAO;

	@Autowired
	ICourseDAO DAO_test;

	private Application app;

	@Before
	public void beforeEachTest() {
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		app  = ctx.getBean("Application", Application.class);
		ctx.getAutowireCapableBeanFactory().autowireBeanProperties(this,
				AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
	}

	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	@Test
	public void test_DAO() {
		ctx.getAutowireCapableBeanFactory().autowireBeanProperties(this,
				AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
		List<CourseModel> res = DAO_test.selectAllCourses();
		if (res.isEmpty() ) {
			assertTrue(false);
		}

	}
//
//	@Test
//	public void testAppRun() throws Exception {
//		Application app  = ctx.getBean("Application", Application.class);
//		final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
//		app.run();
//
//		assertNotNull(systemOutRule.getLog());
//	}


//	@Test
//	public void testAppRun() throws Exception {
//		Application app  = ctx.getBean("Application", Application.class);	    
//	    final SystemOutRule systemOutRule = new SystemOutRule().enableLog();    
//	    app.run();
//        
//	    assertNotNull(systemOutRule.getLog());
//	}
	

	@Test
	public void readCourses() {
		List<String> courses=null;
		try {
			courses=app.readTakenCourses();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			assertTrue( true );
		}

		assertTrue(courses!=null);

	}

	@Test
	public void recommendCourses() {
		List<String> testCourses = new ArrayList<>();
		testCourses.add("WCOMS4771");
		testCourses.add("WCOMS4111");
		List<CourseModel> recommended =  masterController.recommendCourses(testCourses);
		assertTrue(recommended != null);
		testCourses=new ArrayList<>();
		recommended=masterController.recommendCourses(testCourses);
		assertTrue(recommended != null);
	}


	@Test
	public void selectAllCourses() {
		List<CourseModel> totalcourseModel=courseService.selectAllCourses();
		assertTrue(totalcourseModel.size()==93);
	}

	@Test
	public void processTakenCourses() {
		List<CourseModel> totalCourse = courseService.selectAllCourses();
		List<String> testCourses = new ArrayList<>();
		testCourses.add("WCOMS4771");
		testCourses.add("WCOMS4111");
		List<CourseModel> filteredCourses=masterController.processTakenCourses(testCourses,totalCourse);
		filteredCourses.forEach(c-> {
			if (c.getCourseNumber().equals(testCourses.get(0)) || c.getCourseNumber().equals(testCourses.get(1))) {
				assertTrue(false);
			}
		});
		List<String> nullCourses=new ArrayList<>();
		filteredCourses=masterController.processTakenCourses(nullCourses,totalCourse);
		assertTrue(filteredCourses.size()==totalCourse.size());
	}

	@Test
	public void filterByPrerequisites(){
		List<CourseModel>testCoursesModel=new ArrayList<>();
		CourseModel c= new CourseModel();
		c.setCoursePreq(new ArrayList<>());
		testCoursesModel.add(c);
		c= new CourseModel();
		List<String> clist=new ArrayList<>();
		clist.add("WCOMS4111");
		c.setCoursePreq(clist);
		testCoursesModel.add(c);
		List<CourseModel> filteredCourses=masterController.filterByPrerequisites(testCoursesModel);
		assertTrue(filteredCourses.size()!=testCoursesModel.size());
	}

	@Test
	public void searchKeywords(){
		HashSet<String> matchedCourses = app.searchKeywords("machine learning", masterController.fetchAllCourses());
		assertTrue(matchedCourses.contains("Artificial Intelligence WCOMS4701")&& matchedCourses.size()==6);
	}

	@Test
	public void testBreadthCourses() {
		List<CourseModel> filteredBreadthRequirements = masterController.breadthRequirements();
		assertTrue(filteredBreadthRequirements.size() == 4);
	}
//
//	@Test
//    public void filterByPrerequisites(){
//		List<CourseModel> filteredCourses=masterController.filterByPrerequisites(testCoursesModel);
//	    assertTrue(filteredCourses.size()!=testCoursesModel.size());
//    }

}

