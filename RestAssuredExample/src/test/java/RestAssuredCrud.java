import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.mapper.factory.DefaultJackson2ObjectMapperFactory;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.not;

public class RestAssuredCrud {

    @Test
    public void getEmployeesTest(){
        //settear la base url que va a usar para el GET
        RestAssured.baseURI = "https://dummy.restapiexample.com/api/v1";
        //Se almacena la respuesta de la llamada
        Response response = RestAssured
                .when().get("/employees");
        response.then().assertThat().statusCode(200); //verificar el status code
        //que el body no esté vacio
        response.then().assertThat().body("size()", not(0));
        //muestra en la línea de comadno lo que está recibiendo, lo mismo que el postman
        response.then().log().body();
    }

    @Test
    public void getEmployeeTest(){
        RestAssured.baseURI = "https://dummy.restapiexample.com/api/v1";
        Response response = RestAssured
                .given().pathParam("id", "1") //rempalaza 1 por id
                .when().get("/employee/{id}");

        //https://dummy.restapiexample.com/1
        response.then().log().body();
        response.then().assertThat().statusCode(200);
        //data.id=1
        response.then().assertThat().body("data.id", Matchers.equalTo(1));
        //employee_name="Tiger Nixon"
        response.then().assertThat().body("data.employee_name", Matchers.equalTo("Tiger Nixon"));
    }

    @Test
    public void postEmployeeTest() throws JsonProcessingException {
        RestAssured.baseURI = "https://dummy.restapiexample.com/api/v1";
        //Crear un objeto empleado
        Employee employee = new Employee();
        employee.setName("Diego");
        employee.setAge("29");
        employee.setSalary("4500");

        //Convertir el objeto en un json
        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(employee);
        //empleado formato json
        System.out.println(payload);

        //settear el tipo de input
        Response response = RestAssured
                .given().contentType(ContentType.JSON).accept(ContentType.JSON).body(payload)
                .when().post("/create");

        //Mostrar el body en consola
        response.then().log().body();

        //verificar que haya creada el empleado con los mismo datos que hemos enviado
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("data.name", Matchers.equalTo(employee.getName()));
        response.then().assertThat().body("data.salary", Matchers.equalTo(employee.getSalary()));
        response.then().assertThat().body("data.age", Matchers.equalTo(employee.getAge()));
    }

    @Test
    public void putEmployeeTest() throws JsonProcessingException {
        RestAssured.baseURI = "https://dummy.restapiexample.com/api/v1";
        //Creamos un empleado
        Employee employee = new Employee();
        employee.setName("Carlos");
        employee.setAge("25");
        employee.setSalary("4000");

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(employee);

        //Especificamos el tipo del body
        //Enviar parametro como el get
        Response response = RestAssured
                .given().contentType(ContentType.JSON).accept(ContentType.JSON).body(payload)
                .and().pathParam("id", "1")
                .when().put("/update/{id}");
        response.then().log().body();

        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("message", Matchers.equalTo("Successfully! Record has been updated."));
        response.then().assertThat().body("data.name", Matchers.equalTo(employee.getName()));
        response.then().assertThat().body("data.salary", Matchers.equalTo(employee.getSalary()));
        response.then().assertThat().body("data.age", Matchers.equalTo(employee.getAge()));
    }

    @Test
    public void deleteEmployeeTest(){
        RestAssured.baseURI = "https://dummy.restapiexample.com/api/v1";
        //pathParam, enviar parametros en la url
        Response response = RestAssured
                .given().pathParam("id", "3")
                .when().delete("/delete/{id}");
        response.then().log().body();

        response.then().assertThat().statusCode(200);
        //verificar mensaje de succes
        response.then().assertThat().body("message", Matchers.equalTo("Successfully! Record has been deleted"));
        //
        response.then().assertThat().body("data", Matchers.equalTo("3"));
    }

}
