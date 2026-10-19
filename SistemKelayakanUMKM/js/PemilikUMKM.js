import Pengguna from "./Pengguna.js";

class PemilikUMKM extends Pengguna {
  #profilUMKM;

  constructor(id, username, password) {
    super(id, username, password);
    this.#profilUMKM = null;
  }

  buatProfil(profil) {
    this.#profilUMKM = profil;
  }

  lihatProfil() {
    return this.#profilUMKM;
  }

  getDashboard() {
    const dashboard = super.getDashboard();

    if (typeof dashboard === "string") {
      return dashboard;
    }

    return {
      ...dashboard,
      role: "Pemilik UMKM",
      profilUMKM: this.#profilUMKM
    };
  }
}

export default PemilikUMKM;