package com.maimai.builders;

import com.maimai.bean.Constants;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

// build fixed utility classes
@Slf4j
public class BuildBaseClass {
    public static void execute() {
        build("DateUtils", Constants.PATH_UTILS);

    }

    private static void build(String filename, String outputPath) {
        File folder = new File(outputPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        
        File javaFile = new File(outputPath, filename + ".java");
        try (
                OutputStream out = Files.newOutputStream(javaFile.toPath());
                OutputStreamWriter ow = new OutputStreamWriter(out);
                BufferedWriter bw = new BufferedWriter(ow);
                InputStream in = Files.newInputStream(Paths.get(Objects.requireNonNull(BuildBaseClass.class.getClassLoader().getResource("templates/" + filename + ".txt")).getPath()));
                InputStreamReader ir = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(ir)
        ) {
            bw.write("package " + Constants.PACKAGE_UTILS + ";");
            bw.newLine();
            bw.newLine();

            // Add code to copy the content from the template file to the output file here
            String line;
            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.newLine();
            }

        } catch (Exception e) {
            log.info("fail to generate base class: {}", filename, e);
        }
    }
}
