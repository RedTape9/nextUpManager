import { useEffect, useState } from 'react';
import { getAllWaitingTickets, getAllInProgressTickets } from '../service/apiService';
import WaitingTicketInterface from '../interfaces/WaitingTicketsInterface';
import InProgressTicketInterface from '../interfaces/InProgressTicketsInterface';
import '../styles/WaitingRoomPage.css';

const WaitingRoomPage = () => {
    const [tickets, setTickets] = useState<WaitingTicketInterface[]>([]);
    const [inProgressTickets, setInProgressTickets] = useState<InProgressTicketInterface[]>([]);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const socket = new WebSocket('ws://localhost:8080/ws'); // URL anpassen

        socket.onopen = () => {
            console.log('WebSocket connected');
        };

        socket.onmessage = () => {
            console.log('Data updated');
            fetchTickets(); // Lädt Tickets neu, wenn eine Nachricht empfangen wird
        };

        socket.onclose = () => {
            console.log('WebSocket disconnected');
        };

        socket.onerror = (error) => {
            console.error('WebSocket error:', error);
        };

        const fetchTickets = async () => {
            try {
                const waitingData = await getAllWaitingTickets();
                setTickets(waitingData);
                const inProgressData = await getAllInProgressTickets();
                setInProgressTickets(inProgressData);
            } catch (error) {
                setError('Error fetching tickets');
                console.error('Error fetching tickets', error);
            }
        };

        fetchTickets();

        return () => {
            socket.close();
        };
    }, []);

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <>
            <h1>Wartezimmer</h1>
            <div className="m-container">
                <div className="m-card">
                    <div className="ticketContainer">
                        {tickets.map((ticket, index) => (
                            <div key={index} className="card">
                                <h2 className="ticketNr">{ticket.ticketNr}</h2>
                            </div>
                        ))}
                    </div>
                </div>
                <div className="m-card">
                    <div className="ticketContainer">
                        {inProgressTickets.map((ticket, index) => (
                            <div key={index} className="card">
                                <h2 className="ticketNr">{ticket.ticketNr}: {ticket.room}</h2>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </>
    );
};

export default WaitingRoomPage;
