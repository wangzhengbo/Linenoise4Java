package cn.com.linenoise;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public class Linenoise {
	// #define LINENOISE_DEFAULT_HISTORY_MAX_LEN 100
	public static final int DEFAULT_HISTORY_MAX_LEN = 100;

	/* linenoise library name */
	private static final String LIB_LINENOISE = "linenoise";

	/* linenoise library path */
	private static final String DLL_LIB_RESOURCE_PATH = "/"
			+ Linenoise.class.getPackage().getName().replaceAll("\\.", "/");

	private LinenoiseLibrary lineNoise = null;
	private int historyMaxLen = DEFAULT_HISTORY_MAX_LEN;
	@SuppressWarnings("unused")
	private CompletionCallback completionCallback = null;

	private static final String ENV_CONFIG_OS = "CONFIG_OS";

	private static final String OS_NAME_AIX = "AIX";
	private static final String OS_NAME_GNU = "gnu";
	private static final String OS_NAME_FREEBSD = "FreeBSD";
	private static final String OS_NAME_KFREEBSD = "GNU/kFreeBSD";
	private static final String OS_NAME_NETBSD = "NetBSD";
	private static final String OS_NAME_DRAGONFLYBSD = "DragonFly";
	private static final String OS_NAME_MIDNIGHTBSD = "MidnightBSD";

	private static final int AIX = 7;
	private static final int ANDROID = 8;
	private static final int GNU = 9;
	private static final int KFREEBSD = 10;
	private static final int NETBSD = 11;
	private static final int DRAGONFLYBSD = 1000;

	static {
		if (OS_NAME_MIDNIGHTBSD.equalsIgnoreCase(System.getProperty("os.name"))) {
			System.setProperty("os.name", OS_NAME_FREEBSD);
		}
		System.setProperty("jna.library.path",
				System.getProperty("java.io.tmpdir"));
	}

	public Linenoise() throws Exception {
		// Initialize Linenoise
		lineNoise = loadNativeLibrary();
	}

	/**
	 * This method is used by the callback function registered by the user in
	 * order to add completion options given the input string when the user
	 * typed <tab>. See the example.c source code for a very easy to understand
	 * example.
	 * 
	 * @param completions
	 * @param str
	 */
	public void addCompletion(Completions completions, String str) {
		lineNoise.linenoiseAddCompletion(completions, str);
	}

	/**
	 * Clear screen.
	 */
	public void clearScreen() {
		lineNoise.linenoiseClearScreen();
	}

	/**
	 * This is the API call to add a new entry in the linenoise history. It uses
	 * a fixed array of char pointers that are shifted (memmoved) when the
	 * history max length is reached in order to remove the older entry and make
	 * room for the new one, so it is not exactly suitable for huge histories,
	 * but will work well for a few hundred of entries.
	 * 
	 * Using a circular buffer is smarter, but a bit more complex to handle.
	 */
	public boolean historyAdd(String line) {
		return (lineNoise.linenoiseHistoryAdd(defaultString(line)) != 0);
	}

	/**
	 * Returns the history identified by its progressive index (starting from
	 * zero) or null if the index does not exist.
	 * 
	 * @param index
	 *            progressive index (starting from zero)
	 * @return Returns the history identified by its progressive index (starting
	 *         from zero) or null if the index does not exist.
	 */
	public String historyGet(int index) {
		return lineNoise.linenoiseHistoryGet(index);
	}

	/**
	 * Get the current history length.
	 * 
	 * @return Returns the current history length.
	 */
	public int historyGetLen() {
		return lineNoise.linenoiseHistoryGetLen();
	}

	/**
	 * Load the history from the specified file. If the file does not exist zero
	 * is returned and no operation is performed.
	 * 
	 * @param file
	 * @return If the file exists and the operation succeeded true is returned,
	 *         otherwise on error false is returned.
	 */
	public boolean historyLoad(File file) {
		return (lineNoise.linenoiseHistoryLoad(file.getAbsolutePath()) == 0);
	}

	/**
	 * Load the history from the specified file. If the file does not exist zero
	 * is returned and no operation is performed.
	 * 
	 * @param fileName
	 * @return If the file exists and the operation succeeded true is returned,
	 *         otherwise on error false is returned.
	 */
	public boolean historyLoad(String fileName) {
		return historyLoad(new File(fileName));
	}

	/**
	 * Save the history in the specified file.
	 * 
	 * @param file
	 * @return Returns true if success, otherwise returns false.
	 */
	public boolean historySave(File file) {
		return historySave(file.getAbsolutePath());
	}

	/**
	 * Save the history in the specified file.
	 * 
	 * @param fileName
	 * @return Returns true if success, otherwise returns false.
	 */
	public boolean historySave(String fileName) {
		return (lineNoise.linenoiseHistorySave(fileName) == 0);
	}

	/**
	 * Get the maximum length for the history.
	 * 
	 * @return Returns the maximum length for the history.
	 */
	public int historyGetMaxLen() {
		return historyMaxLen;
	}

	/**
	 * Set the maximum length for the history. This method can be called even if
	 * there is already some history, the method will make sure to retain just
	 * the latest 'len' elements if the new history length value is smaller than
	 * the amount of items already inside the history.
	 * 
	 * @param len
	 * @return Returns true if success, otherwise returns false.
	 */
	public boolean historySetMaxLen(int len) {
		boolean status = (lineNoise.linenoiseHistorySetMaxLen(len) != 0);
		if (status) {
			historyMaxLen = len;
		}
		return status;
	}

	/**
	 * This special mode is used by linenoise in order to print scan codes on
	 * screen for debugging / development purposes. It is implemented by the
	 * linenoise_example program using the --keycodes option.
	 */
	public void printKeyCodes() {
		checkOperation();
		lineNoise.linenoisePrintKeyCodes();
	}

	/**
	 * The high level method that is the main API of the linenoise library. This
	 * method checks if the terminal has basic capabilities, just checking for a
	 * blacklist of stupid terminals, and later either calls the line editing
	 * function or uses dummy fgets() so that you will be able to type something
	 * even in the most desperate of the conditions.
	 * 
	 * @return Returns the entered line if success, otherwise return null.
	 */
	public String readLine(String prompt) {
		return lineNoise.linenoise(defaultString(prompt));
	}

	/**
	 * Register a callback function to be called for tab-completion.
	 * 
	 * @param fn
	 */
	public void setCompletionCallback(CompletionCallback fn) {
		completionCallback = fn;
		lineNoise.linenoiseSetCompletionCallback(fn);
	}

	/**
	 * Set if to use or not the multi line mode.
	 * 
	 * @param ml
	 */
	public void setMultiLine(boolean ml) {
		checkOperation();
		lineNoise.linenoiseSetMultiLine(ml ? 1 : 0);
	}

	/**
	 * Unpacking and loading the library into the Java Virtual Machine.
	 */
	private static LinenoiseLibrary loadNativeLibrary() throws Exception {
		OS os = getOS();
		if (os == null) {
			throw new Exception(String.format("Unsupported OS: %s-%s.",
					System.getProperty("os.name"),
					System.getProperty("os.arch")));
		}

		// Get what the system "thinks" the library name should be.
		String libName = LIB_LINENOISE;
		String libNativeName = System.mapLibraryName(libName);

		// Slice up the library name.
		int index = libNativeName.lastIndexOf('.');
		String libNativePrefix = libNativeName.substring(0, index);
		String libNativeSuffix = libNativeName.substring(index);

		// This may return null in some circumstances.
		String libPath = DLL_LIB_RESOURCE_PATH
				+ "/"
				+ getNativeLibraryResourcePrefix(os,
						System.getProperty("os.arch"), os.getName()) + "/"
				+ libNativeName;
		InputStream libInputStream = Linenoise.class
				.getResourceAsStream(libPath);
		if (libInputStream == null) {
			throw new IOException(String.format(
					"Unable to locate the native library '%s'.", libPath));
		}

		// Create the temp file for this instance of the library.
		File libFile = File.createTempFile(libNativePrefix, libNativeSuffix);
		libFile.deleteOnExit();

		// Copy linenoise library to the temp file.
		copyInputStreamToFile(libInputStream, libFile);
		closeQuietly(libInputStream);

		Native.setProtected(true);
		libName = libFile.getName().substring(
				libNativePrefix.length() - libName.length(),
				libFile.getName().length() - libNativeSuffix.length());
		return (LinenoiseLibrary) Native.loadLibrary(libName,
				LinenoiseLibrary.class);
	}

	private void checkOperation() {
		if (Platform.isWindows() || Platform.isWindowsCE()) {
			throw new UnsupportedOperationException(
					"Unsupported operation on Windows platform.");
		}
	}

	private static void closeQuietly(final Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				// Ignore exception
			}
		}
	}

	private static void copyInputStreamToFile(final InputStream input,
			final File file) throws IOException {
		OutputStream output = null;
		try {
			output = new FileOutputStream(file);
			int n = 0;
			byte[] buffer = new byte[4 * 1024];
			while ((n = input.read(buffer)) != -1) {
				output.write(buffer, 0, n);
			}
		} finally {
			closeQuietly(output);
		}
	}

	private static String defaultString(final String string) {
		return (string == null) ? "" : string;
	}

	private static OS getOS() {
		OS os = null;
		String osName = System.getProperty(ENV_CONFIG_OS);
		if (osName != null) {
			for (OS item : OS.values()) {
				if (item.getName().equalsIgnoreCase(osName)) {
					os = item;
					break;
				}
			}
		}

		if (os == null) {
			for (OS item : OS.values()) {
				if (item.getType() == Platform.getOSType()) {
					os = item;
					break;
				}
			}
			if (os == null) {
				osName = System.getProperty("os.name");
				if (osName.startsWith(OS_NAME_AIX)) {
					os = OS.AIX;
				} else if (OS_NAME_GNU.equalsIgnoreCase(osName)) {
					os = OS.GNU;
				} else if (OS_NAME_KFREEBSD.equalsIgnoreCase(osName)) {
					os = OS.KFREEBSD;
				} else if (OS_NAME_NETBSD.equalsIgnoreCase(osName)) {
					os = OS.NETBSD;
				} else if (OS_NAME_DRAGONFLYBSD.equals(osName)) {
					os = OS.DRAGONFLYBSD;
				}
			}
		}

		return os;
	}

	/**
	 * Generate a canonical String prefix based on the given OS type/arch/name.
	 * 
	 * @param osType
	 *            from {@link #getOS(String)}
	 * @param arch
	 *            from <code>os.arch</code> System property
	 * @param name
	 *            from <code>os.name</code> System property
	 */
	private static String getNativeLibraryResourcePrefix(OS osType,
			String arch, String name) {
		String osPrefix;
		arch = arch.toLowerCase().trim();
		if ("powerpc".equals(arch)) {
			arch = "ppc";
		} else if ("powerpc64".equals(arch)) {
			arch = "ppc64";
		} else if ("i386".equals(arch)) {
			arch = "x86";
		} else if ("x86_64".equals(arch) || "amd64".equals(arch)) {
			arch = "x86-64";
		}
		switch (osType) {
		case MAC:
			osPrefix = osType.getName().toLowerCase();
			break;
		case ANDROID:
			if (arch.startsWith("arm")) {
				arch = "arm";
			}
		case WINDOWS:
		case WINDOWSCE:
		case LINUX:
		case SOLARIS:
		case FREEBSD:
		case OPENBSD:
		case NETBSD:
		case KFREEBSD:
			osPrefix = osType.getName().toLowerCase() + "/" + arch;
			break;
		default:
			osPrefix = name.toLowerCase();
			int space = osPrefix.indexOf(" ");
			if (space != -1) {
				osPrefix = osPrefix.substring(0, space);
			}
			osPrefix += "/" + arch;
			break;
		}
		return osPrefix;
	}

	private static interface LinenoiseLibrary extends Library {
		/**
		 * The high level function that is the main API of the linenoise
		 * library. This function checks if the terminal has basic capabilities,
		 * just checking for a blacklist of stupid terminals, and later either
		 * calls the line editing function or uses dummy fgets() so that you
		 * will be able to type something even in the most desperate of the
		 * conditions.
		 */
		public String linenoise(String prompt);

		/**
		 * This function is used by the callback function registered by the user
		 * in order to add completion options given the input string when the
		 * user typed <tab>. See the example.c source code for a very easy to
		 * understand example.
		 */
		public void linenoiseAddCompletion(Completions lc, String str);

		/**
		 * Clear screen.
		 */
		public void linenoiseClearScreen();

		/**
		 * This is the API call to add a new entry in the linenoise history. It
		 * uses a fixed array of char pointers that are shifted (memmoved) when
		 * the history max length is reached in order to remove the older entry
		 * and make room for the new one, so it is not exactly suitable for huge
		 * histories, but will work well for a few hundred of entries.
		 * 
		 * Using a circular buffer is smarter, but a bit more complex to handle.
		 */
		public int linenoiseHistoryAdd(String line);

		/**
		 * Returns the history identified by its progressive index (starting
		 * from zero) or null if the index does not exist.
		 * 
		 * @param index
		 *            progressive index (starting from zero)
		 * @return Returns the history identified by its progressive index
		 *         (starting from zero) or null if the index does not exist.
		 */
		public String linenoiseHistoryGet(int index);

		/**
		 * Get the current history length.
		 * 
		 * @return Returns the current history length.
		 */
		public int linenoiseHistoryGetLen();

		/**
		 * Load the history from the specified file. If the file does not exist
		 * zero is returned and no operation is performed.
		 * 
		 * If the file exists and the operation succeeded 0 is returned,
		 * otherwise on error -1 is returned.
		 */
		public int linenoiseHistoryLoad(String filename);

		/**
		 * Save the history in the specified file. On success 0 is returned
		 * otherwise -1 is returned.
		 */
		public int linenoiseHistorySave(String filename);

		/**
		 * Set the maximum length for the history. This function can be called
		 * even if there is already some history, the function will make sure to
		 * retain just the latest 'len' elements if the new history length value
		 * is smaller than the amount of items already inside the history.
		 */
		public int linenoiseHistorySetMaxLen(int len);

		/**
		 * This special mode is used by linenoise in order to print scan codes
		 * on screen for debugging / development purposes. It is implemented by
		 * the linenoise_example program using the --keycodes option.
		 */
		public void linenoisePrintKeyCodes();

		/**
		 * Register a callback function to be called for tab-completion.
		 * 
		 * @param fn
		 */
		public void linenoiseSetCompletionCallback(CompletionCallback fn);

		/**
		 * Set if to use or not the multi line mode.
		 * 
		 * @param ml
		 */
		public void linenoiseSetMultiLine(int ml);

	}

	private static enum OS {
		MAC(Platform.MAC, "darwin"), LINUX(Platform.LINUX, "linux"), WINDOWS(
				Platform.WINDOWS, "win32"), SOLARIS(Platform.SOLARIS, "sunos"), FREEBSD(
				Platform.FREEBSD, "freebsd"), OPENBSD(Platform.OPENBSD,
				"openbsd"), WINDOWSCE(Platform.WINDOWSCE, "w32ce"), AIX(
				Linenoise.AIX, OS_NAME_AIX), ANDROID(Linenoise.ANDROID,
				"android"), GNU(Linenoise.GNU, OS_NAME_GNU), KFREEBSD(
				Linenoise.KFREEBSD, OS_NAME_KFREEBSD.substring(OS_NAME_KFREEBSD
						.indexOf('/') + 1)), NETBSD(Linenoise.NETBSD,
				OS_NAME_NETBSD), DRAGONFLYBSD(Linenoise.DRAGONFLYBSD,
				OS_NAME_DRAGONFLYBSD);

		private final int type;
		private final String name;

		public int getType() {
			return type;
		}

		public String getName() {
			return name;
		}

		private OS(int type, String name) {
			this.type = type;
			this.name = name;
		}
	}
}
