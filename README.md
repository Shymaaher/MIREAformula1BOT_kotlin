# Автоматизированная система "Календарь Формулы 1"


## Описание
Автоматизированная система "Календарь Формулы 1" представляет собой Telegram-бота, который предоставляет пользователям актуальную информацию о предстоящих гонках Формулы 1. Бот информирует о датах, местах проведения гонок, рейтингах гонщиков и команд, а также предлагает тесты для проверки знаний пользователей о Формуле 1.


## Цели и назначение
Основная цель создания системы — обеспечить оперативный доступ к информации о событиях Формулы 1 и повысить интерес к этому виду спорта.

## Объекты автоматизации
- Даты и места проведения гонок.
- Рейтинг гонщиков и команд.
- Информация о командах и гонщиках.
- Тесты на знания.



## Требования к системе
- Бот должен быть доступен в Telegram.
- Пользователь должен иметь возможность запрашивать информацию о гонках, результатах и статистике.
- Бот должен обрабатывать текстовые команды и отвечать на них.
- Система должна быть устойчива к ошибкам и обеспечивать корректную работу при высокой нагрузке.



## Этапы разработки
1. **Сбор требований и проектирование**:
   - Анализ потребностей пользователей.
   - Определение функциональности бота.

2. **Разработка и интеграция**:
   - Интеграция с API для получения данных о Формуле 1.
   - Реализация функционала для обработки запросов пользователей.

3. **Тестирование**:
   - Проведение тестирования функционала бота.
   - Исправление ошибок и оптимизация работы.

4. **Документация**:
   - Подготовка руководства пользователя.
   - Создание технической документации для разработчиков.

5. **Внедрение и запуск**:
   - Размещение бота на сервере.
   - Запуск и мониторинг работы бота.

## Контроль и приемка
- Проведение тестирования на соответствие требованиям.
- Проверка функциональности с использованием тестовых сценариев.
- Приемка системы по итогам тестирования.


## Документация
- Официальная документация по API Формулы 1.
- Документация по Telegram Bot API.
- Руководство пользователя и техническая документация.

## Контакты
Если у вас есть вопросы или предложения, пожалуйста, свяжитесь с командой разработки по адресу: bromaksedition1@yandex.ru




# Formula 1 Bot

Этот проект представляет собой Telegram-бота, который предоставляет информацию о Формуле 1, включая календарь гонок, турнирные таблицы и викторины.

## Основной функционал
1. **Календарь** — Показывает календарь предстоящих событий Формулы 1.
2. **Турнирная таблица Кубка конструкторов** — Отображает таблицу текущих результатов среди команд.
3. **Турнирная таблица пилотов** — Показывает текущие результаты пилотов.
4. **Информация о машинах 2024 года** — Предоставляет информацию о технических характеристиках автомобилей.
5. **Викторина** — Позволяет пользователям участвовать в викторинах, связанных с Формулой 1.

## Структура классов

### `QuizQuestion`
Это класс, который используется для представления вопросов викторины. Каждый вопрос содержит:
- `question` — текст вопроса,
- `answers` — список возможных ответов,
- `correctAnswerIndex` — индекс правильного ответа в списке.

### `SimpleBot`
Этот класс реализует логику работы бота. Он:
- Обрабатывает команды от пользователей,
- Отправляет сообщения и изображения,
- Управляет викториной,
- Предоставляет актуальную информацию о Формуле 1.

### Основные методы:
- `onUpdateReceived(update: Update?)` — Обрабатывает входящие сообщения.
- `sendResponse(chatId: Long, text: String, keyboard: ReplyKeyboardMarkup)` — Отправляет сообщение пользователю.
- `startQuiz(chatId: Long)` — Запускает викторину.
- `sendQuestion(chatId: Long)` — Отправляет вопрос викторины пользователю.
- `handleAnswer(chatId: Long, answerIndex: Int)` — Обрабатывает выбранный пользователем ответ.









### `Описание ключевых модулей, классов и функций с помощью KDoc`
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

/**
 * Данный класс представляет вопрос викторины.
 *
 * @property question Вопрос викторины.
 * @property answers Список возможных ответов на вопрос.
 * @property correctAnswerIndex Индекс правильного ответа в списке ответов.
 */
