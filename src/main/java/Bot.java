import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.util.WebhookUtils;

import java.util.Optional;


public class Bot {

    private static final String EXTERNAL_URL = "https://" + System.getenv("HEROKU_URL_PREFIX") + ".herokuapp.com/";
    private static final String INTERNAL_URL_AND_PORT = "https://0.0.0.0:" + Optional.ofNullable(System.getenv("PORT")).orElse("8080");

    private static class WebhookHandler extends TelegramWebhookBot {

        public BotApiMethod onWebhookUpdateReceived(Update update) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                System.out.println("received message");
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(update.getMessage().getChatId().toString());
                String text = update.getMessage().getText();
                text = removeUrlParamters(text);
                text = removePreHttpChars(text);
                sendMessage.setText(text);
                return sendMessage;
            }
            return null;
        }

        private String removePreHttpChars(String text) {
            if (!text.contains("http")) {
                return text;
            }
            return text.substring(text.indexOf("http"));
        }

        private String removeUrlParamters(String text) {
            if (!text.contains("?")) {
                return text;
            }
            return text.substring(0, text.indexOf('?'));
        }

        public String getBotUsername() {
            return System.getenv("BOT_USER_NAME");
        }

        public String getBotToken() {
            return System.getenv("TOKEN");
        }

        public String getBotPath() {
            return System.getenv("TOKEN");
        }
    }

    public static void main(String[] args) throws TelegramApiRequestException {
        System.out.println("EXTERNAL_URL " + EXTERNAL_URL);
        System.out.println("INTERNAL_URL_AND_PORT " + INTERNAL_URL_AND_PORT);
        System.out.println("port should be " + System.getenv("PORT"));
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(EXTERNAL_URL, INTERNAL_URL_AND_PORT);
        WebhookHandler bot = new WebhookHandler();
        WebhookUtils.clearWebhook(bot);
        telegramBotsApi.registerBot(bot);
    }
}
