package cn.com.linenoise;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class HistoryAddTest extends BaseTest {
	@Test
	public void historyAdd() {
		String hello = "你好";

		String fileName = "test/historyAdd.txt";
		File file = new File(fileName);
		if (file.exists()) {
			Assert.assertTrue(file.delete());
		}

		try {
			Assert.assertTrue(lineNoise.historyAdd("one"));
			Assert.assertTrue(lineNoise.historyAdd("two"));
			// Can't add duplicated lines
			Assert.assertFalse(lineNoise.historyAdd("two"));
			Assert.assertTrue(lineNoise.historyAdd(null));
			Assert.assertTrue(lineNoise.historyAdd(" "));
			Assert.assertTrue(lineNoise.historyAdd("three"));
			Assert.assertTrue(lineNoise.historySave(fileName));
			Assert.assertArrayEquals(new String[] { "one", "two", "", " ",
					"three" }, readLines(fileName));

			Assert.assertTrue(lineNoise.historyAdd(hello));
			Assert.assertArrayEquals(new String[] { "one", "two", "", " ",
					"three" }, readLines(fileName));
			Assert.assertTrue(lineNoise.historySave(fileName));
			Assert.assertArrayEquals(new String[] { "one", "two", "", " ",
					"three", hello }, readLines(fileName));

			// Set max history length to 9
			Assert.assertEquals(Linenoise.DEFAULT_HISTORY_MAX_LEN,
					lineNoise.historyGetMaxLen());
			Assert.assertTrue(lineNoise.historySetMaxLen(9));
			Assert.assertEquals(9, lineNoise.historyGetMaxLen());
			Assert.assertArrayEquals(new String[] { "one", "two", "", " ",
					"three", hello }, readLines(fileName));

			Assert.assertTrue(lineNoise.historySave(fileName));
			Assert.assertArrayEquals(new String[] { "one", "two", "", " ",
					"three", hello }, readLines(fileName));

			Assert.assertTrue(lineNoise.historyAdd("four"));
			Assert.assertTrue(lineNoise.historyAdd("five"));
			Assert.assertTrue(lineNoise.historySave(fileName));
			Assert.assertArrayEquals(new String[] { "one", "two", "", " ",
					"three", hello, "four", "five" }, readLines(fileName));

			Assert.assertTrue(lineNoise.historyAdd("six"));
			Assert.assertTrue(lineNoise.historySave(fileName));
			Assert.assertArrayEquals(new String[] { "one", "two", "", " ",
					"three", hello, "four", "five", "six" },
					readLines(fileName));

			Assert.assertTrue(lineNoise.historyAdd("seven"));
			Assert.assertTrue(lineNoise.historySave(fileName));
			Assert.assertArrayEquals(new String[] { "two", "", " ", "three",
					hello, "four", "five", "six", "seven" },
					readLines(fileName));
		} finally {
			if (file.exists()) {
				Assert.assertTrue(file.delete());
			}
		}
	}
}
