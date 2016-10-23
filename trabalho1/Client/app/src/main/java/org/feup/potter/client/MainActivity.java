package org.feup.potter.client;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import org.feup.potter.client.main_menu.GridViewAdapter;
import org.feup.potter.client.main_menu.Item;

public class MainActivity extends Activity implements OnItemClickListener
{
    GridView gridview;
    GridViewAdapter gridviewAdapter;
    ArrayList<Item> data = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView(); // Initialize the GUI Components
        fillData(); // Insert The Data
        setDataAdapter(); // Set the Data Adapter
    }

    // Initialize the GUI Components
    private void initView()
    {
        gridview = (GridView) findViewById(R.id.gridView);
        gridview.setOnItemClickListener(this);
    }

    // Insert The Data
    private void fillData()
    {
        data.add(new Item("op 1", getResources().getDrawable(R.drawable.rest_img)));
        data.add(new Item("op 2", getResources().getDrawable(R.drawable.rest_img)));
        data.add(new Item("op 3", getResources().getDrawable(R.drawable.rest_img)));
        data.add(new Item("op 4", getResources().getDrawable(R.drawable.rest_img)));
        data.add(new Item("op 5", getResources().getDrawable(R.drawable.rest_img)));
        data.add(new Item("op 6", getResources().getDrawable(R.drawable.rest_img)));
    }

    // Set the Data Adapter
    private void setDataAdapter()
    {
        gridviewAdapter = new GridViewAdapter(getApplicationContext(), R.layout.grid_view_menu_icon, data);
        gridview.setAdapter(gridviewAdapter);
    }

    @Override
    public void onItemClick(final AdapterView<?> arg0, final View view, final int position, final long id)
    {
        String message = "Clicked : " + data.get(position).getTitle();
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_SHORT).show();
    }

}

