package certh.iti.mklab.music.charts.extraction.framework.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vasgat
 */
public class HTTPRequest {

    private String url;
    private String response;
    private String urlParameters;
    private int responseCode;
    private Map<String, String> requestProperties;

    public HTTPRequest(String url) {
        this.url = url;
        this.urlParameters = "";
        this.response = "";
        this.responseCode = 404;
        this.requestProperties = new HashMap();
    }

    public int getStatus() {
        return this.responseCode;
    }

    public void GET(String method)
            throws UnsupportedEncodingException {
        if (this.urlParameters.equals("")) {
            this.url = (this.url + "/" + method);
        } else {
            this.url = (this.url + "/" + method + "?" + this.urlParameters);
        }
        sendGet();
    }

    public void POST(String method)
            throws IOException {
        if (this.urlParameters.equals("")) {
            this.url = (this.url + "/" + method);
        } else {
            this.url = (this.url + "/" + method + "?" + this.urlParameters);
        }
        sendPost();
    }

    public String getURL() {
        return this.url;
    }

    public void setRequestProperty(String key, String value) {
        this.requestProperties.put(key, value);
    }

    public String toString() {
        return this.url;
    }

    public String getResponse() {
        return this.response;
    }

    public void addParameter(String name, String value)
            throws UnsupportedEncodingException {
        if (this.urlParameters.equals("")) {
            this.urlParameters = (this.urlParameters + name + "=" + URLEncoder.encode(value, "ISO-8859-1"));
        } else {
            this.urlParameters = (this.urlParameters + "&" + name + "=" + URLEncoder.encode(value, "ISO-8859-1"));
        }
    }

    private void sendGet() {
        try {
            URL obj = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            connection.setRequestMethod("GET");

            connection.setRequestProperty("User-Agent", "User-Agent");

            this.responseCode = connection.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + this.url);
            System.out.println("Response Code : " + this.responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

            StringBuffer response = new StringBuffer();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            byte[] utf8 = new String(response.toString().getBytes(), "UTF-8").getBytes("ISO-8859-1");
            this.response = new String(utf8, "ISO-8859-1");
        } catch (MalformedURLException ex) {
            Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendPost()
            throws MalformedURLException, IOException {
        URL obj = new URL(this.url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", "User-Agent");
        for (Map.Entry<String, String> property : this.requestProperties.entrySet()) {
            connection.setRequestProperty((String) property.getKey(), (String) property.getValue());
        }
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.flush();
        wr.close();

        int responseCode = connection.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + this.url);
        System.out.println("Post parameters : " + this.urlParameters);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in;
        if (responseCode == 200) {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        } else {
            in = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
        }
        StringBuffer response = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
        this.response = response.toString();
    }
}
