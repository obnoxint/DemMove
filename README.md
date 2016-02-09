# DemMove

Automatically moves Source demo files to another directory if the game is not running.

I wrote this small app because I was too lazy to navigate to my Left4Dead 2 installation folder each time I want to move
demo files to my library.

DemMove runs in the background and checks for running game processes every 10 seconds. If it doesn't find a running
process, it moves all .dem files to a specified target directory.

I use DemMove with [Sander](http://www.dyxtra.com/sander/ "Sander Homepage") and added the start.bat to my Auto Start
directory to make Sander and DemMove start when I log in to my Windows account. DemMove will then gracefully exit when
you shut down your PC or log out off your Windows session.

DemMove runs silently in the background. If you want to have a console window, so you can exit DemMove by closing this
window, run it with *java* instead of *javaw*.

# Requirements

* Java 8
* A Source based game
* (Optional) Sander

# Installation

* Build the application or download the latest build from
[here](http://files.obnoxint.net/index.php?dir=demmove/ "DemMove Downloads"). If you use Sander, I recommend to put the
DemMove jar in the same directory you installed Sander to.
* Copy the *start.bat* from the jar-file to the same directory you put the jar-file in or write your own start script.
* (Optional) If you want to start Sander along with DemMove, remove "REM" from the first line of the bat file.

# Configuration

The default configuration is meant to be used with Left4Dead 2. DemMove will create a default configuration file if it
doesn't exist. If you like to use it with another game, I recommend to copy the *demmove.properties* file from the
jar-file like you did with the bat-file and modify its values before starting DemMove.

The property values are:

* *exeName* - The **exact** name of the executable game file, like it appears in the Windows Task Manager.
* *source* - The directory where the game places the recorded demo files.
* *target* - The directory where DemMove should move them demo files to. This directory will be created if it doesn't
exist.

**Note:** The character "\" (Backslash) is a special character in Java and must be escaped like this: "\\".

# License

Public Domain

# Disclaimer

DemMove is free software. If it kills your kitten (or your computer), it's your fault and I can not be held reliable.