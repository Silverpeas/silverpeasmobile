package com.silverpeas.mobile.server.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.dto.contact.ContactFilters;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.ContactException;
import com.silverpeas.mobile.shared.services.ServiceContact;
import com.silverpeas.socialnetwork.relationShip.RelationShipService;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.beans.admin.UserDetail;
import com.stratelia.webactiv.beans.admin.UserFull;
import com.stratelia.webactiv.util.GeneralPropertiesManager;

public class ServiceContactImpl extends AbstractAuthenticateService implements ServiceContact{

	private static final long serialVersionUID = 1L;
	private OrganizationController organizationController = new OrganizationController();
	private ArrayList<DetailUserDTO> listuserDTO;
	private DetailUserDTO userDTO;
	private String myId;
	
	/**
	 * Return list of DetailUserDTO of my contacts
	 * @return list of UserDetailDTO
	 * @throws ContactException
	 */
	public List<DetailUserDTO> getContacts(ContactFilters filter) throws ContactException {
		listuserDTO = new ArrayList<DetailUserDTO>();
		List<UserDetail> userDetail = new ArrayList<UserDetail>();
		if(filter.equals(ContactFilters.ALL)){
			UserDetail[] tabUserDetail = organizationController.getAllUsers();
			for(int i=0;i<tabUserDetail.length;i++){
				userDetail.add(tabUserDetail[i]);
			}
		}
		else if(filter.equals(ContactFilters.MY)){
			userDetail = getAllMyUserDetailContacts();
		}
		ArrayList<DetailUserDTO> users = new ArrayList<DetailUserDTO>();
		Mapper mapper = new DozerBeanMapper();
		Iterator<UserDetail> i = userDetail.iterator();
		while(i.hasNext()){
			UserDetail us = i.next();
			users.add(mapper.map(us, DetailUserDTO.class)); 
		}
		listuserDTO = users;
		return listuserDTO;
	}	
	
	/**
	 * Get all the UserDetail of my contacts
	 * @return list of UserDetail
	 * @throws ContactException
	 */
	public List<UserDetail> getAllMyUserDetailContacts()throws ContactException{
		List<UserDetail> listUsers = new ArrayList<UserDetail>();
		List<String> myContactsIds;
		try {
			myContactsIds = getMyContactsIds();
			Iterator<String> i = myContactsIds.iterator();
			while(i.hasNext()){
				String id = i.next();
				UserDetail userDetail = getUserDetail(id);
				listUsers.add(userDetail);
			}
		    return listUsers;
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Return he list of my contacts' id
	 * @return list of my contacts' id
	 * @throws AuthenticationException
	 */
	public List<String> getMyContactsIds() throws AuthenticationException {
	    try {
	    	checkUserInSession();
			UserDetail user = getUserInSession();
			myId = user.getId();
	      return new RelationShipService().getMyContactsIds(Integer.parseInt(myId));
	    } catch (SQLException ex) {
	      SilverTrace.error("com.silverpeas.mobile.server.services", "ServiceDashboardImpl.getMyContactsIds", "", ex);
	    }
	    return new ArrayList<String>();
	}
	
	public DetailUserDTO getContactDetail(String id) throws ContactException{
		UserDetail userDetail = getUserDetail(id);
		UserFull userFull = UserFull.getById(id);
		Mapper mapper = new DozerBeanMapper();
		userDTO = mapper.map(userDetail, DetailUserDTO.class);
		userDTO.setAvatar(GeneralPropertiesManager.getString("ApplicationURL")+userDetail.getAvatar());
		userDTO.setPhoneNumber(userFull.getValue("phone"));
		return userDTO;
	}
	
	/**
	 * Return UserDetail with the id contact
	 * @param id
	 * @return UserDetail
	 * @throws ContactException
	 */
	public UserDetail getUserDetail(String id) throws ContactException{
		String ldapUserId = organizationController.getUserDetailByDBId(Integer.parseInt(id));
		UserDetail User = organizationController.getUserDetail(ldapUserId);
		return User;
	}
}