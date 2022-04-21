package com.example.calsakay;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessagesFragment extends Fragment{

    private RecyclerView rvMessages;
    boolean noMessages = false;
    Dashboard currentAct;

    public final Handler handler = new Handler();
    private Runnable refreshPassengerList = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 3000);
            new AccessDB().execute();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.currentAct = (Dashboard) getActivity();

        this.rvMessages = view.findViewById(R.id.rvMessagesContainer);
        // WHERE (`reciever` = 62 AND `sender` = 80) OR (`reciever` = 80 AND `sender` = 62);
        // SELECT DISTINCT(`reciever`) FROM `messages` WHERE `reciever` = 62 OR `sender` = 62;  SELECT DISTINCT(`sender`) FROM `messages` WHERE `reciever` = 62 OR `sender` = 62;
        handler.postDelayed(refreshPassengerList, 3000);
    }



        class AccessDB extends AsyncTask<Void, Void, Void> {
        String records1 = "", error = "";
        List<Integer> inbox;
        int[] recieverList, senderList, inboxList;
        int userId = Integer.parseInt(currentAct.getUserData().get(0)[0]);
        List<Messages> messages = new ArrayList<>();

        private Date convertToDateObject(String mysqlString) throws ParseException {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-d HH:mm:ss");
            Date date = formatter.parse(mysqlString);
            return date;
        }

        private int[] mergeArray(int[] arr1, int[] arr2){
            int[] a = arr1;
            int[] b = arr2;
            int[] c = new int[a.length+b.length];
            int[] fin = new int[a.length+b.length];
            int[] ret, finalNaArray;
            int i = 0, q = 0;
            for(int j : fin){
                fin[i++] = -1;
            }
            i = 0;
            for(int j : a){
                c[i++] = j;
            }
            for(int j : b){
                c[i++] = j;
            }
            boolean check = false;
            for(int j = 0,k = 0; j < c.length; j++){
                for(int l : fin){
                    if( l == c[j] )
                        check = true;
                }
                if(!check){
                    fin[k++] = c[j];
                } else check = false;
            }

            for (int x : fin){
                if(x == -1){
                    break;
                }
                q++;
            }
            ret = new int[q];
            for (int w = 0; w < q; w++){
                if(fin[w] == -1){
                    break;
                }
                ret[w] = fin[w];
            }

            finalNaArray = new int[q-1];
            int t = 0;
            for(int e = 0; e < q; e++){
                if(ret[e] != userId){
                    finalNaArray[t] = ret[e];
                    t++;
                }
            }

            return finalNaArray;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int emptyMessages = 0;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://163.44.242.10:3306/feqxsxpi_calsakay?characterEncoding=latin1","feqxsxpi_root", "UCC2021bsitKrazy");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(DISTINCT(`reciever`)) AS numRow FROM `messages` WHERE `reciever` = " + userId + " OR `sender` = " + userId);

                resultSet.next();
                if(resultSet.getInt("numRow") == 0){
                    emptyMessages++;
                } else {
                    recieverList = new int[resultSet.getInt("numRow")];
                    resultSet = statement.executeQuery("SELECT DISTINCT(`reciever`) FROM `messages` WHERE `reciever` = " + userId + " OR `sender` = " + userId);
                    int i = 0;
                    while(resultSet.next()){
                        recieverList[i] = resultSet.getInt("reciever");
                        i++;
                    }
                }

                resultSet = statement.executeQuery("SELECT COUNT(DISTINCT(`sender`)) AS numRow FROM `messages` WHERE `reciever` = " + userId + " OR `sender` = " + userId);
                resultSet.next();
                if(resultSet.getInt("numRow") == 0){
                    emptyMessages++;
                    noMessages = true;
                    return null;
                }

                senderList = new int[resultSet.getInt("numRow")];
                resultSet = statement.executeQuery("SELECT DISTINCT(`sender`) FROM `messages` WHERE `reciever` = " + userId + " OR `sender` = " + userId);
                int i = 0;
                while(resultSet.next()){
                    senderList[i] = resultSet.getInt("sender");
                    i++;
                }
                inboxList = new int[mergeArray(senderList, recieverList).length];
                inboxList = mergeArray(senderList, recieverList);



                for (Integer inbId : inboxList){
                    resultSet = statement.executeQuery("SELECT calsakay_tbl_users.user_image AS chatmateImage, CONCAT(calsakay_tbl_users.first_name, ' ', calsakay_tbl_users.last_name) AS threadName, " +
                            "IF(messages.sender = " + userId + ", 'outgoing', 'ingoing') AS messageType, " +
                            "messages.sender, messages.reciever, messages.message_id, messages.message, messages.read, messages.time, messages.read " +
                            "FROM `messages` JOIN calsakay_tbl_users ON " +
                            "IF(messages.sender = " + userId + ", messages.reciever = calsakay_tbl_users.id, messages.sender = calsakay_tbl_users.id)" +
                            "WHERE (`reciever` = " + userId + " AND `sender` = " + inbId + ") " +
                            "OR (`reciever` = " + inbId + " AND `sender` = " + userId + ") " +
                            "ORDER BY message_id DESC LIMIT 1");
                    while(resultSet.next()){
                        messages.add(new Messages(
                                Integer.parseInt(resultSet.getString("sender")),
                                Integer.parseInt(resultSet.getString("reciever")),
                                convertToDateObject(resultSet.getString("time")),
                                resultSet.getString("message"),
                                Integer.parseInt(resultSet.getString("message_id")),
                                resultSet.getString("threadName"),
                                resultSet.getString("messageType"),
                                resultSet.getBoolean("read"),
                                inbId,
                                userId,
                                resultSet.getString("chatmateImage")
                        ));
                    }
                }

            } catch (Exception e) {
                Log.d("MESSAGE RETR ERROR: ", e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {


            MessagesRecViewAdapter messageAdapter = new MessagesRecViewAdapter();

            messageAdapter.setMessages(messages);

            rvMessages.setAdapter(messageAdapter);
            rvMessages.setLayoutManager(new LinearLayoutManager(getActivity()));

            super.onPostExecute(unused);
        }
    }
}