import { OrderTypeEnum } from '$lib/enums';
import { z } from 'zod';

export const assignEmployeeSchema = z.object({
	employeeId: z.number(),
	orderId: z.number(),
	orderType: z.enum(Object.keys(OrderTypeEnum).map((key) => key) as [keyof typeof OrderTypeEnum])
});

export type AssignEmployeeSchema = z.infer<typeof assignEmployeeSchema>;
