package org.patrick;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * @author Patrick
 * 
 * Class containing main method
 *
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Starting HFBot...");
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new HFBot());
            System.out.println("HFBot online");
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println("HFBot failed to start");
        }
    }
}