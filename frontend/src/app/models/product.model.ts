export interface Category {
  categoryId: number;
  name: string;
  description: string;
  imageUrl: string;
  isActive: boolean;
}

export interface Product {
  productId: number;
  sku: string;
  name: string;
  description: string;
  price: number;
  stockQuantity: number;
  category: Category;
  imageUrl: string;
  brand: string;
  isActive: boolean;
  createdAt: Date;
}
