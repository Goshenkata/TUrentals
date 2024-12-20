<script lang="ts">
	import { superForm } from 'sveltekit-superforms';
	import { newCategorySchema } from './schema.js';
	import { zodClient } from 'sveltekit-superforms/adapters';
	import CategoriesTable from './categories-table.svelte';
	import Button from '$lib/components/ui/button/button.svelte';
	import { ChevronLeft, PlusCircle } from 'lucide-svelte';
	import * as Dialog from '$lib/components/ui/dialog';
	import * as Form from '$lib/components/ui/form/index.js';
	import { toast } from 'svelte-sonner';
	import { Input } from '$lib/components/ui/input/index.js';
	import { setContext } from 'svelte';

	import * as Card from '$lib/components/ui/card';
	import Label from '$lib/components/ui/label/label.svelte';
	import { page } from '$app/stores';
	import { goto } from '$app/navigation';
	import { generateUrlParams } from '$lib/utils.js';

	let { data, form } = $props();

	let openCreate = $state(false);

	const newCategoryForm = superForm(data.newCategoryForm, {
		validators: zodClient(newCategorySchema),
		resetForm: false
	});

	setContext('editCategoryForm', data.editCategoryForm);
	setContext('deleteCategoryForm', data.deleteCategoryForm);

	const { form: formData, enhance: createEnhance, delayed } = newCategoryForm;

	let selectedName = $state($page.url.searchParams.get('query') || '');

	const initSearch = async () => {
		await goto($page.url.pathname + generateUrlParams({ query: selectedName }));
	};

	const clearFilters = async () => {
		selectedName = '';
		await goto($page.url.pathname);
	};

	$effect(() => {
		if (form?.createCategorySuccess) {
			toast.success('Успешно създадена категория.');
			newCategoryForm.reset();
			openCreate = false;
		}

		if (form?.categoryExists) {
			toast.error('Тази категория вече съществува.');
		}

		if (form?.errorCreateCategory) {
			toast.error('Възникна грешка при създаването на категорията.');
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
		<h1 class="text-lg font-semibold md:text-2xl">Категории</h1>
	</div>

	<Button
		size="sm"
		onclick={() => {
			openCreate = true;
		}}
		class="flex items-center gap-2 "
		><PlusCircle class="h-5 w-5 max-md:h-4 max-md:w-4" /> Добави категория</Button
	>
</div>

<Card.Root class="mx-auto w-full">
	<Card.Header>
		<Card.Title>Филтри</Card.Title>
	</Card.Header>
	<Card.Content class="grid">
		<div class="grid gap-3">
			<Label>Категория</Label>
			<Input bind:value={selectedName}></Input>
		</div>
	</Card.Content>
	<Card.Footer class="flex flex-col sm:flex-row gap-2 flex-wrap justify-between">
		<Button onclick={initSearch} class="max-sm:w-full">Търси</Button>
		<Button variant="destructive" onclick={clearFilters} class="max-sm:w-full">Изчисти</Button>
	</Card.Footer>
</Card.Root>

{#if data.categories && data.categories.length > 0}
	<div class="">
		{#key data.categories}
			<CategoriesTable categories={data.categories}></CategoriesTable>
		{/key}
	</div>
{:else}
	<div class="flex flex-1 items-center justify-center rounded-lg border border-dashed shadow-sm">
		<div class="flex flex-col items-center gap-1 text-center">
			<h3 class="text-2xl font-bold tracking-tight">
				{'Няма зададени видове МПС'}
			</h3>

			<Button
				onclick={() => {
					openCreate = true;
				}}
				class="mt-4">Създай потребител</Button
			>
		</div>
	</div>
{/if}

<Dialog.Root bind:open={openCreate}>
	<Dialog.Content class="mx-auto max-w-md w-full">
		<Dialog.Header>
			<Dialog.Title>Добави категория</Dialog.Title>
		</Dialog.Header>
		<form method="POST" action="?/createCategory" use:createEnhance class="grid gap-4">
			<div class="">
				<Form.Field form={newCategoryForm} name="name">
					<Form.Control>
						{#snippet children({ props })}
							<Form.Label>Име</Form.Label>
							<Input {...props} disabled={$delayed} bind:value={$formData.name} type="text" />
						{/snippet}
					</Form.Control>
					<Form.FieldErrors />
				</Form.Field>
			</div>

			<Dialog.Footer>
				<div class="flex w-full justify-center gap-4">
					<Form.Button type="submit" disabled={$delayed}>Запази</Form.Button>

					<Button
						onclick={() => {
							openCreate = false;
						}}
						disabled={$delayed}
						variant="outline"
					>
						Отказ
					</Button>
				</div>
			</Dialog.Footer>
		</form>
	</Dialog.Content>
</Dialog.Root>
