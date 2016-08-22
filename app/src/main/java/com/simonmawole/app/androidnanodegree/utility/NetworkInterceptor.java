package com.simonmawole.app.androidnanodegree.utility;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by simon on 21-Aug-16.
 */
public class NetworkInterceptor implements Interceptor {
    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        System.out.println(String.format("Sending request %s on %s%n%s%n%s",
                request.url(), chain.connection(), request.headers(),chain.request().body()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        System.out.println(String.format("RECEIVED RESPONSE FOR %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }
}