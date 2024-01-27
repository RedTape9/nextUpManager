import {useState, useEffect, useRef} from 'react';
// import SockJS from 'sockjs-client';
// import Stomp from 'stompjs';
import { getAllWaitingTickets, getAllInProgressTickets } from '../service/apiService';
import WaitingTicketInterface from '../interfaces/WaitingTicketsInterface';
import InProgressTicketInterface from '../interfaces/InProgressTicketsInterface';
import NavBar from "../components/NavBar.tsx";
import '../styles/colors.css';
import Footer from "../components/Footer.tsx";
import {Card, Col, Container, Row} from "react-bootstrap";

const WaitingRoomPage = () => {
    const [tickets, setTickets] = useState<WaitingTicketInterface[]>([]);
    const [inProgressTickets, setInProgressTickets] = useState<InProgressTicketInterface[]>([]);
    const [error, setError] = useState<string | null>(null);
    // const stompClient = useRef<Stomp.Client | null>(null);

    useEffect(() => {
        // const socket = new SockJS('http://localhost:8080/ws');
        // stompClient.current = Stomp.over(socket);
        //
        // stompClient.current.connect({}, frame => {
        //     console.log('Connected: ' + frame);
        //     stompClient.current?.subscribe('/topic/updates', () => {
        //         fetchTickets();
        //     });
        // }, error => {
        //     console.error('Connection error:', error);
        //     setError('Failed to connect to WebSocket: ' + error);
        // });
        //
        // return () => {
        //     if (stompClient.current?.connected) {
        //         stompClient.current.disconnect(() => {
        //             console.log('Disconnected');
        //         });
        //     }
        // };

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
            <Container className="mt-3">
                <Row>
                    <Col md={2}>
                        <Card>
                            <Card.Header className="w-auto bg-primary brighter text-center text-light">
                                Wartenbereich Einwohnermeldeamt
                            </Card.Header>
                            <Card.Body>
                                {tickets
                                    .filter(ticket => ticket.ticketNr[0].toUpperCase() === 'E')
                                    .slice(0, 9)
                                    .map((ticket, index) => (
                                        <Card key={index} className="w-auto text-bg-primary text-center m-2">
                                            <p className="ticketNr fs-4">{ticket.ticketNr}</p>
                                        </Card>
                                    ))
                                }
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={2}>
                        <Card>
                            <Card.Header className="w-auto bg-primary brighter text-center text-light">
                                Wartenbereich Standesamt
                            </Card.Header>
                            <Card.Body>
                                {tickets
                                    .filter(ticket => ticket.ticketNr[0].toUpperCase() === 'S')
                                    .slice(0, 9)
                                    .map((ticket, index) => (
                                        <Card key={index} className="w-auto text-bg-primary text-center m-2">
                                            <p className="ticketNr fs-4">{ticket.ticketNr}</p>
                                        </Card>
                                    ))}
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={2}>
                        <Card>
                            <Card.Header className="w-auto bg-primary brighter text-center text-light">
                                Wartenbereich KFZ-Zulassungsstelle
                            </Card.Header>
                            <Card.Body>
                                {tickets
                                    .filter(ticket => ticket.ticketNr[0].toUpperCase() === 'K')
                                    .slice(0, 9)
                                    .map((ticket, index) => (
                                        <Card key={index} className="w-auto text-bg-primary text-center m-2">
                                            <p className="ticketNr fs-4">{ticket.ticketNr}</p>
                                        </Card>
                                    ))}
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={6} className="d-flex align-items-center justify-content-center">
                        <Card className="w-75 h-100 d-flex align-items-center justify-content-center">
                            <Card.Header className="w-100 bg-primary brighter text-center text-light">
                                <p className="fs-6">Sie wurden aufgerufen</p>
                            </Card.Header>
                            <Card.Body className="d-flex align-items-center justify-content-center flex-wrap">
                                {inProgressTickets.map((ticket, index) => (
                                    <Card key={index} className="text-bg-primary d-flex align-items-center justify-content-center m-3"
                                          style={{width: '350px', height: '100px'}}>
                                        <p className="ticketNr fs-6">{ticket.ticketNr} Raum: {ticket.room}</p>
                                    </Card>
                                ))}
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