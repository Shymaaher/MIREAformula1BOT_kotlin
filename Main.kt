import org.json.JSONArray
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.io.File

class SimpleBot : TelegramLongPollingBot() {
    override fun getBotUsername(): String {
        return "Formula1CalendarBot"
    }

    override fun getBotToken(): String {
        return "7584499973:AAFL0Vl2qtHlTLr0EUe6dk98WJ2JbGIzi54"
    }

    override fun onUpdateReceived(update: Update?) {
        if (update != null && update.hasMessage() && update.message.hasText()) {
            val messageText = update.message.text
            val chatId = update.message.chatId

            when (messageText) {
                "Календарь" -> {
                    sendCalendarImage(chatId)
                }

                "Турнирная таблица Кубка конструкторов" -> {
                    val response = getConstructorsStandings()
                    sendResponse(chatId, response, showMainButtons())
                }

                "Информация о машинах 2024" -> {
                    sendResponse(chatId, "Выберите команду, чтобы узнать подробную информацию о болиде:", showCarsButtons())
                }

                else -> {
                    // Обработка остальных команд
                    val (response, imageUrl) = processCommand(messageText)
                    sendResponse(chatId, response, showMainButtons())
                    if (imageUrl.isNotEmpty()) {
                        sendCarPhoto(chatId, imageUrl)
                    }
                }
            }
        }
    }

    private fun processCommand(messageText: String): Pair<String, String> {
        return when (messageText) {
            "Календарь" -> Pair(getF1Calendar(), "")
            "Турнирная таблица Кубка конструкторов" -> Pair(getConstructorsStandings(), "")
            "Oracle Red Bull Racing" -> getCarInfo("Oracle Red Bull Racing - RB20")
            "Mercedes-AMG PETRONAS Formula One Team" -> getCarInfo("Mercedes-AMG PETRONAS - W15")
            "Scuderia Ferrari" -> getCarInfo("Scuderia Ferrari - SF-24")
            "McLaren Formula 1 Team" -> getCarInfo("McLaren F1 Team - MCL3")
            "Aston Martin Aramco Formula One Team" -> getCarInfo("Aston Martin - AMR24")
            "BWT Alpine F1 Team" -> getCarInfo("BWT Alpine - A524")
            "Williams Racing" -> getCarInfo("Williams - FW46")
            "Visa Cash App RB Formula One Team" -> getCarInfo("Visa Cash App RB Formula One Team - VCARB 01")
            "Stake F1 Team Kick Sauber" -> getCarInfo("Stake F1 Team Kick Sauber - Kick Sauber")
            "MoneyGram Haas F1 Team" -> getCarInfo("MoneyGram Haas F1 Team -  VF-24")
            "Назад в главное меню" -> Pair("Главное меню", "")
            else -> Pair("Неизвестная команда", "")
        }
    }

