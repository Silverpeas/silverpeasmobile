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

  @DefaultMessage("Accueil")
  String home();

  @DefaultMessage("Go")
  String search();

  @DefaultMessage("Retour")
  String back();

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

  @DefaultMessage("Documents")
  SafeHtml appDocuments();

  @DefaultMessage("Médiathèque")
  SafeHtml appMedia();

  @DefaultMessage("Trouver un <strong>contact</strong>")
  SafeHtml appContact();


  @DefaultMessage("Mettre à jour <strong>mon statut</strong>")
  SafeHtml appStatut();

  @DefaultMessage("Les <strong>actualités</strong>")
  SafeHtml appActus();
}
