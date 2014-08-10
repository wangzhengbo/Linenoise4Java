package cn.com.linenoise;

import org.junit.Test;

public class ReadLineTest extends BaseTest {
	@Test
	public void readLine() {
		System.out.println(String.format(
				"%nType exit to quit the %s, type clear to clear screen.",
				getClass().getSimpleName()));
		while (true) {
			String line = lineNoise.readLine("> ");
			if (line == null || "exit".equals(line)) {
				break;
			}
			if ("clear".equals(line)) {
				lineNoise.clearScreen();
			}
			lineNoise.historyAdd(line);
		}
	}
}
