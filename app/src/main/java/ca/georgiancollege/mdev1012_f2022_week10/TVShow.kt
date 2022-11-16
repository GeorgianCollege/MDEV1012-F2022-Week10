package ca.georgiancollege.mdev1012_f2022_week10

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class TVShow(
    var title: String? = "",
    var studio: String? = ""
){
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "studio" to studio
        )
    }
}
