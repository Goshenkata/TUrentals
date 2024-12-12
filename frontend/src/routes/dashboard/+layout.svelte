<script lang="ts">
	import { Search, CircleUser } from 'lucide-svelte/icons';
	import { Button, buttonVariants } from '$lib/components/ui/button/index.js';
	import * as DropdownMenu from '$lib/components/ui/dropdown-menu/index.js';
	import { Input } from '$lib/components/ui/input/index.js';
	import * as Sidebar from '$lib/components/ui/sidebar';
	import { Separator } from '$lib/components/ui/separator';
	import DashboardSidebar from '$lib/components/dashboard-sidebar.svelte';

	const { children, data } = $props();
</script>

<Sidebar.Provider>
	<DashboardSidebar userRole={data.user.role} />
	<Sidebar.Inset class="relative">
		<header class="flex h-16 shrink-0 items-center gap-2 border-b bg-background px-4">
			<Sidebar.Trigger class="-ml-1" />
			<Separator orientation="vertical" class="mr-2 h-4" />
			<div class="w-full flex-1">
				<form>
					<div class="relative">
						<Search class="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
						<Input
							type="search"
							placeholder="Търси резервации, партньори, обекти..."
							class="w-full appearance-none bg-background pl-8 shadow-none md:w-2/3 lg:w-1/3"
						/>
					</div>
				</form>
			</div>

			<DropdownMenu.Root>
				<DropdownMenu.Trigger
					class="rounded-full {buttonVariants({ variant: 'secondary', size: 'icon' })}"
				>
					<CircleUser class="h-5 w-5" />
					<span class="sr-only">Покажи/скрий менюто</span>
				</DropdownMenu.Trigger>
				<DropdownMenu.Content align="end">
					<DropdownMenu.Label>Моят профил</DropdownMenu.Label>
					<DropdownMenu.Separator />
					<DropdownMenu.Item>Настройки на профила</DropdownMenu.Item>
					<DropdownMenu.Item>Помощ</DropdownMenu.Item>
					<DropdownMenu.Separator />

					<Button size="sm" class="w-full" href="logout">Изход</Button>
				</DropdownMenu.Content>
			</DropdownMenu.Root>
		</header>

		<main class="flex flex-1 flex-col gap-4 px-2 py-4 md:px-4 lg:gap-6 lg:p-6">
			{@render children?.()}
		</main>
	</Sidebar.Inset>
</Sidebar.Provider>
