@echo off

D:
cd D:\eclipse\workspace\Linenoise4Java

set JAVA_HOME=D:\Program Files\java\jdk1.5.0_15
set ANT_HOME=D:\apache-ant-1.8.2

set path=%JAVA_HOME%\bin;%ANT_HOME%\bin;%path%
ant
pause