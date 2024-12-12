import { z } from 'zod';

const categorySchema = z.object({
	name: z
		.string({ required_error: 'Полето е задължително.' })
		.min(1, { message: 'Полето е задължително.' })
		.max(128, {
			message: 'Не може да е по дълго от 128 символа.'
		})
		.trim()
});

export const newCategorySchema = categorySchema;

export type NewCategorySchema = z.infer<typeof newCategorySchema>;

export const editCategorySchema = categorySchema.extend({
	id: z.number()
});

export type EditCategorySchema = z.infer<typeof editCategorySchema>;

export const deleteCategorySchema = z.object({ id: z.number() });

export type DeleteCategorySchema = z.infer<typeof deleteCategorySchema>;
