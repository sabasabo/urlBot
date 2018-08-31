import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class Bot {

    private class webhookHandler extends TelegramWebhookBot {

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
            return null;
        }

        public String getBotToken() {
            return null;
        }

        public String getBotPath() {
            return null;
        }
    }

    public static void main(String[] args) {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
    }
}
