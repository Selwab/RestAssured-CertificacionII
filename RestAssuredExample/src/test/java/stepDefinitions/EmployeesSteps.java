package stepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.EmployeeEndpoints;
import entities.Employee;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en_scouse.An;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import utils.Request;

import java.util.List;

import static org.hamcrest.CoreMatchers.not;

public class EmployeesSteps {
    Response response;
    @Given("I perform a GET to the employees endpoint")
    public void getEmployees(){
        response = Request.get(EmployeeEndpoints.GET_EMPLOYEES);
    }

    @And("I verify status code {int} is returned")
    public void verifyStatusCode(int statusCode){
        response.then().assertThat().statusCode(statusCode);
    }

    @And("I verify that the body does not have size {int}")
    public void verifyResponseSize(int size){
        response.then().assertThat().body("size()", not(size));
    }
    // I perform a POST to the create endpoint with the following data

    @And("I perform a POST to the create endpoint with the following data")
    public void postEmployee(DataTable employeeInfo) throws JsonProcessingException {
        //Recuperamos la tabla en uns lista
        List<String> data = employeeInfo.transpose().asList(String.class);
        //Copia de tests, pero obtenemos los datos de la tabal
        Employee employee = new Employee();
        employee.setName(data.get(0));
        employee.setSalary(data.get(1));
        employee.setAge(data.get(2));

        ObjectMapper mapper = new ObjectMapper();

        String payload = mapper.writeValueAsString(employee);
        //enviar endpoint y payload
        response = Request.post(EmployeeEndpoints.POST_EMPLOYEE, payload);
    }

    @And("I verify the following data in the post body response")
    public void verifyEmployeePostResponseData(DataTable employeeInfo){
        //Recuperamos la la tabla como lista
        List<String> data = employeeInfo.transpose().asList(String.class);
        //copiar lo mismo del test, pero adaptarlo a la lista
        response.then().assertThat().body("data.name", Matchers.equalTo(data.get(0)));
        response.then().assertThat().body("data.salary", Matchers.equalTo(data.get(1)));
        response.then().assertThat().body("data.age", Matchers.equalTo(data.get(2)));

    }

    @Given ("I perform a GET to the employees endpoint with the following {string}")
    public void getEmployeeById(String employeeId){
        response = Request.getWithId(EmployeeEndpoints.GET_EMPLOYEE, employeeId);
    }

    @And ("I verify the following data in the getById employee body response")
    public void verifyEmployeeResponseData(DataTable employeeInfo){
        List<String> data = employeeInfo.transpose().asList(String.class);

        response.then().assertThat().body("status", Matchers.equalTo(data.get(0)));
        response.then().assertThat().body("data.id", Matchers.equalTo(Integer.parseInt(data.get(1))));
    }

    @Given ("I perform a DELETE to the delete endpoint with id {string}")
    public void deleteEmployee(String employeeId){
        response = Request.delete(EmployeeEndpoints.DELETE_EMPLOYEE, employeeId);
    }

    @And ("I verify the following data in the delete body response")
    public void verifyEmployeeDeleteResponseData(DataTable employeeInfo){
        List<String> data = employeeInfo.transpose().asList(String.class);
        response.then().assertThat().body("status", Matchers.equalTo(data.get(0)));
        response.then().assertThat().body("data", Matchers.equalTo(data.get(1)));
        response.then().assertThat().body("message", Matchers.equalTo(data.get(2)));
    }

    @Given ("I perform a PUT to update endpoint with id {string} and following data")
    public void updateEmployee(String employeeId, DataTable employeeInfo) throws JsonProcessingException {
        List<String> data = employeeInfo.transpose().asList(String.class);

        Employee employee = new Employee();
        employee.setName(data.get(0));
        employee.setSalary(data.get(1));
        employee.setAge(data.get(2));

        ObjectMapper mapper = new ObjectMapper();

        String payload = mapper.writeValueAsString(employee);

        response = Request.put(EmployeeEndpoints.PUT_EMPLOYEE, employeeId, payload);
    }

    @And ("I verify the following data in the put body response")
    public void verifyEmployeePutResponseData(DataTable employeeInfo){
        List<String> data = employeeInfo.transpose().asList(String.class);
        response.then().assertThat().body("status", Matchers.equalTo(data.get(0)));
        response.then().assertThat().body("data.name", Matchers.equalTo(data.get(1)));
        response.then().assertThat().body("data.salary", Matchers.equalTo(data.get(2)));
        response.then().assertThat().body("data.age", Matchers.equalTo(data.get(3)));
        response.then().assertThat().body("message", Matchers.equalTo(data.get(4)));
    }

}
