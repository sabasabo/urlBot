import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Optional;

public class Bot {

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
            return System.getenv("HEROKU_URL") + System.getenv("TOKEN");
        }
    }

    public static void main(String[] args) throws TelegramApiRequestException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        telegramBotsApi.registerBot(new webhookHandler());
    }
}
