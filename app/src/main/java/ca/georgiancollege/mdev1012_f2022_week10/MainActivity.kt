package ca.georgiancollege.mdev1012_f2022_week10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity()
{
    private lateinit var database: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Write a message to the database
        //val database = Firebase.database

        database = Firebase.database.reference

        writeNewTVShow("0", "House of the Dragon","HBO")
        writeNewTVShow("1", "Andor","Disney")

        addTVShowEventListener(database)
    }

    fun writeNewTVShow(id: String, title: String, studio: String) {
        val tvShow = TVShow(title, studio)

        database.child("TVShows").child(id).setValue(tvShow)
    }

    private fun addTVShowEventListener(dbReference: DatabaseReference) {
        val TVShowListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                val tvShow = dataSnapshot.getValue()
                Log.i("tvShow-value", "TVShow is: ${tvShow.toString()}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("cancelledTVShow", "loadTVShow:onCancelled", databaseError.toException())
            }
        }
        dbReference.addValueEventListener(TVShowListener)
    }
}