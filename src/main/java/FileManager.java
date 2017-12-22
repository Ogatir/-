import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileManager {
    private Drive service;

    public FileManager(Drive service){
        this.service = service;
    }

    public File UploadFile(String fileName,String fileType) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        java.io.File filePath = new java.io.File(fileName);
        FileContent mediaContent = new FileContent(fileType, filePath);
        File file = service.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
        System.out.println("File ID: " + file.getId());
        return file;
        }

    public String CreateFile(String fileName)throws  IOException{

        OutputStream outputStream = new FileOutputStream(fileName);
        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setMimeType("application/vnd.google-apps.document");
        java.io.File filePath = new java.io.File(fileName);
        FileContent mediaContent = new FileContent("text/plain", filePath);
        try {
            File file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            DBG.Log("Created Google Drive file %s",file.getId());
            return file.getId();
        } catch(IOException e){
            DBG.Log("[ERROR: FileManager.CreateFile()]: %s",e.getMessage());
            return null;
        }
    }

    public int DownloadFile(String fileID, String fileName){
        try {
            OutputStream outputStream = new FileOutputStream(fileName);
            //service.files().get(fileID).executeMediaAndDownloadTo(outputStream);
            service.files().export(fileID,"text/plain").executeMediaAndDownloadTo(outputStream);
            return 0;
        } catch (IOException e) {

            DBG.Log("[ERROR: FileManager.DownloadFile()]: %s",e.getMessage());
            return 1;
        }
    }

    public void UpdateFile(String fileName, String fileId)throws  IOException{
        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setMimeType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        java.io.File filePath = new java.io.File(fileName);
        try {
            FileContent mediaContent =
                    new FileContent("application/vnd.google-apps.document", filePath);
            service.files().update(fileId, fileMetadata, mediaContent).execute();
        } catch (IOException e){
            DBG.Log("[ERROR: FileManager.UpdateFile()]: %s",e.getMessage());
        }
    }
}
