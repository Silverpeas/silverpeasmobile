/*
 * Copyright (C) 2000 - 2022 Silverpeas
 *
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
 * "https://www.silverpeas.org/legal/floss_exception.html"
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
import org.silverpeas.components.kmelia.model.KmeliaPublicationSort;
import org.silverpeas.components.kmelia.service.KmeliaService;
import org.silverpeas.components.quickinfo.model.News;
import org.silverpeas.components.quickinfo.model.QuickInfoService;
import org.silverpeas.core.ResourceReference;
import org.silverpeas.core.admin.ProfiledObjectId;
import org.silverpeas.core.admin.component.model.ComponentInstLight;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.User;
import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.comment.service.CommentServiceProvider;
import org.silverpeas.core.contribution.attachment.AttachmentService;
import org.silverpeas.core.contribution.attachment.AttachmentServiceProvider;
import org.silverpeas.core.contribution.attachment.model.SimpleDocument;
import org.silverpeas.core.contribution.attachment.model.SimpleDocumentPK;
import org.silverpeas.core.contribution.attachment.util.SimpleDocumentList;
import org.silverpeas.core.contribution.publication.dao.PublicationCriteria;
import org.silverpeas.core.contribution.publication.model.*;
import org.silverpeas.core.contribution.publication.service.PublicationService;
import org.silverpeas.core.io.media.image.thumbnail.ThumbnailSettings;
import org.silverpeas.core.node.model.NodeDetail;
import org.silverpeas.core.node.model.NodePK;
import org.silverpeas.core.node.service.NodeService;
import org.silverpeas.core.sharing.services.SharingServiceProvider;
import org.silverpeas.core.sharing.services.SharingTicketService;
import org.silverpeas.core.silverstatistics.access.service.StatisticService;
import org.silverpeas.kernel.bundle.LocalizationBundle;
import org.silverpeas.kernel.bundle.ResourceLocator;
import org.silverpeas.core.util.ServiceProvider;
import org.silverpeas.kernel.bundle.SettingBundle;
import org.silverpeas.kernel.util.StringUtil;
import org.silverpeas.core.util.file.FileServerUtils;
import org.silverpeas.kernel.logging.SilverLogger;
import org.silverpeas.core.web.rs.UserPrivilegeValidation;
import org.silverpeas.core.web.rs.annotation.Authorized;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.documents.AttachmentDTO;
import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import org.silverpeas.mobile.shared.dto.documents.TopicDTO;
import org.silverpeas.mobile.shared.dto.tickets.TicketDTO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Service de gestion des GED.
 */
@WebService
@Authorized
@Path(ServiceDocuments.PATH + "/{appId}")
public class ServiceDocuments extends AbstractRestWebService {

  static final String PATH = "mobile/documents";
  private OrganizationController organizationController = OrganizationController.get();

  @Context
  HttpServletRequest request;

  @PathParam("appId")
  private String componentId;

