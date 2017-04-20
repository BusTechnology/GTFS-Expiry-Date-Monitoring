package util;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by tony on 4/19/17.
 */
public class ZipFileReaderTest {
    @Test
    public void getMaxDate() throws Exception {

        File file;

        file = new File("src/test/resources/GTFS_MTABC_B7.zip");
        int maxEndDate = ZipFileReader.getMaxDate(file);
        Assert.assertEquals(20170701 , maxEndDate);

        file = new File("src/test/resources/GTFS_SURFACE_B_2017-04-09_REV2017-03-13_353.zip");
        maxEndDate = ZipFileReader.getMaxDate(file);
        Assert.assertEquals(20170701 , maxEndDate);

    }

}