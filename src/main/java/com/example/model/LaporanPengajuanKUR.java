package com.example.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

public class LaporanPengajuanKUR implements Exportable {
    private final String id, jenisUsaha, alamat, filePath;
    private String namaUsaha;
    private final LocalDateTime tanggalGenerate;
    private final double totalPemasukan, totalPengeluaran, labaBersih;
    private final int skor, level;
    private final List<TransaksiLaporan> transaksi;

    // DATA VERIFIKASI (dipakai Admin dan tabel laporan_kur)
    private int idLaporan;
    private int idUsaha;
    private int idSkor;
    private String tanggalPembuatan;
    private String lokasiFilePDF;
    private String statusVerifikasi = "MENUNGGU";
    private String alasanPenolakan = "-";

    // Constructor untuk laporan yang dibaca dari database (tabel laporan_kur).
    // Field khusus pembuatan PDF diisi nilai kosong karena tidak disimpan di tabel.
    public LaporanPengajuanKUR(int idLaporan, int idUsaha, int idSkor, String tanggalPembuatan,
            String lokasiFilePDF, String statusVerifikasi, String alasanPenolakan) {
        this.id = String.valueOf(idLaporan);
        this.namaUsaha = "";
        this.jenisUsaha = "";
        this.alamat = "";
        this.totalPemasukan = 0;
        this.totalPengeluaran = 0;
        this.labaBersih = 0;
        this.skor = 0;
        this.level = 0;
        this.transaksi = List.of();
        this.filePath = lokasiFilePDF;
        this.tanggalGenerate = LocalDateTime.now();
        this.idLaporan = idLaporan;
        this.idUsaha = idUsaha;
        this.idSkor = idSkor;
        this.tanggalPembuatan = tanggalPembuatan;
        this.lokasiFilePDF = lokasiFilePDF;
        this.statusVerifikasi = statusVerifikasi;
        this.alasanPenolakan = alasanPenolakan;
    }

    public LaporanPengajuanKUR(String id, String namaUsaha, String jenisUsaha, String alamat,
            double totalPemasukan, double totalPengeluaran, int skor, int level,
            List<TransaksiLaporan> transaksi, String filePath) {
        this(id, namaUsaha, jenisUsaha, alamat, totalPemasukan, totalPengeluaran,
                totalPemasukan - totalPengeluaran, skor, level, transaksi, filePath);
    }

    public LaporanPengajuanKUR(String id, String namaUsaha, String jenisUsaha, String alamat,
            double totalPemasukan, double totalPengeluaran, double labaBersih,
            int skor, int level, List<TransaksiLaporan> transaksi, String filePath) {
        this.id = id;
        this.namaUsaha = namaUsaha;
        this.jenisUsaha = jenisUsaha;
        this.alamat = alamat;
        this.totalPemasukan = totalPemasukan;
        this.totalPengeluaran = totalPengeluaran;
        this.labaBersih = labaBersih;
        this.skor = skor;
        this.level = level;
        this.transaksi = List.copyOf(transaksi);
        this.filePath = filePath;
        this.tanggalGenerate = LocalDateTime.now();
    }

    @Override
    public void exportToPDF() {
        Path output = Path.of(filePath);
        try {
            if (output.getParent() != null) Files.createDirectories(output.getParent());
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                PDType1Font regular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
                PDType1Font bold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

                PDPageContentStream content = new PDPageContentStream(document, page);
                float y = 790;

                // Header
                drawText(content, bold, 20, 50, y, "KURTERA");
                drawText(content, regular, 9, 50, y - 17, "SISTEM INFORMASI PENGELOLAAN UMKM");
                drawText(content, bold, 16, 50, y - 52, "LAPORAN PENGAJUAN KUR");
                drawText(content, regular, 9, 430, y - 48, "No. " + id);
                drawLine(content, 50, y - 65, 545, y - 65);
                y -= 92;

                // Metadata
                drawText(content, regular, 9, 50, y, "Tanggal pembuatan");
                drawText(content, bold, 9, 170, y, tanggalGenerate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                y -= 30;

                // Identitas usaha
                y = sectionTitle(content, bold, y, "1. IDENTITAS USAHA");
                y = infoRow(content, regular, y, "Nama usaha", namaUsaha);
                y = infoRow(content, regular, y, "Jenis usaha", jenisUsaha);
                y = infoRow(content, regular, y, "Alamat", alamat);
                y -= 12;

                // Ringkasan transaksi
                y = sectionTitle(content, bold, y, "2. RINGKASAN TRANSAKSI");
                y = infoRow(content, regular, y, "Total pemasukan", rupiah(totalPemasukan));
                y = infoRow(content, regular, y, "Total pengeluaran", rupiah(totalPengeluaran));
                y = infoRow(content, bold, y, "Laba bersih", rupiah(labaBersih));
                y -= 12;

                // Riwayat transaksi
                y = sectionTitle(content, bold, y, "3. RIWAYAT TRANSAKSI");
                drawText(content, bold, 8, 55, y, "TANGGAL");
                drawText(content, bold, 8, 145, y, "JENIS");
                drawText(content, bold, 8, 250, y, "KETERANGAN");
                drawText(content, bold, 8, 465, y, "NOMINAL");
                y -= 9;
                drawLine(content, 50, y, 545, y);
                y -= 17;

                for (TransaksiLaporan item : transaksi) {
                    if (y < 95) {
                        content.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        content = new PDPageContentStream(document, page);
                        y = 785;
                        drawText(content, bold, 12, 50, y, "RIWAYAT TRANSAKSI (LANJUTAN)");
                        y -= 28;
                        drawText(content, bold, 8, 55, y, "TANGGAL");
                        drawText(content, bold, 8, 145, y, "JENIS");
                        drawText(content, bold, 8, 250, y, "KETERANGAN");
                        drawText(content, bold, 8, 465, y, "NOMINAL");
                        y -= 9;
                        drawLine(content, 50, y, 545, y);
                        y -= 17;
                    }
                    drawText(content, regular, 8, 55, y, safe(item.getTanggal(), 14));
                    drawText(content, regular, 8, 145, y, safe(item.getJenis(), 16));
                    drawText(content, regular, 8, 250, y, safe(item.getKeterangan(), 38));
                    drawTextRight(content, regular, 8, 535, y, rupiah(item.getNominal()));
                    y -= 19;
                }
                y -= 15;

                if (y < 115) {
                    content.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    y = 785;
                }
                y = sectionTitle(content, bold, y, "4. HASIL PENILAIAN KELAYAKAN");
                y = infoRow(content, regular, y, "Skor kelayakan", skor < 0 ? "Belum tersedia" : String.valueOf(skor));
                y = infoRow(content, regular, y, "Level kelayakan", level < 0 ? "Belum tersedia" : "Level " + level);

                // Footer on the final page
                drawLine(content, 50, 55, 545, 55);
                drawText(content, regular, 8, 50, 40, "Dokumen dibuat secara otomatis oleh sistem KURTERA.");
                drawTextRight(content, regular, 8, 545, 40, id);
                content.close();

                document.save(output.toFile());
            }
        } catch (IOException e) {
            throw new IllegalStateException("Gagal membuat laporan PDF: " + e.getMessage(), e);
        }
    }

