import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import EmployeeDetailInfo from "../interfaces/EmployeeDetailInfo.ts";
import { getEmployeeById } from "../service/apiService.ts";
import NavBar from "../components/NavBar.tsx";

const EmployeePage = () => {
    const { employeeId } = useParams<{ employeeId: string }>();
    const [employee, setEmployee] = useState<EmployeeDetailInfo | null>(null);

    useEffect(() => {
        const fetchEmployee = async () => {
            if (!employeeId) {
                console.error('employee id is undefined');
                return;
            }
            const employeeData = await getEmployeeById(employeeId);
            setEmployee(employeeData);
        };

        fetchEmployee();
    }, [employeeId]);

    return (
        <>
            <NavBar />
            <div className="container mt-3">
                <h1>Mitarbeiter</h1>
                <p>Mitarbeiter/-in ID: {employeeId}</p>
                {employee && (
                    <>
                        <p>Name: {employee.name}</p>
                        <p>Surname: {employee.surname}</p>
                        <p>Department ID: {employee.departmentId}</p>
                        <p>Room: {employee.room}</p>
                    </>
                )}
            </div>
        </>
    );
};

export default EmployeePage;