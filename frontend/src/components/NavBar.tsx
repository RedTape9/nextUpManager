import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import '../styles/NavBar.css';
import {Link} from "react-router-dom";
import {BoxArrowInUpRight} from "react-bootstrap-icons";

function NavBar() {
    return (
        <Navbar expand="lg" className="bg-primary brighter-navbar ">
            <Container>
                <Navbar.Brand as={Link} to="/" className="text-white">
                    <BoxArrowInUpRight color="white" size={32}></BoxArrowInUpRight> NextUp Manager
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Link to="/" className={`nav-link ${window.location.pathname === '/' ? 'active' : ''}`}>Home</Link>
                        <Link to="/main-menu" className={`nav-link ${window.location.pathname === '/main-menu' ? 'active' : ''}`}>Hauptmenü</Link>
                        <Link to="/waiting-room" className={`nav-link ${window.location.pathname === '/waiting-room' ? 'active' : ''}`}>Warteraum</Link>

                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default NavBar;