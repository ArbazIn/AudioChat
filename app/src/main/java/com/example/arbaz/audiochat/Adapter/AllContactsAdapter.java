package com.example.arbaz.audiochat.Adapter;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arbaz.audiochat.Model.ContactList;
import com.example.arbaz.audiochat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arbaz on 7/10/16.
 */

public class AllContactsAdapter extends RecyclerView.Adapter<AllContactsAdapter.ContactViewHolder> {

    private List<ContactList> contactVOList = new ArrayList<ContactList>();
    private Context mContext;
    Button btn_call, btn_whatsapp, btn_msg, btn_connect_via;
    String name_temp;
    String phone_temp;
    String uri;
    TextView tv_cp_name, tv_cp_number;

    public AllContactsAdapter(List<ContactList> contactVOList, Context mContext) {
        this.contactVOList = contactVOList;
        this.mContext = mContext;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_contact_list_row, null);
        final ContactViewHolder contactViewHolder = new ContactViewHolder(view);


        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {
        final ContactList contactVO;
        contactVO = contactVOList.get(position);
        try {
            holder.rl_contact.setTag(contactVO);
            holder.tvContactName.setText(contactVO.getContactName());
            holder.tvPhoneNumber.setText(contactVO.getContactNumber());


        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.rl_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContactList contactList;
                contactList = (ContactList) v.getTag();
                name_temp = contactList.getContactName();
                phone_temp = contactList.getContactNumber();


                Dialog socialDialog = new Dialog(mContext, R.style.AppTheme);
                socialDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                Window window = socialDialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                socialDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                socialDialog.setCanceledOnTouchOutside(true);
                socialDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                socialDialog.setContentView(R.layout.custom_social_touch_dialog);


                btn_call = (Button) socialDialog.findViewById(R.id.btn_call);
                btn_msg = (Button) socialDialog.findViewById(R.id.btn_msg);
                btn_whatsapp = (Button) socialDialog.findViewById(R.id.btn_whatsapp);
                //  btn_connect_via = (Button) socialDialog.findViewById(R.id.btn_connect_via);


                tv_cp_name = (TextView) socialDialog.findViewById(R.id.tv_cp_name);
                tv_cp_number = (TextView) socialDialog.findViewById(R.id.tv_cp_number);


             /*   btn_connect_via.setText("How Would You Like To Connect" + "+ name_temp+");*/

                tv_cp_name.setText(name_temp);
                tv_cp_number.setText(phone_temp);

                uri = "tel:" + phone_temp.trim();
                btn_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent iCall = new Intent(Intent.ACTION_CALL);
                        iCall.setData(Uri.parse(uri));
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mContext.startActivity(iCall);
                    }
                });

                btn_whatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri mUri = Uri.parse("smsto:" + phone_temp);
                        Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
                        mIntent.setPackage("com.whatsapp");
                        mIntent.putExtra("sms_body", name_temp);
                        mIntent.putExtra("chat", true);
                        mContext.startActivity(mIntent);
                        preMessage();

                    }
                });
                btn_msg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.setData(Uri.parse("sms:" + phone_temp));
                        sendIntent.putExtra("sms_body", "Hello How Are You ?");
                        mContext.startActivity(sendIntent);
                        preMessage();
                    }
                });

                socialDialog.show();

            }
        });
    }

    public void preMessage() {

        ClipboardManager cManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData cData = ClipData.newPlainText("text", "Hello How Are You ?");
        cManager.setPrimaryClip(cData);
        Toast.makeText(mContext, "Text AllReady Copied Just Paste It ", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return contactVOList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;
        RelativeLayout rl_contact;

        public ContactViewHolder(View itemView) {
            super(itemView);
            rl_contact = (RelativeLayout) itemView.findViewById(R.id.rl_contact);
            ivContactImage = (ImageView) itemView.findViewById(R.id.iv_contact_img);
            tvContactName = (TextView) itemView.findViewById(R.id.tv_contact_name);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tv_contact_number);
        }
    }

    public ContactList getPhoneNumber(int postion) {
        return contactVOList.get(postion);
    }
}