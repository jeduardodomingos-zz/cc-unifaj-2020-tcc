package br.com.unifaj.service;

import br.com.unifaj.constants.Constants;
import br.com.unifaj.interfaces.Validator;
import br.com.unifaj.validators.BornDateValidator;
import br.com.unifaj.validators.DocumentValidator;
import br.com.unifaj.validators.DriverLicenseValidator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static br.com.unifaj.utils.HashUtils.hash;
import static java.util.Arrays.asList;

public class FileProcessService {

    private List<Validator> validators = new ArrayList<>();

    public FileProcessService() {
        this.validators.add(new DriverLicenseValidator());
        this.validators.add(new DocumentValidator());
        this.validators.add(new BornDateValidator());
    }

    private String process(String row) {
        List<String> values = asList(row.split(";"));
        AtomicReference<String> output = new AtomicReference<>("");

        values = values.stream()
                .map(this::hashData)
                .collect(Collectors.toList());

        values.forEach(value -> {
            output.set(output.get() + value + ";");
        });

        return output.get();
    }

    private String hashData(String value) {
        AtomicReference<String> returnValue = new AtomicReference<>(value);

        validators.forEach(validator -> {
            if (validator.validate(returnValue.get())) {
                returnValue.set(hash(returnValue.get(), Constants.MD5.value()));
            }
        });

        return returnValue.get();
    }

    public void start(String path, String output) {
        String line = "";

        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));

            while ((line = in.readLine()) != null) {
                this.write(output, process(line));
            }

            in.close();
        } catch (final IOException e) {
            System.out.println("Erro ao realizar leitura do arquivo");
        }
    }

    private void write(String path, String value) {

        try {
            value += "\n";
            Files.write(Paths.get(path), value.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Erro ao gravar informações processadas.");
        }

    }


}
