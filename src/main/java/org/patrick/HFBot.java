package org.patrick;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.telegram.telegrambots.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * @author Patrick
 * 
 * Telegram Bot
 *
 */
public class HFBot extends TelegramLongPollingBot {

        private static double[] blacklist = {40.0, 45.0, 70.6, 77.7, 87.0};
        
        private static Long chatIdHFB = -1001279920819L;
        
        private static String nameJohn = "John Simmit";
        private static String namePaul = "Paul Prügel";
        private static String nameMakarov = "Andrej Makarov";
        private static String nameVladimir = "Vladimir Iwanow";
        private static String nameHans = "Hans Flucht";       
        
        private static Pattern ffPattern = Pattern.compile("[0-9][0-9]\\.[0-9]");  
    
        @SuppressWarnings("deprecation")
        public void onUpdateReceived(Update update) {
            
            String receivedMsg = "";
            String messageText = "";            
            
            int messageId = 0;
            int receivedMsgId = 0;
            int messageUserId = 0;
            
            Long chatId = 0L;  
            
            double frequency = 0.0;
            
            JSONObject json = null;  
            
            PinChatMessage pinMessage = new PinChatMessage();             
            
            try {
                receivedMsg = update.getMessage().getText();
                receivedMsgId = update.getMessage().getMessageId();
                chatId = update.getMessage().getChatId();
                messageUserId = update.getMessage().getFrom().getId();
            } catch (NullPointerException e) {
                System.out.println("Caught a NullPointerException (likely edited msg)");
            } 
            
            if (receivedMsg != null) {                
            
                Matcher ffMatcher = ffPattern.matcher(receivedMsg);
                
                long startTime = System.nanoTime();
                
                if (receivedMsg.toLowerCase().startsWith("!hfb")) {
                    
                    ArrayList<String> stringList = new ArrayList<String>();
                    
                    json = readJSON("servers", "!HFB");
                
                    JSONArray jsonArrayData = json.getJSONArray("data");                
                    JSONArray jsonArrayPlayers = jsonArrayData.getJSONObject(0).getJSONArray("Players");
                
                    for (Object player : jsonArrayPlayers) {
                        if (player.toString().startsWith("[HFB]")) {
                            stringList.add(player.toString());
                        }
                    }
                
                    messageText = 
                            "Es sind " + Integer.toString(stringList.size()) + " HFB Mitglieder auf dem Server.\n";                    
                    
                    for (String player : stringList) {
                        messageText = messageText + player + "\n";
                    } 
                    
                    sendBotMessage(messageText, chatId, "!hfb");
                    
                } else if (receivedMsg.toLowerCase().startsWith("!geld")) {
                    if (receivedMsg.length() > "!geld".length()) {
                        String args = receivedMsg.split("\\s+")[1];
                        if (receivedMsg.split("\\s+").length > 2) {
                            args = args + " " + receivedMsg.split("\\s+")[2];
                        }
                        
                        String url = "player/";
                        Boolean check = false;
                        
                        if (nameJohn.toLowerCase().contains(args.toLowerCase())) {
                            url = url + Keys.getKeyJohn();
                            messageText = nameJohn;
                            check = true;
                        } else if (namePaul.toLowerCase().contains(args.toLowerCase())) {
                            url = url + Keys.getKeyPaul();
                            messageText = namePaul;
                            check = true;
                        } else if (nameMakarov.toLowerCase().contains(args.toLowerCase())) {
                            url = url + Keys.getKeyMakarov();
                            messageText = nameMakarov;
                            check = true;
                        } else if (nameVladimir.toLowerCase().contains(args.toLowerCase())) {
                            url = url + Keys.getKeyVladimir();
                            messageText = nameVladimir;
                            check = true;
                        } else if (nameHans.toLowerCase().contains(args.toLowerCase())) {
                            url = url + Keys.getKeyHans();
                            messageText = nameHans;
                            check = true;
                        }
                        
                        if (check) {
                            json = readJSON(url, "!geld");
                            JSONArray jsonArrayData = json.getJSONArray("data");
                            int balance = jsonArrayData.getJSONObject(0).getInt("bankacc");

                            messageText = messageText + " hat " + Integer.toString(balance) + "$ auf seinem Konto.";
                        } else {
                            messageText = "Von " + args
                                    + " ist kein API key vorhanden oder kann niemandem zugeordnet werden.";
                        } 
                    } else {
                        messageText = "Gib bitte einen Namen an dessen Kontostand du abfragen möchtest.";
                    }
                    
                    
                    sendBotMessage(messageText, chatId, "!geld");
                    
                } else if (receivedMsg.toLowerCase().startsWith("!server")) {
                    json = readJSON("servers", "!server");
                
                    JSONArray jsonArrayData = json.getJSONArray("data");                
                    JSONArray jsonArrayPlayers = jsonArrayData.getJSONObject(0).getJSONArray("Players");    
                    
                    if (receivedMsg.length() > "!server".length()) {
                        String args = receivedMsg.split("\\s+")[1];
                        if (receivedMsg.split("\\s+").length > 2) {
                            args = args + " " + receivedMsg.split("\\s+")[2];
                        }
                        messageText = args + " ist nicht auf dem Server.";
                        for (Object player : jsonArrayPlayers) {
                            if (player.toString().toLowerCase().contains(args.toLowerCase())) {
                                messageText = args + " ist auf dem Server.";
                                break;
                            }
                        } 
                    } else {
                        messageText = "Gib bitte einen Namen an dessen, online status du abfragen möchtest.";
                    }                    
                    
                    sendBotMessage(messageText, chatId, "!server"); 
                    
                } else if (receivedMsg.toLowerCase().startsWith("!ts")) {
                    json = readJSON("teamspeaks", "!ts");
                
                    JSONArray jsonArrayData = json.getJSONArray("data");                
                    JSONArray jsonArrayUsers = jsonArrayData.getJSONObject(0).getJSONArray("Users");    
                    
                    if (receivedMsg.length() > "!ts".length()) {
                        String args = receivedMsg.split("\\s+")[1];
                        if (receivedMsg.split("\\s+").length > 2) {
                            args = args + " " + receivedMsg.split("\\s+")[2];
                        }
                        messageText = args + " ist nicht auf dem TS.";
                        for (Object users : jsonArrayUsers) {
                            if (users.toString().toLowerCase().contains(args.toLowerCase())) {
                                messageText = args + " ist auf dem TS.";
                                break;
                            }
                        } 
                    } else {
                        messageText = "Gib bitte einen Namen an, dessen online status du abfragen möchtest.";
                    }
                    
                    
                    sendBotMessage(messageText, chatId, "!ts");
                    
                } else if (ffMatcher.find()) {
                    /**
                    message.disableNotification();                  
                    message.setChatId(chatId);                    
                   
                    try {
                        json = JsonReader.readJsonFromUrl("https://api.realliferpg.de/v1/servers");
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                
                    JSONArray jsonArrayData = json.getJSONArray("data");                
                    JSONArray jsonArrayPlayers = jsonArrayData.getJSONObject(0).getJSONArray("Players");    
                
                    int counter = 0;
                
                    for (Object player : jsonArrayPlayers) {
                        if (player.toString().startsWith("[HFB]")) {
                            counter++;
                        }
                    }                    
                
                    messageText = receivedMsg + " | HFB count: " + Integer.toString(counter);
                    **/
                                       
                    try {
                        /**
                        message.setText(messageText);
                        message.setChatId(chatId);   
                        messageId = sendMessage(message).getMessageId();
                        **/                      
                        
                        pinMessage.setChatId(chatId);
                        pinMessage.setMessageId(receivedMsgId);
                        //pinMessage.setMessageId(messageId);
                        pinMessage.setDisableNotification(true);
                        execute(pinMessage);
                        
                        /**
                        delMessage.setChatId(Long.toString(chatId));
                        delMessage.setMessageId(messageId + 1);
                        execute(delMessage);                         
                        
                        delMessage.setChatId(Long.toString(chatId));
                        delMessage.setMessageId(messageId - 1);
                        execute(delMessage);  
                        **/
                    } catch (TelegramApiException e) {
                        System.out.println("Error pinning message after finding ff");
                        e.printStackTrace();
                    }  
                    
                } else if (receivedMsg.equalsIgnoreCase("!ff")) {
                    do {
                        frequency = randomFrequency(30.0, 87.0);
                    } while (isBlacklisted(frequency));          
                    
                    messageId = sendBotMessage(String.valueOf(frequency), chatId, "!ff").getMessageId();
                    
                    try {                        
                        pinMessage.setChatId(chatId);
                        pinMessage.setMessageId(messageId);
                        pinMessage.setDisableNotification(true);
                        execute(pinMessage);
                    } catch (TelegramApiException e) {
                        System.out.println("Error pinning after !ff");
                        e.printStackTrace();
                    }
                    
                } else if (receivedMsg.toLowerCase().startsWith("!botmessage") && messageUserId == 555994770) {
                    String[] messageArray = receivedMsg.split("\\s+");
                    for (String word : messageArray) {
                        if (!word.equalsIgnoreCase("!botmessage")) {
                            messageText = messageText + " " + word;
                        }                        
                    }
                    
                    sendBotMessage(messageText, chatIdHFB, "!botmessage");
                    
                } else if (receivedMsg.toLowerCase().startsWith("!cops")) {
                    
                    ArrayList<String> stringList = new ArrayList<String>();
                    
                    json = readJSON("servers", "!cops");
                
                    JSONArray jsonArrayData = json.getJSONArray("data");                
                    JSONObject jsonObjectSide = jsonArrayData.getJSONObject(0).getJSONObject("Side");
                    JSONArray jsonArrayCops = jsonObjectSide.getJSONArray("Cops");
                
                    for (Object player : jsonArrayCops) {
                        if (!player.toString().startsWith("[Justiz]")) {
                            stringList.add(player.toString());
                        }
                    }
                
                    messageText = 
                            "Es sind " + Integer.toString(stringList.size()) + " Cops auf dem Server.\n";
                    
                    if (receivedMsg.length() > "!cops".length()) {
                        String args = receivedMsg.split("\\s+")[1]; 
                        if (args.toLowerCase().startsWith("list")) {
                            for (String player : stringList) {
                                messageText = messageText + player + "\n";
                            }
                        }
                    }  
                    
                    sendBotMessage(messageText, chatId, "!cops");
                    
                } else if (receivedMsg.equalsIgnoreCase("!help")) {
                    messageText = 
                            "!HFB = Zeigt an wie viele HFB Mitglieder auf dem Server sind und deren Namen.\n"
                            + "!cops (list) = Zeigt an wie viele Cops auf dem Server sind und wahlweise deren Namen.\n"
                            + "!ff = Legt eine neue Frequenz fest und pinnt sie.\n"                            
                            + "!server <Name> = Zeigt an ob <Name> auf dem Server ist.\n"
                            + "!ts <Name> = Zeigt an ob <Name> auf dem Teamspeak ist.\n"
                            + "!Geld <Name> = Zeigt an wie viel Geld <Name> (John/Paul/Makarov/Vladi/Hans) auf der Bank hat.\n"
                            + "!changelog (full) = Zeigt die letzte oder alle Änderung an.\n"
                            + "------------------------------------------------------------------\n"
                            + "Außerdem werden geschriebene Frequenzen automatisch gepinnt.\n"
                            + "Falls jemand Ideen für nützliche Funktionen hat bitte bei John Simmit aka Dipsy melden.";
                                   
                    sendBotMessage(messageText, chatId, "!help");
                    
                } else if (receivedMsg.toLowerCase().startsWith("!changelog")) {
                    messageText = 
                            ">>> 30.12.19 - 19:05 <<<\n"
                            + "-!cops (list) hinzugefügt.\n"
                            + "-!changelog (full) hinzugefügt.\n"
                            + "-!hfb zeigt jetzt immer alle Mitspieler an (@Remagy).\n"
                            + "-Code effizienter gemacht.\n";
                    
                    if (receivedMsg.length() > "!changelog".length()) {
                        String args = receivedMsg.split("\\s+")[1]; 
                        if (args.toLowerCase().startsWith("full")) {
                            messageText = messageText
                                    + ">>> 29.12.19 - 16:33 <<<\n"
                                    + "- !HFB akzeptiert jetzt \"list\" als Argument um alle anwesenden Mitglieder aufzulisten.\n"
                                    + "- API key von Hans Flucht hinzugefügt.\n"
                                    + "- Commands mit Variablen machen den Bot nicht mehr verrückt, wenn man die Variablen vergisst.\n"
                                    + "- Groß und Kleinschreibung sollte jetzt überall egal sein.\n"
                                    + ">>> 29.12.19 - 02:06 <<<\n"                            
                                    + "- !Geld command geupdated (siehe !help).\n"
                                    + "- !server und !ts commands achten nicht mehr auf Groß/Kleinschreibung.\n"
                                    + "- Dem Bot wurde ein Mund angenäht :)";                            
                        }
                    }                
                    
                    sendBotMessage(messageText, chatId, "!changelog");
                    
                }
                
                long endTime = System.nanoTime();
                System.out.println(endTime - startTime);
                
            }          
            
        }         
        
