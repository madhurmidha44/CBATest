package test;

import com.relevantcodes.extentreports.LogStatus;
import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.PutAPI;
import testData.TestData;

import java.io.File;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.StringContains.containsString;

/*
I have chosen limited set of validations (in the tests) which won't be impacted by the variable responses given by Pet APIs'.
Validations chosen are the ones which are very important and make sure that APIs' are working correctly.
Test Data is put in separate java class instead of external file as external files are not ideal for CI/CD. As a class,
test data will be part of build artifact which can be easily accessed by CI/CD pipelines.
Ideally some of the validations should be cross-verified against Database, but since I don't have DB details, so I
verified against length etc. of response fields
*/

public class PetPostAPITests extends TestBase {
    @Test(priority = 1)
    public void uploadsAnImage() //'uploads an image' API
    {
        test.setDescription("Find existing pet id from DB, then uploads an image for the retrieved pet id via 'Uploads an image' Post API and then validate the response");

        PutAPI putAPIObj=new PutAPI();
        long retrievedPetId=putAPIObj.retrievePetIdViaPutAPI(endpoint, test); //Calling Put API and retrieving valid pet id from Database/response

        String path = "pet/"+retrievedPetId+"/uploadImage";

        test.log(LogStatus.INFO,"Calling 'Uploads an image' API with image & metadata and validating status code and 'message' tag in the Response");
        File file = new File(TestData.pathPetImage);
        given().
            headers(TestData.postAPIHeadersWithMultipartForm()).
            formParam("additionalMetadata", TestData.additionalMetadata).
            multiPart("file", file).
        when().
            post(path).
        then().
            statusCode(200). //validating response status code
            body("message", containsString("additionalMetadata: "+TestData.additionalMetadata)). //validates response message contains metadata
            body("message", containsString(TestData.petImageName)). //validates response message contains uploaded image name
            header("Content-Type", equalTo("application/json")); //validating response header
        test.log(LogStatus.INFO, "Successfully uploaded pet image '"+TestData.petImageName+"' for pet id "+retrievedPetId);
        test.log(LogStatus.INFO, "'message' tag in the response of API contains 'additionalMetadata: "+TestData.additionalMetadata+"'");
        test.log(LogStatus.INFO,"'message' tag in the response of API contains '"+TestData.petImageName+"'");
    }

    @Test(priority = 2)
    public void addNewPetToStore() //'Add new pet to the store' API
    {
        test.setDescription("Add new Pet with unique name and status via 'Add new pet to the store' Post API and validate the Response");
        Map<String,String> headers= TestData.postPutAPIHeaders();
        String path = "pet";
        RestAssured.baseURI = endpoint;

        test.log(LogStatus.INFO,"Attempting to add unique pet name and pet status '"+TestData.statusOfAddingPet+"' via 'Add new pet to the store' API");
        test.log(LogStatus.INFO,"Calling 'Add new pet to the store' API with valid request and validating status code, name & status of Pet and header in the Response");

        given().
            headers(headers).
            body(TestData.bodyOfAddingPet).
        when().
            post(path).
        then().
            statusCode(200). //validating response status code
            body("name", equalTo(TestData.nameOfAddingPet)). //validating name of Pet in the Response after adding the Pet
            body("status", equalTo(TestData.statusOfAddingPet)). //validating status of Pet in the Response after adding the Pet
            header("Content-Type", equalTo("application/json")); //validating response header
        test.log(LogStatus.INFO, "Response of 'Add new pet to the store' API has status code 200, pet name '"+TestData.nameOfAddingPet+"' and status '"+TestData.statusOfAddingPet+"'");
        test.log(LogStatus.INFO,"Response header content-type has value application/json");
    // After adding a Pet to the store, details should have been verified via DB/API but since API response of API 'Add a new pet to the store' does not provide pet id of added Pet, so I can't validate the added Pet details in DB/API

    }

