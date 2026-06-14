package org.fp.stamcam.services;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.fp.stamcam.models.Deed;
import org.fp.stamcam.models.IdType;
import org.fp.stamcam.models.Party;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PdfGenerationService {

    private static final DeviceRgb HEADER_BG = new DeviceRgb(30, 64, 120);
    private static final DeviceRgb SECTION_BG = new DeviceRgb(220, 230, 245);
    private static final DeviceRgb ROW_ALT_BG = new DeviceRgb(245, 248, 255);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public byte[] generateDeedPdf(Deed deed) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (Document document = new Document(new PdfDocument(new PdfWriter(baos)))) {
            document.setMargins(36, 36, 36, 36);

            addDeedHeader(document, deed);
            addDeedInfoTable(document, deed);
            addMatterSection(document, deed);
            addPartiesSection(document, deed);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
        return baos.toByteArray();
    }

    private void addDeedHeader(Document document, Deed deed) {
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1}))
                .useAllAvailableWidth();
        Cell headerCell = new Cell()
                .add(new Paragraph("DEED DOCUMENT")
                        .setFontSize(10)
                        .setFontColor(ColorConstants.WHITE)
                        .setTextAlignment(TextAlignment.CENTER))
                .add(new Paragraph(deed.getTitle() != null ? deed.getTitle() : "Untitled Deed")
                        .setFontSize(20)
                        .setBold()
                        .setFontColor(ColorConstants.WHITE)
                        .setTextAlignment(TextAlignment.CENTER))
                .setBackgroundColor(HEADER_BG)
                .setPadding(16)
                .setBorder(Border.NO_BORDER);
        headerTable.addCell(headerCell);
        document.add(headerTable);
        document.add(new Paragraph("\n").setFontSize(4));
    }

    private void addDeedInfoTable(Document document, Deed deed) {
        document.add(sectionLabel("Deed Information"));

        Table table = new Table(UnitValue.createPercentArray(new float[]{30, 70}))
                .useAllAvailableWidth()
                .setMarginBottom(12);

        addInfoRow(table, "Deed ID", deed.getId(), false);
        addInfoRow(table, "Deed Type",
                deed.getType() != null ? deed.getType().getDisplayName() : "-", true);
        addInfoRow(table, "Status",
                deed.getStatus() != null ? deed.getStatus().toString() : "-", false);
        addInfoRow(table, "Created On",
                deed.getCreatedAt() != null ? deed.getCreatedAt().format(DATE_FMT) : "-", true);
        addInfoRow(table, "Last Updated",
                deed.getUpdatedAt() != null ? deed.getUpdatedAt().format(DATE_FMT) : "-", false);

        document.add(table);
    }

    private void addMatterSection(Document document, Deed deed) {
        if (deed.getMatter() == null || deed.getMatter().isBlank()) return;

        document.add(sectionLabel("Deed Matter / Content"));

        Table table = new Table(UnitValue.createPercentArray(new float[]{1}))
                .useAllAvailableWidth()
                .setMarginBottom(12);
        Cell cell = new Cell()
                .add(new Paragraph(deed.getMatter()).setFontSize(10).setTextAlignment(TextAlignment.JUSTIFIED))
                .setPadding(10)
                .setBackgroundColor(ROW_ALT_BG)
                .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f));
        table.addCell(cell);
        document.add(table);
    }

    private void addPartiesSection(Document document, Deed deed) {
        if (deed.getParties() == null || deed.getParties().isEmpty()) return;

        document.add(sectionLabel("Parties Involved"));

        int index = 1;
        for (Party party : deed.getParties()) {
            addPartyBlock(document, party, index++);
        }
    }

    private void addPartyBlock(Document document, Party party, int index) {
        // Party sub-header
        Table subHeader = new Table(UnitValue.createPercentArray(new float[]{1}))
                .useAllAvailableWidth();
        subHeader.addCell(new Cell()
                .add(new Paragraph("Party " + index + ": " + (party.getName() != null ? party.getName() : "-"))
                        .setFontSize(12).setBold().setFontColor(HEADER_BG))
                .setBackgroundColor(SECTION_BG)
                .setPadding(8)
                .setBorder(new SolidBorder(HEADER_BG, 1)));
        document.add(subHeader);

        // Separate user photo from other documents
        org.fp.stamcam.models.Document userPhoto = null;
        List<org.fp.stamcam.models.Document> idDocs = null;

        if (party.getDocuments() != null && !party.getDocuments().isEmpty()) {
            userPhoto = party.getDocuments().stream()
                    .filter(d -> d.getIdType() == IdType.USER_PHOTO)
                    .findFirst()
                    .orElse(null);
            idDocs = party.getDocuments().stream()
                    .filter(d -> d.getIdType() != IdType.USER_PHOTO)
                    .collect(Collectors.toList());
        }

        // Info table + photo side by side
        float[] colWidths = userPhoto != null ? new float[]{65, 35} : new float[]{100};
        Table infoPhotoTable = new Table(UnitValue.createPercentArray(colWidths))
                .useAllAvailableWidth()
                .setMarginTop(2);

        // Left: party info
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{40, 60}))
                .useAllAvailableWidth();
        addInfoRow(infoTable, "Party ID", party.getId(), false);
        addInfoRow(infoTable, "Name", party.getName(), true);
        addInfoRow(infoTable, "Party Type",
                party.getPartyType() != null ? party.getPartyType().toString() : "-", false);
        addInfoRow(infoTable, "Email", party.getEmailId(), true);
        addInfoRow(infoTable, "Phone", party.getPhoneNumber(), false);

        infoPhotoTable.addCell(new Cell()
                .add(infoTable)
                .setBorder(Border.NO_BORDER)
                .setPaddingRight(6));

        // Right: user photo
        if (userPhoto != null) {
            Cell photoCell = new Cell()
                    .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f))
                    .setPadding(4)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);
            Image img = decodeImage(userPhoto.getBase64Image(), 120, 140);
            if (img != null) {
                img.setHorizontalAlignment(HorizontalAlignment.CENTER);
                photoCell.add(new Paragraph("Photo").setFontSize(8)
                        .setFontColor(ColorConstants.GRAY).setTextAlignment(TextAlignment.CENTER));
                photoCell.add(img);
            } else {
                photoCell.add(new Paragraph("Photo unavailable")
                        .setFontSize(9).setFontColor(ColorConstants.GRAY)
                        .setTextAlignment(TextAlignment.CENTER));
            }
            infoPhotoTable.addCell(photoCell);
        }

        document.add(infoPhotoTable);

        // ID document images
        if (idDocs != null && !idDocs.isEmpty()) {
            document.add(new Paragraph("Identity & Address Documents")
                    .setFontSize(10).setBold().setFontColor(HEADER_BG)
                    .setMarginTop(6).setMarginBottom(4));

            // Two columns for document images
            Table docsTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                    .useAllAvailableWidth();

            for (org.fp.stamcam.models.Document doc : idDocs) {
                Cell docCell = new Cell()
                        .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f))
                        .setPadding(6)
                        .setBackgroundColor(ROW_ALT_BG);

                String label = doc.getIdType() != null ? doc.getIdType().getDisplayName() : "Document";
                docCell.add(new Paragraph(label)
                        .setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER));

                Image img = decodeImage(doc.getBase64Image(), 180, 120);
                if (img != null) {
                    img.setHorizontalAlignment(HorizontalAlignment.CENTER);
                    docCell.add(img);
                } else {
                    docCell.add(new Paragraph("Image unavailable")
                            .setFontSize(9).setFontColor(ColorConstants.GRAY)
                            .setTextAlignment(TextAlignment.CENTER));
                }
                docsTable.addCell(docCell);
            }
            // Fill last row if odd number of docs
            if (idDocs.size() % 2 != 0) {
                docsTable.addCell(new Cell().setBorder(Border.NO_BORDER));
            }
            document.add(docsTable);
        }

        document.add(new Paragraph("\n").setFontSize(6));
    }

    private void addInfoRow(Table table, String label, String value, boolean alternate) {
        Cell labelCell = new Cell()
                .add(new Paragraph(label).setFontSize(10).setBold())
                .setBackgroundColor(alternate ? ROW_ALT_BG : ColorConstants.WHITE)
                .setPadding(6)
                .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f));

        Cell valueCell = new Cell()
                .add(new Paragraph(value != null ? value : "-").setFontSize(10))
                .setBackgroundColor(alternate ? ROW_ALT_BG : ColorConstants.WHITE)
                .setPadding(6)
                .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f));

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private Paragraph sectionLabel(String text) {
        return new Paragraph(text)
                .setFontSize(13)
                .setBold()
                .setFontColor(HEADER_BG)
                .setMarginTop(10)
                .setMarginBottom(4)
                .setBorderBottom(new SolidBorder(HEADER_BG, 1.5f));
    }

    private Image decodeImage(String base64, float maxWidth, float maxHeight) {
        if (base64 == null || base64.isBlank()) return null;
        try {
            // Strip data URI prefix if present
            String data = base64.contains(",") ? base64.split(",", 2)[1] : base64;
            byte[] bytes = Base64.getDecoder().decode(data);
            Image img = new Image(ImageDataFactory.create(bytes));
            img.scaleToFit(maxWidth, maxHeight);
            return img;
        } catch (Exception e) {
            return null;
        }
    }
}