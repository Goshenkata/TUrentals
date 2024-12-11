<script lang="ts">
	import Ellipsis from 'lucide-svelte/icons/ellipsis';
	import * as DropdownMenu from '$lib/components/ui/dropdown-menu';
	import { Button, buttonVariants } from '$lib/components/ui/button';
	import { ClipboardCopy, Trash2, PenBox } from 'lucide-svelte';
	import { enhance } from '$app/forms';
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import * as Form from '$lib/components/ui/form';
	import * as Select from '$lib/components/ui/select';
	import { Input } from '$lib/components/ui/input';
	import type { SuperValidated } from 'sveltekit-superforms';
	import { superForm } from 'sveltekit-superforms';
	import {
		deleteUserSchema,
		editUserSchema,
		type DeleteUserSchema,
		type EditUserSchema
	} from './schema';
	import { zodClient } from 'sveltekit-superforms/adapters';
	import { getContext } from 'svelte';
	import TogglePassword from '$lib/components/TogglePassword.svelte';
	import type { NonNullableUser } from '$lib/types';
	import { Role } from '$lib/enums';

	type Props = {
		user: NonNullableUser;
	};
	let { user }: Props = $props();

	const editUserFormData: SuperValidated<EditUserSchema> = getContext('editUserForm');
	const deleteUserFormData: SuperValidated<DeleteUserSchema> = getContext('editUserForm');

	const editUserForm = superForm(editUserFormData, {
		validators: zodClient(editUserSchema),
		resetForm: false
	});

	const deleteUserForm = superForm(deleteUserFormData, {
		validators: zodClient(deleteUserSchema),
		resetForm: true,
		dataType: 'json'
	});

	const { form: editFormData, enhance: editEnhace, delayed: editDelayed } = editUserForm;

	const { form: deleteFormData, enhance: deleteEnhace, delayed: deleteDelayed } = deleteUserForm;

	$deleteFormData.email = user.email;

	$editFormData.id = user.id;
	$editFormData.firstName = user.firstName;
	$editFormData.lastName = user.lastName;
	$editFormData.phoneNumber = user.phoneNumber;
	$editFormData.email = user.email;
	$editFormData.password = '';
	$editFormData.role = user.role;

	let open = $state(false);
	let openEdit = $state(false);
	let toggled = $state(false);
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
				onclick={() => navigator.clipboard.writeText(user.id.toString())}
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
					>{user.firstName} {user.lastName} - {user.email}</span
				>
			</Dialog.Description>
		</Dialog.Header>

		<Dialog.Footer>
			<div class="flex w-full justify-center gap-4">
				<form action="?/deleteUser" method="POST" use:enhance>
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
			<Dialog.Title>Редактиране на потребител</Dialog.Title>
		</Dialog.Header>
		<form method="POST" action="?/editUser" use:editEnhace class="grid gap-4">
			<div class="grid gap-4 grid-cols-2">
				<Form.Field form={editUserForm} name="firstName">
					<Form.Control>
						{#snippet children({ props })}
							<Form.Label>Име</Form.Label>
							<Input
								{...props}
								disabled={$editDelayed}
								bind:value={$editFormData.firstName}
								type="text"
							/>
						{/snippet}
					</Form.Control>
					<Form.FieldErrors />
				</Form.Field>

				<Form.Field form={editUserForm} name="lastName">
					<Form.Control>
						{#snippet children({ props })}
							<Form.Label>Фамилия</Form.Label>
							<Input
								{...props}
								disabled={$editDelayed}
								bind:value={$editFormData.lastName}
								type="text"
							/>
						{/snippet}
					</Form.Control>
					<Form.FieldErrors />
				</Form.Field>
			</div>

			<Form.Field form={editUserForm} name="role">
				<Form.Control>
					{#snippet children({ props })}
						<Form.Label>Роля</Form.Label>
						<Select.Root type="single" bind:value={$editFormData.role} name={props.name}>
							<Select.Trigger {...props}>
								{$editFormData.role ? $editFormData.role : 'Иберете роля'}
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

			<Form.Field form={editUserForm} name="phoneNumber">
				<Form.Control>
					{#snippet children({ props })}
						<Form.Label>Телефон</Form.Label>
						<Input
							{...props}
							disabled={$editDelayed}
							bind:value={$editFormData.phoneNumber}
							type="text"
							placeholder="088...."
						/>
					{/snippet}
				</Form.Control>
				<Form.FieldErrors />
			</Form.Field>

			<Form.Field form={editUserForm} name="email">
				<Form.Control>
					{#snippet children({ props })}
						<Form.Label>Имейл</Form.Label>
						<Input
							{...props}
							disabled={$editDelayed}
							bind:value={$editFormData.email}
							type="email"
							placeholder="m@example.com"
						/>
					{/snippet}
				</Form.Control>
				<Form.FieldErrors />
			</Form.Field>

			<Form.Field form={editUserForm} name="password">
				<Form.Control>
					{#snippet children({ props })}
						<Form.Label>Парола</Form.Label>

						<div class="relative">
							<Input
								{...props}
								disabled={$editDelayed}
								bind:value={$editFormData.password}
								type={toggled ? 'text' : 'password'}
								required
							/>

							<button
								onclick={() => {
									toggled = !toggled;
								}}
								type="button"
								disabled={$editDelayed}
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
