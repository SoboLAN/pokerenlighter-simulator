@echo off
setlocal

REM This file is for building the Poker Enlighter executable.

REM The build log file:
set buildlog=build_.%1_%2.log

REM Some very important notes:
REM - The script expects 2 parameters: the version of Poker Enlighter followed
REM by the build number. For example: "myscript.bat 2.3 538"
REM - The script assumes the availability of the java, javac, jar, xcopy, winrar and md5sum commands.
REM - The path of the rt.jar file (main file of the JRE) is hardcoded within the proguard config file.
REM - The script makes very specific assumptions of the structure of the JAR file, in the packaging
REM stage. Please keep this script in sync with this structure.


REM We're going to count execution time. So, remeber start time. Note that we don't look at the date, so this
REM calculation won't work right if the program run spans local midnight.
set t0=%time: =0%


REM First, let's define some variables that contain most used names and paths
REM throughout the build script. Changes of these variables will propagate down the script.

set tmpbuilddir=release.%1.%2
set manifestfile=Manifest.txt
set proguardfile=proguard.config
set perawfile=Poker.Enlighter.raw.jar
set peobfuscatedfile=Poker.Enlighter.obfuscated.jar
set simulatorjar=simulator.jar
set guijar=Poker Enlighter.jar
set mainclass=org/javafling/pokerenlighter/main/PokerEnlighter
set lib1=lib/substance.lnf/substance.jar
set lib2=lib/substance.lnf/trident.jar
set lib3=lib/easynth.lnf/EaSynthLookAndFeel.jar
set lib4=lib/jfreechart/jcommon-1.0.18.jar
set lib5=lib/jfreechart/jfreechart-1.0.15.jar
set lib6=lib/nimrod.lnf/nimrodlf-1.2.jar
set lib7=lib/seaglass.lnf/seaglasslookandfeel-0.2.jar

REM The actual compilation command. It is compiled without any debugging
REM symbols, to add some obfuscation. These symbols will probably be removed by Proguard anyway, but
REM it's not a bad thing to be extra-careful.

javac -g:none -Xlint:unchecked -classpath .;%simulatorjar%;%lib1%;%lib2%;%lib3%;%lib4%;%lib5%;%lib6%;%lib7% %mainclass%.java > %buildlog% 2>&1
timeout /t 1 /nobreak > NUL

REM Next, the script will move inside the "org/" folder and delete all the source code files.
REM This is to ensure that the resulting executable will not contain the source code.
REM The script will move back to the build folder root after it's done deleting.

cd org
del /s *.java >> %buildlog% 2>&1
timeout /t 1 /nobreak > NUL
cd..

REM Next, the Manifest file is built. This is needed for the JAR file.

echo Main-Class: %mainclass% > %manifestfile%
echo Class-Path: %simulatorjar% %lib1% %lib2% %lib3% %lib4% %lib5% %lib6% %lib7% >> %manifestfile%

REM Package everything in a JAR file.

