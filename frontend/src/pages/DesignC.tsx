import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import {
  Box,
  Chip,
  Container,
  Grid,
  Link,
  Paper,
  Stack,
  Typography,
} from '@mui/material';
import { useCallback } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { CategoryCard } from '../components/cards/CategoryCard';
import { ProductGridSection } from '../components/sections/ProductGridSection';
import { SearchBar } from '../components/search/SearchBar';
import { categories } from '../data/categories';
import { getNewProducts, products } from '../data/products';
import { useShopUi } from '../context/useShopUi';
import { useHashScroll } from '../hooks/useHashScroll';

export default function DesignC() {
  useHashScroll();
  const navigate = useNavigate();
  const { pathname } = useLocation();
  const { addToCart } = useShopUi();
  const newProducts = getNewProducts();

  const goHash = useCallback(
    (id: string) => {
      navigate({ pathname, hash: `#${id}` });
    },
    [navigate, pathname],
  );

  const goCategory = useCallback(
    (href: string) => {
      const h = href.startsWith('#') ? href : `#${href}`;
      navigate({ pathname, hash: h });
    },
    [navigate, pathname],
  );

  return (
    <Box
      sx={{
        pb: 'calc(16px + env(safe-area-inset-bottom, 0px))',
      }}
    >
      <Box
        sx={{
          pt: { xs: 2, md: 3 },
          pb: 2,
          borderBottom: (t) => `1px solid ${t.palette.divider}`,
          bgcolor: 'background.paper',
        }}
      >
        <Container maxWidth="lg">
          <Stack spacing={2}>
            <Typography variant="h5" fontWeight={800}>
              Hva ser du etter i dag?
            </Typography>
            <SearchBar size="medium" />
            <Typography variant="body2" color="text.secondary">
              Handle raskt blant våre mest populære kategorier — store klikkflater og enkel scrolling.
            </Typography>
            <Stack direction="row" spacing={1} sx={{ overflowX: 'auto', flexWrap: 'nowrap', pb: 0.5 }}>
              {categories.map((c) => (
                <Chip
                  key={c.id}
                  label={c.title}
                  onClick={() => goCategory(c.href)}
                  color="primary"
                  variant="outlined"
                  sx={{ flexShrink: 0, cursor: 'pointer', fontWeight: 600, borderRadius: 2 }}
                />
              ))}
            </Stack>
          </Stack>
        </Container>
      </Box>

      <Container maxWidth="lg" sx={{ py: { xs: 3, md: 4 } }} id="categories">
        <Stack direction="row" alignItems="center" justifyContent="space-between" sx={{ mb: 2 }}>
          <Typography variant="h6" fontWeight={800}>
            Kategorier
          </Typography>
          <Link
            component="button"
            type="button"
            variant="caption"
            onClick={() => goHash('products')}
            sx={{ cursor: 'pointer', fontWeight: 700, display: 'inline-flex', alignItems: 'center', gap: 0.5 }}
          >
            Se alle
            <ChevronRightIcon sx={{ fontSize: 18 }} />
          </Link>
        </Stack>
        <Grid container spacing={1.5}>
          {categories.map((cat) => (
            <Grid
              key={cat.id}
              id={cat.id}
              size={{ xs: 12, sm: 6, md: 3 }}
              sx={{ scrollMarginTop: { xs: 96, md: 104 } }}
            >
              <CategoryCard category={cat} imageHeight={160} />
            </Grid>
          ))}
        </Grid>
      </Container>

      <ProductGridSection
        id="products"
        title="Utvalgte produkter"
        subtitle="Enkel browse-opplevelse — optimalisert for mobil."
        products={products.slice(0, 8)}
        visual="c"
        onAddToCart={addToCart}
        onSeeAll={() => goHash('categories')}
        grid="dense"
        sx={{ bgcolor: 'background.default' }}
      />

      <ProductGridSection
        title="Nye produkter"
        subtitle="Ferske favoritter med merket «Ny»."
        products={newProducts.length ? newProducts : products.slice(0, 4)}
        visual="c"
        onAddToCart={addToCart}
        onSeeAll={() => goHash('products')}
        grid="dense"
      />

      <Container maxWidth="lg" sx={{ pb: 4 }}>
        <Paper
          elevation={0}
          sx={{
            p: 2,
            borderRadius: 3,
            bgcolor: 'secondary.light',
            border: (t) => `1px solid ${t.palette.divider}`,
          }}
        >
          <Typography variant="subtitle2" fontWeight={700}>
            Populære valg
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
            Tips: bruk design-bytteren nederst for å sammenligne A, B og C.
          </Typography>
        </Paper>
      </Container>
    </Box>
  );
}
