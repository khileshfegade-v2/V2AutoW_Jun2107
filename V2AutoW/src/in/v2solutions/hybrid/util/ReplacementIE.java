package in.v2solutions.hybrid.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;

public class ReplacementIE extends Constants{

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		
		File strTarget = new File(configxlsPath+"Config.xlsm");
		File cfgFilePath = new File(rootPath+"\\temp\\IE\\Config.xlsm");

		IOUtils.copy(new FileInputStream(cfgFilePath), new FileOutputStream(strTarget));
		// copy file and preserve the time stamp. the sourceFile and destFile are of type java.io.File
		FileUtils.copyFile(cfgFilePath,strTarget);

	}

}
