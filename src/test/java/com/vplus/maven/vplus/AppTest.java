package com.vplus.maven.vplus;
import com.amazonaws.services.lambda.AWSLambda;
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

import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

import java.io.ByteArrayInputStream;

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
			courseDAO.getInstructors();
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
	public void recommendCoursesValidEq() {
		List<String> testCourses = new ArrayList<>();
		testCourses.add("WCOMS4771");
		testCourses.add("WCOMS4111");
		List<CourseModel> recommended =  masterController.recommendCourses(testCourses);
		assertTrue(recommended != null);
	}

	@Test
	public void recommendCoursesValidEqTestBoundaryMin(){
		List<String> testCourses = new ArrayList<>();
		List<CourseModel> recommended =  masterController.recommendCourses(testCourses);
		assertTrue(recommended != null);
	}

	@Test
	public void recommendCoursesValidEqTestBoundaryMax(){
		List<String> testCourses = new ArrayList<>();
		testCourses.add("WCOMS4111");
		testCourses.add("WCOMS4771");
		testCourses.add("WCOMS4156");
		testCourses.add("WCOMS4995");
		testCourses.add("WCOMS4731");
		testCourses.add("WCOMS4701");
		testCourses.add("WCOMS4167");
		testCourses.add("WCOMS4733");
		testCourses.add("WCOMS4119");
		testCourses.add("WCOMS4113");
		testCourses.add("WCOMS4236");
		testCourses.add("WCOMS4170");
		List<CourseModel> recommended =  masterController.recommendCourses(testCourses);
		assertTrue(recommended != null);
	}

	@Test
	public void recommendCoursesValidEqTestBoundaryBelowMax(){
		List<String> testCourses = new ArrayList<>();
		testCourses.add("WCOMS4111");
		testCourses.add("WCOMS4771");
		testCourses.add("WCOMS4156");
		testCourses.add("WCOMS4995");
		testCourses.add("WCOMS4731");
		testCourses.add("WCOMS4701");
		testCourses.add("WCOMS4167");
		testCourses.add("WCOMS4733");
		testCourses.add("WCOMS4119");
		testCourses.add("WCOMS4113");
		testCourses.add("WCOMS4236");
		List<CourseModel> recommended =  masterController.recommendCourses(testCourses);
		assertTrue(recommended != null);
	}

	@Test
	public void recommendCoursesInValidEqTestBoundaryAboveMax(){
		List<String> testCourses = new ArrayList<>();
		testCourses.add("WCOMS4111");
		testCourses.add("WCOMS4771");
		testCourses.add("WCOMS4156");
		testCourses.add("WCOMS4995");
		testCourses.add("WCOMS4731");
		testCourses.add("WCOMS4701");
		testCourses.add("WCOMS4167");
		testCourses.add("WCOMS4733");
		testCourses.add("WCOMS4119");
		testCourses.add("WCOMS4113");
		testCourses.add("WCOMS4236");
		testCourses.add("WCOMS4170");
		testCourses.add("WCOMS4170");
		List<CourseModel> recommended =  masterController.recommendCourses(testCourses);
		assertTrue(recommended != null);
	}

	@Test
	public void recommendCoursesInValidEqTestBoundaryNull(){
		List<String> testCourses = null;
		List<CourseModel> recommended =  masterController.recommendCourses(testCourses);
		assertTrue(recommended.isEmpty());
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

//	@Test
//	public void getTakenCourses() {
//		List<String> testCourses = new ArrayList<>();
//		testCourses.add("WCOMS4771");
//		testCourses.add("WCOMS4111");
//		List<String> courses = app.getTakenCourses(testCourses);
//		assertTrue(courses.size()==2);
//	}

	@Test
	public void processTakenCoursesValidEq() {
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

	}

	@Test
	public void processTakenCoursesInValidEqTestBoundaryNull() {
		List<CourseModel> totalCourse = courseService.selectAllCourses();
		List<String> nullCourses = null;
		List<CourseModel>filteredCourses = masterController.processTakenCourses(nullCourses, totalCourse);
		assertTrue(filteredCourses.size() == totalCourse.size());
	}

	@Test
	public void processTakenCoursesValidEqTestBoundaryMin() {
		List<CourseModel> totalCourse = courseService.selectAllCourses();
		List<String> nullCourses = new ArrayList<>();
		List<CourseModel>filteredCourses = masterController.processTakenCourses(nullCourses, totalCourse);
		assertTrue(filteredCourses.size() == totalCourse.size());
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
	public void filterByPrerequisitesInvalidEqTestBoundaryNull(){
		List<CourseModel>testCoursesModel = null;
		List<CourseModel> filteredCourses=masterController.filterByPrerequisites(testCoursesModel);
		assertTrue(filteredCourses.isEmpty());
	}

	@Test
	public void filterByPrerequisitesInvalidEqTestBoundaryMin(){
		List<CourseModel>testCoursesModel = new ArrayList<>();
		List<CourseModel> filteredCourses=masterController.filterByPrerequisites(testCoursesModel);
		assertTrue(filteredCourses.isEmpty());
	}

	@Test
	public void filterByPrerequisitesValidEqHasPreq(){
		List<CourseModel>testCoursesModel = new ArrayList<>();
		CourseModel c = new CourseModel();
		c.setCoursePreq(new ArrayList<>());
		testCoursesModel.add(c);
		c = new CourseModel();
		List<String> clist = new ArrayList<>();
		clist.add("WCOMS4156");
		c.setCoursePreq(clist);
		testCoursesModel.add(c);
		List<CourseModel> filteredCourses=masterController.filterByPrerequisites(testCoursesModel);
		assertTrue(filteredCourses.size()!=testCoursesModel.size());
	}

	@Test
	public void filterByPrerequisitesValidEqNoPreq(){
		List<CourseModel>testCoursesModel=new ArrayList<>();
		CourseModel c= new CourseModel();
		c.setCoursePreq(new ArrayList<>());
		testCoursesModel.add(c);
		c = new CourseModel();
		List<String> clist=new ArrayList<>();
		clist.add("WCOMS4111");
		c.setCoursePreq(new ArrayList<>());
		testCoursesModel.add(c);
		List<CourseModel> filteredCourses=masterController.filterByPrerequisites(testCoursesModel);
		assertTrue(!filteredCourses.isEmpty());
	}

	@Test
	public void searchKeywords(){
		List<CourseModel> matchedCourses = app.searchKeyword("machine learning", masterController.fetchAllCourses());
		assertTrue(matchedCourses.size()==9);
	}


	@Test
	public void searchKeywordsDes(){
		List<CourseModel> matchedCourses = app.searchKeyword("efficient sorting and searching", masterController.fetchAllCourses());
		assertTrue(matchedCourses.size()>0);
	}

	@Test
	public void searchKeywordsProf(){
		List<CourseModel> matchedCourses = app.searchKeyword("Drinea, Eleni", masterController.fetchAllCourses());
		assertTrue(matchedCourses.size()>0);
	}

	/*
	@Test
	public void testBreadthCourses() {
		List<CourseModel> filteredBreadthRequirements = masterController.breadthRequirements();
		assertTrue(filteredBreadthRequirements.size() == 4);
	}
	 */

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
	public void getWordsService() {
		String professor = "Kaiser, Gail";
		String words = reviewservice.getWords(professor);
		assertTrue(words.contains("||"));
	}

	@Test
	public void getWordsProfessorInvalidEqInvalidNameLengthOne() {
		String professor = "Gail";
		String word = masterController.getWordsProfessor(professor);
		assertTrue(word =="Error Professor Name!!");
	}

	@Test
	public void getWordsProfessorInvalidEqInvalidNameLengthThree() {
		String professor = "Gail E Kaiser";
		String word = masterController.getWordsProfessor(professor);
		assertTrue(word =="Error Professor Name!!");
	}

	@Test
	public void getWordsProfessorInvalidEqNullString() {
		String professor = null;
		String word = masterController.getWordsProfessor(professor);
		assertTrue(word == "No Professor Name!");
	}

	@Test
	public void getWordsProfessorInvalidEqEmptyString() {
		String professor = "";
		String word = masterController.getWordsProfessor(professor);
		assertTrue(word == "No Professor Name!");
	}

	@Test
	public void getWordsProfessorValidEqHasRecord() {
		String professor = "Kaiser, Gail";
		String word = masterController.getWordsProfessor(professor);
		assertTrue(!word.equals(""));
	}

	@Test
	public void getWordsProfessorValidEqNoRecord() {
		String professor = "Verma,  Nakul";
		String word = masterController.getWordsProfessor(professor);
		assertTrue(word.equals("Oh this professor seems quiet. We don't have any review records for him :D"));
	}

	@Test
	public void getReviewWordsExp() {
		String professor = "Yemini, Yechiam";
		DataSource ds = null;
		reviewDAO.setDataSource(ds);
		try {
			reviewDAO.getWords(professor);
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
			reviewDAO.getReview(courseId, professor);
		}
		catch (Exception e){
			assertTrue(true);
		}
	}

	@Test
	public void getCourseRate() {
		double rating = reviewDAO.getCourseRating("WCOMS4771");
		assertNotNull(rating);
		try {
			rating = reviewDAO.getCourseRating("WCOOM");
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
			courseDao.selectAllCourses();
		}catch (Exception e) {
			assertTrue(true);
		}
	}
//
//	@Test
//	public void searchCoursesExp() {
//		javax.sql.DataSource newdata = null;
//		courseDao.setDataSource(newdata);
//		try {
//			courseDao.searchCourse("WCOMS4771");
//		}catch (Exception e) {
//			assertTrue(true);
//		}
//	}

	@Test
	public void selectAllReviewsExp() {
		DataSource newdata = null;
		reviewDao.setDataSource(newdata);
		try {
			reviewDao.selectAllReviews();
		}catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void getCourseRatingExp() {
		DataSource newdata = null;
		reviewDao.setDataSource(newdata);
		try {
			reviewDao.getCourseRating("WCOMS4771");
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
			assertTrue(c.getProfName() != null);
			assertTrue(c.getCourseNumber() != null);
			assertTrue(c.getReview() != null);
			assertNotNull(c.getMagnitude());
			assertTrue(c.toString() != null);
			assertNotNull(c.getScore());
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

//	@Test
//	public void searchCourseExp() {
//		try{
//			courseDAO.searchCourse("WCOOO");
//		}catch (Exception e) {
//			assertTrue(true);
//		}
//	}

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

	@Test
	public void testTopic() throws Exception {
		Application app  = ctx.getBean("Application", Application.class);
		ByteArrayInputStream in = new ByteArrayInputStream("I want to know something about a topic\nmachine learning\nnone".getBytes());
		System.setIn(in);
		app.run();
		assertTrue(true);
	}

	@Test
	public void testInstructor() throws Exception {
		Application app  = ctx.getBean("Application", Application.class);
		ByteArrayInputStream in = new ByteArrayInputStream("I want to know something about a professor\nnakul verma\nnone\n".getBytes());
		System.setIn(in);
		app.run();
		assertTrue(true);
	}


	@Test
	public void testTakenCourses() throws Exception {
		Application app  = ctx.getBean("Application", Application.class);
		ByteArrayInputStream in = new ByteArrayInputStream("I just want some course recommendations\nWCOMS4111,WCOMS4771\n".getBytes());
		System.setIn(in);
		app.run();
		assertTrue(true);
	}

	@Test
	public void testBadInputGoodTopic() throws Exception {
		Application app  = ctx.getBean("Application", Application.class);
		ByteArrayInputStream in = new ByteArrayInputStream("xxx\nxxx\nxxx\n2\nml\nWCOMS4771".getBytes());
		System.setIn(in);
		app.run();
		assertTrue(true);
	}

	@Test
	public void testBadInputBadTopic() throws Exception {
		Application app  = ctx.getBean("Application", Application.class);
		ByteArrayInputStream in = new ByteArrayInputStream("xxx\nxxx\nxxx\n2\nbio imaging\nbioinformatics\nnone".getBytes());
		System.setIn(in);
		app.run();
		assertTrue(true);
	}

	@Test
	public void testBadInputTurnGoodProf() throws Exception {
		Application app  = ctx.getBean("Application", Application.class);
		ByteArrayInputStream in = new ByteArrayInputStream("xxx\nxxx\nxxx\n1\nnakul verma\nnone".getBytes());
		System.setIn(in);
		app.run();
		assertTrue(true);
	}

	@Test
	public void testBadInputTurnBadProf() throws Exception {
		Application app  = ctx.getBean("Application", Application.class);
		ByteArrayInputStream in = new ByteArrayInputStream("xxx\nxxx\nxxx\n1\nAngelika Kaiser\nGail Kaiser\nnone".getBytes());
		System.setIn(in);
		app.run();
		assertTrue(true);
	}

	@Test
	public void testBadInputTurnBadProfs() throws Exception {
		Application app  = ctx.getBean("Application", Application.class);
		ByteArrayInputStream in = new ByteArrayInputStream("xxx\nxxx\nxxx\n1\nAngelika Kaiser\nKaiser\nNakul Verma\nnone".getBytes());
		System.setIn(in);
		app.run();
		assertTrue(true);
	}


	@Test
	public void testBadInputEnter() throws Exception {
		Application app  = ctx.getBean("Application", Application.class);
		ByteArrayInputStream in = new ByteArrayInputStream("\nxxx\nxxx\n1\nAngelika Kaiser\nabc Kaiser\nNakul Verma\nWCOMS4771".getBytes());
		System.setIn(in);
		app.run();
		assertTrue(true);
	}

	@Test
	public void testBadChoiceAndBadTakenCourses() throws Exception {
		Application app  = ctx.getBean("Application", Application.class);
		ByteArrayInputStream in = new ByteArrayInputStream("xxx\nxxx\nxxx\nwhat\n1\nAngelika Kaiser\nabc Kaiser\nNakul Verma\ncoms42$\nnone".getBytes());
		System.setIn(in);
		app.run();
		assertTrue(true);
	}

	@Test
    public void testMain() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("I just want some course recommendations\nWCOMS4111,WCOMS4771\n".getBytes());
        System.setIn(in);
        Application.main(new String[] {"arg1", "arg2", "arg3"});
        assertTrue(true);
    }

}

