<script lang="ts">
	import * as Card from '$lib/components/ui/card/index.js';
	import { Input } from '$lib/components/ui/input/index.js';
	import * as Form from '$lib/components/ui/form';
	import { superForm } from 'sveltekit-superforms';
	import { zodClient } from 'sveltekit-superforms/adapters';
	import TogglePassword from '$lib/components/TogglePassword.svelte';
	import type { ActionData, PageData } from './$types';
	import { toast } from 'svelte-sonner';
	import { loginSchema } from './schema';

	let { data, form }: { data: PageData; form: ActionData } = $props();

	const loginForm = superForm(data.loginForm, {
		validators: zodClient(loginSchema),
		resetForm: false
	});

	const { form: formData, enhance: loginEnhance, delayed } = loginForm;

	let toggled: boolean = $state(false);

	$effect(() => {
		if (form?.wrongCreds) toast.error('Грешен имейл или парола.');
	});

	$effect(() => {
		if (form?.errorLogin) toast.error('Възникна грешка.');
	});
</script>

<div class="flex h-screen w-full items-center justify-center px-4">
	<Card.Root class="mx-auto max-w-sm w-full">
		<Card.Header>
			<Card.Title class="text-2xl">Вход</Card.Title>
		</Card.Header>
		<Card.Content>
			<form method="POST" action="?/login" use:loginEnhance class="grid gap-4">
				<div class="grid gap-2">
					<Form.Field form={loginForm} name="email">
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
					<Form.Field form={loginForm} name="password">
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
				<Form.Button type="submit" class="w-full">Вход</Form.Button>
			</form>
			<div class="mt-4 text-center text-sm">
				Нямаш профил?
				<a href="/register" class="underline"> Регистрирай се </a>
			</div>
		</Card.Content>
	</Card.Root>
</div>
