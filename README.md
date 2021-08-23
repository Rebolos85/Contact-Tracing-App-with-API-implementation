# Contact-Tracing-App-with-Firebase-implementation

This is just my hobby project that I made in kotlin is basically for my learning purpose I implemented all the things that I've learned in just 6 months of developing android apps I code almost 6-10 hours a day during this project. 
I am trying to impersonate the traze app which was uploaded but I change some of there features since I don't have client project yet in my team so basically I am passionate to
develop android application. This is not finished project since I just currently developing this one for 1 month

-Tech stack that I use in this project
  - Kotlin Language
  - Hilt Depedency Injection - for reducing  boiler plate especially on passing depedency to the consumer classes as we all know google recommends it for the developers to make
  their lives a lot easier since the developer will not to do a lot of instantiation and passing object via contructor since hilt is a framework for handling depedency to other classes
  - MVVM architecture
  - Kotlin coroutines for handling network request via firebase in order to prevent freezing the UI since coroutines has really a great help in terms of handling threading
and especially handling reactive programming style since coroutines has a lot to offer
  - Navigation Component which this is only have one activity architecture 
  - safe-args plugin for passing a data in other fragments
  - Glide library for displaying Image
  -Room Databases for storing the image first locally and basic credentials and passing it to firebase when the user has turn on their internet connection
  -
  
 
App Features
   - Phone authentication using Firebase Authentication
    - Verify phone authentication
    - Login features with verify OTP
    - Upload Picture to cloud storage
    - Crop the Image using Android Image Cropper library
    - Register Features
    - Storage credentials via room database first
