
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/tickets';

export const getAllWaitingTickets = async () => {
    try {
        const response = await axios.get(API_BASE_URL+ '/waiting');
        console.log('Fetched tickets:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error fetching tickets', error);
    }
};


export const getAllInProgressTickets = async () => {
    try {
        const response = await axios.get(API_BASE_URL + '/in-progress');
        console.log('Fetched tickets:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error fetching tickets', error);
    }
}