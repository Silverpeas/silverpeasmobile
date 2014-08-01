package com.silverpeas.mobile.server.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.silverpeas.attachment.AttachmentServiceFactory;
import org.silverpeas.attachment.model.DocumentType;
import org.silverpeas.attachment.model.SimpleDocument;

import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.documents.AttachmentDTO;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import com.silverpeas.mobile.shared.dto.documents.TopicDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.DocumentsException;
import com.silverpeas.mobile.shared.services.ServiceDocuments;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.node.control.NodeBm;
import com.stratelia.webactiv.util.node.model.NodeDetail;
import com.stratelia.webactiv.util.node.model.NodePK;
import com.stratelia.webactiv.util.publication.control.PublicationBm;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;
import com.stratelia.webactiv.util.publication.model.PublicationPK;

/**
 * Service de gestion des GED.
 * @author svuillet
 */
public class ServiceDocumentsImpl extends AbstractAuthenticateService implements ServiceDocuments {
	
	private static final long serialVersionUID = 1L;
	private OrganizationController organizationController = new OrganizationController();
	private PublicationBm pubBm;
	private NodeBm nodeBm;
		
	/**
	 * Retourne tous les topics de premier niveau d'un topic.
	 */
	@Override
	public List<TopicDTO> getTopics(String instanceId, String rootTopicId) throws DocumentsException, AuthenticationException {
		checkUserInSession();
		List<TopicDTO> topicsList = new ArrayList<TopicDTO>();
		
		try {
			if (rootTopicId == null || rootTopicId.isEmpty()) {
				rootTopicId = "0";			
			}
			NodePK pk = new NodePK(rootTopicId, instanceId);
			NodeDetail rootNode = getNodeBm().getDetail(pk);			
			ArrayList<NodeDetail> nodes = getNodeBm().getSubTreeByLevel(pk, rootNode.getLevel() + 1);
			for (NodeDetail nodeDetail : nodes) {
				
				if (rootTopicId.equals(nodeDetail.getFatherPK().getId())) {				
					TopicDTO topic = new TopicDTO();
					topic.setId(String.valueOf(nodeDetail.getId()));
					topic.setName(nodeDetail.getName());				
					int childrenNumber = getNodeBm().getChildrenNumber(new NodePK(String.valueOf(nodeDetail.getId()), instanceId));
					topic.setTerminal(childrenNumber == 0);					
					topicsList.add(topic);
				}
			}			
		} catch (Exception e) {
			SilverTrace.error(SpMobileLogModule.getName(), "ServiceDocumentsImpl.getTopics", "root.EX_NO_MESSAGE", e);			
			throw new DocumentsException(e.getMessage());
		}
		return topicsList;
	}
	
	/**
	 * Retourne les publications d'un topic (au niveau 1).
	 */
	@Override
	public List<PublicationDTO> getPublications(String instanceId, String topicId) throws DocumentsException, AuthenticationException {
		checkUserInSession();
		ArrayList<PublicationDTO> pubs = new ArrayList<PublicationDTO>();
	
        try {
        	if (topicId == null || topicId.isEmpty()) {
        		topicId = "0";			
			}
        	NodePK nodePK = new NodePK(topicId, instanceId);
        	PublicationPK pubPK = new PublicationPK("useless", instanceId);
    		String status = "Valid";
    		ArrayList<String> nodeIds = new ArrayList<String>();
    		nodeIds.add(nodePK.getId());   		
			List<PublicationDetail> publications = (List<PublicationDetail>) getPubBm().getDetailsByFatherIdsAndStatus(nodeIds, pubPK, "pubname", status);			
			for (PublicationDetail publicationDetail : publications) {
				PublicationDTO dto = new PublicationDTO();
				dto.setId(publicationDetail.getId());
				dto.setName(publicationDetail.getName());
				pubs.add(dto);				
			}
			
		} catch (Exception e) {
			SilverTrace.error(SpMobileLogModule.getName(), "ServiceDocumentsImpl.getPublications", "root.EX_NO_MESSAGE", e);			
			throw new DocumentsException(e.getMessage());
		}
		
		return pubs;
	}
	
