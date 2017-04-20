package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.simpleflatmapper.csv.CsvParser;

import model.*;

public class ZipFileReader {

	private static int maxEndDate;

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

	public static int getMaxDate(File zipFile) throws IOException {
		ZipFile zip = new ZipFile(zipFile);
		@SuppressWarnings("unchecked")
		Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
		maxEndDate = 0;

		ZipEntry ze;
		while (entries.hasMoreElements()) {
			ze = entries.nextElement();

			if (ze.getName().equals("calendar.txt")) {
				InputStream is = zip.getInputStream(ze);
				CsvParser.mapTo(CalendarModel.class)
						.forEach(new InputStreamReader(is)
								, r -> {
									if (r.getEnd_date() > maxEndDate) {
										maxEndDate = r.getEnd_date();
									}
								});
			}
			else if (ze.getName().equals("calendar_dates.txt")) {
				InputStream is = zip.getInputStream(ze);
				CsvParser.mapTo(CalendarDateModel.class)
						.forEach(new InputStreamReader(is)
								, r -> {
									if (r.getDate() > maxEndDate) {
										maxEndDate = r.getDate();
									}
								});

			}
		}
		zip.close();
		if (maxEndDate == 0){
			throw new IllegalStateException("Did not find an end date.");
		}
		return maxEndDate;
	}
	public static void deleteFile(File file) {
		if (file.listFiles() != null){
			for(File childFile : file.listFiles()){
				if(childFile.getPath().matches(".*\\d{10,}.*")) childFile.delete();
			}
		}
	}
	

}
