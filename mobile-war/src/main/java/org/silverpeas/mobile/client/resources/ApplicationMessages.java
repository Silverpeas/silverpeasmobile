/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

package org.silverpeas.mobile.client.resources;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;


public interface ApplicationMessages extends Messages {

  @DefaultMessage("1. Configurer votre application")
  SafeHtml enrollmentStepSetup();

  @DefaultMessage("Copiez cette clé dans votre application d''authentification pour ajouter votre compte Silverpeas.")
  SafeHtml enrollmentSetupHelp();

  @DefaultMessage("Copier la clé")
  SafeHtml enrollmentCopyKey();

  @DefaultMessage("2. Vérifier le code")
  SafeHtml enrollmentStepVerify();

  @DefaultMessage("Une fois le compte ajouté, saisissez le code à 6 chiffres affiché dans votre application.")
  SafeHtml enrollmentVerifyHelp();

  @DefaultMessage("Activer la double authentification")
  SafeHtml enrollmentActivate();

  @DefaultMessage("Conservez ces codes de récupération en lieu sûr. Ils ne seront plus affichés.")
  SafeHtml enrollmentRecoveryHelp();

  @DefaultMessage("J''ai enregistré mes codes — Continuer")
  SafeHtml enrollmentContinue();

  @DefaultMessage("Chargement de la clé de configuration...")
  String enrollmentLoading();

  @DefaultMessage("Impossible de démarrer l''enrôlement. Reconnectez-vous.")
  String enrollmentStartError();

  @DefaultMessage("Clé copiée dans le presse-papiers.")
  String enrollmentCopied();

  @DefaultMessage("Copie impossible. Sélectionnez et copiez la clé manuellement.")
  String enrollmentCopyError();

  @DefaultMessage("Saisissez le code à 6 chiffres.")
  String enrollmentInvalidCode();

  @DefaultMessage("Vérification du code...")
  String enrollmentVerifying();

  @DefaultMessage("Code incorrect ou session expirée. Réessayez ou reconnectez-vous.")
  String enrollmentVerifyError();


  @DefaultMessage("Pour son fonctionnement, cette application utilise des Cookies. Les informations stockées sont uniquement techniques et ne contiennent pas de données personnelles, ou de suivi.")
  SafeHtml cookiesInformation();

  @DefaultMessage("Login")
  SafeHtml loginLabel();

  @DefaultMessage("Mot de passe")
  SafeHtml passwordLabel();

  @DefaultMessage("Code sécurité")
  SafeHtml codeLabel();

  @DefaultMessage("Faire confiance à cet appareil pendant 30 jours")
  String trustDevice();

  @DefaultMessage("Renvoyer un code")
  SafeHtml reSendSecurityCode();

  @DefaultMessage("Silverpeas &gt; Ecran de connexion")
  SafeHtml loginTitle();

  @DefaultMessage("Silverpeas &gt; Authentification à deux facteurs")
  SafeHtml twoFactorTitle();

  @DefaultMessage("Information")
  SafeHtml informationTitle();

  @DefaultMessage("Confirmer")
  String confirmBtnLabel();
  @DefaultMessage("Annuler")
  String cancelBtnLabel();
  @DefaultMessage("Corriger")
  String correctBtnLabel();

  @DefaultMessage("© 2001-2025 <a target='_blank' href='http://www.silverpeas.com'>Silverpeas</a>	- Tous droits réservés")
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
  SafeHtml tchat();

  @DefaultMessage("Rechercher")
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

  @DefaultMessage("Mes notifications")
  String notifications();

  @DefaultMessage("Déconnexion")
  String disconnect();

  @DefaultMessage("Espace personnel")
  String personalSpace();

  @DefaultMessage("Mes documents")
  String myDocuments();

  @DefaultMessage("Modifier mon statut")
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

  @DefaultMessage("Ressource non trouvée")
  String notfoundError();

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

  @DefaultMessage("Outils")
  SafeHtml shortcutstools();

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

  @DefaultMessage("Vous devez accepter pour accéder à l application")
  String userRefuseTermsOfService();

  @DefaultMessage("Accepter")
  SafeHtml accept();

  @DefaultMessage("Refuser")
  SafeHtml refuse();

  @DefaultMessage("Le mot de passe ne respecte pas les consignes de sécurité")
  String pwdNotValid();

  @DefaultMessage("Afficher le mot de passe")
  String showPwd();
  @DefaultMessage("Reçues")
  SafeHtml received();

  @DefaultMessage("Envoyées")
  SafeHtml sended();

  @DefaultMessage("Mes partages")
  String shares();

  @DefaultMessage("Partager")
  String share();

  @DefaultMessage("Autres favoris")
  String favoritesWithoutCategory();

  @DefaultMessage("Version")
  String version();

  @DefaultMessage("Oui")
  String yes();

  @DefaultMessage("Non")
  String no();

  @DefaultMessage("Installer l''application")
  String installation();
}
