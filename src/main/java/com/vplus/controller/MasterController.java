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
import com.vplus.service.IReviewService;

import javafx.util.Pair;

@Controller("masterController")
public class MasterController implements IMasterController{
	@Autowired
	private ICourseService courseService;

    @Autowired
	private IReviewService reviewService;

	//private ITrackService trackService;
	
	public static final int NUM_REC = 4;
	public static final int SEED = 1;
	public static final Random rnd = new Random(SEED);

	public List<CourseModel> recommendCourses(List<String> takenCourses){
		List<CourseModel> filteredCourses = filterCourses(takenCourses);
		
		List<Pair<String, Float>> result = reviewService.sort_base_on_reviews(filteredCourses);
		result = result.subList(0, NUM_REC);
		List<CourseModel> res = new ArrayList<CourseModel>();
		for ( int i = 0; i < result.size(); ++i ) {
			res.add( courseService.search_couse(result.get(i).getKey()) );
		}
		return res;
	}
	
	public String getWordsProfessor(String profName) {
		
		String input = "";
		String[] buff = profName.split(" ");
		if (buff.length != 2)
			return "Error Professor Name!!";
		
		input = buff[1] + ", " + buff[0];
		String output = reviewService.getWords(input);
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
//		takenCourses = convertCourseForm(takenCourses);
		List<CourseModel> filteredCourses=new ArrayList<>();
		if (takenCourses.isEmpty()) return allCourses;
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

	//convert course name to course number
//	public List<String> convertCourseForm(List<String> takenCourses){
//		List<String> converted = courseService.findCourseNumber(takenCourses);
//		return converted;
//	}

	public List<String> findAllInstructors(){
		List<String> instructors = courseService.findInstructors();
		return instructors;
	}

	//return courses according to breadth requirements
	public List<CourseModel> breadthRequirements(){
		List<CourseModel> filteredBreadthRequirements = new ArrayList<>();
		List<CourseModel> breadthTheory = new ArrayList<>();
		List<CourseModel> breadthSystems = new ArrayList<>();
		List<CourseModel> breadthAI = new ArrayList<>();
		List<CourseModel> allCourses = fetchAllCourses();
		for(CourseModel course : allCourses) {
			//if COMS 42xx and COMS W4995 and CSOR 4231
			//belongs to theory
			if(course.getCourseNumber().contains("COMS42") || course.getCourseNumber().contains("CSOR4231")) {
				breadthTheory.add(course);
			}
			//if COMS 41xx courses except COMS 4121, COMS 416x and COMS 417x; 
			//COMS 4444; CSEE 4119, EECS 4340, CSEE 4823, CSEE 4824, CSEE 4840, CSEE 4868; All COMS 48xx courses;
			//belongs to systems
			else if((course.getCourseNumber().contains("COMS41") && !course.getCourseNumber().contains("COMS4121") &&
					!course.getCourseNumber().contains("COMS416") && !course.getCourseNumber().contains("COMS417")) ||
//					course.getCourseNumber().contains("COMS48") || course.getCourseNumber().contains("COMS4444") ||
//					course.getCourseNumber().contains("CSEE4119") || course.getCourseNumber().contains("EECS4340") ||
					course.getCourseNumber().contains("CSEE4823") || course.getCourseNumber().contains("CSEE4824") ){
//					course.getCourseNumber().contains("CSEE4840") || course.getCourseNumber().contains("CSEE4868")) {
				breadthSystems.add(course);
			}
			//if COMS 47xx courses except COMS 4721 and COMS 4776
			//All COMS 416x and COMS 417x; CBMF 4761
			//belongs to AI & Applications
			else if((course.getCourseNumber().contains("COMS47") && !course.getCourseNumber().contains("COMS4721"))){
//					!course.getCourseNumber().contains("COMS4776")) || course.getCourseNumber().contains("COMS416") ||
//					course.getCourseNumber().contains("COMS417") || course.getCourseNumber().contains("COMS4761")) {
				breadthAI.add(course);
			}
		}
		Collections.shuffle(breadthTheory, rnd);
		Collections.shuffle(breadthSystems, rnd);
		Collections.shuffle(breadthAI, rnd);
		CourseModel courseTmp = breadthTheory.get(0);
		filteredBreadthRequirements.add(courseTmp);
		courseTmp = breadthTheory.get(1);
		filteredBreadthRequirements.add(courseTmp);
		courseTmp = breadthSystems.get(0);
		filteredBreadthRequirements.add(courseTmp);
		courseTmp = breadthAI.get(0);
		filteredBreadthRequirements.add(courseTmp);
		return filteredBreadthRequirements;
	}

	public ICourseService getCourseService() {
		return courseService;
	}

	public void setCourseService(ICourseService courseService) {
		this.courseService = courseService;
	}

}
