class Pengguna {
    #id;
    #username;
    #password;
    #isLogin;

  constructor(id, username, password, isLogin){
    this.#id = id;
    this.#username = username;
    this.#password = password;
    this.#isLogin = false;
  }

  registrasi(){
    if (!this.#username || !this.#password){
        return "Username dan Password wajib di isi !!!";
    }

    return "Registrasi Berhasil !!!";
  }

  login (username, password){
    if (username === this.#username && password == this.#password){
        this.#isLogin = true;
        return "Login Berhasil !!!";
    }

    return "Username dan Password salah !!!";
  }

  logout(){
    if (!this.#isLogin){
        return "Anda belum login";
    }

    this.#isLogin = false;
    return "Logout berhasil";
  }

  getDashboard(){
    if (!this.#isLogin){
        return "Silahkan login terlebih dahulu";
    }
    return {id: this.#id, username: this.#username, pesan: `Selamat datang, ${this.#username}!`};
  }
}

export default Pengguna;