import { error } from '@sveltejs/kit';
import type { Actions, PageServerLoad } from './$types';
import type { OrderLineItem, PendingOrder, User } from '$lib/types';
import { Role } from '$lib/enums';
import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { changeQuantitySchema } from './schema';
import { PUBLIC_API_HOST } from '$env/static/public';

export const load: PageServerLoad = async ({ locals, fetch, url }) => {
	if (!locals.user || locals.user.role !== Role.MANAGER) {
		return error(401, 'Unauthorized');
	}

	const changeQuantityForm = await superValidate(zod(changeQuantitySchema));

	try {
		const itemsRes = await fetch(`${PUBLIC_API_HOST}/warehouse/getLines`, {
			headers: {
				Authorization: `Bearer ${locals.user.token}`
			}
		});

		if (!itemsRes.ok) {
			console.log(itemsRes);
			error(500, 'Failed to fetch warehouse items');
		}

		const lineItems: OrderLineItem[] = await itemsRes.json();

		return { lineItems, changeQuantityForm };
	} catch (err) {
		console.log(err);
		return error(500, 'Failed to fetch warehouse items.');
	}
};

export const actions: Actions = {
	changeQuantity: async ({ request, locals, fetch }) => {
		if (!locals.user || locals.user.role !== Role.MANAGER) {
			return error(403, 'Forbidden');
		}

		const form = await superValidate(request, zod(changeQuantitySchema));

		console.log(form.data);

		try {
			const res = await fetch(`${PUBLIC_API_HOST}/warehouse/setQuantity`, {
				method: 'POST',
				headers: {
					Authorization: `Bearer ${locals.user.token}`,
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(form.data)
			});

			if (!res.ok) {
				console.log(res);
				return { form, errorChangeQuantity: true };
			}

			return { form, changeQuantitySuccess: true };
		} catch (err) {
			console.log(err);
			return { form, errorChangeQuantity: true };
		}
	}
};
