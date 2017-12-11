package io.github.nobodyzxc.dizifingering;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import java.security.interfaces.RSAKey;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity {

    private String[] solfgeName = new String[] {
            "C" , "D" , "E" , "F" , "G" , "A" , "B"
    };
    private String[] sheetTuneName = new String[] {
            "C", "G", "D", "A", "E", "B", "F♯", "C♯",
            "F", "B♭", "E♭", "A♭", "D♭", "G♭", "C♭"
    };
    private String[] diziTuneName = new String[] {
            "G" , "F" , "A" , "D" , "C" , "B♭" , "E"
    };
    private String[] fingerName = new String[] {
            "筒音5" , "筒音2" , "筒音1" , "筒音3" , "筒音6" ,
            "筒音7" , "筒音4"
    };

    private String[] sharpSolfeoName = new String[]{
            "Si" , "Do" , "Do♯" , "Re" , "Re♯" , "Mi" , "Fa" ,
            "Fa♯", "Sol" , "Sol♯" , "La" , "La♯" , "Si" , "Do"
    };

    private String[] reduceSolfeoName = new String[]{
            "Si" , "Do" , "Re♭" , "Re" , "Mi♭" , "Mi" , "Fa" ,
            "Sol♭", "Sol" , "La♭" , "La" , "Si♭" , "Si" , "Do"
    };

    private String[] sharpFingerName = new String[]{
            "7" , "1" , "1♯" , "2" , "2♯" , "3" , "4" ,
            "4♯", "5" , "5♯" , "6" , "6♯" , "7" , "1"
    };

    private String[] reduceFingerName = new String[]{
            "7" , "1" , "2♭" , "2" , "3♭" , "3" , "4" ,
            "5♭", "5" , "6♭" , "6" , "7♭" , "7" , "1"
    };


    private AlertDialog.Builder builder;
    private TableLayout TabLayout;

    Spinner sheetTuneSpin;
    Spinner diziTuneSpin;
    Spinner fingerSpin;

    ArrayList<View> views = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout = findViewById(R.id.tab);
        Button ask = findViewById(R.id.ask);
        TextView sheetTuneTextView = findViewById(R.id.SheetTit);
        TextView DiziTextView = findViewById(R.id.DiziTuneTit);
        TextView MapTextView = findViewById(R.id.MapTit);
        sheetTuneTextView.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        DiziTextView.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        MapTextView.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        sheetTuneSpin = findViewById(R.id.sheetTune);
        diziTuneSpin = findViewById(R.id.diziTune);
        fingerSpin = findViewById(R.id.finger);

        sheetTuneSpin.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sheetTuneName));
        diziTuneSpin.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, diziTuneName));
        fingerSpin.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, fingerName));

        CharSequence[] colors = new CharSequence[] {
                "None" , "Fa♯", "Do♯", "Sol♯", "Re♯", "La♯", "Mi♯", "Si♯",
                "Si♭", "Mi♭", "La♭", "Re♭", "Sol♭", "Do♭", "Fa♭"
        };

        builder = new AlertDialog.Builder(this);
        builder.setTitle("調號最後一升/降音為");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                sheetTuneSpin.setSelection(which);
            }
        });

        ask.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });

        ask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String msg = getResources().getString(R.string.author_msg);
                final SpannableString s = new SpannableString(msg);
                int github = msg.indexOf("github");
                int blog = msg.indexOf("blog");
                s.setSpan(new URLSpan("https://github.com/nobodyzxc"), github , github + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new URLSpan("http://no8dyzxc.pixnet.net/blog"), blog , blog + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                final AlertDialog d = new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.owo)
                        .setMessage(s)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), R.string.gogo, Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                ((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                return true;
            }
        });


        AdapterView.OnItemSelectedListener SpinListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                writeMapping();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        };

        sheetTuneSpin.setOnItemSelectedListener(SpinListener);
        diziTuneSpin.setOnItemSelectedListener(SpinListener);
        fingerSpin.setOnItemSelectedListener(SpinListener);

    }

    public int[] majorSolfeo(int idx){
        final int [] seq = new int []{4 , 1 , 5 , 2 , 6 , 3 , 7};

        int [] solfeoTab = new int []{0 , 0 , 0 , 0 , 0 , 0 , 0};
        if(idx == 0) return solfeoTab;
        int offset = idx > 7 ? -1 : 1;
        int [] range = idx > 7 ?
                Arrays.copyOfRange(seq , 14 - idx , 7) :
                Arrays.copyOf(seq , idx);
        for(int i = 0 ; i < range.length ; i++)
            solfeoTab[range[i] - 1] += offset;
        return solfeoTab;
    }

    public String SRsign(int v){
        if(v != 0) return new String(new char[abs(v)]).replace("\0", v > 0 ? "♯" : "♭");
        else return "";
    }

    public int nor(int v){
        while(v > 7) v -= 7;
        while(v < 1) v += 7;
        return v;
    }

    public void writeMapping() {
        if(views.size() > 0) {
            for(int i = 0 ; i < views.size() ; i++)
                TabLayout.removeView(views.get(i));
            views.clear();
        }
        int [] solfeoTab = new int []{1 , 3 , 5 , 6 , 8 , 10 , 12};
        int spinIdx = sheetTuneSpin.getSelectedItemPosition();
        String [] solfeoNameTab = spinIdx > 7 ? reduceSolfeoName : sharpSolfeoName;
        int [] offsets = majorSolfeo(sheetTuneSpin.getSelectedItemPosition());

        String curDiziTune = diziTuneName[diziTuneSpin.getSelectedItemPosition()];
        int [] diziSolfge = majorSolfeo(Arrays.asList(sheetTuneName).indexOf(curDiziTune));
        for(int i = 0 ; i < diziSolfge.length ; i++)
            diziSolfge[i] += solfeoTab[i];

        String low = fingerName[fingerSpin.getSelectedItemPosition()];
        int lowestIdx = Integer.parseInt(low.substring(low.length() - 1));
        String [] fingering = new String[7];
        for(int i = 0 ; i < 7 ; i++) {
            int off = solfeoTab[i] + offsets[i] - diziSolfge[i];
            int fig =  i - 3 + lowestIdx - (Arrays.asList(solfgeName).indexOf(curDiziTune.substring(0 , 1)) + 1);
            fingering[i] = Integer.toString(nor(fig)) + SRsign(off);
        }
        //筒音

        for (int i = 0 ; i < offsets.length ; i++){
            TableRow row= new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            TextView solfeoTextView = new TextView(this);
            TextView fingeringTextView = new TextView(this);
            solfeoTextView.setText(solfeoNameTab[solfeoTab[i]] + SRsign(offsets[i]));
            fingeringTextView.setText(fingering[i]);
            solfeoTextView.setTextSize(30);
            fingeringTextView.setTextSize(30);
            fingeringTextView.setGravity(1);
            solfeoTextView.setTypeface(Typeface.MONOSPACE, Typeface.NORMAL);
            fingeringTextView.setTypeface(Typeface.MONOSPACE, Typeface.NORMAL);
            row.addView(solfeoTextView);
            row.addView(fingeringTextView);
            TabLayout.addView(row);
            views.add(row);
        }
    }

}