  /**
   * Retourne tous les topics de premier niveau d'un topic.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("topics/{rootTopicId}")
  public List<TopicDTO> getTopics(@PathParam("appId") String instanceId,
      @PathParam("rootTopicId") String rootTopicId) throws Exception {
    List<TopicDTO> topicsList = new ArrayList<>();
    boolean coWriting = false;
    try {
      coWriting = isCoWritingEnabled(instanceId);
    } catch (Exception e) {
    }
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
        rootTopic.setId(rootNode.getId());
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
          if (!nodeDetail.isUnclassified()) {

            if (isCurrentTopicAvailable(nodeDetail)) {
              topic.setId(nodeDetail.getId());
              topic.setName(nodeDetail.getName());
              int childrenNumber = nodeDetail.getChildrenNumber();

              // count publications
              Collection<NodePK> pks = getAllSubNodePKs(nodeDetail.getNodePK());
              List<String> ids = new ArrayList<>();
              ids.add(nodeDetail.getId());
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
              List<PublicationDetail> publications =
                  (List<PublicationDetail>) getPubBm().getDetailsByFatherIds(ids,
                      pubPK.getInstanceId(), false);
              int nbPubNotVisible = 0;
              for (PublicationDetail publication : publications) {
                if (coWriting) {
                  if (isRightsOnTopicsEnabled(instanceId)) {
                    NodePK f = KmeliaService.get()
                        .getBestLocationOfPublicationForUser(publication.getPK(),
                            getUser().getId());
                    NodeDetail node = NodeService.get().getHeader(f, false);
                    ProfiledObjectId profiledObjectId =
                        ProfiledObjectId.fromNode(node.getRightsDependsOn());
                    String[] profiles =
                        organizationController.getUserProfiles(getUser().getId(), instanceId,
                            profiledObjectId);
                    if (isSingleReader(profiles) && publication.isDraft()) {
                      nbPubNotVisible++;
                    }
                  } else {
                    String[] profiles =
                        organizationController.getUserProfiles(getUser().getId(), instanceId);
                    if (isSingleReader(profiles) && publication.isDraft()) {
                      nbPubNotVisible++;
                    }
                  }
                } else {
                  if (publication.isDraft() &&
                      !publication.getUpdaterId().equals(getUser().getId())) {
                    nbPubNotVisible++;
                  }
                }
              }
              topic.setPubCount(publications.size() - nbPubNotVisible);


              topic.setTerminal(childrenNumber == 0);
              if (nodeDetail.isBin()) {
                trash = topic;
              } else {
                topicsList.add(topic);
              }
            }
          }
        }
      }

      if (trash != null) {
        boolean atfirst = getSettings().getBoolean("ged.trash.display.atfirst", true);
        if (atfirst) {
          topicsList.add(0, trash);
        } else {
          topicsList.add(trash);
        }
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
      throw e;
    }
    return topicsList;
  }

  private boolean isSingleReader(final String[] profiles) {
    for (String profile : profiles) {
      if (profile.equals("admin")) {
        return false;
      }
      if (profile.equals("publisher")) {
        return false;
      }
      if (profile.equals("writer")) {
        return false;
      }
    }
    return true;
  }

  private boolean isManager(final String[] profiles) {
    for (String profile : profiles) {
      if (profile.equals("admin")) {
        return true;
      }
    }
    return false;
  }

  private boolean isManagerOrPublisherOrWriter(final String[] profiles) {
    for (String profile : profiles) {
      if (profile.equals("admin")) {
        return true;
      }
      if (profile.equals("publisher")) {
        return true;
      }
      if (profile.equals("writer")) {
        return true;
      }
    }
    return false;
  }

  private Collection<NodePK> getAllSubNodePKs(final NodePK pk) throws Exception {
    CopyOnWriteArrayList<NodePK> subNodes = new CopyOnWriteArrayList<>();
    if (pk != null) {
      subNodes.addAll(getNodeBm().getChildrenPKs(pk));
      for (NodePK subNode : subNodes) {
        subNodes.addAll(getAllSubNodePKs(subNode));
      }
    }
    return subNodes;
  }

  public String getUserTopicProfile(String nodeId, String componentId) {
    return KmeliaService.get()
        .getUserTopicProfile(new NodePK(nodeId, componentId), getUser().getId());
  }

  public boolean isTreeStructure(String componentId) {
    return KmeliaPublicationHelper.isTreeEnabled(componentId);
  }

  public int getDefaultSortValue(String instanceId) throws Exception {
    String defaultSortValue =
        getMainSessionController().getComponentParameterValue(instanceId, "publicationSort");
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

  /**
   * Retourne les publications d'un topic (au niveau 1).
   */
  @GET
  @Path("publications/{topicId}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<PublicationDTO> getPublications(@PathParam("appId") String instanceId,
      @PathParam("topicId") String topicId) throws Exception {
    final String userLanguage = getUser().getUserPreferences().getLanguage();
    LocalizationBundle resource =
        ResourceLocator.getLocalizationBundle("org.silverpeas.mobile.multilang.mobileBundle",
            userLanguage);
    ArrayList<PublicationDTO> pubs = new ArrayList<>();

    try {
      if (topicId == null || topicId.isEmpty()) {
        topicId = "0";
      }
      NodePK nodePK = new NodePK(topicId, instanceId);
      PublicationPK pubPK = new PublicationPK("useless", instanceId);
      List<KmeliaPublication> publications = KmeliaService.get().getAuthorizedPublicationsOfFolder(nodePK,
          getUserTopicProfile(nodePK.getId(), pubPK.getInstanceId()), getUser().getId(),
          isTreeStructure(pubPK.getInstanceId()));
      final int sort;
      if (isManualSortingUsed(publications)) {
        // display publications according to manual order defined by admin
        sort = 99;
      } else {
        // display publications according to default sort defined on application level or instance
        // level
        sort = getDefaultSortValue(pubPK.getInstanceId());
      }
      new KmeliaPublicationSort(sort).withContentLanguage(userLanguage).sort(publications);

      String[] profiles;

      if (isRightsOnTopicsEnabled(instanceId)) {
        NodeDetail node = NodeService.get().getHeader(nodePK, false);
        ProfiledObjectId nodeRef = ProfiledObjectId.fromNode(node.getRightsDependsOn());
        profiles = organizationController.getUserProfiles(getUser().getId(), instanceId, nodeRef);
      } else {
        profiles = organizationController.getUserProfiles(getUser().getId(), instanceId);
      }

      for (KmeliaPublication publication : publications) {
        PublicationDetail publicationDetail = publication.getDetail();
        boolean visible = publicationDetail.isVisible();
        if (isManager(profiles)) {
          visible = true;
        }
        PublicationDTO dto = new PublicationDTO();
        dto.setId(publicationDetail.getId());
        dto.setVignette(getVignetteUrl(publicationDetail));
        dto.setDraft(publicationDetail.isDraft());
        dto.setPublishable(isPublishable(publicationDetail));

        if (publicationDetail.isDraft()) {
          if (publicationDetail.getUpdaterId().equals(getUser().getId())) {
            dto.setName(
                publicationDetail.getName() + " (" + resource.getString("publication.draft") + ")");
          } else {
            visible = false;
          }
        } else if (publicationDetail.isValid()) {
          dto.setName(publicationDetail.getName());
        } else if (publicationDetail.isValidationRequired()) {
          visible = isManagerOrPublisherOrWriter(profiles);
          dto.setName(
              publicationDetail.getName() + " (" + resource.getString("publication.tovalidate") +
                  ")");
        } else if (publicationDetail.isRefused()) {
          visible = isManagerOrPublisherOrWriter(profiles); //TODO manage other cases than coediting
          dto.setName(
              publicationDetail.getName() + " (" + resource.getString("publication.unvalidate") +
                  ")");
        }
        if (visible) {
          pubs.add(dto);
        }
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this)
          .error("ServiceDocumentsImpl.getPublications", "root.EX_NO_MESSAGE", e);
      throw e;
    }

    return pubs;
  }

