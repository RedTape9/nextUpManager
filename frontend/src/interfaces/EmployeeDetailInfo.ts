import EmployeeBasicInfo from "./EmployeeBasicInfo.ts";

interface EmployeeDetailInfo extends EmployeeBasicInfo {
    departmentId: string;
}

export default EmployeeDetailInfo;