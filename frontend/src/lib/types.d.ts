type User = null | {
	token: string;
	role: 'ADMIN' | 'MANAGER' | 'USER';
	username: string;
};
