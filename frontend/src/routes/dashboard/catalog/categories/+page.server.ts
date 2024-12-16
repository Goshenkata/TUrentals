import { error, type Actions } from '@sveltejs/kit';
import type { PageServerLoad } from './$types';
import { fail, superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { deleteCategorySchema, editCategorySchema, newCategorySchema } from './schema';
import { generateUrlParams } from '$lib/utils';
import type { Category } from '$lib/types';

export const load: PageServerLoad = async ({ locals, fetch, url }) => {
	if (!locals.user) {
		return error(401, 'Unauthorized');
	}

	const searchedName = url.searchParams.get('query');

	const searchParams = {
		query: searchedName
	};

	const urlParams = generateUrlParams(searchParams);

	const newCategoryForm = await superValidate(zod(newCategorySchema));
	const editCategoryForm = await superValidate(zod(editCategorySchema));
	const deleteCategoryForm = await superValidate(zod(deleteCategorySchema));

	try {
		const response = await fetch(
			`https://tu-rentals-api.webdevlimited.eu/category/search${urlParams}`,
			{
				headers: {
					Authorization: `Bearer ${locals.user.token}`
				}
			}
		);

		if (!response.ok) {
			console.log(response);
			error(500, 'Failed to fetch categories');
		}

		const categories: Category[] = await response.json();

		return { categories, newCategoryForm, editCategoryForm, deleteCategoryForm };
	} catch (err) {
		console.log(err);
		return error(500, 'Failed to fetch categories');
	}
};

export const actions: Actions = {
	createCategory: async ({ request, fetch, locals }) => {
		if (!locals.user || locals.user.role !== 'MANAGER') {
			return error(403, 'Forbidden');
		}

		const form = await superValidate(request, zod(newCategorySchema));

		if (!form.valid) {
			return fail(400, { form });
		}

		// Check if the email exists
		try {
			const foundCategory = await fetch(
				`https://tu-rentals-api.webdevlimited.eu/category/search?query=${form.data.name}`,
				{
					headers: {
						Authorization: `Bearer ${locals.user.token}`
					}
				}
			);

			if (foundCategory.ok) {
				const [category] = await foundCategory.json();

				if (category) return { form, categoryExists: true };
			}
		} catch (_) {}

		try {
			const response = await fetch(
				`https://tu-rentals-api.webdevlimited.eu/category?name=${encodeURIComponent(form.data.name.toUpperCase())}`,
				{
					headers: {
						Authorization: `Bearer ${locals.user.token}`,
						Accept: 'application/json',
						'Content-Type': 'application/json'
					},
					method: 'POST'
				}
			);

			if (!response.ok) {
				return { form, errorCreateCategory: true };
			}

			return { form, createCategorySuccess: true };
		} catch (err) {
			console.log(err);
			return { form, errorCreateCategory: true };
		}
	}

	// deleteCategory: async ({ request, fetch, locals }) => {
	// 	if (!locals.user || locals.user.role !== 'ADMIN') {
	// 		return error(403, 'Forbidden');
	// 	}

	// 	const form = await superValidate(request, zod(deleteCategorySchema));

	// 	if (!form.valid) {
	// 		return fail(400, { form });
	// 	}

	// 	try {
	// 		const response = await fetch(
	// 			`https://tu-rentals-api.webdevlimited.eu/user/deleteCategory/${encodeURIComponent(form.data.id)}`,
	// 			{
	// 				headers: {
	// 					Authorization: `Bearer ${locals.user.token}`
	// 				},
	// 				method: 'DELETE'
	// 			}
	// 		);

	// 		console.log(response);

	// 		if (!response.ok) {
	// 			return { form, errorDeleteCategory: true };
	// 		}

	// 		return { form, deleteCategorySuccess: true };
	// 	} catch (err) {
	// 		console.log(err);
	// 		return { form, errorDeleteCategory: true };
	// 	}
	// },

	// editCategory: async ({ request, fetch, locals }) => {
	// 	if (!locals.user || locals.user.role !== 'ADMIN') {
	// 		return error(403, 'Forbidden');
	// 	}

	// 	const form = await superValidate(request, zod(editCategorySchema));

	// 	if (!form.valid) {
	// 		return fail(400, { form });
	// 	}

	// 	try {
	// 		const response = await fetch(
	// 			`https://tu-rentals-api.webdevlimited.eu/user/updateCategory/${form.data.id}`,
	// 			{
	// 				headers: {
	// 					Authorization: `Bearer ${locals.user.token}`,
	// 					Accept: 'application/json',
	// 					'Content-Type': 'application/json'
	// 				},
	// 				method: 'POST',
	// 				body: JSON.stringify(form.data)
	// 			}
	// 		);

	// 		if (!response.ok) {
	// 			return { form, errorEditCategory: true };
	// 		}

	// 		return { form, editCategorySuccess: true };
	// 	} catch (err) {
	// 		console.log(err);
	// 		return { form, errorEditCategory: true };
	// 	}
	// }
};
