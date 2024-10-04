package employees;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeesServiceTest {

    @Mock
    EmployeesRepository employeesRepository;

    @Spy
    EmployeeMapper employeeMapper = new EmployeeMapperImpl();

    @InjectMocks
    EmployeesService employeesService;

    @Test
    void createEmployee() {
        doAnswer(invocation -> {
            var arg = (Employee) invocation.getArgument(0);
            arg.setId(66L);
            return arg;
        }).when(employeesRepository).save(any());

        var result = employeesService.createEmployee(new EmployeeResource(null, "John Doe"));
        assertEquals("John Doe", result.name());
        assertEquals(new EmployeeResource(66L, "John Doe"), result);
    }

    @Test
    void updateEmployee() {
        // Given
        var employee = new Employee(1L, "John Doe");
        when(employeesRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        // When
        var result = employeesService.updateEmployee(new EmployeeResource(1L, "Jack Doe"));
        // Then
        assertEquals("Jack Doe", employee.getName());
        assertEquals(new EmployeeResource(1L, "Jack Doe"), result);
    }
}