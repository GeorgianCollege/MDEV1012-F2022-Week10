package ca.georgiancollege.mdev1012_f2022_week10

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TVShowsAdapter(private val dataSet: MutableList<TVShow>):
    RecyclerView.Adapter<TVShowsAdapter.ViewHolder>()
{
    var onTVShowClick: ((TVShow, position: Int) -> Unit)? = null
    var onTVShowSwipeLeft: ((TVShow, position: Int) -> Unit)? = null

    // For Each row we define a Cell
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val title: TextView
        val studio: TextView

        init
        {
            title = view.findViewById(R.id.tv_show_title)
            studio = view.findViewById(R.id.studio)

            view.setOnClickListener{
                onTVShowClick?.invoke(dataSet[adapterPosition], adapterPosition)
            }

            view.setOnTouchListener(object : OnSwipeTouchListener(view.context){
                override fun onSwipeLeft() {
                    super.onSwipeLeft()
                    onTVShowSwipeLeft?.invoke(dataSet[adapterPosition], adapterPosition)
                }
            })
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = dataSet[position].title // index of the list
        viewHolder.studio.text = dataSet[position].studio
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}


