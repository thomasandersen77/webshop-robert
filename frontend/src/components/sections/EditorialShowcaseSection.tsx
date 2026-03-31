import { Box, Container, Grid, Paper, Stack, Typography } from '@mui/material';
import type { ReactNode } from 'react';

type Props = {
  eyebrow?: string;
  title: string;
  body: string;
  imageUrl: string;
  imagePosition?: 'left' | 'right';
  children?: ReactNode;
};

export function EditorialShowcaseSection({
  eyebrow,
  title,
  body,
  imageUrl,
  imagePosition = 'right',
  children,
}: Props) {
  const imageBlock = (
    <Paper
      elevation={0}
      sx={{
        borderRadius: 4,
        overflow: 'hidden',
        minHeight: { xs: 260, md: 360 },
        backgroundImage: `url(${imageUrl})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      }}
    />
  );

  const textBlock = (
    <Stack spacing={2} justifyContent="center" sx={{ py: { xs: 2, md: 4 } }}>
      {eyebrow ? (
        <Typography variant="overline" color="text.secondary" letterSpacing={2}>
          {eyebrow}
        </Typography>
      ) : null}
      <Typography
        variant="h4"
        component="h2"
        sx={{ fontFamily: 'Georgia, "Times New Roman", serif', fontWeight: 700, lineHeight: 1.15 }}
      >
        {title}
      </Typography>
      <Typography variant="body1" color="text.secondary">
        {body}
      </Typography>
      {children}
    </Stack>
  );

  return (
    <Box sx={{ py: { xs: 3, md: 5 } }}>
      <Container maxWidth="lg">
        <Grid container spacing={{ xs: 3, md: 4 }} alignItems="center">
          {imagePosition === 'left' ? (
            <>
              <Grid size={{ xs: 12, md: 6 }}>{imageBlock}</Grid>
              <Grid size={{ xs: 12, md: 6 }}>{textBlock}</Grid>
            </>
          ) : (
            <>
              <Grid size={{ xs: 12, md: 6 }} sx={{ order: { xs: 2, md: 1 } }}>
                {textBlock}
              </Grid>
              <Grid size={{ xs: 12, md: 6 }} sx={{ order: { xs: 1, md: 2 } }}>
                {imageBlock}
              </Grid>
            </>
          )}
        </Grid>
      </Container>
    </Box>
  );
}
