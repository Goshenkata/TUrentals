<script lang="ts">
	import { Cart, handleDecrement, handleDelete, handleIncrement } from '$lib/stores/cart.svelte';
	import type { CartProduct } from '$lib/types';
	import { Minus, Plus, Trash2 } from 'lucide-svelte';

	type Props = {
		cartProduct: CartProduct;
	};

	let { cartProduct = $bindable() }: Props = $props();
</script>

<div class="flex items-center justify-between border-b border-gray-200 py-2">
	<div class="flex items-center">
		<img
			src={cartProduct.product.imageUrl}
			alt="Product"
			class="mr-4 size-12 rounded object-cover"
		/>
		<div>
			<p class="font-medium">{cartProduct.product.name}</p>
			<p class="text-sm">
				{cartProduct.product.pricePerDay.toLocaleString('bg-BG', {
					style: 'currency',
					currency: 'BGN'
				})}
			</p>
		</div>
	</div>
	<div class="flex items-center">
		<button
			onclick={(e) => {
				if (cartProduct.quantity === 1) {
					handleDelete(cartProduct);
				} else {
					handleDecrement(cartProduct);
				}
			}}
			class="rounded p-1 hover:bg-gray-200"
			aria-label="Subtract 1 from quantity"
		>
			<Minus class="size-4" />
		</button>
		<span class="mx-2">
			{cartProduct.quantity}
		</span>
		<button
			class="rounded p-1 hover:bg-gray-200"
			aria-label="Add 1 to quantity"
			onclick={() => handleIncrement(cartProduct)}
		>
			<Plus class="size-4" />
		</button>
		<button
			onclick={() => handleDelete(cartProduct)}
			class="ml-4 rounded p-1 text-red-500 hover:bg-red-100"
		>
			<Trash2 class="size-4" />
		</button>
	</div>
</div>
