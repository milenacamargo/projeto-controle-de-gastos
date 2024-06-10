package com.financontrol.carteira.model.entity;


public enum Tipo {
    RECEITA(1, "RECEITA"),
    DESPESA(0, "DESPESA");

    private final int id;
    private final String nome;

    Tipo(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
