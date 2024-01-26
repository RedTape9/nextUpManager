
import axios from 'axios';


const API_BASE_URL = 'http://localhost:8080/api/tickets';
const API_BASE_URL_DEPARTMENTS = 'http://localhost:8080/api/departments';

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

export const getAllDepartments = async () => {
    try {
        const response = await axios.get(API_BASE_URL_DEPARTMENTS);
        console.log('Fetched departments:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error fetching departments', error);
    }
};

export const createTicketWithDepartment = async (departmentName: string) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/department/${departmentName}`, {});
        console.log('Created ticket:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error creating ticket', error);
    }
};