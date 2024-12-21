import { error } from '@sveltejs/kit';
import type { Actions, PageServerLoad } from './$types';
import type { OrderThatNeedsAttention } from '$lib/types';
import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { PUBLIC_API_HOST } from '$env/static/public';

export const load: PageServerLoad = async ({ locals, fetch, url }) => {
	if (!locals.user || locals.user.role !== 'MANAGER') {
		return error(401, 'Unauthorized');
	}

	try {
		const ordersRes = await fetch(`${PUBLIC_API_HOST}/warehouse/getOrdersThatNeedAttention`, {
			headers: {
				Authorization: `Bearer ${locals.user.token}`
			}
		});

		if (!ordersRes.ok) {
			console.log(ordersRes);
			error(500, 'Failed to fetch pending orders');
		}

		const orders: OrderThatNeedsAttention[] = await ordersRes.json();

		return { orders };
	} catch (err) {
		console.log(err);
		return error(500, 'Failed to fetch pending orders or employees.');
	}
};
