interface TicketUpdateDTO {
    currentStatus: string;
    commentByEmployee: string;
    statusHistory: Array<{status: string}>;
}

export default TicketUpdateDTO;