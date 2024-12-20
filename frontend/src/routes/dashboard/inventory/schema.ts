import { z } from 'zod';

export const changeQuantitySchema = z.object({
	itemId: z.number(),
	newQuantity: z.number()
});

export type ChangeQuantitySchema = z.infer<typeof changeQuantitySchema>;
