package com.oosphere.silverpeasmobile.bean;

import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;
import com.silverpeas.admin.ejb.AdminBm;
import com.silverpeas.formTemplate.ejb.FormTemplateBm;
import com.silverpeas.pdc.ejb.PdcBm;
import com.silverpeas.projectManager.control.ejb.ProjectManagerBm;
import com.silverpeas.tags.util.EJBDynaProxy;
import com.stratelia.webactiv.kmelia.control.ejb.KmeliaBm;
import com.stratelia.webactiv.searchEngine.control.ejb.SearchEngineBm;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.node.control.NodeBm;
import com.stratelia.webactiv.util.publication.control.PublicationBm;
import com.stratelia.webactiv.calendar.control.CalendarBm;

public class WebBeanFactory implements BeanFactoryInterface {

  private AdminBm adminBm;
  private PdcBm pdcBm;
  private FormTemplateBm formTemplateBm;
  private NodeBm nodeBm;
  private KmeliaBm kmeliaBm;
  private PublicationBm publicationBm;
  private SearchEngineBm searchEngineBm;
  private CalendarBm calendarBm;
  private ProjectManagerBm projectManagerBm;

  public CalendarBm getCalendarBm()
      throws SilverpeasMobileException {
    if (calendarBm == null) {
      try {
        calendarBm = (CalendarBm) EJBDynaProxy.createProxy(JNDINames.CALENDARBM_EJBHOME, CalendarBm.class);
      } catch (Exception e) {
        throw new SilverpeasMobileException(this, "getCalendarBm", "EX_GET_BEAN_FAILED", e);
      }
    }
    return calendarBm;
  }
  
  public AdminBm getAdminBm()
      throws SilverpeasMobileException {
    if (adminBm == null) {
      try {
        adminBm = (AdminBm) EJBDynaProxy.createProxy(JNDINames.ADMINBM_EJBHOME, AdminBm.class);
      } catch (Exception e) {
        throw new SilverpeasMobileException(this, "getAdminBm", "EX_GET_BEAN_FAILED", e);
      }
    }
    return adminBm;
  }

  public FormTemplateBm getFormTemplateBm() throws SilverpeasMobileException {
    if (formTemplateBm == null) {
      try {
        formTemplateBm = (FormTemplateBm) EJBDynaProxy.createProxy(
            JNDINames.FORMTEMPLATEBM_EJBHOME, FormTemplateBm.class);
      } catch (Exception e) {
        throw new SilverpeasMobileException(this, "getFormTemplateBm", "EX_GET_BEAN_FAILED", e);
      }
    }
    return formTemplateBm;
  }

  public KmeliaBm getKmeliaBm() throws SilverpeasMobileException {
    if (kmeliaBm == null) {
      try {
        kmeliaBm = (KmeliaBm) EJBDynaProxy.createProxy(JNDINames.KMELIABM_EJBHOME, KmeliaBm.class);
      } catch (Exception e) {
        throw new SilverpeasMobileException(this, "getKmeliaBm", "EX_GET_BEAN_FAILED", e);
      }
    }
    return kmeliaBm;
  }

  public NodeBm getNodeBm() throws SilverpeasMobileException {
    if (nodeBm == null) {
      try {
        nodeBm = (NodeBm) EJBDynaProxy.createProxy(JNDINames.NODEBM_EJBHOME, NodeBm.class);
      } catch (Exception e) {
        throw new SilverpeasMobileException(this, "getNodeBm", "EX_GET_BEAN_FAILED", e);
      }
    }
    return nodeBm;
  }

  public PdcBm getPdcBm() throws SilverpeasMobileException {
    if (pdcBm == null) {
      try {
        pdcBm = (PdcBm) EJBDynaProxy.createProxy(JNDINames.PDCBM_EJBHOME, PdcBm.class);
      } catch (Exception e) {
        throw new SilverpeasMobileException(this, "getPdcBm", "EX_GET_BEAN_FAILED", e);
      }
    }
    return pdcBm;
  }

  public PublicationBm getPublicationBm() throws SilverpeasMobileException {
    if (publicationBm == null) {
      try {
        publicationBm = (PublicationBm) EJBDynaProxy.createProxy(
            JNDINames.PUBLICATIONBM_EJBHOME, PublicationBm.class);
      } catch (Exception e) {
        throw new SilverpeasMobileException(this, "getPublicationBm", "EX_GET_BEAN_FAILED", e);
      }
    }
    return publicationBm;
  }

  public SearchEngineBm getSearchEngineBm() throws SilverpeasMobileException {
    if (searchEngineBm == null) {
      try {
        searchEngineBm = (SearchEngineBm) EJBDynaProxy.createProxy(
            JNDINames.SEARCHBM_EJBHOME, SearchEngineBm.class);
      } catch (Exception e) {
        throw new SilverpeasMobileException(this, "getSearchEngineBm", "EX_GET_BEAN_FAILED", e);
      }
    }
    return searchEngineBm;
  }
  
  public ProjectManagerBm getProjectManagerBm() throws SilverpeasMobileException {
    if (projectManagerBm == null) {
      try {
        projectManagerBm = (ProjectManagerBm) EJBDynaProxy.createProxy(
            JNDINames.PROJECTMANAGERBM_EJBHOME, ProjectManagerBm.class);
      } catch (Exception e) {
        throw new SilverpeasMobileException(this, "getProjectManagerBm", "EX_GET_BEAN_FAILED", e);
      }
    }
    return projectManagerBm;
  }

}