jar cfm0 %perawfile% %manifestfile% org/* >> %buildlog% 2>&1

REM The Proguard config file is created below

echo -injars %perawfile% > %proguardfile%
echo -outjars %peobfuscatedfile% >> %proguardfile%
echo -libraryjars 'C:\Program Files\Java\jre7\lib\rt.jar' >> %proguardfile%
echo -libraryjars %lib1% >> %proguardfile%
echo -libraryjars %lib2% >> %proguardfile%
echo -libraryjars %lib3% >> %proguardfile%
echo -libraryjars %lib4% >> %proguardfile%
echo -libraryjars %lib5% >> %proguardfile%
echo -libraryjars %lib6% >> %proguardfile%
echo -libraryjars %lib7% >> %proguardfile%
echo -target 1.7 >> %proguardfile%
echo -dontshrink >> %proguardfile%
echo -dontoptimize >> %proguardfile%
echo -dontusemixedcaseclassnames >> %proguardfile%
echo -verbose >> %proguardfile%
echo -keepclasseswithmembers public class * { >> %proguardfile%
echo     public static void main(java.lang.String[]); >> %proguardfile%
echo } >> %proguardfile%
echo -keepclassmembers enum  * { >> %proguardfile%
echo public static **[] values(); >> %proguardfile%
echo     public static ** valueOf(java.lang.String); >> %proguardfile%
echo } >> %proguardfile%
echo -keep class * extends javax.swing.plaf.ComponentUI { >> %proguardfile%
echo     public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent); >> %proguardfile%
echo } >> %proguardfile%
echo -keepclasseswithmembers,allowshrinking class * { >> %proguardfile%
echo     native ^<methods^>; >> %proguardfile%
echo } >> %proguardfile%

REM The JAR file is obfuscated.

java -jar proguard.jar @%proguardfile% -verbose >> %buildlog% 2>&1

REM The proguard file and old JAR file is no longer needed

del %proguardfile% >> %buildlog% 2>&1
del %perawfile% >> %buildlog% 2>&1

REM A new temporary directory will be made where the JAR file will be split in 2 and packaged.

mkdir %tmpbuilddir%

REM Move / copy all relevant files to the new directory and switch working dir to that directory

xcopy "lib\*.*" %tmpbuilddir%\lib /E /I >> %buildlog% 2>&1
xcopy "config\*.*" %tmpbuilddir%\config /E /I >> %buildlog% 2>&1
xcopy LICENSE.txt %tmpbuilddir% >> %buildlog% 2>&1
move %manifestfile% %tmpbuilddir% >> %buildlog% 2>&1
move %peobfuscatedfile% %tmpbuilddir% >> %buildlog% 2>&1
cd %tmpbuilddir%

REM Next, the JAR file will be extracted

jar -xf %peobfuscatedfile% >> %buildlog% 2>&1

REM Next, delete the /a and /c directories (/combination and /simulation respectively) and
REM move the images directory in the right location

rmdir "org/javafling/pokerenlighter/a" /S /Q >> %buildlog% 2>&1
rmdir "org/javafling/pokerenlighter/c" /S /Q >> %buildlog% 2>&1
move org/javafling/pokerenlighter/gui/images org/javafling/pokerenlighter/b >> %buildlog% 2>&1
rmdir "org/javafling/pokerenlighter/gui" /S /Q >> %buildlog% 2>&1

REM Next, it's time to rebuild the JAR file

jar cfm0 "%guijar%" %manifestfile% org/* >> %buildlog% 2>&1

REM Next, the simulator's JAR file must be created. So a new extraction needs to be made.

jar -xf %peobfuscatedfile% >> %buildlog% 2>&1
rmdir "org/javafling/pokerenlighter/b" /S /Q >> %buildlog% 2>&1
rmdir "org/javafling/pokerenlighter/gui" /S /Q >> %buildlog% 2>&1
rmdir "org/javafling/pokerenlighter/main" /S /Q >> %buildlog% 2>&1
jar cf0 "%simulatorjar%" org/* >> %buildlog% 2>&1

REM Next, we package everything in a zip file.
REM - "a" means create archive.
REM - "-afzip" means make it a ZIP archive
REM - "-m0" means no compression

winrar a -afzip -r -m0 "poker.enlighter.%1.%2.zip" LICENSE.txt %simulatorjar% "%guijar%" config/* lib/*  >> %buildlog% 2>&1

REM Next, we delete everything but the archive.

del %peobfuscatedfile% >> %buildlog% 2>&1
rmdir "org/" /S /Q >> %buildlog% 2>&1
rmdir "META-INF/" /S /Q >> %buildlog% 2>&1
rmdir "lib/" /S /Q >> %buildlog% 2>&1
rmdir "config/" /S /Q >> %buildlog% 2>&1
del LICENSE.txt >> %buildlog% 2>&1
del %manifestfile% >> %buildlog% 2>&1
del "%guijar%" >> %buildlog% 2>&1
del %simulatorjar% >> %buildlog% 2>&1

REM Finally, we need a MD5 hash value for the archive.

md5sum "poker.enlighter.%1.%2.zip" > md5sum.txt

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