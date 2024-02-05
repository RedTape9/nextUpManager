import { Container, Row, Col } from 'react-bootstrap';

function Footer() {
    return (
        <footer className="bg-primary brighter text-white text-center py-3">
            <Container>
                <Row>
                    <Col>
                        <p>2024 <a href="https://github.com/RedTape9/nextUpManager" target="_blank" rel="noopener noreferrer" className="text-white">RedTape9</a>
                            - Open Source Project</p>
                    </Col>
                </Row>
            </Container>
        </footer>
    );
}

export default Footer;