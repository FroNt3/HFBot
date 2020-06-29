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
    
    MW("![mM][wW]") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, Stalker stalker, HFBot hfbot) 
                throws CustomException, JSONException, IOException, TelegramApiException {
            
            ArrayList<String> stringList = new ArrayList<String>();             
            JSONArray jsonArrayPlayers = ApiReader.getOnlinePlayersJson();
            
            for (Object player : jsonArrayPlayers) {
                if (player.toString().startsWith("[mW]")) {
                    stringList.add(player.toString());
                }
            }
        
            String messageText;
            if (stringList.size() == 1) {
                messageText = 
                        "Es ist 1 mW-Mitglied auf dem Server.\n";
            } else {
                messageText = 
                        "Es sind " + Integer.toString(stringList.size()) + " mW-Mitglieder auf dem Server.\n";
            }           
            
            for (String player : stringList) {
                messageText = messageText + player + "\n";
            } 
            
            hfbot.sendBotMessage(messageText, chatId);            
        }                
    },
    
    GANG("![gG][aA][nN][gG]\\s?(.*)") {    
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, Stalker stalker, HFBot hfbot)
                throws CustomException, JSONException, IOException, TelegramApiException {
            
            ArrayList<String> stringList = new ArrayList<String>();
            
            String tag = matcher.group(1);
                         
            JSONArray jsonArrayPlayers = ApiReader.getOnlinePlayersJson();
        
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
    
    GELD("![gG][eE][lL][dD]\\s?(.*)") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, Stalker stalker, HFBot hfbot)
                throws CustomException, JSONException, IOException, TelegramApiException { 
            
            String name = matcher.group(1);   
            String messageText = "";                      
            Person person = stalker.getPerson(name);
            
            if (person == null) {
                messageText = "Von " + name
                        + " ist kein API key vorhanden oder kann niemandem zugeordnet werden.";
            } else {
                int balance = ApiReader.getBalance(person.getKey());                
                String balanceFormat = Calc.formatInt(balance);

                messageText = person.getName() + " hat " + balanceFormat + "$ auf seinem Konto.";
            }
            
            hfbot.sendBotMessage(messageText, chatId);     
        }        
    },
    
    SERVER("![sS][eE][rR][vV][eE][rR]\\s?(.*)") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, Stalker stalker, HFBot hfbot)
                throws CustomException, JSONException, IOException, TelegramApiException {   
            
            String name = matcher.group(1);
            
            JSONArray jsonArrayPlayers = ApiReader.getOnlinePlayersJson();  
            
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
    
    TS("![tT][sS]\\s?(.*)") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, Stalker stalker, HFBot hfbot)
                throws CustomException, JSONException, IOException, TelegramApiException {
                        
            String name = matcher.group(1);
             
            JSONArray jsonArrayUsers = ApiReader.getOnlineTSUsers();    
            
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
    
    FF("![fF][fF]\\s?((blacklist)\\s(list|get|add|delete)\\s?([0-9]+\\.[0-9])?|(range)\\s(get|set)(\\s?(min|max)\\s([0-9]+\\.[0-9]))?)?") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, Stalker stalker, HFBot hfbot)
                throws CustomException, JSONException, IOException, TelegramApiException {
            
            if (matcher.group(0).length() > "!ff".length()) {
                if (!(matcher.group(2) == null) && matcher.group(2).equals("blacklist")) {
                    String messageText = "";
                    
                    if (matcher.group(3).equals("list") || matcher.group(3).equals("get")) {
                        
                        ArrayList<String> content = FileHandler.readFile(Frequency.getBlacklistPath());
                        messageText = "Folgende Frequenzen sind blacklisted:\n";
                        for (String ffString : content) {
                            messageText = messageText + ffString + " ";
                        }
                        
                    } else if (matcher.group(3).equals("add")) {
                        
                        ArrayList<String> content = FileHandler.readFile(Frequency.getBlacklistPath());
                        String ffAdd = matcher.group(4);
                        boolean check = false;
                        
                        for (String ffString : content) {
                            if (ffString.equals(ffAdd)) {
                                check = true;
                            }
                        }
                        
                        if (check) {
                            messageText = "Frequenz ist bereits blacklisted.";
                        } else {
                            content.add(ffAdd);
                            FileHandler.writeFile(Frequency.getBlacklistPath(), content);
                            messageText = ffAdd + " wurde der Blacklist hinzugefuegt.";
                        }
                        
                    } else if (matcher.group(3).equals("delete")) {
                        
                        ArrayList<String> content = FileHandler.readFile(Frequency.getBlacklistPath());
                        String ffDel = matcher.group(4);
                        
                        for (String ffString : content) {
                            if (ffString.equals(ffDel)) {
                                content.remove(ffDel);
                                FileHandler.writeFile(Frequency.getBlacklistPath(), content);
                                messageText = ffDel + " wurde von der Blacklist entfernt.";
                                break;
                            }
                        }
                    } else {
                        messageText = "Unbekannter Parameter";
                    }

                    hfbot.sendBotMessage(messageText, chatId);
                    
                } else if (matcher.group(5).equals("range")) {
                    
                    double min = Frequency.getMinFF();
                    double max = Frequency.getMaxFF();
                    
                    String messageText = "";
                    
                    if (matcher.group(6).equals("get")) {     
                        
                        messageText = 
                                "Frequenzen werden zwischen " + min + " - " + max + " generiert.\n";
                    } else if (matcher.group(6).equals("set")) {
                        
                        if (matcher.group(8).equals("min")) {
                            min = Double.parseDouble(matcher.group(9));
                        } else if (matcher.group(8).equals("max")) {
                            max = Double.parseDouble(matcher.group(9));
                        } else {
                            System.out.println("Unknown Parameter " + matcher.group(8));
                        }
                        
                        ArrayList<String> content = new ArrayList<String>();
                        content.add(Double.toString(min));
                        content.add(Double.toString(max));
                        FileHandler.writeFile(Frequency.getCfgPath(), content);
                        messageText = 
                                "Frequenzen werden nun zwischen " + min + " - " + max + " generiert.\n";
                    } else {
                        messageText = "Unbekannter Parameter";
                    }
                    
                    hfbot.sendBotMessage(messageText, chatId);
                }
            } else {
                
                double frequency = Frequency.getFrequency();
                
                int messageId = hfbot.sendBotMessage(String.valueOf(frequency), chatId).getMessageId();
                hfbot.pinBotMessage(chatId, messageId);
                
            }                
        }        
    },
    
    BOTMESSAGE("![bB][oO][tT][mM][eE][sS][sS][aA][gG][eE] (.*)") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, Stalker stalker, HFBot hfbot)
                throws CustomException, JSONException, IOException, TelegramApiException {

            //Long chatIdHFB = -1001279920819L;
            Long chatIdHFB = -1001244020466L;
            if (messageUserId == 555994770) {
                hfbot.sendBotMessage(matcher.group(1), chatIdHFB);
            }
        }        
    },
    
    COPS("![cC][oO][pP][sS]?( liste?)?") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, Stalker stalker, HFBot hfbot)
                throws CustomException, JSONException, IOException, TelegramApiException {
            
            ArrayList<String> stringList = new ArrayList<String>();
            
            JSONObject json = JsonReader.readJsonFromUrl("https://api.realliferpg.de/v1/servers");
        
            JSONArray jsonArrayData = json.getJSONArray("data");                
            JSONObject jsonObjectSide = jsonArrayData.getJSONObject(0).getJSONObject("Side");
            JSONArray jsonArrayCops = jsonObjectSide.getJSONArray("Cops");
        
            for (Object player : jsonArrayCops) {
                if (!player.toString().startsWith("[Justiz]")) {
                    stringList.add(player.toString());
                }
            }
        
            String messageText = 
                    "Es sind " + Integer.toString(stringList.size()) + " Cops auf dem Server.\n";
            
            if (matcher.group(0).length() > "!cops".length()) {
                for (String player : stringList) {
                        messageText = messageText + player + "\n";                    
                }
            }  
            
            hfbot.sendBotMessage(messageText, chatId);
        }        
    },
    
    POSI("![pP][oO][sS][iI] (.*)") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, Stalker stalker, HFBot hfbot)
                throws CustomException, JSONException, IOException, TelegramApiException {

            Map map = new Map("map.png", 2048);            
            String name = matcher.group(1);
            String text = "";
            
            stalker.update();
            
            if (name.equalsIgnoreCase("all")) {
                ArrayList<Person> personList = stalker.getPersonList();
                int x = 0;
                int y = 0;
                for (Person person : personList) {
                    x = map.relativizePos(person.getXPos(), 16000.0);
                    y = map.relativizePos(person.getYPos(), 16000.0);
                    text = person.getName();
                    if (!person.getStatus()) {
                        text = text + " [off]";
                    }
                    map.drawPos(x, y, 15, text); 
                }
                map.export();
                hfbot.sendPhoto(chatId, "map_hfb.png");
            } else {
                Person person = stalker.getPerson(name);
                if (person == null) {
                    String messageText = "Von " + name
                            + " ist kein API key vorhanden oder kann niemandem zugeordnet werden.";
                    hfbot.sendBotMessage(messageText, chatId); 
                } else {
                    int x = map.relativizePos(person.getXPos(), 16000.0);
                    int y = map.relativizePos(person.getYPos(), 16000.0);
                    text = person.getName();
                    if (!person.getStatus()) {
                        text = text + " [off]";
                    }
                    map.drawPos(x, y, 15, text); 
                    map.export();
                    hfbot.sendPhoto(chatId, "map_hfb.png");
                }
            }
        }        
    },
    
    
    NOCOMMAND_FF("[0-9][0-9]\\.[0-9]") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, Stalker stalker, HFBot hfbot)
                throws CustomException, JSONException, IOException, TelegramApiException {

            hfbot.pinBotMessage(chatId, receivedMsgId);            
        }        
    },
    
    HELP("![hH][eE][lL][pP]\\s?(.*)") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, Stalker stalker, HFBot hfbot)
                throws CustomException, JSONException, IOException, TelegramApiException {
            
            
            String messageText = 
                    "!mw = Zeigt an wie viele mW Mitglieder auf dem Server sind und deren Namen.\n"
                    + "!posi <name> = Zeigt an wo auf der Karte die Person ist, name=all zeigt alle an\n"
                    + "!cops (list) = Zeigt an wie viele Cops auf dem Server sind und wahlweise deren Namen.\n"
                    + "!ff = Legt eine neue Frequenz fest und pinnt sie.\n"   
                    + "!ff blacklist list = Listet geblacklistete Frequenzen auf.\n"
                    + "!ff blacklist add/delete <Frequenz> = Löscht/Added Frequenz von/zur Blacklist.\n"
                    + "!ff range get = Zeigt in welchem Bereich Frequenzen generiert werden.\n"
                    + "!ff range set min/max <Frequenz> = Legt min/max Frequenz Bereich fest.\n"
                    + "!server <Name> = Zeigt an ob <Name> auf dem Server ist.\n"
                    + "!ts <Name> = Zeigt an ob <Name> auf dem Teamspeak ist.\n"
                    + "!gang <Tag> = Zeigt an wie viele Gangmitglieder von <Tag> auf dem Server sind.\n"
                    + "!Geld <Name> = Zeigt an wie viel Geld <Name> auf der Bank hat.\n"
                    + "!changelog (full) = Zeigt die letzte oder alle Änderung an.\n"
                    + "------------------------------------------------------------------\n"
                    + "Außerdem werden geschriebene Frequenzen automatisch gepinnt.\n"
                    + "Falls jemand Ideen für nützliche Funktionen hat bitte bei John Simmit/Dipsy/Paddy melden.";
                        
        hfbot.sendBotMessage(messageText, chatId);
        }
    },
    
    CHANGELOG("![cC][hH][aA][nN][gG][eE][lL][oO][gG]( full)?") {
        @Override
        public void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, Stalker stalker, HFBot hfbot)
                throws CustomException, JSONException, IOException, TelegramApiException {
            
            String messageText = 
                    ">>> 11.06.20 - 01:07 <<<\n"
                    + "- !posi <name> hinzugefügt\n"
                    + "- Architektur Zeugs\n";
                    
            
            if (matcher.group(0).length() > "!changelog".length()) {
                messageText = messageText
                            + ">>> 09.06.20 - 16:02 <<<\n"
                            + "- Die Möglichkeit hinzugefügt die Blacklist und "
                            + "die Range von !ff zu konfigurieren\n"
                            + "- API Key von Coleman hinzugefügt\n"
                            + ">>> 04.01.20 - 00:19 <<<\n"
                            + "- !gang <Tag> hinzugefügt.\n"
                            + ">>> 01.01.20 - 18:12 <<<\n"
                            + "- Code Architektur komplett Überarbeitet.\n"
                            + "- !geld hat jetzt formatierte Zahlen.\n"
                            + "- github repo: https://github.com/FroNt3/HFBot/tree/master/src/main/java/org/patrick\n"
                            + ">>> 30.12.19 - 19:05 <<<\n"
                            + "- !cops (list) hinzugefÜgt.\n"
                            + "- !changelog (full) hinzugefÜgt.\n"
                            + "- !hfb zeigt jetzt immer alle Mitspieler an (@Remagy).\n"
                            + "- Code effizienter gemacht.\n"
                            + ">>> 29.12.19 - 16:33 <<<\n"
                            + "- !HFB akzeptiert jetzt \"list\" als Argument "
                            + "um alle anwesenden Mitglieder aufzulisten.\n"
                            + "- API key von Hans Flucht hinzugefÜgt.\n"
                            + "- Commands mit Variablen machen den Bot nicht mehr verrückt, "
                            + "wenn man die Variablen vergisst.\n"
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
    private String info;
    
    /**
     * Constructs a new command.
     *
     * @param pattern The regex pattern for the command to validate its usability.
     */
    Command(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }
    
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * Checks an input against the commands above and calls the command if one is found.
     * 
     * @throws CustomException Provides an error message if no command matches.
     */
    public static void executeMatching(String receivedMsg, Long chatId, int receivedMsgId, int messageUserId, Stalker stalker, HFBot hfbot) 
            throws JSONException, IOException, TelegramApiException, CustomException {
        
        for (Command command : Command.values()) {
            Matcher matcher = command.pattern.matcher(receivedMsg);
            if (matcher.matches()) {
                command.execute(matcher, chatId, receivedMsgId, messageUserId, stalker, hfbot);
            }
        }
    }
    
    /**
     * Executes a command.
     *
     * @param matcher The regex matcher that contains the groups of input of the command.
     * @throws CustomException To catch if an error occurs while trying to execute the command.
     */
    public abstract void execute(MatchResult matcher, Long chatId, int receivedMsgId, int messageUserId, Stalker stalker, HFBot hfbot) 
            throws CustomException, JSONException, IOException, TelegramApiException;
    
    /**
     * @return Whether string2 is within string1 or not
     */
    private static boolean containsToLowerCase(String string1, String string2) {
        return string1.toLowerCase().contains(string2.toLowerCase());
    }
}
