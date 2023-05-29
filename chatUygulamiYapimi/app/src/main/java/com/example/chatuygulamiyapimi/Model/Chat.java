package com.example.chatuygulamiyapimi.Model;

public class Chat {
    private String mesajIçerigi,gonderen,alici,mesajTipi,docId;

    public Chat(String mesajIçerigi,String gonderen,String alici,String mesajTipi,String docId){

        this.mesajIçerigi=mesajIçerigi;
        this.gonderen=gonderen;
        this.alici=alici;
        this.mesajTipi=mesajTipi;
        this.docId=docId;


    }
    public Chat(){


    }

    public String getMesajIçerigi() {
        return mesajIçerigi;
    }

    public void setMesajIçerigi(String mesajIçerigi) {
        this.mesajIçerigi = mesajIçerigi;
    }

    public String getGonderen() {
        return gonderen;
    }

    public void setGonderen(String gonderen) {
        this.gonderen = gonderen;
    }

    public String getAlici() {
        return alici;
    }

    public void setAlici(String alici) {
        this.alici = alici;
    }

    public String getMesajTipi() {
        return mesajTipi;
    }

    public void setMesajTipi(String mesajTipi) {
        this.mesajTipi = mesajTipi;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
