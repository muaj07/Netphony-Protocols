package es.tid.bgp.bgp4Peer.peer;
import org.json.simple.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ajmal on 2017-09-27.
 */
public class Create_CMS_Post {


    String EntryPoint;
    int domain;
    boolean localDomain=false;
    String BGPIdentifer;


    public Create_CMS_Post(String EntryPoint, int domain, boolean localDomain, String BGPIdentifier) {

        this.EntryPoint=EntryPoint;
        this.domain=domain;
        this.localDomain=localDomain;
        this.BGPIdentifer=BGPIdentifier;

        //System.out.println("BGP Identifier for Https: " +this.BGPIdentifer);



        try {

            URL url = new URL("http://" +this.BGPIdentifer +"/mdc/domain/");
            //URL url = new URL("http://192.168.56.102/mdc/domain/");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

           // String input = "{\"domain\":%$d,\"localDomain\":%b \"entryPoint\":%s}" ;
            JSONObject obj = new JSONObject();

            obj.put("domain", this.domain);
            obj.put("localDomain", this.localDomain);
            obj.put("entryPoint", this.EntryPoint);
            System.out.println(obj);

            OutputStream os = conn.getOutputStream();
            os.write(obj.toString().getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
            {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

}

