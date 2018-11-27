package com.vplus.controller;

import java.util.List;

import com.vplus.models.CourseModel;

public interface IMasterController{
	
	public List<CourseModel> filterCourses(List<String> takenCourses); 
	
	// acknowledge taken courses, e.g, remove taken courses, make courses available
	public List<CourseModel> processTakenCourses(List<String> takenCourses, List<CourseModel> allCourses);
	
	// remove courses for which prerequisites are not fulfilled
	public List<CourseModel> filterByPrerequisites(List<CourseModel> courses);
	
	public List<CourseModel> recommendCourses(List<String> takenCourses);
	
	public List<CourseModel> breadthRequirements();

	public List<String> findAllInstructors();

	public List<CourseModel> fetchAllCourses();

//	public List<String> convertCourseForm(List<String> takenCourses);

	public String getWordsProfessor(String profName);
}
