package org.feup.apm.cryptoservices;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.util.Set;

public class MainActivity extends Activity {
  TextView tv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    byte[] input, output;
    int k;
    String res = "";

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    tv = (TextView) findViewById(R.id.tv1);

    // For enumerating security providers and their services:
/*
    Provider[] provs;
    Set<Provider.Service> servs;

    provs = Security.getProviders();
    for (Provider p : provs) {
      res += (p.getName() + ":\n");
      servs = p.getServices();
      for (Provider.Service s : servs) {
        res += ("  " + s.getAlgorithm() + "\n");
      }
    }
*/
    // Some data to sign
    input = new byte[100];
    for (k=0; k<100; k++)
      input[k] = (byte) k;

    try {
      KeyPairGenerator kgen = KeyPairGenerator.getInstance("RSA");  //RSA keys
      kgen.initialize(368);                                         //size in bits
      KeyPair kp = kgen.generateKeyPair();
      PrivateKey pri = kp.getPrivate();                             // private key in a Java class
      PublicKey pub = kp.getPublic();                               // the corresponding public key in a Java class
      res += ("Private: (" + pri.toString() +")\n");
      res += ("Public: (" + pub.toString() + ")\n");
      Signature sg = Signature.getInstance("SHA1WithRSA");          // for signing with the stated algorithm
      sg.initSign(pri);                                             // supply the private key
      sg.update(input);                                             // define the data to sign
      output = sg.sign();                                           // produce the signature
      res += ("Signature: (" + output.length + "bytes)\n");
      sg.initVerify(pub);                                          // supply the public key
      sg.update(input);                                            // supply the data to verify
      boolean verify = sg.verify(output);                          // verify the signature (output) using the original data
      res += ("\nverify: " + verify +"\n");

      input[10] = 100;                                             // change a byte
      res += "\nChanged data.\n";
      sg.update(input);
      verify = sg.verify(output);                                  // verify the signature (output) using the changed data
      res += ("verify: " + verify +"\n");
    }
    catch (Exception ex) {
      res += ex.toString();
    }
    tv.setText(res);
  }
}
