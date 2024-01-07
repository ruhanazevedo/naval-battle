package controller;

import pojo.Pdf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Picker extends JFrame {

    private final Image iconImage = Toolkit.getDefaultToolkit().getImage("src\\icon\\boat.png");

    public Picker() {
        firstFrame();
    }

    private void firstFrame(){
        setTitle("Naval-battle");
        setVisible(true);
        setSize(300, 300);
        JButton searchPdf = new JButton("Open a File..");
        add(searchPdf);
        setIconImage(iconImage);
        searchPdf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooser();
            }
        });
    }

    private void chooser() {
        FileDialog fd = new FileDialog((Frame) null);
        fd.setDirectory("C:\\Users\\" + System.getProperty("user.name") + "\\Documents");
        fd.setDirectory(fd.getDirectory());
        fd.setVisible(true);
        String pdfLocation = fd.getDirectory() + fd.getFile();
        Pdf currentPdf = new Pdf(pdfLocation);
        if(fd.getFile() == null){
            currentPdf = null;
        }
        render(currentPdf);

    }

    private void render(Pdf currentPdf) {
        Producer producer = new Producer(currentPdf);
        if(currentPdf == null){
            producer.warning("You must pick a file to continue.");
        }
        else {
            Renderer.turnImage(producer);
            producer.coordinateWindow();
        }
    }
}
