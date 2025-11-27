# SafeDrive – Application mobile de sécurité routière

_Appli Android pour détecter les incidents, signaler les dangers et alerter vos contacts d’urgence._

![Drive safe – Stay aware – Stay alive](https://github.com/user-attachments/assets/883c38bf-7ee8-46d5-b00d-2426a5bcc9c1)

SafeDrive est une application Android développée en **Kotlin** dont l’objectif est d’améliorer la sécurité routière grâce à :
- la détection de freinage brusque ;
- le signalement de dangers ;
- la consultation de la météo locale ;
- l’envoi automatique d’alertes à des contacts d’urgence.

> **Note**  
> SafeDrive est un projet étudiant et n’est affilié à aucune organisation officielle de sécurité routière.

---

## Sommaire

- [Fonctionnalités principales](#fonctionnalités-principales)
- [Captures d’écran](#captures-décran)
- [Modules de l’application](#modules-de-lapplication)
- [Technologies utilisées](#technologies-utilisées)
- [Installation & exécution](#installation--exécution)
- [Évolutions prévues](#évolutions-prévues)

---

## Fonctionnalités principales

### 1. Détection automatique d’incidents
- Analyse de l’**accéléromètre** pour identifier un freinage brusque.  
- Affichage d’un **écran d’alerte** avec bouton « Je vais bien ».  
- Préparation à l’envoi d’une notification aux **contacts d’urgence**.

### 2. Signalement de dangers
L’utilisateur peut signaler différents événements :
- accidents ;
- objets ou obstacles sur la route ;
- bouchons ;
- travaux ;
- météo dangereuse ;
- autres dangers rencontrés.

### 3. Informations météo locales
- Récupération des conditions météo via **API externe**.  
- Localisation **GPS précise**.  
- Affichage de la distance (ex. `3,2 km`) et des coordonnées.  
- Résumé météo + zone géographique.

### 4. Espace utilisateur
- Création de compte ;  
- Connexion / déconnexion ;  
- Gestion du profil ;  
- Enregistrement de **deux contacts d’urgence**.

### 5. Interface simple et intuitive
- Design moderne basé sur **Material Design** ;  
- Boutons larges adaptés à l’usage en conduite ;  
- Navigation fluide et minimaliste.

---

## Captures d’écran

_Toutes les images sont redimensionnées pour limiter l’espace occupé._

<p align="center">
  <img src="https://github.com/user-attachments/assets/9eea9d2b-c767-4131-81bd-dd739a01fcce" width="250"/>
  <img src="https://github.com/user-attachments/assets/2a736f20-a840-4c68-adc1-f0f6f11cb1c3" width="250"/>
  <img src="https://github.com/user-attachments/assets/51527858-d8bb-405e-a232-10c13cacaee7" width="250"/>
  <br/>
  <img src="https://github.com/user-attachments/assets/f6e99279-35ea-49b0-bc68-7a9551b80614" width="250"/>
  <img src="https://github.com/user-attachments/assets/8e70ee9e-6bbd-44e6-89d4-261e5e7f9eaa" width="250"/>
  <img src="https://github.com/user-attachments/assets/86c5c510-dddc-44c2-b512-070dcb329bd0" width="250"/>
  <br/>
  <img src="https://github.com/user-attachments/assets/e1d63e40-d034-46e2-abb8-daea238253f2" width="250"/>
</p>

---

## Modules de l’application

### Module Authentification
- Connexion par **email + mot de passe** ;  
- Vérification des champs ;  
- Stockage sécurisé via **Firebase**.

### Module Sécurité
- Détection de **décélérations brusques** ;  
- Blocage de l’interface en cas d’alerte ;  
- Option future : **compte à rebours automatique** avant alerte.

### Module Météo
- Récupération des données météo via **API externe** ;  
- Localisation avec **FusedLocationProviderClient** ;  
- Affichage des informations météo autour de l’utilisateur.

### Module Danger
- Formulaire de signalement simple ;  
- Récupération de la position GPS ;  
- Transmission des informations liées au danger.

---

## Technologies utilisées

- **Kotlin** – Développement Android natif  
- **FusedLocationProviderClient** – Géolocalisation  
- **API TomTom** – Informations routières et points d’intérêt  
- **Firebase Authentication** – Authentification utilisateur  
- **Firebase Realtime Database / Firestore** – Stockage des données  
- **Accéléromètre** – Détection de freinage brusque  
- **Material Design** – Interface ergonomique

---

## Installation & exécution

### Prérequis

- Android Studio **Flamingo** ou supérieur  
- **JDK 11+**  
- Compte **Firebase** (selon les modules activés)  
- Smartphone Android ou émulateur

### Lancement du projet 

- Ouvrir Android Studio
- file → Open puis sélectionner le dossier du projet
- Attendre la fin de la configuration Gradle

### Exécuter l’application

- sur un émulateur Android, ou
- sur un smartphone connecté en USB.

### Évolutions prévues
- Appel automatique aux contacts d’urgence ;
- Historique des événements détectés ;
- Tracking des trajets et analyse de conduite ;
- Navigation en temps réel ;
- Mode spécialisé camionneurs ;
- Notifications push météo et danger.

### bash
git clone https://github.com/TON-UTILISATEUR/SafeDrive.git