        private static double round(double value) {
            double tmpD = value * 10;
            long tmpL = Math.round(tmpD); 
            return (double) tmpL / 10;
        }
        
        private static double randomFrequency(double min, double max) {
            Random r = new Random();
            double frequenz = min + r.nextFloat() * (max - min);
            frequenz = round(frequenz);
            return frequenz;
        }
        
        private static boolean isBlacklisted(double frequenz) {
            for (double d: blacklist) {
                if (d == frequenz) {
                    return true;
                }                   
            }
            return false;
        }
        
        /**
         * This method exists so the code is less cluttered by try/catch
         */
        private static JSONObject readJSON(String url, String command) {
            JSONObject json = new JSONObject();
            
            try {
                json = JsonReader.readJsonFromUrl("https://api.realliferpg.de/v1/" + url);
            } catch (JSONException | IOException e) {
                System.out.println("Error reading json after " + command);
                e.printStackTrace();
            }
            
            return json;
        }
        
        /**
         * This method exists so the code is less cluttered by try/catch
         */
        @SuppressWarnings("deprecation")
        private Message sendBotMessage(String messageText, Long chatId, String command) {
            SendMessage sendMessage = new SendMessage();
            Message message = new Message();
            
            try {
                sendMessage.setText(messageText);
                sendMessage.setChatId(chatId);
                message = sendMessage(sendMessage);
            } catch (TelegramApiException e) {
                System.out.println("Error sending message after " + command);
                e.printStackTrace();
            }
            
            return message;
        }
        
        
        /**
         * Provides the username of the bot.
         * 
         * @return Name of the bot
         */
        public String getBotUsername() {
            return "Der_HFBot";
        }

        /**
         * Provides the API key of the bot.
         * 
         * @return API key of the bot.
         */
        public String getBotToken() {
            return Keys.getKeyTelegramBot();
        }
    }
