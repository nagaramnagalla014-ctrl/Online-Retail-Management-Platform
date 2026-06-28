export interface Customer {
  customerId: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  address: string;
  city: string;
  state: string;
  pincode: string;
  isActive: boolean;
  createdAt: Date;
}
