package javapad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static javapad.JavaPad.directory;
import static javapad.JavaPad.file;

public class Runner extends Thread{

    @Override
    public void run(){
        try{
            String name = file.getName();
            name = name.substring(0, name.lastIndexOf('.'));
            ProcessBuilder pBuilder = new ProcessBuilder("java", name);
            pBuilder.directory(directory);
            pBuilder.redirectErrorStream(true);
            Process process = pBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
