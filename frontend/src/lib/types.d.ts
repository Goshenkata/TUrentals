import type { Pen } from 'lucide-svelte';
import type { Role } from './enums';

type User = null | {
	id: number;
	firstName: string;
	lastName: string;
	phoneNumber: string;
	role: Role;
	email: string;
	token: string;
};

type LoginInfo = null | {
	token: string;
	username: string;
	role: Role;
};

type NonNullableUser = NonNullable<Exclude<User, null | undefined>>;

type Category = {
	id: number;
	name: string;
};

type ReturnedProduct = {
	id: number;
	name: string;
	categoryName: string;
	pricePerDay: number;
	imageUrl: string;
	description: string;
};

type OrderDeliveryAddress = {
	countryName: string;
	stateName: string;
	townName: string;
	street: string;
	postCodeCode: string;
	description: string;
};

type OrderLineItem = {
	item: ReturnedProduct;
	quantity: number;
};

type OrderType = 'DELIVERY' | 'PICKUP';

type PendingOrder = {
	id: number;
	totalPrice: number;
	deliveryDate: string;
	returnDate: string;
	note: string;
	deliveryAddress: OrderDeliveryAddress;
	lines: OrderLineItem[];
	orderType: OrderType;
	assignenedTo: User;
};

type AssignedOrder = PendingOrder & {
	assignenedTo: Omit<NonNullableUser, 'token'>;
};

type CartProduct = {
	product: ReturnedProduct;
	quantity: number;
};
