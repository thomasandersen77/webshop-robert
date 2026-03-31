import { Box, Button, Divider, Stack, Typography } from '@mui/material';
import { formatNok } from '../../utils/format';

type Props = {
  subtotal: number;
  onCheckout: () => void;
  onContinue: () => void;
};

export function CartSummary({ subtotal, onCheckout, onContinue }: Props) {
  return (
    <Box sx={{ mt: 'auto' }}>
      <Divider />
      <Box sx={{ p: 2 }}>
        <Stack
          direction="row"
          justifyContent="space-between"
          alignItems="baseline"
          sx={{ mb: 2 }}
        >
          <Typography color="text.secondary" fontWeight={700}>
            Delsum
          </Typography>
          <Typography variant="h6" fontWeight={800}>
            {formatNok(subtotal)}
          </Typography>
        </Stack>
        <Stack spacing={1.5}>
          <Button variant="contained" color="primary" fullWidth size="large" onClick={onCheckout}>
            Gå til betaling
          </Button>
          <Button variant="outlined" color="inherit" fullWidth onClick={onContinue}>
            Fortsett å handle
          </Button>
        </Stack>
      </Box>
    </Box>
  );
}
