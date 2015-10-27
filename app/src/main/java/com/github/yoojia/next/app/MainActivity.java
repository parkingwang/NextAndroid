package com.github.yoojia.next.app;

import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.github.yoojia.next.clicks.ClickEvent;
import com.github.yoojia.next.clicks.EmitClick;
import com.github.yoojia.next.clicks.NextClickProxy;
import com.github.yoojia.next.events.Event;
import com.github.yoojia.next.events.EventsFlags;
import com.github.yoojia.next.events.Subscribe;
import com.github.yoojia.next.flux.Action;
import com.github.yoojia.next.flux.Dispatcher;
import com.github.yoojia.next.views.AutoView;
import com.github.yoojia.next.views.NextAutoView;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @AutoView(R.id.helo)
    private TextView mHelo;

    @EmitClick(event = "click")
    @AutoView(R.id.button)
    private Button mButton;

    private final Dispatcher mDispatcher = new Dispatcher();
    private TestStore mStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventsFlags.enabledPerformanceLog(true);
//        EventsFlags.enabledProcessingLog(true);

        // Inject views
        NextAutoView.use(this).inject(this);
        // Click proxy
        NextClickProxy.bind(this);
        // Flux
        mStore = new TestStore(mDispatcher, this);
        mStore.register();
        mDispatcher.register(this);

    }

    @Subscribe
    private void onClick(@Event("click") ClickEvent<Button> evt) {
        long emitStart = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            long genData = System.currentTimeMillis();
            // Emit action, TestStore will handle this request
            mDispatcher.emit(TestActions.newReqClick(genData));
        }
        long diff = System.currentTimeMillis() - emitStart;
        Log.d(TAG, "- Emit 1000 event, takes: " + diff + "ms");
    }

//    @Subscribe
    private void onData(@Event(TestActions.NOTIFY_CLICK) Action evt) {
        final long data = evt.data.getLong("data");
//        Log.d(TAG, "- Received data: " + data);
        mHelo.setText("Received data: " + data);
    }

    @Subscribe(async = true)
    private void onData1(@Event(TestActions.NOTIFY_CLICK) Action evt) {
    }

    @Subscribe(async = true)
    private void onData2(@Event(TestActions.NOTIFY_CLICK) Action evt) {
    }

    @Subscribe(async = true)
    private void onData3(@Event(TestActions.NOTIFY_CLICK) Action evt) {
    }

    @Subscribe(async = true)
    private void onData4(@Event(TestActions.NOTIFY_CLICK) Action evt) {
    }

    @Subscribe(async = true)
    private void onData5(@Event(TestActions.NOTIFY_CLICK) Action evt) {
    }

    @Subscribe(async = true)
    private void onData6(@Event(TestActions.NOTIFY_CLICK) Action evt) {
    }

    @Subscribe(async = true)
    private void onData7(@Event(TestActions.NOTIFY_CLICK) Action evt) {
    }

    @Subscribe(async = true)
    private void onData8(@Event(TestActions.NOTIFY_CLICK) Action evt) {
    }

    @Subscribe(async = true)
    private void onData9(@Event(TestActions.NOTIFY_CLICK) Action evt) {
    }

    @Subscribe(async = true)
    private void onData10(@Event(TestActions.NOTIFY_CLICK) Action evt) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStore.unregister();
        mDispatcher.unregister(this);
        mDispatcher.destroy();
    }
}
