# Robert webshop – Cursor brief v2

Dette dokumentet er en **Cursor-ready build brief** for å generere **tre forskjellige, moderne og lyse nettbutikkdesign** for en proof of concept.  
Målet er å lage et design som **ikke ligner Norges Gass visuelt**, men som tar med det beste derfra:

- tydelig struktur
- mobilvennlig navigasjon
- raske klikkflater
- enkel orientering
- ryddig informasjonsarkitektur

## Teknologistack

Bruk:

- **React**
- **Vite**
- **TypeScript**
- **Material UI (MUI)**
- **React Router**
- **MUI Icons**

## Overordnede krav

Lag **3 separate sider / designvarianter**:

- `/design-a`
- `/design-b`
- `/design-c`

Hver side skal være fullt fungerende som en moderne nettbutikk-forside.

Alle tre design skal:

1. være **lyse**
2. være **moderne**
3. være **mobile first**
4. ha **hamburgermeny på mobil**
5. ha **søkefelt**
6. ha **handlekurv-ikon**
7. ha **klikkbare kategoriruter**
8. ha **klikkbare produktkort**
9. ha **4 produktkategorier**
10. vise **eksempelprodukter under kategoriene**, eller integrert i layouten
11. ha en tydelig CTA
12. bruke **runde hjørner, luft, myke skygger og ryddig spacing**
13. være lette å navigere i for vanlige kunder

## Produktkategorier

Bruk disse fire kategoriene i alle tre design:

1. **Elektronikk**
2. **Hjem & Interiør**
3. **Sport & Fritid**
4. **Skjønnhet & Velvære**

## Bildereferanser

Bruk disse bildene direkte som `image` i mockdata, eller bruk dem som referanse og bytt til tilsvarende lisensierte bilder senere.

### Elektronikk
- Direkte bilde:
  - `https://images.unsplash.com/photo-1749934511277-e90042265d35?auto=format&fit=crop&fm=jpg&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&ixlib=rb-4.1.0&q=60&w=3000`
- Kilde/referanse:
  - `https://unsplash.com/photos/headphones-are-featured-on-a-product-advertisement-YuuOqkgKhx0`

### Hjem & Interiør
- Direkte bilde:
  - `https://images.unsplash.com/photo-1737647862097-80f014f84140?auto=format&fit=crop&fm=jpg&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&ixlib=rb-4.1.0&q=60&w=3000`
- Kilde/referanse:
  - `https://unsplash.com/photos/a-living-room-with-a-couch-and-a-lamp-x-yf-3sQ_FE`

### Sport & Fritid
- Direkte bilde:
  - `https://images.unsplash.com/photo-1705585851308-1b1080ba0144?auto=format&fit=crop&fm=jpg&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&ixlib=rb-4.1.0&q=60&w=3000`
- Alternativ direkte bilde:
  - `https://images.unsplash.com/photo-1676312830459-f6f13dfdd899?auto=format&fit=crop&fm=jpg&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&ixlib=rb-4.1.0&q=60&w=3000`
- Kilde/referanse:
  - `https://unsplash.com/photos/a-pair-of-running-shoes-a-bottle-of-water-and-a-pair-of-socks-I-9CbroW45o`

### Skjønnhet & Velvære
- Direkte bilde:
  - `https://images.unsplash.com/photo-1741896135705-9dfb73461085?auto=format&fit=crop&fm=jpg&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&ixlib=rb-4.1.0&q=60&w=3000`
- Alternativ direkte bilde:
  - `https://images.unsplash.com/photo-1768881187102-ca0131989c8a?auto=format&fit=crop&fm=jpg&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&ixlib=rb-4.1.0&q=60&w=3000`
- Kilde/referanse:
  - `https://unsplash.com/photos/bottles-of-skincare-products-are-displayed-0U4IyX7Zwhg`

## Designretning

Lag tre ulike uttrykk:

### Design A – Hero + kategori-grid
- Stor hero-seksjon øverst
- Kort introtekst
- Tydelig CTA-knapp
- 4 store kategorikort under
- Produktseksjon under kategoriene
- Fokus på enkelhet og premium butikkfølelse

### Design B – Editorial / magasin-stil
- Mer eksklusivt uttrykk
- Store bildeflater
- Asymmetrisk grid
- Kategorier som store visuelle blokker
- Mer “designbutikk” / livsstilsbutikk
- Produkter integrert som innholdskort

### Design C – App-lignende mobilhandel
- Ekstra ryddig og praktisk
- Kortbasert layout
- Tydelig søkefelt
- Rask tilgang til kategorier
- Fokus på enkel browsing på mobil
- Passer godt til vanlige nettbutikker med mange produkter

## Navigasjon

### Desktop
Toppnavigasjon med:
- logo / butikknavn
- søkefelt
- kontoikon
- handlekurvikon

### Mobil
- venstre: **hamburger-ikon**
- midten: logo / butikknavn
- høyre: søk + handlekurv
- hamburgermeny åpner en `Drawer` fra venstre

