import { error } from '@sveltejs/kit';
import type { Actions, PageServerLoad } from './$types';
import type { PendingOrder, User } from '$lib/types';
import { Role } from '$lib/enums';
import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { assignEmployeeSchema, changeStatusSchema } from './schema';

export const load: PageServerLoad = async ({ locals, fetch, url }) => {
	if (!locals.user || (locals.user.role !== Role.MANAGER && locals.user.role !== Role.EMPLOYEE)) {
		return error(401, 'Unauthorized');
	}

	const assignEmployeeForm = await superValidate(zod(assignEmployeeSchema));

	const changeStatusForm = await superValidate(zod(changeStatusSchema));

	try {
		const ordersRes = await fetch(`https://tu-rentals-api.webdevlimited.eu/order/getAssigned`, {
			headers: {
				Authorization: `Bearer ${locals.user.token}`
			}
		});

		if (!ordersRes.ok) {
			console.log(ordersRes);
			error(500, 'Failed to fetch pending orders');
		}

		const orders: PendingOrder[] = await ordersRes.json();

		return { orders, assignEmployeeForm, changeStatusForm };
	} catch (err) {
		console.log(err);
		return error(500, 'Failed to fetch pending orders or employees.');
	}
};

export const actions: Actions = {
	changeStatus: async ({ request, locals, fetch }) => {
		if (!locals.user || (locals.user.role !== Role.MANAGER && locals.user.role !== Role.EMPLOYEE)) {
			return error(403, 'Forbidden');
		}

		const form = await superValidate(request, zod(changeStatusSchema));

		try {
			const res = await fetch(`https://tu-rentals-api.webdevlimited.eu/order/complete`, {
				method: 'POST',
				headers: {
					Authorization: `Bearer ${locals.user.token}`,
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(form.data)
			});

			if (!res.ok) {
				console.log(res);
				return { form, errorChangeStatus: true };
			}

			return { form, changeStatusSuccess: true };
		} catch (err) {
			console.log(err);
			return { form, errorChangeStatus: true };
		}
	}
};
