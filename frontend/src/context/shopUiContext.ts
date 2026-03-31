import { createContext } from 'react';
import type { CartItem, ProductItem } from '../types/shop';

export type ShopUiContextValue = {
  navOpen: boolean;
  setNavOpen: (open: boolean) => void;
  cartOpen: boolean;
  setCartOpen: (open: boolean) => void;
  toggleNav: () => void;
  openCart: () => void;
  cartItems: CartItem[];
  addToCart: (product: ProductItem) => void;
  cartItemCount: number;
};

export const ShopUiContext = createContext<ShopUiContextValue | null>(null);