data class QuizQuestion(
    val question: String, 
    val answers: List<String>, 
    val correctAnswerIndex: Int
)

/**
 * Бот для Telegram, поддерживающий несколько команд, включая отправку информации о командах Формулы 1,
 * проведение викторины, отправку результатов и другие действия.
 *
 * @constructor Создает бот для ответов на различные команды и управление викториной.
 */
class SimpleBot : TelegramLongPollingBot() {

    /**
     * Возвращает имя пользователя бота.
     *
     * @return Имя пользователя бота.
     */
    override fun getBotUsername(): String = "Formula1CalendarBot"

    /**
     * Возвращает токен бота.
     *
     * @return Токен бота.
     */
    override fun getBotToken(): String = "7584499973:AAFL0Vl2qtHlTLr0EUe6dk98WJ2JbGIzi54"

    // Список вопросов викторины
    private val quizQuestions = listOf(
        QuizQuestion("Кто выиграл чемпионат мира Формулы 1 в 2020 году?", listOf("Льюис Хэмилтон", "Валттери Боттас", "Макс Ферстаппен", "Себастьян Феттель"), 0),
        QuizQuestion("Какой автомобиль был первым в истории Формулы 1?", listOf("Mercedes W196", "Ferrari 125 F1", "Lotus 49", "McLaren MP4/4"), 1),
        QuizQuestion("Какой гонщик имеет наибольшее количество побед в Формуле 1?", listOf("Льюис Хэмилтон", "Михаэль Шумахер", "Ален Прост", "Себастьян Феттель"), 0)
        // другие вопросы опущены для краткости
    )
    
    private var currentQuestionIndex = -1
    private val userAnswers = mutableMapOf<Long, MutableList<Int>>()

    /**
     * Обрабатывает входящие обновления (сообщения и запросы с кнопок).
     *
     * @param update Объект обновления, содержащий полученное сообщение или запрос с кнопки.
     */
    override fun onUpdateReceived(update: Update?) {
        if (update != null && update.hasMessage() && update.message.hasText()) {
            val messageText = update.message.text
            val chatId = update.message.chatId

            when (messageText) {
                "Календарь" -> sendCalendarImage(chatId)
                "Турнирная таблица Кубка конструкторов" -> sendResponse(chatId, getConstructorsStandings(), showMainButtons())
                "Информация о машинах 2024" -> sendResponse(chatId, "Выберите команду:", showCarsButtons())
                "Турнирная таблица пилотов" -> sendPilotsStandings(chatId)
                "Викторина" -> startQuiz(chatId)
                "Завершить викторину" -> {
                    if (currentQuestionIndex != -1) {
                        endQuiz(chatId)
                    } else {
                        sendResponse(chatId, "Викторина еще не началась.", showMainButtons())
                    }
                }
                else -> {
                    val (response, imageUrl) = processCommand(messageText)
                    sendResponse(chatId, response, showMainButtons())
                    if (imageUrl.isNotBlank()) sendCarPhoto(chatId, imageUrl)
                }
            }
        } else if (update != null && update.hasCallbackQuery()) {
            val callbackData = update.callbackQuery.data
            val chatId = update.callbackQuery.message.chatId
            handleAnswer(chatId, callbackData.toInt())
        }
    }

    /**
     * Отправляет турнирную таблицу пилотов.
     *
     * @param chatId Идентификатор чата, куда будет отправлено сообщение.
     */
    private fun sendPilotsStandings(chatId: Long) {
        val response = getPilotsStandings()
        sendResponse(chatId, response, showMainButtons())
    }

