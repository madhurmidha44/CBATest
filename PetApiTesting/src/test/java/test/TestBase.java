package test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestBase {

    public static String reportName="PetAPIReport_"+ new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + ".html";

    public static ExtentReports extent=new ExtentReports(System.getProperty("user.dir") + "/Reports/"+reportName, true);

    public ExtentTest test;

    String endpoint="https://petstore.swagger.io/v2/";

    @BeforeMethod(alwaysRun = true)
    public void beforeTest(Method method){
        test = extent.startTest(method.getName());
    }


    @AfterMethod(alwaysRun = true)
    public void afterTest(ITestResult result){
        if (result.getStatus() == ITestResult.SUCCESS)
        {
            test.log(LogStatus.PASS, "Passed");
        } else {
            test.log(LogStatus.FAIL,result.getThrowable().getCause()+"\n"+
                    result.getThrowable().getMessage());
        }

        extent.endTest(test);
        extent.flush();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        extent.close();
    }

}
