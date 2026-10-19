
import Transaksi from "./Transaksi.js";

class Pengeluaran extends Transaksi {
  constructor(id, nominal, tanggal, catatan) {
    super(id, nominal, tanggal, catatan);
  }

  hitungDampakSaldo() {
    return super.hitungDampakSaldo("pengeluaran");
  }
}

export default Pengeluaran;
