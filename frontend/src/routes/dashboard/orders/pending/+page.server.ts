import { error } from '@sveltejs/kit';
import type { PageServerLoad } from './$types';
import type { PendingOrder, User } from '$lib/types';
import { Role } from '$lib/enums';
import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { assignEmployeeSchema } from './schema';

export const load: PageServerLoad = async ({ locals, fetch, url }) => {
	if (!locals.user || locals.user.role !== 'MANAGER') {
		return error(401, 'Unauthorized');
	}

	const assignEmployeeForm = await superValidate(zod(assignEmployeeSchema));

	try {
		const ordersRes = await fetch(`https://tu-rentals-api.webdevlimited.eu/order/getPending`, {
			headers: {
				Authorization: `Bearer ${locals.user.token}`
			}
		});

		if (!ordersRes.ok) {
			console.log(ordersRes);
			error(500, 'Failed to fetch pending orders');
		}

		const orders: PendingOrder[] = await ordersRes.json();

		const employeesRes = await fetch(
			`https://tu-rentals-api.webdevlimited.eu/user/getUsers?role=${Role.EMPLOYEE}`,
			{
				headers: {
					Authorization: `Bearer ${locals.user.token}`
				}
			}
		);

		if (!employeesRes.ok) {
			console.log(employeesRes);
			error(500, 'Failed to fetch employees');
		}

		const employees: User[] = await employeesRes.json();

		return { orders, employees, assignEmployeeForm };
	} catch (err) {
		console.log(err);
		return error(500, 'Failed to fetch pending orders or employees.');
	}
};
