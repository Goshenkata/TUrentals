<script lang="ts">
	import Ellipsis from 'lucide-svelte/icons/ellipsis';
	import * as DropdownMenu from '$lib/components/ui/dropdown-menu';
	import { Button, buttonVariants } from '$lib/components/ui/button';
	import {
		ClipboardCopy,
		CirclePlus,
		ChevronsUpDown,
		Check,
		CircleFadingArrowUp
	} from 'lucide-svelte';
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import * as Form from '$lib/components/ui/form';
	import { Input } from '$lib/components/ui/input';
	import type { SuperValidated } from 'sveltekit-superforms';
	import { superForm } from 'sveltekit-superforms';
	import {
		assignEmployeeSchema,
		changeStatusSchema,
		type AssignEmployeeSchema,
		type ChangeStatusSchema
	} from './schema';
	import { zodClient } from 'sveltekit-superforms/adapters';
	import { getContext, tick } from 'svelte';
	import type { NonNullableUser, PendingOrder } from '$lib/types';
	import * as Command from '$lib/components/ui/command/index.js';
	import * as Popover from '$lib/components/ui/popover/index.js';
	import { useId } from 'bits-ui';
	import { cn } from '$lib/utils';
	import * as Select from '$lib/components/ui/select';
	import { OrderStatusEnum } from '$lib/enums';

	type Props = {
		order: PendingOrder;
	};
	let { order }: Props = $props();

	const assignEmployeeFormData: SuperValidated<AssignEmployeeSchema> =
		getContext('assignEmployeeForm');

	const changeStatusFormAndData: SuperValidated<ChangeStatusSchema> =
		getContext('changeStatusForm');

	const employees: NonNullableUser[] = getContext('employees');

	const assignEmployeeForm = superForm(assignEmployeeFormData, {
		validators: zodClient(assignEmployeeSchema),
		resetForm: true,
		dataType: 'json',
		id: `assign-employee-form-${order.id}-${order.orderType}`
	});

	const changeStatusForm = superForm(changeStatusFormAndData, {
		validators: zodClient(changeStatusSchema),
		resetForm: true,
		dataType: 'json',
		id: `change-status-form-${order.id}-${order.orderType}`
	});

	const {
		form: assignFormData,
		enhance: assignEnhanced,
		delayed: assignDelayed
	} = assignEmployeeForm;

	const {
		form: changeStatusFormData,
		enhance: changeStatusEnhanced,
		delayed: changeStatusDelayed
	} = changeStatusForm;

	$assignFormData.orderId = order.id;
	$assignFormData.orderType = order.orderType;

	$changeStatusFormData.orderId = order.id;

	let openEmployeeDialog = $state(false);
	let openStatusDialog = $state(false);

	let openEmployeePopover = $state(false);

	function closeAndFocusTrigger(triggerId: string) {
		openEmployeePopover = false;
		tick().then(() => {
			document.getElementById(triggerId)?.focus();
		});
	}

	const triggerId = useId();
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
				openEmployeeDialog = true;
			}}
		>
			<CirclePlus class="max-w-5"></CirclePlus>
			<span> Задай изпълнител </span>
		</DropdownMenu.Item>

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

<Dialog.Root bind:open={openEmployeeDialog}>
	<Dialog.Content class="mx-auto max-w-md w-full">
		<Dialog.Header>
			<Dialog.Title>Задаване на изпълнител</Dialog.Title>
		</Dialog.Header>
		<form method="POST" action="?/assignEmployee" use:assignEnhanced class="grid gap-4">
			<div>
				<Form.Field form={assignEmployeeForm} name="employeeId">
					<Popover.Root bind:open={openEmployeePopover}>
						<Form.Control>
							{#snippet children({ props })}
								<div class="grid gap-2">
									<Popover.Trigger
										class={cn(
											buttonVariants({ variant: 'outline' }),
											'justify-between',
											!$assignFormData.employeeId && 'text-muted-foreground'
										)}
										role="combobox"
										{...props}
									>
										{employees.find((em) => em.id === $assignFormData.employeeId)?.firstName ??
											'Избери служител'}
										<ChevronsUpDown class="opacity-50" />
									</Popover.Trigger>
								</div>
							{/snippet}
						</Form.Control>
						<Popover.Content class="w-[200px] p-0">
							<Command.Root>
								<Command.Input autofocus placeholder="Search language..." class="h-9" />
								<Command.Empty>No language found.</Command.Empty>
								<Command.Group>
									{#each employees as employee}
										<Command.Item
											value={employee.id.toString()}
											onSelect={() => {
												$assignFormData.employeeId = employee.id;
												closeAndFocusTrigger(triggerId);
											}}
										>
											<div class="grid gap-1">
												<div>
													{employee.firstName}
													{employee.lastName}
												</div>
												<div class="text-xs text-muted-foreground">{employee.email}</div>
											</div>
											<Check
												class={cn(employee.id !== $assignFormData.employeeId && 'text-transparent')}
											/>
										</Command.Item>
									{/each}
								</Command.Group>
							</Command.Root>
						</Popover.Content>
					</Popover.Root>
					<Form.FieldErrors />
				</Form.Field>
			</div>

			<Dialog.Footer>
				<div class="flex w-full justify-center gap-4">
					<Form.Button type="submit" disabled={$assignDelayed}>Запази</Form.Button>

					<Button
						onclick={() => {
							openEmployeeDialog = false;
						}}
						disabled={$assignDelayed}
						variant="outline"
					>
						Отказ
					</Button>
				</div>
			</Dialog.Footer>
		</form>
	</Dialog.Content>
</Dialog.Root>

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
								{#if order.orderType === 'DELIVERY'}
									<Select.Item
										value={OrderStatusEnum.DELIVERED}
										label={OrderStatusEnum.DELIVERED}
									/>
								{/if}
								{#if order.orderType === 'PICKUP'}
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
