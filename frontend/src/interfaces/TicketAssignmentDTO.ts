interface TicketAssignmentDTO {
    id: string;
    employeeId: string;
    room: string;
    currentStatus: string;
    timestamp: string;
    ticketNr: string;
    statusHistory: Array<{status: string, timestamp: Date}>;
}

export default TicketAssignmentDTO;