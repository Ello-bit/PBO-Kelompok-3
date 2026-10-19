const JenisKategori = Object.freeze({
  MODAL: "MODAL",
  OPERASIONAL: "OPERASIONAL",
  PENJUALAN: "PENJUALAN"
});

class KategoriTransaksi {
  #id;
  #namaKategori;
  #jenis;

  constructor(id, namaKategori, jenis) {
    if (!Object.values(JenisKategori).includes(jenis)) {
      throw new Error("Jenis kategori tidak valid.");
    }

    this.#id = id;
    this.#namaKategori = namaKategori;
    this.#jenis = jenis;
  }

  ubahNama(namaBaru) {
    this.#namaKategori = namaBaru;
  }

  tampilkanData() {
    return {
      id: this.#id,
      namaKategori: this.#namaKategori,
      jenis: this.#jenis
    };
  }
}

export { JenisKategori };
export default KategoriTransaksi;