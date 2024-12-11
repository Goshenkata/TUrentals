<script lang="ts">
	import * as Card from '$lib/components/ui/card/index.js';
	import { Input } from '$lib/components/ui/input/index.js';
	import * as Form from '$lib/components/ui/form';
	import { superForm } from 'sveltekit-superforms';
	import { zodClient } from 'sveltekit-superforms/adapters';
	import TogglePassword from '$lib/components/TogglePassword.svelte';
	import type { ActionData, PageData } from './$types';
	import { toast } from 'svelte-sonner';
	import { registerSchema } from './schema';

	let { data, form }: { data: PageData; form: ActionData } = $props();

	const registerForm = superForm(data.registerForm, {
		validators: zodClient(registerSchema),
		resetForm: false
	});

	const { form: formData, enhance: registerEnhance, delayed } = registerForm;

	let toggled: boolean = $state(false);

	$effect(() => {
		if (form?.errorRegister) toast.error('Възникна грешка.');
	});

	$effect(() => {
		if (form?.passwordsNotMatching) toast.error('Паролите не съвпадат.');
	});
</script>

<div class="flex h-screen w-full items-center justify-center px-4">
	<Card.Root class="mx-auto max-w-md w-full">
		<Card.Header>
			<Card.Title class="text-2xl">Регистрация</Card.Title>
		</Card.Header>
		<Card.Content>
			<form method="POST" action="?/register" use:registerEnhance class="grid gap-4">
				<div class="grid gap-4 grid-cols-2">
					<Form.Field form={registerForm} name="firstName">
						<Form.Control>
							{#snippet children({ props })}
								<Form.Label>Име</Form.Label>
								<Input
									{...props}
									disabled={$delayed}
									bind:value={$formData.firstName}
									type="text"
								/>
							{/snippet}
						</Form.Control>
						<Form.FieldErrors />
					</Form.Field>

					<Form.Field form={registerForm} name="lastName">
						<Form.Control>
							{#snippet children({ props })}
								<Form.Label>Фамилия</Form.Label>
								<Input {...props} disabled={$delayed} bind:value={$formData.lastName} type="text" />
							{/snippet}
						</Form.Control>
						<Form.FieldErrors />
					</Form.Field>
				</div>

				<div class="grid gap-2">
					<Form.Field form={registerForm} name="phoneNumber">
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
				</div>
				<div class="grid gap-2">
					<Form.Field form={registerForm} name="email">
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
				</div>
				<div class="grid gap-2">
					<Form.Field form={registerForm} name="password">
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
				</div>
				<div class="grid gap-2">
					<Form.Field form={registerForm} name="passwordConfirm">
						<Form.Control>
							{#snippet children({ props })}
								<Form.Label>Потвърди паролата</Form.Label>

								<div class="relative">
									<Input
										{...props}
										disabled={$delayed}
										bind:value={$formData.passwordConfirm}
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
				</div>
				<Form.Button type="submit" disabled={$delayed} class="w-full">Регистрация</Form.Button>
			</form>
			<div class="mt-4 text-center text-sm">
				Вече имаш профил?
				<a href="/register" class="underline"> Вход </a>
			</div>
		</Card.Content>
	</Card.Root>
</div>
