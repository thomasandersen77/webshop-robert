import MenuIcon from '@mui/icons-material/Menu';
import SearchIcon from '@mui/icons-material/Search';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import {
  AppBar,
  Badge,
  Box,
  Collapse,
  IconButton,
  Toolbar,
  Typography,
  useScrollTrigger,
} from '@mui/material';
import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useShopUi } from '../../context/useShopUi';
import { SearchBar } from '../search/SearchBar';

export function MobileTopbar() {
  const { toggleNav, openCart, cartItemCount } = useShopUi();
  const navigate = useNavigate();
  const { pathname } = useLocation();
  const [searchOpen, setSearchOpen] = useState(false);
  const trigger = useScrollTrigger({ disableHysteresis: true, threshold: 4 });

  const homePath = pathname.startsWith('/design-c')
    ? '/design-c'
    : pathname.startsWith('/design-b')
      ? '/design-b'
      : '/design-a';

  return (
    <AppBar
      position="sticky"
      color="inherit"
      elevation={trigger ? 2 : 0}
      sx={{
        top: 0,
        zIndex: (t) => t.zIndex.drawer + 1,
        borderBottom: (t) => (trigger ? 'none' : `1px solid ${t.palette.divider}`),
      }}
    >
      <Toolbar sx={{ gap: 1, minHeight: 56 }}>
        <IconButton edge="start" color="inherit" aria-label="Åpne meny" onClick={toggleNav}>
          <MenuIcon />
        </IconButton>
        <Box
          onClick={() => navigate(homePath)}
          sx={{ flex: 1, textAlign: 'center', cursor: 'pointer', minWidth: 0 }}
        >
          <Typography variant="subtitle1" fontWeight={800} color="primary.main" noWrap>
            Robert Shop
          </Typography>
        </Box>
        <IconButton
          color="inherit"
          aria-label="Søk"
          onClick={() => setSearchOpen((o) => !o)}
        >
          <SearchIcon />
        </IconButton>
        <IconButton color="inherit" aria-label="Handlekurv" onClick={openCart}>
          <Badge badgeContent={cartItemCount} color="primary" invisible={cartItemCount === 0} max={99}>
            <ShoppingCartIcon />
          </Badge>
        </IconButton>
      </Toolbar>
      <Collapse in={searchOpen}>
        <Box sx={{ px: 2, pb: 1.5 }}>
          <SearchBar size="small" />
        </Box>
      </Collapse>
    </AppBar>
  );
}
