package com.vplus.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vplus.dao.IReviewDAO;
import com.vplus.models.CourseModel;
import com.vplus.models.ReviewModel;


@Service("reviewService")
public class ReviewService implements IReviewService {
	@Autowired
	private IReviewDAO ReviewDAO;
	
	
	public List<ReviewModel> selectAllReviews(){
		return ReviewDAO.selectAllReviews();
	}
	
	public List<Pair<String, Float>> sort_base_on_reviews(List<CourseModel> course_models) {
		
		List<Pair<String, Float>> res = new ArrayList<Pair<String,Float>>();
		
		for ( int i = 0; i < course_models.size(); ++i  ) {
			String courseID = course_models.get(i).getCourseNumber();
			float score = ReviewDAO.get_course_rating(courseID);
			res.add(new Pair<String, Float>(courseID,score));
		}
		
		res.sort(new Comparator<Pair<String,Float>>(){
	        @Override
	        public int compare(Pair<String, Float> o1, Pair<String, Float> o2) {
	            if (o1.getValue() > o2.getValue()) {
	                return -1;
	            } else if (o1.getValue().equals(o2.getValue())) {
	                return 0;
	            } else {
	                return 1;
	            }
	        }
		});
		
		return res;
	}
	
	public IReviewDAO getReviewDAO() {
		return ReviewDAO;
	}

	public void setReviewDAO(IReviewDAO ReviewDAO) {
		this.ReviewDAO = ReviewDAO;
	}
}
