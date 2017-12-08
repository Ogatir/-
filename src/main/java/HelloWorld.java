import com.google.api.services.drive.model.*;
import com.google.api.services.drive.Drive;

import java.io.IOException;
import java.util.List;

public class HelloWorld {

    public static void main(String[] args) throws IOException {
        // Build a new authorized API client service.
        Authorizer googleAuth = new Authorizer();
        Drive service = googleAuth.getDriveService();

        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name, fileExtension)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.size() == 0) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s) is a %s\n", file.getName(), file.getId(),file.getFileExtension());

            }
        }
    }

}
