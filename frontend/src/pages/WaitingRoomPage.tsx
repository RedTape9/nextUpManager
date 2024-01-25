import { useState, useEffect } from 'react';
// import SockJS from 'sockjs-client';
// import Stomp from 'stompjs';
import { getAllWaitingTickets, getAllInProgressTickets } from '../service/apiService';
import WaitingTicketInterface from '../interfaces/WaitingTicketsInterface';
import InProgressTicketInterface from '../interfaces/InProgressTicketsInterface';
//import '../styles/WaitingRoomPage.css';
import NavBar from "../components/NavBar.tsx";
import '../styles/colors.css';
import Footer from "../components/Footer.tsx";

const WaitingRoomPage = () => {
    const [tickets, setTickets] = useState<WaitingTicketInterface[]>([]);
    const [inProgressTickets, setInProgressTickets] = useState<InProgressTicketInterface[]>([]);
    const [error, setError] = useState<string | null>(null);
    // let stompClient: Stomp.Client | null = null;

    useEffect(() => {
        // const socket = new SockJS('http://localhost:8080/test');
        // stompClient = Stomp.over(socket);

        // stompClient?.connect({}, frame => {
        //     console.log('Connected: ' + frame);
        //     stompClient?.subscribe('/topic/updates', () => {
        //         fetchTickets();
        //     });
        // }, error => {
        //     console.error('Connection error:', error);
        //     setError('Failed to connect to WebSocket: ' + error);
        // });

        // return () => {
        //     if (stompClient?.connected) {
        //         stompClient.disconnect(() => {
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
            <div className="container mt-3" >
                <div className="row">
                    <div className="col-2">
                        <div className="card">
                            <div className="card-header w-auto bg-primary brighter text-center text-light">
                                Wartenbereich Einwohnermeldeamt
                            </div>
                            <div className="card-body">
                                {tickets
                                    .filter(ticket => ticket.ticketNr[0].toUpperCase() === 'E')
                                    .slice(0, 9)
                                    .map((ticket, index) => (
                                        <div key={index} className="card w-auto text-bg-primary text-center m-2">
                                            <h2 className="ticketNr">{ticket.ticketNr}</h2>
                                        </div>
                                    ))
                                }
                            </div>
                        </div>
                    </div>
                    <div className="col-2">
                        <div className="card">
                            <div className="card-header w-auto bg-primary brighter text-center text-light">
                                Wartenbereich Standesamt
                            </div>
                            <div className="card-body">
                                {tickets
                                    .filter(ticket => ticket.ticketNr[0].toUpperCase() === 'S')
                                    .slice(0, 9)
                                    .map((ticket, index) => (
                                        <div key={index} className="card w-auto text-bg-primary text-center m-2">
                                            <h2 className="ticketNr">{ticket.ticketNr}</h2>
                                        </div>
                                    ))}
                            </div>
                        </div>

                    </div>
                    <div className="col-2">
                        <div className="card">
                            <div className="card-header w-auto bg-primary brighter text-center text-light">
                                Wartenbereich KFZ-Zulassungsstelle
                            </div>
                            <div className="card-body">
                                {tickets
                                    .filter(ticket => ticket.ticketNr[0].toUpperCase() === 'K')
                                    .slice(0, 9)
                                    .map((ticket, index) => (
                                        <div key={index} className="card w-auto text-bg-primary text-center m-2">
                                            <h2 className="ticketNr">{ticket.ticketNr}</h2>
                                        </div>
                                    ))}
                            </div>
                        </div>

                    </div>
                    <div className="col-6 d-flex align-items-center justify-content-center">
                        <div className="card w-75 h-100 d-flex align-items-center justify-content-center">
                            <div className="card-header w-100 bg-primary brighter text-center text-light">
                                <h1>Sie wurden aufgerufen</h1>
                            </div>
                            <div className="card-body d-flex align-items-center justify-content-center flex-wrap">
                                {inProgressTickets.map((ticket, index) => (
                                    <div key={index}
                                         className="card text-bg-primary d-flex align-items-center justify-content-center m-3"
                                         style={{width: '350px', height: '100px'}}>
                                        <h1 className="ticketNr">{ticket.ticketNr} Raum: {ticket.room}</h1>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>

                </div>
            </div>
            <Footer/>
        </>
    );
};

export default WaitingRoomPage;