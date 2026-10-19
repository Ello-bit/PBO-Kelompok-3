class Transaksi {
  #id;
  #nominal;
  #tanggal;
  #catatan;

  constructor(id, nominal, tanggal, catatan) {
    if (nominal < 0) {
      throw new Error("Nominal transaksi tidak boleh negatif.");
    }

    this.#id = id;
    this.#nominal = nominal;
    this.#tanggal = tanggal;
    this.#catatan = catatan;
  }

  hitungDampakSaldo(jenis) {
    if (jenis === "pemasukan") {
      return this.#nominal;
    }

    if (jenis === "pengeluaran") {
      return -this.#nominal;
    }

    throw new Error(
      "Jenis transaksi harus pemasukan atau pengeluaran."
    );
  }
}

export default Transaksi;