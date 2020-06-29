package org.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.generics.BotSession;

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
            HFBot hfbot = new HFBot();
            Stalker stalker = new Stalker();
            stalker.update();
            hfbot.setStalker(stalker);
            BotSession botsession = telegramBotsApi.registerBot(hfbot);
            System.out.println("HFBot online");
            //listen(botsession);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println("HFBot failed to start");
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }
    
    /*
    public static void listen(BotSession botsession) {
        new Thread(() -> {
            String input = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while ((input = reader.readLine()) != null) {
                    if (input.equalsIgnoreCase("exit")) {
                        botsession.stop();
                        System.out.println("HFBot offline");
                        reader.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    */
}