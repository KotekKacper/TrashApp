# TrashApp

TrashApp is an Android application for those concerned with their city's cleanliness.
<img align="right" src="https://user-images.githubusercontent.com/71709842/223753772-031bcca0-4291-4a20-9fbd-cb6c7acf3489.gif" alt="TrashAppGIF" width="257">

It uses OSM as a map provider and MySQL Server for database. 

Main features include:
- reporting spotted trash
- adding images to the report
- exploring the map of the current trash reports
- exploring all reports by list
- marking already cleaned trash


## Installation

### Prerequisites
- MySQL80 installed locally
- IntelliJ IDEA
- Android Studio

### Install process
For starters you need to clone server from this repository: https://github.com/KotekKacper/TrashAppServer.

Then you need to make a new mysql user with username: "user" and password: "userpass" (you can change that credentials later, on the server side).
The next step is to import the file trashdb.sql to MySQL.

Then you can run the database server, next TrashAppServer.

Finally you can run TrashApp and create new account or log in as administratior (default credentials are username: "admin" and password: "admin").
Basic data for testing should already be available.
