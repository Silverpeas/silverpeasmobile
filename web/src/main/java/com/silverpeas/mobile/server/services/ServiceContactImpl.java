package com.silverpeas.mobile.server.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import com.google.gwt.regexp.shared.RegExp;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.exceptions.ContactException;
import com.silverpeas.mobile.shared.services.ServiceContact;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.beans.admin.UserDetail;

public class ServiceContactImpl extends AbstractAuthenticateService implements ServiceContact{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OrganizationController organizationController = new OrganizationController();
	private ArrayList<DetailUserDTO> listuserDTO = new ArrayList<DetailUserDTO>();
	private ArrayList<DetailUserDTO> listreg = new ArrayList<DetailUserDTO>();
	
	public void getAllContact() throws ContactException{
		List<UserDetail> userDetail = getAll();
		ArrayList<DetailUserDTO> users = new ArrayList<DetailUserDTO>();
		Mapper mapper = new DozerBeanMapper();
		Iterator<UserDetail> i = userDetail.iterator();
		while(i.hasNext()){
			UserDetail us = i.next();
			users.add(mapper.map(us, DetailUserDTO.class)); 
		}
		listuserDTO = users;
	}	
	
	public List<UserDetail> getAll()throws ContactException{
	    UserDetail currentUser = getUserInSession();
	    String userDomainId = currentUser.getDomainId();
	    
	    List<UserDetail> listUsers = Arrays.asList(organizationController.getAllUsers());
	    List<UserDetail> listUsersOfSameDomain = getListUsersOfDomain(userDomainId, listUsers);
	    
	    return listUsersOfSameDomain;
	}
	
	private List<UserDetail> getListUsersOfDomain(String domainId, List<UserDetail> listUsers) {
	    List<UserDetail> listUsersOfSameDomain = new ArrayList<UserDetail>();
	    for (UserDetail userDetail : listUsers) {
	      if(domainId.equals(userDetail.getDomainId())){
	        listUsersOfSameDomain.add(userDetail);
	      }
	    }
	    return listUsersOfSameDomain;
	}
	
	public List<DetailUserDTO> getContactsByLetter(String letter) throws ContactException{
		Iterator<DetailUserDTO> i = listuserDTO.iterator();
		while(i.hasNext()){
			DetailUserDTO dudto = i.next();
			RegExp regexp = RegExp.compile(letter);
			if(regexp.test(dudto.getLastName())){
				listreg.add(dudto);
			}
		}
		return listreg;
	}
}
