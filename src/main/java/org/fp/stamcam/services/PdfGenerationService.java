package org.fp.stamcam.services;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.fp.stamcam.models.Deed;
import org.fp.stamcam.models.Party;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

@Service
public class PdfGenerationService {

    public byte[] generateDeedPdf(Deed deed) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Add Title
        document.add(new Paragraph(deed.getTitle())
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(20));

        document.add(new Paragraph("\n"));

        // Add Matter
        document.add(new Paragraph("Deed Details")
                .setBold()
                .setFontSize(14));
        document.add(new Paragraph(deed.getMatter()));

        document.add(new Paragraph("\n\n"));

        // Add Parties
        document.add(new Paragraph("Parties Involved")
                .setBold()
                .setFontSize(14));

        for (Party party : deed.getParties()) {
            document.add(new Paragraph("Party Name: " + party.getName()));
            document.add(new Paragraph("Party Type: " + party.getPartyType()));
            document.add(new Paragraph("Email: " + party.getEmailId()));
            document.add(new Paragraph("Phone: " + party.getPhoneNumber()));

            if (party.getDocuments() != null && !party.getDocuments().isEmpty()) {
                document.add(new Paragraph("ID Documents:").setBold());
                for (org.fp.stamcam.models.Document doc : party.getDocuments()) {
                    try {
                        byte[] imageBytes = Base64.getDecoder().decode(doc.getBase64Image());
                        FileOutputStream fos = new FileOutputStream("doc.pdf");
                        fos.write(imageBytes);
                        fos.close();
                        Image img = new Image(ImageDataFactory.create("doc.pdf"));
                        img.scaleToFit(200, 200); // Scale image to fit
                        document.add(img);
                    } catch (Exception e) {
                        // Handle image creation error
                        document.add(new Paragraph("Could not load image for document: " + doc.getId()));
                    }
                }
            }
            document.add(new Paragraph("\n"));
        }

        document.close();
        File pdfFile = new File("doc.pdf");
        if (pdfFile.exists()) {
            pdfFile.delete(); // Clean up temporary file
        }
        return baos.toByteArray();
    }
}
