package controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Renderer {

    public static void turnImage(Producer producer) {
        String pdfLocation = producer.pdf.getLocation();
        try (PDDocument document = PDDocument.load(new File(pdfLocation))) {
            producer.finalPage = document.getNumberOfPages()-1;
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bim = pdfRenderer.renderImageWithDPI(producer.pageIndex, 300, ImageType.RGB);
            producer.imageIcon.setImage(bim);
            final int width = (int) ((producer.imageIcon.getIconWidth() * 0.3204272363150868));
            final int height = (int) (producer.imageIcon.getIconHeight() * 0.3204272363150868);
            producer.imageIcon = new ImageIcon(producer.imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING));
            producer.view.setIcon(producer.imageIcon);
        } catch (IOException e) {
            System.err.println("Exception while trying to create pdf document - " + e);
        }
    }
}
