<script lang="ts">
	import { superForm } from 'sveltekit-superforms';
	import { newUserSchema } from './schema.js';
	import { zodClient } from 'sveltekit-superforms/adapters';
	import UsersTable from './users-table.svelte';
	import Button from '$lib/components/ui/button/button.svelte';
	import { ChevronLeft, ChevronRight, PlusCircle } from 'lucide-svelte';
	import * as Dialog from '$lib/components/ui/dialog';
	import * as Form from '$lib/components/ui/form/index.js';
	import { toast } from 'svelte-sonner';
	import { Input } from '$lib/components/ui/input/index.js';
	import { setContext } from 'svelte';
	import TogglePassword from '$lib/components/TogglePassword.svelte';
	import * as Select from '$lib/components/ui/select/index.js';
	import { Role } from '$lib/enums.js';

	let { data, form } = $props();

	let openCreate = $state(false);

	const newUserForm = superForm(data.newUserForm, {
		validators: zodClient(newUserSchema),
		resetForm: false
	});

	setContext('editUserForm', data.editUserForm);
	setContext('deleteUserForm', data.deleteUserForm);

	const { form: formData, enhance: createEnhance, delayed } = newUserForm;

	let toggled = $state(false);

	$effect(() => {
		if (form?.createUserSuccess) {
			toast.success('Успешно създаден профил.');
			newUserForm.reset();
			openCreate = false;
		}

		if (form?.deleteUserSuccess) {
			toast.success('Успешно изтрит профил.');
		}

		if (form?.userNotUnique) {
			toast.error('Потребител с този имейл вече съществува.');
		}

		if (form?.errorDeleteUser) {
			toast.error('Възникна грешка при изтриването на профила.');
		}
	});
</script>

<div
	class="z-50 -mx-2 flex items-center justify-between gap-4 bg-background px-2 py-1.5 md:sticky md:top-0 md:-mx-4 md:px-4 lg:-mx-6 lg:px-6"
>
	<div class="flex items-center justify-between gap-4">
		<Button variant="outline" size="icon" class="h-7 w-7 flex-shrink-0" href="/control-centre-v1">
			<ChevronLeft class="h-4 w-4" />
			<span class="sr-only">Назад</span>
		</Button>
		<h1 class="text-lg font-semibold md:text-2xl">Екип</h1>
	</div>

	<Button
		size="sm"
		onclick={() => {
			openCreate = true;
		}}
		class="flex items-center gap-2 "
		><PlusCircle class="h-5 w-5 max-md:h-4 max-md:w-4" /> Добави потребител</Button
	>
</div>

{#if data.users && data.users.length > 0}
	<div class="">
		{#key data.users}
			<UsersTable users={data.users}></UsersTable>
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
			<Dialog.Title>Добави потребител</Dialog.Title>
		</Dialog.Header>
		<form method="POST" action="?/registerUser" use:createEnhance class="grid gap-4">
			<div class="grid gap-4 grid-cols-2">
				<Form.Field form={newUserForm} name="firstName">
					<Form.Control>
						{#snippet children({ props })}
							<Form.Label>Име</Form.Label>
							<Input {...props} disabled={$delayed} bind:value={$formData.firstName} type="text" />
						{/snippet}
					</Form.Control>
					<Form.FieldErrors />
				</Form.Field>

				<Form.Field form={newUserForm} name="lastName">
					<Form.Control>
						{#snippet children({ props })}
							<Form.Label>Фамилия</Form.Label>
							<Input {...props} disabled={$delayed} bind:value={$formData.lastName} type="text" />
						{/snippet}
					</Form.Control>
					<Form.FieldErrors />
				</Form.Field>
			</div>

			<Form.Field form={newUserForm} name="role">
				<Form.Control>
					{#snippet children({ props })}
						<Form.Label>Роля</Form.Label>
						<Select.Root type="single" bind:value={$formData.role} name={props.name}>
							<Select.Trigger {...props}>
								{$formData.role ? $formData.role : 'Иберете роля'}
							</Select.Trigger>
							<Select.Content>
								{#each Object.keys(Role) as role}
									<Select.Item value={role} label={role} />
								{/each}
							</Select.Content>
						</Select.Root>
					{/snippet}
				</Form.Control>
				<Form.FieldErrors />
			</Form.Field>

			<Form.Field form={newUserForm} name="phoneNumber">
				<Form.Control>
					{#snippet children({ props })}
						<Form.Label>Телефон</Form.Label>
						<Input
							{...props}
							disabled={$delayed}
							bind:value={$formData.phoneNumber}
							type="text"
							placeholder="088...."
						/>
					{/snippet}
				</Form.Control>
				<Form.FieldErrors />
			</Form.Field>

			<Form.Field form={newUserForm} name="email">
				<Form.Control>
					{#snippet children({ props })}
						<Form.Label>Имейл</Form.Label>
						<Input
							{...props}
							disabled={$delayed}
							bind:value={$formData.email}
							type="email"
							placeholder="m@example.com"
						/>
					{/snippet}
				</Form.Control>
				<Form.FieldErrors />
			</Form.Field>

			<Form.Field form={newUserForm} name="password">
				<Form.Control>
					{#snippet children({ props })}
						<Form.Label>Парола</Form.Label>

						<div class="relative">
							<Input
								{...props}
								disabled={$delayed}
								bind:value={$formData.password}
								type={toggled ? 'text' : 'password'}
								required
							/>

							<button
								onclick={() => {
									toggled = !toggled;
								}}
								type="button"
								disabled={$delayed}
								form="_!"
								class="absolute right-2 top-1.5 text-muted-foreground hover:cursor-pointer"
							>
								<TogglePassword {toggled} />
							</button>
						</div>
					{/snippet}
				</Form.Control>
				<Form.FieldErrors />
			</Form.Field>

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
