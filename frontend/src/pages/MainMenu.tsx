import { useState, useEffect } from 'react';
import NavBar from "../components/NavBar.tsx";
import Footer from "../components/Footer.tsx";
import { useNavigate } from 'react-router-dom';
import {
    getAllWaitingTickets,
    getAllDepartments,
    createTicketWithDepartment,
    getAllEmployees
} from "../service/apiService";
import WaitingTicketInterface from "../interfaces/WaitingTicketsInterface";
import DepartmentGetForOptionDTO from "../interfaces/DepartmentGetForOptionDTO";
import Employee from "../interfaces/Employee";
import { Button, Form } from "react-bootstrap";
import Ticket from "../interfaces/Ticket.ts";

const MainMenu = () => {
    const [tickets, setTickets] = useState<WaitingTicketInterface[]>([]);
    const [currentIndex, setCurrentIndex] = useState(0);
    const [departments, setDepartments] = useState<DepartmentGetForOptionDTO[]>([]);
    const [selectedDepartment, setSelectedDepartment] = useState<string>('');
    const [createdTicket, setCreatedTicket] = useState<Ticket | null>(null);
    const [employees, setEmployees] = useState<Employee[]>([]);
    const [selectedEmployee, setSelectedEmployee] = useState<string>('');
    const navigate = useNavigate();


    useEffect(() => {
        fetchTickets();
        fetchDepartments();
    }, []);

    useEffect(() => {
        fetchEmployees();
    }, []);

    const fetchTickets = async () => {
        try {
            const waitingData = await getAllWaitingTickets();
            setTickets(waitingData);
        } catch (error) {
            console.error('Error fetching tickets', error);
        }
    };

    const fetchDepartments = async () => {
        const departmentsData = await getAllDepartments();
        setDepartments(departmentsData);
        if (!selectedDepartment && departmentsData.length > 0) {
            setSelectedDepartment(departmentsData[0].name);
        }
    };

    const fetchEmployees = async () => {
        try {
            const employeesData = await getAllEmployees();
            setEmployees(employeesData);
            if (!selectedEmployee && employeesData.length > 0) {
                setSelectedEmployee(employeesData[0].id);
            }
        } catch (error) {
            console.error('Error fetching employees', error);
        }
    };

    const handleSelectEmployee = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setSelectedEmployee(e.target.value);
    };

    const handleGoToEmployeePage = () => {
        navigate(`/employee/${selectedEmployee}`);
    };

    const handleBook = async () => {
        const createdTicket = await createTicketWithDepartment(selectedDepartment);
        setCreatedTicket(createdTicket);
    };

    const handlePrevious = () => {
        setCurrentIndex(oldIndex => Math.max(oldIndex - 8, 0));
    };

    const handleNext = () => {
        setCurrentIndex(oldIndex => Math.min(oldIndex + 8, tickets.length - 1));
    };

    return (
        <>
            <NavBar />
            <div className="container mt-3">
                <div className="row">
                    <div className="col-4">
                        <div className="card">
                            <div className="card-header w-auto bg-primary brighter text-center text-light fs-2">
                                Ticket buchen
                            </div>
                            <div className="card-body">
                                <p className="text-primary brighter fs-2">Abteilung auswählen</p>
                                <Form.Select size="lg" className="bg-primary text-light mb-3" value={selectedDepartment}
                                             onChange={(e) => setSelectedDepartment(e.target.value)}>
                                    {departments.map((department) => (
                                        <option key={department.id} value={department.name}>
                                            {department.name}
                                        </option>
                                    ))}
                                </Form.Select>
                                <Button variant="primary" size="lg" onClick={handleBook}>
                                    Buchen
                                </Button>
                                {createdTicket && (
                                    <div className="card mt-3 border-primary border-5">
                                        <div className="card-body bg-primary brighter">
                                            <p className="card-title text-center text-light fs-2">Buchung
                                                erfolgreich:</p>
                                            <p className="card-text text-light text-center fs-2">Ihr
                                                Ticket: {createdTicket.ticketNr}</p>
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                    <div className="col-4">
                        <div className="card">
                            <div className="card-header w-auto bg-primary brighter text-center text-light fs-2">
                                Tickets verwalten
                            </div>
                            <div className="card-body">
                                <Form.Select size="lg" className="bg-primary text-light mb-3" value={selectedEmployee}
                                             onChange={handleSelectEmployee}>
                                    {employees.map((employee) => (
                                        <option key={employee.id} value={employee.id}>
                                            {employee.name + " " + employee.surname}
                                        </option>
                                    ))}
                                </Form.Select>
                                <Button variant="primary" size="lg" onClick={handleGoToEmployeePage}>
                                    zum Mitarbeiter
                                </Button>
                            </div>
                        </div>
                    </div>
                    <div className="col-4">
                        <div className="card">
                            <div className="card-header w-auto bg-primary brighter text-center text-light fs-2">
                                Warteliste
                            </div>
                            <div className="card-body">
                                {tickets
                                    .slice(currentIndex, currentIndex + 8)
                                    .map((ticket, index) => (
                                        <div key={index}
                                             className="card w-auto text-bg-primary text-center m-2">
                                            <p className="ticketNr fs-5">{ticket.ticketNr}</p>
                                        </div>
                                    ))
                                }
                                <div className="d-flex justify-content-between">
                                    <Button onClick={handlePrevious}>
                                        ◀
                                    </Button>
                                    <Button onClick={handleNext}>
                                        ▶
                                    </Button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <Footer/>
        </>
    );
};

export default MainMenu;