package hu.ait.android.sensordemo;


import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hu.ait.android.sensordemo.data.AcceleroData;
import hu.ait.android.sensordemo.data.SzorasData;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    int szoras1xTMP;
    int szoras1yTMP;
    int szoras1zTMP;

    double szoras1x;
    double szoras1y;
    double szoras1z;

    long currentTime = 0;
    DatabaseReference database;
    private EditText etTime;
    private EditText etUser;
    private EditText etPia;
    private EditText etTomeg;
    private TextView tvValue;
    private EditText etKaja;
    private ImageView ivKep;
    List<AcceleroData> meres1Adat = new ArrayList<AcceleroData>();
    List<AcceleroData> meres2Adat = new ArrayList<AcceleroData>();

    private Button btnStart1;
    private Button btnStart2;
    private Button btnStart3;

    String tim2;
    String name;
    String suly;
    String suly2;
    String name2;
    long ido2;
    long ido;


    private boolean meres1 = false;
    private boolean meres2 = false;

    public static final String TAG = "asdasd";

    SharedPreferences.Editor editor;

    float szoras1xatmenet;
    float szoras1yatmenet;
    float szoras1zatmenet;

    int szoras1xMentett;
    int szoras1yMentett;
    int szoras1zMentett;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        btnStart1 = ((Button) findViewById(R.id.btnStart1));
        btnStart2 = ((Button) findViewById(R.id.btnStart2));
        btnStart3 = ((Button) findViewById(R.id.btnStart3));
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        name2 = sharedPref.getString("name" , name);
        suly2 = sharedPref.getString("suly",suly);
        ido2 = sharedPref.getLong("ido", ido);

        szoras1xMentett = sharedPref.getInt("szoras1x", szoras1xTMP);
        szoras1yMentett = sharedPref.getInt("szoras1y", szoras1yTMP);
        szoras1zMentett = sharedPref.getInt("szoras1z", szoras1zTMP);

        Log.d(TAG, "Eredményx: " +szoras1xMentett);

        try {
            this.database = FirebaseDatabase.getInstance().getReference();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.tvValue = (TextView) findViewById(R.id.tvValue);
        this.etUser = (EditText) findViewById(R.id.etUser);
        this.etTime = (EditText) findViewById(R.id.etTime);
        this.etPia = (EditText) findViewById(R.id.etPia);
        this.etKaja = (EditText) findViewById(R.id.etKaja);
        this.etTomeg = (EditText) findViewById(R.id.etTomeg);
        this.ivKep = (ImageView) findViewById(R.id.ivKep);
        final SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


        if (szoras1xMentett > 0 && szoras1yMentett > 0 )
        {
           szoras1xatmenet = szoras1xMentett;
            szoras1x =  szoras1xatmenet;
            szoras1yatmenet = szoras1yMentett;
            szoras1y =  szoras1yatmenet;
            szoras1zatmenet = szoras1zMentett;
            szoras1z =  szoras1zatmenet;
            btnStart2.setEnabled(true);
            btnStart1.setEnabled(false);
            etPia.setEnabled(true);
            etKaja.setEnabled(true);
            btnStart3.setEnabled(true);

            ido = ido2;
            suly = suly2;
            name = name2;
            etUser.setText(""+ name +"");
            etTomeg.setText(""+suly+"");
            etTime.setText(""+ido+"");

            etUser.setEnabled(false);
            etTomeg.setEnabled(false);
            etTime.setEnabled(false);



        }


        tvValue.setText("Üdvözlet! Ez az alkalmazás arra szolgál, hogy megmérd, milyen hatással van az egyensúlyodra az alkohol." +
                "Először írj be egy jeligét, (pl. alma), a testtömeged (kg), és hány másodpercig tart a mérés. " +
                        "Ebben az esetben 30 mp a méréshossz)."+
                "Egyelőre még ne írj semmit az alkoholhoz és az ételhez." +
                "Ha ezekkel végeztél, állj egyenesen úgy, hogy az egyik lábad a másik előtt van (lásd lent), majd nyomd meg a Alapmérés gombot!");

        ivKep.setImageResource(R.mipmap.ic_kep);
        ivKep.getLayoutParams().height = 1000; // OR
        ivKep.getLayoutParams().width = 1000;




        // btnStart1 = ((Button) findViewById(R.id.btnStart1)); //Ha megnyomod a gombot, akkor ...
        btnStart1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (TextUtils.isEmpty(etUser.getText().toString())) {
                    etUser.setError("Add meg a neved!"); // Ha üres reklamáljon
                    return;
                }
                if (TextUtils.isEmpty(etTime.getText().toString())) {
                    etTime.setError("Add meg a mérés hosszát másodpercben!"); //Ha üres reklamáljon
                    return;
                }
                if (TextUtils.isEmpty(etTomeg.getText().toString())) {
                    etTomeg.setError("Add meg hány kilogramm vagy!"); //Ha üres reklamáljon
                    return;
                }


                ivKep.getLayoutParams().height = 0; // OR
                ivKep.getLayoutParams().width = 0;

                meres1Adat.clear();

                meres1 = true;

                btnStart3.setEnabled(true);

                MainActivity.this.currentTime = System.currentTimeMillis();
                sensorManager.registerListener(MainActivity.this, sensorManager.getDefaultSensor(1), 3);
            }
        });

        //btnStart2 = ((Button) findViewById(R.id.btnStart2)); //Ha megnyomod a gombot,akkor...
        btnStart2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (TextUtils.isEmpty(etPia.getText().toString())) {
                    etPia.setError("Add meg az italt!");  // Ha üres reklamáljon
                    return;
                }
                if (TextUtils.isEmpty(etKaja.getText().toString())) {
                    etKaja.setError("Add meg az ételt!");  //Ha üres reklamáljon
                    return;
                }

                if (TextUtils.isEmpty(etUser.getText().toString())) {
                    etUser.setError("Add meg a neved!"); // Ha üres reklamáljon
                    return;
                }
                if (TextUtils.isEmpty(etTime.getText().toString())) {
                    etTime.setError("Add meg a mérés hosszát másodpercben!"); //Ha üres reklamáljon
                    return;
                }
                if (TextUtils.isEmpty(etTomeg.getText().toString())) {
                    etTomeg.setError("Add meg hány kilogramm vagy!"); //Ha üres reklamáljon
                    return;
                }

                ivKep.getLayoutParams().height = 0; // OR
                ivKep.getLayoutParams().width = 0;




                meres2Adat.clear();

                meres2 = true;

                MainActivity.this.currentTime = System.currentTimeMillis();
                sensorManager.registerListener(MainActivity.this, sensorManager.getDefaultSensor(1), 3);



            }
        });

    btnStart3.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {

            ivKep.getLayoutParams().height = 0; // OR
            ivKep.getLayoutParams().width = 0;

            szoras1xTMP = 0;
            editor.putInt("szoras1x", szoras1xTMP);
            editor.commit();

            szoras1yTMP = 0;
            editor.putInt("szoras1y", szoras1yTMP);
            editor.commit();

            szoras1zTMP = 0;
            editor.putInt("szoras1z", szoras1zTMP);
            editor.commit();

            btnStart2.setEnabled(false);
            btnStart1.setEnabled(true);


            etPia.setEnabled(false);
            etKaja.setEnabled(false);

            tvValue.setText("Köszönöm a segítséget!" +
                    "És csak óvatosan... \uD83D\uDE09 !");

            etTime.setEnabled(true);
            etUser.setEnabled(true);
            etTomeg.setEnabled(true);


        }
    });
}
// Annyi ideig mérjen, ameddig akarom
    public void onSensorChanged(SensorEvent event) {
        if (System.currentTimeMillis() - this.currentTime > ((long) (Integer.parseInt(this.etTime.getText().toString()) * 1000))) {
            ((SensorManager) getSystemService(SENSOR_SERVICE)).unregisterListener(this);
            //this.tvValue.setText("Köszönöm, hogy segítettél!");
            // Ha az első mérésnek vége van
            if (meres1) {
                meres1 = false;
                tvValue.setText("Köszönöm! Most bulizz, és mikor úgy gondolod, hogy eleget ittál, töltsd ki az alkohollal és étellel kapcsolatos részeket. Ha végeztél, nyomd meg a Bulis Mérés gombot!" );
                //Bippenjen, ha vége van
                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);

                //átlag számítás
                float meres1XSum = 0;
                float meres1YSum = 0;
                float meres1ZSum = 0;

                for (AcceleroData meres : meres1Adat) {
                    meres1XSum += meres.getX();
                    meres1YSum += meres.getY();
                    meres1ZSum += meres.getZ();
                }

                float meres1XAtlag = meres1XSum / meres1Adat.size();
                float meres1YAtlag = meres1YSum / meres1Adat.size();
                float meres1ZAtlag = meres1ZSum / meres1Adat.size();

                // szórás számítás
                double szoras1x = 0;
                double szoras1y = 0;
                double szoras1z = 0;

                for (AcceleroData meres : meres1Adat) {
                    szoras1x += ((meres.getX()-meres1XAtlag)*(meres.getX()-meres1XAtlag));
                    szoras1y += ((meres.getY()-meres1YAtlag)*(meres.getY()-meres1YAtlag));
                    szoras1z += ((meres.getZ()-meres1ZAtlag)*(meres.getZ()-meres1ZAtlag));
                }

                szoras1x = (float) Math.sqrt(szoras1x / meres1Adat.size())*100000000;
                szoras1y = (float) Math.sqrt(szoras1y / meres1Adat.size())*100000000;
                szoras1z = (float) Math.sqrt(szoras1z / meres1Adat.size())*100000000;

                szoras1xTMP = (int)(szoras1x);
                editor.putInt("szoras1x", szoras1xTMP);
                editor.commit();

                szoras1yTMP = (int)(szoras1y);
                editor.putInt("szoras1y", szoras1yTMP);
                editor.commit();

                szoras1zTMP = (int)(szoras1z);
                editor.putInt("szoras1z", szoras1zTMP);
                editor.commit();



                tvValue.append("\nItt az első mérés szórás adatai láthatók\n"+
                        "1.mérés oldalirány: "+szoras1x/100000000+"\n"+
                        "1.mérés függőlegesen: "+szoras1y/100000000+"\n"+
                        "1.mérés menetirányban "+szoras1z/100000000+"\n\n");


                btnStart2.setEnabled(true);
                btnStart1.setEnabled(false);
                etPia.setEnabled(true);
                etKaja.setEnabled(true);

                etTime.setEnabled(false);
                etUser.setEnabled(false);
                etTomeg.setEnabled(false);

                long ido = (long) (Integer.parseInt(this.etTime.getText().toString()));
                String name = this.etUser.getText().toString().trim(); //Adatok lekérése az EditTextekből
                String suly = this.etTomeg.getText().toString().trim();


                editor.putLong("ido" ,ido);
                editor.commit();

                editor.putString("name" ,name);
                editor.commit();

                editor.putString("suly" ,suly);
                editor.commit();

            }
            //meres2 vége van
            else if (meres2) {
                meres2 = false;
                tvValue.setText("Köszönöm, végeztünk, számolok...");
                //Bippenjen, ha vége van
                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);

                calculateAverage();


            }
            return;
        }
        this.tvValue.setText("X: " + event.values[0] + "\nY: " + event.values[1] + "\nZ: " + event.values[2]);

        AcceleroData data = new AcceleroData(event.values[0], event.values[1], event.values[2]);

        if (meres1) {
            meres1Adat.add(data);
        } else if (meres2) {
            meres2Adat.add(data);
        }
    }

    //számolás
    private void calculateAverage() {

        if (szoras1xMentett > 0 && szoras1yMentett > 0 ) {}

        else{

        float meres1XSum = 0;
        float meres1YSum = 0;
        float meres1ZSum = 0;

        for (AcceleroData meres : meres1Adat) {
            meres1XSum += meres.getX();
            meres1YSum += meres.getY();
            meres1ZSum += meres.getZ();
        }

        float meres1XAtlag = meres1XSum / meres1Adat.size();
        float meres1YAtlag = meres1YSum / meres1Adat.size();
        float meres1ZAtlag = meres1ZSum / meres1Adat.size();

        // szórás számítás
         szoras1x = 0;
         szoras1y = 0;
         szoras1z = 0;

        for (AcceleroData meres : meres1Adat) {
            szoras1x += ((meres.getX()-meres1XAtlag)*(meres.getX()-meres1XAtlag));
            szoras1y += ((meres.getY()-meres1YAtlag)*(meres.getY()-meres1YAtlag));
            szoras1z += ((meres.getZ()-meres1ZAtlag)*(meres.getZ()-meres1ZAtlag));
        }

        szoras1x = (float) Math.sqrt(szoras1x / meres1Adat.size())*100000000;
        szoras1y = (float) Math.sqrt(szoras1y / meres1Adat.size())*100000000;
        szoras1z = (float) Math.sqrt(szoras1z / meres1Adat.size())*100000000;
        }

        // mérés 2 számolás
        float meres2XSum = 0;
        float meres2YSum = 0;
        float meres2ZSum = 0;

        for (AcceleroData meres : meres2Adat) {
            meres2XSum += meres.getX();   //összes tag összeadása
            meres2YSum += meres.getY();
            meres2ZSum += meres.getZ();
        }

        float meres2XAtlag = meres2XSum / meres2Adat.size(); //átlag számítás
        float meres2YAtlag = meres2YSum / meres2Adat.size();
        float meres2ZAtlag = meres2ZSum / meres2Adat.size();

        /* Ha akarnánk kiírná az átlagot
            tvValue.setText("Átlagok:\n"
                "meres1X: "+meres1XAtlag+"\n"+
                "meres1Y: "+meres1YAtlag+"\n"+
                "meres1Z: "+meres1ZAtlag+"\n\n"+
                "meres2X: "+meres2XAtlag+"\n"+
                "meres2Y: "+meres2YAtlag+"\n"+
                "meres2Z: "+meres2ZAtlag+"\n\n");*/

        // szórás számítás
        double szoras2x = 0;
        double szoras2y = 0;
        double szoras2z = 0;

        for (AcceleroData meres : meres2Adat) {
            szoras2x += ((meres.getX()-meres2XAtlag)*(meres.getX()-meres2XAtlag));
            szoras2y += ((meres.getY()-meres2YAtlag)*(meres.getY()-meres2YAtlag));
            szoras2z += ((meres.getZ()-meres2ZAtlag)*(meres.getZ()-meres2ZAtlag));
        }

        szoras2x = Math.sqrt(szoras2x / meres2Adat.size())*100000000;
        szoras2y = Math.sqrt(szoras2y / meres2Adat.size())*100000000;
        szoras2z = Math.sqrt(szoras2z / meres2Adat.size())*100000000;



        tvValue.setText("\nItt a két mérés szórás adatai láthatók\n"+
                "1.mérés oldalirány: "+szoras1x/100000000+", 2.mérés oldalirányban: "+szoras2x/100000000+"\n"+
                "1.mérés függőlegesen: "+szoras1y/100000000+", 2.mérés függőlegesen: "+szoras2y/100000000+"\n"+
                "1.mérés menetirányban "+szoras1z/100000000+", 2.mérés menetirányban: "+szoras2z/100000000+"\n\n");


        // szóras feltöltés firebase-ba
        String name = this.etUser.getText().toString().trim(); //Adatok lekérése az EditTextekből
        String ital = this.etPia.getText().toString().trim();
        String etel = this.etKaja.getText().toString().trim();
        String suly = this.etTomeg.getText().toString().trim();
        String tim2 =new Date(MainActivity.this.currentTime).toString().trim();

        SzorasData data = new SzorasData(tim2, suly ,etel, ital, szoras1x/100000000, szoras1y/100000000, szoras1z/100000000, szoras2x/100000000, szoras2y/100000000, szoras2z/100000000);

        try {
            FirebaseDatabase.getInstance().getReference().child(name).child(
                    this.database.child(name).push().getKey()).setValue(data);
        } catch (Exception e) {
            e.printStackTrace();
        }


        double elteresX = ((szoras2x/szoras1x) * 100) - 100; //százalékos eltérés kiszámítása
        double elteresY = ((szoras2y/szoras1y) * 100) - 100;
        double elteresZ = ((szoras2z/szoras1z) * 100) - 100;
        double elteresatl = (elteresX+elteresY+elteresZ)/3;


        // appendnél látszana az előző kiírás is
        tvValue.append("Az alkohol hatására az egyensúlyod \n"+
                "oldalirányban "+elteresX+"%-kal,"+"\n"+
                "függőlegesen "+elteresY+"%-kal,"+"\n"+
                "menetirányban pedig "+elteresZ+"%-kal lett rosszabb " +
                "Köszönöm a segítséget!" +"\n"+
                        "Ha valamelyik százalék negatív, az azt jelenti, hogy az előző mérésnél jobb lett az utóbbi." +
                "Ha szeretnél még egy alkoholos mérést mérni, nyomd meg újra a Bulis Mérés gombot!" +
                "Ha nem szeretnél többet mérni, nyomd meg a nem szeretnék többet mérni gombot!" );



    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}