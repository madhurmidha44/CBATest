package test;

import com.relevantcodes.extentreports.LogStatus;
import io.restassured.RestAssured;
import org.testng.annotations.Test;
import pages.PutAPI;
import testData.TestData;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.StringContains.containsString;

public class PetDeleteAPITests extends TestBase{

    @Test(priority = 1)
    public void deleteExistingPet() { //'Deletes a pet' API
        test.setDescription("Find existing pet id from DB, then delete the pet using 'Delete Pet' Delete API and then validate that pet id is deleted using 'Find pet by id' API");

        PutAPI putAPIObj=new PutAPI();
        long petId=putAPIObj.retrievePetIdViaPutAPI(endpoint, test); //Calling Put API and retrieving valid pet id from Database/response

        Map<String,String> deleteHeaders=TestData.deleteAPIHeaders();
        String path = "pet/" + petId;
        //Delete pet using retrieved pet id
        test.log(LogStatus.INFO, "Sending 'Delete Pet' API request for the retrieved pet id and validate the status code, message and header in the response");
        given().
            headers(deleteHeaders).
        when().
            delete(path).
        then().
            statusCode(200). //validating response status code
            body("message", containsString(String.valueOf(petId))). //validate message tag has deleted pet id in the response
            header("Content-Type", equalTo("application/json")); //validating response header
        test.log(LogStatus.INFO, "'message' tag in response of 'Delete pet' API contains pet id");
        test.log(LogStatus.INFO, "Response code of 'Delete pet' API is 200");
        test.log(LogStatus.INFO,"Response header content-type has value application/json");

        //validate deleted pet details (via 'Find Pet by id' API) using deleted pet id
        test.log(LogStatus.INFO, "Calling 'Find pet by id' API with deleted pet id and validate the status code, updated name and status of Pet in the Response");
        Map<String,String> getHeaders=TestData.getAPIHeaders();
        given().
            headers(getHeaders).
        when().
            get(path).
        then().
            statusCode(404). //validating response status code is 404 for deleted pet id
            body("name", nullValue()). //validating name of pet is null for deleted pet id
            body("status", nullValue()); //validating status of pet is null for deleted pet id
        test.log(LogStatus.INFO,"''Find Pet by id' API for deleted pet id gives response code 404 and null value for the name and status of the pet");
    }

    @Test(priority = 2)
    public void deleteExistingPetWithApiKeyInRequest() { //'Deletes a pet' API
        test.setDescription("Find existing pet id from DB, then delete the pet using 'Delete Pet' Delete API with alphanumeric api key in request header and then validate that pet id is deleted using 'Find pet by id' API");

        PutAPI putAPIObj=new PutAPI();
        long petId=putAPIObj.retrievePetIdViaPutAPI(endpoint, test); //Calling Put API and retrieving valid pet id from Database/response

        Map<String,String> deleteHeadersWithApiKey=TestData.deleteAPIHeadersWithApiKey();
        String path = "pet/" + petId;
        //Delete pet using retrieved pet id
        test.log(LogStatus.INFO, "Sending 'Delete Pet' API request with api_key header for the retrieved pet id and validate the status code, message and header in the response");
        given().
            headers(deleteHeadersWithApiKey).
        when().
            delete(path).
        then().
            statusCode(200). //validating response status code
            body("message", containsString(String.valueOf(petId))). //validate message tag has deleted pet id in the response
            header("Content-Type", equalTo("application/json")); //validating response header
        test.log(LogStatus.INFO, "'message' tag in response of 'Delete pet' API contains pet id");
        test.log(LogStatus.INFO, "Response code of 'Delete pet' API is 200");
        test.log(LogStatus.INFO,"Response header content-type has value application/json");

        //validate deleted pet details (via 'Find Pet by id' API) using deleted pet id
        test.log(LogStatus.INFO, "Calling 'Find pet by id' API with deleted pet id and validate the status code, updated name and status of Pet in the Response");
        Map<String,String> getHeaders=TestData.getAPIHeaders();
        given().
            headers(getHeaders).
        when().
            get(path).
        then().
            statusCode(404). //validating response status code is 404 for deleted pet id
            body("name", nullValue()). //validating name of pet is null for deleted pet id
            body("status", nullValue()); //validating status of pet is null for deleted pet id
        test.log(LogStatus.INFO,"''Find Pet by id' API for deleted pet id gives response code 404 and null value for the name and status of the pet");
    }

}
