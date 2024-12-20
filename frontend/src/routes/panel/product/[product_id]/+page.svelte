<script lang="ts">
	import { addToCart } from '$lib/stores/cart.svelte.js';

	let { data } = $props();
</script>

<div class="bg-white">
	<div
		class="mx-auto max-w-2xl lg:grid-rows-1 px-4 py-16 sm:px-6 sm:py-24 lg:grid lg:max-w-7xl lg:grid-cols-2 lg:gap-x-8 lg:px-8 items-center"
	>
		<div class="lg:max-w-lg">
			<div class="mt-4">
				<h1 class="text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
					{data.product.name}
				</h1>
			</div>

			<section aria-labelledby="information-heading" class="mt-4">
				<div class="flex items-center">
					<p class="text-lg text-gray-900 sm:text-xl">
						{data.product.pricePerDay.toLocaleString('bg-BG', {
							style: 'currency',
							currency: 'BGN'
						})} / ден
					</p>
				</div>

				<div class="mt-4 space-y-6">
					<p class="text-base text-gray-500">
						{data.product.description}
					</p>
				</div>
			</section>

			<div class="mt-6">
				<section aria-labelledby="options-heading">
					<div class="mt-10">
						<button
							onclick={() => addToCart(data.product, 1)}
							class="flex w-full items-center justify-center rounded-md border border-transparent bg-indigo-600 px-8 py-3 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 focus:ring-offset-gray-50"
							>Добави в количката</button
						>
					</div>
				</section>
			</div>
		</div>

		<!-- Product image -->
		<div class="mt-10 lg:col-start-2 lg:row-span-2 lg:mt-0 lg:self-center">
			<img
				src={data.product.imageUrl}
				alt={data.product.name}
				class="aspect-square w-full rounded-lg object-cover"
			/>
		</div>
	</div>
</div>
