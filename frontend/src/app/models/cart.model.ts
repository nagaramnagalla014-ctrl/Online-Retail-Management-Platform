import { Product } from './product.model';

export interface CartItem {
  cartItemId: number;
  product: Product;
  quantity: number;
}

export interface Cart {
  cartId: number;
  items: CartItem[];
  updatedAt: Date;
}
