
import NavBar from "../components/NavBar.tsx";
import Footer from "../components/Footer.tsx";
import {Card, Col, Container, Row} from "react-bootstrap";
import '../styles/colors.css';

const HomePage = () => {
    return (
        <>
            <NavBar/>
            <Container className="mt-4">
                <Row>
                    <Col md={12}>
                        <Card className="border-info">
                            <Card.Header className="w-auto bg-primary brighter text-center text-light">
                                <p className="mb-1">Willkommen</p>
                            </Card.Header>
                            <Card.Body>
                                <p className="text-primary brighter fs-2">NextUp Manager</p>
                                <p className="text-primary brighter fs-2">Ihr digitaler Wartebereich</p>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
            <Footer/>
        </>
    );
};

export default HomePage;