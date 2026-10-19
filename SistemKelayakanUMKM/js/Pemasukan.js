import Transaksi from "./Transaksi.js";

class Pemasukan extends Transaksi {
  constructor(id, nominal, tanggal, catatan) {
    super(id, nominal, tanggal, catatan);
  }

  hitungDampakSaldo() {
    return super.hitungDampakSaldo("pemasukan");
  }
}

export default Pemasukan;