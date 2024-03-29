import {useState, useEffect} from 'react';
import { getAllWaitingTickets, getAllInProgressTickets } from '../service/apiService';
import WaitingTicketInterface from '../interfaces/WaitingTicketsInterface';
import InProgressTicketInterface from '../interfaces/InProgressTicketsInterface';
import NavBar from "../components/NavBar.tsx";
import '../styles/colors.css';
import Footer from "../components/Footer.tsx";
import {Card, Col, Container, Row, Spinner} from "react-bootstrap";
import {Client} from "@stomp/stompjs";
import {CarFrontFill, DoorOpenFill, PeopleFill, VectorPen} from "react-bootstrap-icons";

const WaitingRoomPage = () => {
    const [tickets, setTickets] = useState<WaitingTicketInterface[]>([]);
    const [inProgressTickets, setInProgressTickets] = useState<InProgressTicketInterface[]>([]);
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(false);
    const [highlightedTicketIndex, setHighlightedTicketIndex] = useState(0);


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

    useEffect(() => {
        const interval = setInterval(() => {
            setHighlightedTicketIndex(prevIndex => (prevIndex + 1) % inProgressTickets.length);
        }, 3000);

        return () => clearInterval(interval);
    }, [inProgressTickets.length]);

    const fetchTickets = async () => {
        setIsLoading(true);
        try {
            const waitingData = await getAllWaitingTickets();
            setTickets(waitingData);
            const inProgressData = await getAllInProgressTickets();
            setInProgressTickets(inProgressData);
        } catch (error) {
            console.error('Error fetching tickets', error);
            setError('Error fetching tickets');
        }
        setIsLoading(false);
    };

    if (error) {
        return <div>Error: {error}</div>;
    }

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
                        <Col md={3}>
                            <Card className="border-info mb-2">
                                <Card.Header className="w-auto bg-primary brighter text-center text-light">
                                    <p className="mb-1"><PeopleFill color="white"></PeopleFill> Einwohnermeldeamt</p>
                                </Card.Header>
                                <Card.Body>
                                    {
                                        tickets.length === 0 ? (
                                            <p className="w-auto text-primary brighter fs-2 text-center m-2">keine Tickets</p>
                                        ) :(
                                            tickets
                                                .filter(ticket => ticket.ticketNr[0].toUpperCase() === 'E')
                                                .slice(0, 8)
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
                                    <p className="mb-1"><VectorPen color="white"></VectorPen> Standesamt</p>
                                </Card.Header>
                                <Card.Body>
                                    {
                                        tickets.length === 0 ? (
                                            <p className="w-auto text-primary brighter fs-2 text-center m-2">keine Tickets</p>
                                        ) :(
                                            tickets
                                                .filter(ticket => ticket.ticketNr[0].toUpperCase() === 'S')
                                                .slice(0, 8)
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
                                    <p className="mb-1"><CarFrontFill color="white"></CarFrontFill> KFZ-Zulassungsstelle</p>
                                </Card.Header>
                                <Card.Body>
                                    {tickets.length === 0 ? (
                                        <p className="w-auto text-primary brighter fs-2 text-center m-2">keine Tickets</p>
                                    ) :(
                                        tickets
                                            .filter(ticket => ticket.ticketNr[0].toUpperCase() === 'K')
                                            .slice(0, 8)
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
                                            <Card key={index} className={`text-bg-primary d-flex align-items-center justify-content-center m-3 ${index === highlightedTicketIndex ? 'bg-primary brighter' : ''}`}
                                                  style={{width: '350px', height: '100px'}}>
                                                <p className="ticketNr fs-5 text-center"><p>{ticket.ticketNr}</p><DoorOpenFill color="white"></DoorOpenFill>Raum: {ticket.room}</p>
                                            </Card>
                                        ))
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
};

export default WaitingRoomPage;