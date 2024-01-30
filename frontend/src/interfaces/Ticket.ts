interface Ticket {
    commentByEmployee: string | null;
    createdAt: Date;
    currentStatus: string;
    departmentId: string;
    employeeId: string | null;
    id: string;
    room: string | null;
    statusHistory: Array<{status: string, timestamp: Date}>;
    ticketNr: string;
}

export default Ticket;