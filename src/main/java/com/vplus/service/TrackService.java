package com.vplus.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vplus.dao.ITrackDAO;
import com.vplus.models.TrackModel;

@Service
public class TrackService implements ITrackService{
	//@Autowired
	private ITrackDAO trackDAO;
	
	public List<TrackModel> selectAllTrack(){
		return trackDAO.selectAllTrack();
	}
	
//	
//	public List<String> selectRequiredCoursesByTrackId(String id) {		
//		List<String> res = new ArrayList<>();
//		
//		List<TrackModel> track = trackDAO.selectAllTrack();
//		
//		for(TrackModel model : track){
//			if(id.equals(model.getTrackID())){
//				res.add(model.getCourseID());
//			}
//		}
//		
//		return res;
//	}

	public ITrackDAO getTrackDAO() {
		return trackDAO;
	}

	public void setTrackDAO(ITrackDAO trackDAO) {
		this.trackDAO = trackDAO;
	}
	
	
}
