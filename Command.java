package org.patrick;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.telegrambots.exceptions.TelegramApiException;

//throw new InputException("Need to add exceptions");

/**
 * @author Patrick
 * 
 * Commands of the HFBot
 *
 */
public enum Command {    
    
    HFB("![hH][fF][bB]") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, HFBot hfbot) 
                throws InputException, JSONException, IOException, TelegramApiException {
            
            ArrayList<String> stringList = new ArrayList<String>();

            JSONObject json = JsonReader.readJsonFromUrl("https://api.realliferpg.de/v1/servers");
        
            JSONArray jsonArrayData = json.getJSONArray("data");                
            JSONArray jsonArrayPlayers = jsonArrayData.getJSONObject(0).getJSONArray("Players");
            
            /**
            String updatedTime = jsonArrayData.getJSONObject(0).getJSONObject("updated_at").getString("date");    
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();  
            String currentTime = dtf.format(now);            
            
            updatedTime = updatedTime.replaceFirst("\\.[0]*", "");
            updatedTime = updatedTime.replaceFirst("[0-9]*-[0-9]*-[0-9]* ", "");
            
            LocalTime t1 = LocalTime.parse(updatedTime);
            LocalTime t2 = LocalTime.parse(currentTime);
            Duration diff = Duration.between(t1, t2);
            **/
            
            for (Object player : jsonArrayPlayers) {
                if (player.toString().startsWith("[HFB]")) {
                    stringList.add(player.toString());
                }
            }
        
            String messageText = 
                    /**"Stand von vor " + diff.toSeconds() + " Sekunden" + "\n**/"Es sind " + Integer.toString(stringList.size()) + " HFB Mitglieder auf dem Server.\n";                    
            
            for (String player : stringList) {
                messageText = messageText + player + "\n";
            } 
            
            hfbot.sendBotMessage(messageText, chatId);            
        }                
    },
    
    GANG("![gG][aA][nN][gG]\\s?([a-zA-Z0-9äöüÄÖÜß]*)") {    
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, HFBot hfbot)
                throws InputException, JSONException, IOException, TelegramApiException {
            
            ArrayList<String> stringList = new ArrayList<String>();
            
            String tag = matcher.group(1);
            
            JSONObject json = JsonReader.readJsonFromUrl("https://api.realliferpg.de/v1/servers");
            
            JSONArray jsonArrayData = json.getJSONArray("data");                
            JSONArray jsonArrayPlayers = jsonArrayData.getJSONObject(0).getJSONArray("Players");
        
            for (Object player : jsonArrayPlayers) {
                if (player.toString().toLowerCase().startsWith("[" + tag.toLowerCase() + "]")) {
                    stringList.add(player.toString());
                }
            }
            
            String messageText = 
                    "Es sind " + Integer.toString(stringList.size()) + " " + tag + " Mitglieder auf dem Server.\n";                    
            
            for (String player : stringList) {
                messageText = messageText + player + "\n";
            } 
            
            hfbot.sendBotMessage(messageText, chatId);
            
        }
    },
    
    GELD("![gG][eE][lL][dD]\\s?([a-zA-Z0-9äöüÄÖÜß]*)(\\s?)([a-zA-Z0-9äöüÄÖÜß]*)") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, HFBot hfbot)
                throws InputException, JSONException, IOException, TelegramApiException {            
            
            String firstName = matcher.group(1);
            String space = matcher.group(2);
            String secondName = matcher.group(3);            
            String name = firstName + space + secondName;
            
            String url = "https://api.realliferpg.de/v1/player/";
            String messageText = "";
            
            String[] keyJohn = Keys.getKeyJohn();
            String[] keyPaul = Keys.getKeyPaul();
            String[] keyMakarov = Keys.getKeyMakarov();
            String[] keyVladimir = Keys.getKeyVladimir();
            String[] keyHans = Keys.getKeyHans();   
            
            Boolean check = false;
            
            if (containsToLowerCase(keyJohn[0], name)) {
                url = url + keyJohn[1];
                messageText = keyJohn[0];
                check = true;
            } else if (containsToLowerCase(keyPaul[0], name)) {
                url = url + keyPaul[1];
                messageText = keyPaul[0];
                check = true;
            } else if (containsToLowerCase(keyMakarov[0], name)) {
                url = url + keyMakarov[1];
                messageText = keyMakarov[0];
                check = true;
            } else if (containsToLowerCase(keyVladimir[0], name)) {
                url = url + keyVladimir[1];
                messageText = keyVladimir[0];
                check = true;
            } else if (containsToLowerCase(keyHans[0], name)) {
                url = url + keyHans[1];
                messageText = keyHans[0];
                check = true;
            }
            
            if (check) {
                JSONObject json = JsonReader.readJsonFromUrl(url);
                JSONArray jsonArrayData = json.getJSONArray("data");
                int balance = jsonArrayData.getJSONObject(0).getInt("bankacc");
                
                String balanceUnformat = Integer.toString(balance);
                String balanceReverse = "";
                String balanceFormat = "";
                int counter = 0;
                
                for (int i = (balanceUnformat.length()-1); i>=0; i--) {
                    balanceReverse = balanceReverse + balanceUnformat.charAt(i);
                    if (++counter == 3 && i != 0) {
                        balanceReverse = balanceReverse + ".";
                        counter = 0;
                    }
                }
                
                for (int i = (balanceReverse.length()-1); i>=0; i--) {
                    balanceFormat = balanceFormat + balanceReverse.charAt(i);
                }

                messageText = messageText + " hat " + balanceFormat + "$ auf seinem Konto.";
            } else {
                messageText = "Von " + name
                        + " ist kein API key vorhanden oder kann niemandem zugeordnet werden.";
            } 
            
            hfbot.sendBotMessage(messageText, chatId);             
        }        
    },
    
    SERVER("![sS][eE][rR][vV][eE][rR]\\s?([a-zA-Z0-9äöüÄÖÜß]*)(\\s?)([a-zA-Z0-9äöüÄÖÜß]*)") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, HFBot hfbot)
                throws InputException, JSONException, IOException, TelegramApiException {           
            
            String firstName = matcher.group(1);
            String space = matcher.group(2);
            String secondName = matcher.group(3);            
            String name = firstName + space + secondName;
            
            JSONObject json = JsonReader.readJsonFromUrl("https://api.realliferpg.de/v1/servers");            
            JSONArray jsonArrayData = json.getJSONArray("data");                
            JSONArray jsonArrayPlayers = jsonArrayData.getJSONObject(0).getJSONArray("Players");    
            
            String messageText = name + " ist nicht auf dem Server.";
            for (Object player : jsonArrayPlayers) {
                if (containsToLowerCase(player.toString(), name)) {
                    messageText = player.toString() + " ist auf dem Server.";
                    break;
                }
            }                   
            
            hfbot.sendBotMessage(messageText, chatId);             
        }        
    },
    
    TS("![tT][sS]\\s?([a-zA-Z0-9äöüÄÖÜß]*)(\\s?)([a-zA-Z0-9äöüÄÖÜß]*)") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, HFBot hfbot)
                throws InputException, JSONException, IOException, TelegramApiException {
                        
            String firstName = matcher.group(1);
            String space = matcher.group(2);
            String secondName = matcher.group(3);            
            String name = firstName + space + secondName;
            
            JSONObject json = JsonReader.readJsonFromUrl("https://api.realliferpg.de/v1/teamspeaks");          
            JSONArray jsonArrayData = json.getJSONArray("data");                
            JSONArray jsonArrayUsers = jsonArrayData.getJSONObject(0).getJSONArray("Users");    
            
            String messageText = name + " ist nicht auf dem TS.";
            for (Object users : jsonArrayUsers) {
                if (containsToLowerCase(users.toString(), name)) {
                    messageText = users.toString() + " ist auf dem TS.";
                    break;
                }
            }
            
            hfbot.sendBotMessage(messageText, chatId);             
        }        
    },
    
    FF("![fF][fF]") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, HFBot hfbot)
                throws InputException, JSONException, IOException, TelegramApiException {

            double frequency = Frequency.getFrequency();
            
            int messageId = hfbot.sendBotMessage(String.valueOf(frequency), chatId).getMessageId();
            hfbot.pinBotMessage(chatId, messageId);
        }        
    },
    
    BOTMESSAGE("![bB][oO][tT][mM][eE][sS][sS][aA][gG][eE] (.*)") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, HFBot hfbot)
                throws InputException, JSONException, IOException, TelegramApiException {

            Long chatIdHFB = -1001279920819L;
            if (messageUserId == 555994770) {
                hfbot.sendBotMessage(matcher.group(1), chatIdHFB);
            }
        }        
    },
    
    COPS("![cC][oO][pP][sS]( liste?)?") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, HFBot hfbot)
                throws InputException, JSONException, IOException, TelegramApiException {
            
            ArrayList<String> stringList = new ArrayList<String>();
            
            JSONObject json = JsonReader.readJsonFromUrl("https://api.realliferpg.de/v1/servers");
        
            JSONArray jsonArrayData = json.getJSONArray("data");                
            JSONObject jsonObjectSide = jsonArrayData.getJSONObject(0).getJSONObject("Side");
            JSONArray jsonArrayCops = jsonObjectSide.getJSONArray("Cops");
            
            /**
            String updatedTime = jsonArrayData.getJSONObject(0).getJSONObject("updated_at").getString("date");    
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();  
            String currentTime = dtf.format(now);            
            
            updatedTime = updatedTime.replaceFirst("\\.[0]*", "");
            updatedTime = updatedTime.replaceFirst("[0-9]*-[0-9]*-[0-9]* ", "");
            
            LocalTime t1 = LocalTime.parse(updatedTime);
            LocalTime t2 = LocalTime.parse(currentTime);
            Duration diff = Duration.between(t1, t2);
            **/
        
            for (Object player : jsonArrayCops) {
                if (!player.toString().startsWith("[Justiz]")) {
                    stringList.add(player.toString());
                }
            }
        
            String messageText = 
                    /**"Stand von vor " + diff.toSeconds() + " Sekunden" + "\n**/"Es sind " + Integer.toString(stringList.size()) + " Cops auf dem Server.\n";
            
            if (matcher.group(0).length() > "!cops".length()) {
                for (String player : stringList) {
                        messageText = messageText + player + "\n";                    
                }
            }  
            
            hfbot.sendBotMessage(messageText, chatId);
        }        
    },
    
    NOCOMMAND_FF("[0-9][0-9]\\.[0-9]") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, HFBot hfbot)
                throws InputException, JSONException, IOException, TelegramApiException {

            hfbot.pinBotMessage(chatId, receivedMsgId);            
        }        
    },
    
    HELP("![hH][eE][lL][pP]") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, HFBot hfbot)
                throws InputException, JSONException, IOException, TelegramApiException {
            
            String messageText = 
                    "!HFB = Zeigt an wie viele HFB Mitglieder auf dem Server sind und deren Namen.\n"
                    + "!cops (list) = Zeigt an wie viele Cops auf dem Server sind und wahlweise deren Namen.\n"
                    + "!ff = Legt eine neue Frequenz fest und pinnt sie.\n"                            
                    + "!server <Name> = Zeigt an ob <Name> auf dem Server ist.\n"
                    + "!ts <Name> = Zeigt an ob <Name> auf dem Teamspeak ist.\n"
                    + "!gang <Tag> = Zeigt an wie viele Gangmitglieder von <Tag> auf dem Server sind.\n"
                    + "!Geld <Name> = Zeigt an wie viel Geld <Name> (John/Paul/Makarov/Vladi/Hans) auf der Bank hat.\n"
                    + "!changelog (full) = Zeigt die letzte oder alle Änderung an.\n"
                    + "------------------------------------------------------------------\n"
                    + "Außerdem werden geschriebene Frequenzen automatisch gepinnt.\n"
                    + "Falls jemand Ideen für nützliche Funktionen hat bitte bei John Simmit aka Dipsy melden.";
                           
            hfbot.sendBotMessage(messageText, chatId);
        }        
    },
    
    CHANGELOG("![cC][hH][aA][nN][gG][eE][lL][oO][gG]( full)?") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, HFBot hfbot)
                throws InputException, JSONException, IOException, TelegramApiException {
            
            String messageText = 
                    ">>> 04.01.20 - 00:19 <<<\n"
                    + "- !gang <Tag> hinzugefügt.\n";
                    
            
            if (matcher.group(0).length() > "!changelog".length()) {
                messageText = messageText
                            + ">>> 01.01.20 - 18:12 <<<\n"
                            + "-Code Architektur komplett Überarbeitet.\n"
                            + "-!geld hat jetzt formatierte Zahlen.\n"
                            + "-github repo: https://github.com/FroNt3/HFBot/tree/master/src/main/java/org/patrick\n"
                            + ">>> 30.12.19 - 19:05 <<<\n"
                            + "-!cops (list) hinzugefÜgt.\n"
                            + "-!changelog (full) hinzugefÜgt.\n"
                            + "-!hfb zeigt jetzt immer alle Mitspieler an (@Remagy).\n"
                            + "-Code effizienter gemacht.\n"
                            + ">>> 29.12.19 - 16:33 <<<\n"
                            + "- !HFB akzeptiert jetzt \"list\" als Argument um alle anwesenden Mitglieder aufzulisten.\n"
                            + "- API key von Hans Flucht hinzugefÜgt.\n"
                            + "- Commands mit Variablen machen den Bot nicht mehr verrückt, wenn man die Variablen vergisst.\n"
                            + "- Groß und Kleinschreibung sollte jetzt überall egal sein.\n"
                            + ">>> 29.12.19 - 02:06 <<<\n"                            
                            + "- !Geld command geupdated (siehe !help).\n"
                            + "- !server und !ts commands achten nicht mehr auf Groß/Kleinschreibung.\n"
                            + "- Dem Bot wurde ein Mund angenäht :)";    
            }               
            
            hfbot.sendBotMessage(messageText, chatId);             
        }        
    };
    
    
    private Pattern pattern;
    
    /**
     * Constructs a new command.
     *
     * @param pattern The regex pattern for the command to validate its usability.
     */
    Command(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }
    
    /**
     * Checks an input against the commands above and calls the command if one is found.
     *
     * @param receivedMsg The user input.
     * @param board The board the command should be run on.
     * @return The command that got executed.
     * @throws InputException Provides an error message if no command matches.
     */
    public static void executeMatching(String receivedMsg, Long chatId, int receivedMsgId, int messageUserId, HFBot hfbot) 
            throws JSONException, IOException, TelegramApiException, InputException {
        
        for (Command command : Command.values()) {
            Matcher matcher = command.pattern.matcher(receivedMsg);
            if (matcher.matches()) {
                command.execute(matcher, chatId, receivedMsgId, messageUserId, hfbot);
            }
        }
    }
    
    /**
     * Executes a command.
     *
     * @param matcher The regex matcher that contains the groups of input of the command.
     * @param board The board the command should be run on.
     * @throws InputException To catch if an error occurs while trying to execute the command.
     */
    public abstract void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, HFBot hfbot) 
            throws InputException, JSONException, IOException, TelegramApiException;
    
    /**
     * @return Whether string2 is within string1 or not
     */
    private static boolean containsToLowerCase(String string1, String string2) {
        return string1.toLowerCase().contains(string2.toLowerCase());
    }
}
