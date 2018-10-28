package com.vplus.service;

import java.util.List;

import com.vplus.models.TrackModel;

public interface ITrackService {
	public List<TrackModel> selectAllTrack();
	
	//public List<String> selectRequiredCoursesByTrackId(String id);

	
	// public List<String> selectAllCoursesByTrackId(String id);
}
