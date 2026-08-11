# Chronicles

Monorepo: backend Java (Spring Boot, Java 21, Maven) et frontend React + TypeScript (Vite).

Prérequis
- Java 21 + Maven (ou utiliser le wrapper ./mvnw)
- Node 18+ et npm
- Docker & docker compose (optionnel pour dev intégré)

Démarrage local (développement)

Backend
```
cd backend
./mvnw spring-boot:run
```

Frontend
```
cd frontend
npm install
npm run dev
```

Avec Docker Compose (base de données Postgres incluse)
```
docker compose up --build
```
La configuration de la base se trouve dans `.env` (ne pas committer). Utiliser `.env.example` comme modèle.

Tests

Backend
```
cd backend
./mvnw test
```

Frontend
```
cd frontend
npm run build
npm run lint
```

Sécurité & nettoyages
- NE PAS committer `.env` (variables de connexion). Ce dépôt contenait `.env` — il a été supprimé et il est recommandé de faire une rotation des secrets.
- Ignorer les artefacts de build (`backend/target`) et les fichiers IDE (`.idea/`).

Support
- Pour toute question, ouvrir une issue dans le dépôt.
