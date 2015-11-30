package com.wayne.gestureverify;

public interface GestureCallback {

    void onComplete(String cipherCode);

    void onVerify(boolean state);
}
