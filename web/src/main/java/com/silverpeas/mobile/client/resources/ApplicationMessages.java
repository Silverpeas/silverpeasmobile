package com.silverpeas.mobile.client.resources;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;


public interface ApplicationMessages extends Messages {
  @DefaultMessage("Login")
  SafeHtml loginLabel();

  @DefaultMessage("Silverpeas &gt; Ecran de connexion")
  SafeHtml loginTitle();

  @DefaultMessage("Confirmer")
  String confirmBtnLabel();
  @DefaultMessage("Annuler")
  String cancelBtnLabel();
  @DefaultMessage("Corriger")
  String correctBtnLabel();

  @DefaultMessage("© 2001-2014 <a target='_blank' href='http://www.silverpeas.com'>Silverpeas</a>	- Tous droits réservés")
  SafeHtml copyright();

  @DefaultMessage("Information")
  String infoTitle();
  @DefaultMessage("OK")
  String ok();

  @DefaultMessage("Home")
  String home();

  @DefaultMessage("Mes tâches")
  String tasks();

  @DefaultMessage("Déconnexion")
  String disconnect();

  @DefaultMessage("Espace personnel")
  String personalSpace();

  @Messages.DefaultMessage("Modifier mon statut")
  String editStatus();

  @DefaultMessage("Contenus trouvés")
  String results();
}
