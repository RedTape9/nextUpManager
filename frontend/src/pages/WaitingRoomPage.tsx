import { useState, useEffect } from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { getAllWaitingTickets, getAllInProgressTickets } from '../service/apiService';
import WaitingTicketInterface from '../interfaces/WaitingTicketsInterface';
import InProgressTicketInterface from '../interfaces/InProgressTicketsInterface';
import '../styles/WaitingRoomPage.css';

const WaitingRoomPage = () => {
    const [tickets, setTickets] = useState<WaitingTicketInterface[]>([]);
    const [inProgressTickets, setInProgressTickets] = useState<InProgressTicketInterface[]>([]);
    const [error, setError] = useState<string | null>(null);
    let stompClient = null;

    useEffect(() => {
        connectWebSocket();
        return () => {
            if (stompClient) {
                stompClient.disconnect();
            }
        };
    }, []);

    const connectWebSocket = () => {
        const socket = new SockJS('/chat');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/updates', function (messageOutput) {
                fetchTickets(); // Fetch tickets when a new message is received
            });
        }, function (error) {
            setError('Failed to connect to WebSocket: ' + error);
        });
    };

    const fetchTickets = async () => {
        try {
            const waitingData = await getAllWaitingTickets();
            setTickets(waitingData);
            const inProgressData = await getAllInProgressTickets();
            setInProgressTickets(inProgressData);
        } catch (error) {
            setError('Error fetching tickets');
        }
    };

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <>
            <h1>Wartezimmer</h1>
            <div className="container">
                <div className="row">
                    <div className="col-3">
                        {tickets.map((ticket, index) => (
                            <div key={index} className="card">
                                <h2 className="ticketNr">{ticket.ticketNr}</h2>
                            </div>
                        ))}
                    </div>
                    <div className="col-3">
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
