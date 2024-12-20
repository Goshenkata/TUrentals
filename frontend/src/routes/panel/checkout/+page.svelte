<script lang="ts">
	import { Cart, cartStats, handleDelete } from '$lib/stores/cart.svelte';
</script>

<div class="lg:flex lg:min-h-full lg:flex-row-reverse lg:overflow-hidden m-0.5">
	<h1 class="sr-only">Checkout</h1>

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
												{(cartItem.quantity * cartItem.product.pricePerDay).toLocaleString(
													'bg-BG',
													{
														style: 'currency',
														currency: 'BGN'
													}
												)}
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
										{(cartItem.quantity * cartItem.product.pricePerDay).toLocaleString('bg-BG', {
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
					<dt class="text-base">Тотал</dt>
					<dd class="text-base">
						{$cartStats.total.toLocaleString('bg-BG', { style: 'currency', currency: 'BGN' })}
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
			<form class="mt-6">
				<!-- <div class="grid grid-cols-12 gap-x-4 gap-y-6">
					<div class="col-span-full">
						<label for="email-address" class="block text-sm/6 font-medium text-gray-700"
							>Email address</label
						>
						<div class="mt-2">
							<input
								type="email"
								id="email-address"
								name="email-address"
								autocomplete="email"
								class="block w-full rounded-md bg-white px-3 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
							/>
						</div>
					</div>

					<div class="col-span-full">
						<label for="name-on-card" class="block text-sm/6 font-medium text-gray-700"
							>Name on card</label
						>
						<div class="mt-2">
							<input
								type="text"
								id="name-on-card"
								name="name-on-card"
								autocomplete="cc-name"
								class="block w-full rounded-md bg-white px-3 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
							/>
						</div>
					</div>

					<div class="col-span-full">
						<label for="card-number" class="block text-sm/6 font-medium text-gray-700"
							>Card number</label
						>
						<div class="mt-2">
							<input
								type="text"
								id="card-number"
								name="card-number"
								autocomplete="cc-number"
								class="block w-full rounded-md bg-white px-3 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
							/>
						</div>
					</div>

					<div class="col-span-8 sm:col-span-9">
						<label for="expiration-date" class="block text-sm/6 font-medium text-gray-700"
							>Expiration date (MM/YY)</label
						>
						<div class="mt-2">
							<input
								type="text"
								name="expiration-date"
								id="expiration-date"
								autocomplete="cc-exp"
								class="block w-full rounded-md bg-white px-3 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
							/>
						</div>
					</div>
				</div>

				<div class="mt-6 flex gap-3">
					<div class="flex h-5 shrink-0 items-center">
						<div class="group grid size-4 grid-cols-1">
							<input
								id="same-as-shipping"
								name="same-as-shipping"
								type="checkbox"
								checked
								class="col-start-1 row-start-1 appearance-none rounded border border-gray-300 bg-white checked:border-indigo-600 checked:bg-indigo-600 indeterminate:border-indigo-600 indeterminate:bg-indigo-600 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 disabled:border-gray-300 disabled:bg-gray-100 disabled:checked:bg-gray-100 forced-colors:appearance-auto"
							/>
							<svg
								class="pointer-events-none col-start-1 row-start-1 size-3.5 self-center justify-self-center stroke-white group-has-[:disabled]:stroke-gray-950/25"
								viewBox="0 0 14 14"
								fill="none"
							>
								<path
									class="opacity-0 group-has-[:checked]:opacity-100"
									d="M3 8L6 11L11 3.5"
									stroke-width="2"
									stroke-linecap="round"
									stroke-linejoin="round"
								/>
								<path
									class="opacity-0 group-has-[:indeterminate]:opacity-100"
									d="M3 7H11"
									stroke-width="2"
									stroke-linecap="round"
									stroke-linejoin="round"
								/>
							</svg>
						</div>
					</div>
					<label for="same-as-shipping" class="text-sm font-medium text-gray-900"
						>Billing address is the same as shipping address</label
					>
				</div> -->

				<button
					type="submit"
					class="mt-6 w-full rounded-md border border-transparent bg-indigo-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
					>Плати {$cartStats.total.toLocaleString('bg-BG', {
						style: 'currency',
						currency: 'BGN'
					})}</button
				>
			</form>
		</div>
	</section>
</div>
