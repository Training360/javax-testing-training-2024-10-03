package employees;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeesService {

    private EmployeesRepository repository;

    private EmployeeMapper employeeMapper;

    public List<EmployeeResource> listEmployees() {
        log.info("List employees");
        return repository.findAllResources();
    }

    public EmployeeResource findEmployeeById(long id) {
        return employeeMapper.toDto(repository.findById(id).orElseThrow(notFoundException(id)));
    }

    public EmployeeResource createEmployee(EmployeeResource command) {
        Optional<Employee> employeeWithSameName = repository.findEmployeeByName(command.name());
        if (employeeWithSameName.isPresent()) {
            throw new EmployeeAlreadyExistsWithSameName("Employee already exists with name: %s, id: %s".formatted(command.name(), employeeWithSameName.get().getId()));
        }

        Employee employee = new Employee(command.name());
        repository.save(employee);
        return employeeMapper.toDto(employee);
    }

    @Transactional
    public EmployeeResource updateEmployee(EmployeeResource command) {
        Optional<Employee> employeeWithSameName = repository.findEmployeeByName(command.name());
        if (employeeWithSameName.isPresent() && employeeWithSameName.get().getId() != command.id()) {
            throw new EmployeeAlreadyExistsWithSameName("Employee already exists with name: %s, id: %s".formatted(command.name(), employeeWithSameName.get().getId()));
        }

        Employee employee = repository.findById(command.id()).orElseThrow(notFoundException(command.id()));
        employee.setName(command.name());
        return employeeMapper.toDto(employee);
    }

    public void deleteEmployee(long id) {
        repository.deleteById(id);
    }

    private Supplier<EmployeeNotFoundException> notFoundException(long id) {
        return () -> new EmployeeNotFoundException("Employee not found with id: %d".formatted(id));
    }

}
