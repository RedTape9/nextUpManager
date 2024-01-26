import { useState, useEffect } from 'react';
import NavBar from "../components/NavBar.tsx";
import Footer from "../components/Footer.tsx";
import { getAllWaitingTickets, getAllDepartments, createTicketWithDepartment } from "../service/apiService";
import WaitingTicketInterface from "../interfaces/WaitingTicketsInterface";
import DepartmentGetForOptionDTO from "../interfaces/DepartmentGetForOptionDTO";
import { Button, Form } from "react-bootstrap";

const MainMenu = () => {
    const [tickets, setTickets] = useState<WaitingTicketInterface[]>([]);
    const [currentIndex, setCurrentIndex] = useState(0);
    const [departments, setDepartments] = useState<DepartmentGetForOptionDTO[]>([]);
    const [selectedDepartment, setSelectedDepartment] = useState<string>('');

    useEffect(() => {
        fetchTickets();
        fetchDepartments();
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

    const handleBook = async () => {
        await createTicketWithDepartment(selectedDepartment);
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
                            <div className="card-header w-auto bg-primary brighter text-center text-light">
                                Ticket buchen
                            </div>
                            <div className="card-body">
                                <h4 className="text-primary brighter">Abteilung auswählen</h4>
                                <Form.Select size="lg" className="bg-primary text-light mb-3" value={selectedDepartment} onChange={(e) => setSelectedDepartment(e.target.value)}>
                                    {departments.map((department) => (
                                        <option key={department.id} value={department.name}>
                                            {department.name}
                                        </option>
                                    ))}
                                </Form.Select>
                                <Button variant="primary" size="lg" onClick={handleBook}>
                                    Buchen
                                </Button>{' '}
                            </div>
                        </div>
                    </div>
                    <div className="col-4">
                        <div className="card">
                            <div className="card-header w-auto bg-primary brighter text-center text-light">
                                Tickets verwalten
                            </div>
                            <div className="card-body">

                            </div>
                        </div>
                    </div>
                    <div className="col-4">
                        <div className="card">
                            <div className="card-header w-auto bg-primary brighter text-center text-light">
                                Warteliste
                            </div>
                            <div className="card-body">
                                {tickets
                                    .slice(currentIndex, currentIndex + 8)
                                    .map((ticket, index) => (
                                        <div key={index}
                                             className="card w-auto text-bg-primary text-center m-2">
                                            <h2 className="ticketNr">{ticket.ticketNr}</h2>
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