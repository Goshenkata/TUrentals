<script lang="ts">
	import CategoriesTable from './orders-table.svelte';
	import Button from '$lib/components/ui/button/button.svelte';
	import { ChevronLeft } from 'lucide-svelte';
	import * as Dialog from '$lib/components/ui/dialog';
	import * as Form from '$lib/components/ui/form/index.js';
	import { Input } from '$lib/components/ui/input/index.js';
	import * as Card from '$lib/components/ui/card';
	import Label from '$lib/components/ui/label/label.svelte';
	import { setContext } from 'svelte';

	let { data, form } = $props();

	setContext('assignEmployeeForm', data.assignEmployeeForm);
</script>

<div
	class="z-50 -mx-2 flex items-center justify-between gap-4 bg-background px-2 py-1.5 md:sticky md:top-0 md:-mx-4 md:px-4 lg:-mx-6 lg:px-6"
>
	<div class="flex items-center justify-between gap-4">
		<Button variant="outline" size="icon" class="h-7 w-7 flex-shrink-0" href="/control-centre-v1">
			<ChevronLeft class="h-4 w-4" />
			<span class="sr-only">Назад</span>
		</Button>
		<h1 class="text-lg font-semibold md:text-2xl">Чакащи поръчки</h1>
	</div>
</div>

{#if data.orders && data.orders.length > 0}
	<div class="">
		{#key data.orders}
			<CategoriesTable orders={data.orders}></CategoriesTable>
		{/key}
	</div>
{/if}
