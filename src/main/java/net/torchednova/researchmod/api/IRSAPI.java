package net.torchednova.researchmod.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import static net.torchednova.researchmod.ResearchMod.LOGGER;

public class IRSAPI {

    private static final String LINE_END = "\r\n";
    private static final String twoHyphens = "--";
    private static final String boundary = "*****"; // Change this string to a unique boundary
    private static String apiKey;
    private static String DispApiKey;

    public static void init(String apiKeyNew, String dispAPIKey) {
        //LOGGER.info(apiKeyNew);
        apiKey = apiKeyNew;
        DispApiKey = dispAPIKey;
    }

    public static String setResearch(int id) {
        try {
            URL obj = new URL("https://research.neonetwork.xyz/api/setresearch");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.setDoOutput(true);

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(twoHyphens + boundary + LINE_END);
                wr.writeBytes("Content-Disposition: form-data; name=\"apikey\"" + LINE_END);
                wr.writeBytes(LINE_END);
                wr.writeBytes(apiKey + LINE_END);

                wr.writeBytes(twoHyphens + boundary + LINE_END);
                wr.writeBytes("Content-Disposition: form-data; name=\"stageID\"" + LINE_END);
                wr.writeBytes(LINE_END);
                wr.writeBytes(id + LINE_END);

                wr.writeBytes(twoHyphens + boundary + LINE_END);
                wr.writeBytes("Content-Disposition: form-data; name=\"force\"" + LINE_END);
                wr.writeBytes(LINE_END);
                wr.writeBytes("false" + LINE_END);

                wr.writeBytes(LINE_END);
                wr.writeBytes(twoHyphens + boundary + twoHyphens + LINE_END);
                wr.flush();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            return response.toString();
        } catch (Exception exception) {
            //IndustriaDailies.LOGGER.error("Unable complete request [{}] [{}] [{}]", target.getName().getString(), amount, ref);
            exception.printStackTrace();
            return null;
        }
    }

    public static String getAvResearch() {
        try {
            URL obj = new URL("https://research.neonetwork.xyz/api/getresearchlist");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.setDoOutput(true);

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(twoHyphens + boundary + LINE_END);
                wr.writeBytes("Content-Disposition: form-data; name=\"apikey\"" + LINE_END);
                wr.writeBytes(LINE_END);
                wr.writeBytes(DispApiKey + LINE_END);

                //wr.writeBytes(LINE_END);
                //wr.writeBytes(twoHyphens + boundary + twoHyphens + LINE_END);
                wr.flush();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            return response.toString();
        } catch (Exception exception) {
            //IndustriaDailies.LOGGER.error("Unable complete request [{}] [{}] [{}]", target.getName().getString(), amount, ref);
            exception.printStackTrace();
            return null;
        }
    }

    public static String checkCurrentResearch() {
        try {
            URL obj = new URL("https://research.neonetwork.xyz/api/getresearch");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.setDoOutput(true);

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(twoHyphens + boundary + LINE_END);
                wr.writeBytes("Content-Disposition: form-data; name=\"apikey\"" + LINE_END);
                wr.writeBytes(LINE_END);
                wr.writeBytes(DispApiKey + LINE_END);

                //wr.writeBytes(LINE_END);
                //wr.writeBytes(twoHyphens + boundary + twoHyphens + LINE_END);
                wr.flush();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            return response.toString();
        } catch (Exception exception) {
            //IndustriaDailies.LOGGER.error("Unable complete request [{}] [{}] [{}]", target.getName().getString(), amount, ref);
            exception.printStackTrace();
            return null;
        }
    }

    public static String getAllResearch() {
        try {
            URL obj = new URL("https://research.neonetwork.xyz/api/getresearchtree");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.setDoOutput(true);

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(twoHyphens + boundary + LINE_END);
                wr.writeBytes("Content-Disposition: form-data; name=\"apikey\"" + LINE_END);
                wr.writeBytes(LINE_END);
                wr.writeBytes(apiKey + LINE_END);

                //wr.writeBytes(LINE_END);
                //wr.writeBytes(twoHyphens + boundary + twoHyphens + LINE_END);
                wr.flush();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            return response.toString();
        } catch (Exception exception) {
            //IndustriaDailies.LOGGER.error("Unable complete request [{}] [{}] [{}]", target.getName().getString(), amount, ref);
            exception.printStackTrace();
            return null;
        }
    }
}
