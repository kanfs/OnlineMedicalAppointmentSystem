package com.kanfs.omas.utils;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Service
public class AITriageUtil {
    private Process process;
    private PrintWriter writer;
    private BufferedReader reader;
    private boolean flag;

    @PostConstruct
    public void startPythonProcess() throws IOException {
        File directory = new File("./src/main/resources/python/BertTriage");
        ProcessBuilder builder = new ProcessBuilder("python", "./app.py");
        builder.directory(directory);
        builder.redirectErrorStream(true); // Redirect error stream to the standard output stream
        process = builder.start();
        writer = new PrintWriter(process.getOutputStream(), true, StandardCharsets.UTF_8);
        reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        flag = false;
    }
    private void consumeInitialOutput() throws IOException {
        String line ;
        while ((line = reader.readLine())!=null ) {
            System.out.println("line = "+line);
            if(line.equals("Python process ready")) break;
        }
        flag = true;
    }

    public String sendMessage(String message) throws IOException {
        if( flag == false ) consumeInitialOutput();
        System.out.println(message);
        writer.println(message);

        // Read the output from the standard output and standard error streams
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine())!=null) {
            System.out.println("line = "+line);
            output.append(line).append("\n");
            if(reader.ready()==false) break;
        }
        return output.toString().trim(); // Trim to remove the last newline character
    }

    @PreDestroy
    public void stopPythonProcess() throws IOException {
        writer.close();
        reader.close();
        process.destroy();
    }
}
