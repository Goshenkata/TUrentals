import type { CartProduct, ReturnedProduct } from '$lib/types';
import { writable } from 'svelte/store';
import { derived } from 'svelte/store';

export const Cart = writable<CartProduct[]>([]);

export const IsCartOpen = writable(false);

export const addToCart = (product: ReturnedProduct, qty: number) => {
	Cart.update((currentCart) => {
		console.log(currentCart);
		// Check if the product already exists in the cart
		const existingItem = currentCart.find((item) => item.product.id === product.id);

		if (existingItem) {
			// Update the quantity if the product exists
			const updatedCart = currentCart.map((item) =>
				item.product.id === product.id ? { ...item, quantity: item.quantity + qty } : item
			);
			// Save to local storage
			localStorage.setItem('cart', JSON.stringify(updatedCart));
			IsCartOpen.set(true);
			return updatedCart;
		}

		// Add new item to the cart if it doesn't exist
		const newItem: CartProduct = {
			product,
			quantity: qty
		};
		const updatedCart = [...currentCart, newItem];

		// Save to local storage
		localStorage.setItem('cart', JSON.stringify(updatedCart));
		IsCartOpen.set(true);

		return updatedCart;
	});
};

export const handleDecrement = (item: CartProduct) => {
	const cartId = item.product.id;

	Cart.update((currentCart) => {
		let cardItems = [...currentCart];
		const foundIndex = cardItems.findIndex((item) => item.product.id == cartId);
		cardItems[foundIndex] = { ...item, quantity: item.quantity - 1 };
		localStorage.setItem('cart', JSON.stringify([...cardItems]));
		return [...cardItems];
	});
};

export const handleIncrement = (item: CartProduct) => {
	const cartId = item.product.id;

	Cart.update((currentCart) => {
		let cardItems = [...currentCart];
		const foundIndex = cardItems.findIndex((item) => item.product.id == cartId);
		cardItems[foundIndex] = { ...item, quantity: item.quantity + 1 };
		localStorage.setItem('cart', JSON.stringify([...cardItems]));
		return [...cardItems];
	});
};

export const handleDelete = (item: CartProduct) => {
	const cartId = item.product.id;

	Cart.update((currentCart) => {
		let cardItems = [...currentCart];
		const foundIndex = cardItems.findIndex((item) => item.product.id == cartId);
		const newCardItems = cardItems.filter((item) => item.product.id != cartId);
		localStorage.setItem('cart', JSON.stringify([...newCardItems]));
		return [...newCardItems];
	});
};

export const cartStats = derived(Cart, ($cart) => {
	let quantity = 0;
	let total = 0;
	for (const product of $cart) {
		quantity += product.quantity;
		total += product.product.pricePerDay * product.quantity;
	}
	return {
		quantity,
		total
	};
});
