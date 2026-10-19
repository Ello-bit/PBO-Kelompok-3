
class LaporanPengajuanKUR {
  #id;
  #tanggalGenerate;
  #filepath;

  constructor(id, tanggalGenerate, filepath) {
    this.#id = id;
    this.#tanggalGenerate = tanggalGenerate;
    this.#filepath = filepath;
  }

  exportToPDF() {
    /*const konten = `
      <h1>Laporan Pengajuan KUR</h1>
      <p>ID Laporan: ${this.#id}</p>
      <p>Tanggal Generate: ${this.#tanggalGenerate}</p>
    `;

    const jendela = window.open("", "_blank");

    if (!jendela) {
      throw new Error("Izinkan pop-up browser untuk mencetak PDF.");
    }

    jendela.document.write(`
      <!DOCTYPE html>
      <html lang="id">
      <head>
        <meta charset="UTF-8">
        <title>${this.#filepath}</title>
      </head>
      <body>
        ${konten}
        <script>
          window.onload = () => window.print();
        <\/script>
      </body>
      </html>
    `);

    jendela.document.close();*/
  }
}

export default LaporanPengajuanKUR;