    private float sectionTitle(PDPageContentStream c, PDType1Font font, float y, String title) throws IOException {
        drawText(c, font, 11, 50, y, title);
        drawLine(c, 50, y - 6, 545, y - 6);
        return y - 25;
    }

    private float infoRow(PDPageContentStream c, PDType1Font font, float y, String label, String value) throws IOException {
        drawText(c, font, 9, 60, y, label);
        drawText(c, font, 9, 205, y, ":");
        drawText(c, font, 9, 220, y, safe(value, 65));
        return y - 17;
    }

    private void drawText(PDPageContentStream c, PDType1Font font, int size, float x, float y, String value) throws IOException {
        c.beginText();
        c.setFont(font, size);
        c.newLineAtOffset(x, y);
        c.showText(safe(value, 100));
        c.endText();
    }

    private void drawTextRight(PDPageContentStream c, PDType1Font font, int size, float rightX, float y, String value) throws IOException {
        String safe = safe(value, 100);
        float width = font.getStringWidth(safe) / 1000 * size;
        drawText(c, font, size, rightX - width, y, safe);
    }

    private void drawLine(PDPageContentStream c, float x1, float y1, float x2, float y2) throws IOException {
        c.moveTo(x1, y1);
        c.lineTo(x2, y2);
        c.stroke();
    }

    private String safe(String value, int max) {
        if (value == null) return "-";
        String cleaned = value.replace("\n", " ").replace("\r", " ")
                .replaceAll("[^\\x20-\\x7E]", "?").trim();
        return cleaned.length() > max ? cleaned.substring(0, max - 3) + "..." : cleaned;
    }

    private String rupiah(double value) {
        NumberFormat f = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        f.setMaximumFractionDigits(0);
        return f.format(value).replaceAll("[^\\x20-\\x7E]", "");
    }

    public String getId() { return id; }
    public LocalDateTime getTanggalGenerate() { return tanggalGenerate; }
    public String getFilePath() { return filePath; }

    // GETTER & SETTER DATA VERIFIKASI
    public int getIdLaporan() { return idLaporan; }
    public int getIdUsaha() { return idUsaha; }
    public int getIdSkor() { return idSkor; }
    public String getTanggalPembuatan() { return tanggalPembuatan; }
    public String getLokasiFilePDF() { return lokasiFilePDF; }
    public String getStatusVerifikasi() { return statusVerifikasi; }
    public String getAlasanPenolakan() { return alasanPenolakan; }
    public String getNamaUsaha() { return namaUsaha; }

    public void setIdUsaha(int idUsaha) { this.idUsaha = idUsaha; }
    public void setStatusVerifikasi(String statusVerifikasi) { this.statusVerifikasi = statusVerifikasi; }
    public void setAlasanPenolakan(String alasanPenolakan) { this.alasanPenolakan = alasanPenolakan; }
    public void setNamaUsaha(String namaUsaha) { this.namaUsaha = namaUsaha; }

    public static class TransaksiLaporan {
        private final String tanggal, keterangan, jenis;
        private final double nominal;
        public TransaksiLaporan(String tanggal, String keterangan, String jenis, double nominal) {
            this.tanggal = tanggal;
            this.keterangan = keterangan;
            this.jenis = jenis;
            this.nominal = nominal;
        }
        public String getTanggal() { return tanggal; }
        public String getKeterangan() { return keterangan; }
        public String getJenis() { return jenis; }
        public double getNominal() { return nominal; }
        public String getNominalFormat() { return rupiah(nominal); }
        private String rupiah(double n) {
            NumberFormat f = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            f.setMaximumFractionDigits(0);
            return f.format(n);
        }
    }
}
