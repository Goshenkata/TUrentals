<script lang="ts">
	import ItemsTable from './items-table.svelte';
	import Button from '$lib/components/ui/button/button.svelte';
	import { ChevronLeft } from 'lucide-svelte';
	import { setContext } from 'svelte';
	import { toast } from 'svelte-sonner';

	let { data, form } = $props();

	setContext('changeQuantityForm', data.changeQuantityForm);

	$effect(() => {
		if (form?.changeQuantitySuccess) {
			toast.success('Успешно променено количество.');
		}

		if (form?.errorChangeQuantity) {
			toast.error('Неуспешна промяна на количество.');
		}
	});
</script>

<div
	class="z-50 -mx-2 flex items-center justify-between gap-4 bg-background px-2 py-1.5 md:sticky md:top-0 md:-mx-4 md:px-4 lg:-mx-6 lg:px-6"
>
	<div class="flex items-center justify-between gap-4">
		<Button variant="outline" size="icon" class="h-7 w-7 flex-shrink-0" href="/dashboard">
			<ChevronLeft class="h-4 w-4" />
			<span class="sr-only">Назад</span>
		</Button>
		<h1 class="text-lg font-semibold md:text-2xl">Инвентар</h1>
	</div>
</div>

{#if data.lineItems && data.lineItems.length > 0}
	<div class="">
		{#key data.lineItems}
			<ItemsTable lineItems={data.lineItems}></ItemsTable>
		{/key}
	</div>
{/if}
