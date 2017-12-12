import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class HelloWorld {

    public static void main(String[] args) throws IOException {
        // Build a new authorized API client service.
        Authorizer googleAuth = new Authorizer();

        Drive service = googleAuth.getDriveService();
        FileUploader fileUploader = new FileUploader(service);
        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
                .setPageSize(20)
                .setFields("nextPageToken, files(id, name, fileExtension)")
                .execute();
      //  fileUploader.uploadFile();
        List<File> files = result.getFiles();
      //  HashMap <String,String> fileNames=new HashMap<String, String>();

        if (files == null || files.size() == 0) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            int i=0;
            for (File file : files) {
               // fileNames.put(file.getId(),file.getName());
                System.out.printf("%d: %s (%s) is a %s\n",(i+1), file.getName(), file.getId(),file.getFileExtension());
                i++;
            }
            FileDownloader fileDownloader= new FileDownloader(service);
            fileDownloader.DownloadFile(files.get(8).getId(),files.get(8).getName());
            System.out.printf("Main executed");
        }
    }


}
