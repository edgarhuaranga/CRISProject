package com.edhuaranga.crisproject;

import android.app.Application;

import com.edhuaranga.crisproject.model.User;

public class OrtodentechApplication extends Application {
    private User usuario;

    public User  getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario){
        this.usuario = usuario;
    }

}
