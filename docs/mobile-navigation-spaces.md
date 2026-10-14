# Limiter les espaces dans la navigation mobile — ticket 15022

Dans `org/silverpeas/mobile/mobileSettings.properties` :

```properties
spaces.allowed=WA1,WA3
```

La navigation mobile ne propose que ces espaces et leurs descendants, à condition
qu’ils soient accessibles à l’utilisateur et contiennent une application prise
en charge par spmobile. Les applications de ces espaces restent soumises aux
contrôles existants, notamment leurs droits d’accès et leur visibilité.

La configuration par défaut conserve le comportement actuel :

```properties
spaces.allowed=
```

Les identifiants sont séparés par des virgules. Les espaces autour des valeurs,
les doublons, la casse du préfixe WA et les identifiants numériques équivalents
sont acceptés. Les sous-espaces n’ont pas besoin d’être énumérés.

Si un sous-espace est sélectionné sans son parent, il devient une racine dans la
navigation mobile. Si un parent et son descendant sont sélectionnés, la branche
n’est pas affichée deux fois. Un espace sélectionné mais inaccessible à
l’utilisateur n’est pas ajouté à sa navigation.

Un identifiant inconnu n’élargit pas la liste : une configuration non vide qui
ne correspond à aucun espace accessible produit une liste vide. Une modification
de la configuration doit être prise en compte côté serveur avant de recharger
l’application mobile, selon le mécanisme de configuration de votre installation.

Ce paramètre concerne l’arborescence de navigation commune aux pages d’accueil
et aux espaces. Il ne remplace pas les autorisations Silverpeas et n’est pas
une nouvelle restriction de sécurité sur les contenus, les URL directes,
la recherche, les favoris ou les raccourcis.

Dix tests automatisés couvrent la configuration vide, l’héritage, les espaces
sélectionnés, les sous-espaces sélectionnés comme racines, les doublons,
l’intersection avec les espaces accessibles, les identifiants invalides et
les hiérarchies cycliques. La compilation complète est vérifiée avec GWT en mode strict.
