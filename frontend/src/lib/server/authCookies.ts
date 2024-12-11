import { dev } from '$app/environment';
import type { Cookies } from '@sveltejs/kit';

export const setAuthenticationCookies = (cookies: Cookies, token: string) => {
	cookies.set('tu-rentals-auth', token, {
		path: '/',
		httpOnly: true,
		sameSite: 'lax',
		secure: dev ? false : true,
		maxAge: 60 * 60 * 35 * 30
	});
};

export const deleteAuthenticationCookies = async (cookies: Cookies) => {
	cookies.delete('tu-rentals-auth', { path: '/' });
};
