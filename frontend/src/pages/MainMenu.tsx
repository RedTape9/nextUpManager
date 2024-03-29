import {useState, useEffect, ChangeEvent} from 'react';
import NavBar from "../components/NavBar.tsx";
import Footer from "../components/Footer.tsx";
import { useNavigate } from 'react-router-dom';
import {
    getAllWaitingTickets,
    getAllDepartments,
    createTicketWithDepartment,
    getAllEmployees
} from "../service/apiService.ts";
import '../styles/colors.css';
import WaitingTicketInterface from "../interfaces/WaitingTicketsInterface";
import DepartmentGetForOption from "../interfaces/DepartmentGetForOption.ts";
import EmployeeBasicInfo from "../interfaces/EmployeeBasicInfo.ts";
import {Button, Card, Col, Container, Form, Row, Spinner} from "react-bootstrap";
import Ticket from "../interfaces/Ticket.ts";
import {Client} from "@stomp/stompjs";
import TicketCreateDTO from "../interfaces/TicketCreateDTO.ts";
import {ArrowLeft, ArrowRight} from "react-bootstrap-icons";

const MainMenu = () => {
    const [tickets, setTickets] = useState<WaitingTicketInterface[]>([]);
    const [currentIndex, setCurrentIndex] = useState(0);
    const [departments, setDepartments] = useState<DepartmentGetForOption[]>([]);
    const [selectedDepartment, setSelectedDepartment] = useState<string>('');
    const [createdTicket, setCreatedTicket] = useState<Ticket | null>(null);
    const [employees, setEmployees] = useState<EmployeeBasicInfo[]>([]);
    const [selectedEmployee, setSelectedEmployee] = useState<string>('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {

        const client = new Client({
            brokerURL: 'wss://next-up-manager.onrender.com/wss',
            onConnect: () => {
                client.subscribe('/topic/updates', message =>{
                        console.log(`Received: ${message.body}`);
                        fetchTickets();
                    }
                );
            },
        });
        client.activate();
        return () => {
            client.deactivate();
        }


    }, []);

    useEffect(() => {
        fetchTickets();
        fetchDepartments();
        fetchEmployees();
    }, []);

    const fetchTickets = async () => {
        setIsLoading(true);
        try {
            const waitingData = await getAllWaitingTickets();
            setTickets(waitingData);
        } catch (error) {
            console.error('Error fetching tickets', error);
        } finally {
            setIsLoading(false);
        }
    };

    const fetchDepartments = async () => {
        setIsLoading(true);
        try{
        const departmentsData = await getAllDepartments();
        setDepartments(departmentsData);
        if (!selectedDepartment && departmentsData.length > 0) {
            setSelectedDepartment(departmentsData[0].id);
        }
        } catch (error) {
            console.error('Error fetching departments', error);
        } finally {
        setIsLoading(false);
        }
    };

    const fetchEmployees = async () => {
        setIsLoading(true);
        try {
            const employeesData = await getAllEmployees();
            setEmployees(employeesData);
            if (!selectedEmployee && employeesData.length > 0) {
                setSelectedEmployee(employeesData[0].id);
            }
        } catch (error) {
            console.error('Error fetching employees', error);
        } finally {
            setIsLoading(false);
        }
    };


    const handleSelectEmployee = (e: ChangeEvent<HTMLSelectElement>) => {
        setSelectedEmployee(e.target.value);
    };

    const handleGoToEmployeePage = () => {
        navigate(`/employee/${selectedEmployee}`);
    };

    const handleBook = async () => {
        if (selectedDepartment) {
            const ticketCreateDTO: TicketCreateDTO = {
                departmentId: selectedDepartment
            };
            const createdTicket = await createTicketWithDepartment(ticketCreateDTO);
            setCreatedTicket(createdTicket);
        }
    };

    const handleNext = () => {
        setCurrentIndex(oldIndex => {
            return (oldIndex + 7 >= tickets.length) ? tickets.length - 7 : oldIndex + 7;
        });
    };

    const handlePrevious = () => {
        setCurrentIndex(oldIndex => {
            return (oldIndex - 7 < 0) ? 0 : oldIndex - 7;
        });
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
                    <div className="d-flex align-items-center">
                        <Spinner animation="grow" role="status" variant="primary"/>
                        <span className="ms-3 text-primary">Loading...</span>
                    </div>
                </div>
            )}
            <div style={{filter: isLoading ? 'blur(5px)' : 'none'}}>
                <NavBar/>
                <Container className="mt-4" style={{ minHeight: '520px' }}>
                    <Row>
                        <Col md={4}>
                            <Card className="border-info mb-2">
                                <Card.Header className="w-auto bg-primary brighter text-center text-light fs-2">
                                    Ticket buchen
                                </Card.Header>
                                <Card.Body>
                                    <p className="text-primary brighter fs-2">Abteilung auswählen</p>
                                    <Form.Select size="lg" className="bg-primary text-light mb-3" value={selectedDepartment}
                                                 onChange={(e) => setSelectedDepartment(e.target.value)}>
                                        {departments.map((department) => (
                                            <option key={department.id} value={department.id}>
                                                {department.name}
                                            </option>
                                        ))}
                                    </Form.Select>
                                    <Button variant="primary" size="lg" onClick={handleBook}>
                                        Buchen
                                    </Button>
                                    {createdTicket && (
                                        <Card className="mt-3 border-primary border-5">
                                            <Card.Body className="bg-primary brighter">
                                                <p className="card-title text-center text-light fs-2">Buchung
                                                    erfolgreich:</p>
                                                <p className="card-text text-light text-center fs-2">Ihr
                                                    Ticket: {createdTicket.ticketNr}</p>
                                            </Card.Body>
                                        </Card>
                                    )}
                                </Card.Body>
                            </Card>
                        </Col>
                        <Col md={4}>
                            <Card className="border-info mb-2">
                                <Card.Header className="w-auto bg-primary brighter text-center text-light fs-2">
                                    Verwaltung
                                </Card.Header>
                                <Card.Body>
                                    <p className="text-primary brighter fs-2">Mitarbeiter auswählen</p>
                                    <Form.Select size="lg" className="bg-primary text-light mb-3" value={selectedEmployee}
                                                 onChange={handleSelectEmployee}>
                                        {employees.map((employee) => (
                                            <option key={employee.id} value={employee.id}>
                                                {employee.name + " " + employee.surname + " - ( " + employee.room.substring(0,1) + " )"}
                                            </option>
                                        ))}
                                    </Form.Select>
                                    <Button variant="primary" size="lg" onClick={handleGoToEmployeePage}>
                                        zum Mitarbeiter
                                    </Button>
                                </Card.Body>
                            </Card>
                        </Col>
                        <Col md={4}>
                            <Card className="border-info mb-2">
                                <Card.Header className="w-auto bg-primary brighter text-center text-light fs-2">
                                    Warteliste
                                </Card.Header>
                                <Card.Body>
                                    {
                                        tickets.length === 0 ? (
                                            <p className="w-auto text-primary brighter fs-2 text-center m-2">keine Tickets</p>
                                        ) :(
                                            tickets
                                                .slice(currentIndex, currentIndex + 7)
                                                .map((ticket, index) => (
                                                    <Card key={index}
                                                          className="w-auto text-bg-primary text-center m-2">
                                                        <p className="ticketNr fs-4">{ticket.ticketNr}</p>
                                                    </Card>
                                                )))
                                    }
                                    {tickets.length > 7 && (
                                        <div className="d-flex justify-content-between m-2 ">
                                            <Button className="bg-primary brighter" onClick={handlePrevious}>
                                                <ArrowLeft color="white" size={30} />
                                            </Button>
                                            <Button className="bg-primary brighter" onClick={handleNext}>
                                                <ArrowRight color="white" size={30} />
                                            </Button>
                                        </div>
                                    )}
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

export default MainMenu;