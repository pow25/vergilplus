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
		String query = "select * from track";
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
}