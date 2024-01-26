import EmployeeBasicInfo from "./EmployeeBasicInfo.ts";

interface EmployeeDetailInfo extends EmployeeBasicInfo {
    departmentId: string;
    room: string;
}

export default EmployeeDetailInfo;