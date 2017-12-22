import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.pengrad.telegrambot.model.Message;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;


/**
 * Движок исполнения. В зависимости от входного параметра в программу
 * вызывает одну из функций.
 * */
public class ExecutionEngine {

    /**
     * Скачивание файла
     * @param documentName
     * @throws IOException
     */
    public void Download(String documentName) throws IOException {
        Authorizer googleAuth = new Authorizer();
        Drive service = googleAuth.getDriveService();
        if (service!=null) {
            try {
                HashMap<String, String> filesAndId = new HashMap<String, String>();
                FileList result = service.files().list()
                        .setPageSize(20)
                        .setFields("nextPageToken, files(id, name, fileExtension, kind)")
                        .execute();

                List<File> files = result.getFiles();
                for (File file : files)
                    filesAndId.put(file.getName(), file.getId());

                FileManager fileDownloader = new FileManager(service);
                int statusCode = fileDownloader.DownloadFile(filesAndId.get(documentName), documentName);
                if (statusCode == 0)
                    DBG.Log("File \"%s\" has been downloaded", documentName);
                else DBG.Log("ERROR: File \"%s\" cannot be downloaded", documentName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Создание файла на Google Disk при создании курсовой работы.
     * @param newDocumentName
     * @param docUserEmails
     * @throws IOException
     */
    public void Create(String newDocumentName, String docUserEmails[]) throws IOException {
        Authorizer googleAuth = new Authorizer();
        Drive service = googleAuth.getDriveService();
        if (service!=null) {
            FileManager uploader = new FileManager(service);
            String fileId = uploader.CreateFile(newDocumentName);

            JsonBatchCallback<Permission> callback = new JsonBatchCallback<Permission>() {
                @Override
                public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) throws IOException {
                    System.err.println(e.getMessage());
                }
                @Override
                public void onSuccess(Permission permission, HttpHeaders responseHeaders) throws IOException {
                    DBG.Log("Permission ID: " + permission.getId());
                }
            };
            /*Здесь пользователю добавляется разрешение на работу с файлом.*/
            for (int i = 0; i < docUserEmails.length; i++) {
                BatchRequest batch = service.batch();
                Permission userPermission = new Permission()
                        .setType("user")
                        .setRole("writer")
                        .setEmailAddress(docUserEmails[0]);
                service.permissions().create(fileId, userPermission)
                        .setFields("id")
                        .queue(batch, callback);
                batch.execute();
                batch = null;
            }
        }

    }

    /**
     * Студент добавив что-то в курсовую, делает обновление файла на
     * Google Drive преподавателя
     * @param documentName
     * @throws IOException
     */
    public void Update(String documentName) throws IOException {
        Authorizer googleAuth = new Authorizer();
        Drive service = googleAuth.getDriveService();
        if (service!=null) {
            MessageSendetTelegramBot messenger = new MessageSendetTelegramBot();
            FileList result = service.files().list()
                    .setPageSize(20)
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
            List<File> files = result.getFiles();

            for (File file : files) {
                if (file.getName().compareTo(documentName) == 0) {
                    FileManager updater = new FileManager(service);
                    updater.UpdateFile(documentName, file.getId());
                    messenger.SendNotice("Обновлена курсовая работа \"" + documentName + "\"", documentName);
                }
            }
        }
    }

    /**
     * Просмотр документов в распоряжении у пользователя.
     * Так можно найти файл со своей курсовой
     * @throws IOException
     */
    public void ViewDocuments () throws IOException{
        Authorizer googleAuth = new Authorizer();
        Drive service = googleAuth.getDriveService();
        if (service!=null) {
            FileList result = service.files().list()
                    .setPageSize(50)
                    .setFields("nextPageToken, files(id, name, fileExtension, kind)")
                    .execute();
            List<File> files = result.getFiles();
            if (files == null || files.size() == 0) {
                DBG.Log("No files found.");
            } else {
                DBG.Log("Files:");
                int i = 0;
                for (File file : files) {
                    DBG.Log("%d: %s (%s) is a %s", (i + 1), file.getName(), file.getId(), file.getFileExtension());
                    i++;
                }
            }
        }
    }


}
