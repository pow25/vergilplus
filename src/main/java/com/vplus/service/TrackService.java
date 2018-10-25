package com.vplus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vplus.dao.ITrackDAO;
import com.vplus.models.TrackModel;

@Service
public class TrackService implements ITrackService{
	//@Autowired
	private ITrackDAO trackDAO;
	
	public ITrackDAO getTrackDAO() {
		return trackDAO;
	}

	public void setTrackDAO(ITrackDAO trackDAO) {
		this.trackDAO = trackDAO;
	}

	public TrackModel selectCoursesByTrackId(String id) {		
		TrackModel track = trackDAO.selectCoursesByTrackId(id);
		return track;
	}
//	
//	public ITrackDAO getEmployeeDAO() {
//		return employeeDAO;
//	}
//	
//	public void setEmployeeDAO(IEmployeeDAO employeeDAO) {
//		this.employeeDAO = employeeDAO;
//	}
}
