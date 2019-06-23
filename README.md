Documentation is available on [http://connfa.com/](http://connfa.com/)

Project is supported by [Lemberg Solutions](http://lemberg.co.uk)

# Getting started
The connfa application is based on android flavors, meaning there is one "main" folder and then
there are overrides.

### Copy the source folder
For an easy start, copy the `/app/src/connfa` folder over to a new directory and name it after the
new application.

### Declare the new flavor
Under the `app` folder, edit the `build.gradle` file and add another "flavor" under the
`android:productFlavors` key, or simply copy over the `connfa` flavor over to a new one. Then name
the flavor after the folder you copied before.
Also, change the `applicationId` property to your version.

### Make necessary changes
Under `app/src/<flavor name>/res` folder, upload/change all images that you will to update.
A nice way to do this, is using the `Android Drawable Import` plugin to batch upload images.

To do this, install the plugin and right click on the drawable folder.
Select `New > Batch Drawable Import` and follow the wizard to push multiple versions of an image at
once.

### Google-services
In order to use the google services, a new google-services json file needs to be created.

To generate the new google-services automatically and for more information, head over to
https://console.firebase.google.com.

### Keys
Multiple keys must be installed in the application.

In each flavor, the `/app/src/<flavor>/res/values/secrets.xml` file is ignored from the version
control system. Create a file and add the keys necessary for the application in the following
format:
```yaml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="api_value_google_maps_api_key">12345abc</string>
    <string name="api_value_twitter_api_key">12345abc</string>
    <string name="api_value_twitter_secret">12345abc</string>
</resources>
```
Please, keep in mind that the name of the strings are the onces to be used as they are hardcoded.

### Signing keys
To publish the application, you will need a signing key. To generate a signing key from Android
Studio go to `Build > Generate Signed Bundle/APK`, select APK and click "Create new...".

Fill in the organisation details and create the key. Set the path of your file to something like
(for Linux) `/home/{user}/keystores/android.jks`.

Then, in the `local.properties` file, fill in the following:

```
storeFile=<android.jks path as above>
keyAlias=<the key alias you provided>
keyPassword=<the key password>
storePassword=<the store password>
```
These values are then consumed in the `signingConfigs` of the `/app/build.gradle` file.
