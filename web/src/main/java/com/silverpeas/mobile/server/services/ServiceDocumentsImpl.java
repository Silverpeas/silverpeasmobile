package com.silverpeas.mobile.server.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import com.silverpeas.mobile.shared.dto.documents.TopicDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.DocumentsException;
import com.silverpeas.mobile.shared.services.ServiceDocuments;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.node.control.NodeBm;
import com.stratelia.webactiv.util.node.control.NodeBmHome;
import com.stratelia.webactiv.util.node.model.NodeDetail;
import com.stratelia.webactiv.util.node.model.NodePK;

/**
 * Service de gestion des GED.
 * @author svuillet
 */
public class ServiceDocumentsImpl extends AbstractAuthenticateService implements ServiceDocuments {

	private final static Logger LOGGER = Logger.getLogger(ServiceDocumentsImpl.class);
	private static final long serialVersionUID = 1L;
	//private KmeliaBm kmeliaBm;
	//private PublicationBm pubBm;
	private NodeBm nodeBm;
	
	
	/**
	 * Retourne tous les topics de premier niveau d'un topic.
	 */
	@Override
	public List<TopicDTO> getTopics(String instanceId, String rootTopicId) throws DocumentsException, AuthenticationException {
		checkUserInSession();
		List<TopicDTO> topicsList = new ArrayList<TopicDTO>();
		
		try {
			if (rootTopicId == null) {
				rootTopicId = "0";			
			}
			NodePK pk = new NodePK(rootTopicId, instanceId);
			NodeDetail rootNode = getNodeBm().getDetail(pk);			
			ArrayList<NodeDetail> nodes = getNodeBm().getSubTreeByLevel(pk, rootNode.getLevel() + 1);
			for (NodeDetail nodeDetail : nodes) {
				TopicDTO topic = new TopicDTO();
				topic.setId(String.valueOf(nodeDetail.getId()));
				topic.setName(nodeDetail.getName());
				topicsList.add(topic);
			}			
		} catch (Exception e) {
			LOGGER.error("getTopics", e);
		}
		return topicsList;
	}
	
	/**
	 * 
	 */
	@Override
	public List<PublicationDTO> getPublications(String instanceId, String topicId) throws DocumentsException, AuthenticationException {
		checkUserInSession();
		
		
		
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/*private KmeliaBm getKmeliaBm() throws Exception {
		if (kmeliaBm == null) {
			kmeliaBm = (KmeliaBm) EJBUtilitaire.getEJBObjectRef(JNDINames.KMELIABM_EJBHOME, KmeliaBm.class);
		}
		return kmeliaBm;
	}
	
	private PublicationBm getPubBm() throws Exception {
		if (pubBm == null) {
			pubBm = (PublicationBm) EJBUtilitaire.getEJBObjectRef(JNDINames.PUBLICATIONBM_EJBHOME, PublicationBm.class);
		}
		return pubBm;
	}*/
	
	private NodeBm getNodeBm() throws Exception {
		if (nodeBm == null) {
			NodeBmHome home = EJBUtilitaire.getEJBObjectRef(JNDINames.NODEBM_EJBHOME, NodeBmHome.class);
			nodeBm = home.create(); 
		}
		return nodeBm;
	}
}
