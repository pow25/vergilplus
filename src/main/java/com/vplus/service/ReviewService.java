package com.vplus.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

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
	
	public List<CourseModel> sortCoursesByReviewScores(List<CourseModel> courseModelList){
		courseModelList.sort(Comparator.comparing(a -> ReviewDAO.getCourseRating(a.getCourseNumber()), Comparator.reverseOrder()));
		return courseModelList;
	}
		
	public String getWords(String profName){
		List<String> res = new ArrayList<String>();
		String temp_name[] = profName.split(",");
		temp_name[0] = temp_name[0].toLowerCase();
		temp_name[1] = temp_name[1].replaceAll("\\s","");
		temp_name[1] = temp_name[1].toLowerCase();

		res = ReviewDAO.getWords(profName);
		String result = "";
		HashSet<String> hashset = new HashSet<String>();
		for(int i = 0; i < res.size(); i++){
			String temp = res.get(i);
			temp = temp.replaceAll("\\s","");
			String[] words = temp.split(",");
			
			for(int j = 0; j < words.length; j++) {
				if(words[j].equals(temp_name[0]) || words[j].equals(temp_name[1]))
					continue;
				if ( words[j].isEmpty() )
					continue;
				if ( hashset.contains(words[j]) )
					continue;
				else {
					hashset.add(words[j]);
					result += words[j];
					result += "||";
				}
			}
		}
			
		return result;
	}
	
	public IReviewDAO getReviewDAO() {
		return ReviewDAO;
	}

	public void setReviewDAO(IReviewDAO ReviewDAO) {
		this.ReviewDAO = ReviewDAO;
	}
}
