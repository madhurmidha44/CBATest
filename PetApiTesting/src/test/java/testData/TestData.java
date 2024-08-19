package testData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestData {

    // Test data for Get APIs'
    public static int[] invalidPetIds={0};
    public static String[] validPetStatuses={"available", "pending"};
    public static String[] invalidPetStatuses={"unavailable"};
    public static Map<String, String> getAPIHeaders()
    {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        return headers;
    }

    // Test data for Post APIs'
    public static String nameOfAddingPet="sherry"+new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
    public static String statusOfAddingPet="available";
    public static String bodyOfAddingPet="{\"id\": 0,\"category\": {\"id\": 0,\"name\": \"string\"},\"name\":\"" +nameOfAddingPet+"\",\"photoUrls\": [\"string\"],\"tags\": [{\"id\": 0,\"name\": \"string\"}],\"status\": \""+statusOfAddingPet+"\"}";
    public static String bodyWithBadRequest="{\"id\": 0,\"category\": {\"id\": 0,\"name\": \"string\"},\"name\":" +nameOfAddingPet+",\"photoUrls\": [\"string\"],\"tags\": [{\"id\": 0,\"name\": \"string\"}],\"status\": \""+statusOfAddingPet+"\"}";
    public static String[] updatedPetDetails={"tommy"+new SimpleDateFormat("yyyyMMddHHmm").format(new Date()), "available"}; //name and status to be updated for a Pet for a given pet id at index 0
    public static String[] updateUnavailablePet={"0", "sherry", "sold"}; //name and status to be updated for a Pet for a given pet id at index 0
    public static Map<String, String> postPutAPIHeaders()
    {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        return headers;
    }
    public static Map<String, String> postAPIHeadersWithForm()
    {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return headers;
    }

    // Test data for Put APIs'
    public static String updatedNameOfPet="sadie";
    public static String updatedStatusOfPet="pending";
    public static String requestBodyUpdatePet="{\"id\": 0,\"category\": {\"id\": 0,\"name\": \"string\"},\"name\": \""+TestData.updatedNameOfPet+"\",\"photoUrls\": [\"string\"],\"tags\": [{\"id\": 0,\"name\": \"string\"}],\"status\": \""+TestData.updatedStatusOfPet+"\"}";
    public static String badRequestBodyUpdatePet="{\"id\": 0,\"category\": {\"id\": 0,\"name\": \"string\"},\"name\": "+TestData.updatedNameOfPet+",\"photoUrls\": [\"string\"],\"tags\": [{\"id\": 0,\"name\": \"string\"}],\"status\": \""+TestData.updatedStatusOfPet+"\"}";

    //Test data for Delete APIs'
    public static Map<String, String> deleteAPIHeaders()
    {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        return headers;
    }

}
