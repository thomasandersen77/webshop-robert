import { Box, useMediaQuery } from '@mui/material';
import type { Theme } from '@mui/material/styles';
import { Outlet } from 'react-router-dom';
import { CartDrawer } from '../cart/CartDrawer';
import { DesignSwitcherFloating } from '../navigation/DesignSwitcher';
import { MobileMenuDrawer } from '../navigation/MobileMenuDrawer';
import { DesktopTopbar } from './DesktopTopbar';
import { Footer } from './Footer';
import { MobileTopbar } from './MobileTopbar';

export function AppShell() {
  const isMdUp = useMediaQuery((t: Theme) => t.breakpoints.up('md'));

  return (
    <Box
      sx={{
        minHeight: '100dvh',
        display: 'flex',
        flexDirection: 'column',
        bgcolor: 'background.default',
        pb: { xs: 'calc(72px + env(safe-area-inset-bottom, 0px))', md: 0 },
      }}
    >
      {isMdUp ? <DesktopTopbar /> : <MobileTopbar />}
      <MobileMenuDrawer />
      <CartDrawer />
      <Box component="main" sx={{ flex: 1 }}>
        <Outlet />
      </Box>
      <Footer />
      <DesignSwitcherFloating />
    </Box>
  );
}
