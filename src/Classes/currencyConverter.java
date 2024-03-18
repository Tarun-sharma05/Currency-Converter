package Classes;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class currencyConverter {


    private static final String API_BASE_URL = "https://openexchangerates.org/api/";
   private static final String API_ENDPOINT = "latest.json";
   private static final String API_APP_ID = "e4aa8f0a6c7d4b1488b64bdcbc38e68f";

   private static double exchangeRate;
   public static void fetchExchangeRate(){

       try {
           //Cunstruct API URL with query parameters
           String apiUrl = API_BASE_URL + API_ENDPOINT + "?app_id="+ API_APP_ID;

           URL url = new URL(apiUrl);
           HttpURLConnection con = (HttpURLConnection) url.openConnection();
           con.setRequestMethod("GET");

           BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
           StringBuilder response = new StringBuilder();
           String inputLine;
           while ((inputLine = in.readLine()) != null){
               response.append(inputLine);
           }
           in.close();

           JSONParser parser = new JSONParser();
           Object obj = parser.parse(response.toString());
           JSONObject jsonResponse = (JSONObject) obj;
           String baseCurrency = (String) jsonResponse.get("base");
           JSONObject rates = (JSONObject) jsonResponse.get("rates");

           // Check if the "USD" key exists in the JSONObject
           Object usdRate = rates.get("USD");
           if (rates.containsKey("USD")) {

               if (usdRate instanceof Double) {
                   exchangeRate = (Double) usdRate;
                   System.out.println("Exchange Rate for USD: " + exchangeRate);
               } else {
                   System.out.println("USD rate is not a double value");
               }
           } else {
               System.out.println("USD rate not found in JSON response");
           }

         //  exchangeRate = rates.getDouble("USD");
       } catch (IOException | ParseException e){
           e.printStackTrace();
           JOptionPane.showMessageDialog(null, "Failed to fetch exchange rate. Please try again later.");
       }
   }

    //Function to convert from rupee
    //to the dollar and vice -versa
    //using java swing
public static void converter(){
       //Fetch the latest exchange rate
    fetchExchangeRate();
    //Creating a new frame using JFrame
    JFrame frame = new JFrame("Currency Converter");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //Creating two labels
    JLabel rupeeLabel = new JLabel("Rupees");
    rupeeLabel.setBounds(20,40,60,30);
    JLabel dollarLabel = new JLabel("Dollars:");
    dollarLabel.setBounds(170,40,60,30);

    //Creating two text fields.
    //One for rupee and one for dollar
    JTextField rupeeTextField = new JTextField("0");
    rupeeTextField.setBounds(80,40,70,30);
    JTextField dollarTextField = new JTextField("0");
    dollarTextField.setBounds(240,40,70,30);

    //Creating three buttons
    JButton rupeeToDollarButton = new JButton("INR to USD");
    rupeeToDollarButton.setBounds(50,80,100,30);
    JButton dollarToRupeeButton = new JButton("USD to INR");
    dollarToRupeeButton.setBounds(190,80,100,30);

    JButton closeButton = new JButton("Close");
    closeButton.setBounds(150,150,100,30);


    //Adding action listener
    rupeeToDollarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Converting to double
                try{
                    double rupees = Double.parseDouble(rupeeTextField.getText());
                    double dollars = rupees / exchangeRate;
                    dollarTextField.setText(String.valueOf(dollars));
                    frame.repaint();
                } catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid number.");
                }
            }
    });

    //Adding action listener
    dollarToRupeeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
          try {
              double dollars = Double.parseDouble(dollarTextField.getText());
              double rupees = dollars * exchangeRate;
              rupeeTextField.setText(String.valueOf(rupees));
              frame.repaint();
          } catch (NumberFormatException ex){
              JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid number.");

          }
      }
    });

    //Action listener to close the form
    closeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
        }
    });


    //Adding the created objects to the form
    frame.add(rupeeLabel);
    frame.add(rupeeTextField);
    frame.add(dollarLabel);
    frame.add(dollarTextField);
    frame.add(rupeeToDollarButton);
    frame.add(dollarToRupeeButton);
    frame.add(closeButton);

    frame.setLayout(null);
    frame.setSize(400,300);
    frame.setVisible(true);

}

//Driver code
    public static void main(String[] args) {
        
      converter();
    }
}