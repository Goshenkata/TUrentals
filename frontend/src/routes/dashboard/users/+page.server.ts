import { error } from '@sveltejs/kit';
import type { PageServerLoad } from './$types';
import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { editUserSchema, newUserSchema } from './schema';
import type { NonNullableUser } from '$lib/types';

export const load: PageServerLoad = async ({ locals, fetch }) => {
	if (!locals.user) {
		return error(401, 'Unauthorized');
	}

	const newUserForm = await superValidate(zod(newUserSchema));
	const editUserForm = await superValidate(zod(editUserSchema));

	try {
		const response = await fetch('https://tu-rentals-api.webdevlimited.eu/user/getUsers', {
			headers: {
				Authorization: `Bearer ${locals.user.token}`
			}
		});

		if (!response.ok) {
			error(500, 'Failed to fetch users');
		}

		const users: NonNullableUser[] = await response.json();

		return { users, newUserForm, editUserForm };
	} catch (err) {
		console.log(err);
		return error(500, 'Failed to fetch users');
	}
};
