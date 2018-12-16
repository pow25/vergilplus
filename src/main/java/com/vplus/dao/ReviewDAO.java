package com.vplus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.stereotype.Repository;
import com.vplus.models.ReviewModel;
import com.vplus.dao.IReviewDAO;

@Repository("reviewDAO")
public class ReviewDAO implements IReviewDAO{
	
	private DataSource dataSource;
	
	@Override
	public List<ReviewModel> selectAllReviews(){
		String query = "SELECT * FROM vergilplus.sentiment";
		List<ReviewModel> reviewList = new ArrayList<ReviewModel>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs != null) {
				while(rs.next()){
					ReviewModel review = new ReviewModel();
					
					review.setCourseNumber(rs.getString("number"));
					review.setProfName(rs.getString("professor"));
					review.setMagnitude(Float.parseFloat(rs.getString("magnitude")));
					review.setScore(Float.parseFloat(rs.getString("score")));
					review.setReview(rs.getString("review"));
					reviewList.add(review);
				}
			}
		}catch(Exception e){
			System.err.println(e);
			System.err.println("An Exception occured!");
		}finally{
			try {
				rs.close();
				ps.close();
				con.close();
			} catch (Exception e) {
				System.err.println("An Exception occured in selectAllReviews!");
			}
		}
		return reviewList;
	}	
	
	public List<String> getWords(String professor){

		String query = "SELECT words FROM vergilplus.sentiment where professor=\'";
		query += professor + "\';";
		
		List<String> res = new ArrayList<String>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs != null) {
				while(rs.next()){
					res.add(rs.getString("words"));
				}
			}
		}catch(Exception e){
			System.err.println(e);
			System.err.println("An SQLException occured!");
		}finally{
			try {
				rs.close();
				ps.close();
				con.close();
			} catch (Exception e) {
				System.err.println("An Exception occured in getAdj!");
			}
		}
		return res;
	}
	
	public List<String> getReview(String courseNumber, String profName){
		if(profName.isEmpty() && courseNumber.isEmpty() )
			return null;
		
		String query = "SELECT review FROM vergilplus.sentiment where ";
		
		if (profName.isEmpty())
			query += "number= \'" + courseNumber + "\';" ;
		else if (courseNumber.isEmpty() )
			query += "professor= \'" + profName + "\';" ;
		else
			query += "number= \'" + courseNumber + "\' AND professor= \'" + profName + "\';" ;
		
		List<String> res = new ArrayList<String>();		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs != null) {
				while(rs.next()){
					res.add(rs.getString("review"));
				}
			}
		}catch(Exception e){
			System.err.println(e);
			System.err.println("An SQLException occured!");
		}finally{
			try {
				rs.close();
				ps.close();
				con.close();
			} catch (Exception e) {
				System.err.println("An Exception occured in getReview!");
			}
		}
		
		return res;
	}
	
	@Override
	public double getCourseRating(String courseNumber){
		double res = 0;
		String query = "SELECT * FROM vergilplus.sentiment where number=\'"
					   +courseNumber+"';";
		int count = 0;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs != null) {
				while(rs.next()){
					++count;
					double magnitude = Double.parseDouble(rs.getString("magnitude"));
					double score = Double.parseDouble(rs.getString("score"));
					res += magnitude * score;
				}
			}
		}catch(Exception e){
			System.err.println(e);
			System.err.println("An SQLException occured!");
		}finally{
			try {
				rs.close();
				ps.close();
				con.close();
			} catch (Exception e) {
				System.err.println("An Exception occured in get_course_rating!");
			}
		}
		if ( count != 0 )
			return res/count;
		else
			return Double.MIN_VALUE;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
