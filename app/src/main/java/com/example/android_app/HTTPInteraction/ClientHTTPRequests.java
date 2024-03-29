package com.example.android_app.HTTPInteraction;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

// TODO - На сломанный сервер вызывать окно Oooops...
public class ClientHTTPRequests {

    // TODO - поменять URL
    public static final String hostname = "https://trifonovalex2001.fvds.ru/api";

    /**
     * Функция возвращает true\false если пользователь с этим именем (_Username)
     * существует или нет соответственно
     * @param _Username String (имя пользователя)
     * @return boolean
     * @see <a href="https://github.com/RobertoSenyor/TFG_Documentation/blob/main/API.md#занято-ли-имя">GitHubURL</a>
     */
    public static boolean sendGetRequest_isExistUsername(String _Username) {

        String urlRequest = hostname+"/User/exist_username?username=" + _Username;

        try {
            URL url = new URL(urlRequest);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            String line = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            // TODO - данный вывод можно убрать
            System.out.println("Response code: " + httpURLConnection.getResponseCode());
            System.out.println("Response: " + response.toString());

            // извлечение данных по ключу, здесь ключ "result"
            return new JSONObject(response.toString()).get("result").toString().equals("true") ? true : false;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("sendGetRequest_isExistUsername: Ошибка проверки существования пользователя с заданным именем;");
            return false;
        }
    }

