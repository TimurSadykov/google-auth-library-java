package com.google.testapp;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.IdTokenCredentials;
import com.google.auth.oauth2.IdTokenProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(new FileInputStream("/Users/stim/Documents/keys/GCP_sandbox.json"))
        .createScoped("foo", "bar");
        String serviceUrl = "https://helloworld-qk56ikjwfq-uw.a.run.app";
        if (!(credentials instanceof IdTokenProvider)) {
            throw new IllegalArgumentException("Credentials are not an instance of IdTokenProvider.");
          }
          IdTokenCredentials tokenCredential =
              IdTokenCredentials.newBuilder()
                  .setIdTokenProvider((IdTokenProvider) credentials)
                  .setTargetAudience(serviceUrl)
                  .build();
      
          GenericUrl genericUrl = new GenericUrl(serviceUrl);
          HttpCredentialsAdapter adapter = new HttpCredentialsAdapter(tokenCredential);
          HttpTransport transport = new NetHttpTransport();
          HttpRequest request = transport.createRequestFactory(adapter).buildGetRequest(genericUrl);
          HttpResponse response = request.execute();
          // null
        System.out.println( "Hello World! " + response.getStatusCode());

        // fresh
        request = transport.createRequestFactory(adapter).buildGetRequest(genericUrl);
        response = request.execute();
        System.out.println( "Hello World! " + response.getStatusCode());
        try {
            Thread.sleep(62000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // stale
        request = transport.createRequestFactory(adapter).buildGetRequest(genericUrl);
        response = request.execute();
        System.out.println( "Hello World! " + response.getStatusCode());
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // fresh
        request = transport.createRequestFactory(adapter).buildGetRequest(genericUrl);
        response = request.execute();
        System.out.println( "Hello World! " + response.getStatusCode());

        try {
            Thread.sleep(330000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // expired
        request = transport.createRequestFactory(adapter).buildGetRequest(genericUrl);
        response = request.execute();
        System.out.println( "Hello World! " + response.getStatusCode());
    }
}