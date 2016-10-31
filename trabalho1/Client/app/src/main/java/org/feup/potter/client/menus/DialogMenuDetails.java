package org.feup.potter.client.menus;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.feup.potter.client.R;
import org.feup.potter.client.db.DataBaseHelper;
import org.feup.potter.client.db.ItemTable;

public class DialogMenuDetails extends Dialog implements View.OnClickListener {
    // layout interface
    private TextView name_text_view;
    private TextView price_text_view;
    private TextView description_text_view;

    private ImageView imgView;
    private TextView typeView;


    private String [] menuItem;

    public DialogMenuDetails(Context context, String[] data) {
        super(context);
        setTitle(R.string.dialog_item_details);
        setOwnerActivity((Activity) context);

        this.menuItem = data;
        Log.d("dialog",menuItem.length + "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_menu_item_details);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        this.name_text_view = (TextView) findViewById(R.id.text_view_item_name_details);
        this.price_text_view = (TextView) findViewById(R.id.text_view_item_price_details);
        this.description_text_view = (TextView) findViewById(R.id.text_view_item_description);

        this.imgView = (ImageView) findViewById(R.id.img_item_details);
        this.typeView = (TextView) findViewById(R.id.text_view_item_type);

        this.name_text_view.setText(menuItem[1]);
        this.price_text_view.setText(menuItem[2] + " " + getOwnerActivity().getResources().getString(R.string.money));
        this.description_text_view.setText(menuItem[3]);

        //this.imgView.setImageBitmap(menuItem[4]);

        this.typeView.setText(menuItem[5]);

        Button okbut = (Button) findViewById(R.id.button_item_deatils_done);
        okbut.setOnClickListener(this);
    }

    /* for the dialog button */
    @Override
    public void onClick(View v) {
        dismiss();
    }
}