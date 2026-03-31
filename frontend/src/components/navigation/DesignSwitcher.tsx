import { Box, Button, Stack, Typography, useMediaQuery } from '@mui/material';
import type { Theme } from '@mui/material/styles';
import { NavLink } from 'react-router-dom';

const designs = [
  { path: '/design-a', label: 'A' },
  { path: '/design-b', label: 'B' },
  { path: '/design-c', label: 'C' },
] as const;

const navButtonSx = {
  borderRadius: 999,
  fontWeight: 700,
  minWidth: 40,
  px: 1.25,
  color: 'text.primary',
  '&.active': {
    bgcolor: 'primary.main',
    color: 'primary.contrastText',
    '&:hover': { bgcolor: 'primary.dark' },
  },
} as const;

/** Desktop topbar — compact NavLinks */
export function DesignSwitcherInline() {
  return (
    <Stack direction="row" alignItems="center" spacing={0.5} sx={{ flexShrink: 0 }}>
      <Typography
        variant="caption"
        color="text.secondary"
        sx={{ display: { xs: 'none', lg: 'block' }, mr: 0.5 }}
      >
        Design
      </Typography>
      {designs.map((d) => (
        <Button
          key={d.path}
          component={NavLink}
          to={d.path}
          size="small"
          sx={navButtonSx}
        >
          {d.label}
        </Button>
      ))}
    </Stack>
  );
}

/** Drawer / narrow contexts — full labels */
export function DesignSwitcherNav({ onNavigate }: { onNavigate?: () => void }) {
  return (
    <Stack direction="column" spacing={0.75} sx={{ alignItems: 'stretch' }}>
      <Typography variant="caption" color="text.secondary" fontWeight={600}>
        Bytt design
      </Typography>
      <Stack direction="row" spacing={0.75} flexWrap="wrap" useFlexGap>
        {[
          { path: '/design-a', label: 'Design A' },
          { path: '/design-b', label: 'Design B' },
          { path: '/design-c', label: 'Design C' },
        ].map((d) => (
          <Button
            key={d.path}
            component={NavLink}
            to={d.path}
            size="small"
            onClick={() => onNavigate?.()}
            sx={{ ...navButtonSx, flex: '1 1 auto', minWidth: 100 }}
          >
            {d.label}
          </Button>
        ))}
      </Stack>
    </Stack>
  );
}

/** Floating switcher for mobile */
export function DesignSwitcherFloating() {
  const hideOnDesktop = useMediaQuery((t: Theme) => t.breakpoints.up('md'));

  if (hideOnDesktop) return null;

  return (
    <Box
      sx={{
        position: 'fixed',
        bottom: 'calc(16px + env(safe-area-inset-bottom, 0px))',
        left: '50%',
        transform: 'translateX(-50%)',
        zIndex: (t) => t.zIndex.tooltip,
        px: 1,
        maxWidth: '100%',
      }}
    >
      <Stack
        direction="row"
        alignItems="center"
        spacing={1}
        sx={{
          bgcolor: 'background.paper',
          borderRadius: 999,
          px: 1.5,
          py: 1,
          boxShadow: 4,
          border: (t) => `1px solid ${t.palette.divider}`,
        }}
      >
        <Typography variant="caption" color="text.secondary" sx={{ pr: 0.5, flexShrink: 0 }}>
          Design
        </Typography>
        <Stack direction="row" spacing={0.5}>
          {designs.map((d) => (
            <Button
              key={d.path}
              component={NavLink}
              to={d.path}
              size="small"
              sx={navButtonSx}
            >
              {d.label}
            </Button>
          ))}
        </Stack>
      </Stack>
    </Box>
  );
}
