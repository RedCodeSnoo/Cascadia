# 🌲 Cascadia - Digital Board Game (Java)

## 📝 Présentation du projet
Ce projet est une adaptation numérique du jeu de plateau **Cascadia**. Développé en **Java**, il permet à deux joueurs de s'affronter sur le terminal pour construire l'écosystème le plus harmonieux en combinant tuiles d'habitats et jetons animaux.

Le projet met l'accent sur une architecture logicielle robuste utilisant les principes de la **Programmation Orientée Objet (POO)**.

---

## 🛠️ Stack Technique
* **Langage :** Java (JDK 23+)
* **Concepts POO :** Encapsulation, Héritage, Records (JDK 23+), Classes Finales.
* **Gestion de données :** Utilisation intensive des collections Java (HashMap, ArrayList, List non-mutables).
* **Interface :** Terminal (CLI) avec rendu dynamique des tuiles et du plateau.

---

## 🏗️ Architecture du Projet
L'architecture est découpée en classes spécialisées pour respecter le principe de responsabilité unique :

* **Player & Habitat :** Gestion de l'état du joueur, de son plateau (ArrayList imbriquées) et du calcul des scores individuels.
* **Draw (Pioche) :** Logique de sélection des lots (tuiles + animaux) et gestion du renouvellement aléatoire.
* **Structure :** Moteur central stockant les ressources globales du jeu (pioche mutable, biomes et animaux constants).
* **Tile (Record) :** Utilisation des `record` Java pour une représentation immuable et légère des tuiles d'habitat.
* **CountPoint :** Module de calcul des points basé sur deux variantes (Famille et Intermédiaire), incluant les bonus de biomes majoritaires.

---

## 🎮 Fonctionnalités & Règles
* **Modes de jeu :** Support des variantes "Famille" et "Intermédiaire" pour le décompte des points.
* **Logique de placement :** Vérification des contraintes de proximité (tuiles adjacentes) et de compatibilité des habitats (faune autorisée).
* **Système de lot :** Gestion intelligente des lots identiques (possibilité de repiocher si 3 ou 4 lots sont similaires).
* **Système de points :** Décompte automatisé basé sur la proximité des familles d'animaux et la dominance des biomes (Meadow, River, Swamp, Sea, Mountain).

---

## 📖 Manuel d'utilisation
1. **Compilation :** `javac *.java`
2. **Exécution :** `java Main`
3. **Commandes :** Suivre les instructions textuelles pour choisir un lot et entrer les coordonnées (nombres entiers) pour placer vos éléments.
