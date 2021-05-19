package com.google.testapp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    private static void Runner(int id) {
        System.out.println( "Hello World! #" + id);
        GoogleCredentials credentials;
        try {
            credentials = ServiceAccountCredentials.fromStream(new FileInputStream("/Users/stim/Documents/keys/GCP_sandbox.json"))
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
            System.out.println( "Hello World! #" + id + "  " + response.getStatusCode());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void main( String[] args ) throws IOException
    {
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(2);
        //null
        exec.schedule(new Runnable() {
            public void run() {
                App.Runner(1);
                System.out.println("idTokenRefresh done");
            }
        }, 1, TimeUnit.SECONDS);

        // fresh
    
        exec.schedule(new Runnable() {
            public void run() {
                App.Runner(2);
                System.out.println("idTokenRefresh done");
            }
        }, 5, TimeUnit.SECONDS);
        
            // stale
            exec.schedule(new Runnable() {
                public void run() {
                    App.Runner(3);
                    System.out.println("idTokenRefresh done");
                }
            }, 68, TimeUnit.SECONDS);  
            
            // fresh

            exec.schedule(new Runnable() {
                public void run() {
                    App.Runner(4);
                    System.out.println("idTokenRefresh done");
                }
            }, 98, TimeUnit.SECONDS);  

             
            // expired
            
            exec.schedule(new Runnable() {
                public void run() {
                    App.Runner(5);
                    System.out.println("idTokenRefresh done");
                }
            }, 428, TimeUnit.SECONDS);  
        }
}