### Elementer i hamburgermeny
- Hjem
- Kategorier
- Tilbud
- Nyheter
- Min konto
- Handlekurv

## Klikkbarhet

Følgende skal være klikkbart:

- alle kategorikort
- alle produktkort
- logo
- hamburgermeny-elementer
- handlekurvikon
- CTA-knapper
- produktbilder
- “Se alle” lenker

Bruk `cursor: pointer` der det er naturlig og hover-effekter på desktop.

## Handlekurv

Lag en enkel handlekurv-drawer:

- åpnes fra handlekurvikonet
- viser 2–3 eksempelprodukter
- viser subtotal
- har knappene:
  - `Gå til betaling`
  - `Fortsett å handle`

Handlekurven trenger ikke ekte checkout-logikk, men UI-et må være realistisk.

## Søkefelt

Alle tre design må ha søkefelt.

Krav:
- tydelig placeholder, f.eks. `Søk etter produkter`
- søkeikon i feltet
- ryddig visning på mobil
- desktop kan ha bredere variant

## Eksempeldata

Bruk eksempeldata med disse kategoriene og noen produkter per kategori.

Eksempelprodukter:

### Elektronikk
- Trådløse hodetelefoner
- Smartklokke
- Powerbank
- Bluetooth-høyttaler

### Hjem & Interiør
- Bordlampe
- Lounge-stol
- Sengetøy
- Duftlys

### Sport & Fritid
- Løpesko
- Treningsflaske
- Yogamatte
- Ryggsekk

### Skjønnhet & Velvære
- Serum
- Ansiktskrem
- Rensegel
- Body lotion

## MUI-komponenter som skal brukes

Foretrekk disse:

- `AppBar`
- `Toolbar`
- `Container`
- `Grid`
- `Box`
- `Stack`
- `Card`
- `CardMedia`
- `CardContent`
- `Typography`
- `Button`
- `Chip`
- `TextField`
- `InputAdornment`
- `IconButton`
- `Drawer`
- `List`
- `ListItemButton`
- `Badge`
- `Divider`
- `Paper`

## Ikoner

Bruk MUI icons, for eksempel:

- `Menu`
- `Search`
- `ShoppingCart`
- `PersonOutline`
- `FavoriteBorder`
- `ChevronRight`

## Theme-regler

Lag et lyst theme.

Ønsket uttrykk:
- bakgrunn: lys grå / offwhite
- kort: hvite eller svært lyse
- primærfarge: dempet blå eller petrol
- sekundærfarge: varm beige / lys sand / lys grå
- border radius: 16–24px
- myke skygger
- god whitespace
- ingen tunge mørke flater

## Responsivitet

Bygg **mobile first**.

Krav:
- `xs` er basis
- kategorikort skal stable på mobil
- grid kan gå til 2 kolonner på større mobil / tablet
- desktop kan bruke 3–4 kolonner
- ingen horisontal scroll
- store touch targets
- drawer og handlekurv må fungere bra på mobil

## Filstruktur

Lag en ryddig filstruktur:

```text
src/
  app/
  components/
    layout/
    navigation/
    cards/
    cart/
    search/
  data/
  pages/
    DesignA.tsx
    DesignB.tsx
    DesignC.tsx
  theme/
  routes/
```

## Routing

Lag routing med disse rutene:

- `/` -> redirect til `/design-a`
- `/design-a`
- `/design-b`
- `/design-c`

Gjerne legg inn en enkel design-switcher øverst eller i en liten flytende meny.

## Interaksjoner

Legg inn små, moderne interaksjoner:
- subtil hover på cards
- lett scale eller shadow på hover
- smooth drawer open/close
- tydelig focus state
- klikk på kategorikort kan navigere til `#products` eller en fiktiv kategori-side

## Innholdstonalitet

Norsk språk i UI:

- `Søk etter produkter`
- `Se alle`
- `Legg i handlekurv`
- `Utforsk kategorier`
- `Utvalgte produkter`
- `Nyheter`
- `Tilbud`
- `Gå til betaling`

## Konkrete leveransekrav til Cursor

Generer kode som:

1. kan kjøres direkte med `npm install` og `npm run dev`
2. bruker **MUI** riktig
3. har **3 distinkte visuelle design**
4. har fungerende responsivitet
5. bruker mockdata
6. gjør alle relevante kort og ruter klikkbare
7. har hamburgermeny på mobil
8. har søkefelt og handlekurv
9. er lett å vise frem til kunde
10. ser ut som en moderne nettbutikk, ikke et adminsystem

## Viktig

- **Ikke** kopier utseendet til Norges Gass
- men behold de gode prinsippene:
  - tydelig navigasjon
  - mobilvennlighet
  - klikkbare flater
  - enkel orientering
  - ryddig struktur

## Bonus

Hvis det er tid, legg også til:
- favorittikon på produktkort
- liten badge som `Ny`, `Populær`, `Bestselger`
- enkel mikroanimasjon ved hover
- sticky søk / topbar på mobil

