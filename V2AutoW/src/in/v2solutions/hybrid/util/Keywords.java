package in.v2solutions.hybrid.util;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.lightbody.bmp.proxy.ProxyServer;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;

import edu.umass.cs.benchlab.har.HarWarning;


public class Keywords extends Constants  {
	/* @HELP
	@class:					Keywords
	@Singleton Class: 	Keywords getKeywordsInstance()
	@ constructor: 		Keywords()
	@methods:		
		OpenBrowser(), Navigate(), NavigateTo(), ResizeBrowser(), Login(), Input(), InputDDTdata(), Click(), SelectValueFromDropDownWithAnchorTags(), SelectValueFromDropDown(), SelectUnselectCheckbox(), 
		GetText(), GetDollarPrice(), GetCountOfAllWebElements(), GetCountOfDisplayedWebElements(), GetCountOfImagesDisplayed(), GetSizeOfImages(), GetPositionOfImages(), Wait(), VerifyText(), VerifyTextDDTdata(), 
		VerifyDollarPrice(), VerifyTitle(), VerifyUrl(), VerifyTotalPrice(), VerifyTotalPriceForDDT(), VerifyListOfStrings(), VerifyCountOfAllWebElements(), VerifyCountOfDisplayedWebElements(), VerifyImageCounts()
		VerifyListOfImageDimensions(), VerifyListOfImagePositions(), HighlightNewWindowOrPopup(), HandlingJSAlerts(), Flash_LoadFlashMovie(), Flash_SetPlaybackQuality(), Flash_SetVolume(), Flash_SeekTo(), 
		Flash_VerifyValue(), Flash_StopVideo(), CloseBrowser(), QuitBrowser().
	@parameter:			Different parameters are passed as per the method declaration
	@notes:					Keyword Drives and Executes the framework interacting with the Master xlsx file
	@returns:				All respective methods have there return types
	@END
	 */	
	
	@SuppressWarnings("rawtypes")
	public static Map getTextOrValues = new HashMap();
	// Generating Dynamic Log File
	public String FILE_NAME = System.setProperty("filename", tsName + tcName + " - " + getCurrentTime());
	public String PATH = System.setProperty("ROOTPATH", rootPath);
	public Logger APP_LOGS = Logger.getLogger("AutomationLog");
	public static long start;
	static Keywords keywords = null;
	public boolean Fail=false; 
	public boolean highlight = false;
	public String failedResult = "";
	public static int count=0;
	public static String randomVal;
	public static String actStartDateAndTime;
	public static String actEndDateAndTime="";
	public static String reqData;
	public static String scriptTableFirstRowData="";
	static Properties props;
	public static Connection connection = null;
	public static Statement statement = null;
	public static String sGPname;
	public static String issueID;
	public String parentWindowID;
	public String actualEmployeeCode;
	public String GTestName=null;
	public String getMethodResponce = null;
	private static String urlGet = "http://ews.staging.tree.com/studentloans/api/CalculatorOffer/GetCalculatorOffers?annualIncome=800000&creditScore=1&refinanceRateTerm=120&totalBalance=100000&refiIntrestRate=3";
	private static String urlPost = "https://ews.staging.tree.com/studentloans/api/OfferSave/Post";
	String StrGet = null;
	String StrPost = null;
	
