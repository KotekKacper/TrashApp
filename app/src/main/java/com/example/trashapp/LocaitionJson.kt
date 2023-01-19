package com.example.trashapp

import android.app.ProgressDialog

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class LocaitionJson : AsyncTask<String?, String?, String?>() {

        override fun onPreExecute() {

        }

         override fun doInBackground(vararg p0: String?): String? {
            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null
            try {
                val url = URL(p0[0])
                connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val stream: InputStream = connection.getInputStream()
                reader = BufferedReader(InputStreamReader(stream))
                val buffer = StringBuffer()
                var line = ""
                while (reader.readLine().also { line = it } != null) {
                    buffer.append(
                        """
                        $line
                        
                        """.trimIndent()
                    )
                    Log.d("Response: ", "> $line") //here u ll get whole response...... :-)
                }
                return buffer.toString()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (connection != null) {
                    connection.disconnect()
                }
                try {
                    if (reader != null) {
                        reader.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

        }
}