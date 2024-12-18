<script lang="ts">
	import Checkbox from '$lib/components/ui/checkbox/checkbox.svelte';
	import ScrollArea from '$lib/components/ui/scroll-area/scroll-area.svelte';
	import * as Table from '$lib/components/ui/table/index.js';
	import CategoryActions from './product-actions.svelte';
	import MassActions from './mass-actions.svelte';
	import type { ReturnedProduct } from '$lib/types';

	type Props = {
		products: ReturnedProduct[];
	};
	let { products }: Props = $props();

	let checked: number[] = $state([]);
	let checkedAll = $derived(checked.length === products.length);
</script>

{#snippet productRow(product: ReturnedProduct)}
	<Table.Row>
		<Table.Cell class="">
			<Checkbox
				value={product.id.toString()}
				checked={checked.includes(product.id) || checkedAll}
				onCheckedChange={() => {
					if (checked.includes(product.id)) checked = checked.filter((e) => e !== product.id);
					else checked.push(product.id);
				}}
			/>
		</Table.Cell>

		<Table.Cell class="">
			<div class="w-[40px] h-[40px] overflow-hidden rounded-sm">
				<img
					class="object-contain"
					width="40px"
					height="40px"
					src={product.imageUrl ?? 'https://placehold.co/40'}
					alt=""
				/>
			</div>
		</Table.Cell>

		<Table.Cell class="">
			<span>
				{product.name}
			</span>
		</Table.Cell>

		<Table.Cell class="">
			<span>
				{product.categoryName}
			</span>
		</Table.Cell>

		<Table.Cell class="font-medium text-right pr-5">
			<span>
				{product.pricePerDay.toLocaleString('bg-BG', { style: 'currency', currency: 'BGN' })}
			</span>
		</Table.Cell>

		<Table.Cell class="text-center">
			<CategoryActions {product}></CategoryActions>
		</Table.Cell>
	</Table.Row>
{/snippet}

<div class="mb-3 flex flex-col gap-3 md:flex-row">
	<MassActions {checked} {products}></MassActions>
</div>

<ScrollArea orientation="horizontal" class="mx-auto max-w-[94dvw] rounded-xl border p-2">
	<Table.Root class="">
		<Table.Header>
			<Table.Row class="!bg-background hover:bg-background">
				<Table.Head class="w-[30px]">
					<Checkbox
						checked={checkedAll}
						onCheckedChange={(isChecked) => {
							if (isChecked) {
								checked = products.map((cat) => cat.id);
							} else {
								checked = [];
							}
						}}
					/>
				</Table.Head>
				<Table.Head class="w-[50px]"></Table.Head>
				<Table.Head class="font-bold">Име</Table.Head>
				<Table.Head class="font-bold">Категория</Table.Head>
				<Table.Head class="font-bold text-right pr-5">Цена на ден</Table.Head>

				<Table.Head class="w-[40px]"></Table.Head>
			</Table.Row>
		</Table.Header>
		<Table.Body class="hover:[tr:has(table)]:bg-background">
			{#each products as product (product?.id)}
				{@render productRow(product)}
			{/each}
		</Table.Body>
	</Table.Root>
</ScrollArea>
