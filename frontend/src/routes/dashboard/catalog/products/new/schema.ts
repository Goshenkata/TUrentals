import { z } from 'zod';

const imageTypes = [
	'image/jpeg',
	'image/jpg',
	'image/png',
	'image/webp',
	'image/tiff',
	'image/avif'
];

export const newProductSchema = z.object({
	name: z
		.string({ required_error: 'Полето е задължително.' })
		.min(3, { message: 'Полето е задължително. Минимум 3 символа.' })
		.max(50, { message: 'Не може да е по дълго от 50 символа.' })
		.trim(),
	description: z
		.string()
		.min(3, { message: 'Полето е задължително. Минимум 3 символа.' })
		.max(3000, { message: 'Не може да е по-дълго от 3000 символа.' }),
	categoryId: z.number(),
	pricePerDay: z.number().positive({ message: 'Трябва да е положително число.' }),
	initialQuantity: z
		.number()
		.int({ message: 'Трябва да е цяло число.' })
		.positive({ message: 'Трябва да е положително число.' }),
	image: z
		.instanceof(File)
		.refine(
			(f) => f.size < 52_428_800,
			(f) => ({ message: 'Файлът трябва да е по-малък от 50MB.' })
		)
		.refine(
			(f) => imageTypes.includes(f.type),
			(f) => ({
				message: `Файлът трябва да бъде от тип ${imageTypes.join(', ')}.`
			})
		)
		.optional()
});
