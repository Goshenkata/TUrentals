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
	quantity: number;
	pricePerDay: number;
	imageUrl: string;
};
