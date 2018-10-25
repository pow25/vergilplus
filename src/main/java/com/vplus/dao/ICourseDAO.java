package com.vplus.dao;

import java.util.List;
import com.vplus.models.CourseModel;


public interface ICourseDAO {
	public List<CourseModel> selectAllCourses();
}
