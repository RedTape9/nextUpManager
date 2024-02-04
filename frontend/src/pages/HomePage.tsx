import { useState } from 'react';
import NavBar from "../components/NavBar.tsx";
import Footer from "../components/Footer.tsx";
import {Card, Col, Container, Row, Button, Modal, Image, Tooltip, OverlayTrigger} from "react-bootstrap";
import '../styles/colors.css';
import pic from '../assets/nextUpManager_l.png';

interface ModalProps {
    onHide: () => void;
    show: boolean;
}

function MyVerticallyCenteredModal(props: ModalProps) {
    return (
        <Modal
            {...props}
            size="lg"
            aria-labelledby="contained-modal-title-vcenter"
            centered
        >
            <Modal.Header closeButton>
                <Modal.Title id="contained-modal-title-vcenter">
                    Modal heading
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <h4>Centered Modal</h4>
                <p>
                    Cras mattis consectetur purus sit amet fermentum. Cras justo odio,
                    dapibus ac facilisis in, egestas eget quam. Morbi leo risus, porta ac
                    consectetur ac, vestibulum at eros.
                </p>
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={props.onHide}>Close</Button>
            </Modal.Footer>
        </Modal>
    );
}

const renderTooltip = (props: any) => (
    <Tooltip id="button-tooltip" {...props} className="bg-primary">
        Für einen schnellen Überblick über die Funktionen, klicken Sie bitte hier.
    </Tooltip>
);

const Example = ({ variant, text, onClick }: { variant: string, text: string, onClick?: () => void }) => (
    <OverlayTrigger
        placement="bottom"
        delay={{ show: 250, hide: 400 }}
        overlay={renderTooltip}
    >
        <Button variant={variant} className="m-3" onClick={onClick}>{text}</Button>
    </OverlayTrigger>
);

const HomePage = () => {
    const [modalShow, setModalShow] = useState(false);

    return (
        <>
            <NavBar/>
            <Container className="mt-4" style={{ minHeight: '520px' }}>
                <Row>
                    <Col md={6}>
                        <Card className="border-info">
                            <Card.Header className="w-auto bg-primary brighter text-center text-light">
                                <p className="mb-1">Willkommen auf NextUp Manger, der digitalen Ticktverwaltungs - SPA</p>
                            </Card.Header>
                            <Card.Body>
                                <p>Erleben Sie eine neue Art der Ticketverwaltung und Mitarbeiterkoordination.
                                    NextUp Manager bringt Effizienz und Übersichtlichkeit in Ihren Alltag – sei es in der Warteschlange oder am Arbeitsplatz.
                                    Mit nur wenigen Klicks können Sie Tickets buchen, den Status verfolgen und Echtzeit-Updates erhalten.
                                    Für Mitarbeiter bietet die App intuitive Funktionen zur Bearbeitung und Verwaltung von zugewiesenen Tickets.
                                    Mit dem modernen und benutzerfreundlichen Design ist es einfacher denn je,
                                    den Überblick zu behalten und reibungslose Abläufe zu gewährleisten.
                                    Tauchen Sie ein in die Welt von NextUp Manager, wo Warten und Arbeiten nahtlos zusammenfließen.

                                </p>
                                <div className="d-flex">
                                    <Example variant="success" text="Funktionalität" onClick={() => setModalShow(true)} />
                                    <Example variant="primary" text="Tech Stack"/>
                                    <Example variant="warning" text="Source Code" />
                                    <Example variant="danger" text="Über mich"/>
                                </div>
                                <MyVerticallyCenteredModal show={modalShow} onHide={() => setModalShow(false)} />
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={6}>
                        <Image src={pic} fluid/>
                    </Col>
                </Row>
            </Container>
            <Footer/>
        </>
    );
};

export default HomePage;