import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import '../styles/NavBar.css';
import {Link} from "react-router-dom";

function NavBar() {
    return (
        <Navbar expand="lg" className="bg-primary brighter-navbar ">
            <Container>
                <Navbar.Brand href="#home" className="text-light">
                    {/*<img
                        alt=""
                        src="../assets/logo.png"
                        width="30"
                        height="30"
                        className="d-inline-block align-top"
                    />{' '}*/}
                    NextUp Manager
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Link to="/" className="nav-link">Home</Link>
                        <Link to="/main-menu" className="nav-link">Hauptmenü</Link>
                        <Link to="/waiting-room" className="nav-link">Warteraum</Link>

                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default NavBar;