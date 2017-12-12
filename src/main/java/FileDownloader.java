import com.google.api.services.drive.Drive;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileDownloader {
    private Drive service;

    public FileDownloader(Drive service){
        this.service = service;
    }

    public void DownloadFile(String fileID, String fileName){
        try {
            OutputStream outputStream = new FileOutputStream("/home/ivan/"+fileName+".docx");
            service.files().export(fileID,"application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                    .executeMediaAndDownloadTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
