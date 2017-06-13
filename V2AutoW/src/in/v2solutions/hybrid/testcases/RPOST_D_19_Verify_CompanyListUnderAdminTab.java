package in.v2solutions.hybrid.testcases;

import in.v2solutions.hybrid.util.Keywords;
import in.v2solutions.hybrid.util.TestUtil;
import in.v2solutions.hybrid.util.Constants;
import java.util.Hashtable;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


 public class RPOST_D_19_Verify_CompanyListUnderAdminTab {

 String TCName = "RPOST_D_19_Verify_CompanyListUnderAdminTab";

 String lastTestCaseName = "RPOST_D_02_Verify_UserCanLoginWithValidCredentials";


 String as = "Last Test Case Quit";

 int runModecounter = Keywords.xls.getCellRowNum("Test Data","DDTCIDWithRunMode",TCName)+2;

 @Parameters({ "Suite-Name" })
@BeforeTest
public void beforeTest(@Optional String Suitename) {

String Actsuitename = Suitename;
	if (Actsuitename != null) 
	{
		Keywords.tsName = Actsuitename;
	}
	else 
	{
		Keywords.tcName = TCName;
	}
}


 @Test(dataProvider = "getTestData")
public void verify_CompanyListUnderAdminTab(Hashtable<String, String> data)throws Exception {
if (!TestUtil.isTestCaseExecutable(TCName,Keywords.xls))
	throw new SkipException("Skipping the test as runmode is NO");
	{

if(getTestData().length > 1) {
	String YorN = Keywords.xls.getCellData("Test Data",0,runModecounter);
System.out.println(YorN);
if (YorN.equals("N")){
runModecounter = runModecounter+1;
	throw new SkipException("Skipping the test as runmode is NO DDT");
}
	runModecounter = runModecounter+1;
	}

Keywords k = Keywords.getKeywordsInstance();
	k.executeKeywords(TCName, data);
	}

	}

@AfterTest
public void afterTest() {
		System.out.println("Inside After Test ");
 if (TCName.equals(lastTestCaseName))
	  { System.out.println(as);
	try{  
Constants.driver.quit();
}catch(Exception e){
  Constants.driver = null;
}
Constants.driver = null;
	}

	}


 @DataProvider
public Object[][] getTestData() {
return TestUtil.getData(TCName, Keywords.xls);
}
}
