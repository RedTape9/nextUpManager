import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import HomePage from './pages/HomePage';
import MainMenu from './pages/MainMenu';
import WaitingRoomPage from './pages/WaitingRoomPage';
import EmployeePage from "./pages/EmployeePage.tsx";


function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/main-menu" element={<MainMenu />} />
                <Route path="/waiting-room" element={<WaitingRoomPage />} />
                <Route path="/employee/:employeeId" element={<EmployeePage/>} />
            </Routes>
        </Router>
    );
}

export default App;