@echo off
setlocal

REM This file is for building the Poker Enlighter executable.

REM Some very important notes:
REM - The script assumes the availability of the java, javac and jar commands.
REM - The script makes very specific assumptions of the structure of the JAR file, in the packaging
REM stage. Please keep this script in sync with this structure.

REM We're going to count execution time. So, remeber start time. Note that we don't look at the date, so this
REM calculation won't work right if the program run spans local midnight.
set t0=%time: =0%

REM First, let's define some variables that contain most used names and paths
REM throughout the build script. Changes of these variables will propagate down the script.

set simulatorjar=simulator.jar
set simulatorclass=org/javafling/pokerenlighter/simulation/Simulator
set extraclass1=org/javafling/pokerenlighter/simulation/SimulationExport

REM The actual compilation command. It is compiled without any debugging
REM symbols, to add some obfuscation. These symbols will probably be removed by Proguard anyway, but
REM it's not a bad thing to be extra-careful.

javac -g:none -Xlint:unchecked %simulatorclass%.java %extraclass1%.java 2>&1
timeout /t 1 /nobreak > NUL

REM Next, the script will move inside the "org/" folder and delete all the source code files.
REM This is to ensure that the resulting executable will not contain the source code.
REM The script will move back to the build folder root after it's done deleting.

cd org
del /s *.java 2>&1
timeout /t 1 /nobreak > NUL
cd..

REM Package everything in a JAR file.

jar cf0 %simulatorjar% org/* 2>&1

REM And we are done. Enjoy.

REM Capture the end time before doing anything else
set t=%time: =0%

REM make t0 into a scaler in 100ths of a second, being careful not 
REM to let SET/A misinterpret 08 and 09 as octal
set /a h=1%t0:~0,2%-100
set /a m=1%t0:~3,2%-100
set /a s=1%t0:~6,2%-100
set /a c=1%t0:~9,2%-100
set /a starttime = %h% * 360000 + %m% * 6000 + 100 * %s% + %c%

REM make t into a scaler in 100ths of a second
set /a h=1%t:~0,2%-100
set /a m=1%t:~3,2%-100
set /a s=1%t:~6,2%-100
set /a c=1%t:~9,2%-100
set /a endtime = %h% * 360000 + %m% * 6000 + 100 * %s% + %c%

REM runtime in 100ths is now just end - start
set /a runtime = %endtime% - %starttime%
set runtime = %s%.%c%

echo Ran for %runtime%0 ms