
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
    private static String CHAT_ID2= "117508330";


    TelegramBot bot = new TelegramBot(BOT_TOKEN);


    private String GetUpdate(String documentName) {
        GetUpdates getUpdates = new GetUpdates().limit(100).offset(0).timeout(0);
        GetUpdatesResponse updResp = bot.execute(getUpdates);
        List<Update> updates = updResp.updates();
        for (int i = 0; i < updates.size(); i++) {
            if (updates.get(i).message().text().compareTo(documentName) == 0) {
                String chatID = updates.get(i).message().chat().id().toString();
                DBG.Log("User %s controls document %s and has chatID %s"
                        , updates.get(i).message().chat().firstName(), documentName, chatID);
                return chatID;
            }
        } return null;
    }


    public void SendNotice(String messageToSend, String documentName){
        String chatID = GetUpdate(documentName);
        if (chatID!=null)
        {
            SendMessage request = new SendMessage(chatID, messageToSend)
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true);
            SendResponse response = bot.execute(request);
            response.message();
        } else DBG.Log("[Warning]: couldn't retrieve chatID");

    }

}

