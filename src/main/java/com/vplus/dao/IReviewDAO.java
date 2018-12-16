package com.vplus.dao;

import java.util.List;

import javax.sql.DataSource;

import com.vplus.models.ReviewModel;

public interface IReviewDAO {
	public List<ReviewModel> selectAllReviews();
	public double getCourseRating(String courseNumber);
	public List<String> getReview(String courseNumber, String profName);
	public List<String> getWords(String profName);
	public void setDataSource(DataSource dataSource);
}
