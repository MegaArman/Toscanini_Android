package com.studionobume.musicalgoogle.Networking;

import android.app.Application;

import com.studionobume.musicalgoogle.R;

import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;


/**
 * Created by Togame on 5/1/2017.
 */

public class SocketIO extends Application {
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(getString(R.string.URL));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() { return mSocket;}
}
