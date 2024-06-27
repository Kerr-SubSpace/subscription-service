import './App.css'
import {SubscriptionCard} from './components/SubscriptionCard'
import {Subscription} from './types/Subscription';
import {useEffect, useState} from "react";
import {Button, Divider, Navbar, NavbarBrand, NavbarContent, Spacer} from "@nextui-org/react";

function App() {
    const [subscriptions, setSubscriptions] = useState<Subscription[]>([]);
    useEffect(() => {
        getSubscriptions().then((data) => {
            setSubscriptions(data);
        });
    }, []);
    return (
        <div className="relative h-screen">
            <Navbar className="flex min-w-[64rem] w-full h-min justify-center sticky">
                <NavbarBrand className="">
                    <span>Subscription Tracker</span>
                </NavbarBrand>
                <NavbarContent className="flex-row-reverse">
                    <Button onClick={createSubscription}>Add</Button>
                </NavbarContent>
            </Navbar>
            <Spacer y={32}/>
            <main className="">
                <div style={{
                    display: 'flex',
                    flexWrap: 'wrap',
                    gap: "1rem",
                    justifyContent: 'center',
                }}>
                    {subscriptions.length != 0
                        ? subscriptions.map((subscription) => (
                            <SubscriptionCard key={subscription.id} subscription={subscription}/>
                        ))
                        : <span className={"text-2xl text-neutral-500 italic"}>Add a subscription</span>
                    }
                </div>
            </main>
        </div>
    )
}

const getSubscriptions = async () => {
    const backendHost = import.meta.env.VITE_SERVICE_URL
    const response = await fetch(`${backendHost}/subscription-service/subscriptions`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });
    const json = await response.json();
    return json.map((jsonObject: any): Subscription => ({
        id: jsonObject.id,
        name: jsonObject.name,
        price: jsonObject.price,
        status: jsonObject.status,
        startDate: `${jsonObject.startDate[0]}-${String(jsonObject.startDate[1]).padStart(2, "0")}-${String(jsonObject.startDate[2]).padStart(2, "0")}`,
        // renewalPeriodDays: jsonObject.renewalPeriodDays,
        billingDate:
            `${jsonObject.billingDate[0]}-${String(jsonObject.billingDate[1]).padStart(2, "0")}-${String(jsonObject.billingDate[2]).padStart(2, "0")}`,
    }))
}

const createSubscription = async () => {
    const backendHost = import.meta.env.VITE_SERVICE_URL
    const raw = JSON.stringify({
        "name": "Sporkify",
        "price": Math.floor(Math.random() * 20).toFixed(2),
        "startDate": "2024-04-30",
        "billingDate": "2024-06-21",
        "status": "ACTIVE"
    });

    const requestOptions = {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: raw
    };

    const response = await fetch(`${backendHost}/subscription-service/subscriptions`, requestOptions);
    return response.json()
}

export default App
