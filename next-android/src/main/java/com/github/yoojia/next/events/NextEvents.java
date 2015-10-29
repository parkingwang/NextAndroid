package com.github.yoojia.next.events;

import android.util.Log;

import com.github.yoojia.next.lang.Filter;
import com.github.yoojia.next.lang.MethodsFinder;
import com.github.yoojia.next.lang.QuantumObject;

import java.lang.reflect.Method;
import java.util.List;

import static com.github.yoojia.next.events.Logger.timeLog;
import static com.github.yoojia.next.lang.Preconditions.notEmpty;
import static com.github.yoojia.next.lang.Preconditions.notNull;

/**
 * Next Events
 * @author YOOJIA.CHEN (yoojia.chen@gmail.com)
 * @version 2015-10-11
 */
public class NextEvents {

    protected final String mTag;

    private final EventsRouter mEventsRouter;
    private final Schedulers mEmitSchedulers;

    private final Reactor mReactor = new Reactor();
    private final QuantumObject<OnErrorsListener> mOnErrorsListener = new QuantumObject<>();

    public NextEvents(Schedulers work, Schedulers emit, String tag){
        mTag = tag;
        mEmitSchedulers = emit;
        mEventsRouter = new EventsRouter(work, mOnErrorsListener);
    }

    public NextEvents(Schedulers work, String tag) {
        this(work, Schedulers.single(), tag);
    }

    /**
     * 从指定对象中扫描添加了 @Subscribe 注解的事件订阅方法. 这些事件订阅方法将在发生匹配事件时被回调执行.
     * @param targetHost 指定被扫描的对象
     * @param filter 过滤扫描后的方法的接口;
     */
    public void register(Object targetHost, Filter<Method> filter) {
        final long startScan = System.nanoTime();
        final MethodsFinder finder = new MethodsFinder();
        finder.filter(new Filter<Method>() {
            @Override public boolean accept(Method method) {
                return method.isAnnotationPresent(Subscribe.class);
            }
        });
        final List<Method> annotated = finder.find(targetHost.getClass());
        timeLog(mTag, "EVENTS-SCAN", startScan);
        final long startRegister = System.nanoTime();
        final AnnotatedRegister register = new AnnotatedRegister(targetHost, new InvokableAccess<MethodInvokable>() {
            @Override public void access(MethodInvokable subscriber) {
                mReactor.add(subscriber);
            }
        });
        register.batch(annotated, filter);
        timeLog(mTag, "EVENTS-REGISTER", startRegister);
        if (annotated.isEmpty()){
            Log.e(mTag, "Empty Handlers(with @Subscribe) !");
            Warning.show(mTag);
        }
    }

    /**
     * 指定事件订阅接口及其匹配的事件. 当发生匹配事件时,接口被回调执行.
     * @param subscriber 指定的回调接口
     * @param async 是否异步执行
     * @param events 事件名及类型对, 格式如: ('my-event-1', MyEvent1.class, 'my-event-2', MyEvent2.class)
     */
    public void register(Subscriber subscriber, boolean async, Object...events) {
        final IllegalArgumentException err = new IllegalArgumentException(
                "Events must be String-Class<?> pairs. e.g: ('my-event-1', MyEvent1.class, 'my-event-2', MyEvent2.class) ");
        if (events.length == 0 || events.length % 2 != 0) {
            throw err;
        }
        final Meta[] meta = new Meta[events.length/2];
        for (int i = 0; i < events.length / 2; i++) {
            final Object event = events[i*2];
            final Object type = events[i*2 + 1];
            if (!(event instanceof String) || !(type instanceof Class<?>)) {
                throw err;
            }
            meta[i] = new Meta((String)event, (Class<?>)type);
        }
        mReactor.add(new SubscriberInvokable(meta, subscriber, async));
    }

    /**
     * 反注册将指定对象的全部事件订阅方法
     * @param targetHost 指定对象
     */
    public void unregister(Object targetHost) {
        notNull(targetHost, "Target host host must not be null !");
        mReactor.remove(targetHost);
    }

    /**
     * 反注册指定事件订阅接口
     * @param subscriber 指定事件订阅接口
     */
    public void unregister(Subscriber subscriber) {
        notNull(subscriber, "Subscriber host must not be null !");
        mReactor.remove(subscriber);
    }

    /**
     * 提交事件并立即（阻塞）执行
     * @param eventObject 事件对象
     * @param eventName 事件名
     * @param lenient 是否允许事件没有目标. 如果为false, 当事件没有目标接受时,会抛出异常.
     */
    public void emitImmediately(Object eventObject, String eventName, boolean lenient) {
        notNull(eventObject, "Event object must not be null !");
        notEmpty(eventName, "Event name must not be null !");
        if (EventsFlags.PROCESSING) {
            Log.d(mTag, "- Emit EVENT: NAME=" + eventName + ", OBJECT=" + eventObject + ", LENIENT=" + lenient);
        }
        final long emitStart = System.nanoTime();
        final List<FuelTarget.Target> targets = mReactor.emit(eventName, eventObject, lenient);
        timeLog(mTag, "EVENTS-EMIT", emitStart);
        final long dispatchStart = System.nanoTime();
        mEventsRouter.dispatch(targets);
        timeLog(mTag, "EVENTS-DISPATCH", dispatchStart);
    }

    /**
     * 提交事件, 异步地执行
     * @param eventObject 事件对象
     * @param eventName 事件名
     * @param lenient 是否允许事件没有目标
     */
    public void emit(final Object eventObject, final String eventName, final boolean lenient) {
        mEmitSchedulers.submit(new Runnable() {
            @Override
            public void run() {
                emitImmediately(eventObject, eventName, lenient);
            }
        }, true);
    }

    /**
     * 销毁 NextEvents
     */
    public void destroy(){
        mEmitSchedulers.close();
        mEventsRouter.shutdown();
    }

    /**
     * 设置执行目标方法发生的错误的处理回调接口
     * @param listener 处理回调接口
     */
    public void setOnErrorsListener(OnErrorsListener listener) {
        mOnErrorsListener.set(listener);
    }

}