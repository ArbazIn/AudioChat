package com.example.arbaz.audiochat.Screen;


import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.arbaz.audiochat.Adapter.AllContactsAdapter;
import com.example.arbaz.audiochat.Model.ContactList;
import com.example.arbaz.audiochat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arbaz on 8/10/16.
 */

public class ContactListDialogFragment extends DialogFragment {

    RecyclerView contact_list;
    ContentResolver contentResolver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        View rootView = inflater.inflate(R.layout.custom_contact_dialog, container, false);

        contact_list = (RecyclerView) rootView.findViewById(R.id.lv_contact_custom);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);



        /*for getting contacts*/
        List<ContactList> contactVOList = new ArrayList();
        ContactList contactVO;
        contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contactVO = new ContactList();
                    contactVO.setContactName(name);

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactVO.setContactNumber(phoneNumber);
                    }

                    phoneCursor.close();


                    contactVOList.add(contactVO);
                }
            }

            AllContactsAdapter contactAdapter = new AllContactsAdapter(contactVOList, getActivity());
            contact_list.setLayoutManager(new LinearLayoutManager(getActivity()));
            contact_list.setLayoutManager(new MyCustomLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            contact_list.setAdapter(contactAdapter);
        }
        return rootView;
    }

    public class MyCustomLayoutManager extends LinearLayoutManager {
        private static final float MILLISECONDS_PER_INCH = 9000f;
        private Context mContext;

        public MyCustomLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
            mContext = context;
        }

    }
}