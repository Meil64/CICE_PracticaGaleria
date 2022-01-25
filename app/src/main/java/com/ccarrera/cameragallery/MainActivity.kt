package com.ccarrera.cameragallery

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ccarrera.cameragallery.adapter.PhotoAdapter
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var photoPaths: MutableList<String>
    private lateinit var spManager: SharedPrefsManager
    private lateinit var photoAdapter: PhotoAdapter

    private var lastPhotoTakenUri: Uri? = null

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.
        TakePicture()) { isSuccess ->
            if (isSuccess && lastPhotoTakenUri != null){
                addImageUri(lastPhotoTakenUri)

                //Borro la uri cacheada ahora que ya la he almacenado
                lastPhotoTakenUri = null
            }
    }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { addImageUri(uri) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setButtons()

        spManager = SharedPrefsManager(this)
        photoPaths = spManager.getPhotoPaths()

        setRecycler()
    }

    private fun setButtons(){

        findViewById<CardView>(R.id.cameraButton).setOnClickListener {
            takeImage()
        }

        findViewById<CardView>(R.id.libraryButton).setOnClickListener {
            selectImageFromGalleryResult.launch("image/*")
        }
    }

    private fun takeImage() {

        //Creo un fichero temporal auxiliar en el cache de la app para obtener una uri válida
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).
            apply {
                createNewFile()
                deleteOnExit()
            }
        val uri = FileProvider.getUriForFile(applicationContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile)

        lastPhotoTakenUri = uri
        takeImageResult.launch(uri)
    }

    private fun setRecycler() {

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        photoAdapter = PhotoAdapter(photoPaths, this)

        photoAdapter.setOnItemClickListener(object : PhotoAdapter.OnItemClickListener{
            override fun onDeleteClick(position: Int) {
                photoPaths.removeAt(position)
                photoAdapter.deleteListItem(photoPaths, position)
                spManager.savePhotoPaths(photoPaths)
            }
        })

        recyclerView.adapter = photoAdapter
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false)

        recyclerView.addItemDecoration(CirclePagerIndicatorDecoration())
    }

    private fun addImageUri(imageUri : Uri?) {
        photoPaths.add(imageUri.toString())
        photoAdapter.addListItem(photoPaths, photoPaths.lastIndex)
        spManager.savePhotoPaths(photoPaths)
    }
}