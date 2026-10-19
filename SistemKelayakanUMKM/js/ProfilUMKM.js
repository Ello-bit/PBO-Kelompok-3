class ProfilUMKM {
  #idUsaha;
  #namaUsaha;
  #jenisUsaha;
  #alamat;
  #transaksi;

  constructor(idUsaha, namaUsaha, jenisUsaha, alamat) {
    this.#idUsaha = idUsaha;
    this.#namaUsaha = namaUsaha;
    this.#jenisUsaha = jenisUsaha;
    this.#alamat = alamat;
    this.#transaksi = [];
  }

  tambahTransaksi(transaksi) {
    this.#transaksi.push(transaksi);
  }
}

export default ProfilUMKM;