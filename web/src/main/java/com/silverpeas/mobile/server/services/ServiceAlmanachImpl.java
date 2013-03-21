package com.silverpeas.mobile.server.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.shared.dto.EventDetailDTO;
import com.silverpeas.mobile.shared.exceptions.AlmanachException;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.services.ServiceAlmanach;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.almanach.control.ejb.AlmanachBm;
import com.stratelia.webactiv.almanach.control.ejb.AlmanachBmHome;
import com.stratelia.webactiv.almanach.model.EventDetail;
import com.stratelia.webactiv.almanach.model.EventPK;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;

public class ServiceAlmanachImpl extends AbstractAuthenticateService implements ServiceAlmanach {

	private static final long serialVersionUID = 1L;
	private AlmanachBm currentAlmanachBm;	

	public List<EventDetailDTO> getAlmanach(String instanceId) throws AlmanachException, AuthenticationException {
		checkUserInSession();		
		List<EventDetailDTO> listEventDetailDTO = new ArrayList<EventDetailDTO>();

		try {
			EventPK eventPK = new EventPK("");
			eventPK.setComponentName(instanceId);
			Collection<EventDetail> listEventDetail = getAlmanachBm().getAllEvents(eventPK);
			Mapper mapper = new DozerBeanMapper();
			if (!listEventDetail.isEmpty()) {
				Iterator<EventDetail> i = listEventDetail.iterator();
				while (i.hasNext()) {
					EventDetail eventDetail = i.next();
					listEventDetailDTO.add(mapper.map(eventDetail, EventDetailDTO.class));
				}
			}
		} catch (Exception e) {
			SilverTrace.error(SpMobileLogModule.getName(), "ServiceAlmanachImpl.getAlmanach", "root.EX_NO_MESSAGE", e);			
			throw new AlmanachException(e.getMessage());
		}
		return listEventDetailDTO;
	}

	private AlmanachBm getAlmanachBm() throws Exception {
		if (currentAlmanachBm == null) {
			currentAlmanachBm = ((AlmanachBmHome) EJBUtilitaire.getEJBObjectRef(JNDINames.ALMANACHBM_EJBHOME, AlmanachBmHome.class)).create();			
		}
		return currentAlmanachBm;
	}
}
