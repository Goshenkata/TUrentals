<script lang="ts">
	import Checkbox from '$lib/components/ui/checkbox/checkbox.svelte';
	import ScrollArea from '$lib/components/ui/scroll-area/scroll-area.svelte';
	import * as Table from '$lib/components/ui/table/index.js';
	import CategoryActions from './category-actions.svelte';
	import MassActions from './mass-actions.svelte';
	import type { Category } from '$lib/types';

	type Props = {
		categories: Category[];
	};
	let { categories }: Props = $props();

	let checked: number[] = $state([]);
	let checkedAll = $derived(checked.length === categories.length);
</script>

{#snippet categoryRow(category: Category)}
	<Table.Row>
		<Table.Cell class="">
			<Checkbox
				value={category.id.toString()}
				checked={checked.includes(category.id) || checkedAll}
				onCheckedChange={() => {
					if (checked.includes(category.id)) checked = checked.filter((e) => e !== category.id);
					else checked.push(category.id);
				}}
			/>
		</Table.Cell>

		<Table.Cell class="">
			<span>
				{category.name}
			</span>
		</Table.Cell>

		<Table.Cell class="text-center">
			<CategoryActions {category}></CategoryActions>
		</Table.Cell>
	</Table.Row>
{/snippet}

<div class="mb-3 flex flex-col gap-3 md:flex-row">
	<MassActions {checked} {categories}></MassActions>
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
								checked = categories.map((cat) => cat.id);
							} else {
								checked = [];
							}
						}}
					/>
				</Table.Head>
				<Table.Head class="min-w-[10ch]">Име</Table.Head>

				<Table.Head class="w-[40px]"></Table.Head>
			</Table.Row>
		</Table.Header>
		<Table.Body class="hover:[tr:has(table)]:bg-background">
			{#each categories as category (category?.id)}
				{@render categoryRow(category)}
			{/each}
		</Table.Body>
	</Table.Root>
</ScrollArea>