	private Keywords() throws IOException {
		
	props = new Properties();
	props.load(new FileInputStream(new File(orPath + "OR.properties/")));

		System.out.println(": Initializing keywords");
		APP_LOGS.debug(": Initializing keywords");
		// Initialize properties file
		try {
			// Config
			getConfigDetails();
			// OR
			OR = new Properties();
			fs = new FileInputStream(orPath + "OR.properties/");
			OR.load(fs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void executeKeywords(String testName, Hashtable<String, String> data ) throws Exception
	{
		/* @HELP
		@class:			Keywords
		@method:		executeKeywords()
		@parameter:	String testName, Hashtable<String, String> data
		@notes:			Executes the Keywords as defined in the Master Xslx "Test Steps" Sheet and takes screenshots for any Test Step failure. 
		The test case execution is asserted for any failure in actions and the script execution continues of at all there are some failures in verifications.
		@returns:		No return type
		@END
		 */	
		System.out.println(": =========================================================");
		APP_LOGS.debug(": =========================================================");
		System.out.println(": Executing---" + testName + " Test Case");
		APP_LOGS.debug(": Executing---" + testName + " Test Case");
		String keyword = null;
		String objectKeyFirst = null;
		String objectKeySecond = null;
		String dataColVal = null;
	    GTestName = testName;
	    String links_highlight_true = null;
	    String links_highlight_false =null;
	    String links_on_action = null;
	    
		for (int rNum = 2; rNum <= xls.getRowCount("Test Steps"); rNum++)
		{
			if (testName.equals(xls.getCellData("Test Steps", "TCID", rNum)))
			{
				keyword = xls.getCellData("Test Steps", "Keyword", rNum);
				objectKeyFirst = xls.getCellData("Test Steps", "FirstObject", rNum);
				objectKeySecond = xls.getCellData("Test Steps", "SecondObject", rNum);
				dataColVal = xls.getCellData("Test Steps", "Data", rNum);
				String result = null;
				
				if (keyword.equals("OpenBrowser"))// It is not a keyword, it is a supportive method 
					result = OpenBrowser(dataColVal);

				else if (keyword.equals("Navigate"))
					result = Navigate(dataColVal);
				
				else if (keyword.equals("NavigateTo"))
					result = NavigateTo(dataColVal);
				
				else if (keyword.equals("Login"))
					result = Login();
				
				else if (keyword.equals("InputText"))
					result = InputText(objectKeyFirst, dataColVal);
				
				else if (keyword.equals("InputNumber"))
					result = InputNumber(objectKeyFirst, dataColVal);
				
				else if (keyword.equals("InputDDTdata"))
					result = InputDDTdata(objectKeyFirst, data.get(dataColVal));
				
				else if (keyword.equals("VerifyURLDDTContent"))
					result = VerifyURLDDTContent(data.get(dataColVal));
			
				else if (keyword.equals("VerifyDDTImageExistsByImgSRC"))
					result = VerifyDDTImageExistsByImgSRC(objectKeyFirst,data.get(dataColVal));				
					
				else if (keyword.equals("Click"))
					result = Click(objectKeyFirst);
		
				else if (keyword.equals("ClickOnElementIfPresent"))
					result = ClickOnElementIfPresent(objectKeyFirst);

				else if (keyword.equals("SelectValueFromDropDownWithAnchorTags"))
					result = SelectValueFromDropDownWithAnchorTags(objectKeyFirst,objectKeySecond);
				
				else if (keyword.equals("SelectValueFromDropDown"))
					result = SelectValueFromDropDown(objectKeyFirst,dataColVal);
				
				else if (keyword.equals("SelectUnselectCheckbox"))
					result = SelectUnselectCheckbox(objectKeyFirst,dataColVal);

				else if (keyword.equals("Wait"))
					result = Wait(dataColVal);
				
				else if (keyword.equals("GetText"))
					result = GetText(objectKeyFirst);
				
				else if (keyword.equals("VerifyText"))
					result = VerifyText(objectKeyFirst, objectKeySecond, dataColVal);
				
				else if (keyword.equals("VerifyTotalPrice"))
					result = VerifyTotalPrice(objectKeyFirst, objectKeySecond, dataColVal);
				
				else if (keyword.equals("VerifyTextDDTdata"))
					result = VerifyTextDDTdata(objectKeyFirst, objectKeySecond, data.get(dataColVal));
				
				else if (keyword.equals("VerifyTitle"))
					result = VerifyTitle(actTitle, dataColVal);

				else if (keyword.equals("VerifyUrl"))
					result = VerifyUrl(actUrl, dataColVal);
				
				else if (keyword.equals("HighlightNewWindowOrPopup"))
					result = HighlightNewWindowOrPopup(objectKeyFirst);
				
				else if (keyword.equals("HandlingJSAlerts"))
					result = HandlingJSAlerts();	
				
				else if (keyword.equals("HighlightFrame"))
					result = HighlightFrame(dataColVal);
				
				else if (keyword.equals("OpenDBConnection"))
					result = OpenDBConnection();
				
				else if (keyword.equals("ExecuteAndVerifyDBQuery"))
					result = ExecuteAndVerifyDBQuery(objectKeyFirst, dataColVal);
				
				else if (keyword.equals("ExecuteDBQuery"))
					result = ExecuteDBQuery(objectKeyFirst);

				else if (keyword.equals("CloseDBConnection"))
					result = CloseDBConnection();
				
				else if (keyword.equals("CloseBrowser"))
					result = CloseBrowser();
				
				else if (keyword.equals("QuitBrowser"))
					result = QuitBrowser();
				
				else if (keyword.equals("MouseHover"))
					result = MouseHover(objectKeyFirst);
				
				else if (keyword.equals("MouseHoverAndClick"))
					result = MouseHoverAndClick(objectKeyFirst, objectKeySecond);
				
				else if (keyword.equals("TestCaseEnds"))
					result = TestCaseEnds();
								
				else if(keyword.equals("SwitchToNewWindow"))
					result= SwitchToNewWindow();
				
				else if(keyword.equals("SwitchToParentWindow"))
					result= SwitchToParentWindow();
				
				else if(keyword.equals("ClearTextField"))
					result = clearTextField(objectKeyFirst);
				
				else if(keyword.equals("SwitchToParentWindow"))
					result=SwitchToParentWindow();
				
				else if(keyword.equals("ScrollPageToEnd"))
					result=ScrollPageToEnd(objectKeyFirst);
				
				else if(keyword.equals("VerifyColumnData"))
					result=VerifyColumnData(objectKeyFirst,objectKeySecond,dataColVal);
				
				else if(keyword.equals("VerifyElementPresent"))
					result=VerifyElementPresent(objectKeyFirst,dataColVal);
				
				else if(keyword.equals("GoToHomeLoansSubMenu"))
					result=GoToHomeLoansSubMenu(objectKeyFirst, objectKeySecond);
				
				else if(keyword.equals("SwitchToiFrame"))
					result=switchToiFrame(dataColVal);
				
				else if(keyword.equals("SwitchToDefaultFrameContent"))
					result=switchToDefaultContent();
				
				else if (keyword.equals("VerifyTextContains"))
					result = VerifyTextContains(objectKeyFirst, objectKeySecond, dataColVal);
				
				else if(keyword.equals("VerifyToolTip"))
					result=VerifyToolTip(objectKeyFirst, dataColVal);
				
				else if (keyword.equals("VerifyTextDDTdataContains"))
					result = VerifyTextDDTdataContains(objectKeyFirst, objectKeySecond, data.get(dataColVal));
				
				else if (keyword.equals("VerifyTitleContains"))
					result = VerifyTitleContains(dataColVal);
				
				else if (keyword.equals("OpenMongoDBConnection"))
					result = OpenMongoDBConnection();
				
				else if (keyword.equals("VerifyMongoDBQuery"))
					result = VerifyMongoDBQuery(dataColVal);
				
				else if (keyword.equals("CloseMongoDBConnection"))
					result = CloseMongoDBConnection();
				
				else if (keyword.equals("StartHARReading"))
					result = startHARReading();
				
				else if (keyword.equals("StopHARReading"))
					result = stopHARReading();
				
				else if (keyword.equals("VerifyHARContent"))
					result = VerifyHARContent(dataColVal);
				
				else if(keyword.equals("SignOutIFAlreadyLoggedIn"))
					result=SignOutIFAlreadyLoggedIn(objectKeyFirst, objectKeySecond);
				
				else if (keyword.equals("VerifyXMLContent"))
					result = VerifyXMLContent(dataColVal);
				
				else if (keyword.equals("VerifyCompleteGetResponse"))
					result = VerifyCompleteGetResponse(dataColVal);
				
				else if (keyword.equals("VerifyPOSTRequestContent"))
					result = VerifyPOSTRequestContent(dataColVal);
				
				else if (keyword.equals("MakeGetRequest"))
					result = makeGetRequest(dataColVal);
				
				else if (keyword.equals("MakeGetRequestDDT"))
					result = makeGetRequestDDT(data.get(dataColVal));
				
				else if (keyword.equals("MakePostRequest"))
					result = makePostRequest(dataColVal);
				
				else if (keyword.equals("MakePostRequestJSON"))
					result = makePostRequestJSON(dataColVal);
				
				else if (keyword.equals("DragAndDropByCoordinates"))
					result = dragAndDropByCoordinates(objectKeyFirst,dataColVal);
				
				else if (keyword.equals("DragAndDropByElement"))
					result = dragAndDropByElement(objectKeyFirst, objectKeySecond);
				
				else if(keyword.equals("UploadThroughAutoIT"))
					result = uploadThroughAutoIT();
				
				else if(keyword.equals("CloseTheChildWindow"))
					result = CloseTheChildWindow();
				
				else if(keyword.equals("VerifyTableData"))
					result=VerifyTableData(objectKeyFirst,objectKeySecond,dataColVal);
					
				else if(keyword.equals("VerifyNewlyCreatedProject"))
					result=VerifyNewlyCreatedProject(objectKeyFirst,objectKeySecond,dataColVal);
				
				else if(keyword.equals("ScrollPageToBottom"))
					result=ScrollPageToBottom();
				
				else if(keyword.equals("UploadFile"))
					result=uploadFile(dataColVal);
				
				else if(keyword.equals("SaveFile"))
					result=saveFile();
				
				else if(keyword.equals("VerifyFileDownload"))
					result=VerifyFileDownload(dataColVal);
				
				else if(keyword.equals("DeleteFile"))
					result=DeleteFile(dataColVal);
				
				else if(keyword.equals("SwitchToPdfWindowAndVerifyText"))
					result=SwitchToPdfWindowAndVerifyText(objectKeyFirst,objectKeySecond,dataColVal);
				
				else if(keyword.equals("VerifyElementIsEditable"))
					result=VerifyElementIsEditable(objectKeyFirst);
				
				else if(keyword.equals("VerifyUsersEditables"))
					result=verifyUsersEditables(objectKeyFirst,dataColVal);
				
				else if(keyword.equals("InsertAndCheckUsersBelongings"))
					result=InsertAndCheckUsersBelongings(objectKeyFirst,objectKeySecond);
				
				else if(keyword.equals("VerifyFileIsDownloaded"))
					result=verifyFileIsDownloaded();
				
				else if(keyword.equals("DeleteFilesFromFolder"))
					result=deleteFilesFromFolder(null);
				
				System.out.println(": " + result);
				APP_LOGS.debug(": " + result);

				File scrFile=null;
				 if (keyword.contains("Verify"))
				 {
					if (!result.equals("PASS"))
					{
						// screenshots		
						
						if (highlight == true)
						{ 
							try {
						scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
						scrFileName = testName + "--Failed_AT-" + keyword + "-" + objectKeyFirst + "-" + getCurrentTimeForScreenShot() + ".png";
						links_highlight_true = " , For Error Screenshot please refer to this link  : "+ "<a href="+"'"+scrFileName+"'"+">"+scrFileName+"</a>";

						String filename= SRC_FOLDER2+Forwardslash+failedDataInText;
					    FileWriter fw = new FileWriter(filename,true);
					    String tempStr;
					 	tempStr=testName+"__"+objectKeyFirst+"__"+objectKeyFirst+" Not able to read text. Please check and modify Object Repository or  wait time"+"__"+""+"__"+scrFileName;
					    fw.write(tempStr+"\r\n");
					    fw.close();
							}catch(Exception e){
								System.out.println("-------------------------------Newly added catch");
								Fail = true;
								failedResult = failedResult.concat(result + links_highlight_true + " && " );
							}
						try {
							FileUtils.copyFile(scrFile, new File(screenshotPath + scrFileName));
							System.out.println(": Verification failed. Please refer " + scrFileName);
							APP_LOGS.debug(": Verification failed. Please refer " + scrFileName);
							Fail = true;
							failedResult = failedResult.concat(result + links_highlight_true + " && " );
							} 
						catch (IOException e)
							{
							e.printStackTrace();
							}
						}
					else
						{
						if(keyword.equals("VerifyXMLContent")  )
						{
							APP_LOGS.debug(": Verification failed. VerifyXMLContent" );
							Fail = true;
						}else if(keyword.equals("VerifyPOSTRequestContent")){
							APP_LOGS.debug(": Verification failed. VerifyXMLContent" );
							Fail = true;
						}else if(keyword.equals("VerifyCompleteGetResponse")){
							APP_LOGS.debug(": Verification failed. VerifyXMLContent" );
							Fail = true;
						}
						else
						{
							try{

								highlightElement(returnElementIfPresent(objectKeyFirst));
							}catch(Exception e ){
								Fail = true;
								//failedResult = failedResult.concat(result + Links + " && " );
							}						
						}  
											

						scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
						scrFileName = testName + "--Failed_AT-" + keyword + "-" + objectKeyFirst + "-" + getCurrentTimeForScreenShot() + ".png";
						links_highlight_false = " , For Error Screenshot please refer to this link  : "+ "<a href="+"'"+scrFileName+"'"+">"+scrFileName+"</a>";
						String filename= SRC_FOLDER2+Forwardslash+failedDataInText;
					    FileWriter fw = new FileWriter(filename,true);
					    String tempStr;
					 	tempStr=testName+"__"+objectKeyFirst+"__"+actText+"__"+globalExpText+"__"+scrFileName;					   
					    fw.write(tempStr+"\r\n");
					    fw.close();
						Thread.sleep(500);
						if(keyword.equals("ExecuteAndVerifyDBQuery"))
						{
						}
						else
						{
						unhighlightElement(returnElementIfPresent(objectKeyFirst));						
						}
						
						try {
							FileUtils.copyFile(scrFile, new File(screenshotPath + scrFileName));
							System.out.println(": Verification failed. Please refer " + scrFileName);
							APP_LOGS.debug(": Verification failed. Please refer " + scrFileName);
							Fail = true;
							failedResult = failedResult.concat(result + links_highlight_false + " && " );
							} 
						catch (IOException e)
							{
							e.printStackTrace();
							}
						} 
					}
					String filename= SRC_FOLDER2+Forwardslash+verificationSummaryText;
				   
					
					
					try{
					FileWriter fw = new FileWriter(filename,true);
				    String tempStr=GTestName;
				    if(result.equals("PASS")){
				    	tempStr+=" "+"__"+objectKeyFirst+"__"+keyword+"__"+"Y"+"__"+"-";	
				    }else{
				    	tempStr+=" "+"__"+objectKeyFirst+"__"+keyword+"__"+"-"+"__"+"Y";
				    }
				    count++;
				    
				    fw.write(tempStr+"\r\n");
				    fw.close();
					}
					catch(Exception e){
					System.out.println("Error in count of the verification points..");
						e.printStackTrace();
					}
					}
				 else
				 {
					 if (!result.equals("PASS"))
					 {
						// screenshots
						scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
						scrFileName = testName + "--Failed_AT-" + keyword + "-" + objectKeyFirst + "-" + getCurrentTimeForScreenShot() + ".png";
						links_on_action = " , For Error Screenshot please refer to this link  : "+ "<a href="+"'"+scrFileName+"'"+">"+scrFileName+"</a>";
						String filename= SRC_FOLDER2+Forwardslash+failedDataInText;
					    FileWriter fw = new FileWriter(filename,true);
					    String tempStr;
					    tempStr=testName+"__"+objectKeyFirst+"__"+objectKeyFirst+" Did not appeared after waiting "+waitTime+" seconds. Please check the application status or modify Object Repository, Wait time."+"__"+""+"__"+scrFileName;
					   fw.write(tempStr+"\r\n");
					   fw.close();
						try {
							FileUtils.copyFile(scrFile, new File(screenshotPath + scrFileName));
							System.out.println(": Verification failed. Please refer " + scrFileName);
							APP_LOGS.debug(": Verification failed. Please refer " + scrFileName);
							Fail = true;
							failedResult = failedResult.concat(result + links_on_action + " && " );	
						} 
						catch (IOException e)
						{
							e.printStackTrace();
						}
						System.out.println(": On Action Failed");
						Fail = false;
						QuitBrowser();
						driver = null;
						Assert.assertTrue(false, failedResult);
					}// last if is closing 
				}//first Else is closing. it is of inner IF's
			}//outer If loop is closing
		}//outer For loop is closing 
	}

// **************************************************************************************************Keywords Definitions******************************************************************************************************************************

//***************** 1. OpenBrowser****************//
public String OpenBrowser(String browserType) throws Exception {
		/* @HELP
		@class:			Keywords
		@method:		OpenBrowser ()
		@parameter:	String browserType
		@notes:			Opens Browsers, Sets Timeout parameter and Maximize the Browser
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		 */		
		getConfigDetails();
		int WaitTime;
		NumberFormat nf = NumberFormat.getInstance();
		Number number = nf.parse(waitTime);
		WaitTime = number.intValue();
		failedResult = "";
		System.out.println(": Opening: " + bType + " Browser");
		try {
			
			if(tBedType.equals("DESKTOP")){
				//***************** 1. For Desktop Browsers****************//
				if (bType.equals("Chrome")) {
					System.setProperty("webdriver.chrome.driver", chromedriverPath);
					
					if(GTestName.contains("Segment")){

						System.out.println("Inside segment block of chrome");
						capabilities = new DesiredCapabilities();

						// start the proxy
						server = new ProxyServer(4444);
						server.start();
						// captures the moouse movements and navigations
						server.setCaptureHeaders(true);
						server.setCaptureContent(true);

						// get the Selenium proxy object
						Proxy proxy = server.seleniumProxy();

						capabilities.setCapability(CapabilityType.PROXY, proxy);
						driver = new ChromeDriver(capabilities);
						
					}
					else{
					driver = new ChromeDriver();
					}
					getBrowserVersion();
					APP_LOGS.debug(": Opening: " + bTypeVersion + " Browser");
					driver.manage().timeouts().implicitlyWait(WaitTime, TimeUnit.SECONDS);
					driver.manage().window().maximize();
						
					} else if (bType.equals("Mozilla")) {
						System.setProperty("webdriver.gecko.driver", geckodriverPath);
						
					if(GTestName.contains("Segment")){

						capabilities = new DesiredCapabilities();

						// start the proxy
						server = new ProxyServer(4444);
						server.start();
						// captures the moouse movements and navigations
						server.setCaptureHeaders(true);
						server.setCaptureContent(true);

						// get the Selenium proxy object
						Proxy proxy = server.seleniumProxy();

						capabilities.setCapability(CapabilityType.PROXY, proxy);
						driver = new  FirefoxDriver(capabilities);
						
					}
					else{

						/*ProfilesIni profile = new ProfilesIni();
						FirefoxProfile myprofile = profile.getProfile("FFProfile");
						driver = new FirefoxDriver(myprofile);*/
						driver = new FirefoxDriver();
						
					}
					
					getBrowserVersion();
					//System.out.println("______________ "+getBrowserVersion());
					APP_LOGS.debug(": Opening: " + bTypeVersion + " Browser");
					driver.manage().timeouts().implicitlyWait(WaitTime, TimeUnit.SECONDS);
					driver.manage().window().maximize();
				} else if (bType.equals("Safari")) {
					
					if(GTestName.contains("Segment")){

						capabilities = new DesiredCapabilities();

						// start the proxy
						server = new ProxyServer(4444);
						server.start();
						// captures the moouse movements and navigations
						server.setCaptureHeaders(true);
						server.setCaptureContent(true);

						// get the Selenium proxy object
						Proxy proxy = server.seleniumProxy();

						capabilities.setCapability(CapabilityType.PROXY, proxy);
						driver = new  SafariDriver(capabilities);
						
					}
					else{
						driver = new SafariDriver();
					}
					
					getBrowserVersion();
					APP_LOGS.debug(": Opening: " + bTypeVersion + " Browser");
					driver.manage().timeouts().implicitlyWait(WaitTime, TimeUnit.SECONDS);
					driver.manage().window().maximize();
				} else if (bType.equals("IE")) {
					System.setProperty("webdriver.ie.driver", iedriverPath);
					
					if(GTestName.contains("Segment")){

						capabilities = new DesiredCapabilities();

						// start the proxy
						server = new ProxyServer(4444);
						server.start();
						// captures the moouse movements and navigations
						server.setCaptureHeaders(true);
						server.setCaptureContent(true);

						// get the Selenium proxy object
						Proxy proxy = server.seleniumProxy();

						capabilities.setCapability(CapabilityType.PROXY, proxy);
						driver = new  InternetExplorerDriver(capabilities);
						
					}
					else{
					driver = new InternetExplorerDriver();
					}
					
					getBrowserVersion();
					APP_LOGS.debug(": Opening: " + bTypeVersion + " Browser");
					driver.manage().timeouts().implicitlyWait(WaitTime, TimeUnit.SECONDS);
					driver.manage().window().maximize();
				} else if (bType.equals("Opera")) {
					driver = new OperaDriver();
					
					if(GTestName.contains("Segment")){
					}
					
					getBrowserVersion();
					APP_LOGS.debug(": Opening: " + bTypeVersion + " Browser");
					driver.manage().timeouts().implicitlyWait(WaitTime, TimeUnit.SECONDS);
					driver.manage().window().maximize();
				}
				else if (bType.equals("HtmlUnit")) {
					
					
					if(GTestName.contains("Segment")){
					

						capabilities = new DesiredCapabilities();

						// start the proxy
						server = new ProxyServer(4444);
						server.start();
						// captures the moouse movements and navigations
						server.setCaptureHeaders(true);
						server.setCaptureContent(true);

						// get the Selenium proxy object
						Proxy proxy = server.seleniumProxy();

						capabilities.setCapability(CapabilityType.PROXY, proxy);
						driver = new  HtmlUnitDriver(capabilities);
						
					
					
					}
					/*else if (bType.equals("Edge")) {
						System.setProperty("webdriver.edge.driver", edgedriverPath);
						driver = new EdgeDriver();
						getBrowserVersion();
						APP_LOGS.debug(": Opening: " + bTypeVersion + " Browser");
						driver.manage().timeouts().implicitlyWait(WaitTime, TimeUnit.SECONDS);
						driver.manage().window().maximize();
					}*/
					else{
						driver = new HtmlUnitDriver(true);
					}
					
					APP_LOGS.debug(": Opening: " + bTypeVersion + " Browser");
					driver.manage().timeouts().implicitlyWait(WaitTime, TimeUnit.SECONDS);
				} 
			}else {
				System.out.println(": The Browser Type: " + bType + " is not valid. Please Enter a valid Browser Type");
				return "FAIL - The Browser Type: " + bType + " is not valid. Please Enter a valid Browser Type";
			}
			
			
		} catch (Exception e) {
			return "FAIL - Not able to Open Browser";
		}
		return "PASS";
	}

//***************** 2. Navigate****************//
public String Navigate(String URLKey) {
		/* @HELP
		@class:			Keywords
		@method:		Navigate ()
		@parameter:	String URLKey
		@notes:			Navigate opened Browser to specific URL as metioned in the config details.
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method
		@END
		 */
		getConfigDetails();
		/*System.out.println("deleting cookies");
		driver.manage().deleteAllCookies();*/
		failedResult = "";
		System.out.println(": Navigating to (" + SUTUrl + ") Site");
		APP_LOGS.debug(": Navigating to (" + SUTUrl + ") Site");
		try {
			
			
			if (driver!=null){
				System.out.println(": Driver Handle: "+ driver);
				System.out.println(": Browser is Already Opened and same will be used for this TestScript execution");
				APP_LOGS.debug(": Browser is Already Opened and same will be used for this TestScript execution");
				driver.get(SUTUrl);
				
				if(GTestName.contains("segment")){
				QuitBrowser();
				OpenBrowser(bType);
				}		
			}
			else{
				System.out.println(": Driver Handle: "+ driver);
				System.out.println(": No Opened Browser Available, Opening New one");
				APP_LOGS.debug(": No Opened Browser Available, Opening New one");
				
				
				OpenBrowser(bType);
				driver.get(SUTUrl);
				
			}
		
			/*System.out.println("deleting cookies");
			driver.manage().deleteAllCookies();
		*/
		} catch (Exception e) {
			return "FAIL - Not able to Navigate " + SUTUrl + " Site";
		}
		return "PASS";
	}

//***************** 3. NavigateTo****************//
public String NavigateTo(String URLKey) {
		/* @HELP
		@class:			Keywords
		@method:		NavigateTo ()
		@parameter:	String URLKey
		@notes:			Navigate to specific URL as metioned in the Data Coulmn in "Test Steps" Sheet.
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		 */	
		getConfigDetails();
		
		failedResult = "";
		System.out.println(": Navigating to (" + URLKey + ") Site");
		APP_LOGS.debug(": Navigating to (" + URLKey + ") Site");
		try {
			
			
			if (driver!=null){
				System.out.println(": Driver Handle: "+ driver);
				System.out.println(": Browser is Already Opened and same will be used for this TestScript execution");
				APP_LOGS.debug(": Browser is Already Opened and same will be used for this TestScript execution");
				driver.get(URLKey);
				
				if(GTestName.contains("segment")){
				QuitBrowser();
				OpenBrowser(bType);
				}		
			}
			else{
				System.out.println(": Driver Handle: "+ driver);
				System.out.println(": No Opened Browser Available, Opening New one");
				APP_LOGS.debug(": No Opened Browser Available, Opening New one");
				
				
				OpenBrowser(bType);
				driver.get(URLKey);
				
			}
		} catch (Exception e) {
			System.out.println(": Exception: "+e.getMessage());
			return "FAIL - Not able to Navigate " + URLKey + " Site";
		}
		return "PASS";
	}

//***************** 3. Login****************//
public String Login() {
		/* @HELP
		@class:			Keywords
		@method:		Login ()
		@parameter:	None
		@notes:			Inputs the default login details as mentioned in the "Config  Details" sheet of the master xlsx and performs click action on the login button.
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		*/
		try {
			getConfigDetails();
			System.out.println(": Entering: " + username + " in USERNAME Field");
			APP_LOGS.debug(": Entering: " + username + " in USERNAME Field");
			returnElementIfPresent(GUSER_XPATH).sendKeys(username);
			System.out.println(": PASS");
			APP_LOGS.debug(": PASS");

			System.out.println(": Entering: " + password + " in PASSWORD Field");
			APP_LOGS.debug(": Entering: " + password + " in PASSWORD Field");
			returnElementIfPresent(GPASS_XPATH).sendKeys(password);
			System.out.println(": PASS");
			APP_LOGS.debug(": PASS");

			System.out.println(": Performing Click action on LOGIN");
			APP_LOGS.debug(": Performing Click action on LOGIN");
			returnElementIfPresent(GLOGIN).click();
		} catch (Exception e) {
			APP_LOGS.debug(": FAIL - Not able to Loging with " + username + " : Username and " + password + ": Password");
			return ("FAIL - Not able to Loging with " + username + " : Username and " + password + ": Password");
		}
		return "PASS";
	}

//***************** 4. Input Text****************//
public String InputText(String firstXpathKey, String inputData) throws Exception {
		/* @HELP
		@class:			Keywords
		@method:		Input ()
		@parameter:	String firstXpathKey & String inputData
		@notes:			Inputs the value in any edit box. Value is defined in the master xlsx file and is assigned to "inputData" local variable. We cannot perform a data driven testing using the input keyword.
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		 */	
			System.out.println(": Entering: " + '"' + inputData + '"' + " text in " + firstXpathKey + " Field");
			APP_LOGS.debug(": Entering: " + '"' + inputData + '"' + " text in " + firstXpathKey + " Field");
			try {
				System.out.println("In Input methodddddddddddddd");
				System.out.println("Browserrrrr ------- "+bType);
				if(firstXpathKey.equals("NEWEMAIL")){
					long randomNum=System.currentTimeMillis();
					String randomString=String.valueOf(randomNum);
					inputData=inputData+randomString+"@v2solutions.com";
					returnElementIfPresent(firstXpathKey).sendKeys(inputData);
				}else{
					System.out.println("In elseeeeeeeeeeee");
					if(bType.equals("IE")){
						returnElementIfPresent(firstXpathKey).click();
						System.out.println("Element clicked");
						returnElementIfPresent(firstXpathKey).sendKeys(inputData);
					}else{
					returnElementIfPresent(firstXpathKey).sendKeys(inputData);
					}
					System.out.println("In Input textttt method with -:"+inputData);
				}
			} catch (Exception e) {
				return "FAIL - Not able to enter " + inputData + " in " + firstXpathKey + " Field";
			}	
		return "PASS";
	}
		
//***************** 5. Input Number****************//
public String InputNumber(String firstXpathKey, String inputData) throws Exception {
		/* @HELP
		@class:			Keywords
		@method:		Input ()
		@parameter:	String firstXpathKey & String inputData
		@notes:			Inputs the value in any edit box. Value is defined in the master xlsx file and is assigned to "inputData" local variable. We cannot perform a data driven testing using the input keyword.
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		 */	
	try{
		String regex  = ".*\\d.*";
		if (inputData.matches(regex)) {
			NumberFormat nf = NumberFormat.getInstance();
			Number number = nf.parse(inputData);
			long lnputValue = number.longValue();
			inputData = String.valueOf(lnputValue);

			System.out.println(": Entering: " + '"' + inputData + '"' + " text in " + firstXpathKey + " Field");
			APP_LOGS.debug(": Entering: " + '"' + inputData + '"' + " text in " + firstXpathKey + " Field");				
			returnElementIfPresent(firstXpathKey).clear();
			returnElementIfPresent(firstXpathKey).sendKeys(inputData);					
		} else{
				System.out.println(": Entering: " + '"' + inputData + '"' + " text in " + firstXpathKey + " Field");
				APP_LOGS.debug(": Entering: " + '"' + inputData + '"' + " text in " + firstXpathKey + " Field");
				returnElementIfPresent(firstXpathKey).sendKeys(inputData);				
		}
	}catch (Exception e) {
		return "FAIL - Not able to enter " + inputData + " in " + firstXpathKey + " Field";
	}
	return "PASS";
	}

//***************** 6. InputDDTdata****************//
	public String InputDDTdata(String firstXpathKey, String inputData) throws Exception {
		/* @HELP
		@class:			Keywords
		@method:		InputDDTdata ()
		@parameter:	String firstXpathKey & String inputData
		@notes:			Inputs the value in any edit box. Value will be a multiple test data obtained from the "Test Data" sheet of the master xlsx. Data driven testing is achieved using the keyword InputDDTdata.
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		 */
		String numRegex1 = "[0-9].[0-9]";
		String numRegex2 = "[0-9][0-9].[0-9]";
		if (inputData.matches(numRegex1)) {
			NumberFormat nf = NumberFormat.getInstance();
			Number number = nf.parse(inputData);
			long lnputValue = number.longValue();
			inputData = String.valueOf(lnputValue);
			System.out.println(": Entering: " + '"' + inputData + '"' + " text in " + firstXpathKey + " Field");
			APP_LOGS.debug(": Entering: " + '"' + inputData + '"' + " text in " + firstXpathKey + " Field");
			try {
				returnElementIfPresent(firstXpathKey).clear();
				returnElementIfPresent(firstXpathKey).sendKeys(inputData);
			} catch (Exception e) {
				return "FAIL - Not able to enter " + inputData + " in " + firstXpathKey + " Field";
			}
		} else if (inputData.matches(numRegex2)) {
			NumberFormat nf = NumberFormat.getInstance();
			Number number = nf.parse(inputData);
			long lnputValue = number.longValue();
			inputData = String.valueOf(lnputValue);
			System.out.println(": Entering: " + '"' + inputData + '"' + " text in " + firstXpathKey + " Field");
			APP_LOGS.debug(": Entering: " + '"' + inputData + '"' + " text in " + firstXpathKey + " Field");
			try {
				returnElementIfPresent(firstXpathKey).clear();
				returnElementIfPresent(firstXpathKey).sendKeys(inputData);
			} catch (Exception e) {
				return "FAIL - Not able to enter " + inputData + " in " + firstXpathKey + " Field";
			}
		} else {
			System.out.println(": Entering: " + '"' + inputData + '"' + " text in " + firstXpathKey + " Field");
			APP_LOGS.debug(": Entering: " + '"' + inputData + '"' + " text in " + firstXpathKey + " Field");
			try {
				returnElementIfPresent(firstXpathKey).sendKeys(inputData);
			} catch (Exception e) {
				return "FAIL - Not able to enter " + inputData + " in " + firstXpathKey + " Field";
			}
		}
		return "PASS";
	}
		
//***************** 7. Click****************//
	public String Click(String firstXpathKey) {
		/* @HELP
		@class:			Keywords
		@method:		Click ()
		@parameter:	String firstXpathKey
		@notes:			Performs Click action on link, Hyperlink, selections or buttons of a web page.
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		 */
		System.out.println(": Performing Click action on " + firstXpathKey);
		APP_LOGS.debug(": Performing Click action on " + firstXpathKey);
		try{
		returnElementIfPresent(firstXpathKey).click();
		} catch (Exception e) {
			return "FAIL - Not able to click on -- " + firstXpathKey;
		}
		return "PASS";
	}
		
//***************** 7. Click****************//
		public String ClickOnElementIfPresent(String firstXpathKey) {
			/* @HELP
			@class:			Keywords
			@method:		Click ()
			@parameter:	String firstXpathKey
			@notes:			Performs Click action on link, Hyperlink, selections or buttons of a web page.
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
			 */
			System.out.println(": Performing Click action on " + firstXpathKey+ " Element if it is Present in WebPage");
			APP_LOGS.debug(": Performing Click action on " + firstXpathKey+ " Element if it is Present in WebPage");
			try {
				if (isElementPresent(firstXpathKey)) {
					System.out.println(": "+ firstXpathKey+ "Element is present. Performing Click Action on it.");
					APP_LOGS.debug(": "+ firstXpathKey+ "Element is present. Performing Click Action on it.");
					returnElementIfPresent(firstXpathKey).click();
				} else {
					System.out.println(": "+ firstXpathKey+ "Element is Not present in WebPage");
					APP_LOGS.debug(": "+ firstXpathKey+ "Element is Not present in WebPage");
				}
			} catch (Exception e) {
				return "FAIL - Not able to click on -- " + firstXpathKey;
			}
			return "PASS";
		}
			
	
	
	
//***************************** 8. SelectValueFromDropDownWithAnchorTags*************************************//
public String SelectValueFromDropDownWithAnchorTags(String firstXpathKey, String secondXpathKey) throws Exception {
		/* @HELP
		@class:			Keywords
		@method:		SelectValueFromDropDownWithAnchorTags ()
		@parameter:	String firstXpathKey, String inputData
		@notes:			Click the dropdown and click the value from the List(Which contains anchor tags).
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		 */
		System.out.println(": Selecting : " + secondXpathKey + " from the Dropdown");
		APP_LOGS.debug(": Selecting : " + secondXpathKey + " from the Dropdown");
		try {
			returnElementIfPresent(firstXpathKey).click();
			returnElementIfPresent(secondXpathKey).click();
		} catch (Exception e) {
			return "FAIL - Not able to select " + secondXpathKey + " from the Dropdown";
		}
		return "PASS";
	}
		
//***************************** 9. SelectValueFromDropDown*************************************//
public String SelectValueFromDropDown(String firstXpathKey,String inputData) throws Exception {
			/* @HELP
			@class:			Keywords
			@method:		SelectValueFromDropDown ()
			@parameter:	String firstXpathKey, String inputData
			@notes:			Selects the "inputData" as mentioned in the module xlsx from the DropDown in a webpage.firstXpathKey would be location of the Dropdown on webpage and dataColVal would be visible text of the dropdown.
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
			 */
		System.out.println(": Selecting : " + inputData + " from the Dropdown");
		APP_LOGS.debug(": Selecting : " + inputData + " from the Dropdown");
		try {
			System.out.println(": "+inputData);
			Select sel=new Select(returnElementIfPresent(firstXpathKey));
			sel.selectByVisibleText(inputData);
			} catch (Exception e) {
			return "FAIL - Not able to select " + inputData	+ " from the Dropdown";
		}
		return "PASS";
	}
		
//***************************** 10. SelectUnselectCheckbox*************************************//
	public String SelectUnselectCheckbox(String firstXpathKey, String checkBoxVal) {
		/* @HELP
		@class:			Keywords
		@method:		SelectUnselectCheckbox ()
		@parameter:	String firstXpathKey, String checkBoxVal
		@notes:			Select or Unselect the checkbox of a webpage as per the value of  local variable "chechBoxVal" mentioned in the "Test Steps" sheet in module excel.
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		 */
		System.out.println(": Performing Select Unselect action on " + firstXpathKey);
		APP_LOGS.debug(": Setting " + firstXpathKey + " Checkbox Value As " + checkBoxVal);
		try {
			if (checkBoxVal.equals("TRUE")) {
				if (returnElementIfPresent(firstXpathKey).isSelected()) {
				} else {
					returnElementIfPresent(firstXpathKey).click();
				}
			} else {
				if (returnElementIfPresent(firstXpathKey).isSelected()) {
					returnElementIfPresent(firstXpathKey).click();
				}
			}
		} catch (Exception e) {
			return "FAIL - Not able to Select Unselect Checkbox-- " + firstXpathKey;
		}
		return "PASS";
	}


//***************** 11. Wait****************//
public String Wait(String WaitTime) {
		/* @HELP
		@class:			Keywords
		@method:		Wait ()
		@parameter:	String WaitTime
		@notes:			Wait for a user defined specific time to load the page for ex: 20 seconds. String "WaitTime" captures the value from the module xlsx file.
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		 */	
		try {
			NumberFormat nf = NumberFormat.getInstance();
			Number number = nf.parse(WaitTime);
			long lWaitTime = number.longValue();
			Thread.sleep(lWaitTime * 1000);
			System.out.println(": Waiting for Page to load.");
			APP_LOGS.debug(": Waiting for Page to load.");
		} catch (Exception e) {
			APP_LOGS.debug(": FAIL - Not able to wait for " + WaitTime + " Seconds to load the page");
			return ("FAIL - Not able to wait for " + WaitTime + " Seconds to load the page");
		}
		return "PASS";
	}

//***************** 12. GetText****************//
@SuppressWarnings("unchecked")
public String GetText(String firstXpathKey) throws IOException {
		/* @HELP
		@class:			Keywords
		@method:		GetText ()
		@parameter:	String firstXpathKey
		@notes:			Get the text of the web element of the passed "firstXpathKey" and stores it into a global Hash map "getTextOrValues".
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		 */
		System.out.println(": Getting " + firstXpathKey + " Text from the Page");
		APP_LOGS.debug(": Getting " + firstXpathKey + " Text from the Page");
		try {	
			getTextOrValues.put(firstXpathKey, returnElementIfPresent(firstXpathKey).getText().toLowerCase());
			} catch (Exception e) {
			return "FAIL - Not able to read text from " + firstXpathKey;
		}
		return "PASS";
	}
/*
//----------------------GetTableData------------------------
public String VerifyGetTableData(String firstXpathKey, String secondXpathKey,String expText) throws ParseException {
	
	System.out.println(": Getting " + firstXpathKey + " Text from the Page");
	APP_LOGS.debug(": Getting " + firstXpathKey + " Text from the Page");
	
	String[] inter = expText.split(",");
	for(int i = 0; i< inter.length;i++){
		System.out.println("Values : "+inter[i]);
	}
	
	
	highlight = false;
try
{
	WebElement table =returnElementIfPresent(firstXpathKey);
			//driver.findElement(By.xpath("html/body/div[1]/div/form/table"));

	List<WebElement> th=table.findElements(By.tagName("th"));
	    int col_position=0;
	    for(int i=0;i<th.size();i++){
	    	System.out.println(": Getting " + secondXpathKey + " Text from the Page");
			APP_LOGS.debug(": Getting " + firstXpathKey + " Text from the Page");
	    	
	        if((returnElementIfPresent(secondXpathKey).getText()).equalsIgnoreCase(th.get(i).getText())){
	            col_position=i+1;
	            break;
	        }
	    } 
	List<WebElement> FirstColumns = table.findElements(By.xpath("//tr/td["+col_position+"]"));
	    for(int i = 0;i<inter.length;i++){
	    	WebElement e =  FirstColumns.get(i);
	    	System.out.println("INSIDE FOR TABLE : "+e.getText());
	    	System.out.println(": Getting company name from the table "+e.getText());
	    	APP_LOGS.debug(": Getting company name from the table "+e.getText());
	    	
	    	if(inter[i].contains(e.getText()))
	    	{
	    	System.out.println(": Able to verify the company name from the list");
	    	APP_LOGS.debug(": Able to verify the company name from the list");		
	    	}else
	    	{
	    		System.out.println(": Not able to verify the company name from the list");
	    		APP_LOGS.debug(": Not able to verify the company name from the list");
	    	}
	    	
	    } 
	    
		
	}catch(Exception e){
		highlight = true;
		return "FAIL - Not able to read text from " + firstXpathKey;
	}
return "PASS";
}

*/
//--------------------------ElementPresentInUI---------------------------
public String getColumnData(String firstXpathKey,String secondXpathKey){
	
	//System.out.println("Inside getColumnData initialization");
	String data = "";
	WebElement table =returnElementIfPresent(firstXpathKey);
	List<WebElement> th=table.findElements(By.tagName("th"));
	System.out.println(th.get(1).getText());
	int col_position=0;
	System.out.println(": Getting " + secondXpathKey + " Column Data from the Table");
	APP_LOGS.debug(": Getting " + secondXpathKey + " Column Data from the Table");
    for(int i=0;i<th.size();i++){
    	/*System.out.println(": Getting " + secondXpathKey + " Column Data from the Table");
		APP_LOGS.debug(": Getting " + secondXpathKey + " Column Data from the Table");*/
    	
        if((returnElementIfPresent(secondXpathKey).getText()).equalsIgnoreCase(th.get(i).getText())){
            col_position=i+1;
            break;
        }
    } 
	
    List<WebElement> FirstColumns = table.findElements(By.xpath("//tr/td["+col_position+"]"));
    for (WebElement e : FirstColumns){
    	data = data+e.getText()+",";
    }
    
    
	return data;
}


public String getColumnDataLink(String firstXpathKey,String secondXpathKey){
	
	/*
	 * Need to create a method or mechanism  which calculates unique names from list from String data which have the concatenated string of column Role or we can implement that method in upper method. 
	 * */
	
	String data = "";
	WebElement table =returnElementIfPresent(firstXpathKey);
	
	System.out.println("Table Tag Name : "+table.getTagName());
	//System.out.println(table.findElement(By.tagName("li")));
	
	List<WebElement> ul=table.findElements(By.tagName("th"));
	//System.out.println(ul.get(1).getText());
    int col_position=0; 
    for(int i=0;i<ul.size();i++){
    	System.out.println(": Getting " + secondXpathKey + " Column Data from the Table");
		APP_LOGS.debug(": Getting " + firstXpathKey + " Column Data from the Table");
    	
		/*System.out.println(ul.get(i).getText());
		if(i == 3){
			System.out.println("Third : "+ul.get(3).getText());
			System.out.println();
		}*/
		
        if((returnElementIfPresent(secondXpathKey).getText()).equalsIgnoreCase(ul.get(i).getText())){
            col_position=i+1;
            break;
        }
    } 
	
    List<WebElement> FirstColumns = table.findElements(By.xpath("//tr/td["+col_position+"]"));
    for (WebElement e : FirstColumns){
    	data = data+e.getText()+",";
    }
    
    
	return data;
}


public String VerifyColumnData(String firstXpathKey,String secondXpathKey,String expText){
	
	/* @HELP
	@class:			Keywords
	@method:		VerifyColumnData ()
	@parameter:	String firstXpathKey, String secondXpathKey, String expText
	@notes:			Performs the verification of the table data by getting column data from firstXpathKey and secondXpathKey and verify it against the expText or dataColVal.
	@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
	@END
	 */
	try{
	highlight = false;
	String actText = getColumnData(firstXpathKey,secondXpathKey);
	System.out.println(": Verifying Table Data:");
	APP_LOGS.debug(": Verifying Table Data:");
	
	if (expText.equalsIgnoreCase(actText)){
		
		System.out.println(": Actual is-> " + actText + " AND Expected is-> " + expText);
		APP_LOGS.debug(": Actual is-> " + actText + " AND Expected is->" + expText);
		
	}
	else{
		
		System.out.println("FAIL - Not Able to verify "+actText+" is present or Not-- with " + expText);
		APP_LOGS.debug("FAIL - Not Able to verify "+actText+" is present or Not-- with " + expText);
		return "FAIL - Actual is-> " + actText + " AND Expected is->" + expText;
		
	}
	}
	catch(Exception e)
	{
		highlight = true;
		
		System.out.println("FAIL - Not Able to verify "+actText+" is present or Not-- with " + expText);
		APP_LOGS.debug("FAIL - Not Able to verify "+actText+" is present or Not-- with " + expText);
    
     return "FAIL - Not Able to verify Element is present or Not--" + firstXpathKey;
     }
	return "PASS";
}

public String VerifyTableData(String firstXpathKey,String secondXpathKey,String expText){
	
	//System.out.println("Initialization of keyword");
	highlight = false;
	//String envelopeMessage= OR.getProperty("NO_ENVELOPE_FOUND_MESSAGE");
	WebElement message ;
	
	try{
		//System.out.println("Inside try");
	/*if(driver.findElement(By.xpath("//h2[text()='No envelopes found.']")).isDisplayed()){
		System.out.println("No envelope is displayed.");
		APP_LOGS.debug("No envelope is displayed.");
		//return "No envelope is displayed.";
		System.out.println("In IF");
	}else{*/
		//System.out.println("In ELSE");returnElementIfPresent(firstXpathKey)==null ||
		
		/*if ( driver.findElement(By.xpath("//h2[text()='No envelopes found.']")).isDisplayed())
			{
			System.out.println(": No table displayed with \"No envelopes found.\" Message" );
			APP_LOGS.debug(": No table displayed with \"No envelopes found.\" Message" );
			
		    }*/
		
		//else {
		String actText = getColumnData(firstXpathKey,secondXpathKey);
		//System.out.println("getcolumnData");
		String[] inter = actText.split(",");
		for(int i = 0; i< inter.length;i++){
			System.out.println("Values : "+inter[i]);
		}
		System.out.println("Total "+inter.length+" records found.");
		for(int i = 0; i< inter.length;i++){
		

			if (inter[i].contains(expText)){
				
				System.out.println(": Actual is-> " + inter[i] + " AND Expected is-> " + expText);
				APP_LOGS.debug(": Actual is-> " + inter[i] + " AND Expected is->" + expText);
				
			}
			else{
				highlight = true;
				System.out.println(": Actual is-> " + inter[i] + " AND Expected is-> " + expText);
				return "FAIL - Actual is-> " + inter[i] + " AND Expected is->" + expText;
				}
			}
		//}
	}
	catch(Exception ex){
		highlight = true;
		System.out.println("Unable to match data with table data.");
		APP_LOGS.debug("Unable to match data with table data ");
		return "FAIL - Not able to match data with table data.";
	}
	
	return "PASS";
}


public String InsertAndCheckUsersBelongings(String firstXpathKey,String secondXpathKey){
	
	//System.out.println("Initialization of keyword");
	highlight = false;
	//String envelopeMessage= OR.getProperty("NO_ENVELOPE_FOUND_MESSAGE");
	WebElement message ;
	
	try{
		//System.out.println("Inside try");
	
		//Boolean isPresent = driver.findElements(By.xpath("html/body/div[3]/div/div[4]")).size() != 0;
		String actText = getColumnData(firstXpathKey,secondXpathKey);
		String[] inter = actText.split(",");
		for(int i = 0; i< inter.length;i++){
			System.out.println("Values : "+inter[i]);
		}
		System.out.println("Total "+inter.length+" records found.");
		for(int i = 0,j = 1; i< inter.length;i++,j++){
		
			/*int j = ++i;*/
			WebElement checkBox = driver.findElement(By.xpath("html/body/div[1]/div/div/table/tbody/tr["+j+"]/td[5]/form/label/span"));
			System.out.println(checkBox.getAttribute("class"));
			if(checkBox.getAttribute("class").contains("checked")){
				
				returnElementIfPresent("SETTINGS").click();
				Thread.sleep(5000);
				returnElementIfPresent("USER_SEARCH").sendKeys(inter[i]);
				returnElementIfPresent("SEARCH_BUTTON").click();
				Thread.sleep(2000);
				
				if (isElementPresent(By.xpath("html/body/div[3]/div/div[4]"))){
					
					highlight = true;
					System.out.println(": User does not belongs to Admin -> " + inter[i]+" With error message :\" "+driver.findElement(By.xpath("html/body/div[3]/div/div[4]")).getText()+" \"");
					APP_LOGS.debug(": User does not belongs to Admin -> " + inter[i]+" With error message :\" "+driver.findElement(By.xpath("html/body/div[3]/div/div[4]")).getText()+" \"");
					return ": User does not belongs to Admin -> " + inter[i];
				}
				else{
					System.out.println(": User belongs to Admin -> " + inter[i]);
					APP_LOGS.debug(": User belongs to Admin -> " + inter[i]);
					}
				
			}else{
				System.out.println("User is not Active.");
			}
			
			
			returnElementIfPresent("ADMIN_TAB").click();
			Thread.sleep(2000);
			}
		//}
	}
	catch(Exception ex){
		highlight = true;
		System.out.println("Unable to match data with table data." +ex);
		APP_LOGS.debug("Unable to match data with table data ");
		return "FAIL - Not able to match data with table data.";
	}
	
	return "PASS";
}

/*public static boolean isElementVisible(final By by)
throws InterruptedException {
boolean value = false;
if (driver.findElements(by).size() > 0) {
value = true;
}
return value;
}
*/

public boolean isElementPresent(By by){
try {
driver.findElement(by);
return true;
}
catch (Exception e){
return false;
}
}



public String verifyUsersEditables(String firstXpathKey,String expText){
	
	highlight = false;
	//String envelopeMessage= OR.getProperty("NO_ENVELOPE_FOUND_MESSAGE");
	WebElement message ;
	
	try{
		
		//returnElementIfPresent("LINKOFTHEUSERS").click();
		WebElement listOfTheOptions = returnElementIfPresent(firstXpathKey);
		//System.out.println("::: "+listOfTheOptions.getText());
		//List options = listOfTheOptions.getText();
		String actText = listOfTheOptions.getText();
		actText = actText.trim();
		String[] options = actText.split("\n");
		
		/*for(int i = 0; i< options.length;i++){
			System.out.println("::          :  "+options[i]+" "+i+"*"+options.length);
		}*/
		
		ArrayList<String> actList = new ArrayList<String>(Arrays.asList(options));
		Collections.sort(actList);
		//System.out.println( "___________________----------- : "+Arrays.toString(actList.toArray()));
		
		expText = expText.trim();
		String[] expOptions = expText.split(",");
		
		ArrayList<String> expList = new ArrayList<String>(Arrays.asList(expOptions));
		Collections.sort(expList);
		
		ArrayList<String> temp = new ArrayList<String>();
		temp = actList;
		List<Integer> comparingList = new ArrayList<Integer>();
		
		// adding default values as one
	    for (int a = 0; a < actList.size(); a++) {
	        comparingList.add(0);

	    }
		
	    if(actList.size() == expList.size()){
	    	
			for (int counter = 0; counter < expList.size(); counter++) {
		        if (actList.contains(expList.get(counter))) {
		        	comparingList.set(counter, 1);
		        	/*System.out.println(":All elements are present as Actual is-> " + Arrays.toString(actList.toArray()) + " AND Expected is-> " + Arrays.toString(expList.toArray()));
					APP_LOGS.debug(":All elements are present as Actual is-> " + Arrays.toString(actList.toArray()) + " AND Expected is->" + Arrays.toString(expList.toArray()));*/
		        }/*else{
		        	highlight = true;
					System.out.println("FAIL : as Actual is-> " + Arrays.toString(actList.toArray()) + " AND Expected is-> " + Arrays.toString(expList.toArray()));
					APP_LOGS.debug("FAIL : as Actual is-> " + Arrays.toString(actList.toArray()) + " AND Expected is->" + Arrays.toString(expList.toArray()));
					//return "FAIL - Not able to match data with expected data";
		        }*/
		    }
			
			  //System.out.println(comparingList);
			  
			  if (!comparingList.contains(0)) {
		        	
		        	System.out.println(":All elements are present as Actual is-> " + Arrays.toString(actList.toArray()) + " AND Expected is-> " + Arrays.toString(expList.toArray()));
					APP_LOGS.debug(":All elements are present as Actual is-> " + Arrays.toString(actList.toArray()) + " AND Expected is->" + Arrays.toString(expList.toArray()));
		        }else{
		        	highlight = true;
					System.out.println("FAIL : as Actual is-> " + Arrays.toString(actList.toArray()) + " AND Expected is-> " + Arrays.toString(expList.toArray()));
					APP_LOGS.debug("FAIL : as Actual is-> " + Arrays.toString(actList.toArray()) + " AND Expected is->" + Arrays.toString(expList.toArray()));
					return "FAIL - Not able to match data with expected data";
		        }
		    
	    	
	    }else{
	    	highlight = true;
			System.out.println("FAIL : as Actual is-> " + Arrays.toString(actList.toArray()) + " AND Expected is-> " + Arrays.toString(expList.toArray()));
			APP_LOGS.debug("FAIL : as Actual is-> " + Arrays.toString(actList.toArray()) + " AND Expected is->" + Arrays.toString(expList.toArray()));
			return "FAIL - Not able to match data with expected data";
	    }
		/*if(expList.removeAll(temp)){
			System.out.println(":All elements are present as Actual is-> " + Arrays.toString(actList.toArray()) + " AND Expected is-> " + Arrays.toString(expList.toArray()));
			APP_LOGS.debug(":All elements are present as Actual is-> " + Arrays.toString(actList.toArray()) + " AND Expected is->" + Arrays.toString(expList.toArray()));
		}else{
			highlight = true;
			System.out.println("FAIL : as Actual is-> " + Arrays.toString(actList.toArray()) + " AND Expected is-> " + Arrays.toString(expList.toArray()));
			APP_LOGS.debug("FAIL : as Actual is-> " + Arrays.toString(actList.toArray()) + " AND Expected is->" + Arrays.toString(expList.toArray()));
			return "FAIL - Not able to match data with expected data.";
		}*/
		//Iterator<String> itr=arrayList.iterator();  
		/*while(itr.hasNext()){  
		//System.out.println("Within iterator it is parsing : "+itr.next());  
		 }  
		 */
		
		//System.out.println("Act text : "+actText);
		//String actText = getColumnDataLink(firstXpathKey,secondXpathKey);
		//System.out.println("ActText is here : "+actText);
		/*String[] inter = actText.split(",");
		for(int i = 0; i< inter.length;i++){
			System.out.println("Values : "+inter[i]);
		}
		
		for(int i = 0; i< inter.length;i++){
		

			if (inter[i].contains(expText)){
				
				System.out.println(": Actual is-> " + inter[i] + " AND Expected is-> " + expText);
				APP_LOGS.debug(": Actual is-> " + inter[i] + " AND Expected is->" + expText);
				
			}
			else{
				highlight = true;
				System.out.println(": Actual is-> " + inter[i] + " AND Expected is-> " + expText);
				return "FAIL - Actual is-> " + inter[i] + " AND Expected is->" + expText;
			}
	   }*/
	/* }*/
	}
	catch(Exception ex){
		highlight = true;
		System.out.println("Data is mismatching with given data.");
		APP_LOGS.debug("Data is mismatching with given data.");
		return "FAIL - Data is mismatching with given data.";
	}
	
	return "PASS";
}



public String VerifyNewlyCreatedProject(String firstXpathKey,String secondXpathKey,String expText){
	
	highlight = false;
	String actText = getColumnData(firstXpathKey,secondXpathKey);
	String[] inter = actText.split(",");
	ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(inter));
	for(int i = 0; i< inter.length;i++){
		System.out.println("Values : "+inter[i]);
	}
	
	for(int i = 0; i< inter.length;i++){
	

		if (inter[i].compareTo(expText) == 0){
			
			System.out.println(": Actual is-> " + inter[i] + " AND Expected is-> " + expText);
			APP_LOGS.debug(": Actual is-> " + inter[i] + " AND Expected is->" + expText);
			break;
			
		}
		else if(i == inter.length-1){
			
			if (inter[i].compareTo(expText) == 0){
				
				System.out.println(": Actual is-> " + arrayList + " AND Expected is-> " + expText);
				APP_LOGS.debug(": Actual is-> " + arrayList + " AND Expected is->" + expText);
				break;
				
			}else{
				
			highlight = true;
			System.out.println(": Actual is-> " + inter[i] + " AND Expected is-> " + expText);
			return "FAIL - Actual is-> " + inter[i] + " AND Expected is->" + expText;
			}
		}
		
	
	}
	
	
	return "PASS";
}




public String VerifyElementPresent(String firstXpathKey,String expTEXT) throws ParseException{

	/* @HELP
	@class:			Keywords
	@method:		VerifyElementPresent ()
	@parameter:	String firstXpathKey, String expText
	@notes:			Performs the verification of the table data by getting column data from firstXpathKey and secondXpathKey and verify it against the expText or dataColVal.User can perform negative testing by passing boolean value in dataColVal.
	@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
	@END
	 */
	
	System.out.println(": Checking Element " + firstXpathKey + " Text from the Page");
	APP_LOGS.debug(": Checking Element " + firstXpathKey + " Text from the Page");
	highlight = false;
	
	try
	{
		//System.out.println(": Expeted string is ----> "+expTEXT);
		String sFlag="";
		if(isElementPresent(firstXpathKey)){
			sFlag="TRUE";
			System.out.println(": Yes: Is element Present on Page----> "+sFlag);
			APP_LOGS.debug(": Yes: Is element Present on Page----> "+sFlag);	
		}
		else{
			sFlag="FALSE";
			System.out.println(": No: Is element Present on Page----> "+sFlag);
			APP_LOGS.debug(": No: Is element Present on Page----> "+sFlag);
			}
	if(expTEXT.equals(sFlag))
		{
			System.out.println(": Element is present in the page");
			APP_LOGS.debug(": Element is  " + firstXpathKey + " : Present in the page");
		}else{
			highlight = true;
			System.out.println(": Element is not present in the page");
			APP_LOGS.debug(": Element is  " + firstXpathKey + " : Not Present in the page");
			
		}
	}catch(Exception e)
	{
		highlight = true;
    // System.out.println("inside VerifyElement"+e.getMessage());
     return "FAIL - Not Able to verify Element is present or Not--" + firstXpathKey;
     }
return "PASS";
}
//***************** 13. VerifyText****************//
@SuppressWarnings("unchecked")
public String VerifyText(String firstXpathKey, String secondXpathKey, String expText) throws ParseException {
	/*@HELP
	@class:			Keywords
	@method:		VerifyText ()
	@parameter:	String firstXpathKey, Optional=>String secondXpathKey, Optional=> String expText
	@notes:			Verifies the Actual Text as compared to the Expected Text. Verification can be performed on the same page or on different pages. User can perform two different webelement's text comparision by  passing argument as objectKeySecond.
	@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
	@END
	 */
	
	//System.out.println("----------------------------------------------- : "+returnElementIfPresent(firstXpathKey).isDisplayed());
	//System.out.println("----------------------##------------------------- : "+returnElementIfPresent(firstXpathKey).getText());
		highlight = false;
		System.out.println(": Verifying " + firstXpathKey + " Text on the Page");
		APP_LOGS.debug(": Verifying " + firstXpathKey + " Text on the Page");
		
		String regex  = "[0-9].[0-9]";
		if (expText.matches(regex)) {
			NumberFormat nf = NumberFormat.getInstance();
			Number number = nf.parse(expText);
			long lnputValue = number.longValue();
			expText = String.valueOf(lnputValue);
		}
		if (expText.isEmpty()) {
			getTextOrValues.put(secondXpathKey, returnElementIfPresent(secondXpathKey).getText());
			expText = getTextOrValues.get(secondXpathKey).toString();
		}		
		try {			
			getTextOrValues.put(firstXpathKey, returnElementIfPresent(firstXpathKey).getText());
			actText = getTextOrValues.get(firstXpathKey).toString();		
			actText=actText.trim();
			expText=expText.trim();

			if (actText.compareTo(expText) == 0) {
				System.out.println(": Actual is-> " + actText + " AND Expected is-> " + expText);
				APP_LOGS.debug(": Actual is-> " + actText + " AND Expected is->" + expText);
			} else {
				globalExpText=expText;
				System.out.println(": Actual is-> " + actText + " AND Expected is-> " + expText);
				return "FAIL - Actual is-> " + actText + " AND Expected is->" + expText;
			}
			
		} catch (Exception e) {
			highlight = true;
			return "FAIL - Not able to read text--" + firstXpathKey;
		}
		return "PASS";
	}

//***************** 14. VerifyTextDDTdata****************//
@SuppressWarnings("unchecked")
public String VerifyTextDDTdata(String firstXpathKey, String secondXpathKey, String expText) throws ParseException, InterruptedException {
	/* @HELP
	@class:			Keywords
	@method:		VerifyTextDDTdata ()
	@parameter:	String firstXpathKey, Optional=>String secondXpathKey, Optional=> String expText
	@notes:			Verifies the Actual Text as compared to the Expected Text. Verification can be performed on the same page or on different pages for DDT. 
	@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
	@END
	 */
		highlight = false;
		System.out.println(": Verifying " + firstXpathKey + " Text on the Page");
		APP_LOGS.debug(": Verifying " + firstXpathKey + " Text on the Page");
		
			try {
				getTextOrValues.put(firstXpathKey, returnElementIfPresent(firstXpathKey).getText());
				//actText = getTextOrValues.get(firstXpathKey).toString().toLowerCase();
				actText = getTextOrValues.get(firstXpathKey).toString();
				
				if (actText.compareTo(expText) == 0) {
					System.out.println(": Actual is-> " + actText + " AND Expected is->" + expText);
					APP_LOGS.debug(": Actual is-> " + actText + " AND Expected is->" + expText);
				} else {
					globalExpText=expText;
					System.out.println(": Actual is-> " + actText + " AND Expected is->" + expText);
					return "FAIL - Actual is-> " + actText + " AND Expected is->" + expText;
				}
			} catch (Exception e) {
				    highlight = true;
					return "FAIL - Not able to read text--" + firstXpathKey;
			}
			return "PASS";
	}	
	
//***************** 15. VerifyTotalPrice****************//
	public String VerifyTotalPrice(String firstXpathKey, String secondXpathKey, String getInputData) throws Exception {
		/*
		 * @HELP
		 * @class: Keywords
		 * @method: VerifyTotalPrice ()
		 * @parameter: String firstXpathKey, String secondXpathKey & String getInputData
		 * @notes: Calculates the total product price and stores the value into expText local variable concated with "0" and compares the same with the actual product price. inputData value is used for calculating the total product price.
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method
		 * @END
		 */
		highlight = false;
		System.out.println(": Calculating the Total Price at Run Time & Verifying with Acutal");
		APP_LOGS.debug(": Calculating the Total Price at Run Time & Verifying with Acutal");
		double quantity = 1, totalPrice, actPrice;
		NumberFormat nf = NumberFormat.getInstance();
		Number number = nf.parse(getInputData);
		long lnputValue = number.longValue();
		getInputData = String.valueOf(lnputValue);
		try {
			String qty = getInputData;
			quantity = Double.parseDouble(qty);
			String itemPrice = getTextOrValues.get(secondXpathKey).toString();
			if (itemPrice.contains("$")) {
				itemPrice = itemPrice.replace("$", "");
			}
			if (itemPrice.contains(",")) {
				itemPrice = itemPrice.replace(",", "");
			}
			if (itemPrice.contains(" ")) {
				String str[] = itemPrice.split(" ");
				itemPrice = str[1];
			}
			actPrice = Double.parseDouble(itemPrice);
			totalPrice = actPrice * quantity;
			//System.out.println(": "+totalPrice);

			String expText = String.valueOf(totalPrice).trim();
			actText = returnElementIfPresent(firstXpathKey).getText();

			if (actText.contains("$")) {
				actText = actText.replace("$", "");
			}
			if (actText.contains(",")) {
				actText = actText.replace(",", "");
			}
			if (actText.contains(" ")) {
				String str[] = actText.split(" ");
				actText = str[1];
			}
			if (actText.compareTo(expText) == 0) {
				System.out.println(": Verifying Total Price: Actual is-> $" + actText + " AND Expected is-> $" + expText);
				APP_LOGS.debug(": Actual is-> $" + actText + " AND Expected is-> $" + expText);
			} else {
				System.out.println(": Verifying Total Price: Actual is-> $" + actText + " AND Expected is-> $" + expText);
				return "FAIL - Actual is-> $" + actText + " AND Expected is-> $" + expText;
			}
		} catch (Exception e) {
			highlight = true;
			return "FAIL - Not able to read text or value from --" + firstXpathKey + " OR " + secondXpathKey;
		}
		return "PASS";
	}

//***************** 16. VerifyTitle****************//
public String VerifyTitle(String actTitle, String expTitle) {
	/* @HELP
	@class:			Keywords
	@method:		VerifyTitle ()
	@parameter:	String actTitle & String expTitle
	@notes:			Verifies the Actual Web Page Title as compared to the Expected Web Page title. Verification is performed on the same Web page. 
	@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
	@END
	 */
	
		highlight = false;
		System.out.println(": Verifying Page Title");
		APP_LOGS.debug(": Verifying Page Title");
		try {
			expTitle = expTitle.replace("_", ",");
			actTitle = driver.getTitle();
			if (actTitle.compareTo(expTitle) == 0) {
				System.out.println(": Actual is-> " + actTitle + " AND Expected is->" + expTitle);
				APP_LOGS.debug(": Actual is-> " + actTitle + " AND Expected is->" + expTitle);
			} else {
				highlight = true;
				System.out.println(": Actual is-> " + actTitle + " AND Expected is->" + expTitle);
				return "FAIL - Actual is-> " + actTitle + " AND Expected is->" + expTitle;
			}
		} catch (Exception e) {
			highlight = true;
			return "FAIL - Not able to get title";
		}
		return "PASS";
	}

//***************** 17. VerifyUrl****************//
public String VerifyUrl(String actUrl, String expUrl) {
		/* @HELP
		@class:			Keywords
		@method:		VerifyUrl ()
		@parameter:	String actUrl, String expUrl
		@notes:			Verifies the Actual Web Page URL as compared to the Expected Web Page URL. Verification is performed on the same Web page. 
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		 */
		highlight = false;
		System.out.println(": Verifying Current URL");
		APP_LOGS.debug(": Verifying Current URL");
		try {
			actUrl = driver.getCurrentUrl();
			if (actUrl.compareTo(expUrl) == 0) {
				System.out.println(": Actual is-> " + actUrl + " AND Expected is->" + expUrl);
				APP_LOGS.debug(": Actual is-> " + actUrl + " AND Expected is->" + expUrl);
			} else {
				highlight = true;
				System.out.println(": Actual is-> " + actUrl + " AND Expected is->" + expUrl);
				return "FAIL - Actual is-> " + actUrl + " AND Expected is->" + expUrl;
			}
		} catch (Exception e) {
			highlight = true;
			return "FAIL - Not able to get URL";
		}
		return "PASS";
	}
		

//***************** 18. HighlightNewWindowOrPopup****************//
public String HighlightNewWindowOrPopup(String firstParam) throws Exception {
		/* @HELP
		@class:			Keywords
		@method:		HighlightNewWindowOrPopup ()
		@parameter:	None
		@notes:			WebDriver Object focus should move to New Window or Popup
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		 */			
		System.out.println(": Switching to NewWindow or Popup");
		APP_LOGS.debug(": Switching to NewWindow or Popup");
		try {
			winIDs = driver.getWindowHandles();
			it = winIDs.iterator();
			
			if (firstParam.equals("MainWindow")) {
				firstParam = it.next();
			} else {				
				firstParam = it.next();
				firstParam = it.next();
			}
			driver.switchTo().window(firstParam);
		} catch (Exception e) {
			return "FAIL - Not able to Switch to NewWindow or Popup";
		}
		return "PASS";
	}
	
//***************** 19. HandlingJSAlerts****************//
public String HandlingJSAlerts() throws Exception {
		/* @HELP
		@class:			Keywords
		@method:		HandlingJSAlerts ()
		@parameter:	None
		@notes:			WebDriver Object focus should move to JavaScript Alerts
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		 */
		System.out.println(": Handling Java Scripts Alerts");
		APP_LOGS.debug(": Handling Java Scripts Alerts");
		try {
			Alert alt = driver.switchTo().alert();
			alt.accept();
			// alt.dismiss();
		} catch (Exception e) {
			return "FAIL - Not able to Switch to NewWindow or Popup";
		}
		return "PASS";
	}

//***************** 20. HighlightFrame****************//
public String HighlightFrame(String inputData) throws Exception {
		/* @HELP
		@class:			Keywords
		@method:		HighlightFrame ()
		@parameter:	String inputData
		@notes:			WebDriver Object focus should move to Frame
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		 */
		System.out.println(": Highlighting Frame");
		APP_LOGS.debug(": Highlighting Frame");
		NumberFormat nf = NumberFormat.getInstance();
		Number number = nf.parse(inputData);
		int frameID = number.intValue();
		try {
			driver.switchTo().frame(frameID);
		} catch (Exception e) {
			return "FAIL - Not able to Highlight Frame";
		}
		return "PASS";
	}

//***************** 21. OpenDBConnection****************//
public String OpenDBConnection() {
	/* @HELP
	@class:			Keywords
	@method:		OpenDBConnection ()
	@parameter:	None
	@notes:			Connect to the database mentioned in Config details.
	@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
	@END
	 */	
	System.out.println(": Connecting to the "+databaseType+" database");
	APP_LOGS.debug(": Connecting to the "+databaseType+" database");
	try {
		if(databaseType.equals("My SQL")){
			Class.forName("com.mysql.jdbc.Driver");			
		}else if(databaseType.equals("Oracle")){
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}else if(databaseType.equals("MS SQL Server")){
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		}
	} catch (ClassNotFoundException e) {
		System.out.println(": "+e.getMessage());
	}
	try {
		connection = DriverManager.getConnection(dbConnection, dbUsername, dbPassword);
	} catch (SQLException e) {
		System.out.println(": "+e.getMessage());
		return "FAIL - Not able to connect to the "+databaseType;
	}
	return "PASS";
}

//***************** 22. ExecuteAndVerifyDBQuery****************//
public String ExecuteAndVerifyDBQuery(String firstXpathKey, String expData) throws SQLException {
	/* @HELP
	@class:			Keywords
	@method:		ExecuteAndVerifyDBQuery ()
	@parameter:	String firstXpathKey
	@notes:			Fetches the data from database and verifies the data with expected.
	@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
	@END
	 */	
	System.out.println(": Fetching the data from "+databaseType+" database");
	APP_LOGS.debug(": Fetching the data from "+databaseType+" database");
	String selectQuery = OR.getProperty(firstXpathKey);
	ArrayList<String> actList=new ArrayList<String>();
	ArrayList<String> expList=new ArrayList<String>();
	expList.add(expData);
	try {
		statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(selectQuery);
		ResultSetMetaData rsMetaData = rs.getMetaData();
	    int tableColumnCount = rsMetaData.getColumnCount();
		while (rs.next()) {
			for(int i=1; i<=tableColumnCount; i++){
				actList.add(rs.getString(i));			
			}
		}
		if (actList.toString().equals(expList.toString())) {
			System.out.println(": Actual list is -> " + actList + " AND Expected list is-> " + expList);
			APP_LOGS.debug(": Actual list is -> " + actList + " AND Expected list is-> " + expList);
		} else {
			System.out.println(": Actual list is -> " + actList + " AND Expected list is-> " + expList);
			return "FAIL - Actual list is -> " + actList + " AND Expected list is-> " + expList;
		}
	} catch (SQLException e) {
		System.out.println(": "+e.getMessage());
		return "FAIL";
	}
	return "PASS";
}

//***************** 23. ExecuteDBQuery****************//
public String ExecuteDBQuery(String firstXpathKey) throws SQLException {
	/* @HELP
	@class:			Keywords
	@method:		ExecuteDBQuery ()
	@parameter:	String firstXpathKey
	@notes:			Fetches the data from database and verifies the data with expected.
	@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
	@END
	 */	
	System.out.println(": Executing "+firstXpathKey+" query");
	APP_LOGS.debug(": Executing "+firstXpathKey+" query");
	String query = OR.getProperty(firstXpathKey);
	try {
		statement = connection.createStatement();
		statement.executeUpdate(query);
	} catch (SQLException e) {
		System.out.println(": "+e.getMessage());
		return "FAIL";
	}
	return "PASS";
}

//***************** 24. CloseDBConnection****************//
public String CloseDBConnection() {
	/* @HELP
	@class:			Keywords
	@method:		CloseDBConnection ()
	@parameter:	None
	@notes:			Connect to the database
	@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
	@END
	 */	
	System.out.println(": Closing "+databaseType+" database");
	APP_LOGS.debug(": Closing "+databaseType+" database");
	try {
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
	} catch (Exception e) {
		System.out.println(": "+e.getMessage());
		return "FAIL";
	}
	return "PASS";
}


//***************** 25. CloseBrowser****************//
public String CloseBrowser() {
		/* @HELP
		@class:			Keywords
		@method:		CloseBrowser ()
		@parameter:	None
		@notes:			Closing the opened Browser after the Test Case Execution.
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		*/	
		getTextOrValues.clear();
		scriptTableFirstRowData="";
		System.out.println(": Closing the Browser");
		APP_LOGS.debug(": Closing the Browser");
		try {
				driver.close();
		}catch (Exception e) {
			return "FAIL - Not able to Close Browser";
		}
		return "PASS";
	}

//***************** 26. QuitBrowser****************//
public String QuitBrowser() {
		/* @HELP
		@class:			Keywords
		@method:		QuitBrowser ()
		@parameter:	None
		@notes:			Quits all opened Browsers or Brower instances after the test case Execution.
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		 */	
		getTextOrValues.clear();
		scriptTableFirstRowData="";
		System.out.println(": Quiting all opened Browsers");
		APP_LOGS.debug(": Quiting all opened Browsers");
		try {
				driver.quit();
				driver = null;
		} catch (Exception e) {
			return "FAIL - Not able to Quit all opened Browsers";
		}
		return "PASS";
	}

//***************** 7. MouseHover****************//
	public String MouseHover(String firstXpathKey) {
		/* @HELP
		@class:			Keywords
		@method:		MouseHoverAndClick ()
		@parameter:		String firstXpathKey
		@notes:			Hover mouse over given Object, link, Hyperlink, selections or buttons of a web page.
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		 */
		System.out.println(": Performing Mouse hover on " + firstXpathKey);
		APP_LOGS.debug(": Performing Mouse hover on " + firstXpathKey);
		try {
				Thread.sleep(2000);
				Actions act=new Actions(driver);
				WebElement root=returnElementIfPresent(firstXpathKey);
				act.moveToElement(root).build().perform();
				//Thread.sleep(2000);
		} catch (Exception e) {
			return "FAIL - Not able to do mouse hover on -- " + firstXpathKey;
		}
		return "PASS";
	}
	

//***************** 7. MouseHoverAndClick****************//
	public String MouseHoverAndClick(String firstXpathKey, String secondXpathKey) {
		/* @HELP
		@class:			Keywords
		@method:		MouseHoverAndClick ()
		@parameter:	String firstXpathKey, String secondXpathKey
		@notes:			Performs Click action on link, Hyperlink, selections or buttons of a web page.
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		 */
		System.out.println(": Performing Mouse hover and Click action on " + firstXpathKey);
		APP_LOGS.debug(": Performing Mouse hover and Click action on " + firstXpathKey);
		try {
				Thread.sleep(2000);
				Actions act=new Actions(driver);
				WebElement root=returnElementIfPresent(firstXpathKey);
				act.moveToElement(root).build().perform();
				Thread.sleep(1000);
				returnElementIfPresent(secondXpathKey).click();
		} catch (Exception e) {
			return "FAIL - Not able to do mouse hover and click on -- " + firstXpathKey;
		}
		return "PASS";
	}
	
	// *****************Singleton Class********************
	public static Keywords getKeywordsInstance() throws IOException {
		if (keywords == null) {
			keywords = new Keywords();
		}
		return keywords;
	}
	
	public String TestCaseEnds() {
		/* @HELP
		@class:			Keywords
		@method:		TestCaseEnds ()
		@parameter:		None
		@notes:			Performs necessary actions before concluding the testcase like if testcase has anything fail it will declare by Assert.
		@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
		@END
		 */
		System.out.println(": TestCase is Ending");
		APP_LOGS.debug(": TestCase is Ending");
		getTextOrValues.clear();
		scriptTableFirstRowData="";
		try {
			if (Fail == true) {
				highlight = false;
				Fail = false;
				//QuitBrowser();
				//driver=null;
				Assert.assertTrue(false, failedResult);
			}else {
				Fail = true;
				Assert.assertTrue(true, failedResult);
				Fail = false;
			}
		} catch (Exception e) {
			return "FAIL - Not able to end TC";
		}
		return "PASS";
	}
	
	
	public String getLastTestCaseName(){
		
		/* @HELP
		@class:			Keywords
		@method:		getLastTestCaseName ()
		@returns:		returns last test case name from Master.xlsx file which have runmode Y  into any combination
		@END
		 */
		Xls_Reader x =new Xls_Reader(masterxlsPath + "/Master.xlsx");
	 String suiteType=	suitetype;
	
	 
	 if(!suiteType.contains("_")  &&  !suiteType.equalsIgnoreCase("Regression")){
		int totalRows = x.getRowCount("Test Cases");
		 String lastTestCaseName = null;
		 String tcType=null;
		 String runMode=null;
			 for(int i = 1; i <= totalRows; i++){
				 tcType = x.getCellData("Test Cases",1,i);
					if(tcType.contains(suiteType)){
						 runMode = x.getCellData("Test Cases",2,i);
						if(runMode.contains("Y")){
							lastTestCaseName = x.getCellData("Test Cases",0,i);
						}
				    }
	             }System.out.println("Last Test Case Name is: "+lastTestCaseName);
	             return lastTestCaseName;
				 
	 } else if(suiteType.contains("_")){
        System.out.println(": This suiteType contains UnderScore and is: "+suiteType);
		String splitArray[] = suiteType.split("_");
		 int totalRows = x.getRowCount("Test Cases");
		 String lastTestCaseName = null;
		 String tcType=null;
		 String runMode=null;
			 for(int i = 1; i <= totalRows; i++){
				 tcType = x.getCellData("Test Cases",1,i);
					if(tcType.contains(splitArray[0]) || tcType.contains(splitArray[1])){
						 runMode = x.getCellData("Test Cases",2,i);
						 if(runMode.contains("Y")){
								lastTestCaseName = x.getCellData("Test Cases",0,i);
							}
				    }
	             }System.out.println("Last Test Case Name is: "+lastTestCaseName);
	             return lastTestCaseName;
			 
	 } else {
	        System.out.println(": This suiteType is "+suiteType);
			int totalRows = x.getRowCount("Test Cases");
			String lastTestCaseName = null;
			String runMode=null;
				 for(int i = 1; i <= totalRows; i++){
					 runMode = x.getCellData("Test Cases",2,i);
						if(runMode.equalsIgnoreCase("Y")){
						lastTestCaseName = x.getCellData("Test Cases",0,i);
					    }
		             }System.out.println("Last Test Case Name is: "+lastTestCaseName);
		             return lastTestCaseName;
			}
	 }
	
	
public String SwitchToNewWindow(){
	
	/* @HELP
	@class:			Keywords
	@method:		SwitchToNewWindow ()
	@parameter:		None
	@notes:			Switches to new window and move the control of the driver to the newly opened window.
	@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
	@END
	 */
	
	try{
	System.out.println(": Switching to New Window");
	APP_LOGS.debug(": Switching to New Window");
	Set <String> set = driver.getWindowHandles();
	Iterator<String> itr = set.iterator();
	parentWindowID = itr.next();
	String ChID = itr.next();
	
	driver.switchTo().window(ChID);
	}catch(Exception e){
		
		System.out.println("Not Able To Perform SwitchToNewWindow");
	}
	return "PASS";
	
}

public String SwitchToParentWindow(){
	
	/* @HELP
	@class:			Keywords
	@method:		SwitchToParentWindow ()
	@parameter:		None
	@notes:			Switches to parent window and move the control of the driver main window of the browser.
	@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
	@END
	 */
	
	try{
	System.out.println(": Switching to Parent Window");
	APP_LOGS.debug(": Switching to Parent Window");
	driver.switchTo().window(parentWindowID);
	}catch(Exception e){
		System.out.println("Not Able To Perform SwitchToParentWindow");
	}
			
			return "PASS";
	
}

public String clearTextField(String firstXpathKey){
	

    /* @HELP
    @class:            Keywords
    @method:        clearTextField ()
    @parameter:   	 None
    @notes:            Clearing Text Field.
    @returns:        ("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
    @END
     */ 
	
	try {
		System.out.println(": Clearing Text Field");
		APP_LOGS.debug(": Clearing Text Field");
		Thread.sleep(1000);
		returnElementIfPresent(firstXpathKey).clear();
	} catch (InterruptedException e) {
		System.out.println("Not Able to perform clearTextField");
	}
	
	
	return "PASS";
	
}



public String ScrollPageToBottom() {
    /* @HELP
    @class:            Keywords
    @method:        ScrollPageToBottom ()
    @parameter:    None
    @notes:            Scroll The Page to END in terms of what element is passed in firstXpathKey.
    @returns:        ("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
    @END
     */    
    System.out.println("Scrolling The Page to END Using END key");
    APP_LOGS.debug(": Scrolling The Page to END Using END key");
    try {
    	((JavascriptExecutor) driver)
        .executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }
         catch (Exception e) {
        return "FAIL - Not Able to Scrol The Page to END Using END key";
         }
    return "PASS";
}



public String ScrollPageToEnd(String firstXpathKey) {
    /* @HELP
    @class:            Keywords
    @method:        ScrollPageToEnd ()
    @parameter:    None
    @notes:            Scroll The Page to END in terms of what element is passed in firstXpathKey.
    @returns:        ("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
    @END
     */    
    System.out.println("Scrolling The Page to END Using END key");
    APP_LOGS.debug(": Scrolling The Page to END Using END key");
    try {
        WebElement element = returnElementIfPresent(firstXpathKey);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
         catch (Exception e) {
        return "FAIL - Not Able to Scrol The Page to END Using END key";
         }
    return "PASS";
}


//***************** VerifyURLDDTContent****************//
public String VerifyURLDDTContent(String expUrlContent) {
	
	/* @HELP
	@class:			Keywords
	@method:		VerifyURLDDTContent ()
	@parameter:	String actUrl, String expUrl
	@notes:			Verifies the Actual Web Page URL as compared to the Expected Web Page URL. Verification is performed on the same Web page in DDT manner. 
	@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
	@END
	 */
	
		System.out.println(": Verifying "+ expUrlContent + " is Present in URL");
		APP_LOGS.debug(": Verifying "+ expUrlContent + " is Present in URL");
		highlight = false;
		try {
			actUrl = driver.getCurrentUrl();
			if (actUrl.contains(expUrlContent)) {
				
			System.out.println(": Expected String-> " + expUrlContent + " is Present in Current URL-> " + actUrl);
			APP_LOGS.debug(": Expected String-> " + expUrlContent + " is Present in Current URL-> " + actUrl);
				
				System.out.println(": Expected is->" + expUrlContent);
				APP_LOGS.debug(": Expected is->" + expUrlContent);
			} else {
				System.out.println(": FAIL- Expected String-> " + expUrlContent + " is NOT Present in Current URL-> " + actUrl);
				APP_LOGS.debug(": FAIL- Expected String-> " + expUrlContent + " is NOT Present in Current URL-> " + actUrl);
				return "FAIL- Expected String-> " + expUrlContent + " is NOT Present in Current URL-> " + actUrl;
			}
		} catch (Exception e) {
			highlight = true;
			
			return "FAIL - Not able to verify URL content";
		}
		return "PASS";
	}

//***************** verifyDDTImageExistsByImgSRC****************//
public String VerifyDDTImageExistsByImgSRC(String firstXpathKey, String inputData) {
	
	 /* @HELP
    @class:            Keywords
    @method:       	VerifyDDTImageExistsByImgSRC ()
    @parameter:  	  None
    @notes:           Performs verification of the Img content src attribute on web page by dataColVal.
    @returns:        ("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
    @END
     */    
   
		System.out.println(": Verifying Image Exists by its SRC property");
		APP_LOGS.debug(":  Verifying Image Exists by its SRC property");
		highlight = false;
		try{
			WebElement img= returnElementIfPresent(firstXpathKey);
			String src = img.getAttribute("src");
			APP_LOGS.debug(": Expected SRC "+ inputData);
			APP_LOGS.debug(": Actual SRC "+ src);
			
			if (src.compareTo(inputData) == 0)
				{
				System.out.println(": Expected SRC Of Image-> " + inputData + " Actual SRC of Image-> " + src);
				APP_LOGS.debug(": Expected SRC Of Image-> " + inputData + " Actual SRC of Image-> " + src);
				}
			else{
			System.out.println(": FAIL- Expected SRC Of Image-> " + inputData + " is not present in Actual SRC of Image-> " + src);
			APP_LOGS.debug(": FAIL- Expected SRC Of Image-> " + inputData + " is not present in Actual SRC of Image-> " + src);
			return "FAIL- Expected SRC Of Image-> " + inputData + " is not present in Actual SRC of Image-> " + src;
			}
		} catch (Exception e) {
			highlight = true;
			return "FAIL - Not able to verify SRC Of Image";
		}
		return "PASS";
	}

public String GoToHomeLoansSubMenu(String firstXpathKeyOption, String secondXpathKeyOption) {
	 /* @HELP
    @class:            Keywords
    @method:        GoToHomeLoansSubMenu ()
    @parameter:    Two
    @notes:        This Method is created as generic method as per LT project requirement as there are two ways to go to "HOME EQUITY" sub menu links.
    				Example: for "HOME EQUITY LOANS", One is via "HOME LOANS> HOME EQUITY LOANS" and other is "ALL>Home Equity". 
    				firstXpathKeyOption is for "HOME EQUITY LOANS" sub link
    				AND
    				secondXpathKeyOption is for "Home Equity" sub link
    				IMP: Please make sure that identifiers for "HOME LOANS" and "ALL" Menu links are present in OR file.
    @returns:        ("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
    @END
     */   
	if (returnElementIfPresent("HOME_LOANSLINK")!=null){
		MouseHoverAndClick("HOME_LOANSLINK",firstXpathKeyOption);
	}
	else{
		MouseHoverAndClick	("ALL_LINK", secondXpathKeyOption);
	}		
	return "PASS";
}


public String switchToiFrame(String dataValue) {
	 /* @HELP
    @class:            Keywords
    @method:        switchToiFrame ()
    @parameter:    	One
    @notes:        	Parameter will be integer of iframe tag index of the HTML page
    @returns:        ("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
    @END
     */  
	System.out.println(": Switching to iframe "+dataValue );
    APP_LOGS.debug(": Switching to iframe");
	 try {
		 driver.switchTo().frame(0);   
		 System.out.println(" : Crossed");
		 }
	         catch (Exception e) {
	        return "FAIL - Not Able to switch to iframe";
	         }
	    
	return "PASS";
}


public String switchToDefaultContent() {
	 /* @HELP
   @class:            Keywords
   @method:        switchToDefaultContent ()
   @parameter:    	One
   @notes:        	No parameter is needed for this method it will give control to the main page for switching form iFrame.  
   @returns:        ("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
   @END
    */  

	System.out.println(": Switch default content from iframe");
    APP_LOGS.debug(": Switch default content from iframe");
	 try {
		 driver.switchTo().defaultContent();
		 }
	         catch (Exception e) {
	        return "FAIL - Not Able to switch default content from iframe";
	         }
	    
	return "PASS";
}

//***************** VerifyTextContains****************//
@SuppressWarnings("unchecked")
public String VerifyTextContains(String firstXpathKey, String secondXpathKey, String expText) throws ParseException {
	/*@HELP
	@class:			Keywords
	@method:		VerifyTextContains ()
	@parameter:	String firstXpathKey, Optional=>String secondXpathKey, Optional=> String expText
	@notes:			Verifies the Actual Text as compared to the Expected Text. Verification can be performed on the same page or on different pages. User can perform two different webelement's text comparision by  passing argument as objectKeySecond. In this it is not necessary expText should have as a whole it uses contains function.
	@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
	@END
	 */
	 
		//System.out.println("^^^^^^^^^^^^^^^^Dhruval Patel : "+expText);
	
		highlight = false;
		System.out.println(": Verifying " + firstXpathKey + " Text on the Page");
		APP_LOGS.debug(": Verifying " + firstXpathKey + " Text on the Page");
		
		String regex  = "[0-9].[0-9]";
		if (expText.matches(regex)) {
			//System.out.println("----------------------------------------------- : 1");
			NumberFormat nf = NumberFormat.getInstance();
			Number number = nf.parse(expText);
			long lnputValue = number.longValue();
			expText = String.valueOf(lnputValue);
		}
		if (expText.isEmpty()) {
			//System.out.println("----------------------------------------------- : 2");
			getTextOrValues.put(secondXpathKey, returnElementIfPresent(secondXpathKey).getText());
			expText = getTextOrValues.get(secondXpathKey).toString();
			System.out.println("----------------------------------------------- : 3");
		}		
		try {
			//System.out.println("----------------------------------------------- : "+returnElementIfPresent(firstXpathKey).isDisplayed());
			getTextOrValues.put(firstXpathKey, returnElementIfPresent(firstXpathKey).getText());
			//System.out.println("----------------------##------------------------- : "+returnElementIfPresent(firstXpathKey).getText());
			actText = getTextOrValues.get(firstXpathKey).toString();		
			actText=actText.trim();
			expText=expText.trim();

			if (actText.contains(expText) == true) {
				System.out.println(": Actual is-> " + actText + " AND Expected is-> " + expText);
				APP_LOGS.debug(": Actual is-> " + actText + " AND Expected is->" + expText);
			} else {
				globalExpText=expText;
				System.out.println(": Actual is-> " + actText + " AND Expected is-> " + expText);
				return "FAIL - Actual is-> " + actText + " AND Expected is->" + expText;
			}
			
		} catch (Exception e) {
			highlight = true;
			return "FAIL - Not able to read text--" + firstXpathKey;
		}
		return "PASS";
	}

//***************** VerifyToolTip****************//
public String VerifyToolTip(String firstXpathKey, String expText) {
	/* @HELP
	@class:			Keywords
	@method:		VerifyToolTip ()
	@parameter:		String firstXpathKey
	@notes:			Hover mouse over given Object, link, Hyperlink, selections or buttons of a web page and get the tooltip from the element and verifies it with expText.
	@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
	@END
	 */
	highlight = false;
	System.out.println(": Performing Mouse hover on " + firstXpathKey);
	APP_LOGS.debug(": Performing Mouse hover on " + firstXpathKey);
	try {
			Thread.sleep(2000);
			Actions act=new Actions(driver);
			WebElement root=returnElementIfPresent(firstXpathKey);
			act.moveToElement(root).build().perform();
			Thread.sleep(2000);
			String actText=root.getText();
			
			if(actText.contains(expText)){
				System.out.println(": Actual is-> " + actText + " AND Expected is->" + expText);
				APP_LOGS.debug(": Actual is-> " + actTitle + " AND Expected is->" + expText);
			} else {
				highlight = true;
				System.out.println(": Actual is-> " + actText + " AND Expected is->" + expText);
				return "FAIL - Actual is-> " + actText + " AND Expected is->" + expText;
			}
		} catch (Exception e) {
			highlight = true;
			return "FAIL - Not able to get tool tip text";
		}
		return "PASS";
		
}

//***************** VerifyTextDDTdataContains****************//
@SuppressWarnings("unchecked")
public String VerifyTextDDTdataContains(String firstXpathKey, String secondXpathKey, String expText) throws ParseException, InterruptedException {
	/* @HELP
	@class:			Keywords
	@method:		VerifyTextDDTdata ()
	@parameter:	String firstXpathKey, Optional=>String secondXpathKey, Optional=> String expText
	@notes:			Verifies the Actual Text as compared to the Expected Text. Verification can be performed on the same page or on different pages for DDT. 
	@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
	@END
	 */
		highlight = false;
		System.out.println(": Verifying " + firstXpathKey + " Text on the Page");
		APP_LOGS.debug(": Verifying " + firstXpathKey + " Text on the Page");
		//System.out.println("Inside VerifyTextDDTdataContains...............................");
		
			try {
				getTextOrValues.put(firstXpathKey, returnElementIfPresent(firstXpathKey).getText());
				//actText = getTextOrValues.get(firstXpathKey).toString().toLowerCase();
				actText = getTextOrValues.get(firstXpathKey).toString();
				actText=actText.trim();
				expText=expText.trim();
				
				if (actText.contains(expText)) {
					System.out.println(": Actual is-> " + actText + " AND Expected is->" + expText);
					APP_LOGS.debug(": Actual is-> " + actText + " AND Expected is->" + expText);
				} else {
					globalExpText=expText;
					System.out.println(": Actual is-> " + actText + " AND Expected is->" + expText);
					return "FAIL - Actual is-> " + actText + " AND Expected is->" + expText;
				}
			} catch (Exception e) {
				    highlight = true;
					return "FAIL - Not able to read text--" + firstXpathKey;
			}
			return "PASS";
	}	
	

//***************** 16. VerifyTitle****************//
public String VerifyTitleContains(String expTitle) {
	/* @HELP
	@class:			Keywords
	@method:		VerifyTitle ()
	@parameter:	String actTitle & String expTitle
	@notes:			Verifies the Actual Web Page Title as compared to the Expected Web Page title. Verification is performed on the same Web page. It is not necessary to have full page title as expTitle. 
	@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
	@END
	 */
		//System.out.println("Inside VerifyTitleContains.................... ");
		highlight = false;
		System.out.println(": Verifying Page Title");
		APP_LOGS.debug(": Verifying Page Title");
		try {
			expTitle = expTitle.replace("_", ",");
			expTitle.trim();
			actTitle = driver.getTitle();
			if (actTitle.contains(expTitle)) {
				System.out.println(": Actual is-> " + actTitle + " AND Expected is->" + expTitle);
				APP_LOGS.debug(": Actual is-> " + actTitle + " AND Expected is->" + expTitle);
			} else {
				highlight = true;
				System.out.println(": Actual is-> " + actTitle + " AND Expected is->" + expTitle);
				return "FAIL - Actual is-> " + actTitle + " AND Expected is->" + expTitle;
			}
		} catch (Exception e) {
			highlight = true;
			return "FAIL - Not able to get title";
		}
		return "PASS";
}
//***************** 21. OpenMongoDBConnection****************//
		public String OpenMongoDBConnection() {
			/* @HELP
			@class:			Keywords
			@method:		OpenMongoDBConnection ()
			@parameter:	None
			@notes:			Connect to the Mongo database
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
			 */	
			System.out.println(": Connecting to the "+databaseType+" database");
			APP_LOGS.debug(": Connecting to the "+databaseType+" database");
			try {
				mongoClient = new MongoClient( new MongoClientURI(dbConnection));
		        db = mongoClient.getDatabase("np-staging-credit-card-offers");			// DB Name
		        System.out.println(": Connected to the "+databaseType+" database Successfully");
				APP_LOGS.debug(": Connected to the "+databaseType+" database Successfully");
		    	} 
			catch (Exception e) {
				System.out.println(": "+e.getMessage());
				return "FAIL - Not able to connect to the "+databaseType;
			}
			return "PASS";
		}
		
//***************** 21. VerifyMongoDBQuery****************//
			public String VerifyMongoDBQuery(final String expDBData) {
				/* @HELP
				@class:			Keywords
				@method:		VerifyMongoDBQuery ()
				@parameter:	None
				@notes:			Verifying MongoDB Data
				@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
				@END
				 */	
				getConfigDetails();
				System.out.println(": Verfying '"+expDBData+"' Issuer is present in database");
				APP_LOGS.debug(": Verfying '"+expDBData+"' Issuer is present in database");
				highlight = false;
				try {
					BasicDBObject whereQuery = new BasicDBObject();
					System.out.println(": Firing {'name' : '"+expDBData+"'} Query to database");
					APP_LOGS.debug(": Firing {'name' : '"+expDBData+"'} Query to database");
					whereQuery.put("name", expDBData);
			        FindIterable<Document> iterable = db.getCollection("issuers").find(whereQuery);
			        iterable.forEach(new Block<Document>() {
			          @Override
			          public void apply(final Document document) {
			        	  	String temp =  document.toString();
			        	  	System.out.println(": Actual data received from Database as result of above Query: "+temp);
							APP_LOGS.debug(": Actual data received from Database as result of above Query: "+temp);
							if (temp.contains(expDBData))
							{
						    System.out.println(": "+expDBData+" Data is present in DataBase");
						    APP_LOGS.debug(": "+expDBData+" Data is present in DataBase");
							}
			          	}
			        });
			      } 
				catch (Exception e) {
					highlight = true;
					System.out.println(": "+e.getMessage());
					return "FAIL - Not able to Verify the Data: "+ expDBData;
				}
				return "PASS";
			}
						
//***************** 21. CloseMongoDBConnection****************//
		public String CloseMongoDBConnection() {
			/* @HELP
			@class:			Keywords
			@method:		CloseMongoDBConnection ()
			@parameter:	None
			@notes:			Close MongoDB Connection
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
			 */	
			System.out.println(": Closing "+databaseType+" Database Connection");
			APP_LOGS.debug(": Closing "+databaseType+" Database Connection");
			try {
				mongoClient.close();
				System.out.println(": "+databaseType+" Database Connection is Closed");
				APP_LOGS.debug(": "+databaseType+" Database Connection is Closed");
		      } 
			catch (Exception e) {
				System.out.println(": "+e.getMessage());
				return "FAIL - Not able to close : " +databaseType+" Database Connection";
			}
			return "PASS";
		}		

//***************** 21. startHARReading****************//
		public String startHARReading() {
			/* @HELP
			@class:			Keywords
			@method:		startHARReading ()
			@parameter:	None
			@notes:			It will start recording the Network panel data with new browser instance.
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
			 */	
			System.out.println(": Capturing Network Panel Data ");
			APP_LOGS.debug(": Capturing Network Panel Data ");
			try {
				// 
			  // HarFileWriter w = new HarFileWriter();
			  // System.out.println("Reading " + harPath);
			      List<HarWarning> warnings = new ArrayList<HarWarning>();
			      //HarLog log = r.readHarFile(f, warnings);
			      server.newHar();
			      Thread.sleep(3000);
				System.out.println(": Started Capturing Network Panel data in HAR Format");
				APP_LOGS.debug(": Started Capturing Network Panel data in HAR Format");
		      } 
			catch (Exception e) {
				System.out.println(": "+e.getMessage());
				return "FAIL - Not able to Start Capturing Network Panel data in HAR Format";
			}
			return "PASS";
		}	
	
//***************** 21. stopHARReading****************//		
		public String stopHARReading() {
			/* @HELP
			@class:			Keywords
			@method:		stopHARReading ()
			@parameter:	None
			@notes:			stops the recording of the Network panel data.
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
			 */	
			System.out.println(": Stopping Network Panel Data Capture and Saving it in HAR file");
			APP_LOGS.debug(": Stopping Network Panel Data Capture and Saving it in HAR file");
			try {

			har = server.getHar();
			FileOutputStream fos = new FileOutputStream(harPath);
			har.writeTo(fos);
			server.endHar();
			Thread.sleep(3000);
			System.out.println(": Network Panel Data is Captured and Saved in HAR file");
			APP_LOGS.debug(": Network Panel Data is Captured and Saved in HAR file");
		      } 
			catch (Exception e) {
				System.out.println(": "+e.getMessage());
				return "FAIL - Not able to Capture and Save Network Panel Data as HAR file";
			}
			return "PASS";
		}	
		
//***************** 21. VerifyHARContent****************//		
		public String VerifyHARContent(String data) {
			/* @HELP
			@class:			Keywords
			@method:		VerifyHARContent ()
			@parameter:	None
			@notes:			Verifies Html archive content through saved .har file in HAR folder. 
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
			 */	

			String[] temp;
			System.out.println(": Verifing : "+data+" is Present in HAR file");
			APP_LOGS.debug(": Verifing : "+data+" is Present in HAR file");
			highlight = false;
			try {
				 log = r.readHarFile(f, warnings);
				 entries = log.getEntries();
				 String mk=entries.toString();

			      temp = data.split(",");
			      
			      for(int i = 0; i<temp.length;i++){
			    	  
			    	  System.out.println(": Verifing "+temp[i]+" is Present in HAR file");
			    	  APP_LOGS.debug(": Verifing "+temp[i]+" is Present in HAR file");
			    	  
			    	  if(mk.contains(temp[i].toString()))
			    	  {
			    	System.out.println(": "+temp[i]+" is Present in HAR file");
					APP_LOGS.debug(": "+temp[i]+" is Present in HAR file");
					
			    	  }else{
			    		  System.out.println("Fail : "+temp[i]+" is Not Present in HAR file");
							APP_LOGS.debug("Fail : "+temp[i]+" is Not Present in HAR file");
					  }
			    	  }
			
				
		      } 
			catch (Exception e) {
				highlight = true;
				System.out.println(": "+e.getMessage());
				return "FAIL - Not Able to verify " +data+ "in HAR file";
			}
			return "PASS";
		}	

//***************** VerifyXMLContent****************//	
		public String VerifyXMLContent(String data) throws IOException,ParserConfigurationException,SAXException,IOException {
			/* @HELP
			@class:			Keywords
			@method:		VerifyXMLContent ()
			@parameter:	None
			@notes:			Verifies content of previously gained HttpRequest in MakeGetRequestDDT with expected data given in module excel.
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
			 */	

			
			System.out.println(": Verifing 'Lender Name' and "+data+" is Present in GET Response XML file");
			APP_LOGS.debug(": Verifing 'Lender Name' and "+data+" is Present in GET Response XML file");
			highlight = false;
			String[] temp;
			ArrayList<String> al = new ArrayList<String>();
			
			File file = new File(SRC_FOLDER2+"/data.xml");
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(StrGet);
			bw.close();
			
			try{

		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		       // factory.setValidating(true);
		        factory.setIgnoringElementContentWhitespace(true);
		        factory.setIgnoringElementContentWhitespace(true);
		        factory.setIgnoringComments(true);
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        
		        org.w3c.dom.Document doc = builder.parse(file);
	            Element element = doc.getDocumentElement();

	            // get all child nodes
	            NodeList nodes = element.getElementsByTagName(data);

	            for (int i = 0; i < nodes.getLength(); i++) {
	                System.out.println("" + nodes.item(i).getTextContent());
	                String dtr= nodes.item(i).getTextContent();
	                al.add(dtr);
	             }
	            
	           //System.out.println( "::::: "+doc.getChildNodes());
	        if (al.size() > 0) {
				System.out.println(": Following 'Lender Names' are Present in GET response XML->" + al.toString());
				APP_LOGS.debug(": Following 'Lender Names' are Present in GET response XML->" + al.toString());
			} else {
				//highlight = true;
				System.out.println("::::::: FAIL: No 'Lender Name' is Present in GET response XML");
				APP_LOGS.debug(": FAIL: No 'Lender Name' is Present in GET response XML");
				return "*FAIL - Not Able to verify 'Lender Name' in GET Response XML";
			}
		} catch (Exception e) {
			//highlight = true;
			System.out.println(": " + e.getMessage());
			return "FAIL - Not Able to verify 'Lender Name' in GET Response XML";
		}
			
			/*try {
			while (StrGet.contains("<StudentLoanOffer>")) {
				temp = StrGet.split("<Lender>", 2);
				if (temp.length == 1) {
					break;
				} else {
					StrGet = temp[1];
				}
				int retval = StrGet.indexOf(data);
				String dataWithForwardslash = data.replace("<", "</");
				int endIndex = StrGet.indexOf(dataWithForwardslash);
				String dtr = StrGet.substring(retval, endIndex);
				dtr = dtr.replace("<Name>", "");
				al.add(dtr);
				}
			if (al.size() > 0) {
				System.out.println(": Following 'Lender Names' are Present in GET response XML->" + al.toString());
				APP_LOGS.debug(": Following 'Lender Names' are Present in GET response XML->" + al.toString());
			} else {
				//highlight = true;
				System.out.println("::::::: FAIL: No 'Lender Name' is Present in GET response XML");
				APP_LOGS.debug(": FAIL: No 'Lender Name' is Present in GET response XML");
				return "*FAIL - Not Able to verify 'Lender Name' in GET Response XML";
			}
		} catch (Exception e) {
			//highlight = true;
			System.out.println(": " + e.getMessage());
			return "FAIL - Not Able to verify 'Lender Name' in GET Response XML";
		}*/
		return "PASS";
	}

		public String VerifyCompleteGetResponse(String data) throws org.json.simple.parser.ParseException {
			/* @HELP
			@class:			Keywords
			@method:		VerifyCompleteGetResponse ()
			@parameter:	None
			@notes:			verifies the complete response of the GET request by equalsIgnoreCase.
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
			 */	

			
			System.out.println(": Verifing and "+data+" is Present in GET Response XML file");
			APP_LOGS.debug(": Verifing and "+data+" is Present in GET Response XML file");
			highlight = false;
			//String[] temp;
			//ArrayList<String> al = new ArrayList<String>();
			 try {

			JSONParser parser = new JSONParser();
			JSONObject obj2_json = (JSONObject) parser.parse(StrGet);
			String temp[] = null;
			temp = data.split(",");
			System.out.println(obj2_json.get(temp[0]));

			if (obj2_json.get(temp[0]).equals(temp[1])) {
				System.out.println(": Following " + temp[1]+ " are Present in GET response ");
				APP_LOGS.debug(": Following " + temp[1]+ " are Present in GET response ");
			} else {

				APP_LOGS.debug("FAIL - Not Able to verify " + temp[1]+ " in GET Response");
				return "FAIL - Not Able to verify " + temp[1] + " in GET Response";
			}

		} catch (Exception e) {
			// highlight = true;
			System.out.println(": " + e.getMessage());
			return "FAIL - Not Able to verify " + data + " in GET Response XML";
		}
		return "PASS";
	}

//***************** VerifyPOSTRequestContent****************//
		@SuppressWarnings("unchecked")
		public String VerifyPOSTRequestContent(String data) {
			/* @HELP
			@class:			Keywords
			@method:		VerifyPOSTRequestContent ()
			@parameter:	None
			@notes:			Verifies content of previously gained HttpRequest in MakePostRequest or MakePostRequestJSON with expected data given in module excel.
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
			 */	

		
			System.out.println(": Verifing : "+data+" is Present in POST response");
			APP_LOGS.debug(": Verifing : "+data+" is Present in POST response");
			highlight = false;
			
			try {				  
					 data = data.trim();	
					/* JSONParser parser = new JSONParser();
					 //Object obj = parser.parse(str);
			        // JSONArray array = (JSONArray)obj;
			         JSONObject obj2 = (JSONObject) parser.parse(str);*/
					/* StrPost = StrPost.replace("\"", "");
					 data = data.replace("\"", "");*/
					// System.out.println("StrPost ::::::::::::::::::::: "+StrPost+" data :::::::::::::::" +data);
					 if (StrPost.contains(data)) {
						// System.out.println("IF ///////////////////////////////");
							System.out.println(": Actual is-> " + StrPost + " AND Expected is->" + data);
							APP_LOGS.debug(": Actual is-> " + StrPost + " AND Expected is->" + data);
						} else {
							//highlight = true;
							//System.out.println("Else ...................................");
							System.out.println(": Actual is-> " + StrPost + " AND Expected is->" + data);
							return ": Actual is-> " + StrPost + " AND Expected is->" + data;
						}	  
			}
			catch (Exception e) {
				//highlight = true;
				System.out.println(": "+e.getMessage());
				return "FAIL - Not Able to verify " +data+ "in XML respose";
			}
			return "PASS";
		}

//***************** executeHttpGet****************//
		public HttpResponse executeHttpGet(String uri, Map<String, String> headerMap) throws ClientProtocolException, IOException {

			HttpClient client = HttpClientBuilder.create().build();
			HttpGet get = new HttpGet(uri);

			Iterator<String> itr = headerMap.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String) itr.next();
				get.addHeader(key, headerMap.get(key).toString());
			}

			return client.execute(get);
		}

//***************** executeHttpPost****************//
		public HttpResponse executeHttpPost(String uri, Map<String, String> headerMap, String body)
				throws ClientProtocolException, IOException {

			EntityBuilder builder = EntityBuilder.create();
			builder.setText(body);
			HttpEntity entity = builder.build();

			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(uri);

			Iterator<String> itr = headerMap.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String) itr.next();
				post.addHeader(key, headerMap.get(key).toString());
			}

