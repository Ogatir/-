import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;

public class FileUploader {
    private Drive service;
    private String fileName;

    public FileUploader(Drive service){
        this.service = service;
        fileName = "/home/ivan/image.jpg";
    }

    public File uploadFile( ) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName("photo.jpg");
        java.io.File filePath = new java.io.File(fileName);
        FileContent mediaContent = new FileContent("image/jpeg", filePath);
        File file = service.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
        System.out.println("File ID: " + file.getId());
        return file;
    }

}
