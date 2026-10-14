# Présentations de la page d’accueil

Le mode liste existant reste le mode par défaut. La liste des actualités n’est pas concernée.

## Activer la surcouche fournie

Dans `mobileSettings.properties` :

```properties
styleSheet=/silverpeas/spmobile/homepage-cards.css
```

Cette surcouche active les cartes pour les espaces et les raccourcis. Les favoris,
les outils, les dernières publications et les événements restent en liste.

Si une surcouche est déjà configurée, conservez son adresse dans `styleSheet` et
ajoutez cette ligne au début de ce fichier, avant les autres règles CSS :

```css
@import url("/silverpeas/spmobile/homepage-cards.css");
```

## Choisir les zones séparément

Pour une autre combinaison, copiez le contenu de `homepage-cards.css` dans votre
surcouche et remplacez, dans **tous** les sélecteurs `:is(...)`, la liste des zones.
N’importez pas également la version standard, qui activerait ses propres zones.

| Zone | Sélecteur |
| --- | --- |
| Espaces et applications | `#homepage-spaces` |
| Raccourcis | `#homepage-shortcuts` |
| Raccourcis outils | `#homepage-tools` |
| Favoris | `#homepage-favorites` |
| Dernières publications | `#homepage-publications` |
| Derniers événements | `#homepage-events` |

Par exemple, pour les raccourcis et les favoris seulement, remplacez partout :

```css
:is(#homepage-spaces, #homepage-shortcuts)
```

par :

```css
:is(#homepage-shortcuts, #homepage-favorites)
```

Les zones absentes de ces sélecteurs restent en liste. Aucun réglage Java ni
changement du composant `UnorderedList` n’est nécessaire.

## Icônes

Les images configurées pour les raccourcis et les SVG des applications/favoris sont
réutilisés. Un emoji situé au début du nom d’un espace est placé au-dessus de son
libellé en mode cartes, et reste à côté du libellé en mode liste. Le nom stocké n’est
pas modifié. Un espace sans icône conserve la place de l’icône en mode cartes.

Une icône peut être définie pour un espace par son identifiant, déjà présent sur
le `li`. Remplacez `123` par l’identifiant réel de l’espace :

```css
#homepage-spaces > li[id="123"] {
  --homepage-card-icon: "🏛️";
  --homepage-card-native-icon-display: none;
}
```

Une image peut être utilisée à la place :

```css
#homepage-spaces > li[id="123"] {
  --homepage-card-icon-image: url("/silverpeas/images/mon-icone.svg");
  --homepage-card-native-icon-display: none;
}
```

Les raccourcis conservent leurs identifiants `shortCut1`, `shortCut2`, etc.
Les raccourcis outils conservent `shortCutTools1`, `shortCutTools2`, etc.

## Ajuster les cartes

Les paramètres peuvent être appliqués à une seule zone ou à tout `.space-content` :

```css
#homepage-shortcuts {
  --homepage-card-gap: 8px;
  --homepage-card-radius: 8px;
  --homepage-card-background: #fff;
  --homepage-card-color: #292929;
  --homepage-card-min-height: 96px;
}
```

Ces variables modifient les cartes déjà activées par les sélecteurs ; elles
n’activent pas à elles seules le mode cartes.
Les cartes sont réparties sur deux colonnes ; les noms longs passent à la ligne.
En dessous de 280 px, la grille passe à une colonne.

## Validation

Le contrôle de rendu utilise une page de démonstration avec la structure des
widgets et les styles du dépôt. Les règles CSS, les libellés longs et les modes
mixtes ont été vérifiés à 320 et 390 px. La compilation complète de l’application
et la validation dans une instance Silverpeas restent à faire : les dépendances
Silverpeas nécessaires ne sont pas disponibles localement.
