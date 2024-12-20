import { error, redirect } from '@sveltejs/kit';
import type { Actions, PageServerLoad } from './$types';
import { Role } from '$lib/enums';
import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { deliverySchema, type DeliverySchema } from './schema';
import { PUBLIC_API_HOST, PUBLIC_LOCAL_HOST } from '$env/static/public';

export const load: PageServerLoad = async ({ locals, url }) => {
	if (!locals.user) {
		return error(401, 'Unauthorized');
	}

	const deliveryForm = await superValidate(zod(deliverySchema));

	return { deliveryForm };
};

export const actions: Actions = {
	checkout: async ({ request, locals, fetch }) => {
		if (!locals.user) {
			return error(403, 'Forbidden');
		}

		const form = await superValidate(request, zod(deliverySchema));

		let result;

		try {
			const res = await fetch(`${PUBLIC_API_HOST}/order/create`, {
				method: 'POST',
				headers: {
					Authorization: `Bearer ${locals.user.token}`,
					'Content-Type': 'application/json',
					Accept: 'application/json'
				},
				body: JSON.stringify({
					deliveryDate: form.data.deliveryDate,
					returnDate: form.data.returnDate,
					address: {
						countryName: form.data.countryName,
						stateName: form.data.stateName,
						townName: form.data.townName,
						street: form.data.street,
						postCodeCode: form.data.postCodeCode,
						description: form.data.description
					},
					items: form.data.items,
					successUrl: `${PUBLIC_LOCAL_HOST}/panel/checkout/success`,
					cancelUrl: `${PUBLIC_LOCAL_HOST}/panel/checkout/failed`
				})
			});

			if (!res.ok) {
				if (res.status === 409) {
					console.log(res);
					return {
						form,
						itemsNotAvailiable: true,
						items: await res?.json()?.then((data) => data.itemsThatCannotBeOrdered)
					};
				}

				return { form, errorCheckout: true };
			}

			result = await res.json();

			if (!result) {
				return { form, errorCheckout: true };
			}
		} catch (err) {
			console.log(err);
			return { form, errorCheckout: true };
		}

		redirect(302, result.stripeSessionUrl);
	}
};
