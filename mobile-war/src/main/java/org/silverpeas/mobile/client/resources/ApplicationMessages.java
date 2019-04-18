/*
 * Copyright (C) 2000 - 2019 Silverpeas
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

package org.silverpeas.mobile.client.resources;

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

  @DefaultMessage("© 2001-2019 <a target='_blank' href='http://www.silverpeas.com'>Silverpeas</a>	- Tous droits réservés")
  SafeHtml copyright();

  @DefaultMessage("Parcourir la plateforme")
  SafeHtml browser();

  @DefaultMessage("Information")
  String infoTitle();
  @DefaultMessage("OK")
  String ok();

  @DefaultMessage("Accueil")
  String home();

  @DefaultMessage("Discussions")
  String tchat();

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

  @DefaultMessage("Agenda")
  String usercalendar();

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

  @DefaultMessage("Login ou mot de passe incorrect")
  String badLoginOrPassword();

  @DefaultMessage("Mot de passe incorrect")
  String badPassword();

  @DefaultMessage("Problème d authentification")
  String badAuthentification();

  @DefaultMessage("Espaces")
  String navigationTitle();

  @DefaultMessage("Mes favoris")
  SafeHtml favorites();

  @DefaultMessage("Raccourcis")
  SafeHtml shortcuts();

  @DefaultMessage("Les dernières publications")
  SafeHtml lastPublications();

  @DefaultMessage("Prochains événements")
  SafeHtml lastEvents();

  @DefaultMessage("brouillon")
  String draft();

  @DefaultMessage("pas encore ou plus visible")
  String notVisible();

  @DefaultMessage("chargement...")
  String loading();

  @DefaultMessage("Média trop volumineux")
  String maxUploadError();

  @DefaultMessage("Format non pris en charge")
  String mediaNotSupportedError();
}
