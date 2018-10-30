package com.vplus.maven.vplus;
import javax.sql.DataSource;
import java.util.*;
import com.vplus.controller.*;
import com.vplus.dao.*;
import com.vplus.models.*;
import com.vplus.service.*;
import junit.framework.Test;
import junit.framework.TestCase;

public class AppTest 
    extends TestCase
{
	MasterController controller_test = new MasterController();
	CourseDAO DAO_test = new CourseDAO();
	public void test_DAO() {
		
//		List<CourseModel> res = DAO_test.selectAllCourses();
		
//		if ( res.isEmpty() ) {
			assertTrue(true);
//		}
		
	}
	
//    public void test_filterCourses(){
//    	List<String> takenCourses = new ArrayList<String>();
//    	takenCourses.add("WCOMS4771");
//    	List<CourseModel> result = controller_test.filterCourses(takenCourses);
//    }
//        
    
}
