package com.vplus.maven.vplus;

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



//import java.io.IOException;
//import java.io.PipedInputStream;
//import java.io.PipedOutputStream;
//import java.io.PrintStream;
//import java.util.Scanner;
//
//import javax.swing.JFrame;
//import javax.swing.JOptionPane;


public class AppTest 
extends TestCase
{

	@Autowired
	ICourseDAO DAO_test;
	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	
//
	public void test_DAO() {
		ctx.getAutowireCapableBeanFactory().autowireBeanProperties(this,
	            AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true); 
		List<CourseModel> res = DAO_test.selectAllCourses();
		System.out.println(res.size());
		
		if ( res.isEmpty() ) {
			assertTrue(false);
		}

	}

	
//	
//	
//	
//	public void test_app_run() throws Exception {
//		Application app  = ctx.getBean("Application", Application.class);	    
//	    final SystemOutRule systemOutRule = new SystemOutRule().enableLog();    
//	    app.run();
//        
//	    assertNotNull(systemOutRule.getLog());
//	}
		
		
	

	
  
    
}

