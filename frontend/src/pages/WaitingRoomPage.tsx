import {useState, useEffect} from 'react';
import { getAllWaitingTickets, getAllInProgressTickets } from '../service/apiService';
import WaitingTicketInterface from '../interfaces/WaitingTicketsInterface';
import InProgressTicketInterface from '../interfaces/InProgressTicketsInterface';
import NavBar from "../components/NavBar.tsx";
import '../styles/colors.css';
import Footer from "../components/Footer.tsx";
import {Card, Col, Container, Row} from "react-bootstrap";
import {Client} from "@stomp/stompjs";

const WaitingRoomPage = () => {
    const [tickets, setTickets] = useState<WaitingTicketInterface[]>([]);
    const [inProgressTickets, setInProgressTickets] = useState<InProgressTicketInterface[]>([]);
    const [error, setError] = useState<string | null>(null);


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
    }, []);
    const fetchTickets = async () => {
        try {
            const waitingData = await getAllWaitingTickets();
            setTickets(waitingData);
            const inProgressData = await getAllInProgressTickets();
            setInProgressTickets(inProgressData);
        } catch (error) {
            console.error('Error fetching tickets', error);
            setError('Error fetching tickets');
        }
    };

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <>
            <NavBar />
            <Container className="mt-4" style={{ minHeight: '520px' }}>
                <Row>
                    <Col md={3}>
                        <Card className="border-info mb-2">
                            <Card.Header className="w-auto bg-primary brighter text-center text-light">
                                <p className="mb-1">Wartenbereich</p><p className="mb-1">Einwohnermeldeamt</p>
                            </Card.Header>
                            <Card.Body>
                                {
                                    tickets.length === 0 ? (
                                        <p className="w-auto text-primary brighter fs-2 text-center m-2">keine Tickets</p>
                                    ) :(
                                    tickets
                                    .filter(ticket => ticket.ticketNr[0].toUpperCase() === 'E')
                                    .slice(0, 7)
                                    .map((ticket, index) => (
                                        <Card key={index} className="w-auto text-bg-primary text-center m-2">
                                            <p className="ticketNr fs-4">{ticket.ticketNr}</p>
                                        </Card>
                                    )))
                                }
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={3}>
                        <Card className="border-info mb-2">
                            <Card.Header className="w-auto bg-primary brighter text-center text-light">
                                <p className="mb-1">Wartenbereich</p><p className="mb-1">Standesamt</p>
                            </Card.Header>
                            <Card.Body>
                                {
                                    tickets.length === 0 ? (
                                        <p className="w-auto text-primary brighter fs-2 text-center m-2">keine Tickets</p>
                                    ) :(
                                    tickets
                                    .filter(ticket => ticket.ticketNr[0].toUpperCase() === 'S')
                                    .slice(0, 7)
                                    .map((ticket, index) => (
                                        <Card key={index} className="w-auto text-bg-primary text-center m-2">
                                            <p className="ticketNr fs-4">{ticket.ticketNr}</p>
                                        </Card>
                                    )))}
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={3}>
                        <Card className="border-info mb-3">
                            <Card.Header className="w-auto bg-primary brighter text-center text-light">
                                <p className="mb-1">Wartenbereich</p> <p className="mb-1">KFZ-Zulassungsstelle</p>
                            </Card.Header>
                            <Card.Body>
                                {tickets.length === 0 ? (
                                        <p className="w-auto text-primary brighter fs-2 text-center m-2">keine Tickets</p>
                                    ) :(
                                    tickets
                                    .filter(ticket => ticket.ticketNr[0].toUpperCase() === 'K')
                                    .slice(0, 7)
                                    .map((ticket, index) => (
                                        <Card key={index} className="w-auto text-bg-primary text-center m-2">
                                            <p className="ticketNr fs-4">{ticket.ticketNr}</p>
                                        </Card>
                                    )))}
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={3} className="d-flex align-items-center justify-content-center">
                        <Card className=" border-info mb-2">
                            <Card.Header className="bg-primary brighter text-center text-light">
                                <p className="fs-2">Sie wurden aufgerufen</p>
                            </Card.Header>
                            <Card.Body className="d-flex align-items-center justify-content-center flex-wrap">
                                {tickets.length === 0 ? (
                                    <p className="w-auto text-primary brighter fs-2 text-center m-2">Derzeit keine Aufrufe</p>
                                ) :(
                                    inProgressTickets.map((ticket, index) => (
                                        <Card key={index} className="text-bg-primary d-flex align-items-center justify-content-center m-3"
                                              style={{width: '350px', height: '100px'}}>
                                            <p className="ticketNr fs-4 text-center"><p>{ticket.ticketNr}</p>Raum: {ticket.room}</p>
                                        </Card>
                                    ))
                                )}
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
            <Footer/>
        </>
    );
};

export default WaitingRoomPage;