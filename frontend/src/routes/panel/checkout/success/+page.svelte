<script lang="ts">
	import { emptyCart } from '$lib/stores/cart.svelte';

	$effect(() => {
		emptyCart();
	});
</script>

<div class="h-[90dvh] flex items-center justify-center">
	<div class="rounded-md bg-green-50 p-4">
		<div class="flex">
			<div class="shrink-0">
				<svg
					class="size-5 text-green-400"
					viewBox="0 0 20 20"
					fill="currentColor"
					aria-hidden="true"
					data-slot="icon"
				>
					<path
						fill-rule="evenodd"
						d="M10 18a8 8 0 1 0 0-16 8 8 0 0 0 0 16Zm3.857-9.809a.75.75 0 0 0-1.214-.882l-3.483 4.79-1.88-1.88a.75.75 0 1 0-1.06 1.061l2.5 2.5a.75.75 0 0 0 1.137-.089l4-5.5Z"
						clip-rule="evenodd"
					/>
				</svg>
			</div>
			<div class="ml-3">
				<h3 class="text-sm font-medium text-green-800">Успшно завършена поръчка</h3>
				<div class="mt-2 text-sm text-green-700">
					<p>
						Поръчката ви е успешно приета. Наш служител ще се свърже с вас за потвърждение.
						Благодарим ви за доверието.
					</p>
				</div>
				<div class="mt-4">
					<div class="-mx-2 -my-1.5">
						<a
							href="/panel"
							class="rounded-md bg-green-50 px-2 py-1.5 text-sm font-medium text-green-800 hover:bg-green-100 focus:outline-none focus:ring-2 focus:ring-green-600 focus:ring-offset-2 focus:ring-offset-green-50"
							>Към панела</a
						>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
