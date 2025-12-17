# 📚 Projet CORBA - Système de Gestion d'Étudiants et Promotions

## 📋 Table des Matières

- [Vue d'ensemble](#-vue-densemble)
- [Problématique](#-problématique)
- [Solution CORBA](#-solution-corba)
- [Architecture du Projet](#-architecture-du-projet)
- [Prérequis](#-prérequis)
- [Installation et Configuration](#-installation-et-configuration)
- [Interfaces IDL](#-interfaces-idl)
- [Implémentation](#-implémentation)
- [Compilation et Exécution](#-compilation-et-exécution)
- [Tests et Résultats](#-tests-et-résultats)
- [Comparaison des Technologies](#-comparaison-des-technologies)
- [Troubleshooting](#-troubleshooting)
- [Références](#-références)

---

## 🎯 Vue d'ensemble

Ce projet implémente un **système distribué de gestion d'étudiants et de promotions** utilisant la technologie **CORBA (Common Object Request Broker Architecture)**. Il permet de gérer les épreuves des étudiants, calculer leurs moyennes, et obtenir des statistiques sur les promotions à travers un réseau.

### Fonctionnalités Principales

#### Interface Étudiant
- ✅ Ajouter une épreuve à un étudiant
- ✅ Lister toutes les épreuves d'un étudiant
- ✅ Calculer la moyenne pondérée d'un étudiant
- ✅ Obtenir le classement des 10 meilleurs étudiants

#### Interface Promotion
- ✅ Créer un nouvel étudiant dans la promotion
- ✅ Rechercher un étudiant existant
- ✅ Calculer le ratio de réussite de la promotion
- ✅ Obtenir la moyenne générale de la promotion

---

## 🔍 Problématique

### Le Défi de l'Architecture Distribuée

Dans un système éducatif moderne, plusieurs besoins se présentent :

#### **1. Distribution Géographique**
```
🏢 Campus Principal (Paris)
   └─ Serveur de gestion des étudiants
   
🏢 Campus Secondaire (Lyon)
   └─ Besoin d'accéder aux données des étudiants
   
🏢 Administration (Marseille)
   └─ Besoin de générer des rapports
```

**Problème** : Comment permettre à ces différents sites d'accéder aux mêmes données de manière transparente ?

#### **2. Hétérogénéité Technologique**

Différents systèmes utilisent différentes technologies :
```
Application Web (Java)     →  Besoin d'accéder aux données
Application Desktop (C++)  →  Besoin d'accéder aux données
Service Mobile (Python)    →  Besoin d'accéder aux données
```

**Problème** : Comment faire communiquer ces applications écrites dans différents langages ?

#### **3. Centralisation vs Distribution**

##### ❌ Architecture Centralisée Classique
```
┌─────────────────────────────────────────┐
│         Base de Données Centrale        │
│                                         │
│  Tous les clients accèdent directement │
│  à la base de données                  │
└─────────────────────────────────────────┘
         ↑        ↑        ↑        ↑
         │        │        │        │
    Client1  Client2  Client3  Client4
```

**Problèmes** :
- 🔴 Couplage fort entre clients et base de données
- 🔴 Logique métier dupliquée dans chaque client
- 🔴 Difficile de changer la structure de la BD
- 🔴 Pas de contrôle d'accès centralisé
- 🔴 Performances limitées (goulot d'étranglement)

##### ✅ Architecture Distribuée avec CORBA
```
┌──────────────┐         ┌──────────────┐
│   Client 1   │         │   Client 2   │
│    (Java)    │         │    (C++)     │
└──────┬───────┘         └──────┬───────┘
       │                        │
       └────────┬───────────────┘
                │
         ┌──────▼──────┐
         │    ORBD     │ ← Service de Nommage
         │ (NameService)│
         └──────┬──────┘
                │
    ┌───────────┴───────────┐
    │                       │
┌───▼────────┐      ┌──────▼──────┐
│  Serveur   │      │   Serveur   │
│  Étudiant  │      │  Promotion  │
└────┬───────┘      └──────┬──────┘
     │                     │
     └──────────┬──────────┘
                │
        ┌───────▼────────┐
        │ Base de Données│
        └────────────────┘
```

**Avantages** :
- ✅ Logique métier centralisée dans les serveurs
- ✅ Clients légers et simples
- ✅ Interopérabilité multi-langages
- ✅ Sécurité et contrôle d'accès centralisés
- ✅ Évolutivité (ajout de serveurs)
- ✅ Maintenance facilitée

#### **4. Cas d'Usage Concret**

**Scénario** : Un professeur à Lyon veut consulter les notes d'un étudiant dont les données sont sur le serveur de Paris.

##### Sans CORBA (Approche Traditionnelle)
```
1. Professeur → Appel HTTP/REST → Serveur Paris
2. Serveur Paris → Requête SQL → Base de données
3. Base de données → Résultat → Serveur Paris
4. Serveur Paris → JSON → Professeur
5. Professeur → Parse JSON → Affichage

❌ Complexité : Gestion manuelle du protocole, sérialisation, etc.
```

##### Avec CORBA
```
1. Professeur → etudiantRef.calculerLaMoyenne("Alice")
2. CORBA gère automatiquement :
   - Sérialisation des paramètres
   - Transmission réseau
   - Désérialisation
   - Invocation côté serveur
   - Retour du résultat
3. Professeur reçoit : float moyenne = 16.5

✅ Simplicité : Appel de méthode transparent comme si l'objet était local
```

---

## 💡 Solution CORBA

### Qu'est-ce que CORBA ?

**CORBA (Common Object Request Broker Architecture)** est un standard de **middleware** qui permet à des objets distribués de communiquer entre eux, indépendamment de leur langage de programmation ou de leur emplacement sur le réseau.

### Composants Clés de CORBA

#### 1. **IDL (Interface Definition Language)**
Un langage **neutre et indépendant** pour définir les interfaces des objets distribués.

```idl
// Définition lisible par tous les langages
interface Etudiant {
    float calculerLaMoyenne(in string nomEtudiant);
};
```

#### 2. **ORB (Object Request Broker)**
Le "courtier" qui gère la communication entre clients et serveurs.

```
Client → ORB → Réseau → ORB → Serveur
```

#### 3. **ORBD (ORB Daemon / NameService)**
Un **annuaire téléphonique** pour les objets distribués.

```
Serveur enregistre : "Je suis EtudiantService à cette adresse"
Client demande : "Où est EtudiantService ?"
ORBD répond : "Le voici !"
```

#### 4. **POA (Portable Object Adapter)**
Gère le cycle de vie des objets côté serveur.

#### 5. **Stubs et Skeletons**
- **Stub (Client)** : Proxy local qui représente l'objet distant
- **Skeleton (Serveur)** : Récepteur qui délègue les appels à l'implémentation réelle

### Workflow CORBA

```
┌─────────────────────────────────────────────────────────────┐
│                    1. Définition IDL                        │
│  interface Etudiant { float calculerMoyenne(...); }         │
└────────────────────┬────────────────────────────────────────┘
                     │
         ┌───────────▼────────────┐
         │  2. Compilation (idlj) │
         └───────────┬────────────┘
                     │
        ┌────────────┴─────────────┐
        │                          │
┌───────▼────────┐        ┌────────▼─────────┐
│  Stub (Client) │        │ Skeleton (Server)│
└───────┬────────┘        └────────┬─────────┘
        │                          │
┌───────▼────────┐        ┌────────▼─────────┐
│  3. CLIENT     │        │  3. SERVEUR      │
│                │        │                  │
│  etudiantRef.  │        │  class           │
│  calculer...() │◄──────►│  EtudiantImpl    │
└────────────────┘  CORBA └──────────────────┘
```

---

## 🏗️ Architecture du Projet

### Structure des Dossiers

```
CorbaProject/
│
├── README.md                    # Ce fichier
│
├── src/
│   ├── idl/                     # Définitions IDL
│   │   ├── Etudiant.idl
│   │   └── Promotion.idl
│   │
│   ├── EtudiantModule/          # Généré par idlj
│   │   ├── Etudiant.java
│   │   ├── EtudiantHelper.java
│   │   ├── EtudiantHolder.java
│   │   ├── EtudiantPOA.java
│   │   ├── Epreuve.java
│   │   ├── EtudiantInfo.java
│   │   └── ...
│   │
│   ├── PromotionModule/         # Généré par idlj
│   │   ├── Promotion.java
│   │   ├── PromotionHelper.java
│   │   ├── PromotionPOA.java
│   │   └── ...
│   │
│   ├── model/                   # Classes métier
│   │   ├── Epreuve.java
│   │   └── EtudiantData.java
│   │
│   ├── server/                  # Implémentation serveur
│   │   ├── EtudiantImpl.java
│   │   ├── PromotionImpl.java
│   │   └── Server.java
│   │
│   └── client/                  # Applications clientes
│       ├── ClientEtudiant.java
│       └── ClientPromotion.java
│
└── out/                         # Fichiers compilés
```

### Diagramme de Classes

```
┌────────────────────────────────────────────────────────────┐
│                       IDL Layer                            │
├────────────────────────────────────────────────────────────┤
│  <<interface>>              <<interface>>                  │
│  Etudiant                   Promotion                      │
│  ─────────────              ──────────                     │
│  + ajouterUneEpreuve()      + creerEtudiant()             │
│  + listeDesEpreuves()       + rechercherUnEtudiant()      │
│  + calculerLaMoyenne()      + calculerRatioReussite()     │
│  + listerLes10Premiers()    + obtenirMoyenneGenerale()    │
└────────────────────────────────────────────────────────────┘
                      │                   │
                      │                   │
        ┌─────────────▼─────────┐  ┌──────▼──────────────┐
        │   EtudiantImpl        │  │  PromotionImpl      │
        │   extends EtudiantPOA │  │  extends PromotionPOA│
        └───────────┬───────────┘  └──────┬──────────────┘
                    │                     │
                    │   uses              │   uses
                    ▼                     ▼
        ┌─────────────────────┐  ┌─────────────────────┐
        │  EtudiantData       │  │  EtudiantData       │
        │  ─────────────       │  │  ─────────────       │
        │  - nom: String      │  │  (same)             │
        │  - epreuves: List   │  │                     │
        │  + calculerMoyenne()│  │                     │
        └──────────┬──────────┘  └─────────────────────┘
                   │
                   │   contains
                   ▼
        ┌─────────────────────┐
        │  Epreuve            │
        │  ────────            │
        │  - nom: String      │
        │  - note: float      │
        │  - coefficient: int │
        └─────────────────────┘
```

---

## 🔧 Prérequis

### Logiciels Requis

| Logiciel | Version Minimale | Rôle |
|----------|------------------|------|
| **JDK** | 8 ou supérieur | Compilation et exécution Java |
| **IntelliJ IDEA** | 2020+ | IDE de développement |
| **idlj** | Inclus avec JDK | Compilateur IDL vers Java |

### Vérification de l'Installation

```bash
# Vérifier Java
java -version
# Sortie attendue : java version "1.8.0_xxx" ou supérieur

# Vérifier le compilateur Java
javac -version
# Sortie attendue : javac 1.8.0_xxx

# Vérifier idlj
idlj -version
# Sortie attendue : idlj version "xxx"

# Si idlj n'est pas trouvé, utilisez le chemin complet
# Windows
"C:\Program Files\Java\jdk1.8.0_xxx\bin\idlj.exe"

# Linux/Mac
/usr/lib/jvm/java-8-openjdk/bin/idlj
```

---

## 📥 Installation et Configuration

### Étape 1 : Créer le Projet dans IntelliJ

1. **Ouvrir IntelliJ IDEA**
2. **File → New → Project**
3. Sélectionner **"Java"**
4. Nommer le projet : `CorbaProject`
5. Cliquer **"Create"**

### Étape 2 : Créer la Structure des Dossiers

Dans la vue **Project**, créer les dossiers suivants :

```bash
src/
├── idl/
├── model/
├── server/
└── client/
```

**Comment** :
- Right-click sur `src` → New → Package
- Créer : `model`, `server`, `client`
- Right-click sur `src` → New → Directory
- Créer : `idl`

### Étape 3 : Créer les Fichiers IDL

#### **src/idl/Etudiant.idl**

```idl
module EtudiantModule {
    
    struct Epreuve {
        string nom;
        float note;
        long coefficient;
    };
    
    typedef sequence<Epreuve> ListeEpreuves;
    
    struct EtudiantInfo {
        string nom;
        float moyenne;
    };
    
    typedef sequence<EtudiantInfo> ListeEtudiants;
    
    interface Etudiant {
        boolean ajouterUneEpreuve(in string nomEtudiant, in Epreuve epreuve);
        ListeEpreuves listeDesEpreuves(in string nomEtudiant);
        float calculerLaMoyenne(in string nomEtudiant);
        ListeEtudiants listerLes10Premiers();
    };
};
```

#### **src/idl/Promotion.idl**

```idl
module PromotionModule {
    
    struct Epreuve {
        string nom;
        float note;
        long coefficient;
    };
    
    typedef sequence<Epreuve> ListeEpreuves;
    
    struct EtudiantInfo {
        string nom;
        float moyenne;
    };
    
    typedef sequence<EtudiantInfo> ListeEtudiants;
    
    interface Promotion {
        boolean creerEtudiant(in string nomEtudiant);
        boolean rechercherUnEtudiant(in string nomEtudiant);
        float calculerRatioReussite();
        float obtenirMoyenneGenerale();
    };
};
```

---

## 🔨 Interfaces IDL

### Explication Détaillée

#### Types de Données IDL

| Type IDL | Type Java | Description |
|----------|-----------|-------------|
| `string` | `String` | Chaîne de caractères |
| `float` | `float` | Nombre à virgule flottante |
| `long` | `int` | Entier 32 bits |
| `boolean` | `boolean` | Booléen |
| `sequence<T>` | `T[]` | Tableau dynamique |

#### Structure `Epreuve`

```idl
struct Epreuve {
    string nom;          // Nom de l'épreuve (ex: "Mathématiques")
    float note;          // Note sur 20
    long coefficient;    // Coefficient de l'épreuve
};
```

**Correspondance Java** :
```java
public class Epreuve {
    public String nom;
    public float note;
    public int coefficient;
}
```

#### Paramètres des Méthodes

| Modificateur | Signification | Équivalent Java |
|--------------|---------------|-----------------|
| `in` | Paramètre d'entrée | Passage par valeur |
| `out` | Paramètre de sortie | Passage par référence |
| `inout` | Entrée/Sortie | Passage par référence |

**Exemple** :
```idl
// IDL
boolean ajouterUneEpreuve(in string nomEtudiant, in Epreuve epreuve);

// Java généré
boolean ajouterUneEpreuve(String nomEtudiant, Epreuve epreuve);
```

---

## 🛠️ Implémentation

### Classes Métier

#### **model/Epreuve.java**

Représente une épreuve avec calcul automatique.

```java
package model;

public class Epreuve {
    private String nom;
    private float note;
    private int coefficient;
    
    // Constructeur, getters, setters...
    
    public float getNoteCoefficiee() {
        return note * coefficient;
    }
}
```

#### **model/EtudiantData.java**

Gère un étudiant et ses épreuves.

```java
package model;

import java.util.ArrayList;
import java.util.List;

public class EtudiantData {
    private String nom;
    private List<Epreuve> epreuves;
    
    public float calculerMoyenne() {
        if (epreuves.isEmpty()) return 0.0f;
        
        float sommeNotesPonderees = 0.0f;
        int sommeCoefficients = 0;
        
        for (Epreuve e : epreuves) {
            sommeNotesPonderees += e.getNote() * e.getCoefficient();
            sommeCoefficients += e.getCoefficient();
        }
        
        return sommeCoefficients > 0 ? 
               sommeNotesPonderees / sommeCoefficients : 0.0f;
    }
}
```

**Exemple de calcul** :
```
Épreuves :
- Math: 16/20, coefficient 3  → 16 × 3 = 48
- Info: 18/20, coefficient 4  → 18 × 4 = 72
                                 ────────
                          Total: 120 / 7 = 17.14
```

### Implémentation Serveur

#### **server/EtudiantImpl.java**

Implémente l'interface `EtudiantPOA` générée par idlj.

```java
package server;

import EtudiantModule.*;
import model.EtudiantData;
import java.util.*;

public class EtudiantImpl extends EtudiantPOA {
    
    private Map<String, EtudiantData> baseDonneesEtudiants;
    
    @Override
    public boolean ajouterUneEpreuve(String nomEtudiant, Epreuve epreuve) {
        // Créer l'étudiant s'il n'existe pas
        if (!baseDonneesEtudiants.containsKey(nomEtudiant)) {
            baseDonneesEtudiants.put(nomEtudiant, new EtudiantData(nomEtudiant));
        }
        
        // Ajouter l'épreuve
        EtudiantData etudiant = baseDonneesEtudiants.get(nomEtudiant);
        etudiant.ajouterEpreuve(new model.Epreuve(
            epreuve.nom, epreuve.note, epreuve.coefficient
        ));
        
        return true;
    }
    
    @Override
    public float calculerLaMoyenne(String nomEtudiant) {
        EtudiantData etudiant = baseDonneesEtudiants.get(nomEtudiant);
        return etudiant != null ? etudiant.calculerMoyenne() : 0.0f;
    }
    
    // Autres méthodes...
}
```

#### **server/Server.java**

Lance le serveur CORBA et enregistre les services.

```java
package server;

import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.PortableServer.*;

public class Server {
    public static void main(String[] args) {
        try {
            // 1. Initialiser l'ORB
            ORB orb = ORB.init(args, null);
            
            // 2. Activer le POA
            POA rootPOA = POAHelper.narrow(
                orb.resolve_initial_references("RootPOA")
            );
            rootPOA.the_POAManager().activate();
            
            // 3. Créer les servants
            EtudiantImpl etudiantImpl = new EtudiantImpl();
            PromotionImpl promotionImpl = new PromotionImpl();
            
            // 4. Obtenir les références CORBA
            org.omg.CORBA.Object refEtudiant = 
                rootPOA.servant_to_reference(etudiantImpl);
            Etudiant etudiantRef = EtudiantHelper.narrow(refEtudiant);
            
            // 5. Enregistrer dans le NameService
            org.omg.CORBA.Object objRef = 
                orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            
            ncRef.rebind(ncRef.to_name("EtudiantService"), etudiantRef);
            
            System.out.println("✓ Serveur prêt");
            
            // 6. Attendre les requêtes
            orb.run();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### Implémentation Client

#### **client/ClientEtudiant.java**

Utilise le service distant.

```java
package client;

import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import EtudiantModule.*;

public class ClientEtudiant {
    public static void main(String[] args) {
        try {
            // 1. Initialiser l'ORB
            ORB orb = ORB.init(args, null);
            
            // 2. Obtenir le NameService
            org.omg.CORBA.Object objRef = 
                orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            
            // 3. Résoudre la référence du service
            Etudiant etudiantRef = EtudiantHelper.narrow(
                ncRef.resolve_str("EtudiantService")
            );
            
            // 4. Utiliser le service comme un objet local !
            Epreuve epreuve = new Epreuve("Math", 16.5f, 3);
            etudiantRef.ajouterUneEpreuve("Alice", epreuve);
            
            float moyenne = etudiantRef.calculerLaMoyenne("Alice");
            System.out.println("Moyenne : " + moyenne);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

## ⚙️ Compilation et Exécution

### Phase 1 : Compiler les Fichiers IDL

```bash
# Terminal IntelliJ (Alt+F12)
cd src/idl

# Compiler Etudiant.idl
idlj -fall Etudiant.idl

# Compiler Promotion.idl
idlj -fall Promotion.idl

# Déplacer les modules générés
mv EtudiantModule ../
mv PromotionModule ../
```

**Résultat** : Création de `EtudiantModule/` et `PromotionModule/` avec les fichiers Java générés.

### Phase 2 : Compiler le Code Java

Dans IntelliJ :
- **Build → Build Project** (Ctrl+F9)

Ou en ligne de commande :
```bash
javac -d out src/model/*.java
javac -d out -cp out src/server/*.java
javac -d out -cp out src/client/*.java
```

### Phase 3 : Démarrer le NameService (ORBD)

**Terminal 1** :
```bash
orbd -ORBInitialPort 1050 -ORBInitialHost localhost
```

**Sortie attendue** :
```
ORBD ready and waiting for requests...
```

### Phase 4 : Lancer le Serveur

**Dans IntelliJ** :

1. **Run → Edit Configurations**
2. Cliquer sur **"+"** → Application
3. **Configuration** :
    - Name: `CORBA Server`
    - Main class: `server.Server`
    - Program arguments: `-ORBInitialPort 1050 -ORBInitialHost localhost`
4. **Apply → OK**
5. **Run "CORBA Server"** (Shift+F10)

**Sortie attendue** :
```
═══════════════════════════════════════════════
    Démarrage du Serveur CORBA
═══════════════════════════════════════════════

✓ ORB initialisé
✓ POA activé
✓ Servants créés
✓ Données initiales chargées : 3 étudiants
✓ Service Étudiant enregistré : EtudiantService
✓ Service Promotion enregistré : PromotionService

═══════════════════════════════════════════════
  Serveur CORBA prêt et en attente...
═══════════════════════════════════════════════
```

### Phase 5 : Lancer le Client

**Configuration similaire** :
- Name: `Client Etudiant`
- Main class: `client.ClientEtudiant`
- Program arguments: `-ORBInitialPort 1050 -ORBInitialHost localhost`

**Sortie attendue** :
```
═══════════════════════════════════════════════
    Client CORBA - Service Étudiant
═══════════════════════════════════════════════

✓ Connexion à l'ORB établie
✓ Service de nommage contacté
✓ Service Étudiant trouvé

─────────────────────────────────────────────
TEST 1 : Ajouter des épreuves
─────────────────────────────────────────────
Ajout épreuve Alice : ✓ Réussi
Ajout épreuve Bob : ✓ Réussi

─────────────────────────────────────────────
TEST 4 : Top 10 des étudiants
─────────────────────────────────────────────
Classement (Top 3) :
1. Claire Bernard : 17.00/20
2. Alice Martin : 16.88/20
3. Bob Dupont : 13.72/20

═══════════════════════════════════════════════
  Tous les tests terminés avec succès !
═══════════════════════════════════════════════
```

---

## 🧪 Tests et Résultats

### Scénarios de Test

#### Test 1 : Ajouter des Épreuves

```java
Epreuve math = new Epreuve("Mathématiques", 16.5f, 3);
boolean resultat = etudiantRef.ajouterUneEpreuve("Alice Martin", math);

// Résultat : true
// Console serveur : ✓ Épreuve ajoutée : Mathématiques (16.5/20, coef 3)
```

#### Test 2 : Lister les Épreuves

```java
Epreuve[] epreuves = etudiantRef.listeDesEpreuves("Alice Martin");

// Résultat :
// [
//   Epreuve{nom="Math", note=16.5, coefficient=3},
//   Epreuve{nom="Physique", note=14.0, coefficient=2},
//   Epreuve{nom="Info", note=18.0, coefficient=4}
// ]
```

#### Test 3 : Calculer une Moyenne

```java
float moyenne = etudiantRef.calculerLaMoyenne("Alice Martin");

// Résultat : 16.88
// Calcul : (16.5×3 + 14.0×2 + 18.0×4) / (3+2+4) = 152/9 = 16.88
```

#### Test 4 : Top 10 des