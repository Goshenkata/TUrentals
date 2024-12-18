import { PUBLIC_API_HOST } from '$env/static/public';
import { Role } from '$lib/enums';
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
		const res = await fetch(`${PUBLIC_API_HOST}/user/isValid`, {
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
		user.token = token;
		event.locals.user = user;
	} catch {
		event.locals.user = null;
	}

	return resolve(event);
};

const authGuard: Handle = async ({ event, resolve }) => {
	const { locals } = event;

	if (
		locals.user?.role !== Role.ADMIN &&
		locals.user?.role !== Role.MANAGER &&
		locals.user?.role !== Role.EMPLOYEE &&
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
