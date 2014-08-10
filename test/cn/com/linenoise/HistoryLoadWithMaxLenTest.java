package cn.com.linenoise;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class HistoryLoadWithMaxLenTest extends BaseTest {
	@Test
	public void historyLoadWithMaxLen() {
		Assert.assertTrue(lineNoise.historySetMaxLen(3));

		String fileName = "test/historyLoad.txt";
		File file = new File(fileName);
		Assert.assertTrue(file.exists());
		Assert.assertFalse(lineNoise
				.historyLoad("test/notExistsHistoryLoad.txt"));
		Assert.assertTrue(lineNoise.historyLoad(file));

		File tmpHistoryLoad = new File("test/historyLoad.tmp");
		try {
			Assert.assertTrue(lineNoise.historyAdd("four"));
			lineNoise.historySave(tmpHistoryLoad);
			Assert.assertArrayEquals(new String[] { "three", "你好", "four" },
					readLines(tmpHistoryLoad.getAbsolutePath()));
		} finally {
			if (tmpHistoryLoad.exists()) {
				Assert.assertTrue(tmpHistoryLoad.delete());
			}
		}
	}
}
