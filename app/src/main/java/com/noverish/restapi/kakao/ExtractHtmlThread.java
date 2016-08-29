package com.noverish.restapi.kakao;

/**
 * Created by Noverish on 2016-08-28.
 */
public class ExtractHtmlThread extends Thread {

    private CustomListener listener;
    private static ExtractHtmlThread thread;
    private android.os.Handler handler;

    private ExtractHtmlThread(android.os.Handler handler, CustomListener listenerParam) {
        this.listener = listenerParam;
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();

        try {
            Thread.sleep(3000);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.listen();
                }
            });
        } catch (Exception ex) {

        }
    }

    public static void post(android.os.Handler handler, CustomListener listenerParam) {
        if(thread != null) {
            thread.interrupt();
        }
        thread = new ExtractHtmlThread(handler, listenerParam);
        thread.start();
    }

    public interface CustomListener {
        void listen();
    }
}
