package stepDefinitions;

import Constants.EmployeeEndpoints;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;
import utils.Request;

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
}
