package org.patrick;

import java.io.IOException;

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
            
            String receivedMsg = "";
            
            int receivedMsgId = 0;
            int messageUserId = 0;
            
            Long chatId = 0L;          
            
            try {
                receivedMsg = update.getMessage().getText();
                receivedMsgId = update.getMessage().getMessageId();
                chatId = update.getMessage().getChatId();
                messageUserId = update.getMessage().getFrom().getId();
            } catch (NullPointerException e) {
                System.out.println("Caught a NullPointerException (likely edited msg)");
            }             
            
            if (receivedMsg != null) {                
                try {
                    Command.executeMatching(receivedMsg, chatId, receivedMsgId, messageUserId, this);
                } catch (JSONException | InputException | IOException | TelegramApiException e) {
                    e.printStackTrace();
                }               
            }           
        }
        
        @SuppressWarnings("deprecation")
        public Message sendBotMessage(String messageText, Long chatId) throws TelegramApiException {
            SendMessage sendMessage = new SendMessage(chatId, messageText);
            return sendMessage(sendMessage);        
        }
        
        public void pinBotMessage(Long chatId, int receivedMsgId) throws TelegramApiException {
            PinChatMessage pinMessage = new PinChatMessage(chatId, receivedMsgId);
            pinMessage.setDisableNotification(true);
            execute(pinMessage);
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