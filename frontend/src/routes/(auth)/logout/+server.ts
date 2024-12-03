import { json, redirect } from '@sveltejs/kit';
import type { RequestHandler } from './$types';
import { deleteAuthenticationCookies } from '$lib/server/authCookies';

export const GET: RequestHandler = async (event) => {
	await deleteAuthenticationCookies(event.cookies);

	return redirect(302, '/login');
};
