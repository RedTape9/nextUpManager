import {useEffect, useState} from 'react';
import NavBar from "../components/NavBar.tsx";
import Footer from "../components/Footer.tsx";
import {
    Card,
    Col,
    Container,
    Row,
    Button,
    Modal,
    Image,
    Tooltip,
    OverlayTrigger,
    ModalProps,
    TooltipProps, Spinner
} from "react-bootstrap";
import {BoxArrowInUpRight, EnvelopeAtFill, Github, Linkedin} from 'react-bootstrap-icons';
import '../styles/colors.css';
import pic from '../assets/nextUpManager_l.png';
import {getAllDepartments} from "../service/apiService.ts";

function DescriptionModal(props: ModalProps) {
    return (
        <Modal {...props} fullscreen={true} aria-labelledby="contained-modal-title-vcenter">
            <Modal.Header className="bg-primary brighter" closeButton closeVariant="white">
                <Modal.Title id="contained-modal-title-vcenter">
                    <div style={{display: 'flex', alignItems: 'center'}}>
                        <BoxArrowInUpRight color="white" size={32}/>
                        <p className="fs-5 bg-primary text-light m-2">NextUp Manager</p>
                    </div>
                </Modal.Title>
            </Modal.Header>
            <Modal.Body className="grid-example">
                <Container>
                    <Row>
                        <Col xs={12} md={8}>
                            <p className="fs-5 text-primary">Technische Details:</p>
                            <p>Ticket-Management-System: Implementierung eines vollständigen Lebenszyklus-Managements für Tickets, einschließlich Erstellung, Aktualisierung, Statusverfolgung und Löschung (CRUD). Verwendung von DTOs (Data Transfer Objects) für effizientes Datenhandling.</p>
                            <p>Mitarbeiterverwaltung: Entwicklung von Funktionen zur Abrufung und Verwaltung von Mitarbeiterdaten, die Operationen wie das Abrufen von Mitarbeiterinformationen für spezifische Aufgaben ermöglichen.</p>
                            <p>Abteilungsoperationen: Implementierung von Funktionen zur Handhabung abteilungsbezogener Daten, einschließlich Abrufen von Abteilungsdetails und Aktualisieren von Abteilungsnummern.</p>
                            <p>Echtzeit-Updates: Integration von WebSocket mit STOMP für Echtzeit-Nachrichtenübermittlung, die sofortige Updates über Ticketstatus ermöglicht.</p>
                            <p>Unterstützung des clientseitigen Routings: Konfiguration von Spring Boot zur Handhabung von Frontend-Routing-Anfragen für eine Single-Page-Application, um eine reibungslose Navigation der Benutzeroberfläche zu gewährleisten.</p>
                        </Col>

                        <Col xs={12} md={4}>
                            <p className="fs-5 text-primary">Hauptfunktionen:</p>
                            <p>Navigation und Routing: Implementierung einer benutzerfreundlichen Navigation durch Einsatz von React Router für eine Single-Page Application. Die App umfasst Seiten wie das Hauptmenü, den Wartebereich, Mitarbeiterdetails und die Startseite.</p>
                            <p>Dynamische Inhalte und Echtzeit-Updates: Verwendung von WebSockets zur Anzeige von Echtzeit-Updates in der Benutzeroberfläche. Implementierung von Funktionalitäten zum Abrufen und Anzeigen von Tickets, Mitarbeiter- und Abteilungsdaten.</p>
                            <p>Mitarbeiter- und Ticketmanagement: Entwickelt interaktive Schnittstellen, die es ermöglichen, Tickets zu buchen, zu verwalten und Mitarbeiterdetails zu betrachten und zu bearbeiten.</p>
                        </Col>

                    </Row>

                    <Row>
                        <Col xs={12} md={4} style={{ marginTop: '20px' }}>
                            <p className="fs-5 text-primary">Backend-Technologien:</p>
                            <p>Spring Boot (Java)</p>
                            <p>MongoDB</p>
                            <p>WebSocket mit STOMP-Protokoll</p>
                            <p>Lombok</p>
                            <p>Spring Data MongoDB</p>
                        </Col>
                        <Col xs={12} md={4} style={{ marginTop: '20px' }}>
                            <p className="fs-5 text-primary">Frontend-Technologien:</p>
                            <p>React.js</p>
                            <p>Bootstrap for React</p>
                            <p>CSS</p>
                            <p>Axios</p>
                            <p>React Router</p>
                            <p>React Hooks und Zustandsmanagement</p>
                            <p>Axios</p>
                        </Col>
                        <Col xs={12} md={4} style={{ marginTop: '20px' }}>
                            <p className="fs-5 text-primary">Sicherheit und Konfiguration:</p>
                            <p>CORS-Konfiguration</p>
                            <p>WebSocket Sicherheitskonfiguration</p>
                        </Col>
                    </Row>
                </Container>
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={props.onHide}>Zurück</Button>
            </Modal.Footer>
        </Modal>
    );
}

