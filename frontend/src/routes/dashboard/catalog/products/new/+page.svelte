<script lang="ts">
	import { Button } from '$lib/components/ui/button';
	import * as Card from '$lib/components/ui/card';
	import { Input } from '$lib/components/ui/input';
	import { Label } from '$lib/components/ui/label';
	import * as Select from '$lib/components/ui/select';
	import { ChevronLeft, Upload, FileCheck, X } from 'lucide-svelte';
	import * as Form from '$lib/components/ui/form';
	import { superForm } from 'sveltekit-superforms';
	import { zodClient } from 'sveltekit-superforms/adapters';
	import { newProductSchema } from './schema.js';
	import { toast } from 'svelte-sonner';
	import { goto } from '$app/navigation';
	import Dropzone from '$lib/components/dropzone/index.js';
	import { SortByEnum } from '$lib/enums.js';
	import { Textarea } from '$lib/components/ui/textarea/index.js';

	const { data, form } = $props();

	const newProductForm = superForm(data.newProductForm, {
		validators: zodClient(newProductSchema),
		dataType: 'json',
		resetForm: false
	});

	const { form: formData, enhance, errors, delayed } = newProductForm;

	let isImageUploaded = $state(false);

	const showPreviewOnDrop = async (event: any) => {
		const { acceptedFiles, fileRejections } = event.detail;

		const files = [...acceptedFiles];

		if (files && files.length > 0) {
			isImageUploaded = true;
			$formData.image = files[0];

			const src = URL.createObjectURL(files[0]);
			const preview = document.getElementById('image-preview') as HTMLImageElement;
			preview.classList.remove('sr-only');
			preview.src = src;
		}
	};

	const deletePreview = () => {
		isImageUploaded = false;
		$formData.image = undefined;

		const preview = document.getElementById('image-preview') as HTMLImageElement;
		preview.classList.add('sr-only');
	};

	$effect(() => {
		if (form?.createProductSuccess) {
			toast.success('Създадена нов продукт.');

			goto('/dashboard/catalog/products');
		}

		if (form?.errorCreateProduct) {
			toast.error('Възникна грешка при създаването на продукта.');
		}
	});
</script>

