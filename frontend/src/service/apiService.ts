// frontend/src/service/apiService.ts
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/tickets';

export const getTickets = async () => {
    try {
        const response = await axios.get(API_BASE_URL);
        return response.data;
    } catch (error) {
        console.error('Error fetching tickets', error);
    }
};