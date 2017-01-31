package com.example.arbaz.audiochat.Screen;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arbaz.audiochat.Adapter.ChatArrayAdapter;
import com.example.arbaz.audiochat.Model.ChatMessage;
import com.example.arbaz.audiochat.R;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RecognitionListener, View.OnClickListener, TextToSpeech.OnInitListener {
    ImageButton ib_audio_chat, ib_send_chat;
    EditText et_msg_chat;

    ListView lv_chat;
    ChatArrayAdapter chatArrayAdapter;
    boolean side = false;
    SpeechRecognizer speech = null;
    Intent recognizerIntent;
    String LOG_TAG = "MyGoogle";
    static TextToSpeech tts;
    ChatMessage chatMessage;
    String send_btn_temp_txt;
    Bundle bundle;
    String user_name;
    Toolbar toolbar;
    TextView tb_txt;
    String msg_text;
    private boolean backPressedToExitOnce = false;
    private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        bundle = getIntent().getExtras();
        user_name = bundle.getString("userName");

        tb_txt = (TextView) toolbar.findViewById(R.id.tb_txt);
        tb_txt.setText("" + user_name);

        getSupportActionBar().setDisplayShowTitleEnabled(false);



        ib_audio_chat = (ImageButton) findViewById(R.id.ib_audio_chat);
        ib_send_chat = (ImageButton) findViewById(R.id.ib_send_chat);
        et_msg_chat = (EditText) findViewById(R.id.et_msg_chat);
        lv_chat = (ListView) findViewById(R.id.lv_chat);

        tts = new TextToSpeech(this, this);
//        getWindow().setBackgroundDrawableResource(R.drawable.bg);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right_msg);
        lv_chat.setAdapter(chatArrayAdapter);


        et_msg_chat.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }

                return false;
            }
        });

        ib_send_chat.setOnClickListener(this);
        ib_audio_chat.setOnClickListener(this);


        lv_chat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                chatMessage = chatArrayAdapter.getMessage(position);
                //Toast.makeText(getApplicationContext(),chatMessage.getMessage(),Toast.LENGTH_SHORT).show();
                speakOut(chatMessage.getMessage());
            }
        });
   /*     getNumber(this.getContentResolver());*/

    }

    /***********************************Use for getting text from (EditText)***********************************/
    private boolean sendChatMessage() {
        send_btn_temp_txt = et_msg_chat.getText().toString();

        chatArrayAdapter.add(new ChatMessage(side, send_btn_temp_txt));
        et_msg_chat.setText("");

        if (side == false && send_btn_temp_txt.equals("ok")) {
            side = !side;
            chatArrayAdapter.add(new ChatMessage(side, "okay google"));

        } else if (side == false && send_btn_temp_txt.equals("Arbaaz")) {
            side = !side;
            chatArrayAdapter.add(new ChatMessage(side, "MohammedaArbaz Shaikh"));
        } else if (side == false && send_btn_temp_txt.equals(getResources().getString(R.string.play_music))) {
            side = !side;
            chatArrayAdapter.add(new ChatMessage(side, getResources().getString(R.string.play_music_here)));
            speakOut(getResources().getString(R.string.play_music_here));
        } else {
            //Webview
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(getResources().getString(R.string.app_name));

            WebView wv = new WebView(this);
            wv.getSettings().setBuiltInZoomControls(true);
            wv.getSettings().setJavaScriptEnabled(true);
            String query = send_btn_temp_txt; // Get the text from EditText here
            String url = "https://www.google.com/search?q=" + query;
            wv.loadUrl(url);
            wv.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);

                    return true;
                }
            });

            alert.setView(wv);
            alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            alert.show();

        }
        side = !side;

        return true;
    }

    /*start For Listening */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut("" + user_name);
                //speakOut("Hey");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    //For Listening (Speak Out)
    private static void speakOut(String txt) {
        String text = txt;
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    /*End Listening*/

    /*Start For Speech*/
    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {
        ib_audio_chat.setEnabled(false);
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Toast.makeText(getApplicationContext(), "BufferReceived ", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onEndOfSpeech() {
        ib_audio_chat.setEnabled(true);
        //Toast.makeText(getApplicationContext(), "End Of Speech", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        //  Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }


    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "Speak Again";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        speakOut(message);
        return message;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }
        killToast();
    }

    @Override
    protected void onResume() {
        super.onResume();
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "hi");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

    }

    /***********************************Use for getting Audio from (Audio Button)***********************************/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String temp_txt = "";
        if (temp_txt.equals("")) {
            temp_txt = matches.get(0);
            //set First letter Upper ByDefault
            msg_text = temp_txt.toLowerCase();
            //add word in to adapter
            chatArrayAdapter.add(new ChatMessage(side, msg_text));

            if (side == false && msg_text.equals(getResources().getString(R.string.whoisarbaaz)) || msg_text.equals("arbaz")) {
                side = !side;
                chatArrayAdapter.add(new ChatMessage(side, getResources().getString(R.string.who_is_arbaz)));
                speakOut(getResources().getString(R.string.who_is_arbaz));
            } else if (side == false && msg_text.equals(getResources().getString(R.string.connect))) {

                side = !side;
                chatArrayAdapter.add(new ChatMessage(side, getResources().getString(R.string.connect_with)));
                speakOut(getResources().getString(R.string.connect_with));


                FragmentManager manager = getFragmentManager();
                ContactListDialogFragment dialogFragment = new ContactListDialogFragment();
                dialogFragment.show(manager, "");


            } else if (side == false && msg_text.equals(getResources().getString(R.string.bye))) {
                side = !side;
                chatArrayAdapter.add(new ChatMessage(side, getResources().getString(R.string.bye_take_care)));
                speakOut(getResources().getString(R.string.bye_take_care));

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        });
                    }
                };
                thread.start();

            } else if (side == false && msg_text.equals(getResources().getString(R.string.play_music))) {
                side = !side;
                chatArrayAdapter.add(new ChatMessage(side, getResources().getString(R.string.play_music_here)));
                speakOut(getResources().getString(R.string.play_music_here));


            } else {
                side = !side;
                chatArrayAdapter.add(new ChatMessage(side, getResources().getString(R.string.from_the_web)));
                speakOut(getResources().getString(R.string.from_the_web));

                //Webview
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle(getResources().getString(R.string.app_name));

                WebView wv = new WebView(this);
                wv.getSettings().setBuiltInZoomControls(true);
                wv.getSettings().setJavaScriptEnabled(true);
                String query = msg_text; // Get the text from EditText here
                String url = "https://www.google.com/search?q=" + query;
                wv.loadUrl(url);
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);

                        return true;
                    }
                });
                alert.setView(wv);
                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                alert.show();


            }

        }
        side = !side;


    }

    @Override
    public void onBackPressed() {
        if (backPressedToExitOnce) {
            super.onBackPressed();
        } else {
            this.backPressedToExitOnce = true;
            speakOut("Are you Sure");
            showToast("Press again to exit");
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressedToExitOnce = false;

                }
            }, 2000);
        }
    }

    private void showToast(String message) {
        if (this.toast == null) {
            // Create toast if found null, it would he the case of first call only
            this.toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);

        } else if (this.toast.getView() == null) {
            // Toast not showing, so create new one
            this.toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);

        } else {
            // Updating toast message is showing
            this.toast.setText(message);
        }

        // Showing toast finally
        this.toast.show();
    }

    private void killToast() {
        if (this.toast != null) {
            this.toast.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_send_chat:
                if (isValdate()) {
                    sendChatMessage();
                }
                break;
            case R.id.ib_audio_chat:
                speech.startListening(recognizerIntent);
                Toast.makeText(getApplicationContext(), "Please Speak ", Toast.LENGTH_SHORT).show();

                break;
            default:
                break;
        }
    }

    public boolean isValdate() {
        boolean valid = true;
        String check_msg_txt = et_msg_chat.getText().toString();
        if (check_msg_txt.trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Need To Write Somthing", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {

        }
        return valid;
    }


}


