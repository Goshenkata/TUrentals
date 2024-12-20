import { error } from '@sveltejs/kit';
import type { LayoutServerLoad } from './$types';

import type { Category } from '$lib/types';
import { PUBLIC_API_HOST } from '$env/static/public';

export const load: LayoutServerLoad = async ({ locals, fetch, url }) => {
	if (!locals.user) {
		return error(401, 'Unauthorized');
	}

	try {
		const response = await fetch(`${PUBLIC_API_HOST}/category/search`, {
			headers: {
				Authorization: `Bearer ${locals.user.token}`
			}
		});

		if (!response.ok) {
			console.log(response);
			error(500, 'Failed to fetch categories');
		}

		const categories: Category[] = await response.json();

		return { categories };
	} catch (err) {
		console.log(err);
		return error(500, 'Failed to fetch categories');
	}
};
