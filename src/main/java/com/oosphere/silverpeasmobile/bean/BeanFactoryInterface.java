package com.oosphere.silverpeasmobile.bean;

import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;
import com.silverpeas.admin.ejb.AdminBm;
import com.silverpeas.formTemplate.ejb.FormTemplateBm;
import com.silverpeas.pdc.ejb.PdcBm;
import com.stratelia.webactiv.kmelia.control.ejb.KmeliaBm;
import com.stratelia.webactiv.searchEngine.control.ejb.SearchEngineBm;
import com.stratelia.webactiv.util.node.control.NodeBm;
import com.stratelia.webactiv.util.publication.control.PublicationBm;

public interface BeanFactoryInterface {

  public AdminBm getAdminBm()
  throws SilverpeasMobileException;
  
  public PdcBm getPdcBm()
  throws SilverpeasMobileException;
  
  public FormTemplateBm getFormTemplateBm()
  throws SilverpeasMobileException;
  
  public NodeBm getNodeBm()
  throws SilverpeasMobileException;
  
  public KmeliaBm getKmeliaBm()
  throws SilverpeasMobileException;
  
  public PublicationBm getPublicationBm()
  throws SilverpeasMobileException;
  
  public SearchEngineBm getSearchEngineBm()
  throws SilverpeasMobileException;
  
}
