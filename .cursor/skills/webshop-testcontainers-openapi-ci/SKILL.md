---
name: webshop-testcontainers-openapi-ci
description: Refactors Spring Boot + Maven webshop_backend so CI builds and integration tests use Testcontainers PostgreSQL instead of localhost:5432, and OpenAPI YAML is generated during verify from a running app backed by that container. Use when fixing "Connection to localhost:5432 refused", "Unable to determine Dialect without JDBC metadata", CI build failures without local Postgres, OpenAPI generation at build time, springdoc /v3/api-docs.yaml, Failsafe vs Surefire, or @ServiceConnection Testcontainers setup for this repo.
---

# Testcontainers + OpenAPI i Maven-bygg (CI-trygt)

## Les først

1. **Arkitektur:** `.aiassistant/rules/BACKEND_RULES.md` — ingen infrastrukturlekkasje inn i `core`; konservative, minimale endringer.
2. **Mål:** Bygg og integrasjonstester skal **ikke** avhenge av manuell Postgres på `localhost:5432`. Bruk **Testcontainers PostgreSQL** og Spring Boot test-integrasjon.
3. **OpenAPI:** Generer **YAML** fra faktisk kjørende app (f.eks. `GET /v3/api-docs.yaml`), skriv til f.eks. `target/generated/openapi.yaml`, i en fase som kjører i CI (typisk `verify` via Failsafe).

## Ikke-forhandlingsbare krav

- **Ingen** krav om lokal Postgres, manuelt `docker compose`, eller skjult `localhost:5432` for build/test/IT-paths.
- **Ikke** «løsning» kun med `hibernate.dialect`, ignorerte feil, eller H2 som erstatning for Postgres i denne flyten — **ekte Postgres** via Testcontainers.
- **Ikke** råd i stil med «start Postgres lokalt», «skip IT i CI», eller «commit manuell openapi.yaml» som hovedløsning.

## Foretrukket retning

| Område | Anbefaling |
|--------|------------|
| Enhetstester | Surefire, raskt, ofte uten DB eller med mocks |
| Integrasjonstester | Failsafe (`verify`), `@SpringBootTest`, `@Testcontainers`, `PostgreSQLContainer`, `@ServiceConnection` hvis støttet av prosjektets Spring Boot-versjon |
| OpenAPI | Dedikert IT-klasse (f.eks. `OpenApiGenerationIT`): start kontekst, treff `/v3/api-docs.yaml`, skriv fil, feil ved feil |
| Konfig | Test/IT-profiler uten `jdbc:postgresql://localhost:5432` som eneste sannhet for IT |

## Undersøk alltid (fra repo — ikke gjett)

1. Rot- og modul-`pom.xml`: Surefire/Failsafe, `spring-boot-maven-plugin`, eventuelle OpenAPI-plugins.
2. `application.yml` / `application-*.yml` og miljøvariabler for datasource.
3. Om OpenAPI kommer fra **springdoc** (`/v3/api-docs.yaml`) eller annet.
4. **Nøyaktig** hvor `localhost:5432` eller manglende JDBC-metadata utløser feilen.

## Leveranseformat

Når brukeren ber om full gjennomgang, svar med strukturen i **[reference.md](reference.md)** (seksjoner A–G).

## Anti-patterns (kort)

- Hardkodet dialect uten fungerende DB-tilkobling som rotfiks.
- Falsk fallback-config som skjuler ødelagt wiring.
- Flytte forretningslogikk mellom moduler uten behov.
