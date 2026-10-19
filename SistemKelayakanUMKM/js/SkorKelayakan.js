import Scoreable from "./Scoreable.js";

class SkorKelayakan extends Scoreable {
  #id;
  #string;
  #nilaiSkor;
  #level;

  constructor(id, string, nilaiSkor = 0, level = "") {
    super();
    this.#id = id;
    this.#string = string;
    this.#nilaiSkor = nilaiSkor;
    this.#level = level;
  }

  hitungSkor() {
    if (this.#nilaiSkor < 0 || this.#nilaiSkor > 100) {
      throw new Error("Nilai skor harus antara 0 sampai 100.");
    }

    //anggepan aja
    if (this.#nilaiSkor >= 80) {
      this.#level = "Tinggi";
    } else if (this.#nilaiSkor >= 60) {
      this.#level = "Sedang";
    } else {
      this.#level = "Rendah";
    }

    return this.#nilaiSkor;
  }
}

export default SkorKelayakan;