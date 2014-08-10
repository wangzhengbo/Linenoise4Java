#!/usr/bin/env bash

Linenoise4JAVA_VERSION=0.1

CLASSPATH=./lib/commons-io-2.2.jar
CLASSPATH=$CLASSPATH:./lib/hamcrest-core-1.3.jar
CLASSPATH=$CLASSPATH:./lib/junit-4.11.jar
CLASSPATH=$CLASSPATH:./dist/$Linenoise4JAVA_VERSION/Linenoise4Java-$Linenoise4JAVA_VERSION.jar
CLASSPATH=$CLASSPATH:./dist/$Linenoise4JAVA_VERSION/Linenoise4Java-$Linenoise4JAVA_VERSION-lib.jar
CLASSPATH=$CLASSPATH:./dist/$Linenoise4JAVA_VERSION/Linenoise4Java-$Linenoise4JAVA_VERSION-test.jar

if [ "$(uname -s)" == "GNU/kFreeBSD" ]; then
  LIB_JNA=./lib/kFreeBSD/jna-3.2.7.jar
elif [ "$(uname -s)" == "NetBSD" ]; then
  LIB_JNA=./lib/NetBSD/jna-4.1.0.jar
elif [ "$(uname -s)" == "OpenBSD" ]; then
  LIB_JNA=./lib/OpenBSD/jna-4.1.0.jar
elif [ "$(uname -s)" == "FreeBSD" ]; then
  LIB_JNA=./lib/jna-4.1.0.jar
elif [ "$(uname -s)" == "DragonFly" ]; then
  LIB_JNA=./lib/DragonFly/jna-4.1.0.jar
elif [ "$(uname -s)" == "Linux" ]; then
  if [ "$(uname -m)" == "ppc" ]; then
    LIB_JNA=./lib/Linux/ppc/jna-3.2.7.jar
  fi
fi

if [ "$LIB_JNA" == "" ]; then
  LIB_JNA=./lib/jna-3.5.2.jar
fi

CLASSPATH=$LIB_JNA:$CLASSPATH

TESTS=AllTests
if [ "$1" != "" ]; then
  TESTS="$1"
fi
PROPERTIES=
if [ "$2" != "" ]; then
  PROPERTIES="-Dfile.encoding=$2"
fi

if [ "$1" != "" ]; then
  java -cp $CLASSPATH $PROPERTIES org.junit.runner.JUnitCore cn.com.linenoise.$TESTS
else
  java -cp $CLASSPATH $PROPERTIES org.junit.runner.JUnitCore cn.com.linenoise.ClearScreenTest
  java -cp $CLASSPATH $PROPERTIES org.junit.runner.JUnitCore cn.com.linenoise.HistoryAddTest
  java -cp $CLASSPATH $PROPERTIES org.junit.runner.JUnitCore cn.com.linenoise.HistoryLoadTest
  java -cp $CLASSPATH $PROPERTIES org.junit.runner.JUnitCore cn.com.linenoise.HistoryLoadWithMaxLenTest
  java -cp $CLASSPATH $PROPERTIES org.junit.runner.JUnitCore cn.com.linenoise.HistorySetMaxLenTest
  java -cp $CLASSPATH $PROPERTIES org.junit.runner.JUnitCore cn.com.linenoise.PrintKeyCodesTest
  java -cp $CLASSPATH $PROPERTIES org.junit.runner.JUnitCore cn.com.linenoise.ReadLineTest
  java -cp $CLASSPATH $PROPERTIES org.junit.runner.JUnitCore cn.com.linenoise.SetCompletionCallbackTest
  java -cp $CLASSPATH $PROPERTIES org.junit.runner.JUnitCore cn.com.linenoise.SetMultiLineTest
fi