    /**
     * Функция получает токен при успешной регистрации пользователя
     * @param _Username String (имя пользователя)
     * @param _SteamURL String (Steam-аккаунт)
     * @param _EMail String (почта пользователя)
     * @param _Password String (пароль для регистрации)
     * @return String
     * @see <a href="https://github.com/RobertoSenyor/TFG_Documentation/blob/main/API.md#регистрация">GitHubURL</a>
     */
    public static String sendPostRequest_RegistrationUser(String _Username, String _SteamURL, String _EMail, String _Password) {

        String urlRequest = hostname+"/User/register";

        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("username", _Username);
            jsonObject.put("steam_url", _SteamURL);
            jsonObject.put("email", _EMail);
            jsonObject.put("password", _Password);

            String post_data = jsonObject.toString();

            URL url = new URL(urlRequest);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            // для отправки в формате JSON
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            // заставляет прочитать сообщение в формате JSON
            httpURLConnection.setRequestProperty("Accept", "application/json");
            // использование соединения для отправки данных
            httpURLConnection.setDoOutput(true);

            // создание и отправка тела запроса
            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = post_data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            String line = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            // TODO - данный вывод можно убрать
            System.out.println("Response code: " + httpURLConnection.getResponseCode());
            System.out.println("Response : " + response.toString());

            if(httpURLConnection.getResponseCode() >= 500 && httpURLConnection.getResponseCode() <= 599)
            {
                // TODO - Вызов окно Oooops...
            }

            return new JSONObject(response.toString()).get("token").toString();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("sendPostRequest_RegistrationUser: Ошибка регистрации пользователя;");
            return "";
        }
    }

    /**
     * Функция получает токен при успешной авторизации пользователя
     * @param _Username String (имя пользователя)
     * @param _Password String (пароль при регистрации)
     * @return String
     * @see <a href="https://github.com/RobertoSenyor/TFG_Documentation/blob/main/API.md#логин">GitHubURL</a>
     */
    public static String sendPostRequest_LoginUser(String _Username, String _Password) {
        String urlRequest = hostname+"/User/login";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", _Username);
            jsonObject.put("password", _Password);

            String post_data = jsonObject.toString();

            URL url = new URL(urlRequest);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            // для отправки в формате JSON
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            // заставляет прочитать сообщение в формате JSON
            httpURLConnection.setRequestProperty("Accept", "application/json");
            // использование соединения для отправки данных
            httpURLConnection.setDoOutput(true);

            // создание и отправка тела запроса
            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = post_data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            String line = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            // TODO - данный вывод можно убрать
            System.out.println("Response code: " + httpURLConnection.getResponseCode());
            System.out.println("Response : " + response.toString());

            return new JSONObject(response.toString()).get("token").toString();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("sendPostRequest_LoginUser: Ошибка автороизации пользователя;");
            return null;
        }
    }

    /**
     * Функция получает токен при успешном выходе пользователя из аккаунта
     * @param _Token String (токен сессии)
     * @return boolean
     * @see <a href="https://github.com/RobertoSenyor/TFG_Documentation/blob/main/API.md#выход">GitHubURL</a>
     */
    public static boolean sendPostRequest_LogoutUser(String _Token) {
        String urlRequest = hostname+"/User/logout?token=" + _Token;

        try {
            URL url = new URL(urlRequest);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");

            String line = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            // TODO - данный вывод можно убрать
            System.out.println("Response code: " + httpURLConnection.getResponseCode());

            return httpURLConnection.getResponseCode() <= 299 &&  httpURLConnection.getResponseCode() >= 200 ? true : false;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("sendPostRequest_LogoutUser: Ошибка выхода пользователя из аккаунта;");
            return false;
        }
    }

    /**
     * Функция возвращает true\false если ссылка на профиль в Steam
     * занята или нет соответственно
     * @param _SteamURL String (ссылка на профиль в Steam)
     * @return boolean
     * @see <a href="https://github.com/RobertoSenyor/TFG_Documentation/blob/main/API.md#занятали-ссылка-на-профиль">GitHubURL</a>
     */
    public static boolean sendGetRequest_isExistSteamURL(String _SteamURL) {
        String urlRequest = hostname+"/User/exist_steam_url?steam_url=" + _SteamURL;
        try {
            URL url = new URL(urlRequest);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            String line = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            // TODO - данный вывод можно убрать
            System.out.println("Response code: " + httpURLConnection.getResponseCode());
            System.out.println("Response: " + response.toString());

            // извлечение данных по ключу, здесь ключ "result"
            return new JSONObject(response.toString()).get("result").toString().equals("true") ? true : false;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("sendGetRequest_isExistSteamURL: Ошибка проверки существования Steam-аккаунта;");
            return false;
        }
    }

    /**
     * Функция получает ответ от сервера при успешном получении токена и информации типа "о себе"
     * @param _About String (информация типа "о себе")
     * @param _Token String (токен сессии)
     * @return boolean
     * @see <a href="https://github.com/RobertoSenyor/TFG_Documentation/blob/main/API.md#обновеление-информации-о-профиле">GitHubURL</a>
     */
    public static boolean sendPostRequest_UpdateUserInfo(String _About, String _Token)
    {
        String urlRequest = hostname+"/User/update_info?token=" + _Token;

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("about", _About);

            String post_data = jsonObject.toString();

            URL url = new URL(urlRequest);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");

            // для отправки в формате JSON
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            // заставляет прочитать сообщение в формате JSON
            httpURLConnection.setRequestProperty("Accept", "application/json");
            // использование соединения для отправки данных
            httpURLConnection.setDoOutput(true);

            // создание и отправка тела запроса
            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = post_data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            String line = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            // TODO - данный вывод можно убрать
            System.out.println("Response code: " + httpURLConnection.getResponseCode());

            return httpURLConnection.getResponseCode() <= 299 &&  httpURLConnection.getResponseCode() >= 200 ? true : false;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("sendPostRequest_UpdateUserInfo: Ошибка обновления данных пользователя;");
            return false;
        }
    }

    /**
     * Функция получает уникальный идентификатор пользователя и токен сессии
     * и возвращает данные об аккаунте в формате JSONObject
     * @param _UserID Integer (уникальный идентификатор пользователя)
     * @param _Token String (токен сессии)
     * @return JSONObject
     * @see <a href="https://github.com/RobertoSenyor/TFG_Documentation/blob/main/API.md#информация-о-профиле">GitHubURL</a>
     * @see ClientHTTPRequests#sendGetRequest_GetUserInfo(String)
     */
    public static JSONObject sendGetRequest_GetUserInfo(Integer _UserID, String _Token)
    {
        String urlRequest = hostname+"/User/get_info?user_id=" + _UserID.toString() + "&token=" + _Token;

        try {
            URL url = new URL(urlRequest);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            String line = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            // TODO - данный вывод можно убрать
            System.out.println("Response code: " + httpURLConnection.getResponseCode());
            System.out.println("Response: " + response.toString());

            return new JSONObject(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("sendGetRequest_GetUserInfo: Ошибка получения информации о пользователе;");
            return new JSONObject();
        }
    }

    /**
     * Функция получает токен сессии
     * и возвращает данные об аккаунте в формате JSONObject
     * @param _Token String (токен сессии)
     * @return JSONObject
     * @see <a href="https://github.com/RobertoSenyor/TFG_Documentation/blob/main/API.md#информация-о-профиле">GitHubURL</a>
     * @see ClientHTTPRequests#sendGetRequest_GetUserInfo(Integer, String)
     */
    public static JSONObject sendGetRequest_GetUserInfo(String _Token)
    {
        String urlRequest = hostname+"/User/get_info?token=" + _Token;

        try {
            URL url = new URL(urlRequest);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            String line = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            // TODO - данный вывод можно убрать
//            System.out.println("Response code: " + httpURLConnection.getResponseCode());
//            System.out.println("Response: " + response.toString());

            return new JSONObject(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("sendGetRequest_GetUserInfo: Ошибка получения информации о пользователе;");
            return new JSONObject();
        }
    }

    /**
     * Функция получает токен сессии
     * и возвращает данные ленты в формате JSONArray
     * @param _Token String (токен сессии)
     * @return JSONArray
     * @see <a href="https://github.com/RobertoSenyor/TFG_Documentation/blob/main/API.md#запрос-ленты">GitHubURL</a>
     */
    public static JSONArray sendGetRequest_GetInfoList(String _Token)
    {
        String urlRequest = hostname+"/Match/get_list?token=" + _Token;

        try {
            URL url = new URL(urlRequest);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            String line = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            // TODO - данный вывод можно убрать
//            System.out.println("Response code: " + httpURLConnection.getResponseCode());
//            System.out.println("Response: " + response.toString());

            // извлечение данных по ключу, здесь ключ "result"
            return new JSONObject(response.toString()).getJSONArray("result");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("sendGetRequest_GetInfoList: Ошибка получения ленты;");
            return new JSONArray();
        }
    }

    /**
     * Функция получает уникальный идентификатор пользователя, токен сессии и "код отношения"
     * коды отношения: 1 - лайк, -1 - дизлайк
     * @param _AttitudeID int
     * @param _UserID String (информация типа "о себе")
     * @param _Token String (токен сессии)
     * @return JSONArray
     * @see <a href="https://github.com/RobertoSenyor/TFG_Documentation/blob/main/API.md#лайк">GitHubURL лайк</a>
     * @see <a href="https://github.com/RobertoSenyor/TFG_Documentation/blob/main/API.md#дизлайк">GitHubURL дизлайк</a>
     */
    public static boolean sendPostRequest_SetAttitude(int _AttitudeID, Integer _UserID, String _Token)
    {
        String urlRequest = "";
        switch (_AttitudeID) {
            case 1 : urlRequest = hostname+"/Match/like?token=" + _Token;
            case -1 : urlRequest = hostname+"/Match/dislike?token=" + _Token;
        }

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", _UserID);

            String post_data = jsonObject.toString();

            URL url = new URL(urlRequest);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");

            // для отправки в формате JSON
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            // заставляет прочитать сообщение в формате JSON
            httpURLConnection.setRequestProperty("Accept", "application/json");
            // использование соединения для отправки данных
            httpURLConnection.setDoOutput(true);

            // создание и отправка тела запроса
            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = post_data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            String line = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            // TODO - данный вывод можно убрать
            System.out.println("Response code: " + httpURLConnection.getResponseCode());

            return httpURLConnection.getResponseCode() <= 299 &&  httpURLConnection.getResponseCode() >= 200 ? true : false;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("sendPostRequest_SetAttitude: Ошибка выставления лайка/дизлайка пользователю;");
            return false;
        }
    }

    /**
     * Функция получает уникальный идентификатор чата, текст сообщения и токен сессии,
     * а также возвращает true при успешной отправке сообщения и false при ошибке
     * @param _ChatID Integer идентификационный номер чата
     * @param _Text String текст сообщения
     * @param _Token String (токен сессии)
     * @return boolean
     * @see <a href="https://github.com/RobertoSenyor/TFG_Documentation/blob/main/API.md#отправка-сообщения">GitHubURL</a>
     */
    public static boolean sendPostRequest_SendMessage(Integer _ChatID, String _Text, String _Token)
    {
        String urlRequest = hostname+"/Messages/send?token=" + _Token;

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("chat_id", _ChatID);
            jsonObject.put("text", _Text);

            String post_data = jsonObject.toString();

            URL url = new URL(urlRequest);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");

            // для отправки в формате JSON
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            // заставляет прочитать сообщение в формате JSON
            httpURLConnection.setRequestProperty("Accept", "application/json");
            // использование соединения для отправки данных
            httpURLConnection.setDoOutput(true);

            // создание и отправка тела запроса
            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = post_data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            String line = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            // TODO - данный вывод можно убрать
            System.out.println("Response code: " + httpURLConnection.getResponseCode());

            return httpURLConnection.getResponseCode() <= 299 &&  httpURLConnection.getResponseCode() >= 200 ? true : false;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("sendPostRequest_SendMessage: Ошибка при отправке сообщения;");
            return false;
        }
    }

    /**
     * Функция получает идентификационный номер чата, первого и последнего сообщения, требуемое количество сообщений
     * флаг, определяющий, нужны старые или новые сообщения и токен сессии
     * Функция возвращает данные каждого необходимого сообщения (идентификационный номер сообщения, пользователя и текст) в формате JSONArray
     * @param _ChatID Integer идентификационный номер чата
     * @param _MessageID Integer идентификационный номер последнего сообщения
     * @param _Count Integer необходимое количество сообщений
     * @param _IsNext boolean флаг, если true, то запрос более новых сообщений, иначе более старых
     * @param _Token String (токен сессии)
     * @return JSONArray
     * @see <a href="https://github.com/RobertoSenyor/TFG_Documentation/blob/main/API.md#сообщения-в-чате">GitHubURL</a>
     */
    public static JSONArray sendGetRequest_GetMessagesList(Integer _ChatID, Integer _MessageID,
                                                           Integer _Count, boolean _IsNext, String _Token)
    {
        int _isNext = _IsNext ? 1 : 0;
        String urlRequest = hostname+"/Messages/get_list?token=" + _Token + "&chat_id=" + _ChatID + "&last_id="
                + _MessageID  + "&count=" + _Count + "&is_next=" + _isNext;

        try {
            URL url = new URL(urlRequest);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            String line = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            // TODO - данный вывод можно убрать
            System.out.println("Response code: " + httpURLConnection.getResponseCode());
            //System.out.println("Response: " + response.toString());

            // извлечение данных по ключу, здесь ключ "result"
            return new JSONObject(response.toString()).getJSONArray("result");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("sendGetRequest_GetMessagesList: Ошибка получения сообщений;");
            return new JSONArray();
        }
    }

    /**
     * Функция получает токен сессии
     * Функция возвращает данные всех чатов пользователя (идентификационный номер чата и собеседника) в формате JSONArray
     * @param _Token String (токен сессии)
     * @return JSONArray
     * @see <a href="https://github.com/RobertoSenyor/TFG_Documentation/blob/main/API.md#список-чатов">GitHubURL</a>
     */
    public static JSONArray sendGetRequest_GetChatsList(String _Token)
    {
        String urlRequest = hostname+"/Messages/get_chat_list?token=" + _Token;

        try {
            URL url = new URL(urlRequest);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            String line = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            // TODO - данный вывод можно убрать
            System.out.println("Response code: " + httpURLConnection.getResponseCode());
//            System.out.println("Response: " + response.toString());

            // извлечение данных по ключу, здесь ключ "result"
            return new JSONObject(response.toString()).getJSONArray("result");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("sendGetRequest_GetChatsList: Ошибка получения чатов;");
            return new JSONArray();
        }
    }

    /**
     * Функция получает уникальный идентификатор чата, текст для поиска и токен сессии
     * Функция возвращает идентификационные номера всех сообщений, в которых имеются совпадения с искомым текстом
     * @param _ChatID Integer идентификационный номер чата
     * @param _Text String текст сообщения
     * @param _Token String (токен сессии)
     * @return JSONArray
     * @see <a href="https://github.com/RobertoSenyor/TFG_Documentation/blob/main/API.md#поиск-в-чате">GitHubURL</a>
     */
    public static JSONArray sendGetRequest_FindMessage(Integer _ChatID, String _Text, String _Token)
    {
        String urlRequest = hostname+"/Messages/find_message?token=" + _Token + "&chat_id=" + _ChatID + "&text=" + _Text;

        try {
            URL url = new URL(urlRequest);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            String line = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            // TODO - данный вывод можно убрать
            System.out.println("Response code: " + httpURLConnection.getResponseCode());
//            System.out.println("Response: " + response.toString());

            // извлечение данных по ключу, здесь ключ "result"
            return new JSONObject(response.toString()).getJSONArray("result");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("sendGetRequest_FindMessage: Ошибка нахождения сообщения;");
            return new JSONArray();
        }
    }

    /**
     * Функция добавляет пользователя в ЧС,
     * принимает токен сессии и идентификатор пользователя, которого
     * блокируют
     * @param _UserID int (идентификатор блокируемого пользователя)
     * @param _Token String (токен сессии)
     * @return boolean
     * @see <a href="https://github.com/RobertoSenyor/TFG_Documentation/blob/main/API.md#добавить-пользователя-в-чс">GitHubURL</a>
     */
    public static boolean sendPostRequest_AddUserInBlackList(int _UserID, String _Token)
    {
        String urlRequest = hostname+"/Messages/black_list?token=" + _Token;

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", _UserID);

            String post_data = jsonObject.toString();

            URL url = new URL(urlRequest);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");

            // для отправки в формате JSON
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            // заставляет прочитать сообщение в формате JSON
            httpURLConnection.setRequestProperty("Accept", "application/json");
            // использование соединения для отправки данных
            httpURLConnection.setDoOutput(true);

            // создание и отправка тела запроса
            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = post_data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            String line = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            // TODO - данный вывод можно убрать
            System.out.println("Response code: " + httpURLConnection.getResponseCode());

            return httpURLConnection.getResponseCode() <= 299 &&  httpURLConnection.getResponseCode() >= 200 ? true : false;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("sendPostRequest_AddUserInBlackList: Ошибка добавления пользователя в \"чёрный\" список;");
            return false;
        }
    }

    /**
     * Функция удаляет чат,
     * принимает токен сессии и идентификатор удаляемого чата
     * @param _ChatID int (идентификатор удаляемого чата)
     * @param _Token String (токен сессии)
     * @return boolean
     * @see <a href="https://github.com/RobertoSenyor/TFG_Documentation/blob/main/API.md#удаляет-чат">GitHubURL</a>
     */
    public static boolean sendPostRequest_DropChat(int _ChatID, String _Token)
    {
        String urlRequest = hostname+"/Messages/delete_chat?token=" + _Token;

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("chat_id", _ChatID);

            String post_data = jsonObject.toString();

            URL url = new URL(urlRequest);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");

            // для отправки в формате JSON
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            // заставляет прочитать сообщение в формате JSON
            httpURLConnection.setRequestProperty("Accept", "application/json");
            // использование соединения для отправки данных
            httpURLConnection.setDoOutput(true);

            // создание и отправка тела запроса
            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = post_data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            String line = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            // TODO - данный вывод можно убрать
            System.out.println("Response code: " + httpURLConnection.getResponseCode());

            return httpURLConnection.getResponseCode() <= 299 &&  httpURLConnection.getResponseCode() >= 200 ? true : false;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("sendPostRequest_DropChat: Ошибка удаления чата;");
            return false;
        }
    }
}