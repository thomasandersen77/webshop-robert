import { createTheme } from '@mui/material/styles';

export const theme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: '#234a67',
      light: '#3a6283',
      dark: '#163049',
      contrastText: '#ffffff',
    },
    secondary: {
      main: '#c9b8a6',
      light: '#dfd3c6',
      dark: '#9f8b75',
      contrastText: '#1c2430',
    },
    background: {
      default: '#f7f5f2',
      paper: '#ffffff',
    },
    text: {
      primary: '#1c2430',
      secondary: '#5f6b76',
    },
    divider: 'rgba(28, 36, 48, 0.08)',
  },
  shape: {
    borderRadius: 20,
  },
  typography: {
    fontFamily: '"Inter", "DM Sans", "Roboto", "Helvetica", "Arial", sans-serif',
    h1: {
      fontWeight: 800,
      letterSpacing: '-0.04em',
    },
    h2: {
      fontWeight: 800,
      letterSpacing: '-0.03em',
    },
    h3: {
      fontWeight: 700,
      letterSpacing: '-0.02em',
    },
    h4: {
      fontWeight: 700,
      letterSpacing: '-0.02em',
    },
    h5: {
      fontWeight: 700,
    },
    h6: {
      fontWeight: 700,
    },
    button: {
      fontWeight: 700,
      textTransform: 'none',
    },
  },
  shadows: [
    'none',
    '0px 2px 8px rgba(28, 36, 48, 0.06)',
    '0px 4px 16px rgba(28, 36, 48, 0.08)',
    '0px 6px 20px rgba(28, 36, 48, 0.09)',
    '0px 8px 24px rgba(28, 36, 48, 0.1)',
    '0px 10px 28px rgba(28, 36, 48, 0.11)',
    '0px 12px 32px rgba(28, 36, 48, 0.12)',
    '0px 14px 36px rgba(28, 36, 48, 0.13)',
    '0px 16px 40px rgba(28, 36, 48, 0.14)',
    '0px 18px 44px rgba(28, 36, 48, 0.15)',
    '0px 20px 48px rgba(28, 36, 48, 0.16)',
    '0px 22px 52px rgba(28, 36, 48, 0.17)',
    '0px 24px 56px rgba(28, 36, 48, 0.18)',
    '0px 26px 60px rgba(28, 36, 48, 0.19)',
    '0px 28px 64px rgba(28, 36, 48, 0.2)',
    '0px 30px 68px rgba(28, 36, 48, 0.2)',
    '0px 32px 72px rgba(28, 36, 48, 0.2)',
    '0px 34px 76px rgba(28, 36, 48, 0.2)',
    '0px 36px 80px rgba(28, 36, 48, 0.21)',
    '0px 38px 84px rgba(28, 36, 48, 0.21)',
    '0px 40px 88px rgba(28, 36, 48, 0.21)',
    '0px 42px 92px rgba(28, 36, 48, 0.21)',
    '0px 44px 96px rgba(28, 36, 48, 0.21)',
    '0px 46px 100px rgba(28, 36, 48, 0.22)',
    '0px 48px 104px rgba(28, 36, 48, 0.22)',
  ],
  components: {
    MuiCssBaseline: {
      styleOverrides: {
        body: {
          margin: 0,
          backgroundColor: '#f7f5f2',
        },
        '*': {
          boxSizing: 'border-box',
        },
        img: {
          display: 'block',
          maxWidth: '100%',
        },
        a: {
          color: 'inherit',
          textDecoration: 'none',
        },
      },
    },
    MuiButton: {
      defaultProps: {
        disableElevation: true,
      },
      styleOverrides: {
        root: {
          minHeight: 48,
          borderRadius: 999,
          paddingLeft: 18,
          paddingRight: 18,
        },
        containedPrimary: {
          boxShadow: '0 10px 24px rgba(35, 74, 103, 0.18)',
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 24,
          boxShadow: '0 10px 30px rgba(18, 28, 45, 0.06)',
          border: '1px solid rgba(28, 36, 48, 0.06)',
          overflow: 'hidden',
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          backgroundColor: 'rgba(255,255,255,0.78)',
          backdropFilter: 'blur(16px)',
          borderBottom: '1px solid rgba(28, 36, 48, 0.08)',
          color: '#1c2430',
          boxShadow: 'none',
        },
      },
    },
    MuiChip: {
      styleOverrides: {
        root: {
          borderRadius: 999,
          fontWeight: 600,
        },
      },
    },
    MuiDrawer: {
      styleOverrides: {
        paper: {
          borderTopLeftRadius: 24,
          borderBottomLeftRadius: 24,
          borderTopRightRadius: 24,
          borderBottomRightRadius: 24,
        },
      },
    },
    MuiTextField: {
      defaultProps: {
        variant: 'outlined',
      },
    },
    MuiPaper: {
      styleOverrides: {
        root: {
          backgroundImage: 'none',
        },
      },
    },
  },
});
