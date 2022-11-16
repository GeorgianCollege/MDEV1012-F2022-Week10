package ca.georgiancollege.mdev1012_f2022_week10

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TVShowsAdapter(private val dataSet: MutableList<TVShow>): RecyclerView.Adapter<TVShowsAdapter.ViewHolder>()
{
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val title: TextView
        val studio: TextView

        init{

        }
    }
}