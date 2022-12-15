# Repository package

Repository are interfaces that express storage domain requirements.

E.g.

    public interface EmployeeRepository extends Repository {
    
        List<Employee> getAllEmployees();
    }
