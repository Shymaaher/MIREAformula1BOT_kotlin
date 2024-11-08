import org.json.JSONArray
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.io.File
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

class SimpleBot(private val fitnessDatabaseManager: FitnessDatabaseManager) : TelegramLongPollingBot() {
    override fun getBotUsername(): String {
        return "Formula1Calendar"
    }

    override fun getBotToken(): String {
        return "7584499973:AAFL0Vl2qtHlTLr0EUe6dk98WJ2JbGIzi548"
    }

    override fun onUpdateReceived(update: Update?) {
        if (update != null && update.hasMessage() && update.message.hasText()) {
            val messageText = update.message.text
            val chatId = update.message.chatId
            
            val response = BotCommands.processCommand(chatId, messageText, fitnessDatabaseManager)
            sendResponse(chatId, response, showMainButtons())
        }
    }

    private fun sendResponse(chatId: Long, text: String, replyMarkup: ReplyKeyboardMarkup? = null) {
        val responseMessage = SendMessage()
        responseMessage.chatId = chatId.toString()
        responseMessage.text = text
        replyMarkup?.let { responseMessage.replyMarkup = it }

        try {
            execute(responseMessage)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    private fun showMainButtons(): ReplyKeyboardMarkup {
        val keyboardMarkup = ReplyKeyboardMarkup()
        keyboardMarkup.resizeKeyboard = true

        val buttons = ArrayList<KeyboardRow>()


        val row1 = KeyboardRow()
        row1.add("Календарь 2024")
        row1.add("Информация о трассах")

        val row2 = KeyboardRow()
        row2.add("Викторина")
        row2.add("Трассы")


        buttons.add(row1)
        buttons.add(row2)

        keyboardMarkup.keyboard = buttons
        return keyboardMarkup
    }
}

fun main() {
    val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
    try {
        botsApi.registerBot(SimpleBot())
        println("Бот запущен!")
    } catch (e: TelegramApiException) {
        e.printStackTrace()
    }
}