    @Test(priority = 3)
    public void invalidRequestForAddingPet() //'Add new pet to the store' API
    {
        test.setDescription("Test 'Add new pet to the store' API with invalid request");
        Map<String,String> headers= TestData.postPutAPIHeaders();
        String path = "pet";
        RestAssured.baseURI = endpoint;

        test.log(LogStatus.INFO,"Calling 'Add new pet to the store' Post API with bad request (double quotes missing for pet name) and validating status code, name and status of Pet in the Response");

        given().
            headers(headers).
            body(TestData.bodyWithBadRequest). //Request body without double quotes for pet name
        when().
            post(path).
        then().
            statusCode(400). //validating response status code
            body("name", nullValue()). //validating name of Pet is null in the Response
            body("status", nullValue()); //validating status of Pet is null in the Response
        test.log(LogStatus.INFO, "Response of 'Add new pet to the store' API with invalid request has status code 400, pet name null and status null");
    }

    @Test(priority = 4)
    public void updatePetWithFormData() //'Update a pet in the store with form data' API
    {
        test.setDescription("Find existing pet id from DB, then update Pet name & status via 'Update a pet in the store with form data' Post API for the retrieved pet id and then validate the response. After that call 'Get pet by id' API and validate the updated details");

        PutAPI putAPIObj=new PutAPI();
        long retrievedPetId=putAPIObj.retrievePetIdViaPutAPI(endpoint, test); //Calling Put API and retrieving valid pet id from Database/response

        String[] updatedPetDetails=TestData.updatedPetDetails; //name and status to be updated for a Pet for retrieved pet id
        String path = "pet/"+retrievedPetId;

        test.log(LogStatus.INFO,"Calling 'Update pet in the store with form data' API with valid request and validating status code of Pet and header in the Response");

        given().
            headers(TestData.postAPIHeadersWithForm()).
            formParam("petId", retrievedPetId).
            formParam("name", updatedPetDetails[0]).
            formParam("status", updatedPetDetails[1]).
        when().
            post(path).
        then().
            statusCode(200). //validating response status code
            header("Content-Type", equalTo("application/json")); //validating response header
        test.log(LogStatus.INFO, "'Update pet in the store with form data' API with valid request gives status code 200");
        test.log(LogStatus.INFO, "'Update pet in the store with form data' API updates name to '"+updatedPetDetails[0]+"' and status to '"+updatedPetDetails[1]+"' for pet id '"+retrievedPetId+"'");
        test.log(LogStatus.INFO,"Response header content-type has value application/json");

        test.log(LogStatus.INFO,"Calling 'Find pet by id' API with updated pet id and validating updated name & status of Pet in the Response");

        given().
            headers(TestData.getAPIHeaders()).
        when().
            get(path).
        then().
            statusCode(200). //validating response status code
            body("name", equalTo(updatedPetDetails[0])). //validating updated name of Pet in the response
            body("status", equalTo(updatedPetDetails[1])); //validating updated status of pet in the response
        test.log(LogStatus.INFO, "Response of 'Find pet by id' API has name '"+updatedPetDetails[0]+"' and status '"+updatedPetDetails[1]+"' for pet id '"+retrievedPetId+"'");
    }

    @Test(priority = 5)
    public void updateNonExistentPetWithFormData() { //'Update a pet in the store with form data' API
        test.setDescription("Attempt to update pet via 'Update a pet in the store with form data' Post API for non-existent pet id and validate the response");
        Map<String, String> headers = TestData.postAPIHeadersWithForm();
        String[] updateUnavailablePet = TestData.updateUnavailablePet; //name and status to be updated for a Pet for non-existent pet id at index 0
        String path = "pet/" + updateUnavailablePet[0];
        RestAssured.baseURI = endpoint;

        test.log(LogStatus.INFO, "Calling 'Update pet in the store with form data' API with non-existent pet id "+updateUnavailablePet[0]+" and validating status code & message in the Response");

        given().
            headers(headers).
            formParam("petId", updateUnavailablePet[0]).
            formParam("name", updateUnavailablePet[1]).
            formParam("status", updateUnavailablePet[2]).
        when().
            post(path).
        then().
            statusCode(404). //validating response status code
            body("message", equalTo("not found")); //validating message in the response
        test.log(LogStatus.INFO,"Response for 'Update a pet in the store with form data' API with non-existent pet id gives status code 404 and message 'not found'");
    }




}
