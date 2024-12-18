import { error } from '@sveltejs/kit';
import type { Actions, PageServerLoad } from './$types';
import type { PendingOrder, User } from '$lib/types';
import { Role } from '$lib/enums';
import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { assignEmployeeSchema, changeStatusSchema } from './schema';
import { PUBLIC_API_HOST } from '$env/static/public';

export const load: PageServerLoad = async ({ locals, fetch, url }) => {
	if (!locals.user || locals.user.role !== 'MANAGER') {
		return error(401, 'Unauthorized');
	}

	const assignEmployeeForm = await superValidate(zod(assignEmployeeSchema));

	const changeStatusForm = await superValidate(zod(changeStatusSchema));

	try {
		const ordersRes = await fetch(`${PUBLIC_API_HOST}/order/getPending`, {
			headers: {
				Authorization: `Bearer ${locals.user.token}`
			}
		});

		if (!ordersRes.ok) {
			console.log(ordersRes);
			error(500, 'Failed to fetch pending orders');
		}

		const orders: PendingOrder[] = await ordersRes.json();

		const employeesRes = await fetch(`${PUBLIC_API_HOST}/user/getUsers?role=${Role.EMPLOYEE}`, {
			headers: {
				Authorization: `Bearer ${locals.user.token}`
			}
		});

		if (!employeesRes.ok) {
			console.log(employeesRes);
			error(500, 'Failed to fetch employees');
		}

		const employees: User[] = await employeesRes.json();

		return { orders, employees, assignEmployeeForm, changeStatusForm };
	} catch (err) {
		console.log(err);
		return error(500, 'Failed to fetch pending orders or employees.');
	}
};

export const actions: Actions = {
	assignEmployee: async ({ request, locals, fetch }) => {
		if (!locals.user || locals.user.role !== 'MANAGER') {
			return error(403, 'Forbidden');
		}

		const form = await superValidate(request, zod(assignEmployeeSchema));

		try {
			const res = await fetch(`${PUBLIC_API_HOST}/order/assignEmployee`, {
				method: 'POST',
				headers: {
					Authorization: `Bearer ${locals.user.token}`,
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(form.data)
			});

			if (!res.ok) {
				console.log(res);
				return { form, errorAssignEmployee: true };
			}

			return { form, assignEmployeeSuccess: true };
		} catch (err) {
			console.log(err);
			return { form, errorAssignEmployee: true };
		}
	},

	changeStatus: async ({ request, locals, fetch }) => {
		if (!locals.user || locals.user.role !== 'MANAGER') {
			return error(403, 'Forbidden');
		}

		const form = await superValidate(request, zod(changeStatusSchema));

		try {
			const res = await fetch(`${PUBLIC_API_HOST}/order/complete`, {
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
