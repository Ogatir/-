import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


/**
 * Авторизация пользователя на сервисе Google Drive
 */
public class Authorizer {

    private static final String APPLICATION_NAME = "KursRabAuto";

    /** Директория для хранения мантдата пользователя */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/drive-java-quickstart");

    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Для обработки ответа в формате json */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(DriveScopes.DRIVE);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Создание объекта, представляющего мандат
     * @return объект мандата
     * @throws IOException
     */
    private Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                ExecutionEngine.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        //Проверяем файл мандата
        if (!new java.io.File(DATA_STORE_DIR + "//StoredCredential").exists())
            DBG.Log("Access token doesn't exist");
        else {
            //Если пользователь хочет сменить аккаунт, меняем файл мандата.
            DBG.Log("Хотите авторизоваться как новый пользователь (y/n)?");
            Scanner consoleInput = new Scanner(System.in);
            if (consoleInput.next().compareToIgnoreCase("y") == 0)
                new java.io.File(DATA_STORE_DIR + "//StoredCredential").delete();
            DBG.Log("Access token exists");
        }
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        try {
            Credential credential = new AuthorizationCodeInstalledApp(
                    flow, new LocalServerReceiver()).authorize("user");

            DBG.Log("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
            return credential;
        } catch (IOException e){
            DBG.Log("[ERROR: Authorizer.Authorize()]: %s",e.getMessage());
            return  null;
        }
    }

    /**
     * Создание объекта сервиса для взаимодействия с Google Drive
     * @return авторизованный Google Drive клиент сервиса
     * @throws IOException
     */
    public Drive getDriveService() throws IOException {
        Credential credential = authorize();
        if (credential!=null) {
            return new Drive.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } else return null;
    }
}
