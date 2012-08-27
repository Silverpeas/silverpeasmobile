package com.silverpeas.mobile.server.services;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import com.silverpeas.mobile.shared.dto.EventDetailDTO;
import com.silverpeas.mobile.shared.exceptions.AlmanachException;
import com.silverpeas.mobile.shared.services.ServiceAlmanach;
import com.stratelia.webactiv.almanach.control.ejb.AlmanachBm;
import com.stratelia.webactiv.almanach.control.ejb.AlmanachBmHome;
import com.stratelia.webactiv.almanach.model.EventDetail;
import com.stratelia.webactiv.almanach.model.EventPK;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;

public class ServiceAlmanachImpl extends AbstractAuthenticateService implements ServiceAlmanach{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AlmanachBm currentAlmanachBm;
	private Collection<EventDetail> listEventDetail;
	private Collection<EventDetailDTO> listEventDetailDTO;
	
	@SuppressWarnings("deprecation")
<<<<<<< HEAD
	public Collection<EventDetailDTO> getAlmanach(String instanceId) throws AlmanachException{
		listEventDetailDTO = new ArrayList<EventDetailDTO>();
		listEventDetail = new ArrayList<EventDetail>();
		currentAlmanachBm = getAlmanachBm();
		EventPK eventPK = new EventPK("");
		eventPK.setComponentName(instanceId);
		Date date = new Date();
=======
	public Collection<EventDetailDTO> getAllRDV(int month) throws AlmanachException{
		listEventDetailDTO = new ArrayList<EventDetailDTO>();
		listEventDetail = new ArrayList<EventDetail>();
		currentAlmanachBm = getAlmanachBm();
		Date date = new Date();
		date.setMonth(month);
		EventPK eventPK = new EventPK("0");
>>>>>>> 09b477a4f719202ea331d7ac57a295f981f9520f
		try {
			listEventDetail = currentAlmanachBm.getMonthEvents(eventPK, date);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		Mapper mapper = new DozerBeanMapper();
		if(!listEventDetail.isEmpty()){
			Iterator<EventDetail> i = listEventDetail.iterator();
			while(i.hasNext()){
				EventDetail eventDetail = i.next();
				listEventDetailDTO.add(mapper.map(eventDetail, EventDetailDTO.class));
			}
		}
<<<<<<< HEAD
=======
		
		EventDetailDTO test = new EventDetailDTO();
		String name = new String("test name");
		Date startDate = new Date();
		Date endDate = new Date();
		String startHour = new String("test startHour");
		String endHour = new String("test endHour");
		String place = new String("test place");
		test.set_name(name);
		test.set_startDate(startDate);
		test.set_endDate(endDate);
		test.setStartHour(startHour);
		test.setEndHour(endHour);
		test.setPlace(place);
		listEventDetailDTO.add(test);
		
>>>>>>> 09b477a4f719202ea331d7ac57a295f981f9520f
		return listEventDetailDTO;
	}
	
	private AlmanachBm getAlmanachBm(){
	    if (currentAlmanachBm == null) {
	      try {
	        currentAlmanachBm = ((AlmanachBmHome) EJBUtilitaire.getEJBObjectRef(
	            JNDINames.ALMANACHBM_EJBHOME, AlmanachBmHome.class)).create();
	      } catch (Exception e) {
	      }
	    }
	    return currentAlmanachBm;
	  }
}
