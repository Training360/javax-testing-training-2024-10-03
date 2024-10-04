package employees;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@SpringBootTest
//@Import(TestcontainersConfiguration.class)
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
class MockedRepositoryIT {

    @Autowired
    EmployeesController employeesController;

    @MockBean
    EmployeesRepository employeesRepository;

    @Test
    void create() {
        var result = employeesController.createEmployee(new EmployeeResource(null, "John Doe"));

//        assertNotNull(result.id());
        verify(employeesRepository).save(argThat(e -> e.getName().equals("John Doe")));
    }


}
