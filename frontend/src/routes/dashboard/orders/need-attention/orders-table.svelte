<script lang="ts">
	import type { OrderThatNeedsAttention } from '$lib/types';
	import Separator from '$lib/components/ui/separator/separator.svelte';
	import Badge from '$lib/components/ui/badge/badge.svelte';

	type Props = {
		orders: OrderThatNeedsAttention[];
	};
	let { orders }: Props = $props();
</script>

{#snippet orderRow(order: OrderThatNeedsAttention)}
	<div class={'flex flex-col items-start gap-2 rounded-lg border p-3 text-left text-sm '}>
		<div class="w-full space-y-2">
			<div class="flex justify-between gap-2">
				<div class="space-y-1">
					<div class="">
						ID: {order.order.id} | За дата: {new Date(
							order.order.orderType === 'DELIVERY'
								? order.order.deliveryDate
								: order.order.returnDate
						).toLocaleDateString('bg-BG')}
					</div>
					<div>
						Цена: <span class="font-semibold">
							{order.order.totalPrice.toLocaleString('bg-BG', {
								style: 'currency',
								currency: 'BGN'
							})}</span
						>
					</div>
				</div>
				{#if order.order.assignenedTo}
					<div class="grid items-center">
						<div class="font-medium">Изпълнява:</div>
						<div class="">
							<div>{order.order.assignenedTo.firstName} {order.order.assignenedTo.lastName}</div>
						</div>
					</div>
				{/if}
			</div>

			<div></div>

			<div class="space-y-1">
				<div class="font-medium">Адрес</div>
				<Separator></Separator>
				<div class="flex gap-2 flex-wrap items-center">
					<div class="font-medium text-xs">
						<span class="text-muted-foreground">Държава: </span>
						<span>{order.order.deliveryAddress.countryName}</span>
					</div>
					<span class="text-muted-foreground">|</span>
					<div class="font-medium text-xs">
						<span class="text-muted-foreground">Област: </span>
						<span>{order.order.deliveryAddress.stateName}</span>
					</div>
					<span class="text-muted-foreground">|</span>
					<div class="font-medium text-xs">
						<span class="text-muted-foreground">Град: </span>
						<span>{order.order.deliveryAddress.townName}</span>
					</div>
					<span class="text-muted-foreground">|</span>
					<div class="font-medium text-xs">
						<span class="text-muted-foreground">ПК: </span>
						<span>{order.order.deliveryAddress.postCodeCode}</span>
					</div>
					<span class="text-muted-foreground">|</span>
					<div class="font-medium text-xs">
						<span class="text-muted-foreground">Улица: </span>
						<span>{order.order.deliveryAddress.street}</span>
					</div>
				</div>
				<div class="font-medium text-xs">
					<span class="text-muted-foreground">Описание: </span>
					<span>{order.order.deliveryAddress.description}</span>
				</div>
				<div class="font-medium text-xs">
					<span class="text-muted-foreground">Бележка: </span>
					<span>{order.order.note?.length > 0 ? order.order.note : '-'}</span>
				</div>
			</div>
		</div>
		<Separator class="my-3"></Separator>

		<div class="w-full max-lg:space-y-3 lg:grid grid-cols-2 gap-4">
			<div class="w-full space-y-1">
				<div class="font-medium flex flex-wrap gap-2">
					<span>Поръчани артикули</span>
				</div>
				<div class="space-y-2">
					{#each order.order.lines as itemObj}
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
							<div class="flex flex-col items-end gap-1">
								<div>Поръчано количество:</div>
								<div><span class="font-semibold">{itemObj.quantity}</span> бр.</div>
							</div>
						</div>
					{/each}
				</div>
			</div>

			<div class="w-full space-y-1">
				<div class="font-medium">
					<span>Артикули извън наличност</span>
				</div>
				<div class="space-y-2">
					{#each order.missingItemsQuantity as itemObj}
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
							<div class="flex flex-col items-end gap-1">
								<div>Липсващо количество:</div>
								<div><span class="font-semibold">{itemObj.quantity}</span> бр.</div>
							</div>
						</div>
					{/each}
				</div>
			</div>
		</div>
	</div>
{/snippet}

<div class="grid gap-4">
	<div>
		<Separator class="mt-1 mb-3"></Separator>
		<div class="space-y-4">
			{#each orders.filter((or) => or.order.orderType === 'DELIVERY') as order}
				{@render orderRow(order)}
			{/each}
		</div>
	</div>
</div>
