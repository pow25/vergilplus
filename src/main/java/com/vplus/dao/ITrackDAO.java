package com.vplus.dao;

import java.util.List;

import com.vplus.models.TrackModel;

//CRUD operations
public interface ITrackDAO {
	
	public List<TrackModel> selectAllTrack();

	TrackModel selectCoursesByTrackId(String id);
}