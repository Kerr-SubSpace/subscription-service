import './App.css'
import {SubscriptionCard} from './components/SubscriptionCard'
import {Subscription} from './types/Subscription';
import {Divider} from "@nextui-org/react";
import {useEffect, useState} from "react";

function App() {
    const [subscriptions, setSubscriptions] = useState<Subscription[]>([]);
    useEffect(() => {
        getSubscriptions().then((data) => {
            setSubscriptions(data);
        });
    }, []);
    return (
        <>
            <Divider className={"my-4"}/>
            <div style={{display: 'flex', flexWrap: 'wrap', gap: '1rem', justifyContent: 'center'}}>
                {subscriptions.map((subscription) => (
                    <SubscriptionCard key={subscription.id} subscription={subscription}/>
                ))}
            </div>
        </>
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
        billingDate: `${jsonObject.billingDate[0]}-${String(jsonObject.billingDate[1]).padStart(2, "0")}-${String(jsonObject.billingDate[2]).padStart(2, "0")}`,
    }))
}

export default App
