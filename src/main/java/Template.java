import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Template extends JFrame {

    public Template() {
        firstFrame();
    }

    private void firstFrame(){
        setTitle("getting x y coordinates");
        setVisible(true);
        setSize(300, 300);
        JButton searchPdf = new JButton("Open a File..");
        add(searchPdf);
        setIconImage(Toolkit.getDefaultToolkit().getImage("src\\icon\\icon.png"));
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
        System.out.println(fd.getFile());
        Pdf currentPdf = new Pdf(pdfLocation);
        miscellaneous(currentPdf);
    }

    private void miscellaneous(Pdf currentPdf) {
        PdfController controller = new PdfController(currentPdf);
        controller.turnImage();
        controller.coordinateWindow();
    }
}
