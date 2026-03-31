import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import FavoriteIcon from '@mui/icons-material/Favorite';
import {
  Box,
  Button,
  Card,
  CardContent,
  CardMedia,
  Chip,
  IconButton,
  Stack,
  Typography,
} from '@mui/material';
import { useState } from 'react';
import type { ProductBadge, ProductItem } from '../../types/shop';
import { formatNok } from '../../utils/format';

type Visual = 'a' | 'b' | 'c';

const badgeColor = (b: ProductBadge): 'default' | 'primary' | 'secondary' => {
  if (b === 'Ny') return 'primary';
  if (b === 'Bestselger') return 'secondary';
  return 'default';
};

type Props = {
  product: ProductItem;
  visual: Visual;
  onAddToCart?: (product: ProductItem) => void;
};

export function ProductCard({ product, visual, onAddToCart }: Props) {
  const [fav, setFav] = useState(false);

  const mediaHeight =
    visual === 'b'
      ? { xs: 220, sm: 260 }
      : visual === 'c'
        ? { xs: 140, sm: 160 }
        : { xs: 200, sm: 220 };

  return (
    <Card
      onClick={() => onAddToCart?.(product)}
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        cursor: 'pointer',
        transition: 'transform 0.25s ease, box-shadow 0.25s ease',
        '&:hover': {
          transform: 'translateY(-4px) scale(1.01)',
          boxShadow: (t) => t.shadows[8],
        },
        '&:focus-within': {
          outline: (t) => `2px solid ${t.palette.primary.main}`,
          outlineOffset: 2,
        },
      }}
    >
      <Box sx={{ position: 'relative' }}>
        <CardMedia
          component="img"
          image={product.image}
          alt=""
          sx={{
            objectFit: 'cover',
            height: mediaHeight,
            borderBottomLeftRadius: 0,
            borderBottomRightRadius: 0,
          }}
        />
        <IconButton
          size="small"
          aria-label="Favoritt"
          onClick={(e) => {
            e.stopPropagation();
            setFav((v) => !v);
          }}
          sx={{
            position: 'absolute',
            top: 8,
            right: 8,
            bgcolor: 'background.paper',
            boxShadow: 1,
            '&:hover': { bgcolor: 'background.paper' },
          }}
        >
          {fav ? (
            <FavoriteIcon fontSize="small" color="primary" />
          ) : (
            <FavoriteBorderIcon fontSize="small" />
          )}
        </IconButton>
        {product.badge ? (
          <Chip
            label={product.badge}
            size="small"
            color={badgeColor(product.badge)}
            sx={{
              position: 'absolute',
              left: 8,
              top: 8,
              fontWeight: 700,
            }}
          />
        ) : null}
      </Box>
      <CardContent
        sx={{
          flex: 1,
          display: 'flex',
          flexDirection: 'column',
          gap: 0.5,
          pt: visual === 'b' ? 2.5 : 2,
          pb: 2,
        }}
      >
        <Typography
          variant={visual === 'b' ? 'subtitle1' : 'subtitle2'}
          fontWeight={600}
          sx={visual === 'b' ? { fontFamily: 'Georgia, "Times New Roman", serif' } : undefined}
        >
          {product.title}
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ minHeight: 40 }}>
          {product.shortDescription}
        </Typography>
        <Stack direction="row" alignItems="baseline" spacing={1} sx={{ mt: 'auto', flexWrap: 'wrap' }}>
          <Typography variant="h6" color="primary" fontWeight={700}>
            {formatNok(product.price)}
          </Typography>
          {product.oldPrice ? (
            <Typography
              variant="body2"
              color="text.secondary"
              sx={{ textDecoration: 'line-through' }}
            >
              {formatNok(product.oldPrice)}
            </Typography>
          ) : null}
        </Stack>
        <Button
          variant={visual === 'c' ? 'contained' : 'outlined'}
          color="primary"
          fullWidth
          onClick={(e) => {
            e.stopPropagation();
            onAddToCart?.(product);
          }}
        >
          Legg i handlekurv
        </Button>
      </CardContent>
    </Card>
  );
}
