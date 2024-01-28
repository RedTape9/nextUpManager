interface Ticket {
    commentByEmployee: string | null;
    createdAt: string;
    currentStatus: string;
    departmentId: string;
    employeeId: string | null;
    id: string;
    room: string | null;
    statusHistory: Array<{status: string, timestamp: string}>;
    ticketNr: string;
}

export default Ticket;