import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import MainMenu from './pages/MainMenu';
import WaitingRoomPage from './pages/WaitingRoomPage'; // Import WaitingRoomPage

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/main-menu" element={<MainMenu />} />
                <Route path="/waiting-room" element={<WaitingRoomPage />} /> {/* Use WaitingRoomPage */}
            </Routes>
        </Router>
    );
}

export default App;