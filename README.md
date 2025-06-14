# LigueyLu Backend

**LigueyLu** est une application de gestion des prestations de services (ex. : électricien, plombier, maçon, etc.). Ce dépôt contient la partie **backend** développée avec **Spring Boot**.

---

## 🚀 Fonctionnalités

- Gestion des utilisateurs (prestataires et clients)
- Création et gestion de prestations de services
-  Les types de services :
   - Électricien
   - Plombier
   - Maçon
   - Jardinier
   - Peintre
   - Menuisier
   - Climaticien
- API REST sécurisée avec Spring Security
- Connexion à une base de données MySQL
- Support de la validation, pagination, et gestion d’erreurs

---

## 🛠️ Technologies utilisées

- Java 17+
- Spring Boot
   - Spring Web
   - Spring Data JPA
   - Spring Security
- Base de données : MySQL (ou H2 pour les tests)
- Maven
- Lombok
- Swagger/OpenAPI (documentation API)

---

## 📁 Structure du projet

```bash
ligueylu-backend/
│
├── src/main/java/master/ipld/ligueylu/
│   ├── controller/         # Contrôleurs REST
│   ├── service/            # Logique métier
│   ├── repository/         # Accès aux données
│   ├── model/
│   │   ├── entities/       # Entités JPA
│   │   └── enums/          # Enumérations (ex: TypeService)
│   ├── security/           # Configuration Spring Security
│   └── LigueyLuApplication.java # Classe principale
│
├── src/main/resources/
│   ├── application.properties (ou application.yml)
│
└── build.gradle
