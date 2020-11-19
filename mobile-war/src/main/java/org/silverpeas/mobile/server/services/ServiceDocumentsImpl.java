/*
 * Copyright (C) 2000 - 2020 Silverpeas
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.server.services;

import org.silverpeas.components.kmelia.KmeliaPublicationHelper;
import org.silverpeas.components.kmelia.model.KmeliaPublication;
import org.silverpeas.components.kmelia.model.PubliAuthorComparatorAsc;
import org.silverpeas.components.kmelia.model.PubliCreationDateComparatorAsc;
import org.silverpeas.components.kmelia.model.PubliImportanceComparatorDesc;
import org.silverpeas.components.kmelia.model.PubliRankComparatorAsc;
import org.silverpeas.components.kmelia.model.PubliUpdateDateComparatorAsc;
import org.silverpeas.components.kmelia.service.KmeliaService;
import org.silverpeas.core.ResourceReference;
import org.silverpeas.core.admin.ProfiledObjectId;
import org.silverpeas.core.admin.component.model.ComponentInstLight;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.User;
import org.silverpeas.core.comment.service.CommentServiceProvider;
import org.silverpeas.core.contribution.attachment.AttachmentServiceProvider;
import org.silverpeas.core.contribution.attachment.model.SimpleDocument;
import org.silverpeas.core.contribution.attachment.model.SimpleDocumentPK;
import org.silverpeas.core.contribution.publication.model.CompletePublication;
import org.silverpeas.core.contribution.publication.model.PublicationDetail;
import org.silverpeas.core.contribution.publication.model.PublicationLink;
import org.silverpeas.core.contribution.publication.model.PublicationPK;
import org.silverpeas.core.contribution.publication.service.PublicationService;
import org.silverpeas.core.node.model.NodeDetail;
import org.silverpeas.core.node.model.NodePK;
import org.silverpeas.core.node.service.NodeService;
import org.silverpeas.core.silverstatistics.access.service.StatisticService;
import org.silverpeas.core.util.LocalizationBundle;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.ServiceProvider;
import org.silverpeas.core.util.StringUtil;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.mobile.server.common.SpMobileLogModule;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.documents.AttachmentDTO;
import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import org.silverpeas.mobile.shared.dto.documents.TopicDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.DocumentsException;
import org.silverpeas.mobile.shared.services.ServiceDocuments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Service de gestion des GED.
 * @author svuillet
 */
public class ServiceDocumentsImpl extends AbstractAuthenticateService implements ServiceDocuments {

  private static final long serialVersionUID = 1L;
  private OrganizationController organizationController = OrganizationController.get();

