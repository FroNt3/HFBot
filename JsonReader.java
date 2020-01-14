package org.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Patrick, Roland Illig
 * 
 * Reads a JSON from an URL and parses it to JSON object
 * 
 */
public final class JsonReader {

    private static String readAll(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int cp;
        while ((cp = reader.read()) != -1) {
            stringBuilder.append((char) cp);
        }    
        return stringBuilder.toString();
    }

    /**
     * @param url to read the JSON from
     * @return json object
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream inputStream = new URL(url).openStream();
        try {
            BufferedReader bufferedReader = 
                    new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String jsonText = readAll(bufferedReader);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            inputStream.close();
        }
    }
    
    private JsonReader() {        
    }
    
}