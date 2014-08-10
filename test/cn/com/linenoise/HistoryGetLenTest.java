package cn.com.linenoise;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class HistoryGetLenTest extends BaseTest {
	@Test
	public void historyGetLen() {
		String fileName = "test/historySetMaxLen.txt";
		File file = new File(fileName);
		if (file.exists()) {
			Assert.assertTrue(file.delete());
		}

		try {
			// Set max history length to 3
			Assert.assertEquals(Linenoise.DEFAULT_HISTORY_MAX_LEN,
					lineNoise.historyGetMaxLen());
			Assert.assertEquals(0, lineNoise.historyGetLen());

			Assert.assertFalse(lineNoise.historySetMaxLen(-1));
			Assert.assertEquals(Linenoise.DEFAULT_HISTORY_MAX_LEN,
					lineNoise.historyGetMaxLen());
			Assert.assertEquals(0, lineNoise.historyGetLen());

			Assert.assertFalse(lineNoise.historySetMaxLen(0));
			Assert.assertEquals(Linenoise.DEFAULT_HISTORY_MAX_LEN,
					lineNoise.historyGetMaxLen());
			Assert.assertEquals(0, lineNoise.historyGetLen());

			Assert.assertTrue(lineNoise.historySetMaxLen(1));
			Assert.assertEquals(1, lineNoise.historyGetMaxLen());
			Assert.assertEquals(0, lineNoise.historyGetLen());

			Assert.assertTrue(lineNoise.historySetMaxLen(3));
			Assert.assertEquals(3, lineNoise.historyGetMaxLen());
			Assert.assertEquals(0, lineNoise.historyGetLen());

			Assert.assertTrue(lineNoise.historyAdd("one"));
			Assert.assertEquals(1, lineNoise.historyGetLen());
			Assert.assertTrue(lineNoise.historyAdd("two"));
			Assert.assertEquals(2, lineNoise.historyGetLen());
			Assert.assertTrue(lineNoise.historyAdd("three"));
			Assert.assertEquals(3, lineNoise.historyGetLen());
			Assert.assertTrue(lineNoise.historySave(fileName));
			Assert.assertArrayEquals(new String[] { "one", "two", "three" },
					readLines(fileName));

			Assert.assertTrue(lineNoise.historyAdd("four"));
			Assert.assertEquals(3, lineNoise.historyGetLen());
			Assert.assertTrue(lineNoise.historySave(fileName));
			Assert.assertArrayEquals(new String[] { "two", "three", "four" },
					readLines(fileName));

			Assert.assertTrue(lineNoise.historyAdd("five"));
			Assert.assertEquals(3, lineNoise.historyGetLen());
			Assert.assertTrue(lineNoise.historyAdd("six"));
			Assert.assertEquals(3, lineNoise.historyGetLen());
			Assert.assertTrue(lineNoise.historySave(fileName));
			Assert.assertArrayEquals(new String[] { "four", "five", "six" },
					readLines(fileName));

			Assert.assertTrue(lineNoise.historyAdd("seven"));
			Assert.assertEquals(3, lineNoise.historyGetLen());
			Assert.assertTrue(lineNoise.historyAdd("eight"));
			Assert.assertEquals(3, lineNoise.historyGetLen());
			Assert.assertTrue(lineNoise.historyAdd("nine"));
			Assert.assertEquals(3, lineNoise.historyGetLen());
			Assert.assertTrue(lineNoise.historyAdd("ten"));
			Assert.assertEquals(3, lineNoise.historyGetLen());
			Assert.assertTrue(lineNoise.historySave(fileName));
			Assert.assertArrayEquals(new String[] { "eight", "nine", "ten" },
					readLines(fileName));

			Assert.assertTrue(lineNoise.historySetMaxLen(2));
			Assert.assertEquals(2, lineNoise.historyGetLen());
			Assert.assertTrue(lineNoise.historyAdd("eleven"));
			Assert.assertEquals(2, lineNoise.historyGetLen());
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
