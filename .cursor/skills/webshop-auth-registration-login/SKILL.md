---
name: webshop-auth-registration-login
description: Implements user registration, login, and authenticated current-user endpoints for webshop_backend (api/core/persistence) under strict layered DDD rules. Use when the user asks for auth, registrering, innlogging, JWT or session login, PasswordEncoder, User aggregate, @CurrentUser, /api/auth/register, /api/auth/login, /api/auth/me, RbacService, or security flows for this Kotlin Spring backend.
---

# Webshop auth: registrering og innlogging

## Les først

1. **Kanonical backend-regler:** `.aiassistant/rules/BACKEND_RULES.md`. Rot `.cursorrules` henviser til samme dokument — avvik er ikke tillatt uten eksplisitt brukerbeskjed.
2. **Krav:** `docs/kravspesifikasjon-webshop.md` når filen finnes — den vinner ved konflikt.
3. **Eksisterende kode:** Kartlegg `User`, `UserRole`, `UserPrincipal`, `@CurrentUser`, `CurrentUserArgumentResolver`, og `UserRepository` før du endrer — se [Eksisterende tilstand nedenfor](#eksisterende-tilstand-i-repoet).

## Absolutte arkitekturregler (denne oppgaven)

- **`api`:** kun HTTP-grense — DTO-er, controller, syntaksvalidering, eksplisitt mapping til/fra domene, `@CurrentUser user: User`, ingen forretningslogikk, ingen direkte repository-kall fra controller.
- **`core`:** domeneaggregat, value objects, commands, use-case-tjenester, repository-**grensesnitt**, `RbacService` for autorisasjon i tjenestelaget. Ingen DTO-er, ingen `*Entity`, ingen Spring Security-/servlet-typer, ingen singleton/thread-local current user.
- **Persistence:** `UserEntity`, JPA, adapter som implementerer `UserRepository` (eller tilsvarende port), eksplisitt `UserEntity` ↔ `User`.
- **Skriveflyt:** `DTO → domain command → aggregate/repository → Entity`. **Leseflyt:** `Entity → User → DTO`.
- **`User` er aggregate root:** alle bruker-skrivinger går gjennom domenet + én repository-grense; ingen parallelle write-paths.
- **Passord:** hash og sammenligning i riktig lag (typisk `api`/infrastruktur med `PasswordEncoder`); `core` skal ikke importere Spring Security — modeller verifisering som domenebehov (f.eks. resultat/exception) uten framework-typer.
- **JPA:** suffiks `Entity`, ikke `data class` for entities. **Liquibase:** nye changesets for schema; aldri redigere gamle.
- **Språk:** norske forretningslogger og forretnings-/brukervendte exception-meldinger; identifikatorer på engelsk.

## Endepunkter (mål)

| Flyt | Konservativ kontrakt |
|------|----------------------|
| Registrering | `POST /api/auth/register` — request DTO → command → service → hash → persist `UserEntity` → response DTO |
| Innlogging | `POST /api/auth/login` — credentials → service verifiserer → etabler sesjon/JWT **i tråd med eksisterende** security-oppsett → response DTO |
| Nåværende bruker | `GET /api/auth/me` — `@CurrentUser user: User`, kun mapping til response DTO i controller |

## Obligatorisk eksplisitt mapping

Agenten skal implementere og vise (i kode eller patch):

- `RegisterUserRequestDto` → `RegisterUserCommand`
- `LoginRequestDto` → `LoginCommand`
- `UserEntity` → `User`
- `User` → `UserResponseDto`

Ingen hopping over mapping-lag. Ingen DTO i `core`. Ingen entity i `core`.

## Før implementering: bekreft eller merk

Sjekk kravdok og kode. Ting som **ikke** kan verifiseres uten kilde, skal merkes **`MÅ VERIFISERES`** i leveransen:

- Unik e-post
- Standardrolle `CUSTOMER` ved registrering
- Om `ADMIN` kun opprettes internt
- JWT vs session vs begge
- Aktiv/inaktiv bruker i domene
- Eksakt request/response-felter

## Eksisterende tilstand i repoet

Verifisert mønstre (oppdater ved behov etter endringer):

- `core`: `User` (id, email, password, role), `UserRole`, `UserRepository` — **merk:** dagens `UserRepository` er annotert med Spring `@Repository` i `core`; streng tolkning av BACKEND_RULES tilsier at repository-**grensesnitt** lever i `core` uten Spring-annotasjoner og at implementasjon ligger i persistence/api-modul — **refaktorer om nødvendig** som del av auth-leveransen.
- `api`: `UserPrincipal`, `@CurrentUser`, `CurrentUserArgumentResolver` (henter `User` via `UserRepository` fra principal-id).

Auth-endepunkter og full login-flyt kan mangle — implementer i henhold til tabellen over.

## Arbeidsflyt for agenten

1. Les BACKEND_RULES + eventuell kravspec.
2. Kartlegg eksisterende SecurityFilterChain, encoder, og brukerlagring.
3. Design `RegisterUserCommand` / `LoginCommand` og tjenester i `core`; hash/Security-konfig i `api` eller dedikert persistence etter prosjektets mønster uten å lekke framework inn i `core`.
4. Legg Liquibase-changeset hvis tabell/kolonner mangler.
5. Skriv tester: MockK i `core`; integrasjonstester for tjenester + DB i **`core`** (BACKEND_RULES §13.0); MockMvc i `api` kun for HTTP-kontrakt.

## Leveranseformat og detaljer

Full mal for svar (seksjoner A–G), testliste og kvalitets-sjekkliste: **[reference.md](reference.md)**.
