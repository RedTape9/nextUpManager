import {useState, useEffect, useCallback, useRef} from 'react';
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
import {Button, Card, Col, Container, Form, Row, Alert, Spinner} from 'react-bootstrap';
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
    const [isLoading, setIsLoading] = useState(false);
    const loadingTimeoutRef = useRef<NodeJS.Timeout | null>(null);

    const fetchEmployee = useCallback(async () => {
        loadingTimeoutRef.current = setTimeout(() => setIsLoading(true), 1000);
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
        clearTimeout(loadingTimeoutRef.current);
        setIsLoading(false);
    }, [employeeId]);

    const fetchTicket = useCallback(async () => {
        loadingTimeoutRef.current = setTimeout(() => setIsLoading(true), 1000);
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
        clearTimeout(loadingTimeoutRef.current);
        setIsLoading(false);
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
                commentByEmployee: comment,
                currentStatus: 'CANCELED'
            };
            await updateTicketStatus(assignedTicket.id, updateDTO);
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
                commentByEmployee: comment,
                currentStatus: 'FINISHED'
            };
            await updateTicketStatus(assignedTicket.id, updateDTO);
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
            {isLoading && (
                <div style={{
                    position: 'fixed',
                    top: 0,
                    left: 0,
                    width: '100%',
                    height: '100%',
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    backgroundColor: 'rgba(0, 0, 0, 0.5)',
                    zIndex: 9999,
                }}>
                    <Spinner animation="border" role="status" variant="primary">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </div>
            )}
            <div style={{filter: isLoading ? 'blur(5px)' : 'none'}}>
                <NavBar />
                <Container className="mt-4" style={{ minHeight: '520px' }}>
                    <Row>
                        <Col md={4}>
                            <Card className="border-info mb-2">
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
                            <Card className="border-info mb-2">
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
                                    <div style={{display: 'flex', justifyContent: 'space-between'}}>
                                        <Button variant="danger" className="mx-3" onClick={handleConfirmDelete}>Ja, lösche
                                            alles!</Button>
                                        <Button variant="primary " onClick={() => setShowAlert(false)}>Nein</Button>
                                    </div>
                                </Alert>
                            )}

                            <Card>
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
            </div>
        </>
    );
}

export default EmployeePage;
