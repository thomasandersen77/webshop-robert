import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import { Container, Grid, Link, Stack, Typography } from '@mui/material';
import type { CategoryItem } from '../../types/shop';
import { CategoryCard } from '../cards/CategoryCard';

type Props = {
  id?: string;
  title: string;
  categories: CategoryItem[];
  /** If set, overrides per-category hash navigation */
  onCategoryClick?: () => void;
  onSeeAll?: () => void;
  seeAllLabel?: string;
  imageHeight?: number;
};

export function CategoryGridSection({
  id,
  title,
  categories,
  onCategoryClick,
  onSeeAll,
  seeAllLabel = 'Se alle',
  imageHeight,
}: Props) {
  return (
    <Container maxWidth="lg" sx={{ py: { xs: 4, md: 6 } }} id={id}>
      <Stack direction="row" alignItems="baseline" justifyContent="space-between" sx={{ mb: 3 }}>
        <Typography variant="h4" component="h2" fontWeight={700}>
          {title}
        </Typography>
        {onSeeAll ? (
          <Link
            component="button"
            type="button"
            variant="body2"
            onClick={onSeeAll}
            sx={{
              cursor: 'pointer',
              fontWeight: 600,
              display: 'inline-flex',
              alignItems: 'center',
              gap: 0.5,
            }}
          >
            {seeAllLabel}
            <ChevronRightIcon fontSize="small" />
          </Link>
        ) : null}
      </Stack>
      <Grid container spacing={{ xs: 2, md: 3 }}>
        {categories.map((cat) => (
          <Grid
            key={cat.id}
            id={cat.id}
            size={{ xs: 12, sm: 6, md: 3 }}
            sx={{ scrollMarginTop: { xs: 96, md: 104 } }}
          >
            <CategoryCard category={cat} onClick={onCategoryClick} imageHeight={imageHeight} />
          </Grid>
        ))}
      </Grid>
    </Container>
  );
}