    /**
     * Читает и форматирует турнирную таблицу пилотов из JSON-файла.
     *
     * @return Строка, содержащая турнирную таблицу пилотов.
     */
    private fun getPilotsStandings(): String {
        return try {
            val jsonString = File("table.json").readText(Charsets.UTF_8)
            val teamsArray = JSONArray(jsonString)

            val standings = StringBuilder("Турнирная таблица пилотов:\n\n")
            standings.append("║════════════════════════════════════║\n\n")

            for (i in 0 until teamsArray.length()) {
                val team = teamsArray.getJSONObject(i)
                standings.append(
                    "Команда:  ${team.getString("Имя")}, " +
                            "Поб: ${team.getString("Название команды")}, " +
                            "ПЛ: ${team.getString("ПОБ")}, " +
                            "ЛК: ${team.getString("ПЛ")}, " +
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

    /**
     * Запускает викторину для пользователя, инициализируя вопросы и ответы.
     *
     * @param chatId Идентификатор чата, где будет проходить викторина.
     */
    private fun startQuiz(chatId: Long) {
        currentQuestionIndex = 0
        userAnswers[chatId] = mutableListOf()
        sendQuestion(chatId)
    }

    /**
     * Завершает викторину для пользователя и отображает результаты.
     *
     * @param chatId Идентификатор чата, где проходит викторина.
     */
    private fun endQuiz(chatId: Long) {
        if (currentQuestionIndex != -1) {
            sendResponse(chatId, "Викторина завершена!", showMainButtons())
            currentQuestionIndex = -1
            userAnswers.remove(chatId)
        } else {
            sendResponse(chatId, "Викторина еще не началась.", showMainButtons())
        }
    }

    /**
     * Отправляет текущий вопрос викторины пользователю.
     *
     * @param chatId Идентификатор чата, куда будет отправлен вопрос.
     */
    private fun sendQuestion(chatId: Long) {
        if (currentQuestionIndex < quizQuestions.size) {
            val question = quizQuestions[currentQuestionIndex]
            val markup = InlineKeyboardMarkup()
            val buttons = question.answers.mapIndexed { index, answer -> 
                InlineKeyboardButton(answer).apply { callbackData = index.toString() } 
            }
            markup.keyboard = listOf(buttons)

            val message = SendMessage().apply {
                this.chatId = chatId.toString()
                text = question.question
                replyMarkup = markup
            }
            try {
                execute(message)
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }
        } else {
            sendResults(chatId)
        }
    }

    /**
     * Обрабатывает ответ пользователя и переходит к следующему вопросу.
     *
     * @param chatId Идентификатор чата, где проходит викторина.
     * @param answerIndex Индекс выбранного ответа пользователем.
     */
    private fun handleAnswer(chatId: Long, answerIndex: Int) {
        userAnswers[chatId]?.add(answerIndex)
        currentQuestionIndex++
        sendQuestion(chatId)
    }

    /**
     * Отправляет результаты викторины пользователю.
     *
     * @param chatId Идентификатор чата, где будут показаны результаты.
     */
    private fun sendResults(chatId: Long) {
        val answers = userAnswers[chatId] ?: return
        var score = 0

        quizQuestions.forEachIndexed { index, question ->
            if (answers.getOrNull(index) == question.correctAnswerIndex) {
                score++
            }
        }
        sendResponse(chatId, "Ваш результат: $score/${quizQuestions.size}", showMainButtons())
    }

    /**
     * Формирует строку с результатами для отображения.
     *
     * @param chatId Идентификатор чата, где будет отправлена строка с результатами.
     * @param text Текст для отправки.
     * @param keyboard Опциональная клавиатура для прикрепления к сообщению.
     */
    private fun sendResponse(chatId: Long, text: String, keyboard: ReplyKeyboardMarkup) {
        val message = SendMessage().apply {
            this.chatId = chatId.toString()
            this.text = text
            this.replyMarkup = keyboard
        }
        try {
            execute(message)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    /**
     * Создает клавиатуру с основными кнопками для навигации.
     *
     * @return Клавиатура с основными кнопками.
     */
    private fun showMainButtons(): ReplyKeyboardMarkup {
        val keyboard = ReplyKeyboardMarkup().apply {
            val row1 = KeyboardRow().apply {
                add("Календарь")
                add("Турнирная таблица Кубка конструкторов")
            }
            val row2 = KeyboardRow().apply {
                add("Информация о машинах 2024")
                add("Турнирная таблица пилотов")
            }
            val row3 = KeyboardRow().apply {
                add("Викторина")
            }
            keyboard.rows = listOf(row1, row2, row3)
        }
        return keyboard
    }
}
