# Compte rendu – AutoLoc API

## 1. Injection des dépendances

Dans ce projet, j’ai utilisé l’injection des dépendances de Spring pour faciliter la gestion des objets et leur utilisation dans les différentes classes.

Il existe plusieurs façons de faire l’injection : par attribut, par setter et par constructeur.

L’injection par attribut consiste à utiliser l’annotation `@Autowired` directement sur un attribut. Elle est simple à écrire, mais elle rend les dépendances moins visibles.

L’injection par setter permet d’injecter une dépendance à travers une méthode. Elle peut être utile lorsque la dépendance doit être modifiable.

Pour ce projet, j’ai privilégié l’injection par constructeur. Elle permet de déclarer clairement les dépendances nécessaires à une classe. Avec `@RequiredArgsConstructor` de Lombok, le constructeur peut être généré automatiquement pour les attributs déclarés `final`.

## 2. Organisation des services

Le projet AutoLoc API utilise Spring Boot et Spring Data JPA pour gérer les données de l’application.

Les repositories servent à communiquer avec la base de données. Ils permettent notamment d’ajouter, de rechercher, de modifier et de supprimer des entités.

Les services contiennent la logique de traitement. Ils utilisent les repositories pour effectuer les opérations demandées.

Les principaux services du projet concernent les agences, les employés, les véhicules, les équipements, les clients, les réservations, les contrats, les paiements et les maintenances.

Cette organisation permet de séparer les responsabilités et de rendre le code plus facile à comprendre et à maintenir.

## 3. Gestion des données

Chaque entité représente un élément de l’application. Par exemple, la classe `Vehicule` contient des informations comme l’immatriculation, la marque, le modèle, la catégorie et le tarif journalier.

La classe `Client` contient les informations des clients, notamment leur nom, leur prénom, leur adresse e-mail et leur numéro de permis.

Les réservations relient un client à un véhicule. Les contrats sont associés aux réservations et peuvent contenir plusieurs paiements.

Dans les services, certaines vérifications peuvent être effectuées avant d'enregistrer les données. Par exemple, il est préférable de vérifier qu’un tarif journalier n’est pas négatif et qu’un identifiant existe avant de modifier une entité.

Lorsqu’une entité recherchée n’existe pas, une exception peut être utilisée pour signaler le problème.

## 4. Gestion des erreurs avec Spring

Spring utilise des annotations pour identifier les classes qui participent au fonctionnement de l’application. L’annotation `@Service` permet notamment de déclarer une classe comme composant de la couche service.

Plusieurs problèmes peuvent survenir lors de la configuration d’une application Spring. Un bean peut être introuvable si une classe n’est pas correctement déclarée ou détectée. Une dépendance circulaire peut également se produire lorsque plusieurs classes dépendent directement les unes des autres.

Lorsqu’il existe plusieurs beans du même type, Spring peut avoir besoin d’une indication supplémentaire, par exemple avec `@Qualifier` ou `@Primary`.

Il est donc important de bien organiser les classes et de déclarer correctement leurs dépendances.

## 5. Qualité du code

La qualité du code est importante pour faciliter la maintenance du projet. SonarQube peut être utilisé pour analyser le code source et signaler différents problèmes potentiels.

Parmi les points à surveiller, on peut citer la répétition du code, les méthodes trop longues, les problèmes de lisibilité et la gestion des exceptions.

L’utilisation des services, des repositories et de l’injection par constructeur contribue à rendre le code plus structuré.

Les résultats d’une analyse SonarQube doivent être consultés pour identifier les problèmes réellement présents dans le projet.

## 6. Questions de synthèse

**1. Qu’est-ce que l’IoC ?**

L’IoC, ou Inversion of Control, signifie que la création et la gestion des objets sont confiées au framework plutôt que d’être entièrement réalisées par le programmeur.

**2. Quelle est la différence entre IoC et injection des dépendances ?**

L’IoC est le principe général. L’injection des dépendances est une manière de l’appliquer en fournissant à une classe les objets dont elle a besoin.

**3. Pourquoi utiliser l’injection par constructeur ?**

Elle permet de voir directement les dépendances nécessaires à une classe et de s’assurer qu’elles sont fournies lors de sa création.

**4. À quoi sert `@Service` ?**

Cette annotation indique à Spring qu’une classe appartient à la couche service et doit être prise en compte comme composant de l’application.

**5. Quelle est la différence entre un service et un repository ?**

Le repository s’occupe principalement de l’accès aux données, tandis que le service gère les traitements et les règles de l’application.

## Conclusion

Ce travail m’a permis de mieux comprendre l’organisation d’une application Spring Boot et le rôle des différentes couches. La séparation entre les services et les repositories facilite la gestion des données et permet de garder un code plus clair. L’injection des dépendances permet également de mieux organiser les relations entre les classes du projet.