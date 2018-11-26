package com.vplus.dao;

import java.util.List;

import javax.sql.DataSource;

import com.vplus.models.ReviewModel;

public interface IReviewDAO {
	public List<ReviewModel> selectAllReviews();
	public void setDataSource(DataSource dataSource);
	public float get_course_rating( String courseID);
	public List<String> getReview(String courseID, String Prof);
}
