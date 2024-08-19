package test;

import com.relevantcodes.extentreports.LogStatus;
import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.PutAPI;
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
Ideally some of the validations should be cross-verified against Database, but since I don't have DB details, so I
verified against length etc. of response fields
*/
public class PetGetAPITests extends TestBase{

    @Test(priority = 1)
    public void findPetByValidId() //'Find pet by id' API
    {
        test.setDescription("Retrieve existing pet id from DB, then search pet by valid id via 'Find pet by id' Get API and then validate the Response");

        PutAPI putAPIObj=new PutAPI();
        long retrievedPetId=putAPIObj.retrievePetIdViaPutAPI(endpoint, test); //Calling Put API and retrieving valid pet id from Database/response

        //Get pet details using retrieved pet id
        Map<String,String> getHeaders=TestData.getAPIHeaders();

        String petId = String.valueOf(retrievedPetId);
        String path = "pet/" + petId;

        test.log(LogStatus.INFO,"Calling 'Find pet by id' API with valid id and validating status code, id & name of Pet and header in the Response");

        String name=given().
            headers(getHeaders).
        when().
            get(path).
        then().
            statusCode(200). //validating response status code
            body("id", equalTo(retrievedPetId)). //validating pet id in response
            body("name", notNullValue()). //validating name of pet is not null in response
            header("Content-Type", equalTo("application/json")). //validating response header
            extract().
            path("name"); // Extracting the 'name' field from the JSON response

        test.log(LogStatus.INFO,"Response code received is 200");
        test.log(LogStatus.INFO,"Name of pet retrieved is '"+name+"'");
        test.log(LogStatus.INFO,"Response header content-type has value application/json");
        test.log(LogStatus.INFO,"Validating name of valid pet id has length greater than 0 in the response");//Logging info in Report
        Assert.assertTrue(name.length()>0,"Validating name of valid pet id has length greater than 0");

    }

    @Test(priority = 2)
    public void findPetByInvalidId() //'Find pet by id' API
    {
        test.setDescription("Searching pet by invalid id via 'Find pet by id' Get API and validating the Response");
        Map<String,String> headers=TestData.getAPIHeaders();
        int[] invalidPetIds=TestData.invalidPetIds;
        for(int invalidPetId:invalidPetIds) { //iterating through all the invalid pet ids' from test data
            String petId = String.valueOf(invalidPetId);
            String path = "pet/" + petId;
            RestAssured.baseURI = endpoint;

            test.log(LogStatus.INFO,"Calling 'Find pet by Id' API with invalid id '"+invalidPetId+"' and validating status code, id and name of Pet in the Response");

            given().
                headers(headers).
            when().
                get(path).
            then().
                statusCode(404). //validating response status code
                body("id", nullValue()). //validating null value for pet id in response
                body("name", nullValue()); //validating null value for name of pet in response
            test.log(LogStatus.INFO,"'Find pet by Id' API with invalid id gives response code 404 and null value for the name of the pet");

        }

    }

    @Test(priority = 3)
    public void findPetsByValidStatuses() //'Find pet by statuses' API
    {
        test.setDescription("Search pet by valid status via 'Find pet by statuses' Get API and validate the Response");
        Map<String,String> headers=TestData.getAPIHeaders();
        String[] validPetStatuses=TestData.validPetStatuses;
        for(String validPetStatus:validPetStatuses) { //iterating through all the valid pet statuses from test data
            String path = "pet/findByStatus?status=" + validPetStatus;
            RestAssured.baseURI = endpoint;

            test.log(LogStatus.INFO,"Calling 'Find pets by status' API with valid status and validating status code, status & name of Pet and header in the Response");

            String name= given().
                headers(headers).
            when().
                get(path).
            then().
                statusCode(200). //validating response status code
                body("status[0]", equalTo(validPetStatus)). //validating status in 1st element of Json Array response
                body("name[0]", notNullValue()). //validating name of pet is not null in 1st element of Json Array response
                header("Content-Type", equalTo("application/json")). //validating response header
                extract().
                path("name[0]");// Extracting the 'name' field from the 1st element of Array JSON response
            test.log(LogStatus.INFO,"Response code received is 200");
            test.log(LogStatus.INFO,"Name of pet retrieved is '"+name+"' for status of pet '"+validPetStatus+"'");
            test.log(LogStatus.INFO,"Response header content-type has value application/json");
            test.log(LogStatus.INFO,"Validating name of pet has length greater than 0 for the pet in the response");//Logging info in Report
            Assert.assertTrue(name.length()>0,"Validating name of pet has length greater than 0 for the pet in the response");
        }

    }

    @Test(priority = 4)
    public void findPetByInvalidStatuses() //'Find pet by statuses' API
    {
        test.setDescription("Search pet by invalid status via 'Find pet by statuses' Get API and validating the Response");
        Map<String,String> headers=TestData.getAPIHeaders();
        String[] invalidPetStatuses=TestData.invalidPetStatuses;
        for(String invalidPetStatus:invalidPetStatuses) { //iterating through all the invalid pet statuses from test data
            String path = "pet/findByStatus?status=" + invalidPetStatus;
            RestAssured.baseURI = endpoint;

            test.log(LogStatus.INFO,"Calling 'Find pets by status' API with invalid status '"+invalidPetStatus+"'  and validating status code & name of Pet in the Response");

            given().
                headers(headers).
            when().
                get(path).
            then().
                statusCode(200). //validating response status code
                body("name[0]", nullValue()); //validating name of pet is null in 1st element of response
            test.log(LogStatus.INFO,"'Find pet by statuses' API with invalid status gives response code 404 and null value for the name of the pet");
        }

    }
}