  /**
   * Retourne tous les topics de premier niveau d'un topic.
   */
  @Override
  public List<TopicDTO> getTopics(String instanceId, String rootTopicId) throws DocumentsException, AuthenticationException {
    checkUserInSession();
    List<TopicDTO> topicsList = new ArrayList<TopicDTO>();
    boolean coWriting = false;
    try {
      coWriting = isCoWritingEnabled(instanceId);
    } catch (Exception e) { }
    try {
      if (rootTopicId == null || rootTopicId.isEmpty()) {
        rootTopicId = "0";
      }
      NodePK pk = new NodePK(rootTopicId, instanceId);
      NodeDetail rootNode = getNodeBm().getDetail(pk);
      TopicDTO rootTopic = new TopicDTO();
      rootTopic.setRoot(true);
      if (rootNode.hasFather()) {
        rootTopic.setName(rootNode.getName());
        rootTopic.setId(String.valueOf(rootNode.getId()));
      } else {
        ComponentInstLight app = Administration.get().getComponentInstLight(instanceId);
        rootTopic.setName(app.getLabel());
      }
      topicsList.add(rootTopic);
      List<NodeDetail> nodes = getNodeBm().getSubTreeByLevel(pk, rootNode.getLevel() + 1);
      TopicDTO trash = null;
      for (NodeDetail nodeDetail : nodes) {
        if (rootTopicId.equals(nodeDetail.getFatherPK().getId())) {
          TopicDTO topic = new TopicDTO();
          if (nodeDetail.getId() != 2) {

            if (isCurrentTopicAvailable(nodeDetail)) {
              topic.setId(String.valueOf(nodeDetail.getId()));
              topic.setName(nodeDetail.getName());
              int childrenNumber = nodeDetail.getChildrenNumber();

              // count publications
              Collection<NodePK> pks = getAllSubNodePKs(nodeDetail.getNodePK());
              List<String> ids = new ArrayList<String>();
              ids.add(String.valueOf(nodeDetail.getId()));
              if (isRightsOnTopicsEnabled(instanceId)) {
                for (NodePK onePk : pks) {
                  NodeDetail oneNode = getNodeBm().getDetail(onePk);
                  if (isCurrentTopicAvailable(oneNode)) {
                    ids.add(onePk.getId());
                  }
                }
              } else {
                for (NodePK onePk : pks) {
                  NodeDetail oneNode = getNodeBm().getDetail(onePk);
                  ids.add(onePk.getId());
                }
              }
              PublicationPK pubPK = new PublicationPK("useless", instanceId);
              List<PublicationDetail> publications = (List<PublicationDetail>) getPubBm().getDetailsByFatherIds(ids, pubPK.getInstanceId(), false);
              int nbPubNotVisible = 0;
              for (PublicationDetail publication : publications) {
                if (coWriting) {
                  if (isRightsOnTopicsEnabled(instanceId)) {
                    NodePK f = KmeliaService.get().getPublicationFatherPK(publication.getPK(), getUserInSession().getId());
                    NodeDetail node = NodeService.get().getHeader(f, false);
                    ProfiledObjectId profiledObjectId = ProfiledObjectId.fromNode(node.getRightsDependsOn());
                    String[] profiles = organizationController
                        .getUserProfiles(getUserInSession().getId(), instanceId, profiledObjectId);
                    if (isSingleReader(profiles) && publication.isDraft()) {
                      nbPubNotVisible++;
                    }
                  } else {
                    String [] profiles = organizationController.getUserProfiles(getUserInSession().getId(), instanceId);
                    if (isSingleReader(profiles) && publication.isDraft()) {
                      nbPubNotVisible++;
                    }
                  }
                } else {
                  if (publication.isDraft() && !publication.getUpdaterId().equals(getUserInSession().getId())) {
                    nbPubNotVisible++;
                  }
                }
              }
              topic.setPubCount(publications.size() - nbPubNotVisible);


              topic.setTerminal(childrenNumber == 0);
              if (nodeDetail.getId() ==1) {
                trash = topic;
              } else {
                topicsList.add(topic);
              }
            }
          }
        }
      }
      if (trash != null) topicsList.add(0, trash);
    } catch (Exception e) {
      SilverLogger.getLogger(this).error("ServiceDocumentsImpl.getTopics", "root.EX_NO_MESSAGE", e);
      throw new DocumentsException(e.getMessage());
    }
    return topicsList;
  }

  private boolean isSingleReader(final String[] profiles) {
    for (String profile : profiles) {
      if (profile.equals("admin")) return false;
      if (profile.equals("publisher")) return false;
      if (profile.equals("writer")) return false;
    }
    return true;
  }

  private boolean isManager(final String[] profiles) {
    for (String profile : profiles) {
      if (profile.equals("admin")) return true;
    }
    return false;
  }

  private boolean isManagerOrPublisherOrWriter(final String[] profiles) {
    for (String profile : profiles) {
      if (profile.equals("admin")) return true;
      if (profile.equals("publisher")) return true;
      if (profile.equals("writer")) return true;
    }
    return false;
  }

  private Collection<NodePK> getAllSubNodePKs(final NodePK pk) throws Exception {
    CopyOnWriteArrayList<NodePK> subNodes = new CopyOnWriteArrayList<NodePK>();
    if (pk != null) {
      subNodes.addAll(getNodeBm().getChildrenPKs(pk));
      for (NodePK subNode : subNodes) {
        subNodes.addAll(getAllSubNodePKs(subNode));
      }
    }
    return subNodes;
  }

  public String getUserTopicProfile(String nodeId, String componentId) {
    return KmeliaService.get().getUserTopicProfile(new NodePK(nodeId, componentId), getUserInSession().getId());
  }

  public boolean isTreeStructure(String componentId) {
    return KmeliaPublicationHelper.isTreeEnabled(componentId);
  }

  public int getDefaultSortValue(String instanceId) throws Exception {
    String defaultSortValue = getMainSessionController().getComponentParameterValue(instanceId, "publicationSort");
    if (!StringUtil.isDefined(defaultSortValue)) {
      defaultSortValue = getSettings().getString("publications.sort.default", "2");
    }
    return Integer.parseInt(defaultSortValue);
  }

  private boolean isManualSortingUsed(List<KmeliaPublication> publications) {
    for (KmeliaPublication publication : publications) {
      if (publication.getDetail().getExplicitRank() > 0) {
        return true;
      }
    }
    return false;
  }

  private List<KmeliaPublication> sortByTitle(List<KmeliaPublication> publications) {
    KmeliaPublication[] pubs = publications.toArray(new KmeliaPublication[publications.size()]);
    for (int i = pubs.length; --i >= 0; ) {
      boolean swapped = false;
      for (int j = 0; j < i; j++) {
        if (pubs[j].getDetail().getName(getUserInSession().getUserPreferences().getLanguage())
            .compareToIgnoreCase(pubs[j + 1].getDetail().getName(getUserInSession().getUserPreferences().getLanguage())) > 0) {
          KmeliaPublication pub = pubs[j];
          pubs[j] = pubs[j + 1];
          pubs[j + 1] = pub;
          swapped = true;
        }
      }
      if (!swapped) {
        break;
      }
    }
    return Arrays.asList(pubs);
  }

