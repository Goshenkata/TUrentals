<script lang="ts">
	import Ellipsis from 'lucide-svelte/icons/ellipsis';
	import * as DropdownMenu from '$lib/components/ui/dropdown-menu';
	import { Button, buttonVariants } from '$lib/components/ui/button';
	import { Pen } from 'lucide-svelte';
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import * as Form from '$lib/components/ui/form';
	import { Input } from '$lib/components/ui/input';
	import type { SuperValidated } from 'sveltekit-superforms';
	import { superForm } from 'sveltekit-superforms';
	import { changeQuantitySchema, type ChangeQuantitySchema } from './schema';
	import { zodClient } from 'sveltekit-superforms/adapters';
	import { getContext } from 'svelte';
	import type { OrderLineItem } from '$lib/types';

	type Props = {
		lineItem: OrderLineItem;
	};
	let { lineItem }: Props = $props();

	const changeQuantityFormAndData: SuperValidated<ChangeQuantitySchema> =
		getContext('changeQuantityForm');

	const changeQuantityForm = superForm(changeQuantityFormAndData, {
		validators: zodClient(changeQuantitySchema),
		resetForm: false,
		dataType: 'json',
		id: `change-status-form-${lineItem.item.id}`
	});

	const {
		form: changeQuantityFormData,
		enhance: changeQuantityEnhanced,
		delayed: changeQuantityDelayed
	} = changeQuantityForm;

	$changeQuantityFormData.itemId = lineItem.item.id;
	$changeQuantityFormData.newQuantity = lineItem.quantity;

	let openQuantityDialog = $state(false);
</script>

<DropdownMenu.Root>
	<DropdownMenu.Trigger class="{buttonVariants({ variant: 'ghost', size: 'icon' })} h-8 w-8">
		<span class="sr-only">Отвори менюто</span>
		<Ellipsis class="h-4 w-4" />
	</DropdownMenu.Trigger>
	<DropdownMenu.Content>
		<DropdownMenu.Group>
			<DropdownMenu.Label>Действия</DropdownMenu.Label>
		</DropdownMenu.Group>

		<DropdownMenu.Item
			class="cursor-pointer"
			onclick={() => {
				openQuantityDialog = true;
			}}
		>
			<Pen class="max-w-5"></Pen>
			<span> Промени количество </span>
		</DropdownMenu.Item>
	</DropdownMenu.Content>
</DropdownMenu.Root>

<Dialog.Root bind:open={openQuantityDialog}>
	<Dialog.Content class="mx-auto max-w-md w-full">
		<Dialog.Header>
			<Dialog.Title>Смяна на количество</Dialog.Title>
		</Dialog.Header>
		<form method="POST" action="?/changeQuantity" use:changeQuantityEnhanced class="grid gap-4">
			<Form.Field form={changeQuantityForm} name="newQuantity">
				<Form.Control>
					{#snippet children({ props })}
						<Form.Label>Ново количество</Form.Label>

						<Input
							{...props}
							disabled={$changeQuantityDelayed}
							bind:value={$changeQuantityFormData.newQuantity}
							type="number"
						/>
					{/snippet}
				</Form.Control>
				<Form.FieldErrors />
			</Form.Field>

			<Dialog.Footer>
				<div class="flex w-full justify-center gap-4">
					<Form.Button type="submit" disabled={$changeQuantityDelayed}>Запази</Form.Button>

					<Button
						onclick={() => {
							openQuantityDialog = false;
						}}
						disabled={$changeQuantityDelayed}
						variant="outline"
					>
						Отказ
					</Button>
				</div>
			</Dialog.Footer>
		</form>
	</Dialog.Content>
</Dialog.Root>
