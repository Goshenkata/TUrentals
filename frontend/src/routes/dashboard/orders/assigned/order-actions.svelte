<script lang="ts">
	import Ellipsis from 'lucide-svelte/icons/ellipsis';
	import * as DropdownMenu from '$lib/components/ui/dropdown-menu';
	import { Button, buttonVariants } from '$lib/components/ui/button';
	import { ClipboardCopy, CircleFadingArrowUp } from 'lucide-svelte';
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import * as Form from '$lib/components/ui/form';
	import { Input } from '$lib/components/ui/input';
	import type { SuperValidated } from 'sveltekit-superforms';
	import { superForm } from 'sveltekit-superforms';
	import { changeStatusSchema, type ChangeStatusSchema } from './schema';
	import { zodClient } from 'sveltekit-superforms/adapters';
	import { getContext, tick } from 'svelte';
	import type { PendingOrder } from '$lib/types';
	import * as Select from '$lib/components/ui/select';
	import { OrderStatusEnum } from '$lib/enums';

	type Props = {
		order: PendingOrder;
	};
	let { order }: Props = $props();

	const changeStatusFormAndData: SuperValidated<ChangeStatusSchema> =
		getContext('changeStatusForm');

	const changeStatusForm = superForm(changeStatusFormAndData, {
		validators: zodClient(changeStatusSchema),
		resetForm: true,
		dataType: 'json',
		id: `change-status-form-${order.id}`
	});

	const {
		form: changeStatusFormData,
		enhance: changeStatusEnhanced,
		delayed: changeStatusDelayed
	} = changeStatusForm;

	$changeStatusFormData.orderId = order.id;

	let openStatusDialog = $state(false);
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
				openStatusDialog = true;
			}}
		>
			<CircleFadingArrowUp class="max-w-5"></CircleFadingArrowUp>
			<span> Промени статус </span>
		</DropdownMenu.Item>
	</DropdownMenu.Content>
</DropdownMenu.Root>

<Dialog.Root bind:open={openStatusDialog}>
	<Dialog.Content class="mx-auto max-w-md w-full">
		<Dialog.Header>
			<Dialog.Title>Смяна на статус</Dialog.Title>
		</Dialog.Header>
		<form method="POST" action="?/changeStatus" use:changeStatusEnhanced class="grid gap-4">
			<Form.Field form={changeStatusForm} name="status">
				<Form.Control>
					{#snippet children({ props })}
						<Form.Label>Статус</Form.Label>
						<Select.Root type="single" bind:value={$changeStatusFormData.status} name={props.name}>
							<Select.Trigger {...props}>
								{$changeStatusFormData.status ? $changeStatusFormData.status : 'Избери статус'}
							</Select.Trigger>
							<Select.Content>
								{#if (order.orderType = 'DELIVERY')}
									<Select.Item
										value={OrderStatusEnum.DELIVERED}
										label={OrderStatusEnum.DELIVERED}
									/>
								{:else}
									<Select.Item
										value={OrderStatusEnum.COMPLETED}
										label={OrderStatusEnum.COMPLETED}
									/>
								{/if}
								<Select.Item value={OrderStatusEnum.CANCELED} label={OrderStatusEnum.CANCELED} />
							</Select.Content>
						</Select.Root>
					{/snippet}
				</Form.Control>

				<Form.FieldErrors />
			</Form.Field>

			<Form.Field form={changeStatusForm} name="note">
				<Form.Control>
					{#snippet children({ props })}
						<Form.Label>Бележка</Form.Label>

						<Input
							{...props}
							disabled={$changeStatusDelayed}
							bind:value={$changeStatusFormData.note}
							type="text"
						/>
					{/snippet}
				</Form.Control>
				<Form.FieldErrors />
			</Form.Field>

			<Dialog.Footer>
				<div class="flex w-full justify-center gap-4">
					<Form.Button type="submit" disabled={$changeStatusDelayed}>Запази</Form.Button>

					<Button
						onclick={() => {
							openStatusDialog = false;
						}}
						disabled={$changeStatusDelayed}
						variant="outline"
					>
						Отказ
					</Button>
				</div>
			</Dialog.Footer>
		</form>
	</Dialog.Content>
</Dialog.Root>
