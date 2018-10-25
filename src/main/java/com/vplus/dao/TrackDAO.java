package com.vplus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import org.springframework.stereotype.Component;
import com.vplus.models.TrackModel;

@Component
public class TrackDAO implements ITrackDAO {

	// @Autowired
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public List<TrackModel> selectAllTrack() {
		String query = "select * from Track_Course";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			List<TrackModel> tracks = new ArrayList<TrackModel>();
			if (rs != null) {
				while (rs.next()) {
					TrackModel track = new TrackModel();
					track.setCourseID(rs.getString("CourseID"));
					track.setTrackID(rs.getString("TrackID"));
					track.setRequired(rs.getBoolean("Required"));
					tracks.add(track);
					System.out.println("Track courses fetch succeed");
				}
			} else
				System.out.println("Track courses fetch fail");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public TrackModel selectCoursesByTrackId(String id) {
		String query = "select * from Track_Course where TrackID = ?";
		TrackModel track = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, id);
			rs = ps.executeQuery();

			if (rs.next()) {
				track = new TrackModel();
				track.setTrackID(rs.getString("TrackID"));
				track.setCourseID(rs.getString("CourseID"));
				System.out.println("Track course Found::" + track.getCourseID() + " " + track.getTrackID()
						+ " is required:" + track.getRequired());
			} else {
				System.out.println("No Employee found with id=" + id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return track;
	}
	//
	// @Override
	// public void save(Employee employee) {
	// String query = "insert into Employee (id, name, role) values (?,?,?)";
	// Connection con = null;
	// PreparedStatement ps = null;
	// try{
	// con = dataSource.getConnection();
	// ps = con.prepareStatement(query);
	// ps.setInt(1, employee.getId());
	// ps.setString(2, employee.getName());
	// ps.setString(3, employee.getRole());
	// int out = ps.executeUpdate();
	// if(out !=0){
	// System.out.println("Employee saved with id="+employee.getId());
	// }else System.out.println("Employee save failed with id="+employee.getId());
	// }catch(SQLException e){
	// e.printStackTrace();
	// }finally{
	// try {
	// ps.close();
	// con.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// @Override
	// public Employee getById(int id) {
	// String query = "select name, role from Employee where id = ?";
	// Employee emp = null;
	// Connection con = null;
	// PreparedStatement ps = null;
	// ResultSet rs = null;
	// try{
	// con = dataSource.getConnection();
	// ps = con.prepareStatement(query);
	// ps.setInt(1, id);
	// rs = ps.executeQuery();
	//
	// if(rs.next()){
	// emp = new Employee();
	// emp.setId(id);
	// emp.setName(rs.getString("name"));
	// emp.setRole(rs.getString("role"));
	// System.out.println("Employee Found::"+emp);
	// }else{
	// System.out.println("No Employee found with id="+id);
	// }
	// }catch(Exception e){
	// e.printStackTrace();
	// }finally{
	// try {
	// rs.close();
	// ps.close();
	// con.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return emp;
	// }
	//
	// @Override
	// public void update(Employee employee) {
	// String query = "update Employee set name=?, role=? where id=?";
	// Connection con = null;
	// PreparedStatement ps = null;
	// try{
	// con = dataSource.getConnection();
	// ps = con.prepareStatement(query);
	// ps.setString(1, employee.getName());
	// ps.setString(2, employee.getRole());
	// ps.setInt(3, employee.getId());
	// int out = ps.executeUpdate();
	// if(out !=0){
	// System.out.println("Employee updated with id="+employee.getId());
	// }else System.out.println("No Employee found with id="+employee.getId());
	// }catch(SQLException e){
	// e.printStackTrace();
	// }finally{
	// try {
	// ps.close();
	// con.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// @Override
	// public void deleteById(int id) {
	// String query = "delete from Employee where id=?";
	// Connection con = null;
	// PreparedStatement ps = null;
	// try{
	// con = dataSource.getConnection();
	// ps = con.prepareStatement(query);
	// ps.setInt(1, id);
	// int out = ps.executeUpdate();
	// if(out !=0){
	// System.out.println("Employee deleted with id="+id);
	// }else System.out.println("No Employee found with id="+id);
	// }catch(SQLException e){
	// e.printStackTrace();
	// }finally{
	// try {
	// ps.close();
	// con.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// @Override
	// public List<Employee> getAll() {
	// String query = "select id, name, role from Employee";
	// List<Employee> empList = new ArrayList<Employee>();
	// Connection con = null;
	// PreparedStatement ps = null;
	// ResultSet rs = null;
	// try{
	// con = dataSource.getConnection();
	// ps = con.prepareStatement(query);
	// rs = ps.executeQuery();
	// while(rs.next()){
	// Employee emp = new Employee();
	// emp.setId(rs.getInt("id"));
	// emp.setName(rs.getString("name"));
	// emp.setRole(rs.getString("role"));
	// empList.add(emp);
	// }
	// }catch(SQLException e){
	// e.printStackTrace();
	// }finally{
	// try {
	// rs.close();
	// ps.close();
	// con.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// return empList;
	// }

}