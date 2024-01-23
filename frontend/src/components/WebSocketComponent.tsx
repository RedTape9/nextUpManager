import { useState, useEffect } from 'react';
import { connectWebSocket, sendMessage, subscribeToMessages } from '../WebSocketService';

const WebSocketComponent = () => {
    const [message, setMessage] = useState('');
    const [receivedMessage, setReceivedMessage] = useState('');

    useEffect(() => {
        connectWebSocket();
        subscribeToMessages((message) => {
            setReceivedMessage(message);
        });
    }, []);

    const handleSendMessage = () => {
        sendMessage(message);
    };

    return (
        <div>
            <h2>WebSocket Example</h2>
            <div>
                <input type="text" value={message} onChange={(e) => setMessage(e.target.value)} />
                <button onClick={handleSendMessage}>Send Message</button>
            </div>
            <div>
                <strong>Received Message:</strong> {receivedMessage}
            </div>
        </div>
    );
};

export default WebSocketComponent;