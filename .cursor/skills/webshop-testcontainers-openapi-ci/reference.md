# Referanse: Testcontainers + OpenAPI — leveransemal

Bruk denne strukturen når brukeren ber om full analyse eller implementasjon.

---

## A. Root cause

Forklar nøyaktig hvorfor dagens oppsett feiler med `localhost:5432 refused` / `Unable to determine Dialect without JDBC metadata`.

---

## B. Refactoring plan

Liste over minimale endringer for å flytte build/test/OpenAPI til Testcontainers.

---

## C. Files to change

| Path | Action | Reason |
|------|--------|--------|
| … | … | … |

---

## D. Exact code changes

Fullstendige patcher eller hele filer for:

- Parent / modul `pom.xml`
- `application.yml` / `application-*.yml`
- Integrasjonstestklasser
- OpenAPI-genereringstest (f.eks. `OpenApiGenerationIT`)
- Testcontainers-relatert konfig (`@ServiceConnection` eller tilsvarende)

---

## E. Maven lifecycle

Forklar hvilken fase som gjør hva etter refaktor (compile, test, verify, osv.).

---

## F. How this prevents the localhost:5432 error

Konkret hvorfor feilen ikke lenger kan oppstå i CI.

---

## G. Risks / MÅ VERIFISERES

Kun punkter som ikke kan bekreftes fra repoet.

---

## Kvalitetskrav (sjekkliste)

- [ ] Implementasjonsklart og repo-spesifikt
- [ ] Ekte PostgreSQL via Testcontainers på IT-path
- [ ] OpenAPI YAML fra kjørende app, reproduserbart i CI
- [ ] Ingen skjult avhengighet til manuell Postgres på localhost for build/IT
- [ ] Ikke primærløsning: kun dialect, skip IT, manuell openapi, H2 i stedet for Postgres, «start DB lokalt»

---

## Original prompt (utfyllende kontekst)

Refaktorer Spring Boot + Maven slik at:

1. Bygg fungerer i CI uten lokal PostgreSQL.
2. Feilene `Connection to localhost:5432 refused` og `Unable to determine Dialect without JDBC metadata` ikke oppstår under build eller integrasjonstest.
3. API/OpenAPI-spec fortsatt kan genereres under Maven-bygg.
4. Løsningen bruker **Testcontainers**.
5. Ren modulær monolitt beholdes.

OpenAPI foretrukket: IT som starter app med Testcontainers-DB, kaller `/v3/api-docs.yaml`, skriver til f.eks. `target/generated/openapi.yaml`.

Failsafe for integrasjonstester i `verify` der det passer. Bruk moderne Spring Boot + Testcontainers (`@Testcontainers`, `@Container`, `@ServiceConnection` når passende).
