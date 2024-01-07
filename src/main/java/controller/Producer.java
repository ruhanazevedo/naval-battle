package controller;

import org.apache.commons.lang3.tuple.ImmutablePair;
import pojo.Pdf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Producer {

    private final Image iconImage = Toolkit.getDefaultToolkit().getImage("src\\icon\\boat.png");
    protected Pdf pdf;
    protected ImageIcon imageIcon;
    protected int pageIndex;
    protected int finalPage;
    protected JLabel view;

    public Producer(Pdf pdf) {
        this.pdf = pdf;
        view = new JLabel();
        imageIcon = new ImageIcon();
    }

    /**
     * create a window for show coordinates of clicks
     */
    public void coordinateWindow() {
        JFrame frame = new JFrame();
        frame.setTitle("Naval-battle");
        frame.setIconImage(iconImage);
        frame.setResizable(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width / 2, screenSize.height / 4);

        Insets insets = view.getInsets();
        frame.setSize(new Dimension(insets.left + insets.right + imageIcon.getIconWidth(), insets.bottom + insets.top + imageIcon.getIconHeight()));

        JScrollPane jsp = new JScrollPane(view);
        frame.add(jsp);
        frame.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(10,10,10,10);
        final JButton previous = new JButton("<");
        previous.setEnabled(false);

        final JButton next = new JButton(">");
        if(finalPage==0){
            next.setEnabled(false);
        }
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageIndex--;
                if(pageIndex-1 < 0){
                    previous.setEnabled(false);
                }
                next.setEnabled(true);
                Renderer.turnImage(Producer.this);
            }
        });
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageIndex++;
                if(pageIndex+1 > finalPage){
                    next.setEnabled(false);
                }
                previous.setEnabled(true);
                Renderer.turnImage(Producer.this);
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

    public void warning(String message){
        javax.swing.JOptionPane.showMessageDialog(null, message, "WARNING", javax.swing.JOptionPane.WARNING_MESSAGE);
    }

}
