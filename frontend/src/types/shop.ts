export type CategoryKey = 'electronics' | 'home' | 'sport' | 'beauty';

export interface CategoryItem {
  id: CategoryKey;
  title: string;
  description: string;
  image: string;
  /** Hash only, e.g. `#electronics` — combined with current route in navigation */
  href: string;
  badge?: string;
}

export type ProductBadge = 'Ny' | 'Populær' | 'Bestselger';

export interface ProductItem {
  id: string;
  title: string;
  category: CategoryKey;
  price: number;
  oldPrice?: number;
  image: string;
  badge?: ProductBadge;
  shortDescription: string;
}

export interface CartItem {
  id: string;
  title: string;
  price: number;
  image: string;
  quantity: number;
}
