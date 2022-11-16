package ca.georgiancollege.mdev1012_f2022_week10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Write a message to the database
        val database = Firebase.database


        val myRef = database.getReference("message")
        myRef.setValue("Hello, World!")


        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue()
                Log.i("firebase-value", "Value is: ${value.toString()}")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("firebase-error", "Failed to read value.", error.toException())
            }
        })
    }
}