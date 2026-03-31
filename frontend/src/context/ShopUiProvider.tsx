import {
  useCallback,
  useMemo,
  useState,
  type ReactNode,
} from 'react';
import { products } from '../data/products';
import type { CartItem, ProductItem } from '../types/shop';
import { ShopUiContext } from './shopUiContext';

function productToCartLine(p: ProductItem): CartItem {
  return {
    id: p.id,
    title: p.title,
    price: p.price,
    image: p.image,
    quantity: 1,
  };
}

function initialDemoCart(): CartItem[] {
  return products.slice(0, 3).map(productToCartLine);
}

export function ShopUiProvider({ children }: { children: ReactNode }) {
  const [navOpen, setNavOpen] = useState(false);
  const [cartOpen, setCartOpen] = useState(false);
  const [cartItems, setCartItems] = useState<CartItem[]>(initialDemoCart);

  const toggleNav = useCallback(() => {
    setNavOpen((o) => !o);
  }, []);

  const openCart = useCallback(() => {
    setNavOpen(false);
    setCartOpen(true);
  }, []);

  const addToCart = useCallback((product: ProductItem) => {
    setNavOpen(false);
    setCartItems((prev) => {
      const i = prev.findIndex((c) => c.id === product.id);
      if (i >= 0) {
        return prev.map((c, idx) =>
          idx === i ? { ...c, quantity: c.quantity + 1 } : c,
        );
      }
      return [...prev, productToCartLine(product)];
    });
    setCartOpen(true);
  }, []);

  const cartItemCount = useMemo(
    () => cartItems.reduce((n, c) => n + c.quantity, 0),
    [cartItems],
  );

  const value = useMemo(
    () => ({
      navOpen,
      setNavOpen,
      cartOpen,
      setCartOpen,
      toggleNav,
      openCart,
      cartItems,
      addToCart,
      cartItemCount,
    }),
    [navOpen, cartOpen, toggleNav, openCart, cartItems, addToCart, cartItemCount],
  );

  return (
    <ShopUiContext.Provider value={value}>{children}</ShopUiContext.Provider>
  );
}
