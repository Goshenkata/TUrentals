import { error } from '@sveltejs/kit';
import type { PageServerLoad } from './$types';
import { generateUrlParams } from '$lib/utils';
import type { Category, ReturnedProduct } from '$lib/types';
import { PUBLIC_API_HOST } from '$env/static/public';

export const load: PageServerLoad = async ({ locals, fetch, url }) => {
	if (!locals.user) {
		return error(401, 'Unauthorized');
	}

	const searchedName = url.searchParams.get('nameQuery');
	const searchedPriceFrom = url.searchParams.get('priceFrom');
	const searchedPriceTo = url.searchParams.get('priceTo');
	const searchedCategory = url.searchParams.get('category');
	const sortBy = url.searchParams.get('sortBy');

	const searchParams = {
		nameQuery: searchedName,
		priceFrom: searchedPriceFrom,
		priceTo: searchedPriceTo,
		category: searchedCategory,
		sortBy: sortBy
	};

	const urlParams = generateUrlParams(searchParams);

	try {
		const response = await fetch(`${PUBLIC_API_HOST}/item/search${urlParams}`, {
			headers: {
				Authorization: `Bearer ${locals.user.token}`
			}
		});

		if (!response.ok) {
			console.log(await response.json());
			error(500, 'Failed to fetch products');
		}

		const products: ReturnedProduct[] = await response.json();

		const categoriesResponse = await fetch(`${PUBLIC_API_HOST}/category/search`, {
			headers: {
				Authorization: `Bearer ${locals.user.token}`
			}
		});

		if (!categoriesResponse.ok) {
			console.log(categoriesResponse);
			error(500, 'Failed to fetch categories');
		}

		const categories: Category[] = await categoriesResponse.json();

		return { products, categories };
	} catch (err) {
		console.log(err);
		return error(500, 'Failed to fetch products');
	}
};
