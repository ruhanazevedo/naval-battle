import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PdfController {

    private Pdf pdf;
    private ImageIcon imageIcon;
    private int pageIndex;
    private JLabel view;

    public PdfController(Pdf pdf) {
        this.pdf = pdf;
        view = new JLabel();
        imageIcon = new ImageIcon();
    }

    /**
     * explicit nomenclature
     */
    public void turnImage() {
        String pdfLocation = pdf.getLocation();
        try (PDDocument document = PDDocument.load(new File(pdfLocation))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bim = pdfRenderer.renderImageWithDPI(pageIndex, 300, ImageType.RGB);
            System.out.println("pageIndex = "+pageIndex);
            imageIcon.setImage(bim);
            System.out.println("Width = " + imageIcon.getIconWidth() + " | Height = " + imageIcon.getIconHeight());
            final int width = (int) ((imageIcon.getIconWidth() * 0.3204272363150868));
            final int height = (int) (imageIcon.getIconHeight() * 0.3204272363150868);
            imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING));
            view.setIcon(imageIcon);
            //coordinateWindow();
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
        System.out.println("Width = " + imageIcon.getIconWidth() + " | Height = " + imageIcon.getIconHeight());
        System.out.println("Width = " + imageIcon.getIconWidth() + " | Height = " + imageIcon.getIconHeight());

        Insets insets = view.getInsets();
        frame.setSize(new Dimension(insets.left + insets.right + imageIcon.getIconWidth(), insets.bottom + insets.top + imageIcon.getIconHeight()));

        JScrollPane jsp = new JScrollPane(view);
        frame.add(jsp);
        frame.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(10,10,10,10);
        final JButton previous = new JButton("<");
        previous.setEnabled(false);
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("<");
                if(pageIndex-1 == 0){
                    pageIndex--;
                    previous.setEnabled(false);
                }
                turnImage();
            }
        });
        final JButton next = new JButton(">");
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(">");
                previous.setEnabled(true);
                pageIndex++;
                turnImage();
            }
        });

        buttonPanel.add(previous);
        buttonPanel.add(next);
        frame.add(buttonPanel, BorderLayout.WEST);

        final JLabel clickCoordinate = new JLabel();
        frame.add(clickCoordinate, BorderLayout.SOUTH);
        view.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent me) {
            }

            public void mouseReleased(MouseEvent me) {
            }

            public void mouseEntered(MouseEvent me) {
            }

            public void mouseExited(MouseEvent me) {
            }

            public void mouseClicked(MouseEvent me) {
                Float pageHeight = Float.valueOf(view.getHeight());
                ImmutablePair pair = convertingCoordinates(me.getX(), me.getY(), view.getWidth(), view.getHeight(), imageIcon.getIconWidth(), imageIcon.getIconHeight(), pageHeight);
                Float x = (Float) pair.getKey();
                Float y = (Float) pair.getValue();
                int X = (int) (x * 0.7423580786026201);
                int Y = (int) (y * 0.7407407407407407);
                if (x > imageIcon.getIconWidth() || x < 0 || y > imageIcon.getIconHeight() || y < 0) {
                    clickCoordinate.setText("SELECIONE UMA POSIÇÃO VÁLIDA");
                } else {
                    clickCoordinate.setText("X:" + X + "f ; Y:" + Y + "f");
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
