<script lang="ts">
	import Checkbox from '$lib/components/ui/checkbox/checkbox.svelte';
	import ScrollArea from '$lib/components/ui/scroll-area/scroll-area.svelte';
	import * as Table from '$lib/components/ui/table/index.js';
	import ItemActions from './item-actions.svelte';
	import type { OrderLineItem } from '$lib/types';

	type Props = {
		lineItems: OrderLineItem[];
	};
	let { lineItems }: Props = $props();
</script>

{#snippet itemRow(item: OrderLineItem)}
	<Table.Row>
		<Table.Cell class="">
			<div class="w-[40px] h-[40px] overflow-hidden rounded-sm">
				<img
					class="object-contain"
					width="40px"
					height="40px"
					src={item.item.imageUrl ?? 'https://placehold.co/40'}
					alt=""
				/>
			</div>
		</Table.Cell>

		<Table.Cell class="">
			<span>
				{item.item.name}
			</span>
		</Table.Cell>

		<Table.Cell class="">
			<span>
				{item.item.categoryName}
			</span>
		</Table.Cell>

		<Table.Cell class="font-medium text-right pr-5">
			<span>
				{item.item.pricePerDay.toLocaleString('bg-BG', { style: 'currency', currency: 'BGN' })}
			</span>
		</Table.Cell>

		<Table.Cell class="font-medium text-right pr-5">
			<span>
				{item.quantity} бр.
			</span>
		</Table.Cell>

		<Table.Cell class="text-center">
			<ItemActions lineItem={item}></ItemActions>
		</Table.Cell>
	</Table.Row>
{/snippet}

<ScrollArea orientation="horizontal" class="mx-auto max-w-[94dvw] rounded-xl border p-2">
	<Table.Root class="">
		<Table.Header>
			<Table.Row class="!bg-background hover:bg-background">
				<Table.Head class="w-[50px]"></Table.Head>
				<Table.Head class="font-bold">Име</Table.Head>
				<Table.Head class="font-bold">Категория</Table.Head>
				<Table.Head class="font-bold text-right pr-5">Цена на ден</Table.Head>

				<Table.Head class="font-bold text-right pr-5">Брой</Table.Head>

				<Table.Head class="w-[40px]"></Table.Head>
			</Table.Row>
		</Table.Header>
		<Table.Body class="hover:[tr:has(table)]:bg-background">
			{#each lineItems as lineItem (lineItem.item?.id)}
				{@render itemRow(lineItem)}
			{/each}
		</Table.Body>
	</Table.Root>
</ScrollArea>
