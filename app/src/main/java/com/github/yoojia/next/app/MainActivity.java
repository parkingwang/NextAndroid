package com.github.yoojia.next.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.github.yoojia.next.clicks.Click;
import com.github.yoojia.next.clicks.ClickEvent;
import com.github.yoojia.next.clicks.ClickHandler;
import com.github.yoojia.next.clicks.NextClickProxy;
import com.github.yoojia.next.events.Runs;
import com.github.yoojia.next.events.Subscribe;
import com.github.yoojia.next.flux.Action;
import com.github.yoojia.next.flux.Dispatcher;
import com.github.yoojia.next.views.BindView;
import com.github.yoojia.next.views.NextBindView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.helo)
    private TextView mHelo;

    @Click("click")
    @BindView(R.id.button)
    private Button mButton;

    private final Dispatcher mDispatcher = new Dispatcher();

    private TestStore mStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Inject views
        NextBindView.use(this).inject(this);
        // Click proxy
        NextClickProxy.bind(this);
        // Flux
        mStore = new TestStore(mDispatcher);
        mDispatcher.register(mStore);
        mDispatcher.register(this);
    }

    @Subscribe(on = ActionTypes.CHANGED_MESSAGES, run = Runs.ON_CALLER)
    public void onChanged(Action action) {
        Log.d(TAG, "- Emit event, finish");
    }

    @ClickHandler(on = "click")
    private void onClick(ClickEvent<Button> evt) {
        long emitStart = System.currentTimeMillis();
        long longData = System.currentTimeMillis();
        mDispatcher.emit(ActionTypes.createRawMessage(new LongMessage(longData)));
        long diff = System.currentTimeMillis() - emitStart;
        Log.d(TAG, "- Emit an event, takes: " + diff + "ms");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDispatcher.unregister(mStore);
        mDispatcher.unregister(this);
    }

}
