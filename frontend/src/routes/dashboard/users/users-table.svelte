<script lang="ts">
	import Checkbox from '$lib/components/ui/checkbox/checkbox.svelte';
	import ScrollArea from '$lib/components/ui/scroll-area/scroll-area.svelte';
	import * as Table from '$lib/components/ui/table/index.js';
	import UserActions from './user-actions.svelte';
	import MassActions from './mass-actions.svelte';
	import Badge from '$lib/components/ui/badge/badge.svelte';
	import type { NonNullableUser } from '$lib/types';

	type Props = {
		users: NonNullableUser[];
	};
	let { users }: Props = $props();

	let checked: string[] = $state([]);
	let checkedAll = $derived(checked.length === users.length);
</script>

{#snippet userRow(user: NonNullableUser)}
	<Table.Row>
		<Table.Cell class="">
			<Checkbox
				value={user.email}
				checked={checked.includes(user.email) || checkedAll}
				onCheckedChange={() => {
					if (checked.includes(user.email)) checked = checked.filter((e) => e !== user.email);
					else checked.push(user.email);
				}}
			/>
		</Table.Cell>

		<Table.Cell class="">
			<span>
				{user.firstName}
			</span>
		</Table.Cell>

		<Table.Cell class="">
			<span>
				{user.lastName}
			</span>
		</Table.Cell>

		<Table.Cell class="">
			{#if user.role === 'ADMIN'}
				<Badge variant="destructive">{user.role}</Badge>
			{/if}

			{#if user.role === 'MANAGER'}
				<Badge class=" bg-yellow-500 text-foreground">{user.role}</Badge>
			{/if}

			{#if user.role === 'EMPLOYEE'}
				<Badge variant="default">{user.role}</Badge>
			{/if}

			{#if user.role === 'USER'}
				<Badge variant="secondary">{user.role}</Badge>
			{/if}
		</Table.Cell>

		<Table.Cell class="">
			<span>
				{user.email}
			</span>
		</Table.Cell>

		<Table.Cell class="text-center">
			<UserActions {user}></UserActions>
		</Table.Cell>
	</Table.Row>
{/snippet}

<div class="mb-3 flex flex-col gap-3 md:flex-row">
	<MassActions {checked} {users}></MassActions>
</div>

<ScrollArea orientation="horizontal" class="mx-auto max-w-[94dvw] rounded-xl border p-2">
	<Table.Root class="">
		<Table.Header>
			<Table.Row class="!bg-background hover:bg-background">
				<Table.Head class="w-[30px]">
					<Checkbox
						checked={checkedAll}
						onCheckedChange={(isChecked) => {
							if (isChecked) {
								checked = users.map((u) => u.email);
							} else {
								checked = [];
							}
						}}
					/>
				</Table.Head>
				<Table.Head class="min-w-[10ch]">Име</Table.Head>
				<Table.Head class="min-w-[10ch]">Фамилия</Table.Head>
				<Table.Head class="min-w-[8ch]">Роля</Table.Head>
				<Table.Head class="min-w-[25ch]">Имейл</Table.Head>

				<Table.Head class="w-[40px]"></Table.Head>
			</Table.Row>
		</Table.Header>
		<Table.Body class="hover:[tr:has(table)]:bg-background">
			{#each users as user (user?.id)}
				{@render userRow(user)}
			{/each}
		</Table.Body>
	</Table.Root>
</ScrollArea>
