export enum Role {
	ADMIN = 'ADMIN',
	MANAGER = 'MANAGER',
	USER = 'USER',
	EMPLOYEE = 'EMPLOYEE'
}

export enum SortByEnum {
	NAME_ASC = 'Име възходящо',
	NAME_DESC = 'Име низходящо',
	PRICE_ASC = 'Цена възходящо',
	PRICE_DESC = 'Цена низходящо'
}

export enum OrderTypeEnum {
	DELIVERY = 'DELIVERY',
	PICKUP = 'PICKUP'
}

export enum OrderStatusEnum {
	PENDING = 'PENDING',
	DELIVERED = 'DELIVERED',
	COMPLETED = 'COMPLETED'
}
