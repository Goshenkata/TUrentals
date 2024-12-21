<script lang="ts">
	import CartComponent from '$lib/components/cart-component.svelte';
	import { CircleUser } from 'lucide-svelte/icons';
	import { Button, buttonVariants } from '$lib/components/ui/button/index.js';
	import * as DropdownMenu from '$lib/components/ui/dropdown-menu/index.js';
	import { navigating } from '$app/stores';
	import { IsCartOpen } from '$lib/stores/cart.svelte.js';

	let { children, data } = $props();

	$effect(() => {
		if ($navigating) {
			$IsCartOpen = false;
		}
	});
</script>

<div class="bg-white">
	<div class="relative z-40 lg:hidden" role="dialog" aria-modal="true">
		{#if $IsCartOpen}
			<div class="fixed inset-0 bg-black/25" aria-hidden="true"></div>
			<div class="fixed inset-0 z-40 flex">
				<div
					class="relative flex w-full max-w-xs flex-col overflow-y-auto bg-white pb-12 shadow-xl"
				>
					<div class="flex px-4 pb-2 pt-5">
						<button
							type="button"
							class="-m-2 inline-flex items-center justify-center rounded-md p-2 text-gray-400"
							onclick={() => ($IsCartOpen = false)}
						>
							<span class="sr-only">Close menu</span>
							<svg
								class="size-6"
								fill="none"
								viewBox="0 0 24 24"
								stroke-width="1.5"
								stroke="currentColor"
								aria-hidden="true"
								data-slot="icon"
							>
								<path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
							</svg>
						</button>
					</div>

					<!-- Links -->
					<div class="mt-2">
						<a
							href="/panel"
							class="block px-4 py-2 text-base font-medium text-gray-900 hover:bg-gray-100"
						>
							Начало
						</a>
						{#each data.categories as category}
							<a
								href="/panel/category/{category.name}"
								class="block px-4 py-2 text-base font-medium text-gray-900 hover:bg-gray-100"
							>
								{category.name}
							</a>
						{/each}
					</div>
				</div>
			</div>
		{/if}
	</div>

	<!-- Hero section -->
	<div class="relative bg-gray-900">
		<div aria-hidden="true" class="absolute inset-0 bg-gray-900 opacity-50"></div>

		<!-- Navigation -->
		<header class="relative z-10">
			<nav aria-label="Top">
				<!-- Secondary navigation -->
				<div class="bg-white/10 backdrop-blur-md backdrop-filter">
					<div class="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
						<div>
							<div class="flex h-16 items-center justify-between">
								<button
									type="button"
									class="lg:hidden -ml-2 p-2 text-white"
									onclick={() => ($IsCartOpen = true)}
								>
									<span class="sr-only">Open menu</span>
									<svg
										class="size-6"
										fill="none"
										viewBox="0 0 24 24"
										stroke-width="1.5"
										stroke="currentColor"
										aria-hidden="true"
										data-slot="icon"
									>
										<path
											stroke-linecap="round"
											stroke-linejoin="round"
											d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"
										/>
									</svg>
								</button>
								<div class="text-white hidden lg:flex gap-4">
									<a href="/panel" class="text-sm font-medium text-white hover:text-gray-100">
										Начало
									</a>
									{#each data.categories as category}
										<a
											href="/panel/category/{category.name}"
											class="text-sm font-medium text-white hover:text-gray-100"
										>
											{category.name}
										</a>
									{/each}
								</div>
								<div class="flex flex-1 items-center justify-end">
									<div class="relative ml-auto flex items-center">
										<CartComponent></CartComponent>
									</div>
								</div>
								<DropdownMenu.Root>
									<DropdownMenu.Trigger
										class="rounded-full text-white {buttonVariants({
											variant: 'ghost',
											size: 'icon'
										})}"
									>
										<CircleUser class="h-5 w-5" />
										<span class="sr-only">Покажи/скрий менюто</span>
									</DropdownMenu.Trigger>
									<DropdownMenu.Content align="end">
										<DropdownMenu.Label>
											<div>
												<div>{data.user.firstName} {data.user.lastName}</div>
												<div class="text-xs text-muted-foreground">
													{data.user.email}
												</div>
											</div>
										</DropdownMenu.Label>
										<DropdownMenu.Separator />

										<Button size="sm" class="w-full" variant="ghost" href="/"
											>Към началната страница</Button
										>

										<DropdownMenu.Separator />

										<Button size="sm" class="w-full" href="/logout">Изход</Button>
									</DropdownMenu.Content>
								</DropdownMenu.Root>
							</div>
						</div>
					</div>
				</div>
			</nav>
		</header>
	</div>

	<main>
		{@render children?.()}
	</main>
</div>
