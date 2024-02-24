import { Container, Row, Col } from 'react-bootstrap';

function Footer() {
    return (
        <footer className="bg-white brighter text-primary text-center py-3">
            <Container>
                <Row>
                    <Col>
                        <p>2024 <a href="https://github.com/RedTape9/nextUpManager" target="_blank" rel="noopener noreferrer" className="text-primary">RedTape9</a>
                            &nbsp;- Open Source Project</p>
                        <p className="fs-6">favicon by <a href="https://favicon.io/emoji-favicons/man-surfing" target="_blank" rel="noopener noreferrer" className="text-primary">favicon.io</a></p>
                    </Col>
                </Row>
            </Container>
        </footer>
    );
}

export default Footer;