package com.github.yoojia.next.events;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.github.yoojia.next.lang.QuantumObject;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static com.github.yoojia.next.events.Logger.timeLog;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 1.0
 */
class Dispatcher {

    private static final String TAG = Dispatcher.class.getSimpleName();

    private final Handler mMainThread = new Handler(Looper.getMainLooper());
    private final ExecutorService mThreads;
    private final QuantumObject<OnErrorsListener> mOnErrorsListener;

    Dispatcher(ExecutorService threads, QuantumObject<OnErrorsListener> onErrorsListener) {
        mThreads = threads;
        mOnErrorsListener = onErrorsListener;
    }

    public void dispatch(List<Target.Trigger> triggers){
        for (final Target.Trigger trigger : triggers){
            final Callable<Void> finalTask = new Callable<Void>() {
                @Override public Void call() throws Exception {
                    if (EventsFlags.PROCESSING) {
                        Log.d(TAG, "- Target run on thread.id= " + Thread.currentThread().getId());
                    }
                    try {
                        trigger.invoke();
                    } catch (Exception error) {
                        if (mOnErrorsListener.watch()) {
                            mOnErrorsListener.get().onErrors(error);
                        }else{
                            throw error;
                        }
                    }
                    return null;
                }
            };
            if ( ! trigger.async) {
                submitMainThread(finalTask);
            }else{
                submitThreads(finalTask);
            }
        }
    }

    public void shutdown(){
        mThreads.shutdown();
    }

    private void submitThreads(Callable<Void> task) {
        mThreads.submit(task);
    }

    private void submitMainThread(final Callable<Void> task) {
        final long submitStart = System.nanoTime();
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                try {
                    timeLog(TAG, "WAIT-FOR-MAIN-THREAD", submitStart);
                    task.call();
                } catch (Exception error) {
                    throw new IllegalStateException(error);
                }
            }
        });
    }
}
