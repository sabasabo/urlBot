import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Optional;

public class Bot {

    private static final String EXTERNAL_URL = System.getenv("HEROKU_URL") + System.getenv("TOKEN");
    private static final String INTERNAL_URL_AND_PORT = "https://0.0.0.0:" + Optional.ofNullable(System.getenv("PORT")).orElse("8080");

    private static class webhookHandler extends TelegramWebhookBot {

        public BotApiMethod onWebhookUpdateReceived(Update update) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(update.getMessage().getChatId().toString());
                String text = update.getMessage().getText();
                sendMessage.setText(text.substring(0, Math.min(text.indexOf('?'), text.length())));
                return sendMessage;
            }
            return null;
        }

        public String getBotUsername() {
            return "urlRemoveParametersBot";
        }

        public String getBotToken() {
            return System.getenv("TOKEN");
        }

        public String getBotPath() {
            return EXTERNAL_URL;
        }
    }

    public static void main(String[] args) throws TelegramApiRequestException {
        System.out.println("EXTERNAL_URL " + EXTERNAL_URL);
        System.out.println("INTERNAL_URL_AND_PORT " + INTERNAL_URL_AND_PORT);
        System.out.println("port should be " + System.getenv("PORT"));
        ApiContextInitializer.init();
        new TelegramBotsApi(EXTERNAL_URL, INTERNAL_URL_AND_PORT).registerBot(new webhookHandler());
    }
}
