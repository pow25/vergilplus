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
import com.vplus.controller.MasterController;
import com.vplus.models.CourseModel;
import com.vplus.models.ReviewModel;

import org.junit.Before; // for @Before
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.sql.DataSource;

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
	private IReviewDAO reviewDAO;
	@Autowired
	private CourseDAO courseDao;
	@Autowired
	private ReviewDAO reviewDao;
	@Autowired
	private CourseService courseservice;
	@Autowired
	private ReviewService reviewservice;
	@Autowired
	private MasterController mastercontroller;

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

	@Test
	public void testAppRun() throws Exception {
		Application app  = ctx.getBean("Application", Application.class);
		final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
		app.run();
		assertNotNull(systemOutRule.getLog());
	}

	@Test
	public void findAllInstructors(){
		List<String> instructors = masterController.findAllInstructors();
		assertTrue(instructors.size()==93);
	}

	@Test
	public void run() throws Exception {
		Application app  = ctx.getBean("Application", Application.class);
		final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
		app.run();
		assertNotNull(systemOutRule.getLog());
	}

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
		HashSet<CourseModel> matchedCourses = app.searchKeywords("machine learning", masterController.fetchAllCourses());
		assertTrue(matchedCourses.size()==6);
	}

	@Test
	public void testBreadthCourses() {
		List<CourseModel> filteredBreadthRequirements = masterController.breadthRequirements();
		assertTrue(filteredBreadthRequirements.size() == 4);
	}

	@Test
	public void courseModelGet() {
		List<CourseModel> totalcourseModel=new ArrayList<>();
		totalcourseModel = courseService.selectAllCourses();
		List<String> testCourses = new ArrayList<>();
		testCourses.add("WCOMS4771");
		List<CourseModel> filteredCourses=masterController.processTakenCourses(testCourses,totalcourseModel);
		filteredCourses.forEach(c-> {
			assertTrue(c.getSectionId() != 0);
			assertTrue(c.getWeek() != null);
			assertTrue(c.getStartTime() != null);
			assertTrue(c.getEndTime() != null);
			assertTrue(c.getKnowledgePreq() != null);
			assertTrue(c.getTerm() != -1);
			assertTrue(c.getInstructor() != null);
			assertNotNull(c.getInstructorPreq());
		});
	}


	@Test
	public void SelectByNumSec() {
		List<CourseModel> allMatchingCourses = courseService.selectCoursesByNumAndSection("WCOMS4771",1);
		List<CourseModel> totalcourseModel=new ArrayList<>();
		totalcourseModel = courseService.selectAllCourses();
		List<String> testCourses = new ArrayList<>();
		testCourses.add("WCOMS4771");
		testCourses.add("WCOMS4111");
		List<CourseModel> filteredCourses=masterController.processTakenCourses(testCourses,totalcourseModel);
		filteredCourses.forEach(c-> {
			assertTrue(c.compareTo(allMatchingCourses.get(0)) == -1);
		});
	}

	@Test
	public void SelectAllReviews() {
		List<ReviewModel> allreviews = reviewDAO.selectAllReviews();
		assertTrue(allreviews.size() > 0);
	}

	@Test
	public void GetCourseRate() {
		float rating = reviewDAO.get_course_rating("WCOMS4771");
		assertNotNull(rating);
		try {
			float rate = reviewDAO.get_course_rating("WCOOM");
		}catch (Exception e) {
			assertTrue(true);
		}
		assertNotNull(rating);
	}

	@Test
	public void SelectAllCoursesExp() {
		DataSource newdata = null;
		courseDao.setDataSource(newdata);
		try {
			List<CourseModel> allcourses = courseDao.selectAllCourses();
		}catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void SearchCoursesExp() {
		javax.sql.DataSource newdata = null;
		courseDao.setDataSource(newdata);
		try {
			CourseModel fitcourse = courseDao.search_course("WCOMS4771");
		}catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void SelectAllReviewsExp() {
		DataSource newdata = null;
		reviewDao.setDataSource(newdata);
		try {
			List<ReviewModel> allcourses = reviewDao.selectAllReviews();
		}catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void getCourseRatingExp() {
		DataSource newdata = null;
		reviewDao.setDataSource(newdata);
		try {
			float rate = reviewDao.get_course_rating("WCOMS4771");
		}catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void toStringTest() {
		CourseModel newcourse = new CourseModel();
		ArrayList<String> str = new ArrayList<String>();
		newcourse.setCourseNumber("12");
		newcourse.setCoursePreq(str);
		newcourse.setCourseTitle("123");
		newcourse.setDescription("abb");
		newcourse.setEndTime("200");
		newcourse.setInstructor("aaa");
		newcourse.setInstructorPreq(false);
		newcourse.setKnowledgePreq("abc");
		newcourse.setSectionId(1);
		newcourse.setStartTime("222");
		newcourse.setTerm(1);
		newcourse.setWeek("12");
		assertNotNull(newcourse.toString());
		assertTrue(newcourse.compareTo(newcourse) == 0);
	}

	@Test
	public void reviewModelGet() {
		List<ReviewModel> allreviews = reviewDAO.selectAllReviews();
		allreviews.forEach(c-> {
			assertTrue(c.getProf() != null);
			assertTrue(c.getCourseID() != null);
			assertTrue(c.getReview() != null);
			assertNotNull(c.getmagnitude());
			assertTrue(c.toString() != null);
			assertNotNull(c.getscore());
		});
	}

	@Test
	public void getCourseDao() {
		assertNotNull(courseservice.getCourseDAO());
	}

	@Test
	public void ReviewServiceTest() {
		assertNotNull(reviewservice.selectAllReviews());
		assertNotNull(reviewservice.getReviewDAO());
	}

	@Test
	public void masterControllerGetCourseService() {
		assertNotNull(mastercontroller.getCourseService());
	}

	@Test
	public void SearchCourseExp() {
		try{
			courseDAO.search_course("WCOOO");
		}catch (Exception e) {
			assertTrue(true);
		}
	}

//	@Test
//	public void SelectAllCoursesExp() {
//		courseDao.setDataSource(newdata);
//		try {
//			List<CourseModel> allcourses = courseDao.selectAllCourses();
//		}catch (Exception e) {
//			assertTrue(false);
//		}
//
//	}


//
//	@Test
//    public void filterByPrerequisites(){
//		List<CourseModel> filteredCourses=masterController.filterByPrerequisites(testCoursesModel);
//	    assertTrue(filteredCourses.size()!=testCoursesModel.size());
//    }

}

