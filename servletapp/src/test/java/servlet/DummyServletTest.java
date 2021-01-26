package servlet;

import junit.extension.TestFrameworkRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RunWith(TestFrameworkRunner.class)
public class DummyServletTest {

    @Test
    public void testOne() throws IOException {
        System.out.println("We are running first test now!"); // Just to see in console that we are actually running the test
        makeCall();
    }

    @Test
    public void testTwo() throws IOException {
        System.out.println("We are running second test now!");
        makeCall();
    }

    private void makeCall() throws IOException {
        URL url = new URL("http://localhost:8080/execute");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        Assert.assertEquals(200, responseCode);

        String output = null;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            output = content.toString();
        }
        Assert.assertEquals("Hello, its your dummy servlet!!!", output);

        System.out.println("HTTP Code: " + responseCode + " Value: " + output);
        con.disconnect();
    }
}
