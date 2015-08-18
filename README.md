Java API Dekstop Example
=========

Sample application utilizing the Pyramid Java API for Pyramid bill validators. With this API
you can quickly configure your application to operate with a bill validator. This application shows RS-232 state transition and reports the denomination of accepted notes.
  
This is provided as both a Netbeans and Intellij project.
  

## Requirements

1. A supported bill validator
   - Spectra
   - Apex 7000
   - Apex 5000
   - Trilogy *requires RS-232 firmware*
   - Curve *require RS-232 firmware*
2. Communication Harness
   - [Recommended Harness](http://shop.pyramidacceptors.com/usb-rs-232-communication-cable-harness-for-apex-05aa0023/)
   - Or a DB9 RS-232 connection or USB UART cable of your choice
3. Your favorite Java editor (we use Netbeans because projects are easy to share)

## Setup
If you are using Netbeans, simply open the project and satisfy the PTalk.jar dependency. You will then be able to run and debug the application.

If you are using Intellij, the project should import and handle all of the depency stuff for you via gradle.

## Dependencies
If you prefer to handle your own depencies, here is what you will need

 1. The ptalk.jar file from this project (which itself include its bundled dependencies)
 2. (log4j12-1.7.12)[http://mvnrepository.com/artifact/log4j/log4j/1.2.17]
 2. (Already in PTalk) jSSC latest
 3. (Already in PTalk) Apache commons-collection 4.4+
 
 __Please note that you may omit logging or swap out log4j for another framework.__

## Questions
Please [let us know](https://github.com/PyramidTechnologies/Java-API-desktop-sample/issues/new).


![](https://googledrive.com/host/0B79TkjL8Nm20QjU0UGhObnBTUE0/logo_2.jpg)
