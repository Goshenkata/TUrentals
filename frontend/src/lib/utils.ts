import { type ClassValue, clsx } from 'clsx';
import { twMerge } from 'tailwind-merge';

export function cn(...inputs: ClassValue[]) {
	return twMerge(clsx(inputs));
}

export function generateUrlParams(params: Record<string, string | null>): string {
	const searchParams = new URLSearchParams();
	Object.entries(params).forEach(([key, value]) => {
		if (value) {
			searchParams.append(key, value);
		}
	});
	const queryString = searchParams.toString();
	return queryString ? `?${queryString}` : '';
}
