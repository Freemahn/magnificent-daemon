# Magnificent Health Checker

This program is for saucelabs.com test challenge.  
It simply sends get request in serviceUrl and logs response in separate thread.  
For running in background you can use linux services.
Log file is called **serviceLog.log** and is placed in the same directory as jar.
## How to build
`gradle fatJar`

In {Project.root}/build/libs/ you will find **magnificent-daemon-all-*version*-SNAPSHOT.jar**

## How to run
`java -jar magnificent-daemon-all-1.0-SNAPSHOT.jar` 

Possible arguments:
+ serviceUrl - string
+ delay between requests - integer