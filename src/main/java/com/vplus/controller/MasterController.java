package com.vplus.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.vplus.models.CourseModel;
import com.vplus.service.ICourseService;
//import com.vplus.service.ITrackService;

@Controller("masterController")
public class MasterController implements IMasterController{
	@Autowired
	private ICourseService courseService;
	//private ITrackService trackService;
	
	public static final int NUM_REC = 4;
	public static final int SEED = 1;
	public static final Random rnd = new Random(SEED);
	
	public List<CourseModel> recommendCourses(List<String> takenCourses){
		List<CourseModel> filteredCourses = filterCourses(takenCourses);
		Collections.shuffle(filteredCourses, rnd);
		return filteredCourses.subList(0, NUM_REC);
	}
	
	public List<CourseModel> filterCourses(List<String> takenCourses){
		List<CourseModel> allCourses = courseService.selectAllCourses();
		List<CourseModel> filteredCourses = processTakenCourses(takenCourses, allCourses);
		filteredCourses = filterByPrerequisites(filteredCourses);
		return filteredCourses;
	}
	
	// acknowledge taken courses, e.g, make courses available, remove taken courses
	public List<CourseModel> processTakenCourses(List<String> takenCourses, List<CourseModel> allCourses){
		List<CourseModel> filteredCourses=new ArrayList<>();
		for(CourseModel course : allCourses){
			// if takenCourses and prereqs are not disjoint (i.e., there is overlap),
			// prereq becomes empty, because all prereqs are joined by OR.
			if(!Collections.disjoint(takenCourses, course.getCoursePreq())){
				course.setCoursePreq(new ArrayList<>());
			}
			
			// remove course from all courses if that course is already taken.
			if(!takenCourses.contains(course.getCourseNumber())) {
				filteredCourses.add(course);
			}
		}
		
		return filteredCourses;
	}

	// remove courses for which prerequisites are not fulfilled
	public List<CourseModel> filterByPrerequisites(List<CourseModel> courses){
			List<CourseModel> filteredCourses = new ArrayList<>();
			System.out.println("sdf sd"+courses.size());
			for (CourseModel course : courses) {
				if (course.getCoursePreq().isEmpty()) {
					filteredCourses.add(course);
				}
			}
			return filteredCourses;
	}
	
	// TODO: clean up track table and implement this
	public List<CourseModel> filterByTrackRequirements(String trackId, List<CourseModel> courses){
		return null;
	}

	public ICourseService getCourseService() {
		return courseService;
	}

	public void setCourseService(ICourseService courseService) {
		this.courseService = courseService;
	}
	
	
}
