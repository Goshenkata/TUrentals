<script lang="ts">
	import { page } from '$app/stores';

	let { data } = $props();
</script>

<div class="bg-white">
	<div class="mx-auto max-w-2xl px-4 py-10 sm:px-6 sm:py-16 lg:max-w-7xl lg:px-8">
		<h2 class="text-2xl mb-10 font-bold tracking-tight text-gray-900">
			{$page.params.category_name}
		</h2>
		<div class="grid grid-cols-1 gap-x-6 gap-y-10 sm:grid-cols-2 lg:grid-cols-4 xl:gap-x-8">
			{#each data.products as product}
				<div class="group relative">
					<img
						src={product.imageUrl}
						alt={product.name}
						class="aspect-square w-full rounded-md border bg-gray-200 object-cover group-hover:opacity-75 lg:aspect-auto lg:h-80"
					/>
					<div class="mt-4 flex justify-between">
						<div>
							<h3 class="text-sm text-gray-700">
								<a href="/panel/product/{product.id}" class="font-medium text-gray-900">
									<span aria-hidden="true" class="absolute inset-0"></span>
									{product.name}
								</a>
							</h3>
							<p class="mt-1 text-sm text-gray-500">{product.categoryName}</p>
						</div>
						<p class="text-sm font-medium text-gray-900">
							{product.pricePerDay.toLocaleString('bg-BG', { style: 'currency', currency: 'BGN' })}
						</p>
					</div>
				</div>
			{/each}
		</div>
	</div>
</div>
