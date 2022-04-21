package com.example.calsakay;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess  {
    private String databaseUrl = "163.44.242.10:3306";
    private String databaseName = "feqxsxpi_calsakay";
    private String databaseCharSet = "latin1";
    private String databaseUser = "feqxsxpi_root";
    private String databasePassword = "UCC2021bsitKrazy";
    private String databaseQuery;
    private List<String[]> fetchedData = new ArrayList<>();
    private int columnCount;
    private Context context;
    String urlString = "jdbc:mysql://"
            + databaseUrl
            + "/"+ databaseName
            + "?characterEncoding=" + databaseCharSet;


    public DatabaseAccess(Context context) {
        this.context = context;
    }

    public String getDatabaseQuery() {
        return this.databaseQuery;
    }


    public void executeNonQuery(String databaseQuery){
        this.databaseQuery = databaseQuery;
        new AccessDatabaseNonQuery().execute();
    }


    public void executeQuery(String databaseQuery){
        this.databaseQuery = databaseQuery;
        new AccessDatabaseQuery().execute();
    }

    private List<String[]> convertResultSetToList(ResultSet rs, int columnCount) {
        List<String[]> table = new ArrayList<>();
        try {
            while( rs.next()) {
                String[] row = new String[columnCount];
                for( int iCol = 1; iCol <= columnCount; iCol++ ){
                    Object obj = rs.getObject( iCol );
                    row[iCol-1] = (obj == null) ?null:obj.toString();
                }
                table.add( row );
            }
        } catch (Exception e) {
            Log.d("Convert RS Exception: ", e.getMessage());
        }
        return table;
    }


    class AccessDatabaseQuery extends AsyncTask<Void, Void, Void> {
        private int columnCountAsync;
        private List<String[]> fetchedDataAsync;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(urlString, databaseUser, databasePassword);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(databaseQuery);

                this.columnCountAsync = resultSet.getMetaData().getColumnCount();
                this.fetchedDataAsync = convertResultSetToList(resultSet, columnCountAsync);
                connection.close();
                databaseQuery = "";
            } catch (Exception e) {
                Log.d("SQL QUERY ERROR: ", e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            ((DatabaseAccessCallback)context).QueryResponse(this.fetchedDataAsync);
            super.onPostExecute(unused);
        }
    }


    class AccessDatabaseNonQuery extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(urlString, databaseUser, databasePassword);
                Statement statement = connection.createStatement();
                statement.executeUpdate(databaseQuery);
                connection.close();
                databaseQuery = "";
            } catch (Exception e) {
                Log.d("SQL NONQUERY ERROR: ", e.toString());
                Log.d("SQL QUERY: ", databaseQuery);
                e.printStackTrace();
            }
            return null;
        }
    }

    //    class AccessDatabase1 extends AsyncTask<Void, Void, Void> {
//        String records1 = "", error = "";
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try {
//                Class.forName("com.mysql.jdbc.Driver");
//                Connection connection = DriverManager.getConnection("jdbc:mysql://163.44.242.10:3306/feqxsxpi_calsakay?characterEncoding=latin1","feqxsxpi_root", "UCC2021bsitKrazy");
//                Statement statement = connection.createStatement();
//                ResultSet resultSet = statement.executeQuery("SELECT * FROM sdo");
//
//                while(resultSet.next()){
//                    records1 += resultSet.getString("username") + "\n";
//                }
//
//            } catch (Exception e) {
//                error = e.toString();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void unused) {
//            tvShowDatabase.setText(records1);
//            if(error != ""){
//                tvShowDatabase.setText(error);
//            }
//            super.onPostExecute(unused);
//        }
//    }
}
