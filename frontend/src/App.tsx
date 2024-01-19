import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import MainMenu from './pages/MainMenu';
import EmployeePage from './pages/EmployeePage';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/main-menu" element={<MainMenu />} />
                <Route path="/waiting-room" element={<div>Wartezimmer</div>} />
                <Route path="/employee/:id" element={<EmployeePage />} />
            </Routes>
        </Router>
    );
}

export default App;
