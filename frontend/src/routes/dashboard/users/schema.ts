import { z } from 'zod';

const memberSchema = z.object({
	firstName: z
		.string({ required_error: 'Полето е задължително.' })
		.min(1, { message: 'Полето е задължително.' })
		.max(128, {
			message: 'Не може да е по дълго от 128 символа.'
		})
		.trim(),
	lastName: z
		.string({ required_error: 'Полето е задължително.' })
		.min(1, { message: 'Полето е задължително.' })
		.max(128, {
			message: 'Не може да е по дълго от 128 символа.'
		})
		.trim(),
	email: z
		.string({ required_error: 'Полето е задължително.' })
		.email({ message: 'Невалиден формат на имейла.' })
		.trim(),
	phoneNumber: z
		.string({ required_error: 'Полето е задължително.' })
		.min(10, { message: 'Трябва да е минимум 10 символа.' })
		.max(14, { message: 'Не може да е над 14 символа.' })
		.trim(),
	password: z
		.string({ required_error: 'Полето е задължително.' })
		.min(8, { message: 'Трябва да е минимум 8 символа.' }),
	role: z.union([
		z.literal('ADMIN'),
		z.literal('MANAGER'),
		z.literal('EMPLOYEE'),
		z.literal('USER')
	])
});

export const newUserSchema = memberSchema;

export type NewUserSchema = z.infer<typeof newUserSchema>;

export const editUserSchema = memberSchema.extend({
	id: z.number()
});

export type EditUserSchema = z.infer<typeof editUserSchema>;
