package com.ellize.incident.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        MyAuthentiticator authenticator = new MyAuthentiticator(this);
        return authenticator.getIBinder();
    }
}