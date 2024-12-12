<script lang="ts">
	import Ellipsis from 'lucide-svelte/icons/ellipsis';
	import * as DropdownMenu from '$lib/components/ui/dropdown-menu';
	import { Button, buttonVariants } from '$lib/components/ui/button';
	import { ClipboardCopy, Trash2, PenBox } from 'lucide-svelte';
	import { enhance } from '$app/forms';
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import * as Form from '$lib/components/ui/form';
	import { Input } from '$lib/components/ui/input';
	import type { SuperValidated } from 'sveltekit-superforms';
	import { superForm } from 'sveltekit-superforms';
	import {
		deleteCategorySchema,
		editCategorySchema,
		type DeleteCategorySchema,
		type EditCategorySchema
	} from './schema';
	import { zodClient } from 'sveltekit-superforms/adapters';
	import { getContext } from 'svelte';
	import type { Category } from '$lib/types';

	type Props = {
		category: Category;
	};
	let { category }: Props = $props();

	const editCategoryFormData: SuperValidated<EditCategorySchema> = getContext('editCategoryForm');
	const deleteCategoryFormData: SuperValidated<DeleteCategorySchema> =
		getContext('editCategoryForm');

	const editCategoryForm = superForm(editCategoryFormData, {
		validators: zodClient(editCategorySchema),
		resetForm: false
	});

	const deleteCategoryForm = superForm(deleteCategoryFormData, {
		validators: zodClient(deleteCategorySchema),
		resetForm: true,
		dataType: 'json'
	});

	const { form: editFormData, enhance: editEnhace, delayed: editDelayed } = editCategoryForm;

	const {
		form: deleteFormData,
		enhance: deleteEnhace,
		delayed: deleteDelayed
	} = deleteCategoryForm;

	$deleteFormData.id = category.id;

	$editFormData.id = category.id;
	$editFormData.name = category.name;

	let open = $state(false);
	let openEdit = $state(false);
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
				onclick={() => navigator.clipboard.writeText(category.id.toString())}
				class="cursor-pointer"
			>
				<ClipboardCopy class="max-w-5"></ClipboardCopy>
				<span> Копирай ID </span>
			</DropdownMenu.Item>
		</DropdownMenu.Group>

		<DropdownMenu.Item
			class="cursor-pointer"
			onclick={() => {
				openEdit = true;
			}}
		>
			<PenBox class="max-w-5"></PenBox>
			<span> Редактиране </span>
		</DropdownMenu.Item>

		<DropdownMenu.Separator></DropdownMenu.Separator>
		<DropdownMenu.Item
			class="{buttonVariants({ variant: 'destructive' })} h-8 w-full cursor-pointer p-0"
			onclick={() => {
				open = true;
			}}
		>
			<Trash2 class="ml-0 mr-2 max-w-5"></Trash2>
			Изтрий
		</DropdownMenu.Item>
	</DropdownMenu.Content>
</DropdownMenu.Root>

<Dialog.Root bind:open>
	<Dialog.Content class="sm:max-w-[425px]">
		<Dialog.Header>
			<Dialog.Title class="text-center leading-6">Внимание!</Dialog.Title>
			<Dialog.Description class="text-center">
				Сигурни ли сте че сикате да изтриете: <span class="font-mono font-bold"
					>{category.name}</span
				>
			</Dialog.Description>
		</Dialog.Header>

		<Dialog.Footer>
			<div class="flex w-full justify-center gap-4">
				<form action="?/deleteCategory" method="POST" use:enhance>
					<Button
						type="submit"
						onclick={() => {
							open = false;
						}}
						variant="destructive">Изтрий</Button
					>
				</form>
				<Button
					onclick={() => {
						open = false;
					}}
					class="border-0"
				>
					Отказ
				</Button>
			</div>
		</Dialog.Footer>
	</Dialog.Content>
</Dialog.Root>

<Dialog.Root bind:open={openEdit}>
	<Dialog.Content class="mx-auto max-w-md w-full">
		<Dialog.Header>
			<Dialog.Title>Редактиране на категория</Dialog.Title>
		</Dialog.Header>
		<form method="POST" action="?/editCategory" use:editEnhace class="grid gap-4">
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
</Dialog.Root>
