package com.atividade.rickandmortyservices.modelo;

import org.json.JSONException;
import org.json.JSONObject;

public class Personagem {
    private String nome;
    private String status;
    private String especie;
    private String image;

    public Personagem (JSONObject json) throws JSONException{
        this.nome = json.getJSONArray("results").getJSONObject(0).getString("name");
        this.status = json.getJSONArray("results").getJSONObject(0).getString("status");
        this.especie = json.getJSONArray("results").getJSONObject(0).getString("species");
        this.image = json.getJSONArray("results").getJSONObject(0).getString("image");

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
