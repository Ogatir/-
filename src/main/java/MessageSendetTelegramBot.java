
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.List;

public class MessageSendetTelegramBot
{
    private static String BOT_TOKEN= "474959037:AAFv0iGdEbJB1SpYJ8VVVNTdLlrZHT8URt8";
    private static String CHAT_ID = "113408725";


    TelegramBot bot = new TelegramBot(BOT_TOKEN);

    GetUpdates getUpdates = new GetUpdates().limit(100).offset(0).timeout(0);

    GetUpdatesResponse updResp = bot.execute(getUpdates);
    List<Update> updates = updResp.updates();

    Message message=updates.get(4).message();



}

