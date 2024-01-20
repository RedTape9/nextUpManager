// frontend/src/pages/WaitingRoomPage.tsx
import { useEffect, useState } from 'react';
import { getTickets } from '../service/apiService';
import Ticket from '../interfaces/TicketInterface.ts';
import '../styles/WaitingRoomPage.css';

const WaitingRoomPage = () => {
    const [tickets, setTickets] = useState<Ticket[]>([]);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchTickets = async () => {
            try {
                const data = await getTickets();
                setTickets(data);
            } catch (error) {
                setError('Error fetching tickets');
                console.error('Error fetching tickets', error);
            }
        };

        fetchTickets();
    }, []);

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div className="container">
            <h1>Wartezimmer!</h1>
            {tickets.map((ticket, index) => (
                <div key={index} className="card">
                    <h2>Ticket {index + 1}</h2>
                    <p>Status: {ticket.currentStatus}</p>
                    <p>Ticket Nr: {ticket.ticketNr}</p>
                </div>
            ))}
        </div>
    );
};

export default WaitingRoomPage;