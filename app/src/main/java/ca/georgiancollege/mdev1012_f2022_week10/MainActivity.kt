package ca.georgiancollege.mdev1012_f2022_week10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity()
{
    // Private Instance Members
    private lateinit var database: DatabaseReference
    private lateinit var TVShows: MutableList<TVShow>
    private lateinit var tvShowsAdapter: TVShowsAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initializing
        database = Firebase.database.reference
        TVShows = mutableListOf<TVShow>()
        tvShowsAdapter = TVShowsAdapter(TVShows)

        initializeRecyclerView()

        addTVShowEventListener(database)
    }

    private fun initializeRecyclerView()
    {
        val recyclerView: RecyclerView = findViewById(R.id.TVShow_Recycler_View)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = tvShowsAdapter
    }

    fun writeNewTVShow(id: String, title: String, studio: String) {
        val tvShow = TVShow(title, studio)

        database.child("TVShows").child(id).setValue(tvShow)
    }

    private fun addTVShowEventListener(dbReference: DatabaseReference) {
        val TVShowListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                TVShows.clear()
                val tvShowDB = dataSnapshot.child("TVShows").children

                for(tvShow in tvShowDB)
                {
                    var newShow = tvShow.getValue(TVShow::class.java)

                    if (newShow != null) {
                        TVShows.add(newShow)
                        tvShowsAdapter.notifyDataSetChanged()
                    }
                }

                // output to Logcat for testing / debugging
                for(tvShow in TVShows)
                {
                    Log.i("child", "tvShow: $tvShow")
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("cancelledTVShow", "loadTVShow:onCancelled", databaseError.toException())
            }
        }
        dbReference.addValueEventListener(TVShowListener)
    }
}