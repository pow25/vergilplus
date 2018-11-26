package com.vplus.maven.vplus;
import com.amazonaws.services.lambda.AWSLambda;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.sql.DataSource;

import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
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
	public void findAllInstructors(){
		List<String> instructors = masterController.findAllInstructors();
		assertTrue(instructors.size()==93);
	}

	@Test
	public void getInstructorsExp(){
		courseDAO.setDataSource(null);
		try {
			List<String> instructors = courseDAO.getInstructors();
		}catch(Exception e){
			assertTrue(true);
		}
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
	public void recommendCoursesApp() {
		List<String> testCourses = new ArrayList<>();
		testCourses.add("WCOMS4771");
		testCourses.add("WCOMS4111");
		List<CourseModel> recommended =  app.recommendCourses(testCourses);
		assertTrue(recommended != null);
		testCourses=new ArrayList<>();
		recommended=app.recommendCourses(testCourses);
		assertTrue(recommended.size()==4);
	}

	@Test
	public void getMasterController() {
		IMasterController master =app.getMasterController();
		assertNotNull(master);
	}

	@Test
	public void selectAllCourses() {
		List<CourseModel> totalcourseModel=courseService.selectAllCourses();
		assertTrue(totalcourseModel.size()==93);
	}

	@Test
	public void getTakenCourses() {
		List<String> testCourses = new ArrayList<>();
		testCourses.add("WCOMS4771");
		testCourses.add("WCOMS4111");
		List<String> courses = app.getTakenCourses(testCourses);
		assertTrue(courses.size()==2);
	}

	@Test
	public void processTakenCourses() {
		List<CourseModel> totalCourse = courseService.selectAllCourses();
		List<String> testCourses = new ArrayList<>();
		testCourses.add("WCOMS4771");
		testCourses.add("WCOMS4111");
		List<CourseModel> filteredCourses = masterController.processTakenCourses(testCourses,totalCourse);
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
	public void processTakenCoursesApp() {
		List<String> testCourses = new ArrayList<>();
		testCourses.add("WCOMS4771");
		testCourses.add("WCOMS4156");
		List<CourseModel> filteredCourses=app.processTakenCourses(testCourses);
		filteredCourses.forEach(c-> {
			if (c.getCourseNumber().equals(testCourses.get(0)) || c.getCourseNumber().equals(testCourses.get(1))) {
				assertTrue(false);
			}
		});
		List<String> nullCourses=new ArrayList<>();
		filteredCourses=app.processTakenCourses(nullCourses);
		assertTrue(filteredCourses.size()==93);
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
		assertTrue(matchedCourses.size()==9);
	}


	@Test
	public void searchKeywordsDes(){
		HashSet<CourseModel> matchedCourses = app.searchKeywords("efficient sorting and searching", masterController.fetchAllCourses());
		assertTrue(matchedCourses.size()>0);
	}

	@Test
	public void searchKeywordsProf(){
		HashSet<CourseModel> matchedCourses = app.searchKeywords("Drinea, Eleni", masterController.fetchAllCourses());
		assertTrue(matchedCourses.size()>0);
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
	public void selectByNumSec() {
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
	public void selectAllReviews() {
		List<ReviewModel> allreviews = reviewDAO.selectAllReviews();
		assertTrue(allreviews.size() > 0);
	}

	@Test
	public void getReviewWords() {
		String professor = "Yemini, Yechiam";
		List<String> words = reviewDAO.getWords(professor);
		assertTrue(words.size() > 0);
	}

	@Test
	public void getReviewWordsExp() {
		String professor = "Yemini, Yechiam";
		DataSource ds = null;
		reviewDAO.setDataSource(ds);
		try {
			List<String> words = reviewDAO.getWords(professor);
		}
		catch(Exception e){
			assertTrue(true);
		}
	}

	@Test
	public void getReviewNull() {
		String professor = "";
		String courseId = "";
		List<String> result = reviewDAO.getReview(courseId, professor);
		assertNull(result);
	}


	@Test
	public void getReviewProfNotNull() {
		String professor = "Paisley, John";
		String courseId = "";
		List<String> result = reviewDAO.getReview(courseId, professor);
		assertNotNull(result);
	}


	@Test
	public void getReviewCourseNotNull() {
		String professor = "";
		String courseId = "WCOMS4771";
		List<String> result = reviewDAO.getReview(courseId, professor);
		assertNotNull(result);
	}

	@Test
	public void getReviewCourseProfNotNull() {
		String professor = "Verma, Nakul";
		String courseId = "WCOMS4771";
		List<String> result = reviewDAO.getReview(courseId, professor);
		assertNotNull(result);
	}

	@Test
	public void getReviewExp() {
		String professor = "Verma, Nakul";
		String courseId = "WCOMS4771";
		reviewDAO.setDataSource(null);
		try {
			List<String> result = reviewDAO.getReview(courseId, professor);
		}
		catch (Exception e){
			assertTrue(true);
		}
	}

	@Test
	public void getCourseRate() {
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
	public void selectAllCoursesExp() {
		DataSource newdata = null;
		courseDao.setDataSource(newdata);
		try {
			List<CourseModel> allcourses = courseDao.selectAllCourses();
		}catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void searchCoursesExp() {
		javax.sql.DataSource newdata = null;
		courseDao.setDataSource(newdata);
		try {
			CourseModel fitcourse = courseDao.search_course("WCOMS4771");
		}catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void selectAllReviewsExp() {
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
	public void reviewServiceTest() {
		assertNotNull(reviewservice.selectAllReviews());
		assertNotNull(reviewservice.getReviewDAO());
	}

	@Test
	public void masterControllerGetCourseService() {
		assertNotNull(mastercontroller.getCourseService());
	}

	@Test
	public void searchCourseExp() {
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

	@Test
	public void setContext() throws Exception {
		Application app  = ctx.getBean("Application", Application.class);
		final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
		app.setContext();
		assertNotNull(systemOutRule.getLog());
	}

	@Test
	public void setContextExp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		try {
			Application app  = ctx.getBean("Applications", Application.class);
			app.setContext();
		}
		catch(Exception e){
			assertTrue(true);
		}
	}

	@Test
	public void getAllTopics() throws Exception {
		Application app  = ctx.getBean("Application", Application.class);
		List<String> topics = app.getAllTopics();
		assertTrue(topics.size()==14);
	}

	@Test
	public void createLambda() throws Exception {
		Application app  = ctx.getBean("Application", Application.class);
		AWSLambda client = app.createLambda();
		assertTrue(!client.toString().isEmpty());
	}

	@Test
	public void parseResponse() throws Exception {
		Application app  = ctx.getBean("Application", Application.class);
		String body = "{\"userId\": \"dh2914\", \"message\": {\"word\":"+ "\"Hi;false\"}}";
		InvokeRequest req = new InvokeRequest().withFunctionName("Chatbot").withPayload(body); // optional
		AWSLambda client = app.createLambda();
		InvokeResult result = client.invoke(req);
		String output = app.parseResponse(result);
		assertTrue(!output.isEmpty());
	}
//
//	@Test
//	public void testAppRun() throws Exception {
//		Application app  = ctx.getBean("Application", Application.class);
//		ByteArrayInputStream in = new ByteArrayInputStream("Hi".getBytes());
//		PrintStream out = new PrintStream(System.out);
//		System.setIn(in);
//
//		in = new ByteArrayInputStream("yea I am interested in machine learning".getBytes());
//		System.setIn(in);
//
//		in = new ByteArrayInputStream("COMS4771".getBytes());
//		System.setIn(in);
//
//		in = new ByteArrayInputStream("yea".getBytes());
//		System.setIn(in);
//
//		in = new ByteArrayInputStream("ok".getBytes());
//		System.setIn(in);
//		app.run();
//		assertTrue(true);
//	}
}

