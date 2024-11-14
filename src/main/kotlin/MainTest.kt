import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BotTests {

    // Сначала создаем тестовый экземпляр бота
    private val bot = SimpleBot()
    private val chatId = 123456789L

    // Тест 1: Проверка отправки простого сообщения
    @Test
    fun testSendMessage() {
        // Симулируем команду "Календарь"
        val response = bot.processCommand("Календарь")

        // Проверяем, что отправлено правильное сообщение
        assertEquals("Календарь Формулы 1 отправлен.", response.first)
        assertEquals("", response.second)  // Убедимся, что нет изображения
    }

    // Тест 2: Проверка старта викторины
    @Test
    fun testStartQuiz() {
        // Симулируем запуск викторины
        bot.startQuiz(chatId)

        // Проверяем, что викторина началась с первого вопроса
        assertEquals(0, bot.currentQuestionIndex)
        assertTrue { bot.userAnswers.containsKey(chatId) }
    }

    // Тест 3: Проверка завершения викторины
    @Test
    fun testEndQuiz() {
        // Симулируем начало викторины
        bot.startQuiz(chatId)

        // Завершаем викторину
        bot.endQuiz(chatId)

        // Проверяем, что викторина завершена
        assertEquals(-1, bot.currentQuestionIndex)
        assertTrue { !bot.userAnswers.containsKey(chatId) }  // Пользовательские ответы удалены
    }

    // Тест 4: Проверка обработки неверной команды
    @Test
    fun testUnknownCommand() {
        // Симулируем отправку неизвестной команды
        val response = bot.processCommand("Неизвестная команда")

        // Проверяем, что отправлено сообщение о незнакомой команде
        assertEquals("Неизвестная команда", response.first)
        assertEquals("", response.second)  // Нет изображения
    }

    // Тест 5: Проверка отправки календаря
    @Test
    fun testSendCalendar() {
        // Симулируем команду на отправку календаря
        val response = bot.processCommand("Календарь")

        // Проверяем, что календарь отправлен и URL изображения присутствует
        assertEquals("Календарь Формулы 1 отправлен.", response.first)
        assertEquals("https://f-1world.ru/posters/f1-2024-calendar.webp", response.second)
    }
}
