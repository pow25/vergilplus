package com.vplus.models;

public class ReviewModel {
	private String profName;
	private String courseNumber;
	private String review;
	private double score;
	private double magnitude;
	
	public String getProfName() {
		return profName;
	}

	public void setProfName(String profName) {
		this.profName = profName;
	}

	public String getCourseNumber() {
		return courseNumber;
	}

	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(double magnitude){
		this.magnitude = magnitude;
	}

	@Override
	public String toString(){
		return "{courseNumber="+courseNumber+",profName="+profName+",score="+score+"}";
	}
}