			post.setEntity(entity);

			return client.execute(post);
		}

//***************** processResponse****************//
		public StringBuffer processResponse(HttpResponse response)
				throws ClientProtocolException, IOException {

			BufferedReader rd = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			return result;
		}


		private StringBuffer readFile(String filePath) {

			BufferedReader br = null;
			StringBuffer stringBuffer = new StringBuffer();

			try {

				String lineString = null;
				br = new BufferedReader(new java.io.FileReader(filePath));

				while ((lineString = br.readLine()) != null) {
					stringBuffer.append(lineString);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			return stringBuffer;
		}

		
		
//*****************SignOutIFAlreadyLoggedIn****************//		
		public String SignOutIFAlreadyLoggedIn(String firstXpathKey, String secondXpathKey) {
			/* @HELP
			@class:			Keywords
			@method:		SignOutIFAlreadyLoggedIn ()
			@parameter:		String firstXpathKey & String secondXpathKey
			@notes:			LT PROJECT SPECIFIC KEYWORD: Perform Signout at home page if user is already logged in.
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
			 */

			try {
			if (isElementPresent(firstXpathKey)) {
					System.out.println(": User is already looged in, Performing Mouse hover on " + firstXpathKey);
					APP_LOGS.debug(": User is already looged in, Performing Mouse hover on " + firstXpathKey);
					Thread.sleep(2000);
					Actions act=new Actions(driver);
					WebElement root=returnElementIfPresent(firstXpathKey);
					act.moveToElement(root).build().perform();
					Thread.sleep(2000);
					returnElementIfPresent(secondXpathKey).click();
			}
			else{
				System.out.println(": No User is Logged-IN.");
				APP_LOGS.debug(": No User is Logged-IN.");
			}
				
			} catch (Exception e) {
				return "FAIL - Not able to Signout.";
			}
			return "PASS";
		}
		
//***************** makeGetRequest****************//
		public String makeGetRequest(String data) throws ClientProtocolException, IOException {
			/* @HELP
			@class:			Keywords
			@method:		makeGetRequest ()
			@parameter:	None
			@notes:			takes dataColVal as URL stering to make GET resuest using apache library supported HttpRequest and HttpResponse
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
			 */	

			System.out.println(": Making GET request:-> "+data);
			APP_LOGS.debug(": Making GET request:-> "+data);
			highlight = false;
			
			try {
				data = data.trim();
				HashMap<String, String> headerMap = new HashMap<String, String>();
				headerMap.put("Content-type", "text/xml");
				headerMap.put("Accept", "text/xml");
				HttpResponse response = this.executeHttpGet(data, headerMap);
			
				StrGet = this.processResponse(response).toString();
				//StrGet = getMethodResponce;			
			}
			catch (Exception e) {
				highlight = true;
				System.out.println(": "+e.getMessage());
				return "FAIL - Not Able to make GET request " +data;
			}
			return "PASS";
		}
		
//***************** makeGetRequestDDT****************//
				public String makeGetRequestDDT(String data) throws ClientProtocolException, IOException {
					/* @HELP
					@class:			Keywords
					@method:		makeGetRequest ()
					@parameter:	None
					@notes:			takes dataColVal as URL stering to make GET resuest using apache library supported HttpRequest and HttpResponse in DDT manner.
					@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
					@END
					 */	

					System.out.println(": Making GET request:-> "+data);
					APP_LOGS.debug(": Making GET request:-> "+data);
					highlight = false;
					
					try {
						data = data.trim();
						HashMap<String, String> headerMap = new HashMap<String, String>();
						headerMap.put("Content-type", "text/xml");
						headerMap.put("Accept", "text/xml");
						HttpResponse response = this.executeHttpGet(data, headerMap);
						StrGet = this.processResponse(response).toString();
						//StrGet = getMethodResponce;			
					}
					catch (Exception e) {
						highlight = true;
						System.out.println(": "+e.getMessage());
						return "FAIL - Not Able to make GET request " +data;
					}
					return "PASS";
				}
//***************** makePostRequest****************//
		public String makePostRequest(String data) {
			/* @HELP
			@class:			Keywords
			@method:		VerifyPOSTRequestContent ()
			@parameter:	None
			@notes:			Makes POST request with attached data in form of file saved on HDD( in XMLForLT folder of the framework) using apache apache library supported HttpRequest and HttpResponse.
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
			 */	

			System.out.println(": Making POST request with XML DATA:-> "+data);
			APP_LOGS.debug(": Making POST request with XML DATA:-> "+data);			
			highlight = false;
			try {
					data = data.trim();
					String filePathFromInput = null;
					String twoDimentionArray[] = data.split(",");
					filePathFromInput = twoDimentionArray[1];
					System.out.println(twoDimentionArray[0]+" "+twoDimentionArray[1]);
					String dataR = this.readFile(xmlForLT+filePathFromInput).toString();
					//StringEntity params =new StringEntity("details={\"name\":\"myname\",\"age\":\"20\"} ");
					HashMap<String, String> headerMap = new HashMap<String, String>();
					headerMap.put("Content-type", "text/xml");
					headerMap.put("Accept", "text/xml");
					HttpResponse response = this.executeHttpPost(twoDimentionArray[0], headerMap, dataR);
					StrPost = this.processResponse(response).toString().trim();		
			}
			catch (Exception e) {
				highlight = true;
				System.out.println(": "+e.getMessage());
				return "FAIL - Not Able to verify " +data+ "in XML file";
			}
			return "PASS";
		}
		
		public String makePostRequestJSON(String data) {
			/* @HELP
			@class:			Keywords
			@method:		VerifyPOSTRequestContent ()
			@parameter:	None
			@notes:			Makes POST request with attached data in form of file saved on HDD( in XMLForLT folder of the framework which contains JSON file) using apache apache library supported HttpRequest and HttpResponse. 
							In dataColValue user must pass file path followed by URL e.g  https://offers.dev.lendingtree.com/formstore/submit-lead.ashx,/XMLForLT/LT_02_Verify_POST_API_JSON.json
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
			 */	

			System.out.println(": Making POST request with JSON DATA:-> "+data);
			APP_LOGS.debug(": Making POST request with JSON DATA:-> "+data);			
			highlight = false;
			try {
					data = data.trim();
					String filePathFromInput = null;
					String twoDimentionArray[] = data.split(",");
					filePathFromInput = twoDimentionArray[1];
					System.out.println(twoDimentionArray[0]+" "+twoDimentionArray[1]);
					String dataR = this.readFile(xmlForLT+filePathFromInput).toString();
					//StringEntity params =new StringEntity("details={\"name\":\"myname\",\"age\":\"20\"} ");
					HashMap<String, String> headerMap = new HashMap<String, String>();
					//headerMap.put("Content-type", "text/xml");
					headerMap.put("content-type", "application/json");
					headerMap.put("Accept", "text/xml");
					HttpResponse response = this.executeHttpPost(twoDimentionArray[0], headerMap, dataR);
					StrPost = this.processResponse(response).toString().trim();		
			}
			catch (Exception e) {
				highlight = true;
				System.out.println(": "+e.getMessage());
				return "FAIL - Not Able to procure " +data+ " JSON response";
			}
			return "PASS";
		}
		
		
		public String dragAndDropByCoordinates(String firstXpathKey,String data) {
			/* @HELP
			@class:			Keywords
			@method:		dragAndDropByCoordinates (data)
			@parameter:	None
			@notes:			Makes POST request with attached data in form of file saved on HDD( in XMLForLT folder of the framework which contains JSON file) using apache apache library supported HttpRequest and HttpResponse. 
							In dataColValue user must pass file path followed by URL e.g  https://offers.dev.lendingtree.com/formstore/submit-lead.ashx,/XMLForLT/LT_02_Verify_POST_API_JSON.json
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
			 */	

			System.out.println(": Performing drag and drop :-> "+data);
			APP_LOGS.debug(": Performing drag and drop :-> "+data);			
			highlight = false;
			try {
					data = data.trim();
					//String filePathFromInput = null;
					String twoDimentionArray[] = data.split(",");
					//filePathFromInput = twoDimentionArray[1];
					System.out.println(twoDimentionArray[0]+" "+twoDimentionArray[1]);
					//String dataR = this.readFile(xmlForLT+filePathFromInput).toString();
					Actions act=new Actions(driver);
					WebElement root=returnElementIfPresent(firstXpathKey);
					Integer x = new Integer(twoDimentionArray[0]);
					Integer y = new Integer(twoDimentionArray[1]);
					act.dragAndDropBy(root, x,y).perform();
					Thread.sleep(3000);
					//StringEntity params =new StringEntity("details={\"name\":\"myname\",\"age\":\"20\"} ");
					/*HashMap<String, String> headerMap = new HashMap<String, String>();
					//headerMap.put("Content-type", "text/xml");
					headerMap.put("content-type", "application/json");
					headerMap.put("Accept", "text/xml");
					HttpResponse response = this.executeHttpPost(twoDimentionArray[0], headerMap, dataR);
					StrPost = this.processResponse(response).toString().trim();	*/	
					
			}
			catch (Exception e) {
				highlight = true;
				System.out.println(": "+e.getMessage());
				return "FAIL - Not Able to perform drag and drop ";
			}
			return "PASS";
		}
		
		
		public String dragAndDropByElement(String firstXpathKey,String secondXpathKey) {
			/* @HELP
			@class:			Keywords
			@method:		dragAndDropByCoordinates (data)
			@parameter:	None
			@notes:			Makes POST request with attached data in form of file saved on HDD( in XMLForLT folder of the framework which contains JSON file) using apache apache library supported HttpRequest and HttpResponse. 
							In dataColValue user must pass file path followed by URL e.g  https://offers.dev.lendingtree.com/formstore/submit-lead.ashx,/XMLForLT/LT_02_Verify_POST_API_JSON.json
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
			 */	

			System.out.println(": Performing drag and drop :-> ");
			APP_LOGS.debug(": Performing drag and drop :-> ");				
			highlight = false;
			try {
					/*data = data.trim();
					String filePathFromInput = null;
					String twoDimentionArray[] = data.split(",");
					/*filePathFromInput = twoDimentionArray[1];
					System.out.println(twoDimentionArray[0]+" "+twoDimentionArray[1]);
					*///String dataR = this.readFile(xmlForLT+filePathFromInput).toString();
					Actions act=new Actions(driver);
					WebElement root=returnElementIfPresent(firstXpathKey);
					WebElement target=returnElementIfPresent(secondXpathKey);
					act.dragAndDrop(root, target).perform();
					Thread.sleep(3000);
					//StringEntity params =new StringEntity("details={\"name\":\"myname\",\"age\":\"20\"} ");
					/*HashMap<String, String> headerMap = new HashMap<String, String>();
					//headerMap.put("Content-type", "text/xml");
					headerMap.put("content-type", "application/json");
					headerMap.put("Accept", "text/xml");
					HttpResponse response = this.executeHttpPost(twoDimentionArray[0], headerMap, dataR);
					StrPost = this.processResponse(response).toString().trim();	*/	
					
			}
			catch (Exception e) {
				highlight = true;
				System.out.println(": "+e.getMessage());
				return "FAIL - Not Able to perform drag and drop ";
			}
			return "PASS";
		}
		
		
		public String uploadThroughAutoIT() {
			/* @HELP
			@class:			Keywords
			@method:		dragAndDropByCoordinates (data)
			@parameter:	None
			@notes:			Makes POST request with attached data in form of file saved on HDD( in XMLForLT folder of the framework which contains JSON file) using apache apache library supported HttpRequest and HttpResponse. 
							In dataColValue user must pass file path followed by URL e.g  https://offers.dev.lendingtree.com/formstore/submit-lead.ashx,/XMLForLT/LT_02_Verify_POST_API_JSON.json
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
			 */	

			System.out.println(": Performing uploading :-> ");
			APP_LOGS.debug(": Performing uploading :-> ");				
			highlight = false;
			try {
					/*data = data.trim();
					String filePathFromInput = null;
					String twoDimentionArray[] = data.split(",");
					/*filePathFromInput = twoDimentionArray[1];
					System.out.println(twoDimentionArray[0]+" "+twoDimentionArray[1]);
					*///String dataR = this.readFile(xmlForLT+filePathFromInput).toString();
					/*Actions act=new Actions(driver);
					WebElement root=returnElementIfPresent(firstXpathKey);
					WebElement target=returnElementIfPresent(secondXpathKey);
					act.dragAndDrop(root, target).perform();*/
					Runtime.getRuntime().exec("D://test.exe");
					Thread.sleep(3000);
					//StringEntity params =new StringEntity("details={\"name\":\"myname\",\"age\":\"20\"} ");
					/*HashMap<String, String> headerMap = new HashMap<String, String>();
					//headerMap.put("Content-type", "text/xml");
					headerMap.put("content-type", "application/json");
					headerMap.put("Accept", "text/xml");
					HttpResponse response = this.executeHttpPost(twoDimentionArray[0], headerMap, dataR);
					StrPost = this.processResponse(response).toString().trim();	*/	
					
			}
			catch (Exception e) {
				highlight = true;
				System.out.println(": "+e.getMessage());
				return "FAIL - Not Able to perform uploading :->  ";
			}
			return "PASS";
		}
		
		public String CloseTheChildWindow() {
			/* @HELP
			@class:			Keywords
			@method:		dragAndDropByCoordinates (data)
			@parameter:	None
			@notes:			Makes POST request with attached data in form of file saved on HDD( in XMLForLT folder of the framework which contains JSON file) using apache apache library supported HttpRequest and HttpResponse. 
							In dataColValue user must pass file path followed by URL e.g  https://offers.dev.lendingtree.com/formstore/submit-lead.ashx,/XMLForLT/LT_02_Verify_POST_API_JSON.json
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
			 */	

			System.out.println(": Closing Child Window");
			APP_LOGS.debug(": Closing Child Window");				
			highlight = false;
			try {
				String ParentWindow;
				 String ChildWindow1;
				 Set<String> set=driver.getWindowHandles();
				    Iterator<String> it=set.iterator();
				    ParentWindow=it.next();
				    ChildWindow1=it.next();
				    driver.switchTo().window(ChildWindow1);
				    Thread.sleep(2000);
				    driver.close();
				    driver.switchTo().window(ParentWindow);
					
			}
			catch (Exception e) {
				highlight = true;
				System.out.println(": "+e.getMessage());
				return "FAIL - Not Able to close Child Window";
			}
			return "PASS";
		}
		
		
		public String uploadFile(String fileLocation) throws Exception {
	        try {
	        	System.out.println(": Uploading a file from Specified location "+fileLocation);
	        	//System.out.println(": Uploading a file from Specified location "+fileLocation);
	        	APP_LOGS.debug(": Uploading a file from Specified location "+fileLocation);
	           // setClipboardData(fileLocation);
	            //native key strokes for CTRL, V and ENTER keys
	        	StringSelection stringSelection = new StringSelection(fileLocation);
	  		    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	            Robot robot = new Robot();
	            robot.keyPress(KeyEvent.VK_CONTROL);
	            Thread.sleep(2000);
	            robot.keyPress(KeyEvent.VK_V);
	            Thread.sleep(2000);
	            robot.keyRelease(KeyEvent.VK_V);
	            robot.keyRelease(KeyEvent.VK_CONTROL);
	            Thread.sleep(2000);
	            robot.keyPress(KeyEvent.VK_ENTER);
	            Thread.sleep(2000);
	            robot.keyRelease(KeyEvent.VK_ENTER);
	        }catch(RuntimeException localRuntimeException){
	        	highlight = true;
	    		System.out.println("Error in uploading a file from location: " + localRuntimeException.getMessage());
	    		return "FAIL - Error in uploading a file";
	        	//throw new AutomationException("Error in uploading a file from location: " + localRuntimeException.getMessage());
	    	}
	        return "PASS";
	    }
		
		public String saveFile() throws Exception {
	        try {
	        	
	        	// Add the code for deleting file at the downloads location.
	        	
	        	/*File file = new File("C:\\Users\\dhruval.patel\\Downloads\\Contracts.pdf");
	        	
	        	if(file.exists()){
	        		//System.out.println("Inside file exists block");
	        		file.delete();
	        	}*/
	        	Thread.sleep(2000);
	        	System.out.println(": saving a file ");
	        	//System.out.println(": Uploading a file from Specified location "+fileLocation);
	        	APP_LOGS.debug(": saving a file ");
	           // setClipboardData(fileLocation);
	            //native key strokes for CTRL, V and ENTER keys
	        	//StringSelection stringSelection = new StringSelection(fileLocation);
	  		    //Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	            Robot robot = new Robot();
	            /*robot.keyPress(KeyEvent.VK_CONTROL);
	            Thread.sleep(2000);
	            robot.keyPress(KeyEvent.VK_V);
	            Thread.sleep(2000);
	            robot.keyRelease(KeyEvent.VK_V);
	            robot.keyRelease(KeyEvent.VK_CONTROL);
	            Thread.sleep(2000);*/
	            robot.keyPress(KeyEvent.VK_ENTER);
	            //Thread.sleep(1000);
	            robot.keyRelease(KeyEvent.VK_ENTER);
	        }catch(RuntimeException  exception){
	        	highlight = true;
	    		System.out.println("Error in saving a file: " + exception.getMessage());
	    		return "FAIL - Error in saving a file";
	        	//throw new AutomationException("Error in uploading a file from location: " + localRuntimeException.getMessage());
	    	}
	        
	        return "PASS";
	    }
		
		public String DeleteFile(String path) throws Exception {
	        try {
	        	
	        	// Add the code for deleting file at the downloads location.
	        	
	        	File file = new File(path);
	        	
	        	if(file.exists()){
	        		//System.out.println("Inside file exists block");
	        		file.delete();
	        	}
	        	Thread.sleep(2000);
	        	System.out.println(": Deleted a file ");
	        	//System.out.println(": Uploading a file from Specified location "+fileLocation);
	        	APP_LOGS.debug(": Deleted a file ");
	           
	        }catch(RuntimeException  exception){
	        	highlight = true;
	    		System.out.println("Error in deleting a file: " + exception.getMessage());
	    		return "FAIL - Error in deleting a file";
	        	//throw new AutomationException("Error in uploading a file from location: " + localRuntimeException.getMessage());
	    	}
	        
	        return "PASS";
	    }
		
		
		
		
		public String VerifyFileDownload(String dataColValue) throws Exception {
	        try {
	        	
	        	// Add the code for deleting file at the downloads location.
	        	dataColValue.replaceAll("\\\\", "/");
	        	File file = new File(dataColValue);
	        	
	        	if(file.exists()){
	        		//System.out.println("Inside file checking block");
	        		System.out.println(": File is downloaded successfull at:-> "+dataColValue+ " path");
	        		APP_LOGS.debug(": File is downloaded successfull at:-> "+dataColValue+ " path");
	        	}else{
	        		highlight = true;
	        		System.out.println(": File is not present at the location ");
	        		APP_LOGS.debug(":  File is not present at the location ");
	        		return "FAIL - File is not present at the location ";
	        	}
	        	
	        	/*System.out.println(": saving a file ");
	        	//System.out.println(": Uploading a file from Specified location "+fileLocation);
	        	APP_LOGS.debug(": saving a file ");*/
	           
	        }catch(Exception  exception){
	        	highlight = true;
	    		System.out.println("Error in saving a file: " + exception.getMessage());
	    		return "FAIL - Error in saving a file";
	        	//throw new AutomationException("Error in uploading a file from location: " + localRuntimeException.getMessage());
	    	}
	        
	        return "PASS";
	    }
		
		public String SwitchToPdfWindowAndVerifyText(String firstXpathKey, String secondXpathKey, String expText) throws ParseException {
	/*@HELP
	@class:			Keywords
	@method:		SwitchToPdfWindowAndVerifyText ()
	@parameter:	String firstXpathKey, Optional=>String secondXpathKey, Optional=> String expText
	@notes:			Switches to child window and Verifies the Actual Text as compared to the Expected Text. Verification can be performed on the same page or on different pages. User can perform two different webelement's text comparision by  passing argument as objectKeySecond.
	@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
	@END
	 */
	
	//System.out.println("----------------------------------------------- : "+returnElementIfPresent(firstXpathKey).isDisplayed());
	//System.out.println("----------------------##------------------------- : "+returnElementIfPresent(firstXpathKey).getText());
		highlight = false;
		System.out.println(": Verifying " + firstXpathKey + " Text on the Page");
		APP_LOGS.debug(": Verifying " + firstXpathKey + " Text on the Page");
		
				 
		
		
		String regex  = "[0-9].[0-9]";
		if (expText.matches(regex)) {
			NumberFormat nf = NumberFormat.getInstance();
			Number number = nf.parse(expText);
			long lnputValue = number.longValue();
			expText = String.valueOf(lnputValue);
		}
		if (expText.isEmpty()) {
			getTextOrValues.put(secondXpathKey, returnElementIfPresent(secondXpathKey).getText());
			expText = getTextOrValues.get(secondXpathKey).toString();
		}		
		try {	

				 String ParentWindow;
				 String ChildWindow1;
				 Set<String> set=driver.getWindowHandles();
				    Iterator<String> it=set.iterator();
				    ParentWindow=it.next();
				    ChildWindow1=it.next();
				    driver.switchTo().window(ChildWindow1);
				    Thread.sleep(2000);

					
			getTextOrValues.put(firstXpathKey, returnElementIfPresent(firstXpathKey).getText());
			actText = getTextOrValues.get(firstXpathKey).toString();		
			actText=actText.trim();
			expText=expText.trim();

			if (actText.compareTo(expText) == 0) {
				System.out.println(": Actual is-> " + actText + " AND Expected is-> " + expText);
				APP_LOGS.debug(": Actual is-> " + actText + " AND Expected is->" + expText);
			} else {
				globalExpText=expText;
				System.out.println(": Actual is-> " + actText + " AND Expected is-> " + expText);
				return "FAIL - Actual is-> " + actText + " AND Expected is->" + expText;
			}
			
		} catch (Exception e) {
			highlight = true;
			return "FAIL - Not able to read text--" + firstXpathKey;
		}
		return "PASS";
	}

		public String VerifyElementIsEditable(String firstXpathKey) throws Exception {
	        
			try {
	        	
				if(returnElementIfPresent(firstXpathKey).isEnabled()){
					System.out.println(": Element is editable "+firstXpathKey);
					APP_LOGS.debug(": Element is editable "+firstXpathKey);
					highlight = true;
				}else{
					System.out.println(": Element is not editable "+firstXpathKey);
					APP_LOGS.debug(":  Element is not editable "+firstXpathKey);
					return "FAIL - Element is not editable ";
        	}
	        	
	        }catch(Exception  exception){
	        	highlight = true;
	    		System.out.println("Error in saving a file: " + exception.getMessage());
	    		return "FAIL - Error in saving a file";
	        	//throw new AutomationException("Error in uploading a file from location: " + localRuntimeException.getMessage());
	    	}
	        
	        return "PASS";
	    }
		
		public String deleteFilesFromFolder(String filePath){

			String directoryName = "C:\\Users\\dhruval.patel\\Downloads";
			try{
				File directory = new File(directoryName);
						
			if(directory.isDirectory()){

				for(int i = 0;i <directory.list().length;i++){
					File file = new File(directory+"\\"+directory.list()[i]);
					file.delete();
				}
				
			}else{
				System.out.println("Parent Directory has not anything.");
			}
			System.out.println("Successfully deleted directory : "+directoryName);
			APP_LOGS.debug("Successfully deleted directory : "+directoryName);
		}catch(Exception ex){
			highlight = true;
    		System.out.println("Error in deleting contents of the directory : "+directoryName+" with exception "+ex.getMessage());
    		return "FAIL - Error in deleting contents of the directory : "+directoryName;
		}
			return "PASS";
		}
	public String verifyFileIsDownloaded() {

		String directoryName = "C:\\Users\\dhruval.patel\\Downloads";
		try{
		File directory = new File(directoryName);

		// System.out.println(directory.getName());
		if (directory.isDirectory()) {
			for (int i = 0; i < directory.list().length; i++) {
				File file = new File(directory + "\\" + directory.list()[i]);
				if (file.getName().contains(".pdf")
						|| file.getName().contains(".xml")) {
					System.out
							.println("Successfully executed with file name of : "
									+ file.getName());
				} else {
					System.out
							.println("directory has files with different file extention or folders.");
				}
			}
		} else {
			System.out.println("It is not a directory.");
		}
		System.out.println("Successfully verified files : "+directoryName);
		APP_LOGS.debug("Successfully verified files : "+directoryName);
		}catch(Exception ex){
			highlight = true;
    		System.out.println("Error in verifing contents of the directory : "+directoryName+" with exception "+ex.getMessage());
    		return "FAIL - Error in verifing contents of the directory : "+directoryName;
		}
		return "PASS";
	}
}