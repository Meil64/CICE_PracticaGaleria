package com.ccarrera.cameragallery

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class SharedPrefsManager(val activity: AppCompatActivity) {

    private val SPKEY_PHOTOPATHS = "SPKEY_PHOTOPATHS"

    public fun getPhotoPaths() : MutableList<String>
    {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val photoPaths = sharedPref
            .getStringSet(SPKEY_PHOTOPATHS, hashSetOf<String>())
                as MutableSet<String>

        return photoPaths.toMutableList()
    }

    public fun savePhotoPaths(photoPaths: List<String>)
    {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putStringSet(SPKEY_PHOTOPATHS, photoPaths.toSet())
            apply()
        }

        /*
        todo: en lugar de guardar el Set debería guardar el listado en json para conservar el orden de las fotos
        Perdona Kamil pero voy de culo y además así la app es más divertida :D
        */
    }
}