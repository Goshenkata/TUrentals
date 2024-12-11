import { fail, json, redirect } from '@sveltejs/kit';
import type { Actions, PageServerLoad } from './$types';
import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { loginSchema } from './schema';
import { setAuthenticationCookies } from '$lib/server/authCookies';
import type { LoginInfo } from '$lib/types';

export const load: PageServerLoad = async ({ locals }) => {
	if (locals.user) {
		return redirect(302, '/');
	}
	const loginForm = await superValidate(zod(loginSchema));

	return { loginForm };
};

export const actions: Actions = {
	login: async ({ cookies, request, fetch }) => {
		const form = await superValidate(request, zod(loginSchema));

		if (!form.valid) {
			return fail(400, { form });
		}

		const { email, password } = form.data;

		let loginInfo: null | LoginInfo = null;

		try {
			const res = await fetch('https://tu-rentals-api.webdevlimited.eu/user/login', {
				headers: {
					Accept: 'application/json',
					'Content-Type': 'application/json'
				},
				method: 'POST',
				body: JSON.stringify({
					email,
					password
				})
			});

			if (!res.ok) {
				return { form, wrongCreds: true };
			}

			loginInfo = await res.json();
		} catch (err) {
			console.log(err);
			return { form, errorLogin: true };
		}

		if (loginInfo) {
			setAuthenticationCookies(cookies, loginInfo.token);

			switch (loginInfo.role) {
				case 'ADMIN':
					redirect(302, '/dashboard');
				case 'EMPLOYEE':
					redirect(302, '/dashboard');
				case 'MANAGER':
					redirect(302, '/dashboard');
				case 'USER':
					redirect(302, '/panel');
				default:
					redirect(302, '/');
			}
		}

		return { form, errorLogin: true };
	}
};
