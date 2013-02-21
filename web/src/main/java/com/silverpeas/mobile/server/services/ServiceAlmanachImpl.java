package com.silverpeas.mobile.server.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
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

public class ServiceAlmanachImpl extends AbstractAuthenticateService implements ServiceAlmanach {

	private static final long serialVersionUID = 1L;
	private AlmanachBm currentAlmanachBm;
	private final static Logger LOGGER = Logger.getLogger(ServiceAlmanachImpl.class);

	public Collection<EventDetailDTO> getAlmanach(String instanceId) throws AlmanachException {
		Collection<EventDetail> listEventDetail = new ArrayList<EventDetail>();
		Collection<EventDetailDTO> listEventDetailDTO = new ArrayList<EventDetailDTO>();

		try {
			EventPK eventPK = new EventPK("");
			eventPK.setComponentName(instanceId);
			listEventDetail = getAlmanachBm().getAllEvents(eventPK);
			Mapper mapper = new DozerBeanMapper();
			if (!listEventDetail.isEmpty()) {
				Iterator<EventDetail> i = listEventDetail.iterator();
				while (i.hasNext()) {
					EventDetail eventDetail = i.next();
					listEventDetailDTO.add(mapper.map(eventDetail, EventDetailDTO.class));
				}
			}
		} catch (Exception e) {
			LOGGER.error("getAlmanach", e);
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