function ContactModal(props: ModalProps) {
    return (
        <Modal {...props} fullscreen={true} aria-labelledby="contained-modal-title-vcenter">
            <Modal.Header className="bg-primary brighter" closeButton closeVariant="white">
                <Modal.Title id="contained-modal-title-vcenter">
                    <div style={{display: 'flex', alignItems: 'center'}}>
                        <BoxArrowInUpRight color="white" size={32}/>
                        <p className="fs-5 bg-primary text-light m-2">Kontakt</p>
                    </div>
                </Modal.Title>
            </Modal.Header>
            <Modal.Body className="grid-example">
                <Container>
                    <Row>
                        <Col xs={12} md={4}>
                            <Image
                                src="https://avatars.githubusercontent.com/u/84500134?v=4"
                                alt="Sergejs avatar"
                                width="192"
                                height="192"
                                className="rounded-circle mb-3"
                            />
                        </Col>
                        <Col xs={12} md={8}>
                            <p className="fs-4 text-primary">Sergej Jaudszims</p>
                            <p className="fs-4 text-primary">Backend Entwickler mit Erfindergeist</p>
                            <p className="fs-6 text-primary">Folgen Sie mir über die sozialen Medien</p>
                        </Col>
                        <Col xs={12} md={4}>
                            <p className="fs-5 text-primary">E-Mail: <a
                                href="mailto:ser.jaudszims@gmail.com"><EnvelopeAtFill color="blue" size={32}></EnvelopeAtFill> </a></p>
                        </Col>
                        <Col xs={12} md={4}>
                            <p className="fs-5 text-primary">GitHub: <a href="https://github.com/RedTape9"><Github
                                color="blue" size={32}/></a></p>
                        </Col>
                        <Col xs={12} md={4}>
                            <p className="fs-5 text-primary">LinkedIn: <a
                                href="https://www.linkedin.com/in/sergej-jaudszims-80672a250/"><Linkedin color="blue" size={32}/></a></p>
                        </Col>
                    </Row>
                </Container>
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={props.onHide}>Zurück</Button>
            </Modal.Footer>
        </Modal>
    );
}


const fetchDepartments = async () => {
    try{
        await getAllDepartments();
    } catch (error) {
        console.error('Error fetching departments', error);
    }
};


const renderTooltip = (props: TooltipProps, tooltipText: string) => (
    <Tooltip id="button-tooltip" {...props} className="bg-primary">
        {tooltipText}
    </Tooltip>
);

const ModalButton = ({variant, text, onClick, tooltipText}: {
    variant: string,
    text: string,
    onClick?: () => void,
    tooltipText: string
}) => (
    <OverlayTrigger
        placement="bottom"
        delay={{show: 250, hide: 400 }}
        overlay={(props: TooltipProps) => renderTooltip(props, tooltipText)}
    >
        <Button variant={variant} className="m-3" onClick={onClick}>{text}</Button>
    </OverlayTrigger>
);


const HomePage = () => {
    const [modalShow, setModalShow] = useState(false);
    const [contactModalShow, setContactModalShow] = useState(false);
    const [imageLoaded, setImageLoaded] = useState(false);

    // for preloading database data
    useEffect(() => {
        fetchDepartments();
    }, []);

    return (
        <>
            {!imageLoaded && (
                <div style={{
                    position: 'fixed',
                    top: 0,
                    left: 0,
                    width: '100%',
                    height: '100%',
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    backgroundColor: 'rgba(0, 0, 0, 0.5)',
                    zIndex: 9999,
                }}>
                    <div className="d-flex align-items-center">
                        <Spinner animation="grow" role="status" variant="primary"/>
                        <span className="ms-3 text-primary">Loading...</span>
                    </div>
                </div>
            )}
            <div style={{filter: !imageLoaded ? 'blur(5px)' : 'none'}}>
                <NavBar/>
                <Container className="mt-4" style={{ minHeight: '520px' }}>
                    <Row>
                        <Col md={6}>
                            <Image src={pic} fluid onLoad={() => setImageLoaded(true)} />
                        </Col>
                        <Col md={6}>
                            <Card className="border-info m-2">
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

                                <ModalButton variant="primary" text="HowTo" onClick={() => setModalShow(true)} tooltipText="Für einen schnellen Überblick über die Funktionen, klicken Sie bitte hier." />

                                <ModalButton variant="success" text="Kontakt" onClick={() => setContactModalShow(true)} tooltipText="Konnte ich Sie überzeugen? Dann kontaktieren Sie mich bitte hier."/>
                                <DescriptionModal show={modalShow} onHide={() => setModalShow(false)} />
                                <ContactModal show={contactModalShow} onHide={() => setContactModalShow(false)} />
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
            <Footer/>
        </div>
    </>
);
}

export default HomePage;