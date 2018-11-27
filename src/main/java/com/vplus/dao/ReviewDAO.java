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
					
					review.setCourseID(rs.getString("number"));
					review.setProf(rs.getString("professor"));
					review.setmagnitude(Float.parseFloat(rs.getString("magnitude")));
					review.setscore(Float.parseFloat(rs.getString("score")));
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
	
	public List<String> getWords( String professor ) {

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
	
	public List<String> getReview(String courseID, String Prof) {
		
		if ( Prof.isEmpty() && courseID.isEmpty() )
			return null;
		
		String query = "SELECT review FROM vergilplus.sentiment where ";
		
		if ( Prof.isEmpty() )
			query += "number= \'" + courseID + "\';" ;
		else if ( courseID.isEmpty() )
			query += "professor= \'" + Prof + "\';" ;
		else
			query += "number= \'" + courseID + "\' AND professor= \'" + Prof + "\';" ;
		
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
	public float get_course_rating( String courseID ){
		float res = 0f;
		String query = "SELECT * FROM vergilplus.sentiment where number=\'"
					   +courseID+"';";
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
					float magnitude = Float.parseFloat(rs.getString("magnitude"));
					float score = Float.parseFloat(rs.getString("score"));
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
			return Float.MIN_VALUE;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
