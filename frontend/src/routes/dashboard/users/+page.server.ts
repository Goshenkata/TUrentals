import { error, type Actions } from '@sveltejs/kit';
import type { PageServerLoad } from './$types';
import { fail, superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { deleteUserSchema, editUserSchema, newUserSchema } from './schema';
import type { NonNullableUser } from '$lib/types';
import { generateUrlParams } from '$lib/utils';

export const load: PageServerLoad = async ({ locals, fetch, url }) => {
	if (!locals.user) {
		return error(401, 'Unauthorized');
	}

	const searchedRole = url.searchParams.get('role');
	const searchedEmail = url.searchParams.get('email');

	const searchParams = {
		role: searchedRole,
		email: searchedEmail
	};

	const urlParams = generateUrlParams(searchParams);

	const newUserForm = await superValidate(zod(newUserSchema));
	const editUserForm = await superValidate(zod(editUserSchema));
	const deleteUserForm = await superValidate(zod(deleteUserSchema));

	try {
		const response = await fetch(
			`https://tu-rentals-api.webdevlimited.eu/user/getUsers${urlParams}`,
			{
				headers: {
					Authorization: `Bearer ${locals.user.token}`
				}
			}
		);

		if (!response.ok) {
			error(500, 'Failed to fetch users');
		}

		const users: NonNullableUser[] = await response.json();

		return { users, newUserForm, editUserForm, deleteUserForm };
	} catch (err) {
		console.log(err);
		return error(500, 'Failed to fetch users');
	}
};

export const actions: Actions = {
	registerUser: async ({ request, fetch, locals }) => {
		if (!locals.user || locals.user.role !== 'ADMIN') {
			return error(403, 'Forbidden');
		}

		const form = await superValidate(request, zod(newUserSchema));

		if (!form.valid) {
			return fail(400, { form });
		}

		// Check if the email exists
		try {
			const foundUser = await fetch(
				`https://tu-rentals-api.webdevlimited.eu/user/getUsers?email=${form.data.email}`,
				{
					headers: {
						Authorization: `Bearer ${locals.user.token}`
					}
				}
			);

			if (foundUser.ok) {
				const [user] = await foundUser.json();

				if (user) return { form, emailExists: true };
			}
		} catch (_) {}

		try {
			const response = await fetch('https://tu-rentals-api.webdevlimited.eu/user/createUser', {
				headers: {
					Authorization: `Bearer ${locals.user.token}`,
					Accept: 'application/json',
					'Content-Type': 'application/json'
				},
				method: 'POST',
				body: JSON.stringify(form.data)
			});

			if (!response.ok) {
				return { form, errorCreateUser: true };
			}

			return { form, createUserSuccess: true };
		} catch (err) {
			console.log(err);
			return { form, errorCreateUser: true };
		}
	},

	deleteUser: async ({ request, fetch, locals }) => {
		if (!locals.user || locals.user.role !== 'ADMIN') {
			return error(403, 'Forbidden');
		}

		const form = await superValidate(request, zod(deleteUserSchema));

		if (!form.valid) {
			return fail(400, { form });
		}

		try {
			const response = await fetch(
				`https://tu-rentals-api.webdevlimited.eu/user/deleteUser/${encodeURIComponent(form.data.email)}`,
				{
					headers: {
						Authorization: `Bearer ${locals.user.token}`
					},
					method: 'DELETE'
				}
			);

			console.log(response);

			if (!response.ok) {
				return { form, errorDeleteUser: true };
			}

			return { form, deleteUserSuccess: true };
		} catch (err) {
			console.log(err);
			return { form, errorDeleteUser: true };
		}
	},

	editUser: async ({ request, fetch, locals }) => {
		if (!locals.user || locals.user.role !== 'ADMIN') {
			return error(403, 'Forbidden');
		}

		const form = await superValidate(request, zod(editUserSchema));

		if (!form.valid) {
			return fail(400, { form });
		}

		try {
			const response = await fetch(
				`https://tu-rentals-api.webdevlimited.eu/user/updateUser/${form.data.id}`,
				{
					headers: {
						Authorization: `Bearer ${locals.user.token}`,
						Accept: 'application/json',
						'Content-Type': 'application/json'
					},
					method: 'POST',
					body: JSON.stringify(form.data)
				}
			);

			if (!response.ok) {
				return { form, errorEditUser: true };
			}

			return { form, editUserSuccess: true };
		} catch (err) {
			console.log(err);
			return { form, errorEditUser: true };
		}
	}
};
