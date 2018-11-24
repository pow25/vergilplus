package com.vplus.models;

public class ReviewModel {
	private String Professor_name;
	private String courseID;
	private String Review;
	private float score;
	private float magnitude;
	
	public String getProf() {
		return Professor_name;
	}

	public void setProf(String Professor_name) {
		this.Professor_name = Professor_name;
	}

	public String getCourseID() {
		return courseID;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	public String getReview() {
		return Review;
	}

	public void setReview(String Review) {
		this.Review = Review;
	}
	
	public float getscore() {
		return score;
	}

	public void setscore(float score) {
		this.score = score;
	}
	
	public float getmagnitude() {
		return magnitude;
	}

	public void setmagnitude(float magnitude) {
		this.magnitude = magnitude;
	}
	
	@Override
	public String toString(){
		return "{courseID="+courseID+",Professor="+Professor_name+",Score:"+score+"}";
	}
}