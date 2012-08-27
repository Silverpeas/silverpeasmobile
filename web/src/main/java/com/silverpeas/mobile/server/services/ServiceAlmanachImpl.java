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
	public Collection<EventDetailDTO> getAlmanach(String instanceId) throws AlmanachException{
		listEventDetailDTO = new ArrayList<EventDetailDTO>();
		listEventDetail = new ArrayList<EventDetail>();
		currentAlmanachBm = getAlmanachBm();
		EventPK eventPK = new EventPK("");
		eventPK.setComponentName(instanceId);
		Date date = new Date();
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
