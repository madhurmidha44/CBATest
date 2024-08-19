package pages;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.RestAssured;
import testData.TestData;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class PutAPI {

    public long retrievePetIdViaPutAPI(String endpoint, ExtentTest test)
    {
        Map<String, String> headers = TestData.postPutAPIHeaders();
        String path = "pet";
        RestAssured.baseURI = endpoint;
        //Retrieve valid pet id from the Database/response

        test.log(LogStatus.INFO, "Retrieving valid pet id from Database/response");
        long petId=given().
            headers(headers).
            body(TestData.requestBodyUpdatePet).
        when().
            put(path).
        then().
            statusCode(200). //validating response status code
            body("name", equalTo(TestData.updatedNameOfPet)). //validating updated name of Pet in the response
            body("status", equalTo(TestData.updatedStatusOfPet)). //validating updated status of pet in the response
            extract().
            path("id"); //get petId for the pet from the Database/response
        //System.out.println(petId);
        test.log(LogStatus.INFO, "Valid Pet id retrieved from Database is '"+petId+"'");
        return petId;
    }
}