    private fun sendCarPhoto(chatId: Long, imageUrl: String) {
        val sendPhoto = SendPhoto(
            chatId.toString(),
            InputFile(imageUrl)
        ).apply {
            caption = "Фотография автомобиля команды"
        }

        try {
            execute(sendPhoto)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    private fun showCarsButtons(): ReplyKeyboardMarkup {
        val keyboardMarkup = ReplyKeyboardMarkup()
        keyboardMarkup.resizeKeyboard = true

        val buttons = ArrayList<KeyboardRow>()

        val row1 = KeyboardRow()
        row1.add("Oracle Red Bull Racing")
        row1.add("Mercedes-AMG PETRONAS Formula One Team")

        val row2 = KeyboardRow()
        row2.add("Scuderia Ferrari")
        row2.add("McLaren Formula 1 Team")

        val row3 = KeyboardRow()
        row3.add("Aston Martin Aramco Formula One Team")
        row3.add("BWT Alpine F1 Team")

        val row4 = KeyboardRow()
        row4.add("Williams Racing")
        row4.add("Visa Cash App RB Formula One Team")

        val row5 = KeyboardRow()
        row5.add("Stake F1 Team Kick Sauber")
        row5.add("MoneyGram Haas F1 Team")

        val row6 = KeyboardRow()
        row6.add("Назад в главное меню")

        buttons.add(row1)
        buttons.add(row2)
        buttons.add(row3)
        buttons.add(row4)
        buttons.add(row5)
        buttons.add(row6)

        keyboardMarkup.keyboard = buttons
        return keyboardMarkup
    }

    private fun sendResponse(chatId: Long, response: String, keyboard: ReplyKeyboardMarkup) {
        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = response
        message.replyMarkup = keyboard

        try {
            execute(message)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    private fun getCarInfo(carName: String): Pair<String, String> {
        return when (carName) {
            "Oracle Red Bull Racing - RB20" -> Pair("""
            Информация о Red Bull RB20:
            Шасси: RB20
            Мотор: Honda RBPT
        """.trimIndent(), "https://cdn.f1ne.ws/userfiles/RB20-2024.jpg")

            "Mercedes-AMG PETRONAS - W15" -> Pair("""
            Информация о Mercedes W15:
            Шасси: Mercedes AMG W15
            Мотор: Mercedes
        """.trimIndent(), "https://cdn.f1ne.ws/userfiles/W15-2024.jpg")
            "Scuderia Ferrari - SF-24" -> Pair("""
            Информация о Ferrari - SF-24:
            Шасси: Ferrari SF-24
            Мотор: Ferrari
        """.trimIndent(), "https://cdn.f1ne.ws/userfiles/SF-24.jpg")

            "McLaren F1 Team - MCL39" -> Pair("""
            Информация о McLaren MCL39 :
            Шасси: McLaren MCL38
            Мотор: Mercedes.
        """.trimIndent(), "https://cdn.f1ne.ws/userfiles/MCL38-2024.jpg")
            "Aston Martin - AMR24" -> Pair("""
            Информация о Aston Martin AMR24:
            Шасси: Aston Martin AMR24
            Мотор: Mercedes
        """.trimIndent(), "https://cdn.f1ne.ws/userfiles/AMR24-1.jpg")
            "BWT Alpine - A524" -> Pair("""
            Информация о Alpine A524:
            Шасси: Alpine A524
            Мотор: Renault
        """.trimIndent(), "https://cdn.f1ne.ws/userfiles/A524.jpg")
            "Williams Racing - FW46" -> Pair("""
            Информация о Williams Racing - FW46:
            Команда: MoneyGram Haas F1 Team
            Шасси: VF-24
            Мотор: Ferrari
        """.trimIndent(), "https://cdn.f1ne.ws/userfiles/FW46.jpg")
            "Visa Cash App RB Formula One Team - VCARB 01" -> Pair("""
            Информация о Visa Formula One Team - VCARB 01:
            Шасси: VCARB 01
            Мотор: Honda RBPT
        """.trimIndent(), "https://cdn.f1ne.ws/userfiles/VCARB-01.jpg")
            "Stake F1 Team Kick Sauber - Kick Sauber" -> Pair("""
            Информация о Stake F1 Team Kick Sauber - Kick Sauber:
            Шасси: Kick Sauber
            Мотор: Ferrari
        """.trimIndent(), "https://cdn.f1ne.ws/userfiles/sauber-2024.jpg")
            "MoneyGram Haas F1 Team -  VF-24" -> Pair("""
             Информация о MoneyGram Haas F1 Team -  VF-24:
             Шасси: VF-24
             Мотор: Ferrari
        """.trimIndent(), "https://cdn.f1ne.ws/userfiles/VF-24.jpg")




            else -> Pair("Информация о $carName не найдена.", "")
        }
    }


    private fun showMainButtons(): ReplyKeyboardMarkup {
        val keyboardMarkup = ReplyKeyboardMarkup()
        keyboardMarkup.resizeKeyboard = true

        val buttons = ArrayList<KeyboardRow>()
        val row = KeyboardRow()


        val row1 = KeyboardRow()
        row1.add("Календарь")
        row1.add("Турнирная таблица")
        buttons.add(row1)


        val row2 = KeyboardRow()
        row2.add("Информация о машинах 2024")
        buttons.add(row2)

        keyboardMarkup.keyboard = buttons
        return keyboardMarkup
    }

    private fun getF1Calendar(): String {
        return "Календарь Формулы 1 отправлен."
    }

    private fun sendCalendarImage(chatId: Long) {
        val imageUrl = "https://f-1world.ru/posters/f1-2024-calendar.webp"
        val sendPhoto = SendPhoto(
            chatId.toString(),
            InputFile(imageUrl)
        ).apply {
            caption = "Календарь Формулы 1 на 2024 год"
        }

        try {
            execute(sendPhoto)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    private fun getConstructorsStandings(): String {
        return try {
            val jsonString = File("teams.json").readText(Charsets.UTF_8)
            val teamsArray = JSONArray(jsonString)

            val standings = StringBuilder(
                "Турнирная таблица Кубка конструкторов:\n\n\n"
            )
            standings.append("║════════════════════════════════════║\n\n")

            for (i in 0 until teamsArray.length()) {
                val team = teamsArray.getJSONObject(i)
                standings.append(
                    "Команда:  ${team.getString("Команда")}, " +
                            "Поб: ${team.getString("Поб")}, " +
                            "ПЛ: ${team.getString("ПЛ")}, " +
                            "ЛК: ${team.getString("ЛК")}, " +
                            "Очки: ${team.getString("Очки")}\n\n"
                )
            }
            standings.append("║════════════════════════════════════║\n\n")

            standings.append("Примечание: Поб - количество побед, ПЛ - количество поул-позиций, ЛК - количество лучших кругов.\n")

            standings.toString()
        } catch (e: Exception) {
            "Не удалось загрузить турнирную таблицу"
        }
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