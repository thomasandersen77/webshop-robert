import { Box, Container, Divider, Link, Stack, Typography } from '@mui/material';

export function Footer() {
  return (
    <Box
      component="footer"
      sx={{
        mt: 'auto',
        py: { xs: 4, md: 5 },
        bgcolor: 'background.paper',
        borderTop: (t) => `1px solid ${t.palette.divider}`,
      }}
    >
      <Container maxWidth="lg">
        <Stack spacing={2}>
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} justifyContent="space-between">
            <Typography variant="subtitle1" fontWeight={800} color="primary.main">
              Robert Shop
            </Typography>
            <Stack direction="row" spacing={2} flexWrap="wrap" useFlexGap>
              <Link href="#" variant="body2" color="text.secondary" underline="hover">
                Kundeservice
              </Link>
              <Link href="#" variant="body2" color="text.secondary" underline="hover">
                Frakt
              </Link>
              <Link href="#" variant="body2" color="text.secondary" underline="hover">
                Personvern
              </Link>
            </Stack>
          </Stack>
          <Divider />
          <Typography variant="caption" color="text.secondary">
            © {new Date().getFullYear()} Robert Shop · Proof of concept
          </Typography>
        </Stack>
      </Container>
    </Box>
  );
}
