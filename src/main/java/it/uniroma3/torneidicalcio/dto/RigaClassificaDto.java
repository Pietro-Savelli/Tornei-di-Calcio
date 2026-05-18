package it.uniroma3.torneidicalcio.dto;

public class RigaClassificaDto {
    private Long squadraId;
    private String nomeSquadra;
    private int punti;
    private int partiteGiocate;
    private int vittorie;
    private int pareggi;
    private int sconfitte;
    private int golFatti;
    private int golSubiti;

    public RigaClassificaDto(Long squadraId, String nomeSquadra) {
        this.squadraId = squadraId;
        this.nomeSquadra = nomeSquadra;
        this.punti = 0;
        this.partiteGiocate = 0;
        this.vittorie = 0;
        this.pareggi = 0;
        this.sconfitte = 0;
        this.golFatti = 0;
        this.golSubiti = 0;
    }

    public void aggiungiPartita(int golFattiPartita, int golSubitiPartita) {
        this.partiteGiocate++;
        this.golFatti += golFattiPartita;
        this.golSubiti += golSubitiPartita;

        if (golFattiPartita > golSubitiPartita) {
            this.vittorie++;
            this.punti += 3;
        } else if (golFattiPartita == golSubitiPartita) {
            this.pareggi++;
            this.punti += 1;
        } else {
            this.sconfitte++;
        }
    }

    public Long getSquadraId() {
        return squadraId;
    }

    public void setSquadraId(Long squadraId) {
        this.squadraId = squadraId;
    }

    public String getNomeSquadra() {
        return nomeSquadra;
    }

    public void setNomeSquadra(String nomeSquadra) {
        this.nomeSquadra = nomeSquadra;
    }

    public int getPunti() {
        return punti;
    }

    public void setPunti(int punti) {
        this.punti = punti;
    }

    public int getPartiteGiocate() {
        return partiteGiocate;
    }

    public void setPartiteGiocate(int partiteGiocate) {
        this.partiteGiocate = partiteGiocate;
    }

    public int getVittorie() {
        return vittorie;
    }

    public void setVittorie(int vittorie) {
        this.vittorie = vittorie;
    }

    public int getPareggi() {
        return pareggi;
    }

    public void setPareggi(int pareggi) {
        this.pareggi = pareggi;
    }

    public int getSconfitte() {
        return sconfitte;
    }

    public void setSconfitte(int sconfitte) {
        this.sconfitte = sconfitte;
    }

    public int getGolFatti() {
        return golFatti;
    }

    public void setGolFatti(int golFatti) {
        this.golFatti = golFatti;
    }

    public int getGolSubiti() {
        return golSubiti;
    }

    public void setGolSubiti(int golSubiti) {
        this.golSubiti = golSubiti;
    }
}