interface TicketAssignmentDTO {
    employeeId: string;
    room: string;
    currentStatus: string;
    timestamp: string;
    ticketNr: string;
    statusHistory: Array<{status: string, timestamp: string}>;
}

export default TicketAssignmentDTO;