  private List<KmeliaPublication> sortByDescription(List<KmeliaPublication> publications) {
    KmeliaPublication[] pubs = publications.toArray(new KmeliaPublication[publications.size()]);
    for (int i = pubs.length; --i >= 0; ) {
      boolean swapped = false;
      for (int j = 0; j < i; j++) {
        String p1 = pubs[j].getDetail().getDescription(getUserInSession().getUserPreferences().getLanguage());
        if (p1 == null) {
          p1 = "";
        }
        String p2 = pubs[j + 1].getDetail().getDescription(getUserInSession().getUserPreferences().getLanguage());
        if (p2 == null) {
          p2 = "";
        }
        if (p1.compareToIgnoreCase(p2) > 0) {
          KmeliaPublication pub = pubs[j];
          pubs[j] = pubs[j + 1];
          pubs[j + 1] = pub;
          swapped = true;
        }
      }
      if (!swapped) {
        break;
      }
    }
    return Arrays.asList(pubs);
  }

  /**
   * Retourne les publications d'un topic (au niveau 1).
   */
  @Override
  public List<PublicationDTO> getPublications(String instanceId, String topicId) throws DocumentsException, AuthenticationException {
    checkUserInSession();
    LocalizationBundle resource = ResourceLocator
        .getLocalizationBundle("org.silverpeas.mobile.multilang.mobileBundle", getUserInSession().getUserPreferences().getLanguage());
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

      //List<PublicationDetail> publications = (List<PublicationDetail>) getPubBm().getDetailsByFatherIds(nodeIds, pubPK.getInstanceId(), false);

      List<KmeliaPublication> publications = KmeliaService.get().getPublicationsOfFolder(nodePK, getUserTopicProfile(nodePK.getId(),pubPK.getInstanceId()), getUserInSession().getId(), isTreeStructure(pubPK.getInstanceId()));
      int sort = -1;
      if (isManualSortingUsed(publications) && sort == -1) {
        // display publications according to manual order defined by admin
        sort = 99;
      } else if (sort == -1) {
        // display publications according to default sort defined on application level or instance
        // level
        sort = getDefaultSortValue(pubPK.getInstanceId());
      }

      switch (sort) {
        case 0:
          Collections.sort(publications, new PubliAuthorComparatorAsc());
          break;
        case 1:
          Collections.sort(publications, new PubliUpdateDateComparatorAsc());
          break;
        case 2:
          Collections.sort(publications, new PubliUpdateDateComparatorAsc());
          Collections.reverse(publications);
          break;
        case 3:
          Collections.sort(publications, new PubliImportanceComparatorDesc());
          break;
        case 4:
          publications = sortByTitle(publications);
          break;
        case 5:
          Collections.sort(publications, new PubliCreationDateComparatorAsc());
          break;
        case 6:
          Collections.sort(publications, new PubliCreationDateComparatorAsc());
          Collections.reverse(publications);
          break;
        case 7:
          publications = sortByDescription(publications);
          break;
        default:
          // display publications according to manual order defined by admin
          Collections.sort(publications, new PubliRankComparatorAsc());
      }


      String[] profiles;

      if (isRightsOnTopicsEnabled(instanceId)) {
        NodeDetail node = NodeService.get().getHeader(nodePK, false);
        ProfiledObjectId nodeRef = ProfiledObjectId.fromNode(node.getRightsDependsOn());
        profiles = organizationController.getUserProfiles(getUserInSession().getId(), instanceId, nodeRef);
      } else {
        profiles = organizationController.getUserProfiles(getUserInSession().getId(), instanceId);
      }

      for (KmeliaPublication publication : publications) {
        PublicationDetail publicationDetail = publication.getDetail();
        boolean visible = publicationDetail.isVisible();
        if (isManager(profiles)) visible = true;
        PublicationDTO dto = new PublicationDTO();
        dto.setId(publicationDetail.getId());
        if (publicationDetail.isDraft()) {
          if (publicationDetail.getUpdaterId().equals(getUserInSession().getId())) {
            dto.setName(publicationDetail.getName() + " (" + resource.getString("publication.draft") + ")");
          } else {
            visible = false;
          }
        } else if (publicationDetail.isValid()) {
          dto.setName(publicationDetail.getName());
        } else if (publicationDetail.isValidationRequired()) {
          visible = isManagerOrPublisherOrWriter(profiles);
          dto.setName(publicationDetail.getName() + " (" + resource.getString("publication.tovalidate") + ")");
        } else if (publicationDetail.isRefused()) {
          visible = isManagerOrPublisherOrWriter(profiles); //TODO manage other cases than coediting
          dto.setName(publicationDetail.getName() + " (" + resource.getString("publication.unvalidate") + ")");
        }
        if (visible) pubs.add(dto);
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this).error("ServiceDocumentsImpl.getPublications", "root.EX_NO_MESSAGE", e);
      throw new DocumentsException(e.getMessage());
    }

