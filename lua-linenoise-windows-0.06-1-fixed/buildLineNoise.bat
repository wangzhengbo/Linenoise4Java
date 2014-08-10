cl /MD /O2 -c -Folinenoise.obj linenoiselib.c
cl /MD /O2 -c -Fowin32fixes.obj win32fixes.c
link -dll -def:linenoise.def -out:linenoise.dll linenoise.obj win32fixes.obj ws2_32.lib

del linenoise.exp
del linenoise.lib
del linenoise.obj
del win32fixes.obj
IF /i "%PROCESSOR_IDENTIFIER:~0,3%"=="X86" (
	copy /B /Y linenoise.dll ..\src\cn\com\linenoise\win32\x86
) ELSE (
	copy /B /Y linenoise.dll ..\src\cn\com\linenoise\win32\x86-64
)
del linenoise.dll