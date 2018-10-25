package com.vplus.models;

public class TrackModel {
	private String trackID;
	private String courseID;
	private Boolean Required;
	
	public String getTrackID() {
		return trackID;
	}



	public void setTrackID(String trackID) {
		this.trackID = trackID;
	}



	public String getCourseID() {
		return courseID;
	}



	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}



	public Boolean getRequired() {
		return Required;
	}



	public void setRequired(Boolean required) {
		Required = required;
	}

	
	
	@Override
	public String toString(){
		return "{ID="+trackID+",courseID="+courseID+",isRequired="+Required+"}";
	}
}