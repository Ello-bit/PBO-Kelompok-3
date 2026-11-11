package com.example.laporan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

public class LaporanPengajuanKUR implements Exportable {

    // Atribut laporan sesuai diagram kelas
    private int id;
    private LocalDateTime tanggalGenerate;
    private String filePath;

    // Constructor
    public LaporanPengajuanKUR(int id, String filePath) {
        this.id = id;
        this.filePath = filePath;
        this.tanggalGenerate = LocalDateTime.now();
    }

    // Method untuk ekspor PDF
    @Override
    public void exportToPDF() {

        try {
            Path output = Path.of(filePath);

            if (output.getParent() != null) {
                Files.createDirectories(output.getParent());
            }

            try (PDDocument document = new PDDocument()) {

                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                PDType1Font font = new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA
                );

                try (PDPageContentStream content =
                        new PDPageContentStream(document, page)) {

                    content.beginText();
                    content.setFont(font, 18);
                    content.newLineAtOffset(50, 780);
                    content.showText("LAPORAN PENGAJUAN KUR");

                    content.setFont(font, 12);
                    content.newLineAtOffset(0, -40);
                    content.showText("Nomor Laporan: " + id);

                    content.newLineAtOffset(0, -25);
                    content.showText(
                        "Tanggal: " +
                        tanggalGenerate.format(
                            DateTimeFormatter.ofPattern(
                                "dd-MM-yyyy HH:mm"
                            )
                        )
                    );

                    content.newLineAtOffset(0, -40);
                    content.showText("DATA PENGAJUAN");

                    content.newLineAtOffset(0, -25);
                    content.showText(
                        "Data profil UMKM akan dihubungkan."
                    );

                    content.newLineAtOffset(0, -25);
                    content.showText(
                        "Data transaksi akan dihubungkan."
                    );

                    content.newLineAtOffset(0, -25);
                    content.showText(
                        "Data skor kelayakan akan dihubungkan."
                    );

                    content.endText();
                }

                document.save(output.toFile());
            }

            System.out.println(
                "Laporan PDF berhasil dibuat: " + filePath
            );

        } catch (IOException e) {
            throw new IllegalStateException(
                "Gagal membuat laporan PDF: " + e.getMessage(), e
            );
        }
    }

    // Getter
    public int getId() {
        return id;
    }

    public LocalDateTime getTanggalGenerate() {
        return tanggalGenerate;
    }

    public String getFilePath() {
        return filePath;
    }
}
