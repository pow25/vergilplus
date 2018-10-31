package com.vplus.maven.vplus;
import java.io.FileNotFoundException;
import com.vplus.Application;
import com.vplus.controller.IMasterController;
import com.vplus.controller.MasterController;
import com.vplus.models.CourseModel;
import com.vplus.models.TrackModel;
import com.vplus.service.CourseService;
import junit.framework.TestCase;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import static org.junit.Assert.*; // Allows you to use directly assert methods, such as assertTrue(...), assertNull(...)
import org.junit.Test; // for @Test
import org.junit.Before; // for @Before
import java.util.ArrayList;
import java.util.List;

import java.io.PipedOutputStream;
import java.io.PrintStream;
import javax.sql.DataSource;
import java.util.*;

import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vplus.controller.*;
import com.vplus.dao.*;
import com.vplus.models.*;
import com.vplus.service.*;
import com.vplus.*;
import org.junit.Test;
import junit.framework.TestCase;
import static org.junit.Assert.*;

import org.junit.contrib.java.lang.system.SystemOutRule;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Unit test for simple App.
 */
//
////This function is to test the two functions: recommendCourses and readTakenCourses
//@Context()//For Every it will give you a random port number
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)//It will take your test methods in assending order
public class AppTest
{
	//        private CourseService courseService=null;
	@Autowired
	private IMasterController masterController;
	@Autowired
	private ICourseService courseService;
	private Application app;
	private ClassPathXmlApplicationContext ctx;
	private List<String> testCourses;
	private List<CourseModel> testCoursesModel=new ArrayList<>();
	private List<CourseModel> totalcourseModel=new ArrayList<>();
	private String testFile="user_input.json";
	@Before
	public void beforeEachTest() {
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		app  = ctx.getBean("Application", Application.class);
		ctx.getAutowireCapableBeanFactory().autowireBeanProperties(this,
				AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
//
//		CourseModel c1=new CourseModel();
//        c1.setCourseNumber("WCOMS4771");
//        System.out.println(c1);
//        CourseModel c2=new CourseModel();
//        c2.setCourseNumber("WCOMS4111");
//        testCoursesModel.add(c1);
//        testCoursesModel.add(c2);
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

		testCourses=new ArrayList<>();
		testCourses.add("WCOMS4771");
		testCourses.add("WCOMS4111");
		List<CourseModel> recommended =  masterController.recommendCourses(testCourses);
		System.out.println(recommended);
		assertTrue(recommended != null);
	}

	@Test
	public void selectAllCourses() {
		List<CourseModel> totalcourseModel=courseService.selectAllCourses();
		assertTrue(totalcourseModel.size()==93);
	}
	@Test
	public void processTakenCourses() {
		List<CourseModel> filteredCourses=masterController.processTakenCourses(testCourses,totalcourseModel);
		filteredCourses.forEach(c-> {
			if (c.getCourseNumber().equals(testCourses.get(0)) || c.getCourseNumber().equals(testCourses.get(1))) {
				assertTrue(false);
			}
		});
	}
//
//	@Test
//    public void filterByPrerequisites(){
//		List<CourseModel> filteredCourses=masterController.filterByPrerequisites(testCoursesModel);
//	    assertTrue(filteredCourses.size()!=testCoursesModel.size());
//    }

}



