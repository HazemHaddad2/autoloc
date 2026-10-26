# AutoLoc API — Atelier 5 : Spring MVC et SpringDoc

## A. Endpoints de l'API

L'application AutoLoc utilise des contrôleurs REST pour gérer les différentes ressources. Les contrôleurs reçoivent les requêtes HTTP et délèguent le traitement aux services.

### 1. ContratController

- **GET `/api/contrats`** : récupérer la liste des contrats. Le code de succès attendu est `200 OK`.
- **GET `/api/contrats/{id}`** : consulter un contrat par son identifiant. Le code de succès est `200 OK`. Si le contrat n'existe pas, l'API doit renvoyer `404 Not Found`.
- **POST `/api/contrats`** : créer un contrat. Le code de succès attendu est `201 Created`. Des données invalides peuvent provoquer `400 Bad Request`.
- **PUT `/api/contrats/{id}`** : modifier un contrat. Le code de succès attendu est `200 OK`. Les erreurs possibles sont `400 Bad Request` et `404 Not Found`.
- **DELETE `/api/contrats/{id}`** : supprimer un contrat. Le code de succès attendu est `204 No Content`. Si le contrat n'existe pas, l'API doit renvoyer `404 Not Found`.

Ces opérations passent par l'interface `IContratService`, qui sépare le contrôleur de la logique métier.

### 2. PaiementController

- **GET `/api/paiements`** : récupérer la liste des paiements. Le code attendu est `200 OK`.
- **GET `/api/paiements/{id}`** : consulter un paiement par son identifiant. Le code attendu est `200 OK`, ou `404 Not Found` si le paiement n'existe pas.
- **POST `/api/paiements`** : cette opération n'est pas autorisée dans l'atelier, car le contrôleur des paiements est en lecture seule. La réponse attendue est `405 Method Not Allowed`.

Les paiements peuvent être créés avec un contrat grâce à la relation entre `Contrat` et `Paiement` et à la cascade de persistance configurée dans l'entité `Contrat`.

### 3. Autres contrôleurs

Les autres contrôleurs de l'application suivent les conventions REST pour les ressources suivantes :

- `AgenceController` : `/api/agences`
- `EmployeController` : `/api/employes`
- `VehiculeController` : `/api/vehicules`
- `EquipementController` : `/api/equipements`
- `ClientController` : `/api/clients`
- `ReservationController` : `/api/reservations`
- `MaintenanceController` : `/api/maintenances`

Ils proposent les opérations CRUD : création, consultation, modification et suppression, selon les fonctionnalités implémentées. Les données invalides peuvent entraîner `400 Bad Request`, et une ressource inexistante peut entraîner `404 Not Found`.

## B. Annotations Jackson

### 1. Association entre Contrat et Paiement

L'association entre `Contrat` et `Paiement` est bidirectionnelle : un contrat contient une liste de paiements, et chaque paiement possède une référence vers son contrat.

Dans l'entité `Contrat`, l'attribut `paiements` utilise :

`@JsonManagedReference("contrat-paiements")`

Cette annotation permet d'inclure les paiements dans le JSON du contrat.

Dans l'entité `Paiement`, l'attribut `contrat` utilise :

`@JsonBackReference("contrat-paiements")`

Cette annotation empêche la sérialisation de repartir du paiement vers le contrat. Les deux annotations portent le même nom afin de relier les deux côtés de l'association.

Ces annotations évitent une boucle infinie lors de la conversion des objets Java en JSON.

### 2. Différence entre @JsonBackReference et @JsonIgnore

`@JsonBackReference` est utilisée avec `@JsonManagedReference` pour gérer une association bidirectionnelle. Elle empêche la sérialisation du côté inverse de l'association.

`@JsonIgnore` permet d'exclure complètement un attribut de la sérialisation JSON.

Dans notre projet, `Contrat.paiements` utilise `@JsonManagedReference` parce que les paiements doivent apparaître dans la réponse JSON d'un contrat. En revanche, `Agence.vehicules` utilise `@JsonIgnore` pour ne pas exposer la collection des véhicules dans le JSON de l'agence.

## C. Postman et Swagger UI

### 1. Postman

Postman est un outil qui permet d'envoyer des requêtes HTTP à l'API et d'observer les réponses. Il affiche notamment le code HTTP, les en-têtes et le corps de la réponse. Il permet également d'enregistrer les requêtes et de les rejouer.

Pendant l'atelier, Postman permet par exemple de tester `GET /api/contrats` pour récupérer les contrats, puis `GET /api/contrats/9999` pour vérifier le comportement lorsque l'identifiant demandé n'existe pas.

La requête `POST /api/paiements` permet aussi de vérifier que la création directe d'un paiement est refusée, avec le statut attendu `405 Method Not Allowed`.

### 2. OpenAPI, SpringDoc et Swagger UI

OpenAPI est une spécification standard qui décrit les endpoints d'une API REST. SpringDoc analyse le code de l'application, notamment les contrôleurs et leurs annotations, pour générer cette documentation.

Swagger UI présente la documentation dans une interface web et permet d'exécuter les requêtes directement depuis le navigateur.

Dans notre projet, Swagger UI est accessible à l'adresse :

`http://localhost:8080/swagger-ui.html`

La documentation OpenAPI au format JSON est accessible à l'adresse :

`http://localhost:8080/v3/api-docs`

