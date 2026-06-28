import { Product } from './product.model';

export interface OrderItem {
  orderItemId: number;
  product: Product;
  productName: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
}

export interface Payment {
  paymentId: number;
  transactionId: string;
  amount: number;
  paymentMethod: string;
  status: string;
  paymentDate: Date;
}

export interface Shipment {
  shipmentId: number;
  trackingNumber: string;
  carrier: string;
  status: string;
  estimatedDelivery: Date;
  shippedAt: Date;
  deliveredAt: Date;
}

export interface Order {
  orderId: number;
  orderNumber: string;
  subtotal: number;
  discount: number;
  shippingCharge: number;
  totalAmount: number;
  status: string;
  shippingAddress: string;
  shippingCity: string;
  shippingState: string;
  shippingPincode: string;
  notes: string;
  createdAt: Date;
  items: OrderItem[];
  payment: Payment;
  shipment: Shipment;
}
