import { error } from '@sveltejs/kit';
import type { PageServerLoad } from './$types';
import type { Category } from '$lib/types';
import { fail, superValidate, withFiles } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { newProductSchema } from './schema';
import { deserialize } from '$app/forms';

export const load: PageServerLoad = async ({ locals, fetch }) => {
	if (!locals.user) {
		return error(401, 'Unauthorized');
	}

	const newProductForm = await superValidate(zod(newProductSchema));

	try {
		const categoriesResponse = await fetch(
			'https://tu-rentals-api.webdevlimited.eu/category/search',
			{
				headers: {
					Authorization: `Bearer ${locals.user.token}`
				}
			}
		);

		if (!categoriesResponse.ok) {
			console.log(categoriesResponse);
			error(500, 'Failed to fetch categories');
		}

		const categories: Category[] = await categoriesResponse.json();

		return { categories, newProductForm };
	} catch (err) {
		console.log(err);
		return error(500);
	}
};

export const actions = {
	createProduct: async ({ request, fetch, locals }) => {
		if (!locals.user || locals.user.role !== 'MANAGER') {
			return error(403, 'Forbidden');
		}

		const form = await superValidate(request, zod(newProductSchema));

		if (!form.valid) {
			return fail(400, { form: withFiles(form) });
		}

		let imgUrl = undefined;

		if (form.data.image) {
			const imageData = new FormData();

			imageData.append('file', form.data.image);

			try {
				const imgRes = await fetch('https://tu-rentals-api.webdevlimited.eu/image/upload', {
					headers: {
						Authorization: `Bearer ${locals.user.token}`,
						Accept: 'application/json'
					},
					method: 'POST',
					body: imageData
				});

				if (!imgRes.ok) {
					return error(500, 'Failed to upload image');
				}

				imgUrl = await imgRes.text();
			} catch (err) {
				console.log(err);
			}
		}

		try {
			const response = await fetch('https://tu-rentals-api.webdevlimited.eu/item/create', {
				headers: {
					Authorization: `Bearer ${locals.user.token}`,
					Accept: 'application/json',
					'Content-Type': 'application/json'
				},
				method: 'POST',
				body: JSON.stringify({
					name: form.data.name,
					description: form.data.description,
					pricePerDay: form.data.pricePerDay,
					initialQuantity: form.data.initialQuantity,
					categoryId: form.data.categoryId,
					imageUrl: imgUrl ?? null
				})
			});

			if (!response.ok) {
				return { form: withFiles(form), errorCreateProduct: true };
			}

			return { form: withFiles(form), createProductSuccess: true };
		} catch (err) {
			console.log(err);
			return { form: withFiles(form), errorCreateProduct: true };
		}
	}
};
