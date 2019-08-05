package com.edhuaranga.ortodontech;

import android.app.Application;

import com.edhuaranga.ortodontech.model.User;

public class OrtodentechApplication extends Application {
    private User usuario;

    public User  getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario){
        this.usuario = usuario;
    }

}
