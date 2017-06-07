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

  @DefaultMessage("© 2001-2017 <a target='_blank' href='http://www.silverpeas.com'>Silverpeas</a>	- Tous droits réservés")
  SafeHtml copyright();

  @DefaultMessage("Parcourir la plateforme")
  SafeHtml browser();

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

  @DefaultMessage("Aide")
  String help();

  @DefaultMessage("Voir mes favoris")
  SafeHtml appFavortis();

  @DefaultMessage("Configuration")
  String config();

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

  @DefaultMessage("Cette action nécessite une connexion.")
  String needToBeOnline();

  @DefaultMessage("Erreur système")
  String systemError();

  @DefaultMessage("Espaces")
  String navigationTitle();

  @DefaultMessage("Mes favoris")
  SafeHtml favorites();

  @DefaultMessage("Les dernières publications")
  SafeHtml lastPublications();

  @DefaultMessage("brouillon")
  String draft();

  @DefaultMessage("pas encore ou plus visible")
  String notVisible();

}
