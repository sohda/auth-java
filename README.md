# Ricoh Auth Client for Java

This open-source library allows you to integrate Ricoh API's [Authorization and Discovery Service](http://docs.ricohapi.com/docs/authorization-and-discovery-service/) into your Java app.

Learn more at http://docs.ricohapi.com/

## Requirements

* java 1.8+

You'll also need

* Ricoh API Client Credentials (client_id & client_secret)
* Ricoh ID (user_id & password)

If you don't have them, please register yourself and your client from [THETA Developers Website](http://contest.theta360.com/).

## Installation
This section shows you to install Ricoh Auth Client for Java in your application.  
See [Auth Sample](https://github.com/ricohapi/auth-java/tree/master/sample#auth-sample) to try out a sample of Ricoh Auth Client for Java.

* Download: [`ricoh-api-auth.jar`](https://github.com/ricohapi/auth-java/blob/v1.0.0/lib/ricoh-api-auth.jar?raw=true)
* Drag `ricoh-api-auth.jar` into a directory (ex. `libs`) of your application.
* Edit your application's `build.gradle` as follows.
```java
dependencies {
    compile files('libs/ricoh-api-auth.jar')
}
```
* Click the `Sync Project with Gradle Files` icon to clean and build your application.
* Install completed! See [Sample Flow](https://github.com/ricohapi/auth-java#sample-flow) for a coding example.

## Sample Flow
```java
// Import
import com.ricohapi.auth.AuthClient;
import com.ricohapi.auth.CompletionHandler;
import com.ricohapi.auth.Scope;
import com.ricohapi.auth.entity.AuthResult;

// Set your Ricoh API Client Credentials
AuthClient authClient = new AuthClient("<your_client_id>", "<your_client_secret>");

// Set your Ricoh ID
authClient.setResourceOwnerCreds("<your_user_id>", "<your_password>");

// Open a new session
authClient.session(Scope.MSTORAGE, new CompletionHandler<AuthResult>(){
    @Override
    public void onCompleted(AuthResult result) {
        String accessToken = result.getAccessToken();
        // do something
    }

    @Override
    public void onThrowable(Throwable t) {
        // Something wrong happened.
    }
});
```

## SDK API Samples

### Constructor
```java
AuthClient authClient = new AuthClient("<your_client_id>", "<your_client_secret>");
```

### Set resource owner credentials
```java
authClient.setResourceOwnerCreds("<your_user_id>", "<your_password>");
```

### Open a new session
```java
authClient.session(Scope.MSTORAGE, new CompletionHandler<AuthResult>(){
    @Override
    public void onCompleted(AuthResult result) {
        String accessToken = result.getAccessToken();
        // do something
    }

    @Override
    public void onThrowable(Throwable t) {
        // Something wrong happened.
    }
});
```

### Resume a preceding session
```java
authClient.getAccessToken(new CompletionHandler<AuthResult>(){
    @Override
    public void onCompleted(AuthResult result) {
        String accessToken = result.getAccessToken();
        // do something
    }

    @Override
    public void onThrowable(Throwable t) {
        // Something wrong happened.
    }
});
```
