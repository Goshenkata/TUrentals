<script lang="ts">
	import Button from '$lib/components/ui/button/button.svelte';
	import { enhance } from '$app/forms';
	import { invalidateAll } from '$app/navigation';
	import { Trash2 } from 'lucide-svelte';
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import ScrollArea from '$lib/components/ui/scroll-area/scroll-area.svelte';
	import { toast } from 'svelte-sonner';
	import type { Category } from '$lib/types';

	type Props = {
		checked: number[];
		categories: Category[];
	};
	let { checked, categories }: Props = $props();

	let disabled = $state(false);
	let openDelete = $state(false);

	const handleSubmit = async () => {
		disabled = true;
		const t = toast.loading('Работим по въпроса ;)', { duration: 1000000000 });
		//@ts-ignore
		return async ({ update, result }) => {
			await invalidateAll();
			await update();
			disabled = false;
			toast.dismiss(t);
		};
	};
</script>

<Button
	variant="destructive"
	class="cursor-pointer"
	{disabled}
	onclick={() => {
		if (checked.length > 0) openDelete = true;
	}}
>
	<Trash2 class="ml-0 mr-2 max-w-5"></Trash2>
	Изтрий</Button
>

<Dialog.Root bind:open={openDelete}>
	<Dialog.Content class="sm:max-w-lg">
		<Dialog.Header>
			<Dialog.Title class="text-center leading-6">Внимание!</Dialog.Title>
			<Dialog.Description class="text-center">
				Сигурни ли сте че сикате да изтриете:
				<ScrollArea orientation="vertical" class="mt-2 h-[52dvh] rounded-md bg-muted p-1.5">
					<ol class="flex min-w-0 flex-col items-start gap-1 text-left font-mono font-bold">
						{#each categories.filter((ftr) => checked.includes(ftr.id)) as category, i}
							<li>{i + 1}. {category?.id} - {category?.name}</li>
						{/each}
					</ol>
				</ScrollArea>
			</Dialog.Description>
		</Dialog.Header>

		<Dialog.Footer>
			<div class="flex w-full justify-center gap-4">
				<form action="?/massDeleteCategories" method="POST" use:enhance={handleSubmit}>
					<input type="hidden" name="ids" value={checked} />

					<Button
						type="submit"
						onclick={() => {
							openDelete = false;
						}}
						{disabled}
						variant="destructive">Изтрий</Button
					>
				</form>
				<Button
					onclick={() => {
						openDelete = false;
					}}
					class="border-0"
				>
					Отказ
				</Button>
			</div>
		</Dialog.Footer>
	</Dialog.Content>
</Dialog.Root>
