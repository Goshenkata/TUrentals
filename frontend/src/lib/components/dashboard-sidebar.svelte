<script lang="ts">
	import * as Sidebar from '$lib/components/ui/sidebar/index.js';
	import type { ComponentProps } from 'svelte';
	import * as Collapsible from '$lib/components/ui/collapsible/index.js';
	import ChevronRight from 'lucide-svelte/icons/chevron-right';
	import {
		Boxes,
		CalendarRange,
		LayoutDashboard,
		Users,
		PackagePlus,
		Settings,
		BookOpen,
		Package,
		CircleDashed,
		CalendarClock,
		CalendarCheck,
		PackageOpen,
		CalendarSync,
		ShoppingCart
	} from 'lucide-svelte/icons';
	import { page } from '$app/stores';
	import { Badge } from './ui/badge';
	import Skeleton from './ui/skeleton/skeleton.svelte';
	import type { User } from '$lib/types';

	type SidebarPropsWithRole = Omit<ComponentProps<typeof Sidebar.Root>, 'userRole'> & {
		user: User;
	};

	let {
		ref = $bindable(null),
		collapsible = 'icon',
		user,
		...restProps
	}: SidebarPropsWithRole = $props();
</script>

<Sidebar.Root bind:ref {collapsible} {...restProps}>
	<Sidebar.Header>
		<Sidebar.Menu>
			<Sidebar.MenuItem>
				<a href="/">
					<Sidebar.MenuButton size="lg">
						<div class="flex aspect-square size-10 items-center justify-center rounded-lg">
							<ShoppingCart class="h-5 w-5 mr-2" />
						</div>
						<div class="grid flex-1 text-left text-lg font-bold leading-tight">Tu rentals</div>
					</Sidebar.MenuButton>
				</a>
			</Sidebar.MenuItem>
		</Sidebar.Menu>
	</Sidebar.Header>

	<Sidebar.Content>
		<Sidebar.Group>
			<Sidebar.Menu>
				<Sidebar.MenuItem>
					<Sidebar.MenuButton
						isActive={$page.url.pathname === `/dashboard` || $page.url.pathname === `/dashboard`}
					>
						{#snippet child({ props })}
							<a href="/dashboard" {...props}>
								<LayoutDashboard />
								<span>Панел</span>
							</a>
						{/snippet}
					</Sidebar.MenuButton>
				</Sidebar.MenuItem>

				{#if user?.role === 'MANAGER' || user?.role === 'EMPLOYEE'}
					<Collapsible.Root open={$page.url.pathname.includes('/orders')} class="group/collapsible">
						{#snippet child({ props })}
							<Sidebar.MenuItem {...props}>
								<Collapsible.Trigger>
									{#snippet child({ props })}
										<Sidebar.MenuButton {...props}>
											{#snippet tooltipContent()}
												Поръчки
											{/snippet}
											<CalendarRange />
											<span>Поръчки</span>
											<ChevronRight
												class="ml-auto transition-transform duration-200 group-data-[state=open]/collapsible:rotate-90"
											/>
										</Sidebar.MenuButton>
									{/snippet}
								</Collapsible.Trigger>
								<Collapsible.Content>
									<Sidebar.MenuSub>
										{#if user?.role === 'MANAGER'}
											<Sidebar.MenuSubItem>
												<Sidebar.MenuSubButton
													isActive={$page.url.pathname === '/dashboard/orders/pending'}
												>
													{#snippet child({ props })}
														<a href="/dashboard/orders/pending" {...props}>
															<CalendarClock />
															<span>Чакащи</span>
														</a>
													{/snippet}
												</Sidebar.MenuSubButton>
											</Sidebar.MenuSubItem>

											<!-- <Sidebar.MenuSubItem>
												<Sidebar.MenuSubButton
													isActive={$page.url.pathname === '/dashboard/orders/need-attention'}
												>
													{#snippet child({ props })}
														<a href="/dashboard/orders/need-attention" {...props}>
															<CalendarSync />
															<span>Очакващи преглед</span>
														</a>
													{/snippet}
												</Sidebar.MenuSubButton>
											</Sidebar.MenuSubItem> -->
										{/if}

										{#if user?.role === 'EMPLOYEE'}
											<Sidebar.MenuSubItem>
												<Sidebar.MenuSubButton
													isActive={$page.url.pathname === '/dashboard/orders/assigned'}
												>
													{#snippet child({ props })}
														<a href="/dashboard/orders/assigned" {...props}>
															<CalendarCheck />
															<span>Мои поръчки</span>
														</a>
													{/snippet}
												</Sidebar.MenuSubButton>
											</Sidebar.MenuSubItem>
										{/if}
									</Sidebar.MenuSub>
								</Collapsible.Content>
							</Sidebar.MenuItem>
						{/snippet}
					</Collapsible.Root>
				{/if}

				{#if user?.role === 'MANAGER'}
					<Sidebar.MenuItem>
						<Sidebar.MenuButton isActive={$page.url.pathname.includes('/dashboard/inventory')}>
							{#snippet child({ props })}
								<a href="/dashboard/inventory" {...props}>
									<PackageOpen />
									<span>Инвентар</span>
								</a>
							{/snippet}
						</Sidebar.MenuButton>
					</Sidebar.MenuItem>
				{/if}

				{#if user?.role === 'MANAGER'}
					<Collapsible.Root
						open={$page.url.pathname.includes('/catalog')}
						class="group/collapsible"
					>
						{#snippet child({ props })}
							<Sidebar.MenuItem {...props}>
								<Collapsible.Trigger>
									{#snippet child({ props })}
										<Sidebar.MenuButton {...props}>
											{#snippet tooltipContent()}
												Каталог
											{/snippet}
											<BookOpen />
											<span>Каталог</span>
											<ChevronRight
												class="ml-auto transition-transform duration-200 group-data-[state=open]/collapsible:rotate-90"
											/>
										</Sidebar.MenuButton>
									{/snippet}
								</Collapsible.Trigger>
								<Collapsible.Content>
									<Sidebar.MenuSub>
										<Sidebar.MenuSubItem>
											<Sidebar.MenuSubButton
												isActive={$page.url.pathname === '/dashboard/catalog/categories'}
											>
												{#snippet child({ props })}
													<a href="/dashboard/catalog/categories" {...props}>
														<Boxes />
														<span>Категории</span>
													</a>
												{/snippet}
											</Sidebar.MenuSubButton>
										</Sidebar.MenuSubItem>

										<Sidebar.MenuSubItem>
											<Sidebar.MenuSubButton
												isActive={$page.url.pathname === '/dashboard/catalog/products'}
											>
												{#snippet child({ props })}
													<a href="/dashboard/catalog/products" {...props}>
														<Package />
														<span>Продукти</span>
													</a>
												{/snippet}
											</Sidebar.MenuSubButton>
										</Sidebar.MenuSubItem>

										<Sidebar.MenuSubItem>
											<Sidebar.MenuSubButton
												isActive={$page.url.pathname === '/dashboard/catalog/products/new'}
											>
												{#snippet child({ props })}
													<a href="/dashboard/catalog/products/new" {...props}>
														<PackagePlus />
														<span>Добавяне на продукт</span>
													</a>
												{/snippet}
											</Sidebar.MenuSubButton>
										</Sidebar.MenuSubItem>
									</Sidebar.MenuSub>
								</Collapsible.Content>
							</Sidebar.MenuItem>
						{/snippet}
					</Collapsible.Root>
				{/if}

				{#if user?.role === 'ADMIN'}
					<Sidebar.MenuItem>
						<Sidebar.MenuButton isActive={$page.url.pathname.includes('/dashboard/users')}>
							{#snippet child({ props })}
								<a href="/dashboard/users" {...props}>
									<Users />
									<span>Потребители</span>
								</a>
							{/snippet}
						</Sidebar.MenuButton>
					</Sidebar.MenuItem>
				{/if}
			</Sidebar.Menu>
		</Sidebar.Group>
	</Sidebar.Content>
	<Sidebar.Footer>
		<Sidebar.Menu>
			<Sidebar.MenuItem>
				<Sidebar.MenuButton isActive={$page.url.pathname.includes('/dashboard/settings')}>
					{#snippet child({ props })}
						<a href="/dashboard/settings" {...props}>
							<Settings class="h-5 w-5" />

							<span>Настройки</span>
						</a>
					{/snippet}
				</Sidebar.MenuButton>
			</Sidebar.MenuItem>
		</Sidebar.Menu>
	</Sidebar.Footer>
	<Sidebar.Rail />
</Sidebar.Root>
