import { OrderStatusEnum, OrderTypeEnum } from '$lib/enums';
import { string, z } from 'zod';

export const assignEmployeeSchema = z.object({
	employeeId: z.number(),
	orderId: z.number(),
	orderType: z.enum(Object.keys(OrderTypeEnum).map((key) => key) as [keyof typeof OrderTypeEnum])
});

export type AssignEmployeeSchema = z.infer<typeof assignEmployeeSchema>;

export const changeStatusSchema = z.object({
	orderId: z.number(),
	note: string().optional(),
	status: z.enum(Object.keys(OrderStatusEnum).map((key) => key) as [keyof typeof OrderStatusEnum])
});

export type ChangeStatusSchema = z.infer<typeof changeStatusSchema>;
