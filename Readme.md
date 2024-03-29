# **Mazzland**

## **Projet**

Le projet est un simple serveur de stockage de fichiers. Je me base sur un modèle MVC (Model View Controller).

---
## **Démarrage**

Dans un bash Linux, executer `./gradlew run`  
Dans un cmd Windows, executer `gradlew run`  
**Attention:** lancer le programme depuis WSL fonctionnera mais le port ne sera pas ouvert de
l'exterieur (seul le https localhost fonctionnera)

---
## **Setup https**

Pour mettre en place le https, j'ai suivi ce [tutoriel](https://srimalfernando.medium.com/ssl-for-java-spark-framework-on-jetty-deployed-in-a-docker-container-eda93657d13) 
qui s'adapte bien a la config, j'ai stocké mes certificats dans un dossier deploy à la racine
qui n'est pas présent sur ce git pour insister sur le fait que chacun doit utiliser ses propres
certificats et que vous ne pourrez de toute façon pas utiliser mes certificats.

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

5. **Cipher** pour un encryptage basique et save des password en BDD.

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

- [x] Modification nom fichiers (max 260 caractères)

- [x] Ajout de string "path"  (260 caractères) dans la BDD des fichiers pour gérer un affichage sous forme de dossier, sous dossier... en fonction de ce que souhaite l'utilisateur sur sa page HTML dans son navigateur.

- [x] Faire un endpoint desinscription -> delete de toutes les données liées à l'utilisateur

- [x] Faire des logs basique serveur (connexion/deconnection/inscription/upload/filesize/download)

- [ ] Fonctionnalité CRUD sur un "dossier" (path en BDD)

- [x] Ajout d'un int size dans la BDD fichiers pour stocker le nombre d'octets approximatif du tuple (fichier+id+authorid+name+path)

- [x] Renommer un fichier stocké

- [ ] Ajout d'une option pour vérifier si un utilisateur upload deux fois le même fichier.

- [x] Vérifier automatiquement l'extansion d'un fichier que l'on renomme

- [ ] Gestion de l'espace de stockage global et par utilisateur en fonction de l'infrastructure physique (bon courage)

- [ ] Proposer un onglet profil customisable.

- [ ] Encryptage de toutes les datas fichiers/noms fichier pour augmenter la sécurité.

- [ ] Refactoring-Optimisations (placer les méthodes aux bons endroits, réduire la redondance notamment) - techniquement infini

- [ ] Pouvoir accéder aux fichiers utilisateurs depuis la page admin (uniquement à des fins de modération/gestion d'espace - ne pas mettre d'accès au contenu)

- [ ] Créer une API Rest pour upload, download des fichiers, créer des path sans passer par un navigateur

- [ ] UI : Proposer une prévisualisation des images uploadées

- [ ] UI : Proposer une prévisualisation des audios uploadés

- [ ] UI : Proposer une lecture en streaming des vidéos uploadées

- [ ] Imaginer d'autres services à proposer

