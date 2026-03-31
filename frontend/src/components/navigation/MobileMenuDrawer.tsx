import CategoryIcon from '@mui/icons-material/Category';
import HomeIcon from '@mui/icons-material/Home';
import LocalOfferIcon from '@mui/icons-material/LocalOffer';
import NewReleasesIcon from '@mui/icons-material/NewReleases';
import PersonOutlineIcon from '@mui/icons-material/PersonOutline';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import {
  Box,
  Divider,
  Drawer,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Toolbar,
  Typography,
} from '@mui/material';
import { useCallback } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useShopUi } from '../../context/useShopUi';
import { DesignSwitcherNav } from './DesignSwitcher';

const drawerWidth = 288;

export function MobileMenuDrawer() {
  const { navOpen, setNavOpen, openCart } = useShopUi();
  const navigate = useNavigate();
  const { pathname } = useLocation();

  const go = useCallback(
    (path: string, hashId?: string) => {
      setNavOpen(false);
      navigate({ pathname: path, hash: hashId ? `#${hashId}` : undefined });
    },
    [navigate, setNavOpen],
  );

  const designBase = pathname.startsWith('/design-c')
    ? '/design-c'
    : pathname.startsWith('/design-b')
      ? '/design-b'
      : '/design-a';

  return (
    <Drawer
      anchor="left"
      open={navOpen}
      onClose={() => setNavOpen(false)}
      slotProps={{
        paper: {
          sx: {
            width: drawerWidth,
          },
        },
      }}
    >
      <Toolbar sx={{ px: 2 }}>
        <Typography variant="subtitle1" fontWeight={700} color="primary.main">
          Robert Shop
        </Typography>
      </Toolbar>
      <Divider />
      <Box sx={{ px: 2, py: 2 }}>
        <DesignSwitcherNav onNavigate={() => setNavOpen(false)} />
      </Box>
      <Divider />
      <List sx={{ px: 1, py: 1 }}>
        <ListItemButton onClick={() => go(designBase)} sx={{ borderRadius: 2, mb: 0.5 }}>
          <ListItemIcon>
            <HomeIcon />
          </ListItemIcon>
          <ListItemText primary="Hjem" />
        </ListItemButton>
        <ListItemButton onClick={() => go(designBase, 'categories')} sx={{ borderRadius: 2, mb: 0.5 }}>
          <ListItemIcon>
            <CategoryIcon />
          </ListItemIcon>
          <ListItemText primary="Kategorier" />
        </ListItemButton>
        <ListItemButton onClick={() => go(designBase, 'products')} sx={{ borderRadius: 2, mb: 0.5 }}>
          <ListItemIcon>
            <LocalOfferIcon />
          </ListItemIcon>
          <ListItemText primary="Tilbud" />
        </ListItemButton>
        <ListItemButton onClick={() => go(designBase, 'products')} sx={{ borderRadius: 2, mb: 0.5 }}>
          <ListItemIcon>
            <NewReleasesIcon />
          </ListItemIcon>
          <ListItemText primary="Nyheter" />
        </ListItemButton>
        <ListItemButton onClick={() => setNavOpen(false)} sx={{ borderRadius: 2, mb: 0.5 }}>
          <ListItemIcon>
            <PersonOutlineIcon />
          </ListItemIcon>
          <ListItemText primary="Min konto" />
        </ListItemButton>
        <ListItemButton
          onClick={() => {
            setNavOpen(false);
            openCart();
          }}
          sx={{ borderRadius: 2 }}
        >
          <ListItemIcon>
            <ShoppingCartIcon />
          </ListItemIcon>
          <ListItemText primary="Handlekurv" />
        </ListItemButton>
      </List>
      <Box sx={{ px: 2, py: 2, mt: 'auto' }}>
        <Typography variant="caption" color="text.secondary">
          Bytt design raskt nederst på skjermen (eller her i menyen)
        </Typography>
      </Box>
    </Drawer>
  );
}
