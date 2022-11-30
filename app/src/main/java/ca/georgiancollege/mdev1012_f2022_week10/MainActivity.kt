package ca.georgiancollege.mdev1012_f2022_week10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    private lateinit var addTVShowFAB: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initializing
        database = Firebase.database.reference
        TVShows = mutableListOf<TVShow>()
        tvShowsAdapter = TVShowsAdapter(TVShows)

        tvShowsAdapter.onTVShowClick = { tvShow, position ->
            showCreateTVShowDialog(AlertAction.UPDATE, tvShow, position)
        }

        tvShowsAdapter.onTVShowSwipeLeft = { tvShow, position ->
            showCreateTVShowDialog(AlertAction.DELETE, tvShow, position)
        }

        initializeRecyclerView()
        initializeFAB()

        addTVShowEventListener(database)
    }

    private fun initializeFAB()
    {
        addTVShowFAB = findViewById(R.id.add_TV_Show_FAB)
        addTVShowFAB.setOnClickListener{
            showCreateTVShowDialog(AlertAction.ADD, null, null)
        }
    }

    private fun initializeRecyclerView()
    {
        val recyclerView: RecyclerView = findViewById(R.id.TVShow_Recycler_View)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = tvShowsAdapter
    }

    fun writeNewTVShow(tvShow: TVShow)
    {
        var id = TVShows.size.toString() //  generate new ID
        database.child("TVShows").child(id).setValue(tvShow)
    }

    fun updateTVShow(id: String, tvShow: TVShow)
    {
        database.child("TVShows").child(id).setValue(tvShow)
    }

    fun deleteTVShow(id: String, tvShow: TVShow?)
    {
        TVShows.remove(tvShow);
        var tempList = this.TVShows.toMutableList()
        database.child("TVShows").removeValue()
        TVShows.clear()
        for(item in tempList)
        {
            writeNewTVShow(item)
        }
        tvShowsAdapter.notifyDataSetChanged()
    }

    private fun showCreateTVShowDialog(alertAction: AlertAction, tvShow: TVShow?, position: Int?)
    {
        var dialogTitle: String = ""
        var positiveButtonTitle: String = ""
        var negativeButtonTitle: String = "Cancel"

        when(alertAction)
        {
            AlertAction.ADD -> {
                dialogTitle = getString(R.string.add_dialog_title)
                positiveButtonTitle = getString(R.string.add_tv_show)
            }
            AlertAction.UPDATE -> {
                dialogTitle = getString(R.string.update_dialog_title)
                positiveButtonTitle = getString(R.string.update_tv_show)
            }
            AlertAction.DELETE -> {
                dialogTitle = "Delete TV Show"
                positiveButtonTitle = "Delete TV Show"
            }
        }

        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.add_new_tv_show_item, null)
        builder.setTitle(dialogTitle)
        builder.setView(view)

        val tvShowTitleText = view.findViewById<TextView>(R.id.tv_show_title)
        val tvShowTitleEditText = view.findViewById<EditText>(R.id.TV_Show_Title_EditText)
        val studioTitleText = view.findViewById<TextView>(R.id.Studio_Title)
        val studioTitleEditText = view.findViewById<EditText>(R.id.Studio_Name_EditText)

        when(alertAction)
        {
            AlertAction.ADD -> {
                builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
                    dialog.dismiss()
                    val newTVShow = TVShow(tvShowTitleEditText.text.toString(),
                                           studioTitleEditText.text.toString())
                    writeNewTVShow(newTVShow)
                }

            }
            AlertAction.UPDATE -> {
                if(tvShow != null)
                {
                    tvShowTitleEditText.setText(tvShow?.title)
                    studioTitleEditText.setText(tvShow?.studio)
                }
                builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
                    dialog.dismiss()
                    val newTVShow = TVShow(tvShowTitleEditText.text.toString(),
                        studioTitleEditText.text.toString())
                    updateTVShow(position.toString(),newTVShow)
                }
            }
            AlertAction.DELETE -> {
                tvShowTitleText.setText("Are you Sure?")
                tvShowTitleEditText.isVisible = false
                studioTitleEditText.isVisible = false
                studioTitleText.isVisible = false
                builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
                    dialog.dismiss()
                    deleteTVShow(position.toString(), tvShow)
                }
                builder.setNegativeButton(negativeButtonTitle) { dialog, _ ->
                    dialog.cancel()
                }
            }

        }
        builder.create().show()
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