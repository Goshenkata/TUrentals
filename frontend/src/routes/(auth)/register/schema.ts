import { z } from 'zod';

export const registerSchema = z
	.object({
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

		passwordConfirm: z
			.string({ required_error: 'Полето е задължително.' })
			.min(8, { message: 'Трябва да е минимум 8 символа.' })
	})
	.superRefine(({ passwordConfirm, password }, ctx) => {
		if (passwordConfirm !== password) {
			ctx.addIssue({
				code: z.ZodIssueCode.custom,
				message: 'Паролите трябва да съвпадат.',
				path: ['passwordConfirm']
			});
		}
	});
