export enum SubscriptionStatus {
    ACTIVE = 'ACTIVE',
    PAUSED = 'PAUSED',
    CANCELLED = 'CANCELLED'
}

export interface Subscription {
    id: number; // Assuming your backend generates IDs as numbers
    name: string;
    price: number; // Consider using a library like 'decimal.js' for precise currency calculations
    status: SubscriptionStatus;
    startDate: string; // ISO 8601 date format (YYYY-MM-DD)
    endDate?: string; // Nullable for non-recurring subscriptions
    renewalPeriodDays?: number; // Total Number of days in billing cycle
    billingDate: string; // ISO 8601 date format
}