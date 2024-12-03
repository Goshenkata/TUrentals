import { z } from 'zod';

export const loginSchema = z.object({
	email: z.string().email({ message: 'Невалиден имейл формат.' }),
	password: z
		.string({ required_error: 'Полето е задължително.' })
		.min(1, { message: 'Полето е задължително.' })
});