  private boolean isCurrentTopicAvailable(NodeDetail node) throws Exception {
    if (isRightsOnTopicsEnabled(node.getNodePK().getInstanceId())) {
      if (node.haveRights()) {
        return node.canBeAccessedBy(getUser());
      }
    }
    return true;
  }

  private boolean isRightsOnTopicsEnabled(String instanceId) throws Exception {
    String value =
        getMainSessionController().getComponentParameterValue(instanceId, "rightsOnTopics");
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

  private StatisticService getStatisticService() {
    return ServiceProvider.getService(StatisticService.class);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("publication/{id}")
  public PublicationDTO getPublication(@PathParam("id") String id,
      @QueryParam("contributionId") String contributionId, @QueryParam("type") String type) {
    SilverLogger.getLogger(this)
        .debug("ServiceDocumentsImpl.getPublication", "getPublication for id " + id);

    try {
      final String userLanguage = getUser().getUserPreferences().getLanguage();
      LocalizationBundle resource =
              ResourceLocator.getLocalizationBundle("org.silverpeas.mobile.multilang.mobileBundle",
                      userLanguage);
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

      PublicationDetail pub = getPubBm().getDetail(new PublicationPK(id));

      PublicationDTO dto = new PublicationDTO();
      dto.setId(pub.getId());
      dto.setName(pub.getName());
      dto.setCreator(pub.getCreator().getDisplayedName());
      dto.setPublishable(isPublishable(pub));
      dto.setUpdater(organizationController.getUserDetail(pub.getUpdaterId()).getDisplayedName());
      dto.setVersion(pub.getVersion());
      dto.setDraft(pub.isDraft());
      if (pub.isDraft())  dto.setName(pub.getName() + " (" + resource.getString("publication.draft") + ")");

      dto.setDescription(pub.getDescription());
      dto.setUpdateDate(sdf.format(pub.getLastUpdateDate()));
      dto.setCreationDate(sdf.format(pub.getCreationDate()));
      dto.setNotAllowedDownloads(new ArrayList<>());

      if (type.equals(ContentsTypes.Publication.toString())) {
        try {
          KmeliaPublication kPub = KmeliaService.get().getPublication(new PublicationPK(id), null);
          dto.setViewsNumber(kPub.getNbAccess());
        } catch (Exception e) {
          SilverLogger.getLogger(this).warn("Unable to get views number", e);
        }

        // List all attachements not downloadable for readers
        ResourceReference foreignKey = new ResourceReference(id, componentId);
        SimpleDocumentList<SimpleDocument> attachements = AttachmentServiceProvider.getAttachmentService().listAllDocumentsByForeignKey(foreignKey, null);
        ArrayList<String> notAllowedDownloads = new ArrayList<>();
        for (SimpleDocument attachement : attachements) {
          if (!attachement.isDownloadAllowedForReaders() && !attachement.canBeModifiedBy(getUser())) {
            notAllowedDownloads.add(attachement.getId());
          }
        }
        dto.setNotAllowedDownloads(notAllowedDownloads);

      } else if (type.equals(ContentsTypes.News.toString())) {
        dto.setViewsNumber(QuickInfoService.get().getNews(contributionId).getNbAccess());
      } else {
        dto.setViewsNumber(0);
      }

      if (type.equals("News")) {
        final ResourceReference ref = new ResourceReference(new PublicationPK(contributionId));
        dto.setCommentsNumber(
            CommentServiceProvider.getCommentService().getCommentsCountOnResource(type, ref));
      } else {
        final ResourceReference ref = new ResourceReference(new PublicationPK(id));
        dto.setCommentsNumber(
            CommentServiceProvider.getCommentService().getCommentsCountOnResource(type, ref));
      }
      dto.setInstanceId(pub.getInstanceId());
      String wysiwyg = pub.getContent().getRenderer().renderView();
      if (wysiwyg == null || !wysiwyg.trim().isEmpty() || !pub.getInfoId().equals("0")) {
        dto.setContent(true);
      }

      CompletePublication completePublication =
          PublicationService.get().getCompletePublication(pub.getPK());
      List<PublicationLink> linkedPublications = completePublication.getLinkList();
      List<PublicationDTO> linkedPub = new ArrayList<>();
      for (PublicationLink link : linkedPublications) {
        PublicationDetail pubLinked =
            getPubBm().getDetail(new PublicationPK(link.getTarget().getId()));
        PublicationDTO linkDto = new PublicationDTO();
        linkDto.setId(link.getId());
        linkDto.setName(pubLinked.getName());
        linkDto.setUpdateDate(sdf.format(pubLinked.getLastUpdateDate()));
        linkedPub.add(linkDto);
      }
      dto.setLinkedPublications(linkedPub);

      ResourceReference resourceReference = new ResourceReference(id, pub.getInstanceId());
      getStatisticService().addStat(getUser().getId(), resourceReference, 1, "Publication");

      return dto;
    } catch (Throwable e) {
      SilverLogger.getLogger(this)
          .error("ServiceDocumentsImpl.getPublication", "root.EX_NO_MESSAGE", e);
      throw e;
    }
  }

  private String getVignetteUrl(PublicationDetail pub) {
    SettingBundle publicationSettings =
        ResourceLocator.getSettingBundle("org.silverpeas.publication.publicationSettings");
    SettingBundle kmeliaSettings =
        ResourceLocator.getSettingBundle("org.silverpeas.kmelia.settings.kmeliaSettings");

    int width = kmeliaSettings.getInteger("vignetteWidth", -1);
    int height = kmeliaSettings.getInteger("vignetteHeight", -1);
    ThumbnailSettings thumbnailSettings =
        ThumbnailSettings.getInstance(pub.getInstanceId(), width, height);

    String vignetteUrl;
    String w = String.valueOf(thumbnailSettings.getWidth());
    String h = String.valueOf(thumbnailSettings.getHeight());

    if (pub.getImage() == null) {
      return null;
    }

    if (pub.getImage().startsWith("/")) {
      vignetteUrl = pub.getImage();
    } else {
      vignetteUrl =
          FileServerUtils.getUrl(pub.getPK().getComponentName(), "vignette", pub.getImage(),
              pub.getImageMimeType(), publicationSettings.getString("imagesSubDirectory"));
      if (StringUtil.isDefined(pub.getThumbnail().getCropFileName())) {
        // thumbnail is cropped, no resize
        w = null;
        h = null;
      } else {
        vignetteUrl += "&Size=" + StringUtil.defaultStringIfNotDefined(w) + "x" +
            StringUtil.defaultStringIfNotDefined(h);
      }
    }

    return vignetteUrl;
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("tickets")
  public List<TicketDTO> getTickets(@PathParam("appId") String appId, List<TicketDTO> tickets) throws Exception {
    for (TicketDTO dto : tickets) {
      if (dto.getSharedObjectType().equalsIgnoreCase("Node")) {
        NodeDetail node = NodeService.get().getDetail(new NodePK(dto.getSharedObjectId(), dto.getComponentId()));
        dto.setName(node.getName());
      } else if (dto.getSharedObjectType().equalsIgnoreCase("Publication")) {
        PublicationDetail pub = PublicationService.get().getDetail(new PublicationPK(dto.getSharedObjectId()));
        dto.setName(pub.getName());
      } else {
        SimpleDocument doc = AttachmentServiceProvider.getAttachmentService().searchDocumentById(new SimpleDocumentPK(
                dto.getSharedObjectId()), getUser().getUserPreferences().getLanguage());
        dto.setName(doc.getFilename());
      }
    }
    return tickets;
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("deletetickets")
  public List<TicketDTO> deleteTickets(@PathParam("appId") String appId, List<TicketDTO> tickets) throws Exception {
    for (TicketDTO t : tickets) {
      SharingServiceProvider.getSharingTicketService().deleteTicket(t.getToken());
    }
    return tickets;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("attachment/{attachmentId}")
  public AttachmentDTO getAttachment(@PathParam("attachmentId") String attachmentId,
      @PathParam("appId") String appId) throws Exception {
    SimpleDocumentPK pk = new SimpleDocumentPK(attachmentId, appId);
    SimpleDocument doc = AttachmentServiceProvider.getAttachmentService()
        .searchDocumentById(pk, getUser().getUserPreferences().getLanguage());
    return populate(doc, getUser());
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

  @GET
  @Path("topicsAndPublications/{rootTopicId}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<BaseDTO> getTopicsAndPublications(@PathParam("appId") String instanceId,
      @PathParam("rootTopicId") String rootTopicId) throws Exception {
    ArrayList<BaseDTO> list = new ArrayList<>();
    list.addAll(getTopics(instanceId, rootTopicId));
    list.addAll(getPublications(instanceId, rootTopicId));
    return list;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("nextpublication/{id}/{direction}")
  public PublicationDTO getNextPublication(@PathParam("appId") String instanceId, @PathParam("id") String id, @PathParam("direction") String direction) throws Exception {

    PublicationDTO dto = new PublicationDTO();
    try {
      PublicationPK pubPK = new PublicationPK(id);
      PublicationDetail pub = getPubBm().getDetail(pubPK);

      List<Location> locations = getPubBm().getAllLocations(pubPK);
      String folderId = locations.get(0).getLocalId();


      NodePK nodePK = new NodePK(folderId, instanceId);
      List<KmeliaPublication> publications = KmeliaService.get().getAuthorizedPublicationsOfFolder(nodePK,
              getUserTopicProfile(nodePK.getId(), instanceId), getUser().getId(),
              isTreeStructure(instanceId));


      int sort;
      if (isManualSortingUsed(publications)) {
        sort = 99;
      } else {
        sort = getDefaultSortValue(pubPK.getInstanceId());
      }
      new KmeliaPublicationSort(sort).withContentLanguage(getUser().getUserPreferences().getLanguage()).sort(publications);

      KmeliaPublication next = null;
      for (int i = 0; i < publications.size(); i++) {
        if (publications.get(i).getId().equals(id)) {
          if (direction.equalsIgnoreCase("right")) {
            if (i == publications.size() - 1) {
              next = publications.get(0);
            } else {
              next = publications.get(i + 1);
            }
          } else if (direction.equalsIgnoreCase("left")) {
            if (i == 0) {
              next = publications.get(publications.size() - 1);
            } else {
              next = publications.get(i - 1);
            }
          }
        }
      }
      
      dto.setId(next.getId());
      dto.setName(next.getName());
      dto.setDescription(next.getDescription());

    } catch(Throwable e) {
      SilverLogger.getLogger(this)
              .error("ServiceDocumentsImpl.getNextPublication", "root.EX_NO_MESSAGE", e);
      throw e;
    }

    return dto;
  }

  private boolean isPublishable(PublicationDetail pub) {
    return pub.canBeModifiedBy(getUser());
  }

  @POST
  @Path("publish/{pubId}")
  @Produces(MediaType.APPLICATION_JSON)
  public PublicationDTO publish(@PathParam("appId") String appId, @PathParam("pubId") String pubId) throws Exception {
    PublicationDTO dto = new PublicationDTO();
    if (appId.startsWith("quickinfo")) {
      News n = QuickInfoService.get().getNewsByForeignId(pubId);
      QuickInfoService.get().publish(n.getId(), getUser().getId());
      dto.setName(QuickInfoService.get().getNews(n.getId()).getName());
    } else {
      CompletePublication pub = PublicationService.get().getCompletePublication(new PublicationPK(pubId));
      NodePK nodePK = KmeliaService.get().getPublicationFatherPK(new PublicationPK(pubId, pub.getPublicationDetail().getInstanceId()));
      String profile = getUserTopicProfile(nodePK.getId(), appId);
      KmeliaService.get().draftOutPublication(new PublicationPK(pubId), new NodePK(nodePK.getId(), appId), profile);
      dto.setName(pub.getPublicationDetail().getName(getUser().getUserPreferences().getLanguage()));
    }
    return dto;
  }

  @Override
  protected String getResourceBasePath() {
    return PATH;
  }

  @Override
  public void validateUserAuthorization(UserPrivilegeValidation validation) {
    if (getComponentId() != null && !getComponentId().equalsIgnoreCase("null")) {
      super.validateUserAuthorization(validation);
    }
  }

  @Override
  public String getComponentId() {
    return this.componentId;
  }
}
