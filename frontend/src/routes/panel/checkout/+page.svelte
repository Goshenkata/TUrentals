<script lang="ts">
	import { Cart, cartStats, handleDelete } from '$lib/stores/cart.svelte';
	import { superForm } from 'sveltekit-superforms';
	import { zodClient } from 'sveltekit-superforms/adapters';
	import { deliverySchema } from './schema.js';
	import { Input } from '$lib/components/ui/input/index.js';
	import * as Form from '$lib/components/ui/form';
	import { Textarea } from '$lib/components/ui/textarea/index.js';
	import CalendarIcon from 'lucide-svelte/icons/calendar';
	import * as Popover from '$lib/components/ui/popover/index.js';
	import { toast } from 'svelte-sonner';
	import { untrack } from 'svelte';
	import {
		DateFormatter,
		type DateValue,
		getLocalTimeZone,
		parseDate,
		today
	} from '@internationalized/date';
	import { Button } from '$lib/components/ui/button/index.js';
	import { Calendar } from '$lib/components/ui/calendar/index.js';
	import { cn } from '$lib/utils.js';

	const { data, form } = $props();

	const deliveryForm = superForm(data.deliveryForm, {
		validators: zodClient(deliverySchema),
		dataType: 'json',
		resetForm: false
	});

	const { form: formData, enhance, errors, delayed } = deliveryForm;

	const df = new DateFormatter('en-US', {
		dateStyle: 'long'
	});

	$effect(() => {
		untrack(() => {
			$formData.items = [];
		});
		$Cart.forEach((cartItem) => {
			untrack(() => {
				$formData.items.push({
					itemId: cartItem.product.id,
					quantity: cartItem.quantity
				});
			});
		});
	});

	let returnDate = $state<DateValue | undefined>();
	let deliveryDate = $state<DateValue | undefined>();

	let dayDifferece = $derived(
		Math.max(
			1,
			Math.ceil(
				((returnDate?.toDate(getLocalTimeZone()).getTime() ?? 0) -
					(deliveryDate?.toDate(getLocalTimeZone()).getTime() ?? 0)) /
					(1000 * 3600 * 24)
			)
		)
	);

	$effect(() => {
		returnDate = $formData.returnDate ? parseDate($formData.returnDate) : undefined;
	});

	$effect(() => {
		deliveryDate = $formData.deliveryDate ? parseDate($formData.deliveryDate) : undefined;
	});

	let placeholder = $state(today(getLocalTimeZone()));

	$effect(() => {
		if (form?.errorCheckout) {
			toast.error('Възникна грешка, моля опитайте отново.');
		}

		if (form?.itemsNotAvailiable && form.items.length > 0) {
			toast.warning(
				'Следните артикули не са налични: ' + form.items.map((i: any) => i.item.name).join(', ')
			);
		}
	});
</script>

