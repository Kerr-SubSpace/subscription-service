import {
    Button,
    ButtonGroup,
    Card,
    CardBody,
    CardFooter,
    CardHeader, Chip, ChipProps,
    Divider,
    Progress, Table, TableBody, TableCell, TableColumn, TableHeader, TableRow
} from "@nextui-org/react";

export function SubscriptionCard({subscription}) {
    let randomNumber = Math.floor(Math.random() * 30) + 1;
    const statusColorMap: Record<string, ChipProps["color"]> = {
        active: "success",
        paused: "warning",
        cancelled: "danger",
    };
    return (
        <Card>
            <CardHeader>
                <div className="flex flex-col">
                    <p className="text-md">{subscription.name}</p>
                    <p className="text-small text-opacity-80 text-default-500">{subscription.price}/month</p>
                </div>
            </CardHeader>
            <Divider/>
            <CardBody>
                <Chip className="capitalize" color={statusColorMap[subscription.status]} size="lg" variant="flat">
                    {subscription.status}
                </Chip>
                <Progress color={"secondary"}
                          value={randomNumber}
                          maxValue={subscription.renewalPeriodDays}
                          label={randomNumber + " days"}/>
                {/* Progress Bar (if applicable) */}
            </CardBody>
            <CardBody>
                <Table>
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
                            <TableCell>{subscription.status}</TableCell>
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
            </CardBody>
            <Divider/>
            <CardFooter>
                <ButtonGroup>
                    <Button name={"Details"}>Details</Button>
                    <Button name={"Pause"}>Pause</Button>
                    <Button name={"Cancel"}>Cancel</Button>
                </ButtonGroup>
                {/* Edit/Cancel Buttons */}
            </CardFooter>
        </Card>
    );
}
