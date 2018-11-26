package com.vplus.dao;

import java.util.List;
import com.vplus.models.CourseModel;

import javax.sql.DataSource;


public interface ICourseDAO {
	public List<CourseModel> selectAllCourses();
	public CourseModel search_course(String courseID);
	public List<String> getInstructors();
	public void setDataSource(DataSource dataSource);
}
