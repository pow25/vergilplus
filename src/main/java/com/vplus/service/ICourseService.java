package com.vplus.service;

import java.util.List;
import com.vplus.models.CourseModel;

public interface ICourseService {	
	public List<CourseModel> selectAllCourses();
	public List<CourseModel> selectCoursesByNumAndSection(String courseNum, int sectionId);
	public List<String> findInstructors();
	public CourseModel searchCourse(String courseNumber);
}
