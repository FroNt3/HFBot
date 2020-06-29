package org.patrick;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//todo : implement methods from commands to here, siehe posi und geld
public final class ApiReader {
    
    public static ArrayList<String> getOnlinePlayers() throws JSONException, IOException {
        ArrayList<String> stringList = new ArrayList<String>();
        
        JSONObject json = JsonReader.readJsonFromUrl("https://api.realliferpg.de/v1/servers");            
        JSONArray jsonArrayData = json.getJSONArray("data");                
        JSONArray jsonArrayPlayers = jsonArrayData.getJSONObject(0).getJSONArray("Players"); 
        
        for (Object player : jsonArrayPlayers) {
            stringList.add(player.toString());
        } 
        
        return stringList;
    }
    
    public static JSONArray getOnlinePlayersJson() throws JSONException, IOException {
        JSONObject json = JsonReader.readJsonFromUrl("https://api.realliferpg.de/v1/servers");            
        JSONArray jsonArrayData = json.getJSONArray("data");                
        JSONArray jsonArrayPlayers = jsonArrayData.getJSONObject(0).getJSONArray("Players"); 
        
        return jsonArrayPlayers;
    }
    
    public static JSONArray getOnlineTSUsers() throws JSONException, IOException {        
        JSONObject json = JsonReader.readJsonFromUrl("https://api.realliferpg.de/v1/teamspeaks");          
        JSONArray jsonArrayData = json.getJSONArray("data");                
        JSONArray jsonArrayUsers = jsonArrayData.getJSONObject(0).getJSONArray("Users");  
        
        return jsonArrayUsers;
    }
    
    public static String getPosition(String key) throws JSONException, IOException {
        JSONArray jsonArrayData = getPlayerData(key);
        String pos = jsonArrayData.getJSONObject(0).getString("pos");
        
        return pos;
    }
    
    public static int getBalance(String key) throws JSONException, IOException {
        JSONArray jsonArrayData = getPlayerData(key);
        int balance = jsonArrayData.getJSONObject(0).getInt("bankacc");
        
        return balance;
    }
    
    public static JSONArray getPlayerData(String key) throws JSONException, IOException {
        String url = "https://api.realliferpg.de/v1/player/" + key;        
        JSONObject json = JsonReader.readJsonFromUrl(url);
        return json.getJSONArray("data");
    }
    
    private ApiReader() {        
    }

}