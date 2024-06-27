import {
    Button,
    Card,
    CardBody,
    CardHeader,
    Chip, getKeyValue,
    Modal,
    ModalBody, ModalContent, ModalFooter,
    ModalHeader,
    Progress,
    Spacer,
    Table,
    TableBody, TableCell,
    TableColumn,
    TableHeader, TableRow,
    useDisclosure
} from "@nextui-org/react";
import {SubscriptionStatus} from "../types/Subscription.tsx";

export function SubscriptionCard({subscription}) {
    let randomNumber = Math.floor(Math.random() * 30) + 1;
    const {isOpen, onOpen, onOpenChange} = useDisclosure();
    return (
        <>
            <Card isPressable className={"max-w-2xl"} onClick={onOpen}>
                <CardHeader>
                    <h4 className="nextui-text-md">{subscription.name}</h4>
                    <Spacer x={0.5}/>
                    <p className="nextui-text-sm nextui-font-bold">
                        ${subscription.price}/month
                    </p>
                </CardHeader>
                <CardBody className={"py-8"}>
                    <StatusChip status={subscription.status}/>
                    <Spacer y={3}/>
                    <Progress
                        value={randomNumber}
                        maxValue={subscription.renewalPeriodDays}
                        color="primary"
                        className="nextui-progress-sm"
                    />
                </CardBody>
            </Card>
            <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
                <ModalContent className={"dark"}>
                    {(onClose) => (
                        <>
                            <ModalHeader className={"flex flex-col gap-1"}>{subscription.name}</ModalHeader>
                            <ModalBody>
                                <Table hideHeader>
                                    <TableHeader>
                                        <TableColumn>Field</TableColumn>
                                        <TableColumn>Value</TableColumn>
                                    </TableHeader>
                                    <TableBody>
                                        <TableRow>
                                            <TableCell>ID</TableCell>
                                            <TableCell>{subscription.id}</TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>Name</TableCell>
                                            <TableCell>{subscription.name}</TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>Price</TableCell>
                                            <TableCell>{subscription.price}</TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>Status</TableCell>
                                            <TableCell>
                                                <StatusChip status={subscription.status}/>
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>Billing Date</TableCell>
                                            <TableCell>{subscription.billingDate}</TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>StartDate</TableCell>
                                            <TableCell>{subscription.startDate}</TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>EndDate</TableCell>
                                            <TableCell>{subscription.endDate}</TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>Renewal</TableCell>
                                            <TableCell>{subscription.renewalPeriodDays}</TableCell>
                                        </TableRow>
                                    </TableBody>
                                </Table>
                            </ModalBody>
                            <ModalFooter>
                                {/* Add Edit/Update buttons here if needed */}
                                <Button className={"flex-auto"} color="danger" variant={"light"} onPress={onClose}>
                                    Close
                                </Button>
                            </ModalFooter>
                        </>)}
                </ModalContent>
            </Modal>
        </>
    );
}

function StatusChip({status}) {
    return (
        <Chip
            color={`${status === SubscriptionStatus.PAUSED
                ? "warning"
                : status === SubscriptionStatus.CANCELLED
                    ? "danger"
                    : "default"}`}
            className={`nextui-text-sm capitalize`}
            variant={"flat"}
        >
            {status}
        </Chip>)
}
