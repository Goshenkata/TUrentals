<script lang="ts">
	import ProductsTable from './products-table.svelte';
	import Button from '$lib/components/ui/button/button.svelte';
	import { ChevronLeft, PlusCircle } from 'lucide-svelte';
	import { Input } from '$lib/components/ui/input/index.js';
	import * as Card from '$lib/components/ui/card';
	import Label from '$lib/components/ui/label/label.svelte';
	import { page } from '$app/stores';
	import { goto } from '$app/navigation';
	import { generateUrlParams } from '$lib/utils.js';
	import * as Select from '$lib/components/ui/select';
	import { SortByEnum } from '$lib/enums';

	let { data } = $props();

	let selectedName = $state($page.url.searchParams.get('nameQuery') || '');
	let selectedPriceFrom = $state($page.url.searchParams.get('priceFrom') || '');
	let selectedPriceTo = $state($page.url.searchParams.get('priceTo') || '');
	let selectedCategory = $state($page.url.searchParams.get('category') || '');
	let sortBy = $state($page.url.searchParams.get('sortBy') || '');

	const initSearch = async () => {
		await goto(
			$page.url.pathname +
				generateUrlParams({
					nameQuery: selectedName,
					priceFrom: selectedPriceFrom,
					priceTo: selectedPriceTo,
					category: selectedCategory,
					sortBy
				})
		);
	};

	const clearFilters = async () => {
		selectedName = '';
		selectedPriceFrom = '';
		selectedPriceTo = '';
		selectedCategory = '';
		sortBy = '';

		await goto($page.url.pathname);
	};
</script>

<div
	class="z-50 -mx-2 flex items-center justify-between gap-4 bg-background px-2 py-1.5 md:sticky md:top-0 md:-mx-4 md:px-4 lg:-mx-6 lg:px-6"
>
	<div class="flex items-center justify-between gap-4">
		<Button variant="outline" size="icon" class="h-7 w-7 flex-shrink-0" href="/dashboard">
			<ChevronLeft class="h-4 w-4" />
			<span class="sr-only">Назад</span>
		</Button>
		<h1 class="text-lg font-semibold md:text-2xl">Продукти</h1>
	</div>

	<Button size="sm" class="flex items-center gap-2" href="/dashboard/catalog/products/new">
		<PlusCircle class="h-5 w-5 max-md:h-4 max-md:w-4" /> Добави продукт
	</Button>
</div>

<Card.Root class="mx-auto w-full">
	<Card.Header>
		<Card.Title>Филтри</Card.Title>
	</Card.Header>
	<Card.Content class="grid md:grid-cols-2 xl:grid-cols-5 gap-6">
		<div class="grid gap-3">
			<Label>Име</Label>
			<Input bind:value={selectedName}></Input>
		</div>
		<div class="grid gap-3">
			<Label>Цена от</Label>
			<Input type="number" min="0" step="0.01" bind:value={selectedPriceFrom}></Input>
		</div>
		<div class="grid gap-3">
			<Label>Цена до</Label>
			<Input type="number" min="0" step="0.01" bind:value={selectedPriceTo}></Input>
		</div>
		<div class="grid gap-3">
			<Label>Категория</Label>
			<Select.Root type="single" bind:value={selectedCategory}>
				<Select.Trigger class="">{selectedCategory}</Select.Trigger>
				<Select.Content>
					{#each data.categories as category}
						<Select.Item value={category.name} label={category.name} />
					{/each}
				</Select.Content>
			</Select.Root>
		</div>
		<div class="grid gap-3">
			<Label>Подреди по</Label>

			<Select.Root type="single" bind:value={sortBy}>
				<Select.Trigger class="">{SortByEnum[sortBy as keyof typeof SortByEnum]}</Select.Trigger>
				<Select.Content>
					{#each Object.keys(SortByEnum) as sort}
						<Select.Item value={sort} label={SortByEnum[sort as keyof typeof SortByEnum]} />
					{/each}
				</Select.Content>
			</Select.Root>
		</div>
	</Card.Content>
	<Card.Footer class="flex flex-col sm:flex-row gap-2 flex-wrap justify-between">
		<Button onclick={initSearch} class="max-sm:w-full">Търси</Button>
		<Button variant="destructive" onclick={clearFilters} class="max-sm:w-full">Изчисти</Button>
	</Card.Footer>
</Card.Root>

{#if data.products && data.products.length > 0}
	<div class="">
		{#key data.products}
			<ProductsTable products={data.products}></ProductsTable>
		{/key}
	</div>
{:else}
	<div class="flex flex-1 items-center justify-center rounded-lg border border-dashed shadow-sm">
		<div class="flex flex-col items-center gap-1 text-center">
			<h3 class="text-2xl font-bold tracking-tight">
				{'Няма зададени видове МПС'}
			</h3>

			<Button class="mt-4" href="/dashboard/catalog/products/new">Създай продукт</Button>
		</div>
	</div>
{/if}
