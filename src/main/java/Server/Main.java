package Server;

public class Main {
    public static void main(String[] args) {
        new ServerEndpoint().startServer();
    }
}

/*
1) main -> startServer()
2) startServer() -> создается серверный сокет, запускается прослушка методом accept
3) accept() -> clientSocket -> clientManager.onClientConnected(clientSocket)
4) new Client(clientSocket, clientManager) -> .login()
5) login() -> clientManager.onClientSignedIn()
6) client -> clients, client.startMessaging()
 */

/*
1) Протестировать на старом клиенте
2) Отправка личных сообщений через json
    При отправке клиентом сообщения, он заполняет объект класса Message данными
    Далее этот объект переводится в json и отправляется на сервер
    Сервер получает сообщение в onMessageReceived и парсит в объект класса Message,
    а затем уже отправляет в SendingThread

    Пример отправки: @receiver_username@message_text

3) Проверка на существующий логин

Паттерны:
Одиночка (+хабр потокобезопасность)
Наблюдатель
Стратегия!!!!
Итератор
 */
