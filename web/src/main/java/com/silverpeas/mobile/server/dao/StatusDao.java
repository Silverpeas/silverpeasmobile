package com.silverpeas.mobile.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.silverpeas.socialNetwork.model.SocialInformation;
import com.silverpeas.socialNetwork.status.SocialInformationStatus;
import com.silverpeas.socialNetwork.status.Status;
import com.stratelia.webactiv.util.DBUtil;

public class StatusDao {
	
	public List<Status> list_statut;
	
	public StatusDao(){
		list_statut = new ArrayList<Status>();
	}
	
	public Map<Date, String> getAllStatus(Connection connection, int userId, int indicator) throws SQLException {
		    PreparedStatement pstmt = null;
		    ResultSet rs = null;
		    try {
		      String query =
		          "SELECT id,userid, creationdate, description FROM sb_sn_status WHERE userid = ? ORDER BY creationdate DESC";
		      pstmt = connection.prepareStatement(query);
		      pstmt.setInt(1, userId);
		      rs = pstmt.executeQuery();

		      return getSocialInformationsList(rs, indicator);
		    } finally {
		      DBUtil.close(rs, pstmt);
		    }
	}
	
	private Map<Date, String> getSocialInformationsList(ResultSet rs, int indicator) throws SQLException {
		if(indicator==0){
			list_statut = new ArrayList<Status>();
		}
		Map<Date, String> status_list = new HashMap<Date, String>();
		Iterator<Status> i = list_statut.iterator();
		ArrayList<Status> list_statut_temp = new ArrayList<Status>();
	    int j = 0;
	    while (rs.next() && j<5) {
	    	if(i.hasNext()){
	    		Status s = i.next();
	    		if(rs.getInt(1)!=s.getId() && j < 5){
		    		Status status = new Status();
		    		status.setId(rs.getInt(1));
		    		status.setUserId(rs.getInt(2));
		    		status.setCreationDate(new Date(rs.getTimestamp(3).getTime()));
		    	    status.setDescription(rs.getString(4));
		    		SocialInformation si = new SocialInformationStatus(status);
		    		status_list.put(si.getDate(), si.getDescription());
		    		list_statut_temp.add(status);
		    		j++;
		    	}
	    	}
	    	else if(j < 5){
	    		Status status = new Status();
    		    status.setId(rs.getInt(1));
    		    status.setUserId(rs.getInt(2));
    		    status.setCreationDate(new Date(rs.getTimestamp(3).getTime()));
	    	    status.setDescription(rs.getString(4));
    		    SocialInformation si = new SocialInformationStatus(status);
    		    status_list.put(si.getDate(), si.getDescription());
    		    list_statut_temp.add(status);
    		    j++;
	    	}
	    }
	    list_statut.addAll(list_statut_temp);
	    TreeMap<Date, String> final_list = new TreeMap<Date, String>(status_list);
	    return final_list;
	}
	
	public void addLastStatus(Status status, int id){
		status.setId(id);
		list_statut.add(0, status);
	}
}
