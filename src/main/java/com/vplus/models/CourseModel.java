package com.vplus.models;

import java.util.ArrayList;
import java.util.List;

public class CourseModel implements Comparable<CourseModel>{
	private String courseNumber;
	
	private int sectionId;
	
	private String courseTitle;
	
	private String week;
	
	private String startTime;
	
	private String endTime;
	
	private String instructor;
	
	/**
	 * course prerequisites, listed by their courseNumber
	 */
	private List<String> coursePreq;
	
	private boolean instructorPreq;
	
	private String knowledgePreq;
	
	private int term;
	
	private String description;
	
	public String getCourseNumber() {
		return courseNumber;
	}

	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}

	public int getSectionId() {
		return sectionId;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}


	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public List<String> getCoursePreq() {
		return coursePreq;
	}

	public void setCoursePreq(List<String> coursePreq) {
		this.coursePreq = coursePreq;
	}

	public boolean getInstructorPreq() {
		return instructorPreq;
	}

	public void setInstructorPreq(boolean instructorPreq) {
		this.instructorPreq = instructorPreq;
	}

	public String getKnowledgePreq() {
		return knowledgePreq;
	}

	public void setKnowledgePreq(String knowledgePreq) {
		this.knowledgePreq = knowledgePreq;
	}

	@Override
	public int compareTo(CourseModel c){
		return this.courseNumber.equals(c.getCourseNumber()) 
				&& this.sectionId == c.getSectionId()
				? 0 : -1;
	}

	@Override
	public String toString() {
		String res =  "{"
					+ " courseNumber = " + courseNumber
					+ "\nsectionId = " + sectionId
					+ "\ncourseTitle = " + courseTitle
					+ "\nweek = " + week
					+ "\nstartTime = " + startTime
					+ "\nendTime = " + endTime
					+ "\ninstructor = " + instructor
					+ "\nterm = " + term
					+ "\ndescription = " + description;
		
		List<String> modelStrList = new ArrayList<>();
		coursePreq.forEach(model -> modelStrList.add(courseNumber + "," + sectionId));
		String preqs = String.join(",", modelStrList);
		res += " \ncoursePreq = " + preqs
				+ "\ninstructorPreq =  " + instructorPreq
				+ "\nknowledgePreq = " + knowledgePreq
				+ "}";
		return res;
	}
}
