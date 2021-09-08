package pl.futurecollars.invoicing.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.futurecollars.invoicing.config.Configurations;

@Repository
@NoArgsConstructor
public class FileService {

    private void writeToFile(String line, String path) {
        try {
            Files.writeString(Paths.get(path), line + "\n", StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> readFromDatabase() {
        try {
            return Files.readAllLines(Paths.get(Configurations.FILE_DATABASE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeToDatabase(String line) {
        writeToFile(line, Configurations.FILE_DATABASE_PATH);
    }

    public void writeToIdKeeper(String id) {
        writeToFile(id, Configurations.FILE_ID_KEEPER_PATH);
    }

    public void clearDatabase() {
        try {
            Files.writeString(Paths.get(Configurations.FILE_DATABASE_PATH), "");
            Files.writeString(Paths.get(Configurations.FILE_ID_KEEPER_PATH), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
