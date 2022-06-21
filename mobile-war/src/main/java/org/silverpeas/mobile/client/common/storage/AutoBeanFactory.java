package org.silverpeas.mobile.client.common.storage;

import com.google.web.bindery.autobean.shared.AutoBean;
import org.silverpeas.mobile.shared.dto.IFullUser;
import org.silverpeas.mobile.shared.dto.authentication.IUserProfile;
import org.silverpeas.mobile.shared.dto.configuration.IConfig;

/**
 * @author svu
 */
public interface AutoBeanFactory extends com.google.web.bindery.autobean.shared.AutoBeanFactory {
  AutoBean<IConfig> iconfig();
  AutoBean<IUserProfile> iuserprofile();

  AutoBean<IFullUser> ifulluser();
}
