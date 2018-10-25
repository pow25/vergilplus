package com.vplus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.vplus.models.CourseModel;

@Repository("courseDAO")
public class CourseDAO implements ICourseDAO{
	
	private DataSource dataSource;
	
	@Override
	public List<CourseModel> selectAllCourses(){
		String query = "SELECT * FROM course";
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
					
					course.setCourseNumber(rs.getString("CourseID"));
					course.setSectionId(rs.getInt("Section"));
					course.setCourseTitle(rs.getString("CourseTitle"));
					course.setWeek(rs.getString("Week"));
					course.setStartTime(rs.getString("Start_Time"));
					course.setEndTime(rs.getString("End_Time"));
					course.setInstructor(rs.getString("Instructor"));
					course.setPrerequisite(rs.getString("Prerequisite"));
					course.setTerm(rs.getInt("Term"));
					course.setDescription(rs.getString("Description"));
					courseList.add(course);
				}
			}
		}catch(SQLException e){
			System.err.println("An SQLException occured!");
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
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}


