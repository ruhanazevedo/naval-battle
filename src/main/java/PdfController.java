import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PdfController {

    private Pdf pdf;
    private ImageIcon imageIcon;

    public PdfController(Pdf pdf) {
        this.pdf = pdf;
    }

    /**
     * explicit nomenclature
     */
    public void turnImage() {
        String pdfLocation = pdf.getLocation();
        imageIcon = null;
        try (PDDocument document = PDDocument.load(new File(pdfLocation))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
                imageIcon = new ImageIcon();
                imageIcon.setImage(bim);
                System.out.println("Width = " + imageIcon.getIconWidth() + " | Height = " + imageIcon.getIconHeight());
            }
            document.close();
        } catch (IOException e) {
            System.err.println("Exception while trying to create pdf document - " + e);
        }
    }

    /**
     * create a window for show coordinates of clicks
     */
    public void coordinateWindow() {
        JFrame frame = new JFrame();
        frame.setTitle("getting x y coordinates");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("src\\icon\\icon.png"));
        frame.setResizable(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width / 2, screenSize.height / 4);

        final int width = (int) ((imageIcon.getIconWidth() * 0.3204272363150868));
        final int height = (int) (imageIcon.getIconHeight() * 0.3204272363150868);
        System.out.println("Width = " + imageIcon.getIconWidth() + " | Height = " + imageIcon.getIconHeight());
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING));
        System.out.println("Width = " + imageIcon.getIconWidth() + " | Height = " + imageIcon.getIconHeight());
        final JLabel jb = new JLabel(imageIcon);
        Insets insets = jb.getInsets();
        frame.setSize(new Dimension(insets.left + insets.right + width, insets.bottom + insets.top + height));

        JScrollPane jsp = new JScrollPane(jb);
        frame.add(jsp);
        frame.setSize(width, height);

        final JTextField text = new JTextField();
        frame.add(text, BorderLayout.SOUTH);
        jb.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent me) {
            }

            public void mouseReleased(MouseEvent me) {
            }

            public void mouseEntered(MouseEvent me) {
            }

            public void mouseExited(MouseEvent me) {
            }

            public void mouseClicked(MouseEvent me) {
                Float pageHeight = Float.valueOf(jb.getHeight());
                ImmutablePair pair = convertingCoordinates(me.getX(), me.getY(), jb.getWidth(), jb.getHeight(), width, height, pageHeight);
                Float x = (Float) pair.getKey();
                Float y = (Float) pair.getValue();
                int X = (int) (x * 0.7423580786026201);
                int Y = (int) (y * 0.7407407407407407);
                if (x > width || x < 0 || y > height || y < 0) {
                    text.setText("SELECIONE UMA POSIÇÃO VÁLIDA");
                } else {
                    text.setText("X:" + X + "f ; Y:" + Y + "f");
                }
            }
        });

        frame.setVisible(true);
    }

    /**
     * @param _x - coordinate of x
     * @param _y - coordinate of y
     * @param actualWidth - actual width
     * @param actualHeight - actual height
     * @param initialWidth - initial width
     * @param initialHeight - initial height
     * @param pageHeight
     * @return
     */
    private ImmutablePair convertingCoordinates(int _x, int _y, int actualWidth, int actualHeight, int initialWidth, int initialHeight, Float pageHeight) {
        ImmutablePair pair;
        Float x = _x + 0.0f;
        Float y = _y * -1 + pageHeight;

        if (actualWidth > initialWidth) x -= (actualWidth - initialWidth) / 2;
        if (actualWidth < initialWidth) x += (initialWidth - actualWidth) / 2;

        if (actualHeight > initialHeight) y -= (actualHeight - initialHeight) / 2;
        if (actualHeight < initialHeight) y += (initialHeight - actualHeight) / 2;

        pair = new ImmutablePair(x, y);
        return pair;
    }

}
