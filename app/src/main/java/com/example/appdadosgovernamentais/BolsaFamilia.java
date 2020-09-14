package com.example.appdadosgovernamentais;

import java.util.ArrayList;
import java.util.List;

public class BolsaFamilia {
    private String nomeMunicipio;
    private String estado;
    private int beneficiarios;
    private double totalPago;

    public BolsaFamilia() {}

    public BolsaFamilia(String nomeMunicipio, String estado, int beneficiarios, double totalPago) {
        this.nomeMunicipio = nomeMunicipio;
        this.estado = estado;
        this.beneficiarios = beneficiarios;
        this.totalPago = totalPago;
    }

    public String getNomeMunicipio() {
        return nomeMunicipio;
    }

    public void setNomeMunicipio(String nomeMunicipio) {
        this.nomeMunicipio = nomeMunicipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getBeneficiarios() {
        return beneficiarios;
    }

    public void setBeneficiarios(int beneficiarios) {
        this.beneficiarios = beneficiarios;
    }

    public double getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(double totalPago) {
        this.totalPago = totalPago;
    }
}
