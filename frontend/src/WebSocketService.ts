import io from 'socket.io-client';

let socket;

export const connectWebSocket = () => {
    socket = io('http://localhost:8080');

    socket.on('connect', () => {
        console.log('Connected to WebSocket server');
    });

    socket.on('disconnect', () => {
        console.log('Disconnected from WebSocket server');
    });

    socket.on('connect_error', (err) => {
        console.error('Connection error:', err.message);
    });
};

export const sendMessage = (message) => {
    if (socket && socket.connected) {
        socket.emit('send-message', message);
    } else {
        console.error('Cannot send message. WebSocket is not connected.');
    }
};

export const subscribeToMessages = (callback) => {
    if (socket) {
        socket.on('message', (message) => {
            callback(message);
        });
    } else {
        console.error('Cannot subscribe to messages. WebSocket is not connected.');
    }
};
