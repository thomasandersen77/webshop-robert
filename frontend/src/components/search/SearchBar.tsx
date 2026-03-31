import SearchIcon from '@mui/icons-material/Search';
import { InputAdornment, TextField } from '@mui/material';
import type { TextFieldProps } from '@mui/material/TextField';

const placeholder = 'Søk etter produkter';

type Props = {
  fullWidth?: boolean;
  size?: TextFieldProps['size'];
  sx?: TextFieldProps['sx'];
};

export function SearchBar({ fullWidth = true, size = 'small', sx }: Props) {
  return (
    <TextField
      placeholder={placeholder}
      aria-label={placeholder}
      size={size}
      fullWidth={fullWidth}
      sx={[
        {
          '& .MuiOutlinedInput-root': {
            borderRadius: 999,
            backgroundColor: 'background.paper',
          },
        },
        ...(Array.isArray(sx) ? sx : sx ? [sx] : []),
      ]}
      slotProps={{
        input: {
          startAdornment: (
            <InputAdornment position="start">
              <SearchIcon color="action" fontSize={size === 'medium' ? 'medium' : 'small'} />
            </InputAdornment>
          ),
        },
      }}
    />
  );
}
