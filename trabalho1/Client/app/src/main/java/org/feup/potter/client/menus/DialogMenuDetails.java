package org.feup.potter.client.menus;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
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
    private EditText typeView;


    private String itemId;

    public DialogMenuDetails(Context context, String itemId) {
        super(context);

        this.name_text_view = (TextView) findViewById(R.id.text_view_item_name_details);
        this.price_text_view = (TextView) findViewById(R.id.text_view_item_price_details);
        this.description_text_view = (TextView) findViewById(R.id.text_view_item_description);

        this.imgView = (ImageView) findViewById(R.id.img_item_details);
        this.typeView = (EditText) findViewById(R.id.text_view_item_type);
        // to do not make it editable
        this.typeView.setKeyListener(null);


        setTitle(R.string.dialog_item_details);
        setOwnerActivity((Activity) context);
        this.itemId = itemId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_menu_item_details);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // check if give conflicts with the previous association done by the list
        DataBaseHelper dataBase = new DataBaseHelper(getOwnerActivity());

        ItemTable dataHelper = dataBase.getItemsTable();

        Cursor c = dataHelper.getByIdItem(itemId);
        c.moveToFirst();

        this.name_text_view.setText(dataHelper.getName(c)+ ": ");
        this.price_text_view.setText(dataHelper.getPrice(c) + " " + getOwnerActivity().getResources().getString(R.string.money));
        this.description_text_view.setText(dataHelper.getDescription(c));

        this.imgView.setImageBitmap(dataHelper.getImg(c));
        this.typeView.setText(dataHelper.getType(c));

        // Closes the Cursor
        c.close();

        Button okbut = (Button) findViewById(R.id.button_item_deatils_done);
        okbut.setOnClickListener(this);
    }

    /* for the dialog button */
    @Override
    public void onClick(View v) {
        dismiss();
    }
}