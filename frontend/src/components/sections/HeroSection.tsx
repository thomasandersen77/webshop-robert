import { Box, Button, Container, Stack, Typography } from '@mui/material';
import type { ReactNode } from 'react';

type Props = {
  title: string;
  subtitle: string;
  backgroundImage: string;
  primaryCta: { label: string; onClick: () => void };
  secondaryCta?: { label: string; onClick: () => void };
  /** subtle gradient overlay */
  overlay?: 'left' | 'center' | 'bottom';
  minHeight?: { xs: number; md: number };
  children?: ReactNode;
};

export function HeroSection({
  title,
  subtitle,
  backgroundImage,
  primaryCta,
  secondaryCta,
  overlay = 'left',
  minHeight = { xs: 420, md: 520 },
  children,
}: Props) {
  const gradient =
    overlay === 'center'
      ? `linear-gradient(135deg, rgba(26,58,64,0.82) 0%, rgba(42,111,122,0.5) 50%, rgba(244,243,239,0.15) 100%), url(${backgroundImage})`
      : overlay === 'bottom'
        ? `linear-gradient(180deg, rgba(244,243,239,0.05) 0%, rgba(26,58,64,0.88) 100%), url(${backgroundImage})`
        : `linear-gradient(90deg, rgba(26,58,64,0.88) 0%, rgba(42,111,122,0.42) 48%, rgba(244,243,239,0.08) 100%), url(${backgroundImage})`;

  return (
    <Box
      sx={{
        position: 'relative',
        color: 'common.white',
        minHeight,
        display: 'flex',
        alignItems: 'flex-end',
        pb: { xs: 5, md: 8 },
        backgroundImage: gradient,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      }}
    >
      <Container maxWidth="lg">
        <Stack spacing={2} sx={{ maxWidth: { md: '58%' } }}>
          <Typography variant="overline" sx={{ opacity: 0.92, letterSpacing: 3 }}>
            Robert Shop
          </Typography>
          <Typography variant="h3" component="h1" fontWeight={800} sx={{ lineHeight: 1.12 }}>
            {title}
          </Typography>
          <Typography variant="body1" sx={{ opacity: 0.96, maxWidth: 520 }}>
            {subtitle}
          </Typography>
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} sx={{ pt: 1 }}>
            <Button
              variant="contained"
              color="secondary"
              size="large"
              onClick={primaryCta.onClick}
              sx={{ color: 'text.primary', px: 3 }}
            >
              {primaryCta.label}
            </Button>
            {secondaryCta ? (
              <Button
                variant="outlined"
                size="large"
                onClick={secondaryCta.onClick}
                sx={{
                  borderColor: 'rgba(255,255,255,0.72)',
                  color: 'common.white',
                  '&:hover': {
                    borderColor: 'common.white',
                    bgcolor: 'rgba(255,255,255,0.06)',
                  },
                }}
              >
                {secondaryCta.label}
              </Button>
            ) : null}
          </Stack>
          {children}
        </Stack>
      </Container>
    </Box>
  );
}