Postman et Swagger UI sont complémentaires. Postman est pratique pour organiser et rejouer des scénarios de test, tandis que Swagger UI permet d'explorer les endpoints et de consulter leur documentation.

## D. Anomalies SonarQube

L'analyse du projet avec SonarQube for IDE a détecté 16 problèmes dans 8 contrôleurs. Les problèmes concernent la règle `java:S4684`.

### Anomalie 1 : Utilisation d'entités JPA dans les paramètres des contrôleurs

**Règle :** `java:S4684` — Replace this persistent entity with a simple POJO or DTO object.

**Explication :** certains endpoints reçoivent directement des entités JPA dans leurs paramètres, notamment lors des opérations de création et de modification. Cela couple le format des requêtes HTTP au modèle de persistance de la base de données.

**Correction recommandée :** créer des DTO pour recevoir les données envoyées par le client, puis convertir ces DTO en entités avant d'appeler le service.

### Anomalie 2 : Utilisation d'entités JPA dans les réponses des contrôleurs

**Règle :** `java:S4684` — Replace this persistent entity with a simple POJO or DTO object.

**Explication :** certains endpoints retournent directement des entités JPA. Cela peut exposer des attributs internes et rendre la structure de l'API dépendante de celle de la base de données. Les associations entre entités peuvent également compliquer la sérialisation JSON.

**Correction recommandée :** créer des DTO de réponse contenant uniquement les informations nécessaires au client, puis convertir les entités en DTO avant de retourner les réponses HTTP.

### Résultat de l'analyse

SonarQube a signalé 2 problèmes dans chacun des contrôleurs suivants : `AgenceController`, `ClientController`, `ContratController`, `EmployeController`, `EquipementController`, `MaintenanceController`, `ReservationController` et `VehiculeController`.

Au total, 16 problèmes ont été détectés. L'utilisation de DTO constitue une amélioration recommandée pour réduire le couplage entre l'API REST et les entités JPA.

Ces recommandations n'ont pas été considérées comme corrigées dans le cadre de cette analyse ; elles sont documentées comme améliorations à réaliser.

## E. Questions de compréhension

### 1. Quel composant reçoit toutes les requêtes, et comment retrouve-t-il la méthode du contrôleur à appeler ?

Le `DispatcherServlet` de Spring MVC reçoit les requêtes HTTP. Il utilise les mappings définis par les annotations comme `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping` et `@DeleteMapping` pour retrouver le contrôleur et la méthode correspondant à l'URL et au verbe HTTP.

### 2. Pourquoi ContratController dépend-il de IContratService et non de ContratRepository ni de ContratServiceImpl ?

`ContratController` dépend de `IContratService` pour respecter la séparation des couches et limiter le couplage entre les composants. Le contrôleur ne doit pas accéder directement au repository, car l'accès aux données est géré par la couche service.

L'utilisation de l'interface permet également de remplacer l'implémentation du service plus facilement et de simplifier les tests.

### 3. Pourquoi un POST renvoie-t-il 201 et un DELETE 204 ? Pourquoi une ressource absente donne-t-elle 404 et non 500 ?

Le code `201 Created` indique qu'une nouvelle ressource a été créée avec succès.

Le code `204 No Content` indique qu'une opération a réussi sans qu'un corps de réponse soit nécessaire, par exemple après une suppression.

Le code `404 Not Found` indique que la ressource demandée n'existe pas. Une exception comme `ResourceNotFoundException` doit être correctement convertie en réponse HTTP 404 par la gestion des exceptions. Si cette exception n'est pas gérée correctement, l'application risque de renvoyer `500 Internal Server Error`.

### 4. Pourquoi PaiementController n'expose-t-il que des GET ? Quelle réponse reçoit un POST /api/paiements ?

`PaiementController` est volontairement en lecture seule dans cet atelier. Il permet de consulter les paiements, mais pas de les créer directement. La création d'un paiement peut être effectuée à travers un contrat, grâce à l'association entre `Contrat` et `Paiement` et à la cascade de persistance.

Une requête `POST /api/paiements` reçoit normalement `405 Method Not Allowed`, car cette méthode HTTP n'est pas définie dans le contrôleur.

### 5. Quels risques présente l'exposition directe des entités JPA ? Pourquoi Contrat.paiements utilise-t-il @JsonManagedReference alors que Agence.vehicules utilise @JsonIgnore ?

L'exposition directe des entités JPA peut révéler des informations internes, lier étroitement le format de l'API à la structure de la base de données et provoquer des problèmes de sérialisation des associations bidirectionnelles. Les DTO permettent de contrôler plus précisément les données reçues et renvoyées.

`Contrat.paiements` utilise `@JsonManagedReference` pour inclure les paiements dans le JSON du contrat tout en évitant une boucle de sérialisation.

`Agence.vehicules` utilise `@JsonIgnore` pour exclure la collection des véhicules du JSON de l'agence, car cette collection n'a pas besoin d'être exposée dans cette réponse.

## Conclusion

Cet atelier a permis de travailler sur les contrôleurs REST, les conventions HTTP, la gestion des exceptions, les associations entre entités JPA, les annotations Jackson et la documentation générée par SpringDoc.

Postman permet de tester les requêtes et leurs réponses, tandis que Swagger UI permet de consulter et d'exécuter les endpoints documentés. La séparation entre contrôleurs, services et repositories contribue à rendre l'application plus claire et plus facile à maintenir.
