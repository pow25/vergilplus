package com.vplus.service;

import java.util.List;
import com.vplus.models.CourseModel;

public interface ICourseService {	
	public List<CourseModel> selectAllCourses();
	public List<String> findCourseNumber(List<String> course_names);
	public List<CourseModel> selectCoursesByNumAndSection(String courseNum, int sectionId);
	public CourseModel search_couse(String courseID);
}
