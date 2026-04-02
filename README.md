# webshop-robert

Backend og database for nettbutikk (Robert E). Frontend er skilt ut til eget repo: `webshop-robert-web` (klon/kopi ved siden av dette repoet).

## Database (PostgreSQL) fra prosjektroten

Prosjektet har en root `pom.xml` og `docker-compose.yml` slik at databasen kan styres fra samme IDE-prosjekt.

Maven-profiler:

- Start DB: `mvn -Pdb-up validate`
- Stopp DB: `mvn -Pdb-down validate`
- Reset DB (slett volum + start pa nytt): `mvn -Pdb-reset initialize`

Direkte docker compose (alternativ):

- `docker compose up -d postgres`
- `docker compose down`

Standard DB-verdier:

- database: `webshop`
- user: `webshop`
- password: `webshop`
- host: `localhost`
- port: `5432`
