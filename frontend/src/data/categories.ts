import type { CategoryItem } from '../types/shop';

export const categories: CategoryItem[] = [
  {
    id: 'electronics',
    title: 'Elektronikk',
    description: 'Smartere produkter for hjem, lyd og hverdag',
    image:
      'https://images.unsplash.com/photo-1749934511277-e90042265d35?auto=format&fit=crop&fm=jpg&q=80&w=1600',
    href: '#electronics',
    badge: 'Populær',
  },
  {
    id: 'home',
    title: 'Hjem & Interiør',
    description: 'Lys, tekstiler og detaljer for et roligere hjem',
    image:
      'https://images.unsplash.com/photo-1737647862097-80f014f84140?auto=format&fit=crop&fm=jpg&q=80&w=1600',
    href: '#home',
  },
  {
    id: 'sport',
    title: 'Sport & Fritid',
    description: 'Produkter for aktivitet, tur og trening',
    image:
      'https://images.unsplash.com/photo-1705585851308-1b1080ba0144?auto=format&fit=crop&fm=jpg&q=80&w=1600',
    href: '#sport',
    badge: 'Ny',
  },
  {
    id: 'beauty',
    title: 'Skjønnhet & Velvære',
    description: 'Hudpleie og velværeprodukter i moderne uttrykk',
    image:
      'https://images.unsplash.com/photo-1741896135705-9dfb73461085?auto=format&fit=crop&fm=jpg&q=80&w=1600',
    href: '#beauty',
  },
];
