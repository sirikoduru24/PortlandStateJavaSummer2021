This is a README file!

@author Siri Koduru
This Project-3 is an extension of Project-3 which has more options along with print and textFile
This code helps to
- print the appointment which is the latest one.
- store the appointment to a textFile when -textFile option is invoked.
- pretty print the appointments in a sorted order either to console or textFile when options -/textFile are chosen for option -pretty.

The usage of this code is:
java -jar target/apptbook-2021.0.0.jar [options] arguments.

The options can be in any order and can either be one of [-readme -print -textFile file -pretty -/file] or all.
If one of the options is -readme the code presents the README description.

The arguments are of the below format:
name: name of the owner of the appointment.
description: description of the appointment.
begin: valid date and time in the format mm/dd/yyyy hh:mm am/pm.
end: valid date and time in the format mm/dd/yyyy hh:mm am/pm.

-print - Prints the latest appointment taking the arguments from console as input
-textFile file - Stores the latest appointment to textFile and each textFile belongs only to a particular owner.
-pretty -/file - Prints the appointments to the console/textFile basing upon the chosen option.
A combination of these options can also be chosen and each does the same thing
If combination of print and pretty are chosen the code prints the latest appointment and pretty format of appointment to chosen option.
Similarly -textFile file and -pretty -/file are chosen - All appointments in file of -textFile and console are stored to Appointment book
and then printed in a -pretty format.
If all three options are chosen the above things happen together.