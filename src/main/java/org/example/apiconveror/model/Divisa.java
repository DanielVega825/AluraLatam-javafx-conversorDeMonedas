package org.example.apiconveror.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Divisa {
    private String id;
    private static int auxid = 0;

    private TipoDivisa base_code;
    private TipoDivisa paisConver;
    private Double monedaOrigen;
    private Double monedaFinal;
    private LocalDateTime fechaConsulta;


    private static List<Divisa> historial = new ArrayList<>();



    public Divisa(DivisaExchangeRate divisaExchangeRate) {
        this.id = String.valueOf(++auxid);
        this.base_code = TipoDivisa.valueOf(divisaExchangeRate.base_code());
        this.fechaConsulta = LocalDateTime.now();
    }


    //Getters And Setters

    public TipoDivisa getBase_code() {
        return base_code;
    }


    public LocalDateTime getFechaConsulta() {
        return fechaConsulta;
    }



    public Double getMonedaFinal() {
        return monedaFinal;
    }

    public void setMonedaFinal(Double monedaFinal) {
        this.monedaFinal = monedaFinal;
    }


    public Double getMonedaOrigen() {
        return monedaOrigen;
    }

    public void setMonedaOrigen(Double monedaOrigen) {
        this.monedaOrigen = monedaOrigen;
    }

    public TipoDivisa getPaisConver() {
        return paisConver;
    }

    public void setPaisConver(TipoDivisa paisConver) {
        this.paisConver = paisConver;
    }


    public String getId() {
        return id;
    }

    public  void setId(String id) {
        this.id = id;
    }

    //metodo estatico

    public static List<Divisa> addHistorial(Divisa divisa){
        historial.add(divisa);
        return historial;
    }

    public static List<Divisa> getHistorial() {

        return historial;
    }

    @Override
    public String toString() {
        return "Divisa{" +
                "id='" + id + '\'' +
                ", base_code=" + base_code +
                ", paisConver=" + paisConver +
                ", monedaOrigen=" + monedaOrigen +
                ", monedaFinal=" + monedaFinal +
                ", fechaConsulta=" + fechaConsulta +
                '}';
    }
}