import { OrderStatusEnum } from '$lib/enums';
import { string, z } from 'zod';

export const changeStatusSchema = z.object({
	orderId: z.number(),
	note: string().optional(),
	orderStatus: z.enum(
		Object.keys(OrderStatusEnum).map((key) => key) as [keyof typeof OrderStatusEnum]
	)
});

export type ChangeStatusSchema = z.infer<typeof changeStatusSchema>;