<div class="lg:flex lg:min-h-full lg:flex-row-reverse lg:overflow-hidden m-0.5">
	<!-- Mobile order summary -->
	<section aria-labelledby="order-heading" class="bg-gray-50 px-4 py-6 sm:px-6 lg:hidden">
		<div class="mx-auto max-w-lg">
			<div class="flex items-center justify-between">
				<h2 id="order-heading" class="text-lg font-medium text-gray-900">Вашата поръчка</h2>
			</div>

			<div id="disclosure-1">
				<ul role="list" class="divide-y divide-gray-200 border-b border-gray-200">
					{#each $Cart as cartItem}
						<li class="flex space-x-6 py-6">
							<img
								src={cartItem.product.imageUrl}
								alt={cartItem.product.name}
								class="size-40 flex-none rounded-md bg-gray-200 object-cover"
							/>
							<div class="flex flex-col justify-between space-y-4 w-full">
								<div class="space-y-1 text-sm font-medium">
									<h3 class="text-gray-900">{cartItem.product.name}</h3>
									<p class="text-gray-500">{cartItem.product.categoryName}</p>

									<dl class="mt-10 space-y-2 text-sm font-medium text-gray-500">
										<div class="flex justify-between">
											<dt>Цена на ден</dt>
											<dd class="text-gray-900">
												{cartItem.product.pricePerDay.toLocaleString('bg-BG', {
													style: 'currency',
													currency: 'BGN'
												})}
											</dd>
										</div>

										<div class="flex justify-between">
											<dt>Количество</dt>
											<dd class="text-gray-900">{cartItem.quantity}</dd>
										</div>

										<div class="flex justify-between">
											<dt>Общо</dt>
											<dd class="text-gray-900">
												{(
													cartItem.quantity *
													cartItem.product.pricePerDay *
													dayDifferece
												).toLocaleString('bg-BG', {
													style: 'currency',
													currency: 'BGN'
												})}
											</dd>
										</div>
									</dl>
								</div>
								<div class="flex space-x-4">
									<button
										onclick={() => handleDelete(cartItem)}
										class="text-sm font-medium text-indigo-600 hover:text-indigo-500">Изтрий</button
									>
								</div>
							</div>
						</li>
					{/each}
				</ul>
			</div>

			<p
				class="mt-6 flex items-center justify-between border-t border-gray-200 pt-6 text-sm font-medium text-gray-900"
			>
				<span class="text-base">Дни на заемане</span>
				<span class="text-base">{dayDifferece}</span>
			</p>

			<p
				class="mt-6 flex items-center justify-between border-t border-gray-200 pt-6 text-sm font-medium text-gray-900"
			>
				<span class="text-base">Междинно</span>
				<span class="text-base"
					>{dayDifferece} дни * {$cartStats.total.toLocaleString('bg-BG', {
						style: 'currency',
						currency: 'BGN'
					})}</span
				>
			</p>
			<p
				class="mt-6 flex items-center justify-between border-t border-gray-200 pt-6 text-sm font-medium text-gray-900"
			>
				<span class="text-base">Тотал</span>
				<span class="text-base"
					>{$cartStats.total.toLocaleString('bg-BG', { style: 'currency', currency: 'BGN' })}</span
				>
			</p>
		</div>
	</section>

	<!-- Order summary -->
	<section
		aria-labelledby="summary-heading"
		class="hidden w-full max-w-md flex-col bg-gray-50 lg:flex"
	>
		<ul role="list" class="flex-auto divide-y divide-gray-200 overflow-y-auto px-6">
			{#each $Cart as cartItem}
				<li class="flex space-x-6 py-6">
					<img
						src={cartItem.product.imageUrl}
						alt={cartItem.product.name}
						class="size-40 flex-none rounded-md bg-gray-200 object-cover"
					/>
					<div class="flex flex-col justify-between space-y-4 w-full">
						<div class="space-y-1 text-sm font-medium">
							<h3 class="text-gray-900">{cartItem.product.name}</h3>
							<p class="text-gray-500">{cartItem.product.categoryName}</p>

							<dl class="mt-10 space-y-2 text-sm font-medium text-gray-500">
								<div class="flex justify-between">
									<dt>Цена на ден</dt>
									<dd class="text-gray-900">
										{cartItem.product.pricePerDay.toLocaleString('bg-BG', {
											style: 'currency',
											currency: 'BGN'
										})}
									</dd>
								</div>

								<div class="flex justify-between">
									<dt>Количество</dt>
									<dd class="text-gray-900">{cartItem.quantity}</dd>
								</div>

								<div class="flex justify-between">
									<dt>Общо</dt>
									<dd class="text-gray-900">
										{(
											cartItem.quantity *
											cartItem.product.pricePerDay *
											dayDifferece
										).toLocaleString('bg-BG', {
											style: 'currency',
											currency: 'BGN'
										})}
									</dd>
								</div>
							</dl>
						</div>
						<div class="flex space-x-4">
							<button
								onclick={() => handleDelete(cartItem)}
								class="text-sm font-medium text-indigo-600 hover:text-indigo-500">Изтрий</button
							>
						</div>
					</div>
				</li>
			{/each}
		</ul>

		<div class="sticky bottom-0 flex-none border-t border-gray-200 bg-gray-50 p-6">
			<dl class="mt-10 space-y-6 text-sm font-medium text-gray-500">
				<div class="flex items-center justify-between border-t border-gray-200 pt-6 text-gray-900">
					<dt class="text-base">Дни на заемане</dt>
					<dd class="text-base">
						{dayDifferece}
					</dd>
				</div>
			</dl>
			<dl class="mt-6 space-y-6 text-sm font-medium text-gray-500">
				<div class="flex items-center justify-between border-t border-gray-200 pt-6 text-gray-900">
					<dt class="text-base">Междинно</dt>
					<dd class="text-base">
						{dayDifferece} дни * {$cartStats.total.toLocaleString('bg-BG', {
							style: 'currency',
							currency: 'BGN'
						})}
					</dd>
				</div>
			</dl>
			<dl class="mt-6 space-y-6 text-sm font-medium text-gray-500">
				<div class="flex items-center justify-between border-t border-gray-200 pt-6 text-gray-900">
					<dt class="text-base">Тотал</dt>
					<dd class="text-base">
						{($cartStats.total * dayDifferece).toLocaleString('bg-BG', {
							style: 'currency',
							currency: 'BGN'
						})}
					</dd>
				</div>
			</dl>
		</div>
	</section>

	<!-- Checkout form -->
	<section
		aria-labelledby="payment-heading"
		class="flex-auto overflow-y-auto px-4 pb-16 pt-12 sm:px-6 sm:pt-16 lg:px-8 lg:pb-24 lg:pt-0"
	>
		<div class="mx-auto max-w-lg">
			<form class="mt-6" method="POST" action="?/checkout" use:enhance>
				<div class="grid md:gap-6 md:grid-cols-2">
					<Form.Field form={deliveryForm} name="countryName">
						<Form.Control>
							{#snippet children({ props })}
								<Form.Label>Държава</Form.Label>
								<Input
									{...props}
									disabled={$delayed}
									required
									bind:value={$formData.countryName}
									type="text"
								/>
							{/snippet}
						</Form.Control>
						<Form.FieldErrors />
					</Form.Field>

					<Form.Field form={deliveryForm} name="stateName">
						<Form.Control>
							{#snippet children({ props })}
								<Form.Label>Област</Form.Label>
								<Input
									{...props}
									disabled={$delayed}
									required
									bind:value={$formData.stateName}
									type="text"
								/>
							{/snippet}
						</Form.Control>
						<Form.FieldErrors />
					</Form.Field>
				</div>
				<div class="grid md:gap-6 md:grid-cols-2">
					<Form.Field form={deliveryForm} name="townName">
						<Form.Control>
							{#snippet children({ props })}
								<Form.Label>Град</Form.Label>
								<Input
									{...props}
									disabled={$delayed}
									required
									bind:value={$formData.townName}
									type="text"
								/>
							{/snippet}
						</Form.Control>
						<Form.FieldErrors />
					</Form.Field>

					<Form.Field form={deliveryForm} name="postCodeCode">
						<Form.Control>
							{#snippet children({ props })}
								<Form.Label>Пощенски код</Form.Label>
								<Input
									{...props}
									disabled={$delayed}
									required
									bind:value={$formData.postCodeCode}
									type="text"
								/>
							{/snippet}
						</Form.Control>
						<Form.FieldErrors />
					</Form.Field>
				</div>
				<Form.Field form={deliveryForm} name="street">
					<Form.Control>
						{#snippet children({ props })}
							<Form.Label>Улица</Form.Label>
							<Input
								{...props}
								disabled={$delayed}
								required
								bind:value={$formData.street}
								type="text"
							/>
						{/snippet}
					</Form.Control>
					<Form.FieldErrors />
				</Form.Field>

				<Form.Field form={deliveryForm} name="description">
					<Form.Control>
						{#snippet children({ props })}
							<Form.Label>Описание</Form.Label>
							<Textarea
								{...props}
								disabled={$delayed}
								required
								bind:value={$formData.description}
							/>
						{/snippet}
					</Form.Control>
					<Form.FieldErrors />
				</Form.Field>
				<div class="mt-4 grid md:gap-6 md:grid-cols-2">
					<Form.Field form={deliveryForm} name="deliveryDate" class="flex flex-col ">
						<Form.Control>
							{#snippet children({ props })}
								<Form.Label>Дата за доставка</Form.Label>
								<Popover.Root>
									<Popover.Trigger {...props}>
										{#snippet child({ props })}
											<Button
												variant="outline"
												class={cn(
													' justify-start pl-4 text-left font-normal',
													!deliveryDate && 'text-muted-foreground'
												)}
												{...props}
											>
												{deliveryDate
													? df.format(deliveryDate.toDate(getLocalTimeZone()))
													: 'Изберете дата'}
												<CalendarIcon class="ml-auto size-4 opacity-50" />
											</Button>
										{/snippet}
									</Popover.Trigger>
									<Popover.Content class="w-auto p-0" side="top">
										<Calendar
											type="single"
											value={deliveryDate}
											bind:placeholder
											calendarLabel="Date"
											minValue={today(getLocalTimeZone()).add({ days: 1 })}
											onValueChange={(v) => {
												if (v) {
													$formData.deliveryDate = v.toString();
												} else {
													$formData.deliveryDate = '';
												}
											}}
										/>
									</Popover.Content>
								</Popover.Root>
								<Form.FieldErrors />
							{/snippet}
						</Form.Control>
					</Form.Field>

					<Form.Field form={deliveryForm} name="returnDate" class="flex flex-col ">
						<Form.Control>
							{#snippet children({ props })}
								<Form.Label>Дата за връщане</Form.Label>
								<Popover.Root>
									<Popover.Trigger {...props}>
										{#snippet child({ props })}
											<Button
												variant="outline"
												class={cn(
													'justify-start pl-4 text-left font-normal',
													!returnDate && 'text-muted-foreground'
												)}
												{...props}
											>
												{returnDate
													? df.format(returnDate.toDate(getLocalTimeZone()))
													: 'Изберете дата'}
												<CalendarIcon class="ml-auto size-4 opacity-50" />
											</Button>
										{/snippet}
									</Popover.Trigger>
									<Popover.Content class="w-auto p-0" side="top">
										<Calendar
											type="single"
											value={returnDate}
											bind:placeholder
											minValue={deliveryDate || today(getLocalTimeZone())}
											calendarLabel="Date"
											onValueChange={(v) => {
												if (v) {
													$formData.returnDate = v.toString();
												} else {
													$formData.returnDate = '';
												}
											}}
										/>
									</Popover.Content>
								</Popover.Root>
								<Form.FieldErrors />
							{/snippet}
						</Form.Control>
					</Form.Field>
				</div>
				<button
					type="submit"
					class="mt-6 w-full rounded-md border border-transparent bg-indigo-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
					>Плати {($cartStats.total * dayDifferece).toLocaleString('bg-BG', {
						style: 'currency',
						currency: 'BGN'
					})}</button
				>
			</form>
		</div>
	</section>
</div>
