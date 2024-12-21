<script lang="ts">
	let { data } = $props();
</script>

<h1 class=" text-3xl">Административен панел</h1>

<div class="grid grid-cols-3 mt-8 gap-6">
	{#if data.user?.role === 'EMPLOYEE'}
		<a
			href="/dashboard/orders/assigned"
			class="border rounded-lg p-4 shadow-md hover:border-indigo-400 transition-colors"
		>
			<h2 class="text-xl font-semibold">Моите поръчки</h2>
			<p class="text-sm text-gray-500">
				Вижте и управлявайте поръчките, които трябва да обработите
			</p>
		</a>
	{/if}

	{#if data.user?.role === 'MANAGER'}
		<a
			href="/dashboard/orders/pending"
			class="border rounded-lg p-4 shadow-md hover:border-indigo-400 transition-colors"
		>
			<h2 class="text-xl font-semibold">Непотвърдени поръчки</h2>
			<p class="text-sm text-gray-500">Вижте и управлявайте непотвърдените поръчки</p>
		</a>
	{/if}

	{#if data.user?.role === 'MANAGER'}
		<a
			href="/dashboard/inventory"
			class="border rounded-lg p-4 shadow-md hover:border-indigo-400 transition-colors"
		>
			<h2 class="text-xl font-semibold">Инвентар</h2>
			<p class="text-sm text-gray-500">Вижте и управлявайте инвентара</p>
		</a>
	{/if}

	{#if data.user?.role === 'MANAGER'}
		<a
			href="/dashboard/catalog/categories"
			class="border rounded-lg p-4 shadow-md hover:border-indigo-400 transition-colors"
		>
			<h2 class="text-xl font-semibold">Категории</h2>
			<p class="text-sm text-gray-500">Вижте и управлявайте наличните категории</p>
		</a>
	{/if}

	{#if data.user?.role === 'MANAGER'}
		<a
			href="/dashboard/catalog/products"
			class="border rounded-lg p-4 shadow-md hover:border-indigo-400 transition-colors"
		>
			<h2 class="text-xl font-semibold">Продукти</h2>
			<p class="text-sm text-gray-500">Вижте и управлявайте наличните продукти</p>
		</a>
	{/if}

	{#if data.user?.role === 'ADMIN'}
		<a
			href="/dashboard/users"
			class="border rounded-lg p-4 shadow-md hover:border-indigo-400 transition-colors"
		>
			<h2 class="text-xl font-semibold">Потребители</h2>
			<p class="text-sm text-gray-500">Вижте и управлявайте потребителите</p>
		</a>
	{/if}
</div>
