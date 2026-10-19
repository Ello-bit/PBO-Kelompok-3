import Pengguna from "./Pengguna.js";

class Admin extends Pengguna {
  constructor(id, username, password) {
    super(id, nama, password);
    this.role = "Admin";
  }

  kelolaPengguna(pengguna, aksi) {
    if (!pengguna) {
      return "Data pengguna tidak ditemukan.";
    }

    switch (aksi) {
      case "lihat":
        return "Data pengguna berhasil diakses.";

      case "hapus":
        return "Penghapusan pengguna perlu diproses oleh sistem.";

      default:
        return "Aksi pengelolaan pengguna tidak valid.";
    }
  }

  VerifikasiLaporan(laporan, status) {
    if (!laporan) {
      return "Laporan tidak ditemukan.";
    }

    const statusValid = [
      "Menunggu",
      "Diproses",
      "Disetujui",
      "Ditolak"
    ];

    if (!statusValid.includes(status)) {
      return "Status laporan tidak valid.";
    }

    laporan.ubahStatus(status);

    return `Laporan ${laporan.id} berhasil diubah menjadi ${status}.`;
  }
}

export default Admin;