This is a README file!

@author Siri Koduru
This Project-2 is an extension of Project-1 which has more options along with print
This code helps to
- print the appointment which is the latest one.
- store the appointment to a textFile when -textFile option is invoked.

The usage of this code is:
java -jar target/apptbook-2021.0.0.jar [options] arguments.

The options can be in any order and can either be one of [-print -readme -textFile file] or all.
If one of the options is -readme the code presents the README description.

The arguments are of the below format:
name: name of the owner of the appointment.
description: description of the appointment.
start date: valid date in the format mm/dd/yyyy.
start time: valid time in the format hh:mm.
end date: valid date in the format mm/dd/yyyy.
end time: valid time in the format hh:mm.

An addition to this is the -textFile where we can read and write the appointments.
When only the -print option is present the code prints the latest appointment.
When only the -textFile file option is present the code reads from the textFile and each textFile has one
particular owner and writes all the Appointments back to the textFile.
When both -textFile, -print options are present both the above operations together are performed.