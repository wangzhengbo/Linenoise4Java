package cn.com.linenoise;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class HistorySetMaxLenTest extends BaseTest {
	@Test
	public void historySetMaxLen() {
		String fileName = "test/historySetMaxLen.txt";
		File file = new File(fileName);
		if (file.exists()) {
			Assert.assertTrue(file.delete());
		}

		try {
			// Set max history length to 3
			Assert.assertEquals(Linenoise.DEFAULT_HISTORY_MAX_LEN,
					lineNoise.historyGetMaxLen());

			Assert.assertFalse(lineNoise.historySetMaxLen(-1));
			Assert.assertEquals(Linenoise.DEFAULT_HISTORY_MAX_LEN,
					lineNoise.historyGetMaxLen());

			Assert.assertFalse(lineNoise.historySetMaxLen(0));
			Assert.assertEquals(Linenoise.DEFAULT_HISTORY_MAX_LEN,
					lineNoise.historyGetMaxLen());

			Assert.assertTrue(lineNoise.historySetMaxLen(1));
			Assert.assertEquals(1, lineNoise.historyGetMaxLen());

			Assert.assertTrue(lineNoise.historySetMaxLen(3));
			Assert.assertEquals(3, lineNoise.historyGetMaxLen());

			Assert.assertTrue(lineNoise.historyAdd("one"));
			Assert.assertTrue(lineNoise.historyAdd("two"));
			Assert.assertTrue(lineNoise.historyAdd("three"));
			Assert.assertTrue(lineNoise.historySave(fileName));
			Assert.assertArrayEquals(new String[] { "one", "two", "three" },
					readLines(fileName));

			Assert.assertTrue(lineNoise.historyAdd("four"));
			Assert.assertTrue(lineNoise.historySave(fileName));
			Assert.assertArrayEquals(new String[] { "two", "three", "four" },
					readLines(fileName));

			Assert.assertTrue(lineNoise.historyAdd("five"));
			Assert.assertTrue(lineNoise.historyAdd("six"));
			Assert.assertTrue(lineNoise.historySave(fileName));
			Assert.assertArrayEquals(new String[] { "four", "five", "six" },
					readLines(fileName));

			Assert.assertTrue(lineNoise.historyAdd("seven"));
			Assert.assertTrue(lineNoise.historyAdd("eight"));
			Assert.assertTrue(lineNoise.historyAdd("nine"));
			Assert.assertTrue(lineNoise.historyAdd("ten"));
			Assert.assertTrue(lineNoise.historySave(fileName));
			Assert.assertArrayEquals(new String[] { "eight", "nine", "ten" },
					readLines(fileName));

			Assert.assertTrue(lineNoise.historySetMaxLen(2));
			Assert.assertTrue(lineNoise.historyAdd("eleven"));
			Assert.assertTrue(lineNoise.historySave(fileName));
			Assert.assertArrayEquals(new String[] { "ten", "eleven" },
					readLines(fileName));
		} finally {
			if (file.exists()) {
				Assert.assertTrue(file.delete());
			}
		}
	}
}
