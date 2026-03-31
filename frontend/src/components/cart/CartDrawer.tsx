import CloseIcon from '@mui/icons-material/Close';
import {
  Box,
  Divider,
  Drawer,
  IconButton,
  Stack,
  Toolbar,
  Typography,
} from '@mui/material';
import { useShopUi } from '../../context/useShopUi';
import { formatNok } from '../../utils/format';
import { CartSummary } from './CartSummary';

export function CartDrawer() {
  const { cartOpen, setCartOpen, cartItems } = useShopUi();

  const subtotal = cartItems.reduce((sum, item) => sum + item.price * item.quantity, 0);

  return (
    <Drawer
      anchor="right"
      open={cartOpen}
      onClose={() => setCartOpen(false)}
      slotProps={{
        paper: {
          sx: {
            width: { xs: '100%', sm: 400 },
            maxWidth: '100%',
            borderTopLeftRadius: { sm: 24 },
            borderBottomLeftRadius: { sm: 24 },
            display: 'flex',
            flexDirection: 'column',
          },
        },
      }}
    >
      <Toolbar
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          flexShrink: 0,
        }}
      >
        <Typography variant="h6" fontWeight={800}>
          Handlekurv
        </Typography>
        <IconButton onClick={() => setCartOpen(false)} aria-label="Lukk">
          <CloseIcon />
        </IconButton>
      </Toolbar>
      <Divider />
      <Stack spacing={2} sx={{ p: 2, flex: 1, overflow: 'auto' }}>
        {cartItems.map((item) => (
          <Stack
            key={item.id}
            direction="row"
            spacing={1.5}
            sx={{
              p: 1.5,
              borderRadius: 3,
              bgcolor: 'background.default',
            }}
          >
            <Box
              component="img"
              src={item.image}
              alt=""
              sx={{
                width: 72,
                height: 72,
                objectFit: 'cover',
                borderRadius: 2,
                flexShrink: 0,
              }}
            />
            <Box sx={{ flex: 1, minWidth: 0 }}>
              <Typography variant="subtitle2" fontWeight={700} noWrap>
                {item.title}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Antall: {item.quantity}
              </Typography>
              <Box>
                <Typography variant="body2" fontWeight={700}>
                  {formatNok(item.price * item.quantity)}
                </Typography>
                {item.quantity > 1 ? (
                  <Typography variant="caption" color="text.secondary">
                    {formatNok(item.price)} × {item.quantity}
                  </Typography>
                ) : null}
              </Box>
            </Box>
          </Stack>
        ))}
      </Stack>
      <CartSummary
        subtotal={subtotal}
        onCheckout={() => setCartOpen(false)}
        onContinue={() => setCartOpen(false)}
      />
    </Drawer>
  );
}
