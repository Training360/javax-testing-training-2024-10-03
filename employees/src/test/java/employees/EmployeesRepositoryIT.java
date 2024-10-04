package employees;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeesRepositoryIT {

    @Autowired
    EmployeesRepository employeesRepository;

    @Test
    void findAllResources() {
        // Given
        var employee1 = new Employee("John Doe " + UUID.randomUUID());
        employeesRepository.save(employee1);
        var employee2 = new Employee("Jack Doe " + UUID.randomUUID());
        employeesRepository.save(employee2);
        // When
        var employees = employeesRepository.findAllResources();

        assertThat(employees).
                extracting(EmployeeResource::name)
                .contains(employee1.getName(), employee2.getName());
    }
}
