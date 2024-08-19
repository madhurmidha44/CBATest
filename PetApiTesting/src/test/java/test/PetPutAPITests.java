package test;

import com.relevantcodes.extentreports.LogStatus;
import io.restassured.RestAssured;
import org.testng.annotations.Test;
import testData.TestData;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/*
I have chosen limited set of validations (in the tests) which won't be impacted by the variable responses given by Pet APIs'.
Validations chosen are the ones which are very important and make sure that APIs' are working correctly.
Test Data is put in separate java class instead of external file as external files are not ideal for CI/CD. As a class,
test data will be part of build artifact which can be easily accessed by CI/CD pipelines.
*/
public class PetPutAPITests extends TestBase{

    @Test(priority = 1)
    public void updateExistingPet() { //'update an existing pet' API
        test.setDescription("Update Pet with unique name & status via 'update an existing pet' Put API, validate the Response details & also grab pet id from the Respone and then validate the updated pet details via 'Find pet by id' API");
        Map<String, String> headers = TestData.postPutAPIHeaders();
        String path = "pet";
        RestAssured.baseURI = endpoint;
        //Updating pet and retrieving pet id from the response
        test.log(LogStatus.INFO, "Updating pet name to '"+TestData.updatedNameOfPet+"' and status to '"+TestData.updatedStatusOfPet+"' via 'update an existing pet' API and validating status code, updated name, updated status and header in the Response. Also retrieving pet id from the response");

        long petId=given().
            headers(headers).
            body(TestData.requestBodyUpdatePet).
        when().
            put(path).
        then().
            statusCode(200). //validating response status code
            body("name", equalTo(TestData.updatedNameOfPet)). //validating updated name of Pet in the response
            body("status", equalTo(TestData.updatedStatusOfPet)). //validating updated status of pet in the response
            header("Content-Type", equalTo("application/json")). //validating response header
            extract().
            path("id"); //get petId for the updated pet from the response
        //System.out.println(petId);
        test.log(LogStatus.INFO, "Response code retrieved from response of 'update an existing pet' API is 200");
        test.log(LogStatus.INFO, "Pet id retrieved from response of 'update an existing pet' API is '"+petId+"'");
        test.log(LogStatus.INFO, "Pet name retrieved from response of 'update an existing pet' API is '"+TestData.updatedNameOfPet+"'");
        test.log(LogStatus.INFO, "Pet status retrieved from response of 'update an existing pet' API is '"+TestData.updatedStatusOfPet+"'");
        test.log(LogStatus.INFO,"Response header content-type has value application/json");

        Map<String,String> getHeaders=TestData.getAPIHeaders();
        path = "pet/" + petId;

        //validate updated pet details (via 'Find Pet by id' API) using above retrieved pet id
        test.log(LogStatus.INFO,"Calling 'Find pet by id' API with retrieved pet id and validating status code, updated name and status of Pet in the Response");

        given().
            headers(getHeaders).
        when().
            get(path).
        then().
            statusCode(200). //validating response status code
            body("name", equalTo(TestData.updatedNameOfPet)). //validating updated name of pet in response
            body("status", equalTo(TestData.updatedStatusOfPet)); //validating updated status of pet in response
        test.log(LogStatus.INFO, "Response code retrieved from response of 'Find Pet by id' API is 200");
        test.log(LogStatus.INFO, "Pet name retrieved from response of 'Find Pet by id' API is '"+TestData.updatedNameOfPet+"'");
        test.log(LogStatus.INFO, "Pet status retrieved from response of 'Find Pet by id' API is '"+TestData.updatedStatusOfPet+"'");

    }

    @Test(priority = 2)
    public void updatePetWithBadRequest() { //'update an existing pet' API
        test.setDescription("Attempt to update pet name & status by giving bad request (without double quotes for the pet name) via 'update an existing pet' API and validate the response details");

        //Adding new Pet
        Map<String, String> headers = TestData.postPutAPIHeaders();
        String path = "pet";
        RestAssured.baseURI = endpoint;

        test.log(LogStatus.INFO, "Trying to update pet name to '"+TestData.updatedNameOfPet+"' and status to '"+TestData.updatedStatusOfPet+"' with bad request of 'update an existing pet' API and validating status code, name and status in the Response");

        given().
            headers(headers).
            body(TestData.badRequestBodyUpdatePet). //Bad request without double quotes for the pet name in the request
        when().
            put(path).
        then().
            statusCode(400). //validating response status code
            body("name", nullValue()). //validating name of Pet is null in the Response
            body("status", nullValue()); //validating status of Pet is null in the Response
        test.log(LogStatus.INFO,"'Update an existing pet' API with bad request gives response code 404 and null value for the name of the pet");

    }

}
