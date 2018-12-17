package com.vplus.service;

import java.util.List;

import com.vplus.models.CourseModel;
import com.vplus.models.ReviewModel;

public interface IReviewService {
	public List<ReviewModel> selectAllReviews();
	public List<CourseModel> sortCoursesByReviewScores(List<CourseModel> courseModelList);
	public String getWords(String profName);
}
