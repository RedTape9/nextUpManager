interface TicketAssignmentDTO {
    id: string;
    employeeId: string;
    room: string;
    currentStatus: string;
    ticketNr: string;
    //statusHistory: Array<{status: string, timestamp: Date}>;
}

export default TicketAssignmentDTO;