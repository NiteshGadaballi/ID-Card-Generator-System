import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class IDCardGenerator {
    private static final String OUTPUT_FOLDER = "output/";

    public static File generatePDF(String name, String id, String designation, String contact, File photo) {
        File outputFile = new File(OUTPUT_FOLDER + id + "_IDCard.pdf");
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);

                // Drawing Border
                contentStream.setLineWidth(2);
                contentStream.moveTo(50, 700);
                contentStream.lineTo(550, 700);
                contentStream.lineTo(550, 500);
                contentStream.lineTo(50, 500);
                contentStream.closeAndStroke();

                // Add Text
                contentStream.beginText();
                contentStream.setLeading(20);
                contentStream.newLineAtOffset(60, 680);
                contentStream.showText("K. C. College"); // You can customize this
                contentStream.newLine();
                contentStream.showText("ID Card");
                contentStream.newLine();
                contentStream.showText("Name: " + name);
                contentStream.newLine();
                contentStream.showText("ID: " + id);
                contentStream.newLine();
                contentStream.showText("Designation: " + designation);
                contentStream.newLine();
                contentStream.showText("Contact: " + contact);
                contentStream.endText();

                // Adding Photo
                if (photo != null) {
                    BufferedImage image = ImageIO.read(photo);
                    PDImageXObject pdImage = PDImageXObject.createFromFile(photo.getAbsolutePath(), document);
                    contentStream.drawImage(pdImage, 400, 550, 100, 100);
                }
            }

            document.save(outputFile);
            System.out.println("PDF Generated: " + outputFile.getAbsolutePath());
            return outputFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
