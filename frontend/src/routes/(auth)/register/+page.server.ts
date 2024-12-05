import { fail, json, redirect } from '@sveltejs/kit';
import type { Actions, PageServerLoad } from './$types';
import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { registerSchema } from './schema';
import { setAuthenticationCookies } from '$lib/server/authCookies';

export const load: PageServerLoad = async ({ locals }) => {
	if (locals.user) {
		return redirect(302, '/');
	}
	const registerForm = await superValidate(zod(registerSchema));

	return { registerForm };
};

export const actions: Actions = {
	register: async ({ request, fetch }) => {
		const form = await superValidate(request, zod(registerSchema));

		if (!form.valid) {
			return fail(400, { form });
		}

		const { email, password, passwordConfirm, firstName, lastName, phoneNumber } = form.data;

		if (password !== passwordConfirm) {
			return { form, passwordsNotMatching: true };
		}

		let success = false;

		try {
			const res = await fetch('https://tu-rentals-api.webdevlimited.eu/user/register', {
				headers: {
					Accept: 'application/json',
					'Content-Type': 'application/json'
				},
				method: 'POST',
				body: JSON.stringify({
					email,
					password,
					firstName,
					lastName,
					phoneNumber
				})
			});

			// console.log(await res.json());

			if (!res.ok) {
				return { form, errorRegister: true };
			}

			success = true;
		} catch (err) {
			console.log(err);
			return { form, errorRegister: true };
		}

		if (success) redirect(302, '/login');
	}
};