<form action="?/createProduct" method="POST" use:enhance enctype="multipart/form-data">
	<div class="mx-auto grid flex-1 auto-rows-max gap-4">
		<div
			class="flex items-center justify-between z-50 gap-4 md:sticky md:top-0 -mx-2 md:-mx-4 lg:-mx-6 px-2 md:px-4 lg:px-6 bg-background py-1.5"
		>
			<div class="flex items-center justify-between gap-4">
				<Button
					variant="outline"
					size="icon"
					class="h-7 w-7 flex-shrink-0"
					href="/dashboard/catalog/categories"
				>
					<ChevronLeft class="h-4 w-4" />
					<span class="sr-only">Назад</span>
				</Button>
				<h1 class="text-lg font-semibold md:text-2xl">Нов продукт</h1>
			</div>

			<div class="hidden items-center gap-2 md:ml-auto md:flex">
				<Button variant="outline" size="sm" href="/dashboard/catalog/categories">Отказ</Button>
				<Form.Button disabled={$delayed} type="submit" size="sm" class="flex gap-2 items-center">
					<FileCheck class="w-4 h-4" /> Запази
				</Form.Button>
			</div>
		</div>
		<div class="grid gap-4 lg:grid-cols-3 lg:gap-8">
			<div class="grid place-content-start justify-stretch gap-4 lg:col-span-2 lg:gap-8">
				<Card.Root>
					<Card.Header>
						<Card.Title>Основна информация</Card.Title>
					</Card.Header>
					<Card.Content>
						<div class="grid gap-6">
							<div class="grid md:gap-6 md:grid-cols-2">
								<Form.Field form={newProductForm} name="name">
									<Form.Control>
										{#snippet children({ props })}
											<Form.Label>Име на продукта</Form.Label>
											<Input
												{...props}
												disabled={$delayed}
												required
												bind:value={$formData.name}
												type="text"
												placeholder="Роботи..."
											/>
										{/snippet}
									</Form.Control>
									<Form.FieldErrors />
								</Form.Field>

								<Form.Field form={newProductForm} name="pricePerDay">
									<Form.Control>
										{#snippet children({ props })}
											<Form.Label>Цена на ден</Form.Label>
											<Input
												{...props}
												disabled={$delayed}
												required
												bind:value={$formData.pricePerDay}
												type="number"
												min="0"
												step="0.01"
											/>
										{/snippet}
									</Form.Control>
									<Form.FieldErrors />
								</Form.Field>
							</div>
						</div>
						<div class="grid gap-6 sm:grid-cols-2">
							<Form.Field form={newProductForm} name="categoryId">
								<Form.Control>
									{#snippet children({ props })}
										<Form.Label>Категория</Form.Label>
										<Select.Root
											{...props}
											type="single"
											value={String($formData.categoryId)}
											disabled={$delayed}
											allowDeselect={false}
											controlledValue={true}
											onValueChange={(v) => {
												$formData.categoryId = parseInt(v);
											}}
										>
											<Select.Trigger id="categoryId" aria-label="Категория">
												{data.categories.find((c) => c.id === $formData.categoryId)?.name ||
													'Избери категория'}
											</Select.Trigger>

											<Select.Content>
												{#each data.categories as category}
													<Select.Item value={category.id.toString()} label={category.name} />
												{/each}
											</Select.Content>
										</Select.Root>
									{/snippet}
								</Form.Control>
								<Form.FieldErrors />
							</Form.Field>
							<Form.Field form={newProductForm} name="initialQuantity">
								<Form.Control>
									{#snippet children({ props })}
										<Form.Label>Количество</Form.Label>
										<Input
											{...props}
											disabled={$delayed}
											required
											bind:value={$formData.initialQuantity}
											type="number"
											min="0"
											step="0.01"
										/>
									{/snippet}
								</Form.Control>
								<Form.FieldErrors />
							</Form.Field>
						</div>
						<div>
							<Form.Field form={newProductForm} name="description">
								<Form.Control>
									{#snippet children({ props })}
										<Form.Label>Описание</Form.Label>
										<Textarea
											{...props}
											disabled={$delayed}
											required
											bind:value={$formData.description}
											rows={13}
										/>
									{/snippet}
								</Form.Control>
								<Form.FieldErrors />
							</Form.Field>
						</div>
					</Card.Content>
				</Card.Root>
			</div>
			<div class="grid auto-rows-max items-start gap-4 lg:gap-8">
				<Card.Root class="overflow-hidden">
					<Card.Header>
						<Card.Title>Изображение на продукта</Card.Title>
					</Card.Header>
					<Card.Content>
						<div class="grid gap-2 relative">
							{#if isImageUploaded}
								<Button
									variant="destructive"
									class="absolute z-20 top-1 right-1 flex items-center justify-center p-3 rounded-full h-auto"
									onclick={deletePreview}
									><X class="w-6 h-6"></X>
								</Button>
							{/if}
							<Dropzone
								name="logo"
								id="logo"
								type="file"
								accept="image/*"
								disabled={$delayed}
								multiple={false}
								on:drop={showPreviewOnDrop}
							>
								{#if !isImageUploaded}
									<Upload class="absolute text-muted-foreground h-4 w-4" />
									<span class="sr-only">Качи снимка</span>
								{/if}
								<img
									alt="Лого"
									class="z-10 rounded-md object-contain max-h-full sr-only"
									id="image-preview"
								/>
							</Dropzone>
						</div>

						{#if $errors.image}
							<p class="text-destructive">{$errors.image}</p>
						{/if}
					</Card.Content>
				</Card.Root>
			</div>
		</div>
		<div class="flex items-center justify-center gap-2 md:hidden">
			<Button variant="outline" size="sm" href="/dashboard/catalog/categories">Отказ</Button>
			<Form.Button disabled={$delayed} type="submit" size="sm" class="flex gap-2 items-center">
				<FileCheck class="w-4 h-4" /> Запази
			</Form.Button>
		</div>
	</div>
</form>
