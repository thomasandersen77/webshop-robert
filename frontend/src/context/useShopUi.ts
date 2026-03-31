import { useContext } from 'react';
import { ShopUiContext } from './shopUiContext';

export function useShopUi() {
  const ctx = useContext(ShopUiContext);
  if (!ctx) {
    throw new Error('useShopUi must be used within ShopUiProvider');
  }
  return ctx;
}