	private PublicationBm getPubBm() throws Exception {
		if (pubBm == null) {			 
			pubBm = EJBUtilitaire.getEJBObjectRef(JNDINames.PUBLICATIONBM_EJBHOME, PublicationBm.class);			 
		}
		return pubBm;
	}
	
	private NodeBm getNodeBm() throws Exception {
		if (nodeBm == null) {
			nodeBm = EJBUtilitaire.getEJBObjectRef(JNDINames.NODEBM_EJBHOME, NodeBm.class);			 
		}
		return nodeBm;
	}

	@Override
	public PublicationDTO getPublication(String pubId) throws DocumentsException, AuthenticationException {		
		SilverTrace.debug(SpMobileLogModule.getName(), "ServiceDocumentsImpl.getPublication", "getPublication for id " + pubId);
		checkUserInSession();				
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			PublicationDetail pub = getPubBm().getDetail(new PublicationPK(pubId));
			PublicationDTO dto = new PublicationDTO();
			dto.setId(pub.getId());
			dto.setName(pub.getName());
			dto.setCreator(pub.getCreator().getDisplayedName() + " " + sdf.format(pub.getCreationDate()));
			dto.setUpdater(organizationController.getUserDetail(pub.getUpdaterId()).getDisplayedName());		
			dto.setVersion(pub.getVersion());
			dto.setDescription(pub.getDescription());
			dto.setUpdateDate(sdf.format(pub.getUpdateDate()));
			String content = pub.getWysiwyg();
			//TODO : convert img url to data uri
			// use jsoup
			
			dto.setWysiwyg(content);
						
			ArrayList<AttachmentDTO> attachments = new ArrayList<AttachmentDTO>();						
			SilverTrace.debug(SpMobileLogModule.getName(), "ServiceDocumentsImpl.getPublication", "Get attachments");		
			
			List<SimpleDocument> pubAttachments = AttachmentServiceFactory.getAttachmentService().listDocumentsByForeignKeyAndType(pub.getPK(), DocumentType.attachment, "fr"); // TODO manager langue
						
			SilverTrace.debug(SpMobileLogModule.getName(), "ServiceDocumentsImpl.getPublication", "Attachments number=" + pubAttachments.size());
			
			for (SimpleDocument attachment : pubAttachments) {
				AttachmentDTO attach = new AttachmentDTO();						
				attach.setTitle(attachment.getTitle());
				if (attachment.getTitle() == null || attachment.getTitle().isEmpty()) {
					attach.setTitle(attachment.getFilename());
				}			
				attach.setInstanceId(attachment.getInstanceId());
				attach.setId(attachment.getId());
				attach.setLang(attachment.getLanguage());
				
				
				
				attach.setUserId(getUserInSession().getId());
				attach.setType(attachment.getContentType());
				attachments.add(attach);
				attach.setAuthor(attachment.getCreatedBy());
				attach.setOrderNum(attachment.getOrder());
				attach.setSize(attachment.getSize());
				attach.setCreationDate(attachment.getCreated());		
			}
			dto.setAttachments(attachments);		
			
			return dto;
		} catch (Throwable e) {
			SilverTrace.error(SpMobileLogModule.getName(), "ServiceDocumentsImpl.getPublication", "root.EX_NO_MESSAGE", e);			
			throw new DocumentsException(e.getMessage());
		}	
	}

	@Override
	public List<BaseDTO> getTopicsAndPublications(String instanceId, String rootTopicId) throws DocumentsException, AuthenticationException {
		checkUserInSession();	
		ArrayList<BaseDTO> list = new ArrayList<BaseDTO>();		
		list.addAll(getTopics(instanceId, rootTopicId));
		list.addAll(getPublications(instanceId, rootTopicId));
		return list;
	}
}
