<script lang="ts">
	import Ellipsis from 'lucide-svelte/icons/ellipsis';
	import * as DropdownMenu from '$lib/components/ui/dropdown-menu';
	import { Button, buttonVariants } from '$lib/components/ui/button';
	import { ClipboardCopy, Trash2, PenBox } from 'lucide-svelte';
	import { enhance } from '$app/forms';
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import * as Form from '$lib/components/ui/form';
	import { Input } from '$lib/components/ui/input';
	import type { ReturnedProduct } from '$lib/types';

	type Props = {
		product: ReturnedProduct;
	};
	let { product }: Props = $props();

	let open = $state(false);
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
				onclick={() => navigator.clipboard.writeText(product.id.toString())}
				class="cursor-pointer"
			>
				<ClipboardCopy class="max-w-5"></ClipboardCopy>
				<span> Копирай ID </span>
			</DropdownMenu.Item>
		</DropdownMenu.Group>

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
				Сигурни ли сте че сикате да изтриете: <span class="font-mono font-bold">{product.name}</span
				>
			</Dialog.Description>
		</Dialog.Header>

		<Dialog.Footer>
			<div class="flex w-full justify-center gap-4">
				<form action="?/deleteCategory" method="POST" use:enhance>
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
