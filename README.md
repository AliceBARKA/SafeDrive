SafeDrive – Application mobile de sécurité routière

SafeDrive est une application Android développée en Kotlin, dont l’objectif est d’améliorer la sécurité routière grâce à :

la détection de freinage brusque,

le signalement de dangers,

la consultation de la météo locale,

l’envoi automatique d’alertes à des contacts d’urgence.

Note : SafeDrive est un projet étudiant et n’est affilié à aucune organisation officielle de sécurité routière.

Fonctionnalités principales
1. Détection automatique d’incidents

Analyse de l’accéléromètre pour identifier un freinage brusque.

Affichage d’un écran d’alerte avec bouton « Je vais bien ».

Préparation à l’envoi d’une notification à un contact d’urgence.

2. Signalement de dangers

L’utilisateur peut signaler différents événements :

accidents

objets ou obstacles sur la route

bouchons

travaux

météo dangereuse

autres dangers rencontrés

3. Informations météo locales

Récupération des conditions météo via API.

Localisation GPS précise.

Affichage de la distance (ex : 3,2 km) et coordonnées.

Résumé météo + zone géographique.

4. Espace utilisateur

Création de compte

Connexion / déconnexion

Gestion du profil

Possibilité d’enregistrer deux contacts d’urgence

5. Interface simple et intuitive

Design moderne basé sur Material Design

Boutons larges adaptés à l’usage en conduite

Navigation fluide et minimaliste

Captures d’écran

Toutes les images sont redimensionnées pour éviter une surcharge visuelle.

<img src="https://github.com/user-attachments/assets/9eea9d2b-c767-4131-81bd-dd739a01fcce" width="300"/> <img src="https://github.com/user-attachments/assets/2a736f20-a840-4c68-adc1-f0f6f11cb1c3" width="300"/> <img src="https://github.com/user-attachments/assets/51527858-d8bb-405e-a232-10c13cacaee7" width="300"/> <img src="https://github.com/user-attachments/assets/f6e99279-35ea-49b0-bc68-7a9551b80614" width="300"/> <img src="https://github.com/user-attachments/assets/e87d2305-465e-4ea9-a520-f69df93c1638" width="300"/> <img src="https://github.com/user-attachments/assets/8e70ee9e-6bbd-44e6-89d4-261e5e7f9eaa" width="300"/> <img src="https://github.com/user-attachments/assets/86c5c510-dddc-44c2-b512-070dcb329bd0" width="300"/> <img src="https://github.com/user-attachments/assets/e1d63e40-d034-46e2-abb8-daea238253f2" width="300"/>
Modules de l’application
Module Authentification

Email + mot de passe

Vérification des champs

Stockage sécurisé via Firebase

Module Sécurité

Détection de décélération brusque

Blocage de l’interface en cas d’alerte

Option future : compte à rebours automatique

Module Météo

API externe

GPS FusedLocationProviderClient

Informations météo multiples autour de l’utilisateur

Module Danger

Formulaire intuitif

Récupération de la position

Transmission des informations

Technologies utilisées

Kotlin – Développement Android natif

FusedLocationProviderClient – Géolocalisation

API TomTom – Informations routières et POI

Firebase Authentication

Firebase Realtime Database / Firestore

Accéléromètre – Détection d’incidents

Material Design – Interface ergonomique

Installation & Exécution
Prérequis

Android Studio Flamingo ou supérieur

JDK 11+

Compte Firebase (optionnel selon modules activés)

Smartphone ou émulateur Android

Lancement du projet
git clone https://github.com/TON-UTILISATEUR/SafeDrive.git


Ouvrir Android Studio

File → Open → sélectionner le projet

Attendre la configuration Gradle

Exécuter l’application sur :

émulateur Android

ou smartphone en USB

Évolutions prévues

Appel automatique aux contacts d’urgence

Historique des événements détectés

Tracking des trajets et analyse de conduite

Navigation en temps réel

Mode spécialisé camionneurs

Notifications météo et danger
