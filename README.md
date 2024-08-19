# CBATest
CBATestTask3

Jargons meaning:
API Tests = tests that are created in PetDeleteAPITests, PetGetAPITests, PetPostAPITests and PetPutAPITests classes under test package

Introduction:
This is API Automation Testing project to test various Pet APIs' specified at https://petstore.swagger.io/#/pet. Tech stack used is Java and build tool Maven. 'Rest Assured' java library is used to trigger and validate the APIs along with Test NG assertions. Tests are created in Gherkins language (given, when, then format). Test report is generated using 'Extent Reports'.

Pre-requisite:
a) Install Java and Maven. And their respective paths to enviornment variables as shown in the link: https://www.tutorialspoint.com/maven/maven_environment_setup.htm. Make sure that 'java -version' and 'mvn -version' commands are working from the terminal/cmd

b) Install git and make sure that git command is working in the terminal/cmd

Framework and structure of the project:
1- Framework overview: 
Hybrid framework composed of Test Ng, Gherkins BDD flavour (with given, when and then commands inherited from Rest Assured library) Page Object model, Data-Driven, Modular and OOPS concepts such as Inheritence

2- Structure:
a) Source code is available in PetApiTesting->src->test->java folder

b) There is a 'Testbase' class in test package. It contains details such as endpoint of APIs' and various hooks. 'BeforeMethod' hook is run before each test case starts and it initializes the starting test for the extent report so that tests details are documented in the report. 'AfterMethod' hook is run once test case finishes/fail and closes the finished test case in Extent Report. This hook also logs the status details of the finished test in the test report. 'AfterSuite' hook is run once all the tests finish and it closes the Extent Report.

c1) There are PetDeleteAPITests, PetGetAPITests, PetPostAPITests and PetPutAPITests classes that inherit TestBase class. These classes contains test cases with @Test attribute for delete, get, post and put APIs' respectively of pet. For details of API tests under Delete, Get, Post and PUT APIs' of pet, please refer to the link "https://petstore.swagger.io/#/pet". These classes contain positive and negative tests for the corresponding APIs'.
c2) Tests are run under these classes via Gherkins language (given, when, then) with the assistance of Rest assured library. For ex. 'given' statement is used for pre-requisite such as Headers and Body etc of the API request. 'When' statement is used to trigger the API with endpoint and path etc. 'then' statement is used to grab the response details along with putting assertions on the response codes & body etc. Rest other assertions are done using Test NG library. 

d) There is a page object model having class(es) under pages package. Such class(es) enhances modularity, improve readability and reduce maintainability & redundancy of the code. These classes contain specific functionalities which are repeatedly required by the calling test cases by creating their objects and thus, reduced redundancy. For ex. in current project, 'PutAPI' page class is created to grab valid pet id from the database and this pet id can be utilised by various tests under 'API tests classes' to do their operations.

e) There is a 'TestData' class under 'testData' package. This class contains static test data which can be utilised by API tests to do their testing. Some of the test data in this class are request headers of APIs', status of pet and invalid id of pet etc. for Delete, Get, Post and Put APIs'. Reason for having static test data is that we dont need to create objects to grab the data and thus, can save memory. 

f) testng.xml file: This file is placed at the root of the project and it contains details of classes whose tests need to be run. Basically, it contains 'API Tests' classes and will act as a driver for which test cases to be run. Further details of how to use this file are in 'How to trigger tests' section

g) pom.xml file: This file is placed at the root of the project and it contains various maven dependencies and plugins required to get the various libraries in order to write, compile and run the code for API Tests. For ex. Rest Assured, Test NG & Extent Reports etc dependencies to refer their respective libraries and Maven Surefire plugin. These libraries are referred in the source code to accomplish the objectives such as calling API or doing assertions. Further details of how to use this file are in 'How to trigger tests' section

h) Dockerfile: This file is placed at the root of the project. I have not used this file to run tests in Ubuntu docker containers to make execution faster with reduced resources as there was no such requirement mentioned in the task. Therefore, I have kep this file just for info purpose in order to convey that tests can also be run in Docker container. This file basically installs Java and Maven in Ubuntu containers and setup their Home etc path. There is another option of using ready-made Java and Maven images from Docker Hub in this file as well. This file also has the details to run the maven command to execute the tests. Docker commands to trigger this 'Dockerfile' are available in attached 'Docker commands.txt' file at the root of this project. As I said, this is just for info purpose just in acse if there was a requirement to run tests in Docker containers

3. How to trigger tests:
a) In the pom.xml file, I have added Maven surefire plugin. This plugin refers to the testng.xml file of the project in its config. 'testng.xml' file has details of the classes whose methods/tests (with @Test annotation) need to be run. This plugin has the capability to run test cases in parallel based on test classes or suites etc. I have commented parallel config as of now in the plugin but it can be uncommented as well to run the test cases in parallel based on the test classes. While running test cases in parallel, there is a risk of failure in situations such as when one test case is fetching the pet details based on pet id and another test case is updating the pet details based on the same pet id

b) In order to run the tests, Maven surefire plugin needs to be triggered and this can be done via 'mvn test' command which builds the solution and then trigger the tests referred in testng.xml file

c) So to execute the tests, open terminal/cmd and run command: "git clone https://github.com/madhurmidha44/CBATest.git". Then navigate to where '.git' folder of the project is present using "cd CBATest" command. Then switch to 'feature/MadhurMidha-CBATest' branch using command "git checkout feature/MadhurMidha-CBATest". Then navigate to where 'pom.xml' file of the project is present using command "cd PetApiTesting". After that, run command 'mvn test' and this is how execution of API Tests is done, its very simple

4. How to run tests in CI/CD pipeline (for ex. in Jenkins etc):
In the pipeline file/config, insert following commands once API development code is pushed to the repository in say 'dev' or 'test' branch: 

a) "git clone https://github.com/madhurmidha44/CBATest.git" in case 'PetApiTesting' project is not present on the machine. Once this is done, navigate to where '.git' folder of the project is present using "cd CBATest" command and then switch to 'feature/MadhurMidha-CBATest' branch using command "git checkout feature/MadhurMidha-CBATest". 
If project is present on the machine, then navigate where '.git' folder of the project is present using cd command, then switch to 'feature/MadhurMidha-CBATest' branch using command "git checkout feature/MadhurMidha-CBATest" and then run 'git pull' command. 
This will make sure that we have up to date 'PetApiTesting' project and we are on the required feature branch.

b) Navigate to where 'pom.xml' file of the project is present using command "cd PetApiTesting"

c) Run command "mvn test". This will trigger the tests. On passing all the tests, pipeline will move to the next stage (deploying code to the next environment) otherwise it will be stopped

5. Test Results Report:
a) Once execution is finished, HTML test results report with the name format "PetAPIReport_yyyyMMddHHmm.html" is generated in the 'Reports' folder of the project

b) Report contains details of each & every test case that is run along with the information logs of test description, operations, validations and response details

c) Report also contains detailed error logs against the test case in case the test case fails

d) There is already one sample test report that is already present in the 'Reports' folder. This was generated during the last execution run. Just view this report for familiariazing with the report format and status of test cases


