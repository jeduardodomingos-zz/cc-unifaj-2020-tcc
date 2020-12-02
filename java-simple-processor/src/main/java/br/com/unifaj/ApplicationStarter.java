package br.com.unifaj;

import br.com.unifaj.service.FileProcessService;

import java.nio.file.Files;
import java.nio.file.Paths;

import static java.time.ZonedDateTime.now;

public class ApplicationStarter  {

    public static void main(String[] args) {
        String filePath = "/home/jdomingos/TCC/output/generated.csv";
        String outputPath = "/home/jdomingos/TCC/Projetos/java-simple-processor/src/main/resources/output/generated.csv";
        FileProcessService processService = new FileProcessService();

        System.out.println("Starting File process: " + now());
        System.out.println("The input file size is: " + getFileSize(filePath) + " MB");

        processService.start(filePath, outputPath);

        System.out.println("Finishing File process: " + now());
    }

    private static Long getFileSize(String path) {

        try {
            Long size = Files.size(Paths.get(path));

            return (size / 1024) / 1024;

        }catch (Exception ex) {
            return 0L;
        }
    }

}
