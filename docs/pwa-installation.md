# Installation PWA de spmobile

Le menu contient une action permanente « Installer l’application ». Elle reste
accessible après un refus de la proposition ponctuelle. L’action est masquée
lorsque spmobile s’exécute en mode installé ou après l’événement `appinstalled`.
L’absence de `beforeinstallprompt` ne signifie plus que l’application est installée.

## Parcours

- Si le navigateur fournit une proposition native, l’action l’ouvre directement
  depuis le clic utilisateur, une seule fois par événement.
- Sur iPhone/iPad dans Safari : instructions Partager → Sur l’écran d’accueil → Ajouter.
- Sur iPhone/iPad dans un autre navigateur : invitation à ouvrir la même adresse dans Safari.
- Autres navigateurs sans proposition native : instructions pour le menu Installer
  l’application / Ajouter à l’écran d’accueil, avec distinction possible entre
  un raccourci et une installation complète.
- En contexte non sécurisé : invitation à utiliser HTTPS avec un certificat reconnu.

Le navigateur reste responsable de l’installation et de ses critères d’éligibilité.
Ce code ne force pas l’apparition d’une fenêtre non prise en charge par le navigateur.
Depuis un onglet normal, la détection d’une installation préexistante n’est pas
universelle ; la détection du mode installé est distincte de l’éligibilité.

## Proposition ponctuelle

`pwa-install.js` est chargé avant GWT et conserve un éventuel événement précoce.
La proposition spmobile est déclenchée à l’arrivée de l’événement, sans délai
fixe d’une seconde, et au maximum une fois par chargement de l’application.
Le cookie existant `ask_install` et `displayInstallQuestionFrequency` restent
respectés pour cette proposition ponctuelle, sans masquer l’action du menu.

Le manifest par défaut inclut maintenant une icône 192 px en SVG, contenant
l’image PNG 512 px existante, et conserve l’icône PNG 512 px. Aucune modification
visuelle de l’icône existante. Les manifests personnalisés par configuration
restent à vérifier séparément.

## Vérifications

Depuis la racine du dépôt, lancer un serveur HTTP local puis ouvrir
`/tests/pwa-install.html`. Pour tester une application déployée, copier les deux
fichiers `tests/pwa-install.html` et `tests/pwa-install.test.js` dans un dossier
`tests` à côté de `spmobile`, puis ouvrir `/silverpeas/tests/pwa-install.html`
après connexion. Le test détecte automatiquement les deux organisations et
charge les ressources déployées ; il ne nécessite pas les sources Java sur le serveur.
Une réponse HTML (erreur ou page de connexion) est signalée explicitement. Les treize contrôles portent sur les événements
précoces/tardifs, la consommation unique de la proposition native, les erreurs,
les instructions de secours, les modes installés, les abonnements du menu,
les navigateurs iPhone/iPad/Android et le chargement des icônes du manifest.

Le helper, les messages, les ressources et le vrai XML du menu ont été compilés
avec GWT 2.12.2 dans un module isolé. La compilation complète du projet et les
essais d’installation sur appareils réels restent à effectuer.
