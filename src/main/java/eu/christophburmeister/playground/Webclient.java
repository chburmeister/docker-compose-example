package eu.christophburmeister.playground;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.apache.http.HttpHeaders.USER_AGENT;

public class Webclient {

    private static int maxErrorCount = 3;
    private static int currentErrorCount = 0;

    public static void main(String[] args) {
        String targeturl = System.getProperty("targeturl");
        String useragent = System.getProperty("useragent");
        int sleepMSecs = Integer.valueOf(System.getProperty("sleepmsecs"));

        while (true) {
            try {
                Thread.sleep(sleepMSecs);
                HttpClient client = HttpClientBuilder.create().build();
                HttpGet request = new HttpGet(targeturl);
                request.addHeader("User-Agent", useragent);
                HttpResponse resp = client.execute(request);

                System.out.println(targeturl + " [resp:" + resp.getStatusLine().getStatusCode() + "]");

            } catch (Exception e) {
                System.out.println(e.getMessage());
                // in case the target is really not reachable...
                currentErrorCount++;
                if (currentErrorCount > maxErrorCount){
                    System.out.println("reached limit of requests-errors, shutting down client...");
                    System.exit(1);
                }
            }
        }
    }
}
