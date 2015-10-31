package com.github.yoojia.next.events;

import android.util.Log;

import com.github.yoojia.next.lang.QuantumObject;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 1.0
 */
class EventsRouter {

    private static final String TAG = EventsRouter.class.getSimpleName();

    private final Schedulers mSchedulers;
    private final QuantumObject<OnErrorsListener> mOnErrorsListener;

    EventsRouter(Schedulers schedulers, QuantumObject<OnErrorsListener> onErrorsListener) {
        mSchedulers = schedulers;
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
                    } catch (Exception exception) {
                        final EventsException throwIt = EventsException.recatch(exception);
                        if (mOnErrorsListener.has()) {
                            mOnErrorsListener.get().onErrors(trigger.eventNames, throwIt);
                        }else{
                            throw throwIt;
                        }
                    }
                    return null;
                }
            };
            try{
                mSchedulers.submit(finalTask, trigger.runAsync());
                // Not sync task will throws exceptions
            }catch (Exception exception) {
                final EventsException throwIt = EventsException.recatch(exception);
                if (mOnErrorsListener.has()) {
                    mOnErrorsListener.get().onErrors(trigger.eventNames, throwIt);
                }else{
                    throw throwIt;
                }
            }
        }
    }

    public void close(){
        mSchedulers.close();
    }

}
