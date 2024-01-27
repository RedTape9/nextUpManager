import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import EmployeeDetailInfo from "../interfaces/EmployeeDetailInfo.ts";
import {
    getEmployeeById,
    getDepartmentById,
    getInProgressTicketByEmployeeId,
    assignNextTicketToEmployee, updateTicketStatus
} from "../service/apiService.ts";
import NavBar from "../components/NavBar.tsx";
import {Button, Card, Col, Container, Form, Row} from 'react-bootstrap';
import Footer from "../components/Footer.tsx";
import TicketAssignmentDTO from "../interfaces/TicketAssignmentDTO.ts";
import TicketUpdateDTO from "../interfaces/TicketUpdateDTO.ts";

const EmployeePage = () => {
    const { employeeId } = useParams<{ employeeId: string }>();
    const [employee, setEmployee] = useState<EmployeeDetailInfo | null>(null);
    const [department, setDepartment] = useState(null);
    const [assignedTicket, setAssignedTicket] = useState<TicketAssignmentDTO | null>(null);
    const [comment, setComment] = useState<string>('');

    const fetchEmployee = async () => {
        if (!employeeId) {
            console.error('employee id is undefined');
            return;
        }
        const employeeData = await getEmployeeById(employeeId);
        setEmployee(employeeData);

        if (employeeData.departmentId) {
            const departmentData = await getDepartmentById(employeeData.departmentId);
            setDepartment(departmentData);
        }
    };

    const fetchTicket = async () => {
        if (!employeeId) {
            console.error('employee id is undefined');
            return;
        }
        try {
            const assignedTicketData = await getInProgressTicketByEmployeeId(employeeId);
            console.log("test: " + JSON.stringify(assignedTicketData, null, 2));
            setAssignedTicket(assignedTicketData);
            console.log("test: " + JSON.stringify(assignedTicket, null, 2));
        } catch (error) {
            if (error instanceof Error) {
                console.error(error.message);
            }
        }
    };



    useEffect(() => {
        fetchEmployee();
        fetchTicket();
    }, [employeeId]);

    useEffect(() => {
        console.log("Assigned Ticket nach Update: ", assignedTicket);
    }, [assignedTicket]);

    const handleBookTicket = async () => {
        if (employeeId) {
            await assignNextTicketToEmployee(employeeId);
            await fetchTicket();
        }
    };

    const handleCancelTicket = async () => {
        if (assignedTicket.id && employeeId) {
            const updateDTO: TicketUpdateDTO = {
                currentStatus: 'CANCELED',
                commentByEmployee: comment,
                statusHistory: [...assignedTicket.statusHistory, {status: 'CANCELED'}]
            };
            await updateTicketStatus(assignedTicket.id, employeeId, updateDTO);
            setComment('');
            await fetchTicket();
        }
        else {
            console.error('assigned ticket id is undefined assignedTickedId: ' + assignedTicket.id + ' employeeId: ' + employeeId);
        }
    };

    const handleFinishTicket = async () => {
        if (assignedTicket.id && employeeId) {
            const updateDTO: TicketUpdateDTO = {
                currentStatus: 'FINISHED',
                commentByEmployee: comment,
                statusHistory: [...assignedTicket.statusHistory, {status: 'FINISHED'}]
            };
            await updateTicketStatus(assignedTicket.id, employeeId, updateDTO);
            setComment('');
            await fetchTicket();
        }
        else {
            console.error('assigned ticket id is undefined assignedTickedId: ' + assignedTicket.id + ' employeeId: ' + employeeId);
        }
    };

    return (
        <>
            <NavBar />
            <Container className="mt-3">
                <Row>
                    <Col md={4}>
                        <Card>
                            <Card.Header className="w-auto bg-primary brighter text-center text-light fs-2">
                                Mitarbeiter Details
                            </Card.Header>
                            <Card.Body className="text-primary fs-3">
                                {employee && (
                                    <>
                                        <p>Name: {employee.name} {employee.surname}</p>
                                        <p>Abteilung: {department?.name}</p>
                                        <p>Raum: {employee.room}</p>
                                    </>
                                )}
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={4}>
                        <Card>
                            <Card.Header className="w-auto bg-primary brighter text-center text-light fs-2">
                                Bearbeitung
                            </Card.Header>
                            <Card.Body>
                                <div className="d-flex align-items-baseline">
                                    <Button variant="primary" size="lg" onClick={handleBookTicket}>Ticket buchen</Button>
                                    <p className="text-primary fs-4 mx-4">Ticket: {assignedTicket?.ticketNr}</p>
                                </div>

                                <Form className="mt-3">
                                    <Form.Label className="text-primary fs-4">Kommentar</Form.Label>
                                    <Form.Control as="textarea" rows={6} value={comment} onChange={e => setComment(e.target.value)}/>
                                </Form>
                                <div className="d-flex align-items-baseline">
                                    <Button onClick={handleCancelTicket} className="m-2">Kunde nicht erschienen</Button>
                                    <Button onClick={handleFinishTicket}>Ticket abschließen</Button>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={4}>
                        <Card>
                            <Card.Header className="w-auto bg-primary brighter text-center text-light fs-2">
                                Status
                            </Card.Header>
                            <Card.Body>
                                <p>Status: {assignedTicket?.currentStatus}</p>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
            <Footer/>
        </>
    );
}

export default EmployeePage;