export interface User {
  id?: number;
  email: string;
  fullName: string;
  role?: string;
  passwordHash?: string;
  createdAt?: string;
}
