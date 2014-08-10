package cn.com.linenoise;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ClearScreenTest.class, HistoryAddTest.class,
		HistoryGetLenTest.class, HistoryGetTest.class, HistoryLoadTest.class,
		HistoryLoadWithMaxLenTest.class, HistorySetMaxLenTest.class,
		PrintKeyCodesTest.class, ReadLineTest.class,
		SetCompletionCallbackTest.class, SetMultiLineTest.class })
public class AllTests {

}
