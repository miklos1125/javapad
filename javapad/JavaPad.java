package javapad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;


public class JavaPad extends JPanel{
    
    final static int SCREEN_HEIGHT =
            Toolkit.getDefaultToolkit().getScreenSize().height;
    private static final int COLS = 85;
    static JFrame frame;
    static JTextArea area;
    static File file;
    static File directory;
    Image coffee;
        
    public static void main(String[] args){
        file = new File("MyJavaApp.java");
        JFileChooser fr = new JFileChooser();
        FileSystemView fsw = fr.getFileSystemView();
        directory = new File(fsw.getDefaultDirectory().toString());
    
        frame = new JFrame();
        renameFrame();
        frame.getContentPane().add(new JavaPad());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(ClassNotFoundException | IllegalAccessException |
                InstantiationException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        JOptionPane.showMessageDialog(frame, 
                "Greetings!\n"
                + "This is a poor man's IDE.\n"
                + "You can load, save and edit java files\n"
                + "and compile them in the same directory.\n\n"
                + "This app has been created for experimenting,\n"
                + "and for expanding my programming skills.\n"
                + "Thanks for watching!");
    }
    
    static void renameFrame(){
        frame.setTitle("JAVA EDITOR - " + file.toString());
    }
    
    JavaPad(){
        URL u = this.getClass().getResource("pic/coffee.png");
        coffee = Toolkit.getDefaultToolkit().createImage(u);
                
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 10, 10);
        setLayout(flowLayout);
        this.setPreferredSize(new Dimension((int)(COLS*8),
                (int)(SCREEN_HEIGHT*0.8)));
        
        Color beige = new Color(220, 216, 190);
        setBackground(beige);
        
        String starterCode = "public class MyJavaApp{\n\n\t"
                + "public static void main(String[] args){\n\t\t"
                + "System.out.println(\"I need a good coffee!\");\n\t}\n\n}";
        area = new JTextArea((int)(SCREEN_HEIGHT*0.043), COLS);
        area.setTabSize(2);
        area.setFont(new Font ("Monospaced", Font.PLAIN, 12));

        area.setText(starterCode);
        JScrollPane scrollPane = new JScrollPane(area,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        area.setBackground(Color.WHITE);
        //area.setLineWrap(true);
        
        JButton load = new JButton("Load");
        load.setBackground(new Color(170, 255, 0));
        load.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                load();
            }
        });
        
        JButton saveAs = new JButton("Save as");
        saveAs.setBackground(Color.YELLOW);
        saveAs.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                saveAs();
            }
        });
        
        JButton save = new JButton("Save");
        save.setBackground(Color.ORANGE);
        save.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                save();
            }
        });
        
        JButton clear = new JButton("Clear");
        clear.setBackground(Color.RED);
        clear.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                clear();
            }
        });
        
        JButton compile = new JButton("Compile");
        compile.setBackground(beige);
        compile.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                try{
                    compile();
                } catch (InterruptedException ex){
                    ex.printStackTrace();
                }
            }
        });
        
        add(scrollPane, BorderLayout.PAGE_START);
        add(load);
        add(saveAs);
        add(save);
        add(clear);
        add(compile);
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(coffee, 0, this.getHeight()-180, this.getWidth(), 200, this);
    }

    
    void load(){
        JFileChooser jfc = new JFileChooser(directory);
        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter =
                new FileNameExtensionFilter("Java source files", "java");
        jfc.addChoosableFileFilter(filter);
        int choice = jfc.showOpenDialog(frame);
        if (choice == JFileChooser.APPROVE_OPTION){
        file = new File(jfc.getSelectedFile().getName());
        directory = jfc.getCurrentDirectory();
        } else {
            return;
        }
        
        try {
            String filePlaceAndName = directory.toString() +"\\"+ file.toString();
            FileInputStream fis = new FileInputStream(filePlaceAndName);
            int number = fis.available();
            byte[] y = new byte[number];
            fis.read(y);
            fis.close();
            String backtext = new String(y);
            area.setText(backtext);
            renameFrame();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    

    
    void save(){
        int choice = JOptionPane.showConfirmDialog(frame,
                "Are you sure?", "Overwrite "+file+"?", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.OK_OPTION){
            return;
        }
        try {
            saveContent();
//            String filePathAndName = directory.toString() +"\\"+ file.toString();
//            FileOutputStream fos = new FileOutputStream(filePathAndName);
//            String text = area.getText();
//            byte[] b = text.getBytes();
//            fos.write(b);
//            fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    
    void saveAs(){
        JFileChooser jfc = new JFileChooser(directory);
            jfc.setSelectedFile(file);
            jfc.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = 
                    new FileNameExtensionFilter("Java source files", "java");
            jfc.addChoosableFileFilter(filter);
            int choice = jfc.showSaveDialog(frame);
            if (choice == JFileChooser.APPROVE_OPTION){
                file = new File(javaExtension(jfc.getSelectedFile().getName()));
                directory = jfc.getCurrentDirectory();
            } else{
                return;
            }
        try {
            saveContent();
//            String filePathAndName = directory.toString() +"\\"+ file.toString();
//            FileOutputStream fos = new FileOutputStream(filePathAndName);
//            String text = area.getText();
//            byte[] b = text.getBytes();
//            fos.write(b);
//            fos.close();
            renameFrame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    void clear(){
        int choice = JOptionPane.showConfirmDialog(frame,
                "Are you sure?", "Delete all the code?", JOptionPane.YES_NO_OPTION);  
        if (choice == JOptionPane.OK_OPTION){
            area.setText("");
        }
    }
    
    void compile() throws InterruptedException{
        String filePathAndName = directory.toString() +"\\"+ file.toString();
        try{
            Runtime.getRuntime().exec("javac "+file.toString(), null, directory);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
    
    String javaExtension(String toTest){
        int index = toTest.indexOf('.');
        if (index == -1){
            return toTest.concat(".java");
        } else if (toTest.substring(index+1).equals("java")){
            return toTest;
        } else{
            return toTest.substring(0, index+1).concat("java");
        }
    }
    
    void saveContent() throws IOException{
        String filePathAndName = directory.toString() +"\\"+ file.toString();
        FileOutputStream fos = new FileOutputStream(filePathAndName);
        String text = area.getText();
        byte[] b = text.getBytes();
        fos.write(b);
        fos.close();
    }
}
