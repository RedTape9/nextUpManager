import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import EmployeeDetailInfo from "../interfaces/EmployeeDetailInfo.ts";
import {
    getEmployeeById,
    getDepartmentById,
    getInProgressTicketByEmployeeId,
    assignNextTicketToEmployee
} from "../service/apiService.ts";
import NavBar from "../components/NavBar.tsx";
import {Button, Card, Col, Container, Form, Row} from 'react-bootstrap';
import Footer from "../components/Footer.tsx";
import TicketAssignmentDTO from "../interfaces/TicketAssignmentDTO.ts";

const EmployeePage = () => {
    const { employeeId } = useParams<{ employeeId: string }>();
    const [employee, setEmployee] = useState<EmployeeDetailInfo | null>(null);
    const [department, setDepartment] = useState(null);
    const [assignedTicket, setAssignedTicket] = useState<TicketAssignmentDTO | null>(null);

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


    // TODO: der 404 Fehler taucht trotzdem in der console auf, sofern noch kein Ticket IN_PROGRESS ist
    const fetchTicket = async () => {
        if (!employeeId) {
            console.error('employee id is undefined');
            return;
        }
        try {
            const assignedTicketData = await getInProgressTicketByEmployeeId(employeeId);
            setAssignedTicket(assignedTicketData);
        } catch (error) {
            if (error.response && error.response.status === 404) {
                setAssignedTicket(null);
            } else {
                console.error('Error fetching ticket', error);
            }
        }
    };

    useEffect(() => {
        fetchEmployee();
        fetchTicket();
    }, [employeeId]);

    const handleBookTicket = async () => {
        await assignNextTicketToEmployee(employeeId);
        await fetchTicket();
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
                            <Card.Body>
                                {employee && (
                                    <>
                                        <p>Name: {employee.name}</p>
                                        <p>Abteilung: {department?.name}</p>
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
                                    <Form.Control as="textarea" rows={4}/>
                                </Form>
                                <div className="d-flex align-items-baseline">
                                    <Button variant="primary" size="lg" className="m-3">Kunde nicht erschienen</Button>
                                    <Button variant="primary" size="lg" className="m-3">Ticket abschließen</Button>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={4}>
                        <Card>
                            <Card.Header className="w-auto bg-primary brighter text-center text-light fs-2">
                                Ticketverlauf
                            </Card.Header>
                            <Card.Body>
                                Verlauf...
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