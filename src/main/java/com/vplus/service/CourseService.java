package com.vplus.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vplus.dao.ICourseDAO;
import com.vplus.models.CourseModel;

@Service("courseService")
public class CourseService implements ICourseService{
	@Autowired
	private ICourseDAO courseDAO;
	
	public List<CourseModel> selectAllCourses(){
		return courseDAO.selectAllCourses();
	}
	
	public CourseModel search_couse(String courseID) {
		return courseDAO.search_course(courseID);
	}
	
	public List<String> findCourseNumber(List<String> course_names) {
		List<CourseModel> allCourses = courseDAO.selectAllCourses();
		List<String> res = new ArrayList<String>();
		
		for(CourseModel course : allCourses) {
			for ( String course_name:course_names ) {
				if(course_name.equals( course.getCourseTitle() )){
					res.add(course.getCourseNumber());
				}
			}
		}
		
		return res;
	}
	
	public List<CourseModel> selectCoursesByNumAndSection(String courseNum, int sectionId){
		List<CourseModel> allCourses = courseDAO.selectAllCourses();
		
		List<CourseModel> res = new ArrayList<>();
		for(CourseModel course : allCourses) {
			if(courseNum.equals(course.getCourseNumber())
				&& sectionId == course.getSectionId()){
				res.add(course);
			}
		}
		
		return res;
	}

	public ICourseDAO getCourseDAO() {
		return courseDAO;
	}

	public void setCourseDAO(ICourseDAO courseDAO) {
		this.courseDAO = courseDAO;
	}
	
	
}
