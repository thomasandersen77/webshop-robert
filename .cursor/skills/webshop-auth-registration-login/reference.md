# Referanse: leveransemal og testkrav (auth)

Bruk denne strukturen når brukeren ber om implementasjon eller plan med full leveranse.

---

## A. Bekreftede regler

List kun punkter som er bekreftet fra eksisterende kode eller `docs/kravspesifikasjon-webshop.md`.

---

## B. MÅ VERIFISERES

List kun forhold som ikke kan bekreftes (JWT vs session, unik e-post, default-rolle, admin-only opprettelse, aktiv/inaktiv, osv.).

---

## C. Modulansvar

| Modul | Innhold |
|--------|---------|
| `api` | Request/response DTO-er, auth-controller(e), `@CurrentUser`, syntaktisk validering, DTO↔domene-mapping, HTTP/Spring Security-konfig som hører til grensen |
| `core` | `User` aggregate, commands, domain services/use cases, `UserRepository` **interface**, passordverifisering som domenekontrakt uten Spring-typer, `RbacService`-bruk for autorisasjon |
| Persistence | `UserEntity`, JPA repository, adapter, `UserEntity`↔`User` mapping |

---

## D. Filer som skal opprettes eller endres

Tabell:

| Path | Handling | Begrunnelse |
|------|-----------|-------------|
| … | opprett/endre/slett | … |

---

## E. Fullstendige kodeforslag

Lever komplette filer eller patcher for minst:

- Auth controller
- Request/response DTO-er
- Domain commands
- Domain user aggregate (evt. utvid eksisterende `User`)
- Repository interface (+ persistence adapter / entity)
- `UserEntity`
- Mappere: DTO↔domain, entity↔domain
- Service(r) for registrering og innlogging
- `GET /api/auth/me` med `@CurrentUser`

---

## F. Liquibase

Ny changeset YAML hvis schema må endres. Aldri redigere eksisterende changesets.

---

## G. Tester

Konkrete tester for:

- Registrering (happy path + validering/konflikt)
- Innlogging (suksess + feil credentials)
- `/api/auth/me` (autentisert + uautentisert)
- Mapping (enhet der det gir mening)
- Repository/persistence (integrasjon om prosjektet har mønster)
- Security-grense (MockMvc, filter chain, `@CurrentUser`)

---

## Kvalitets-sjekkliste (obligatorisk)

- [ ] Ingen forretningslogikk i controller
- [ ] Ingen DTO-er i `core`
- [ ] Ingen JPA entities i `core`
- [ ] Ingen repository-kall fra controller
- [ ] `core` bruker kun domain types
- [ ] `User` aggregate root er eneste write path for brukeropprettelse/-endring som omfattes
- [ ] Innlogget bruker sendes eksplisitt som domain `User` inn i services der det er relevant
- [ ] Ingen singleton / global mutable current user / thread-local i `core`
- [ ] Norsk i forretningsexceptions og forretningslogger
