package org.patrick;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONException;
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
    
        public void onUpdateReceived(Update update) {
            
            Message message = null;
            
            String receivedMsg = "";
            
            int receivedMsgId = 0;
            int messageUserId = 0;
            
            Long chatId = 0L;  
            
            try {    
                message = update.getMessage();
                if (message == null) {
                    message = update.getEditedMessage();
                }               
                
                receivedMsg = message.getText();
                receivedMsgId = message.getMessageId();
                chatId = message.getChatId();
                messageUserId = message.getFrom().getId();
                
                if (chatId != -1001279920819L && chatId != 555994770L) {
                    System.out.println(
                            "chatID: " + chatId + " userId: " + messageUserId + " Message: " + receivedMsg);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }             
            
            if (receivedMsg != null) {                
                try {
                    Command.executeMatching(receivedMsg, chatId, receivedMsgId, messageUserId, this);
                } catch (JSONException | InputException | IOException | TelegramApiException e) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
                    LocalDateTime now = LocalDateTime.now();  
                    String currentTime = dtf.format(now);
                    System.out.println(currentTime);
                    e.printStackTrace();
                }               
            }
        }
        
        public Message sendBotMessage(String messageText, Long chatId) throws TelegramApiException {
            SendMessage sendMessage = new SendMessage(chatId, messageText);
            return execute(sendMessage);        
        }
        
        public boolean pinBotMessage(Long chatId, int receivedMsgId) throws TelegramApiException {
            PinChatMessage pinMessage = new PinChatMessage(chatId, receivedMsgId);
            pinMessage.setDisableNotification(true);
            return execute(pinMessage);
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
            return PrivateInfo.getKeyTelegramBot();
        }
    }