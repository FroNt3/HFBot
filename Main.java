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

    /**
     * @param args from the command line
     */
    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        System.out.println("Starting HFBot...");
        try {
            telegramBotsApi.registerBot(new HFBot());
            System.out.println("HFBot online");
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println("HFBot failed to start");
        }


    }
}
