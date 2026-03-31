import type { CategoryKey, ProductItem } from '../types/shop';

export const products: ProductItem[] = [
  {
    id: 'p1',
    title: 'Trådløse hodetelefoner',
    category: 'electronics',
    price: 1299,
    oldPrice: 1499,
    image:
      'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&fm=jpg&q=80&w=900',
    badge: 'Bestselger',
    shortDescription: 'Støydemping og klar lyd for hverdagsbruk.',
  },
  {
    id: 'p2',
    title: 'Smartklokke',
    category: 'electronics',
    price: 2490,
    image:
      'https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&fm=jpg&q=80&w=900',
    badge: 'Ny',
    shortDescription: 'Aktivitet, søvn og varsler i ett grep.',
  },
  {
    id: 'p3',
    title: 'Bluetooth-høyttaler',
    category: 'electronics',
    price: 899,
    oldPrice: 1099,
    image:
      'https://images.unsplash.com/photo-1608043152269-423dbba4e7e2?auto=format&fit=crop&fm=jpg&q=80&w=900',
    badge: 'Populær',
    shortDescription: 'Kompakt lyd med fyldig bass.',
  },
  {
    id: 'p4',
    title: 'Bordlampe',
    category: 'home',
    price: 699,
    image:
      'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?auto=format&fit=crop&fm=jpg&q=80&w=900',
    shortDescription: 'Dempet lys til kveldsstemning.',
  },
  {
    id: 'p5',
    title: 'Lounge-stol',
    category: 'home',
    price: 4990,
    oldPrice: 5690,
    image:
      'https://images.unsplash.com/photo-1567538096630-e0c55bd6374c?auto=format&fit=crop&fm=jpg&q=80&w=900',
    badge: 'Bestselger',
    shortDescription: 'Komfortabel sitteflate og rolig uttrykk.',
  },
  {
    id: 'p6',
    title: 'Sengetøy',
    category: 'home',
    price: 899,
    image:
      'https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?auto=format&fit=crop&fm=jpg&q=80&w=900',
    badge: 'Ny',
    shortDescription: 'Mykt, pustende og enkelt å vedlikeholde.',
  },
  {
    id: 'p7',
    title: 'Løpesko',
    category: 'sport',
    price: 1199,
    image:
      'https://images.unsplash.com/photo-1542291026-7eec264c27b2?auto=format&fit=crop&fm=jpg&q=80&w=900',
    badge: 'Populær',
    shortDescription: 'Lett såle og god demping på asfalt.',
  },
  {
    id: 'p8',
    title: 'Yogamatte',
    category: 'sport',
    price: 449,
    image:
      'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?auto=format&fit=crop&fm=jpg&q=80&w=900',
    shortDescription: 'Sklisikkert grep og behagelig tykkelse.',
  },
  {
    id: 'p9',
    title: 'Treningsflaske',
    category: 'sport',
    price: 199,
    image:
      'https://images.unsplash.com/photo-1602143407151-7111542de6e8?auto=format&fit=crop&fm=jpg&q=80&w=900',
    shortDescription: 'BPA-fri og lekker å ha med i sekken.',
  },
  {
    id: 'p10',
    title: 'Serum',
    category: 'beauty',
    price: 549,
    image:
      'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?auto=format&fit=crop&fm=jpg&q=80&w=900',
    badge: 'Bestselger',
    shortDescription: 'Lett konsistens som trekker raskt inn.',
  },
  {
    id: 'p11',
    title: 'Ansiktskrem',
    category: 'beauty',
    price: 329,
    oldPrice: 399,
    image:
      'https://images.unsplash.com/photo-1570194065650-d99fb4b38b14?auto=format&fit=crop&fm=jpg&q=80&w=900',
    shortDescription: 'Døgnfukt uten tung følelse.',
  },
  {
    id: 'p12',
    title: 'Rensegel',
    category: 'beauty',
    price: 279,
    image:
      'https://images.unsplash.com/photo-1556228578-0d85b1a4d571?auto=format&fit=crop&fm=jpg&q=80&w=900',
    badge: 'Ny',
    shortDescription: 'Mild rens som lar huden puste.',
  },
];

export function getProductsByCategory(key: CategoryKey): ProductItem[] {
  return products.filter((p) => p.category === key);
}

export function getNewProducts(): ProductItem[] {
  return products.filter((p) => p.badge === 'Ny');
}
