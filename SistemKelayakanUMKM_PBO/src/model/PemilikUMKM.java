package model;

public class PemilikUMKM extends Pengguna {
    private ProfilUMKM profilUMKM;

    public PemilikUMKM (int id, String username, String password, ProfilUMKM profilUMKM){
        super(id, username, password);
        this.profilUMKM = profilUMKM;
    }

    @Override 
    public void registrasi(){
        System.out.println("Registrasi Pemilik UMKM berhasil !!!");
    }

    @Override 
    public void getDashboard(){
        System.out.println("=== DASHBOARD PEMILIK UMKM ===");

        if (profilUMKM != null){
            System.out.println("Nama Usaha :" +profilUMKM.getNamaUsaha());
        }
    }

    public ProfilUMKM getProfilUMKM(){
        return profilUMKM;
    }

    public void setProfilUMKM(ProfilUMKM profilUMKM){
        this.profilUMKM = profilUMKM;
    }
}
