import { useCallback } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { HeroSection } from '../components/sections/HeroSection';
import { CategoryGridSection } from '../components/sections/CategoryGridSection';
import { ProductGridSection } from '../components/sections/ProductGridSection';
import { PromoBanner } from '../components/sections/PromoBanner';
import { categories } from '../data/categories';
import { products } from '../data/products';
import { useShopUi } from '../context/useShopUi';
import { useHashScroll } from '../hooks/useHashScroll';

export default function DesignA() {
  useHashScroll();
  const navigate = useNavigate();
  const { pathname } = useLocation();
  const { addToCart } = useShopUi();

  const goHash = useCallback(
    (id: string) => {
      navigate({ pathname, hash: `#${id}` });
    },
    [navigate, pathname],
  );

  return (
    <>
      <HeroSection
        title="Moderne produkter for hjem, hverdag og livsstil"
        subtitle="Utforsk et lyst og nøye kuratert vareutvalg — bygget for rask orientering på mobil og desktop."
        backgroundImage={categories[1].image}
        primaryCta={{ label: 'Utforsk kategorier', onClick: () => goHash('categories') }}
        secondaryCta={{ label: 'Utvalgte produkter', onClick: () => goHash('products') }}
      />
      <CategoryGridSection
        id="categories"
        title="Kategorier"
        categories={categories}
        onSeeAll={() => goHash('products')}
      />
      <ProductGridSection
        id="products"
        title="Utvalgte produkter"
        subtitle="Populære valg akkurat nå — klikk for å legge i handlekurv."
        products={products.slice(0, 8)}
        visual="a"
        onAddToCart={addToCart}
        onSeeAll={() => goHash('categories')}
        sx={{ bgcolor: 'background.paper' }}
      />
      <PromoBanner
        title="Gratis frakt over 500 kr denne måneden"
        subtitle="Enkel checkout og tydelig levering — akkurat slik kunder forventer i 2026."
        ctaLabel="Se tilbud"
        onCta={() => goHash('products')}
      />
    </>
  );
}
