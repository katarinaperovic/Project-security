import { ObjectId } from 'bson';
import { Address } from './address';

export interface User {
  name?: string|null;
  surname?: string|null;
  firmName?: string | null; 
  pib?: string | null;
  phone:string;
  confirmpassword: string;
  email: string;
  password: string;
  address: Address | null;
  packageType?: PackageType|null;
}

export enum PackageType {
  BASIC = 'BASIC',
  STANDARD = 'STANDARD',
  GOLD = 'GOLD'
}
