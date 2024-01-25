import React from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';


const container = document.getElementById('root');
const root = createRoot(container!); // Wenn Sie sich sicher sind, dass 'root' existiert, können Sie das Non-null Assertion-Operator (!) verwenden.

root.render(
    <React.StrictMode>
        <App />
    </React.StrictMode>
);
