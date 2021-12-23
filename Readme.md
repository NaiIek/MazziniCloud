# **Mazzini Cloud (nom à potentiellement changer)**

## **Projet**

Le projet est un simple serveur de stockage de fichiers. Je me base sur un modèle MVC (Model View Controller)

---
## **Technos**

### Front

Pour la face visible du site: 
1. **Freemarker** (templates html) qui permettent de générer une page unique pour chaque utilisateurs.
2. Du **CSS**, obligatoire pour une "UI" correcte sur navigateur
3. (Plus tard) Du **javascript** pour effectuer certains traitements d'affichage côté utilisateur (cf Specifications)

### Back

Pour le côté serveur:
1. **Java** pour les GUI, les DAO et globalement les traitements côté serveur.

2. Une **base de donnée H2** pour le stockage des données car facile à implémenter et elle prends les requêtes **SQL**.

3. **Spark** pour les différents endpoints.

4. **JWT** pour la gestion de l'authentification et des accès.

5. **Cipher** pour un encryptage basique et sale des password en BDD.

6. **Gson** pour sérialiser facilement des input en **Json** et désérialiser des output

---
## **Structure**

Le service web contient une gestion de différents utilisateurs, les informations demandées sont un pseudo, un email, un mot de passe, chaqu'un est lié à un id en base de donnée. Chaque utilisateur peut stocker des fichiers (types de fichiers acceptés selon une liste définie dans un ftl). 
Chaque fichier est stocké en base avec un id, l'id de son auteur/propriétaire, ses données sous forme blob, son nom.

---
## **Cahier des Charges / Specifications - [ ] Todo | [x] Done**

- [x] GUI: voir une page d'acceuil, d'explications, d'inscription, de connexion

- [x] Créer un utilisateur admin

- [x] Créer/Gérer des utilisateurs lambda

- [x] Bannir des utilisateurs depuis la page admin

- [x] GUI: voir son pseudo affiché lorsque l'on est connecté, voir un bouton de deconnexion

- [x] Endpoint pour upload des fichiers

- [x] GUI: voir les différents fichiers stockés qui nous appartiennent depuis la page d'acceuil

- [x] Stocker des fichiers en base de données

- [x] Endpoint pour supprimer des fichiers

- [x] Endpoint pour télécharger des fichiers

- [ ] Modification nom fichiers (max 260 caractères)

- [ ] Ajout de string "path"  (260 caractères) dans la BDD des fichiers pour gérer un affichage sous forme de dossier, sous dossier... en fonction de ce que souhaite l'utilisateur sur sa page HTML dans son navigateur.

- [ ] Gestion de l'espace de stockage global en fonction de l'infrastructure physique (bon courage)

- [ ] Encryptage de toutes les datas fichiers/noms fichier pour augmenter la sécurité.

- [ ] Refactoring-Optimisations (placer les méthodes aux bons endroits, réduire la redondance notamment)

- [ ] Pouvoir accéder aux fichiers utilisateurs depuis la page admin (uniquement à des fins de modération/gestion d'espace - ne pas mettre d'accès au contenu)

- [ ] Créer une API Rest pour upload, download des fichiers, créer des path sans passer par un navigateur

- [ ] Proposer une prévisualisation des images uploadées

- [ ] Proposer une prévisualisation des audios uploadés

- [ ] Proposer une lecture en streaming des vidéos uploadées

- [ ] Imaginer d'autres services à proposer