    return pubs;
  }

  private boolean isCurrentTopicAvailable(NodeDetail node) throws Exception {
    if (isRightsOnTopicsEnabled(node.getNodePK().getInstanceId())) {
      if (node.haveRights()) {
        return node.canBeAccessedBy(getUserInSession());
      }
    }
    return true;
  }

  private boolean isRightsOnTopicsEnabled(String instanceId) throws Exception {
    String value = getMainSessionController().getComponentParameterValue(instanceId, "rightsOnTopics");
    return StringUtil.getBooleanValue(value);
  }

  private boolean isCoWritingEnabled(String instanceId) throws Exception {
    String value = getMainSessionController().getComponentParameterValue(instanceId, "coWriting");
    return StringUtil.getBooleanValue(value);
  }

  private PublicationService getPubBm() {
   return PublicationService.get();
  }

  private NodeService getNodeBm() {
    return NodeService.get();
  }

  private StatisticService getStatisticService() { return ServiceProvider.getService(StatisticService.class); }

  @Override
  public PublicationDTO getPublication(String pubId, String contentType) throws DocumentsException, AuthenticationException {
    SilverLogger.getLogger(this).debug("ServiceDocumentsImpl.getPublication", "getPublication for id " + pubId);
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
      dto.setCommentsNumber(CommentServiceProvider.getCommentService().getCommentsCountOnPublication(contentType, new PublicationPK(pubId)));
      dto.setInstanceId(pub.getInstanceId());
      String wysiwyg = pub.getContent().getRenderer().renderView();
      if (wysiwyg == null|| !wysiwyg.trim().isEmpty() || !pub.getInfoId().equals("0")) {
        dto.setContent(true);
      }

      CompletePublication completePublication = PublicationService.get().getCompletePublication(pub.getPK());
      List<PublicationLink> linkedPublications = completePublication.getLinkList();
      List<PublicationDTO> linkedPub = new ArrayList<>();
      for (PublicationLink link :linkedPublications) {        
        PublicationDetail pubLinked = getPubBm().getDetail(new PublicationPK(link.getTarget().getId()));
        PublicationDTO linkDto = new PublicationDTO();
        linkDto.setId(link.getId());
        linkDto.setName(pubLinked.getName());
        linkDto.setUpdateDate(sdf.format(pubLinked.getUpdateDate()));
        linkedPub.add(linkDto);
      }
      dto.setLinkedPublications(linkedPub);

      ResourceReference resourceReference = new ResourceReference(pubId, pub.getComponentInstanceId());
      getStatisticService().addStat(getUserInSession().getId(), resourceReference, 1, "Publication");

      return dto;
    } catch (Throwable e) {
      SilverLogger.getLogger(this).error("ServiceDocumentsImpl.getPublication", "root.EX_NO_MESSAGE", e);
      throw new DocumentsException(e.getMessage());
    }
  }

  @Override
  public AttachmentDTO getAttachment(String attachmentId, String appId) throws DocumentsException, AuthenticationException {
    SimpleDocumentPK pk = new SimpleDocumentPK(attachmentId, appId);
    SimpleDocument doc = AttachmentServiceProvider.getAttachmentService().searchDocumentById(pk, getUserInSession().getUserPreferences().getLanguage());
    return populate(doc, getUserInSession());
  }

  private AttachmentDTO populate(SimpleDocument attachment, User user) {
    AttachmentDTO attach = new AttachmentDTO();
    attach.setTitle(attachment.getTitle());
    if (attachment.getTitle() == null || attachment.getTitle().isEmpty()) {
      attach.setTitle(attachment.getFilename());
    }
    if (attachment.getDescription() == null || attachment.getDescription().isEmpty()) {
      attach.setDescription(attachment.getDescription());
    }

    attach.setInstanceId(attachment.getInstanceId());
    attach.setId(attachment.getId());
    attach.setLang(attachment.getLanguage());
    attach.setUserId(user.getId());
    attach.setType(attachment.getContentType());
    attach.setAuthor(attachment.getCreatedBy());
    attach.setOrderNum(attachment.getOrder());
    attach.setSize(attachment.getSize());
    attach.setDownloadAllowed(attachment.isDownloadAllowedForRolesFrom(user));
    return attach;
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
