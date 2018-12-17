package com.vplus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.vplus.models.CourseModel;

@Repository("courseDAO")
public class CourseDAO implements ICourseDAO{
	
	private DataSource dataSource;
	
	@Override
	public List<CourseModel> selectAllCourses(){
		String query = "SELECT * FROM vergilplus.course;";
		List<CourseModel> courseList = new ArrayList<CourseModel>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs != null) {
				while(rs.next()){
					CourseModel course = new CourseModel();
					
					course.setCourseNumber(rs.getString("CourseNumber"));
					course.setSectionId(rs.getInt("Section"));
					course.setCourseTitle(rs.getString("CourseTitle"));
					course.setWeek(rs.getString("Week"));
					course.setStartTime(rs.getString("Start_Time"));
					course.setEndTime(rs.getString("End_Time"));
					course.setInstructor(rs.getString("Instructor"));
					
					// set the three prerequisites
					String[] preqs = rs.getString("Prerequisite").split(";",-1);
					List<String> coursePreq = Arrays.asList(preqs[0].split(" "));

					if (coursePreq.get(0).length()==0){
						coursePreq = new ArrayList<>();
					}

					String knowledgePreq = preqs[1];
					boolean instructorPreq = preqs[2].length() > 0;
					
					course.setCoursePreq(coursePreq);
					course.setKnowledgePreq(knowledgePreq);
					course.setInstructorPreq(instructorPreq);
					
					
					course.setTerm(rs.getInt("Term"));
					course.setDescription(rs.getString("Description"));
					courseList.add(course);
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
				System.err.println("An Exception occured in selectAllCourses!");
			}
		}
		return courseList;
	}	
	
//	public CourseModel searchCourse(String courseNumber) {
//		CourseModel course = new CourseModel();
//		String query = "SELECT * FROM vergilplus.course where CourseNumber=\'" + courseNumber + "';";
//		Connection con = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		try{
//			con = dataSource.getConnection();
//			ps = con.prepareStatement(query);
//			rs = ps.executeQuery();
//			if(rs != null) {
//				while(rs.next()){
//
//					course.setCourseNumber(rs.getString("CourseNumber"));
//					course.setSectionId(rs.getInt("Section"));
//					course.setCourseTitle(rs.getString("CourseTitle"));
//					course.setWeek(rs.getString("Week"));
//					course.setStartTime(rs.getString("Start_Time"));
//					course.setEndTime(rs.getString("End_Time"));
//					course.setInstructor(rs.getString("Instructor"));
//
//					// set the three prerequisites
//					String[] preqs = rs.getString("Prerequisite").split(";",-1);
//					List<String> coursePreq = Arrays.asList(preqs[0].split(" "));
//
//					if (coursePreq.get(0).length()==0){
//						coursePreq = new ArrayList<>();
//					}
//
//					String knowledgePreq = preqs[1];
//					boolean instructorPreq = preqs[2].length() > 0;
//
//					course.setCoursePreq(coursePreq);
//					course.setKnowledgePreq(knowledgePreq);
//					course.setInstructorPreq(instructorPreq);
//
//
//					course.setTerm(rs.getInt("Term"));
//					course.setDescription(rs.getString("Description"));
//					break;
//				}
//			}
//		}catch(Exception e){
//			System.err.println(e);
//			System.err.println("An Exception occured!");
//		}finally{
//			try {
//				rs.close();
//				ps.close();
//				con.close();
//			} catch (Exception e) {
//				System.err.println("An Exception occured in search_course function!");
//			}
//		}
//		return course;
//	}


	public List<String> getInstructors() {
		String query = "SELECT Instructor FROM vergilplus.course;";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<String> instructors = new ArrayList<>();
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs != null) {
				while(rs.next()){
					instructors.add(rs.getString("Instructor").toUpperCase());
				}
			}
		}catch(SQLException e){
			System.err.println(e);
			System.err.println("An SQLException occured!");
		}finally{
			try {
				rs.close();
				ps.close();
				con.close();
			} catch (Exception e) {
				System.err.println("An Exception occured in instructor collection!");
			}
		}
		return instructors;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}


