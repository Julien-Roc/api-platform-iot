package com.example.iot;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; //!< le TAG de la classe pour les logs
    ClientMQTT clientMQTT = null;
    TextView bienvenue;
    TextView credit;
    Button ouvrir;
    Button ferme;
    Client[] list_client = new Client[10];
    int id_client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_client[0] = new Client("Julien", "1234", 10);
        list_client[1] = new Client("Vincent", "2431", 10);
        list_client[2] = new Client("Andrya", "2341", 10);

        bienvenue = findViewById(R.id.bienvenue);
        credit = findViewById(R.id.credit);
        ouvrir = findViewById(R.id.ouvrir);
        ferme = findViewById(R.id.ferme);

        clientMQTT = new ClientMQTT(getApplicationContext());

        demarrerMQTT();

        ouvrir.setOnClickListener(view -> {
            clientMQTT.publishMessage("Ouvrir");
            list_client[id_client].setCredit(list_client[id_client].getCredit()-1);
        });

        ferme.setOnClickListener(view -> {
            clientMQTT.publishMessage("Fermer");
        });
    }

    private void demarrerMQTT()
    {
        clientMQTT.reconnecter();

        clientMQTT.mqttAndroidClient.setCallback(new MqttCallbackExtended()
        {
            @Override
            public void connectComplete(boolean b, String s)
            {
                Log.w(TAG,"connectComplete");
            }

            @Override
            public void connectionLost(Throwable throwable)
            {
                Log.w(TAG,"connectionLost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception
            {
                Log.w(TAG, "messageArrived : " + mqttMessage.toString());
                if(!connexion(mqttMessage.toString())){
                    bienvenue.setText("Mauvais code");
                }
                //bienvenue.setText(mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken)
            {
                Log.w(TAG, "deliveryComplete");
            }
        });
    }

    private boolean connexion(String input){
        boolean client_existe = false;
        for(int i = 0; i < list_client.length; i++){
            if(list_client[i].code.equals(input)){
                client_existe = true;
                bienvenue.setText("Bienvenue " + list_client[i].getNom());
                id_client = i;
            }
        }
        return client_existe;
    }
}
