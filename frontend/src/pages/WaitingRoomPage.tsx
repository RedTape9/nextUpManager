// frontend/src/pages/WaitingRoomPage.tsx
import { useEffect, useState } from 'react';
import { getTickets } from '../service/apiService';
import { Card, Container } from 'react-bootstrap';

const WaitingRoomPage = () => {
    const [tickets, setTickets] = useState([]);

    useEffect(() => {
        const fetchTickets = async () => {
            const data = await getTickets();
            setTickets(data);
        };

        fetchTickets();
    }, []);

    //kommentar
    return (
        <Container>
            <h1>Wartezimmer</h1>
            {tickets.map((ticket, index) => (
                <Card key={index} className="mb-3">
                    <Card.Header>Ticket {index + 1}</Card.Header>
                    <Card.Body>
                        <Card.Title>{ticket.id}</Card.Title>
                        <Card.Text>
                            {ticket.ticketNr}
                        </Card.Text>
                    </Card.Body>
                </Card>
            ))}
        </Container>
    );
};

export default WaitingRoomPage;