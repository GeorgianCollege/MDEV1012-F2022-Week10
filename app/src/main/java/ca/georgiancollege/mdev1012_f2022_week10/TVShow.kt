package ca.georgiancollege.mdev1012_f2022_week10

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class TVShow(
    var id: String? = "",
    var title: String? = "",
    var studio: String? = ""
){
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to id,
            "title" to title,
            "studio" to studio
        )
    }
}
