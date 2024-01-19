
import { Link } from 'react-router-dom';

const HomePage = () => {
    return (
        <div>
            <h1>Willkommen beim Ticket-System</h1>
            <Link to="/main-menu">Zum Hauptmenü</Link>
            <Link to="/waiting-room">Zum Wartezimmer</Link>
        </div>
    );
};

export default HomePage;