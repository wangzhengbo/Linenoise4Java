@echo off

set JAVA_HOME=D:\Program Files\java\jdk1.5.0_15
set PATH="%JAVA_HOME%\bin";%PATH%

set LINENOISE4JAVA_VERSION=0.1

set CLASSPATH=.\lib\commons-io-2.2.jar
set CLASSPATH=%CLASSPATH%;.\lib\hamcrest-core-1.3.jar
set CLASSPATH=%CLASSPATH%;.\lib\jna-3.5.2.jar
set CLASSPATH=%CLASSPATH%;.\lib\junit-4.11.jar
set CLASSPATH=%CLASSPATH%;.\dist\%LINENOISE4JAVA_VERSION%\Linenoise4Java-%LINENOISE4JAVA_VERSION%.jar
set CLASSPATH=%CLASSPATH%;.\dist\%LINENOISE4JAVA_VERSION%\Linenoise4Java-%LINENOISE4JAVA_VERSION%-lib.jar
set CLASSPATH=%CLASSPATH%;.\dist\%LINENOISE4JAVA_VERSION%\Linenoise4Java-%LINENOISE4JAVA_VERSION%-test.jar

set TESTS=AllTests
IF NOT "%1"=="" (
	set TESTS="%1"
)
set PROPERTIES=
IF NOT "%2"=="" (
	set PROPERTIES="-Dfile.encoding=%2"
)
java -cp %CLASSPATH% %PROPERTIES% org.junit.runner.JUnitCore cn.com.linenoise.%TESTS%
pause
