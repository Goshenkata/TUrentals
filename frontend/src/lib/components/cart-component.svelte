<script lang="ts">
	import CartItem from '$lib/components/cart-item.svelte';
	import { Cart, cartStats } from '$lib/stores/cart.svelte.js';
	import type { CartProduct } from '$lib/types.js';
	import { ShoppingCart, X } from 'lucide-svelte';

	let cartOpen = $state(false);

	$effect(() => {
		const storedCart = localStorage.getItem('cart');

		Cart.set(storedCart ? (JSON.parse(storedCart) as CartProduct[]) : []);
	});
</script>

<button
	onclick={() => (cartOpen = !cartOpen)}
	class="flex items-center rounded-full px-4 py-2 text-white"
>
	<ShoppingCart class="mr-2 size-5" />
	<span>({$cartStats.quantity})</span>
</button>
{#if cartOpen}
	<div class="absolute right-0 top-8 z-10 mt-2 w-80 rounded-lg bg-white shadow-xl">
		<div class="relative p-4">
			<h2 class="mb-4 text-lg font-semibold">Вашата Количка</h2>
			<button
				class="absolute right-4 top-4 rounded-full p-1 hover:bg-gray-100"
				aria-label="close cart"
				onclick={() => (cartOpen = false)}
			>
				<X class="size-4" />
			</button>
			{#each $Cart as _, i}
				<CartItem bind:cartProduct={$Cart[i]} />
			{/each}
			<div class="mt-4 border-gray-200 pt-4">
				<p class="text-lg font-semibold">
					Тотал: {$cartStats.total.toLocaleString('bg-BG', {
						style: 'currency',
						currency: 'BGN'
					})}
				</p>
			</div>
		</div>
		<a
			class="flex m-2 items-center justify-center rounded-md border border-transparent bg-indigo-600 px-6 py-2 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 focus:ring-offset-gray-50"
			href="/panel/checkout">Плати</a
		>
	</div>
{/if}
