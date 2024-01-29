import { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import EmployeeDetailInfo from "../interfaces/EmployeeDetailInfo.ts";
import {
    getEmployeeById,
    getDepartmentById,
    getInProgressTicketByEmployeeId,
    assignNextTicketToEmployee,
    updateTicketStatus,
    deleteAllTickets
} from "../service/apiService.ts";
import '../styles/colors.css';
import NavBar from "../components/NavBar.tsx";
import {Button, Card, Col, Container, Form, Row, Alert} from 'react-bootstrap';
import Footer from "../components/Footer.tsx";
import TicketAssignmentDTO from "../interfaces/TicketAssignmentDTO.ts";
import TicketUpdateDTO from "../interfaces/TicketUpdateDTO.ts";
import DepartmentGetForOption from "../interfaces/DepartmentGetForOption.ts";

const EmployeePage = () => {
    const { employeeId } = useParams<{ employeeId: string }>();
    const [employee, setEmployee] = useState<EmployeeDetailInfo | null>(null);
    const [department, setDepartment] = useState<DepartmentGetForOption | null>(null);
    const [assignedTicket, setAssignedTicket] = useState<TicketAssignmentDTO | null>(null);
    const [comment, setComment] = useState<string>('');
    const [showAlert, setShowAlert] = useState(false);

    const fetchEmployee = useCallback(async () => {
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
    }, [employeeId]);

    const fetchTicket = useCallback(async () => {
        if (!employeeId) {
            console.error('employee id is undefined');
            return;
        }
        try {
            const assignedTicketData = await getInProgressTicketByEmployeeId(employeeId);
            setAssignedTicket(assignedTicketData);
        } catch (error) {
            if (error instanceof Error) {
                console.error(error.message);
            }
        }
    }, [employeeId]);

    useEffect(() => {
        const fetchData = async () => {
            if (fetchEmployee) {
                await fetchEmployee();
            }
            if (fetchTicket) {
                await fetchTicket();
            }
        };

        fetchData();
    }, [employeeId, fetchEmployee, fetchTicket]);

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
        if (assignedTicket && assignedTicket.id && employeeId) {
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
            console.error('assigned ticket id is undefined assignedTickedId: ' + assignedTicket?.id + ' employeeId: ' + employeeId);
        }
    };

    const handleFinishTicket = async () => {
        if (assignedTicket && assignedTicket.id && employeeId) {
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
            console.error('assigned ticket id is undefined assignedTickedId: ' + assignedTicket?.id + ' employeeId: ' + employeeId);
        }
    };

    const handleDeleteAllTickets = async () => {
        setShowAlert(true);
    };

    const handleConfirmDelete = async () => {
        await deleteAllTickets();
        setShowAlert(false);
    };

    return (
        <>
            <NavBar />
            <Container className="mt-4">
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
                                    <Button variant="primary" size="lg" disabled={!!assignedTicket?.ticketNr} onClick={handleBookTicket}>Ticket buchen</Button>
                                    <p className="text-primary fs-4 mx-4">Ticket: {assignedTicket?.ticketNr}</p>
                                </div>

                                <Form className="mt-3">
                                    <Form.Label className="text-primary fs-4">Kommentar</Form.Label>
                                    <Form.Control as="textarea" rows={6} value={comment} onChange={e => setComment(e.target.value)}/>
                                </Form>
                                <div className="d-flex align-items-baseline">
                                    <Button onClick={handleCancelTicket} disabled={!assignedTicket?.ticketNr} className="m-2">Kunde nicht erschienen</Button>
                                    <Button onClick={handleFinishTicket} disabled={!assignedTicket?.ticketNr}>Ticket abschließen</Button>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={4}>
                        {showAlert && (
                            <Alert variant="danger" onClose={() => setShowAlert(false)} dismissible>
                                <Alert.Heading>Sind Sie sich sicher?</Alert.Heading>
                                <p className="mt-3">
                                    Wollen Sie wirklich alle Tickets löschen?
                                </p>
                                <p className="mb=3">
                                    Diese Aktion kann nicht rückgängig gemacht werden!
                                </p>
                                <Button variant="danger" className="mx-3" onClick={handleConfirmDelete}>Ja, lösche alles!</Button>
                                <Button variant="primary " onClick={() => setShowAlert(false)}>Nein, besser nicht</Button>
                            </Alert>
                        )}

                        <Card >
                            <Card.Header className="w-auto bg-warning brighter text-center text-danger fs-2">
                                Danger Zone
                            </Card.Header>
                            <Card.Body className="d-flex justify-content-center">
                                <Button variant="danger" className="fs-2" disabled={showAlert} onClick={handleDeleteAllTickets}>ALLE Tickets löschen!!!</Button>
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