# Restaurant Automation - Codename Adam: Restaurant Website README
Contains information about all the files used to develop the website 

### database.js
All insertion, modification, get, and delete queries for our MongoDB server are written here. 
They are written using JavaScript Promises or in try-catch format. 

### website.js
All API endpoints for website pages are written here. 
They are written in REST API format and some API endpoints call functions written in database.js. 

### api.js
All API endpoints that are too be hidden from the public website are written here. 
They are also written in REST API format and most API endpoints call functions written in database.js.
Endpoints in this file are responsible for user authentication and handling of data returned from database before displaying it on the website. 

### app.js
This file sets up the restaurant website.
All paths to HTML5UP templates are declared here as well as paths to website and API endpoints. 

### view/*.ejs files 
This folder contains all the front-end code. 
EJS allows us to write HTML code with the advantage of using pure JavaScript to write HTML code dynamically. 

### bin/www
This file is used to start the restaurant website server. 

### assets_escape_velocity/
All files from HTML5UPs' Escape Velocity template are here, modified as needed to ensure our website looks good. 

### assets_phantom/
All files from HTML5UPs' Phantom template are here, modified as needed to ensure our website looks good. 