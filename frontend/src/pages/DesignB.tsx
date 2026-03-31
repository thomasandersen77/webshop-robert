import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import {
  Box,
  Button,
  Container,
  Grid,
  Link,
  Paper,
  Stack,
  Typography,
} from '@mui/material';
import { useCallback } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { ProductCard } from '../components/cards/ProductCard';
import { EditorialShowcaseSection } from '../components/sections/EditorialShowcaseSection';
import { ProductGridSection } from '../components/sections/ProductGridSection';
import { categories } from '../data/categories';
import { products } from '../data/products';
import { useShopUi } from '../context/useShopUi';
import { useHashScroll } from '../hooks/useHashScroll';

export default function DesignB() {
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

  const goCategory = useCallback(
    (href: string) => {
      const h = href.startsWith('#') ? href : `#${href}`;
      navigate({ pathname, hash: h });
    },
    [navigate, pathname],
  );

  const [c0, c1, c2, c3] = categories;

  return (
    <Box>
      <Container maxWidth="lg" sx={{ pt: { xs: 3, md: 5 }, pb: 1 }}>
        <Typography variant="overline" color="text.secondary" sx={{ letterSpacing: 3, fontWeight: 600 }}>
          Robert Shop · Editorial
        </Typography>
        <Typography
          variant="h2"
          component="h1"
          sx={{
            mt: 1,
            mb: 2,
            fontWeight: 700,
            fontFamily: 'Georgia, "Times New Roman", serif',
            lineHeight: 1.08,
            maxWidth: 900,
          }}
        >
          Utforsk produkter med et rolig, moderne uttrykk
        </Typography>
        <Typography variant="h6" color="text.secondary" sx={{ maxWidth: 720, fontWeight: 400 }}>
          Designet for kunder som vil finne frem raskt — store bildeflater, asymmetrisk rytme og produkter som
          innholdskort.
        </Typography>
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} sx={{ mt: 3 }}>
          <Button variant="contained" size="large" onClick={() => goHash('categories')}>
            Utforsk kategorier
          </Button>
          <Button variant="text" size="large" onClick={() => goHash('products')} endIcon={<ChevronRightIcon />}>
            Utvalgte produkter
          </Button>
        </Stack>
      </Container>

      <EditorialShowcaseSection
        eyebrow="Livsstil"
        title="En butikk som føles som et magasin"
        body="Vi kombinerer tydelig navigasjon med varme materialer og rolig typografi — uten å miste fart på mobil."
        imageUrl={c1.image}
        imagePosition="right"
      />

      <Box id="categories" sx={{ py: { xs: 2, md: 3 } }}>
        <Container maxWidth="lg">
          <Grid container spacing={{ xs: 2, md: 3 }}>
            <Grid size={{ xs: 12, md: 7 }}>
              <Paper
                id={c0.id}
                onClick={() => goCategory(c0.href)}
                sx={{
                  borderRadius: 4,
                  overflow: 'hidden',
                  cursor: 'pointer',
                  minHeight: { xs: 280, md: 420 },
                  scrollMarginTop: { xs: 96, md: 104 },
                  backgroundImage: `linear-gradient(180deg, rgba(0,0,0,0.08), rgba(0,0,0,0.55)), url(${c0.image})`,
                  backgroundSize: 'cover',
                  backgroundPosition: 'center',
                  display: 'flex',
                  alignItems: 'flex-end',
                  transition: 'transform 0.25s ease',
                  '&:hover': { transform: 'scale(1.01)' },
                }}
              >
                <Box sx={{ p: 3, color: 'common.white' }}>
                  <Typography variant="h4" fontWeight={700} sx={{ fontFamily: 'Georgia, serif' }}>
                    {c0.title}
                  </Typography>
                  <Typography variant="body2" sx={{ opacity: 0.95 }}>
                    {c0.description}
                  </Typography>
                </Box>
              </Paper>
            </Grid>
            <Grid size={{ xs: 12, md: 5 }}>
              <Stack spacing={{ xs: 2, md: 3 }} sx={{ height: '100%' }}>
                <Paper
                  id={c1.id}
                  onClick={() => goCategory(c1.href)}
                  sx={{
                    borderRadius: 4,
                    overflow: 'hidden',
                    cursor: 'pointer',
                    flex: 1,
                    minHeight: { xs: 200, md: 'auto' },
                    scrollMarginTop: { xs: 96, md: 104 },
                    backgroundImage: `linear-gradient(135deg, rgba(42,111,122,0.45), rgba(0,0,0,0.45)), url(${c1.image})`,
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                    display: 'flex',
                    alignItems: 'flex-end',
                    transition: 'transform 0.25s ease',
                    '&:hover': { transform: 'translateY(-2px)' },
                  }}
                >
                  <Box sx={{ p: 2.5, color: 'common.white' }}>
                    <Typography variant="h5" fontWeight={700}>
                      {c1.title}
                    </Typography>
                  </Box>
                </Paper>
                <Grid container spacing={2}>
                  <Grid size={{ xs: 6 }}>
                    <Paper
                      id={c2.id}
                      onClick={() => goCategory(c2.href)}
                      sx={{
                        borderRadius: 3,
                        overflow: 'hidden',
                        cursor: 'pointer',
                        height: 160,
                        scrollMarginTop: { xs: 96, md: 104 },
                        backgroundImage: `url(${c2.image})`,
                        backgroundSize: 'cover',
                        backgroundPosition: 'center',
                        display: 'flex',
                        alignItems: 'flex-end',
                        '&:hover': { boxShadow: 8 },
                      }}
                    >
                      <Box sx={{ p: 1.5, width: '100%', bgcolor: 'rgba(255,255,255,0.94)' }}>
                        <Typography variant="subtitle1" fontWeight={700}>
                          {c2.title}
                        </Typography>
                      </Box>
                    </Paper>
                  </Grid>
                  <Grid size={{ xs: 6 }}>
                    <Paper
                      id={c3.id}
                      onClick={() => goCategory(c3.href)}
                      sx={{
                        borderRadius: 3,
                        overflow: 'hidden',
                        cursor: 'pointer',
                        height: 160,
                        scrollMarginTop: { xs: 96, md: 104 },
                        backgroundImage: `url(${c3.image})`,
                        backgroundSize: 'cover',
                        backgroundPosition: 'center',
                        display: 'flex',
                        alignItems: 'flex-end',
                        '&:hover': { boxShadow: 8 },
                      }}
                    >
                      <Box sx={{ p: 1.5, width: '100%', bgcolor: 'rgba(255,255,255,0.94)' }}>
                        <Typography variant="subtitle1" fontWeight={700}>
                          {c3.title}
                        </Typography>
                      </Box>
                    </Paper>
                  </Grid>
                </Grid>
              </Stack>
            </Grid>
          </Grid>
        </Container>
      </Box>

      <Box sx={{ bgcolor: 'background.paper', py: { xs: 2, md: 4 } }}>
        <Container maxWidth="lg">
          <Typography variant="h5" sx={{ fontFamily: 'Georgia, serif', fontWeight: 700, mb: 2 }}>
            Utvalgt nå
          </Typography>
          <Grid container spacing={{ xs: 2, md: 3 }}>
            {products.slice(0, 2).map((p) => (
              <Grid key={p.id} size={{ xs: 12, md: 6 }}>
                <ProductCard product={p} visual="b" onAddToCart={addToCart} />
              </Grid>
            ))}
          </Grid>
        </Container>
      </Box>

      <ProductGridSection
        id="products"
        title="Utvalgte produkter"
        subtitle="Integrert som redaksjonelle kort — perfekt for å sammenligne uttrykk og pris."
        products={products.slice(2)}
        visual="b"
        onAddToCart={addToCart}
        onSeeAll={() => goHash('categories')}
      />

      <Container maxWidth="lg" sx={{ pb: 4 }}>
        <Link
          component="button"
          type="button"
          variant="body2"
          onClick={() => goHash('categories')}
          sx={{ cursor: 'pointer', fontWeight: 600, display: 'inline-flex', alignItems: 'center', gap: 0.5 }}
        >
          Se alle kategorier
          <ChevronRightIcon fontSize="small" />
        </Link>
      </Container>
    </Box>
  );
}
