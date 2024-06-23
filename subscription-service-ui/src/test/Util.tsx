import {SubscriptionStatus} from "../types/Subscription.tsx";

const randomSub = (name: string) => {
    const date = new Date()
    date.setDate(date.getDate() + Math.floor(Math.random() * 30))
    return {
        "id": Math.floor(Math.random() * 1000),
        "name": name,
        "price": (Math.random() * 20).toFixed(2),
        "status": randomEnum(SubscriptionStatus),
        "startDate": "2023-01-15",
        "renewalPeriodDays": 30,
        "billingDate": date.toDateString()
    }
}

export const subscriptions = [randomSub("Netflix"),
    randomSub("Spotify"),
    randomSub("Youtube Premium"),
    randomSub("Google One"),
    randomSub("IntelliJ IDEA"),
    randomSub("Hosting"),
    randomSub("Domain 1"),
    randomSub("Domain 2"),
    randomSub("Phone bill"),
    randomSub("Amazon"),
    randomSub("Kekazon"),
    randomSub("Testazon"),
    randomSub("Testarossa"),
    randomSub("LOLKEK"),
    randomSub("Ass"),
]

function randomEnum<T>(anEnum: T): T[keyof T] {
    // @ts-ignore
    const enumValues = (Object.values(anEnum) as unknown) as T[keyof T][];
    const randomIndex = Math.floor(Math.random() * enumValues.length);
    return enumValues[randomIndex];
}