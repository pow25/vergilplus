package com.vplus.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.vplus.models.CourseModel;
import com.vplus.service.ICourseService;
import com.vplus.service.IReviewService;

@Controller("masterController")
public class MasterController implements IMasterController{
	@Autowired
	private ICourseService courseService;

    @Autowired
	private IReviewService reviewService;

	public static final int NUM_REC = 4;
	public static final int SEED = 1;
	public static final Random rnd = new Random(SEED);

	public List<CourseModel> recommendCourses(List<String> takenCourses){
		List<CourseModel> res = new ArrayList<CourseModel>();
		if(takenCourses == null){
			System.out.println("Taken courses null!");
			return res;
		}
		if(takenCourses.size() >= 12){
			return res;
		}
		List<CourseModel> filteredCourses = filterCourses(takenCourses);
		List<CourseModel> result = reviewService.sortCoursesByReviewScores(filteredCourses);
		res = result.subList(0, NUM_REC);
		return res;
	}
	
	public String getWordsProfessor(String profName) {
		if(profName == null || profName.equals("")){
			return "No Professor Name!";
		}

		String[] buff = profName.split(", ");

		if (buff.length != 2) {
			return "Error Professor Name!!";
		}

		String output = reviewService.getWords(profName);
		if (output.isEmpty())
			return "Oh this professor seems quiet. We don't have any review records for him :D";
		else
			return output;
	}
	
	public List<CourseModel> filterCourses(List<String> takenCourses){
		List<CourseModel> allCourses = fetchAllCourses();
		List<CourseModel> filteredCourses = processTakenCourses(takenCourses, allCourses);
		filteredCourses = filterByPrerequisites(filteredCourses);
		return filteredCourses;
	}
	
	// acknowledge taken courses, e.g, make courses available, remove taken courses
	public List<CourseModel> processTakenCourses(List<String> takenCourses, List<CourseModel> allCourses){

		List<CourseModel> filteredCourses=new ArrayList<>();
		if (takenCourses == null || takenCourses.isEmpty()) return allCourses;
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
			if(courses == null || courses.isEmpty()){
				return filteredCourses;
			}
			for (CourseModel course : courses) {
				if (course.getCoursePreq().isEmpty()) {
					filteredCourses.add(course);
				}
			}
			return filteredCourses;
	}

    //return courses according to breadth requirements
	public List<CourseModel> fetchAllCourses(){
		List<CourseModel> allCourses = courseService.selectAllCourses();
		return allCourses;
	}

	public List<String> findAllInstructors(){
		List<String> instructors = courseService.findInstructors();
		return instructors;
	}
	
	public boolean isValidCourse(String courseNumber){
		for(CourseModel c : fetchAllCourses()){
			if(c.getCourseNumber().equals(courseNumber)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isValidProfessor(String profName){
		for(String inst : findAllInstructors()){
			if(inst.equals(profName)){
				return true;
			}
		}
		return false;
	}

	public ICourseService getCourseService() {
		return courseService;
	}

	public void setCourseService(ICourseService courseService) {
		this.courseService = courseService;
	}

}
