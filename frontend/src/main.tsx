// main.tsx
/*if (typeof global === 'undefined') {
    (window as any).global = window;
}*/

//window.process = {} as any;

import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.tsx'
import './index.css'



ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
)
