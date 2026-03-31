import { Box, Card, CardContent, Chip, Typography } from '@mui/material';
import { useLocation, useNavigate } from 'react-router-dom';
import type { CategoryItem } from '../../types/shop';

type Props = {
  category: CategoryItem;
  /** Overrides default navigation to category href on current route */
  onClick?: () => void;
  imageHeight?: number | { xs: number; sm?: number };
};

export function CategoryCard({ category, onClick, imageHeight = 200 }: Props) {
  const navigate = useNavigate();
  const { pathname } = useLocation();

  const handleClick = () => {
    if (onClick) {
      onClick();
      return;
    }
    const h = category.href.startsWith('#') ? category.href : `#${category.href}`;
    navigate({ pathname, hash: h });
  };

  return (
    <Card
      role="button"
      tabIndex={0}
      onClick={handleClick}
      onKeyDown={(e) => {
        if (e.key === 'Enter' || e.key === ' ') {
          e.preventDefault();
          handleClick();
        }
      }}
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        cursor: 'pointer',
        overflow: 'hidden',
        transition: 'transform 0.2s ease, box-shadow 0.2s ease',
        '&:hover': {
          transform: 'translateY(-2px)',
          boxShadow: '0 16px 32px rgba(18, 28, 45, 0.1)',
        },
        '&:focus-visible': {
          outline: (t) => `2px solid ${t.palette.primary.main}`,
          outlineOffset: 2,
        },
      }}
    >
      <Box
        component="img"
        src={category.image}
        alt=""
        sx={{
          width: '100%',
          height: imageHeight,
          objectFit: 'cover',
          display: 'block',
        }}
      />
      <CardContent sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 0.5, p: 2.5 }}>
        <Typography variant="h6" fontWeight={700} component="h3">
          {category.title}
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ flex: 1 }}>
          {category.description}
        </Typography>
        {category.badge ? (
          <Chip
            size="small"
            label={category.badge}
            color="primary"
            variant="outlined"
            sx={{ alignSelf: 'flex-start', mt: 1, width: 'fit-content' }}
          />
        ) : null}
      </CardContent>
    </Card>
  );
}
