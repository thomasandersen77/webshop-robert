import { Box, Button, Container, Stack, Typography } from '@mui/material';

type Props = {
  title: string;
  subtitle: string;
  ctaLabel: string;
  onCta: () => void;
};

export function PromoBanner({ title, subtitle, ctaLabel, onCta }: Props) {
  return (
    <Box
      sx={{
        py: { xs: 4, md: 5 },
        background: (t) =>
          `linear-gradient(110deg, ${t.palette.primary.dark} 0%, ${t.palette.primary.main} 42%, ${t.palette.secondary.light} 100%)`,
        color: 'primary.contrastText',
      }}
    >
      <Container maxWidth="lg">
        <Stack
          direction={{ xs: 'column', md: 'row' }}
          spacing={3}
          alignItems={{ xs: 'flex-start', md: 'center' }}
          justifyContent="space-between"
        >
          <Box sx={{ maxWidth: 720 }}>
            <Typography variant="h5" fontWeight={800}>
              {title}
            </Typography>
            <Typography variant="body2" sx={{ mt: 1, opacity: 0.95 }}>
              {subtitle}
            </Typography>
          </Box>
          <Button
            variant="contained"
            color="secondary"
            size="large"
            onClick={onCta}
            sx={{ color: 'text.primary', flexShrink: 0 }}
          >
            {ctaLabel}
          </Button>
        </Stack>
      </Container>
    </Box>
  );
}
