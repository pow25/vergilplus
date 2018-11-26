package com.vplus.service;

import java.util.List;

import com.vplus.dao.IReviewDAO;
import com.vplus.models.CourseModel;
import com.vplus.models.ReviewModel;

import javafx.util.Pair;

public interface IReviewService {
	public List<ReviewModel> selectAllReviews();
	public List<Pair<String, Float>> sort_base_on_reviews(List<CourseModel> course_models);
	public IReviewDAO getReviewDAO();
	public void setReviewDAO(IReviewDAO ReviewDAO);
	public String getWords(String profName);
}
