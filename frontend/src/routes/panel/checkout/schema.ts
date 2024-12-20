import { z } from 'zod';

export const deliverySchema = z.object({
	countryName: z.string({ required_error: 'Полето е задължително.' }),
	stateName: z.string({ required_error: 'Полето е задължително.' }),
	townName: z.string({ required_error: 'Полето е задължително.' }),
	street: z.string({ required_error: 'Полето е задължително.' }),
	postCodeCode: z.string({ required_error: 'Полето е задължително.' }),
	description: z.string({ required_error: 'Полето е задължително.' }),

	items: z.array(
		z.object({
			itemId: z.number(),
			quantity: z.number()
		})
	),

	deliveryDate: z.date({ required_error: 'Полето е задължително.' }),
	returnDate: z.date({ required_error: 'Полето е задължително.' })
});

export type DeliverySchema = z.infer<typeof deliverySchema>;
