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

	public List<String> findCourseNumber(List<String> takenCourses){
		List<CourseModel> allCourses = courseDAO.selectAllCourses();
		for(int i =0 ; i< takenCourses.size() ; i++){
			for(int j =0;j<takenCourses.size();j++){
				if(allCourses.get(j).getCourseTitle().equals(takenCourses.get(i))){
					takenCourses.set(i, allCourses.get(j).getCourseNumber());
				}
			}
		}
		return takenCourses;
	}

	public ICourseDAO getCourseDAO() {
		return courseDAO;
	}

	public void setCourseDAO(ICourseDAO courseDAO) {
		this.courseDAO = courseDAO;
	}
	
	
}
