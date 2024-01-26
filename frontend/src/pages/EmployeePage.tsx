import { useParams} from "react-router-dom";
import Employee from "../interfaces/Employee";
import NavBar from "../components/NavBar.tsx";


const EmployeePage = () => {
    const { employeeId } = useParams<{ employeeId: string }>();
    // const [employee, setEmployee] = useState<Employee | null>(null);


    return (
        <>
            <NavBar />
            <div className="container mt-3">
                <h1>Mitarbeiter</h1>
                <p>Mitarbeiter/-in ID: {employeeId}</p>
            </div>
        </>
    );
};

export default EmployeePage;
