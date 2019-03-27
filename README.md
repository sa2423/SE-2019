# SE-2019

Code Repository for Software Engineering (ECE 452) Group #3 - Spring 2019 



### Operating Systems
Ubuntu (website), iOS (Android apps)



### System Requirements 
Node.js (10.11), npm (6.4.1), MongoDB (shell version 4.0.7), Android Studio 



### Setting up Restaurant Website 

1. Clone the repository with `git clone https://github.com/sa2423/SE-2019.git`

2. Enter the adam_server directory with `cd adam_server`

3. Run `npm install`

4. Type `node bin/www` to start the server

### Setting up MongoDB

1. Setup the MongoDB Package Database by running `sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10`

2. Create a list file for MongoDB by running `echo 'deb http://downloads-distro.mongodb.org/repo/ubuntu-upstart dist 10gen' | sudo tee /etc/apt/sources.list.d/mongodb.list`

3. Reload package database by running `sudo apt-get update`

4. Install latest stable version of MongoDB by running `sudo apt-get install -y mongodb-org`

5. Start MongoDB by running service with `sudo service mongod start`

6. Enter the MongoDB command line by typing `mongo`

### Setting up Android Applications

1. Install the “Android Studio” and necessary Android SDK components.

2. Open the app project you’re interest in in Android Studio

3. Click build to compile the app

4. Create a new virtual android device through AVD manager, or run on an actual device by enabling USB debugging in that device’s settings and plugging it in via USB.