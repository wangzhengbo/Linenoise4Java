package cn.com.linenoise;

public class LinenoiseExample {
	public static void main(String[] args) throws Exception {

		final Linenoise lineNoise = new Linenoise();

		/* Parse options, with --multiline we enable multi line editing. */
		for (String arg : args) {
			if (arg.trim().length() == 0) {
				continue;
			}
			if ("--multiline".equals(arg)) {
				lineNoise.setMultiLine(true);
				System.out.printf("Multi-line mode enabled.%n");
			} else if ("--keycodes".equals(arg)) {
				lineNoise.printKeyCodes();
				System.exit(0);
			} else {
				System.err.printf(
						"Usage: java %s [--multiline] [--keycodes]%n",
						LinenoiseExample.class.getName());
				System.exit(1);
			}
		}

		/*
		 * Set the completion callback. This will be called every time the user
		 * uses the <tab> key.
		 */
		lineNoise.setCompletionCallback(new CompletionCallback() {
			public void callback(String buf, Completions completions) {
				if ((buf.length() > 0) && (buf.charAt(0) == 'h')) {
					lineNoise.addCompletion(completions, "hello");
					lineNoise.addCompletion(completions, "hello there");
				}
			}
		});

		/*
		 * Load history from file. The history file is just a plain text file
		 * where entries are separated by newlines.
		 */
		lineNoise.historyLoad("examples/history.txt"); /* Load the history at startup */

		System.out.println(String.format("Type exit to quit the %s.",
				LinenoiseExample.class.getSimpleName()));

		/*
		 * Now this is the main loop of the typical linenoise-based application.
		 * The call to linenoise() will block as long as the user types
		 * something and presses enter.
		 * 
		 * The typed string is returned as a malloc() allocated string by
		 * linenoise, so the user needs to free() it.
		 */
		String line;
		while ((line = lineNoise.readLine("hello> ")) != null) {
			if ("exit".equals(line)) {
				break;
			}
			if (line.length() > 0) {
				/* Do something with the string. */
				if (line.charAt(0) != '/') {
					System.out.printf("echo: '%s'%n", line);
				} else if (line.startsWith("/historylen")) {
					/* The "/historylen" command will change the history len. */
					int len = Integer.parseInt(line.substring(11));
					lineNoise.historySetMaxLen(len);
				} else {
					System.out.printf("Unreconized command: %s%n", line);
				}
				lineNoise.historyAdd(line); /* Add to the history. */
				lineNoise.historySave("examples/history.txt"); /* Save the history on disk. */
			}
		}
	}
}
