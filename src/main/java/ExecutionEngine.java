import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ExecutionEngine {

    public void Download(String documentName) throws IOException {
        Authorizer googleAuth = new Authorizer();
        Drive service = googleAuth.getDriveService();
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
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void Create(String newDocumentName, String docUserEmail) throws IOException {
        Authorizer googleAuth = new Authorizer();
        Drive service = googleAuth.getDriveService();




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

        BatchRequest batch = service.batch();
        Permission userPermission = new Permission()
                .setType("user")
                .setRole("writer")
                .setEmailAddress(docUserEmail);
        service.permissions().create(fileId, userPermission)
                .setFields("id")
                .queue(batch, callback);
        batch.execute();

    }

    public void Update(String documentName) throws IOException {
        Authorizer googleAuth = new Authorizer();
        Drive service = googleAuth.getDriveService();

        FileList result = service.files().list()
                .setPageSize(20)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();

        for (File file: files) {
            if (file.getName().compareTo(documentName)==0){
                FileManager updater = new FileManager(service);
                updater.UpdateFile(documentName,file.getId());
            }
        }

    }

    public void ViewDocuments () throws IOException{
        Authorizer googleAuth = new Authorizer();
        Drive service = googleAuth.getDriveService();

        FileList result = service.files().list()
                .setPageSize(20)
                .setFields("nextPageToken, files(id, name, fileExtension, kind)")
                .execute();
        List<File> files = result.getFiles();

        if (files == null || files.size() == 0) {
            DBG.Log("No files found.");
        } else {
            DBG.Log("Files:");
            int i=0;
            for (File file : files) {
                DBG.Log("%d: %s (%s) is a %s",(i+1), file.getName(), file.getId(),file.getFileExtension());
                i++;
            }
        }
    }


}
