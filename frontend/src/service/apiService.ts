import axios from 'axios';
import TicketAssignmentDTO from "../interfaces/TicketAssignmentDTO.ts";
import TicketUpdateDTO from "../interfaces/TicketUpdateDTO.ts";
import DepartmentGetForOption from "../interfaces/DepartmentGetForOption.ts";
import TicketCreateDTO from "../interfaces/TicketCreateDTO.ts";

const API_BASE_URL = '/api/tickets';
const API_BASE_URL_DEPARTMENTS = '/api/departments';
const API_BASE_URL_EMPLOYEES = '/api/employees';

export const getAllWaitingTickets = async () => {
    try {
        const response = await axios.get(API_BASE_URL+ '/waiting');
        // console.log('Fetched tickets:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error fetching tickets', error);
    }
};


export const getAllInProgressTickets = async () => {
    try {
        const response = await axios.get(API_BASE_URL + '/in-progress');
        // console.log('Fetched tickets:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error fetching tickets', error);
    }
}

export const getInProgressTicketByEmployeeId = async (employeeId: string): Promise<TicketAssignmentDTO | null> => {
    try {
        const response = await axios.get(`${API_BASE_URL}/in-progress/${employeeId}`);
        return response.data;
    } catch (error: any) {
        if (error.response?.status === 404) {
            return null;
        }
        throw error;
    }
};

export const getAllDepartments = async () => {
    try {
        const response = await axios.get(API_BASE_URL_DEPARTMENTS);
        // console.log('Fetched departments:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error fetching departments', error);
    }
};


export const getDepartmentById = async (id: string): Promise<DepartmentGetForOption | null> => {
    try {
        const response = await axios.get(`${API_BASE_URL_DEPARTMENTS}/${id}`);
        // console.log('Fetched department:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error fetching department', error);
        return null;
    }
};

export const getAllEmployees = async () => {
    try {
        const response = await axios.get(API_BASE_URL_EMPLOYEES);
        //console.log('Fetched employees:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error fetching employees', error);
    }
};

export const getEmployeeById = async (id: string) => {
    try {
        const response = await axios.get(`${API_BASE_URL_EMPLOYEES}/${id}`);
        // console.log('Fetched employee:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error fetching employee', error);
    }
}


export const createTicketWithDepartment = async (ticketCreateDTO: TicketCreateDTO) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/department`, ticketCreateDTO);
        // console.log('Created ticket:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error creating ticket', error);
    }
};

export const assignNextTicketToEmployee = async (employeeId: string): Promise<TicketAssignmentDTO> => {
    const response = await axios.put(`${API_BASE_URL}/next/${employeeId}`);
    return response.data;
};

/*
export const updateTicketStatus = async (ticketId: string, employeeId: string, updateDTO: TicketUpdateDTO) => {
    const response = await axios.put(`${API_BASE_URL}/${ticketId}/status/${employeeId}`, updateDTO);
    return response.data;
};*/

export const updateTicketStatus = async (ticketId: string, updateDTO: TicketUpdateDTO) => {
    const response = await axios.put(`${API_BASE_URL}/${ticketId}`, updateDTO);
    return response.data;
};

export const deleteAllTickets = async () => {
    try {
        const response = await axios.delete(API_BASE_URL + '/deleteAll');
        // console.log('Deleted all tickets:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error deleting all tickets', error);
    }
};