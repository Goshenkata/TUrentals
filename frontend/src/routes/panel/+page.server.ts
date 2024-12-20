import { error } from '@sveltejs/kit';
import type { PageServerLoad } from './$types';
import { generateUrlParams } from '$lib/utils';
import type { Category, ReturnedProduct } from '$lib/types';
import { PUBLIC_API_HOST } from '$env/static/public';

export const load: PageServerLoad = async ({ locals, fetch, url }) => {
	if (!locals.user) {
		return error(401, 'Unauthorized');
	}

	try {
		const response = await fetch(`${PUBLIC_API_HOST}/item/search`, {
			headers: {
				Authorization: `Bearer ${locals.user.token}`
			}
		});

		if (!response.ok) {
			console.log(await response.json());
			error(500, 'Failed to fetch products');
		}

		const products: ReturnedProduct[] = await response.json();

		return { products };
	} catch (err) {
		console.log(err);
		return error(500, 'Failed to fetch products');
	}
};
