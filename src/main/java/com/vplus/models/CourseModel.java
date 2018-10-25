package com.vplus.models;

public class CourseModel {
	private String courseNumber;
	
	private int sectionId;
	
	private String courseTitle;
	
	private String week;
	
	private String startTime;
	
	private String endTime;
	
	private String instructor;
	
	private String prerequisite;
	
	private String term;
	
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

	public String getPrerequisite() {
		return prerequisite;
	}

	public void setPrerequisite(String prerequisite) {
		this.prerequisite = prerequisite;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "{"
				+ " courseNumber = " + courseNumber
				+ " sectionId = " + sectionId
				+ " courseTitle = " + courseTitle
				+ " week = " + week
				+ " startTime = " + startTime
				+ " endTime = " + endTime
				+ " instructor = " + instructor
				+ " prerequisite = " + prerequisite
				+ " term = " + term
				+ " description = " + description
				+ "}";
	}
}
