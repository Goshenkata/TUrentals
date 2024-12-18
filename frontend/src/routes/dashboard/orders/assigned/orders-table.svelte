<script lang="ts">
	import OrderActions from './order-actions.svelte';
	import type { PendingOrder } from '$lib/types';
	import Separator from '$lib/components/ui/separator/separator.svelte';
	import Badge from '$lib/components/ui/badge/badge.svelte';

	type Props = {
		orders: PendingOrder[];
	};
	let { orders }: Props = $props();
</script>

{#snippet orderRow(order: PendingOrder)}
	<div class={'flex flex-col items-start gap-2 rounded-lg border p-3 text-left text-sm '}>
		<div class="w-full space-y-2">
			<div class="flex justify-between gap-2">
				<div class="space-y-1">
					<div class="">
						ID: {order.id} | За дата: {new Date(
							order.orderType === 'DELIVERY' ? order.deliveryDate : order.returnDate
						).toLocaleDateString('bg-BG')}
					</div>
					<div>
						Цена: <span class="font-semibold">
							{order.totalPrice.toLocaleString('bg-BG', {
								style: 'currency',
								currency: 'BGN'
							})}</span
						>
					</div>
				</div>
				{#if order.assignenedTo}
					<div class="grid items-center">
						<div class="font-medium">Изпълнява:</div>
						<div class="">
							<div>{order.assignenedTo.firstName} {order.assignenedTo.lastName}</div>
						</div>
					</div>
				{/if}
				<OrderActions {order}></OrderActions>
			</div>

			<div></div>

			<div class="space-y-1">
				<div class="font-medium">Адрес</div>
				<Separator></Separator>
				<div class="flex gap-2 flex-wrap items-center">
					<div class="font-medium text-xs">
						<span class="text-muted-foreground">Държава: </span>
						<span>{order.deliveryAddress.countryName}</span>
					</div>
					<span class="text-muted-foreground">|</span>
					<div class="font-medium text-xs">
						<span class="text-muted-foreground">Област: </span>
						<span>{order.deliveryAddress.stateName}</span>
					</div>
					<span class="text-muted-foreground">|</span>
					<div class="font-medium text-xs">
						<span class="text-muted-foreground">Град: </span>
						<span>{order.deliveryAddress.townName}</span>
					</div>
					<span class="text-muted-foreground">|</span>
					<div class="font-medium text-xs">
						<span class="text-muted-foreground">ПК: </span>
						<span>{order.deliveryAddress.postCodeCode}</span>
					</div>
					<span class="text-muted-foreground">|</span>
					<div class="font-medium text-xs">
						<span class="text-muted-foreground">Улица: </span>
						<span>{order.deliveryAddress.street}</span>
					</div>
				</div>
				<div class="font-medium text-xs">
					<span class="text-muted-foreground">Описание: </span>
					<span>{order.deliveryAddress.description}</span>
				</div>
				<div class="font-medium text-xs">
					<span class="text-muted-foreground">Бележка: </span>
					<span>{order.note?.length > 0 ? order.note : '-'}</span>
				</div>
			</div>
		</div>
		<div class="w-full space-y-1">
			<div class="font-medium flex flex-wrap gap-2">
				<span>Поръчани артикули</span>
				<Badge>{order.orderType}</Badge>
			</div>
			<div class="space-y-2">
				{#each order.lines as itemObj}
					<Separator></Separator>
					<div class="flex justify-between gap-2">
						<div class="flex gap-2">
							<img
								width="40px"
								height="40px"
								class="max-w-[40px] max-h-[40px] object-contain"
								src={itemObj.item.imageUrl}
								alt=""
							/>
							<div class="space-y-1">
								<div>{itemObj.item.name}</div>
								<div class="text-xs">
									Цена на ден: <span class="font-semibold">
										{itemObj.item.pricePerDay.toLocaleString('bg-BG', {
											style: 'currency',
											currency: 'BGN'
										})}</span
									>
								</div>
							</div>
						</div>
						<div><span class="font-semibold">{itemObj.quantity}</span> бр.</div>
					</div>
				{/each}
			</div>
		</div>
	</div>
{/snippet}

<div class="grid gap-4 grid-cols-2">
	<div>
		<span class="font-bold text-lg">Доставка</span>
		<Separator class="mt-1 mb-3"></Separator>
		<div class="space-y-4">
			{#each orders.filter((or) => or.orderType === 'DELIVERY') as order}
				{@render orderRow(order)}
			{/each}
		</div>
	</div>

	<div>
		<span class="font-bold text-lg">Прибиране</span>
		<Separator class="mt-1 mb-3"></Separator>
		<div class="space-y-4">
			{#each orders.filter((or) => or.orderType === 'PICKUP') as order}
				{@render orderRow(order)}
			{/each}
		</div>
	</div>
</div>
