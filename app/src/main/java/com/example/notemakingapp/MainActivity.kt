package com.example.notemakingapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.notemakingapp.database.NoteDatabase
import com.example.notemakingapp.databinding.ActivityMainBinding
import com.example.notemakingapp.noteViewModel.NoteViewModel
import com.example.notemakingapp.noteViewModel.NoteViewModelFactory
import com.example.notemakingapp.repository.NoteRepository

class MainActivity : AppCompatActivity() {

    lateinit var noteViewModel: NoteViewModel
    lateinit var binding :  ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewModel()


    }

    private fun setUpViewModel() {
        val noteRepository =  NoteRepository(NoteDatabase(this))

        val  viewModelProviderFactory = NoteViewModelFactory(application,
            noteRepository
        )
        noteViewModel =  ViewModelProvider(
            this,
            viewModelProviderFactory


        ).get(NoteViewModel::class.java)
    }
}