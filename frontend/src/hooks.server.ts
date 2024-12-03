import { deleteAuthenticationCookies } from '$lib/server/authCookies';
import { redirect, type Handle } from '@sveltejs/kit';
import { sequence } from '@sveltejs/kit/hooks';

const handleAuth: Handle = async ({ event, resolve }) => {
	const token = event.cookies.get('tu-rentals-auth');

	const { fetch, cookies } = event;

	if (!token) {
		await deleteAuthenticationCookies(cookies);
		event.locals.user = null;
		return await resolve(event);
	}

	try {
		const res = await fetch('https://tu-rentals-api.webdevlimited.eu/user/isValid', {
			headers: {
				Authorization: `Bearer ${token}`
			},
			method: 'GET'
		});

		// console.log(res);

		if (!res.ok) {
			event.locals.user = null;
			await deleteAuthenticationCookies(cookies);
		}

		const user = await res.json();
		event.locals.user = user;
	} catch {
		event.locals.user = null;
	}

	// console.log(event.locals.user);

	return resolve(event);
};

const authGuard: Handle = async ({ event, resolve }) => {
	const { locals } = event;

	if (
		locals.user?.role !== 'ADMIN' &&
		locals.user?.role !== 'MANAGER' &&
		event.url.pathname.startsWith('/dashboard')
	) {
		redirect(303, '/');
	}

	if (!locals.user?.role && event.url.pathname.startsWith('/panel')) {
		redirect(303, '/login');
	}

	return resolve(event);
};

export const handle = sequence(handleAuth, authGuard);
