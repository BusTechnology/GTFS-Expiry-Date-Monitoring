package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.simpleflatmapper.csv.CsvParser;

import model.CalendarModel;

public class ZipFileReader {

	private static int MAX_END_DATE;

	public static List<String> getFileName(String path) {
		List<String> result = new ArrayList<String>();

		File f = new File(path);
		if (!f.exists()) {
			System.out.println(path + " not exists");
			return null;
		}

		File fa[] = f.listFiles();
		for (int i = 0; i < fa.length; i++) {
			File fs = fa[i];
			if (!fs.isDirectory()) result.add(fs.getName());
		}
		return result;
	}

	public static void getZipFileContent(File zipFile, String readFileName) throws ZipException, IOException {  
		ZipFile zip = new ZipFile(zipFile);  
		@SuppressWarnings("unchecked")
		Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();  

		ZipEntry ze;  
		while (entries.hasMoreElements()) {  
			ze = entries.nextElement();  
			MAX_END_DATE = 0;
			if (ze.getName().equals(readFileName)) {  
				InputStream is = zip.getInputStream(ze);
				CsvParser.mapTo(CalendarModel.class)
				.forEach(new InputStreamReader(is)
						, r -> {
							int endDate = r.getEnd_date();
							if(endDate > MAX_END_DATE) MAX_END_DATE = endDate;
						});
				int currentDate = getCurrentDate();
				if(MAX_END_DATE - currentDate <= 7) 
					SNSNotification.sendSNSNotification(zipFile.getName(), MAX_END_DATE);
			}
		}  
		zip.close();    
	} 

	public static void deleteFile(File file) {
		for(File childFile : file.listFiles()){
			if(childFile.getPath().matches(".*\\d{10,}.*")) childFile.delete();
		}
	}
	
	public static int getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		dateFormat.setTimeZone(TimeZone.getTimeZone("America/New York"));
		int currentDate = Integer.valueOf(dateFormat.format(date));	
		return currentDate;
	}
}
