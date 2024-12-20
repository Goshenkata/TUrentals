import { error } from '@sveltejs/kit';
import type { PageServerLoad } from './$types';
import { generateUrlParams } from '$lib/utils';
import type { Category, ReturnedProduct } from '$lib/types';
import { PUBLIC_API_HOST } from '$env/static/public';

export const load: PageServerLoad = async ({ locals, fetch, params }) => {
	if (!locals.user) {
		return error(401, 'Unauthorized');
	}

	try {
		const response = await fetch(`${PUBLIC_API_HOST}/item/${params.product_id}`, {
			headers: {
				Authorization: `Bearer ${locals.user.token}`
			}
		});

		if (!response.ok) {
			console.log(await response.json());
			error(500, 'Failed to fetch product');
		}

		const product: ReturnedProduct = await response.json();

		return { product };
	} catch (err) {
		console.log(err);
		return error(500, 'Failed to fetch product');
	}
};
