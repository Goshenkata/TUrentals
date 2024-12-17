<script lang="ts">
	import Ellipsis from 'lucide-svelte/icons/ellipsis';
	import * as DropdownMenu from '$lib/components/ui/dropdown-menu';
	import { Button, buttonVariants } from '$lib/components/ui/button';
	import { ClipboardCopy, Trash2, PenBox, CirclePlus } from 'lucide-svelte';
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import * as Form from '$lib/components/ui/form';
	import { Input } from '$lib/components/ui/input';
	import type { SuperValidated } from 'sveltekit-superforms';
	import { superForm } from 'sveltekit-superforms';
	import { assignEmployeeSchema, type AssignEmployeeSchema } from './schema';
	import { zodClient } from 'sveltekit-superforms/adapters';
	import { getContext } from 'svelte';
	import type { PendingOrder } from '$lib/types';

	type Props = {
		order: PendingOrder;
	};
	let { order }: Props = $props();

	const assignEmployeeFormData: SuperValidated<AssignEmployeeSchema> =
		getContext('assignEmployeeForm');

	const assignEmployeeForm = superForm(assignEmployeeFormData, {
		validators: zodClient(assignEmployeeSchema),
		resetForm: true,
		dataType: 'json'
	});

	const { form: formData, enhance, delayed } = assignEmployeeForm;

	let open = $state(false);
</script>

<DropdownMenu.Root>
	<DropdownMenu.Trigger class="{buttonVariants({ variant: 'ghost', size: 'icon' })} h-8 w-8">
		<span class="sr-only">Отвори менюто</span>
		<Ellipsis class="h-4 w-4" />
	</DropdownMenu.Trigger>
	<DropdownMenu.Content>
		<DropdownMenu.Group>
			<DropdownMenu.Label>Действия</DropdownMenu.Label>
			<DropdownMenu.Item
				onclick={() => navigator.clipboard.writeText(order.id.toString())}
				class="cursor-pointer"
			>
				<ClipboardCopy class="max-w-5"></ClipboardCopy>
				<span> Копирай ID </span>
			</DropdownMenu.Item>
		</DropdownMenu.Group>

		<DropdownMenu.Item
			class="cursor-pointer"
			onclick={() => {
				open = true;
			}}
		>
			<CirclePlus class="max-w-5"></CirclePlus>
			<span> Задай изпълнител </span>
		</DropdownMenu.Item>
	</DropdownMenu.Content>
</DropdownMenu.Root>

<!-- <Dialog.Root bind:open>
	<Dialog.Content class="mx-auto max-w-md w-full">
		<Dialog.Header>
			<Dialog.Title>Редактиране на категория</Dialog.Title>
		</Dialog.Header>
		<form method="POST" action="?/editCategory" use:enhance class="grid gap-4">
			<div>
				<Form.Field form={editCategoryForm} name="name">
					<Form.Control>
						{#snippet children({ props })}
							<Form.Label>Име</Form.Label>
							<Input
								{...props}
								disabled={$editDelayed}
								bind:value={$editFormData.name}
								type="text"
							/>
						{/snippet}
					</Form.Control>
					<Form.FieldErrors />
				</Form.Field>
			</div>

			<Dialog.Footer>
				<div class="flex w-full justify-center gap-4">
					<Form.Button type="submit" disabled={$editDelayed}>Запази</Form.Button>

					<Button
						onclick={() => {
							openEdit = false;
						}}
						disabled={$editDelayed}
						variant="outline"
					>
						Отказ
					</Button>
				</div>
			</Dialog.Footer>
		</form>
	</Dialog.Content>
</Dialog.Root> -->
