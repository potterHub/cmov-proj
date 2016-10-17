package org.feup.apm.coloranim;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends Activity {
  final int NTHREADS = 3;
  int colors[] = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.GRAY, Color.BLACK};
  int ids[] = {R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6, R.id.imageView7, R.id.imageView8, R.id.imageView9};
  GradientDrawable[] circles = new GradientDrawable[ids.length];
  colorThr[] thrs = new colorThr[NTHREADS];
  boolean isAnimOn = false;

  private class AnimHandler extends Handler {
    @Override
    public void handleMessage(Message m) {
      int color = m.getData().getInt("color");
      int circle = m.getData().getInt("circle");
      circles[circle].setColor(colors[color]);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    for (int k=0; k<ids.length; k++)
     circles[k] = (GradientDrawable)((ImageView)findViewById(ids[k])).getDrawable();
  }

  @Override
  protected void onPause() {
    super.onPause();
    tryStop();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.start:
        tryStart();
        return true;
      case R.id.stop:
        tryStop();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public void tryStart() {
    int k;

    if (!isAnimOn) {
      for (k=0; k<NTHREADS; k++)
        thrs[k] = new colorThr(k*NTHREADS, new AnimHandler());
      for (k = 0; k < NTHREADS; k++)
        thrs[k].start();
      isAnimOn = true;
    }
  }

  public void tryStop() {
    if (isAnimOn) {
      for (int k = 0; k < NTHREADS; k++)
        thrs[k].stopThread();
      isAnimOn = false;
    }
  }

  private class colorThr extends Thread {
    private int first;
    private Handler handler;
    private Random rand;
    private volatile boolean stop = false;

    public colorThr(int first, Handler handler) {
      this.first = first;
      this.handler = handler;
      try {
        Thread.sleep(100);    // to guarantee a different seed
      }
      catch (InterruptedException e) {
      }
      rand = new Random(System.currentTimeMillis());
    }

    public void stopThread() {
      stop = true;
    }

    @Override
    public void run() {
      int circle, color;
      Message m;
      Bundle b;

      while(!stop) {
        circle = rand.nextInt(circles.length / NTHREADS) + first;
        color = rand.nextInt(colors.length);
        m = handler.obtainMessage();
        b = new Bundle();
        b.putInt("color", color);
        b.putInt("circle", circle);
        m.setData(b);
        handler.sendMessage(m);
        try {
          Thread.sleep(10);             // slow down the flow of messages
        }
        catch (InterruptedException e) {
        }
      }
    }
  }
}
