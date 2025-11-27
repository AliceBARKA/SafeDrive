🚗 SAFEDRIVE – Application mobile de sécurité routière


SafeDrive est une application Android conçue pour améliorer la sécurité routière grâce à la détection de freinage brusque, le signalement de dangers, 
la consultation de la météo locale, ainsi que la transmission automatique d’alertes aux contacts d’urgence en cas d’incident.
![Drive safe  Stay aware  Stay alive](https://github.com/user-attachments/assets/c920d152-b2ea-4d56-95ab-f34383408c56)
Développée en Kotlin, SafeDrive met l’accent sur une interface simple, accessible et adaptée aux conducteurs.
   |Note : SafeDrive est un projet étudiant et n’est affilié à aucune structure officielle de sécurité routière.

Fonctionnalités principales
  Détection automatique d’incidents

Détection d’un freinage brusque grâce à l’accéléromètre du téléphone.

Affichage d’un écran d’alerte + bouton “Je vais bien”.

Préparation à l’envoi automatique d’une notification à un contact d’urgence.

  Signalement de dangers

Permet aux utilisateurs de signaler eux-mêmes :

accidents

objets sur la route

bouchons

travaux

météo dangereuse
… ou tout autre danger rencontré.

  Informations météo locales

Récupération et affichage des conditions météo proches de la position actuelle.

Calcul de distance (ex: “à 3,2 km”).

Informations détaillées : localisation, coordonnées GPS, description météo.

  Espace utilisateur complet

Création de compte

Connexion / déconnexion

Gestion du profil

Deux contacts d’urgence enregistrés au sein du compte

  Interface simple et intuitive

Design moderne, coloré et accessible

Boutons larges adaptés à l’utilisation en conduite (STOP / START / DANGER / TOMTOM)

Capture d'écran
![login png](https://github.com/user-attachments/assets/9eea9d2b-c767-4131-81bd-dd739a01fcce)
![popp_alerte png](https://github.com/user-attachments/assets/2a736f20-a840-4c68-adc1-f0f6f11cb1c3)
![detection png](https://github.com/user-attachments/asse![danger png](https://github.com/user-attachments/assets/f6e99279-35ea-49b0-bc68-7a9551b80614)
![danger png](https://github.com/user-attachments/assets/e87d2305-465e-4ea9-a520-f69df93c1638)
ts/51527858-d8bb-405e-a232-10c13cacaee7)
![home png](https://github.com/user-attachments/assets/8e70ee9e-6bbd-44e6-89d4-261e5e7f9eaa)
![signup png](https://github.com/user-attachments/assets/86c5c510-dddc-44c2-b512-070dcb329bd0)
![splash png](https://github.com/user-attachments/assets/e1d63e40-d034-46e2-abb8-daea238253f2)

Modules de l’application
  Authentification

Email + mot de passe

Vérifications des champs

Stockage des données utilisateur

  Module Sécurité

Sensibilité réglée pour détecter une décélération brusque

Blocage de l’interface jusqu’à confirmation

Activation d’un compte à rebours (option future)

  Module Météo

Récupération API

Localisation GPS

Affichage multi-points autour de l’utilisateur

  Module Danger

Formulaire de signalement

Récupération de la position

Envoi des informations

  
  Technologies utilisées
  
Kotlin – Développement Android natif

FusedLocationProviderClient – Récupération GPS précise

API TomTom – Informations routières et points d’intérêt

Firebase Authentication – Inscription / connexion utilisateur

Firebase Realtime Database (ou Firestore selon ton projet) – Stockage des alertes et profils

Capteurs du téléphone (accéléromètre) – Détection de freinage brusque

Material Design – Interface simple et ergonomique

Installation & Exécution (Développeurs)
🔧 Prérequis

Android Studio Flamingo ou supérieur

JDK 11+

Compte Firebase (si module activé)

Un smartphone ou émulateur Android

  Lancer le projet

Clone le repo

git clone https://github.com/TON-UTILISATEUR/SafeDrive.git


Ouvre Android Studio

File → Open → Sélectionne le dossier du projet

Laisse Gradle s’installer

Lance l’application
→ sur émulateur
→ ou téléphone en USB

  Fonctionnalités futures

-Appel automatique aux contacts d’urgence

-Historique des événements détectés

-Tracking des trajets + analyse de conduite

-Navigation intégrée en temps réel

-Mode camionneurs (longueur/hauteur/poids)

-Notifications push météo / danger



