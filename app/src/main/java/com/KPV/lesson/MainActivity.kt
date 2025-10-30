package com.kpv.lesson

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kpv.lesson.entities.Note
import com.kpv.lesson.navigation.Keys
import com.kpv.lesson.navigation.NavigationIds
import com.kpv.lesson.navscreens.createnotes.CreateNote
import com.kpv.lesson.navscreens.noteslist.NotesList
import com.kpv.lesson.navscreens.regpage.RegPage
import com.kpv.lesson.utils.CreateURL
import java.util.LinkedList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val notesList = remember { mutableStateOf(LinkedList<Note>())  }
            val email = remember { mutableStateOf("") }
            NavHost(
                navController = navController,
                startDestination = CreateURL(
                    NavigationIds.REGISTRATION_PAGE.name,
                    listOf()
                )
            ){
                composable(route = CreateURL(
                    NavigationIds.REGISTRATION_PAGE.name,
                    listOf()
                )
                ){
                    RegPage(navController, email)
                }
                composable(
                    route = CreateURL(
                        NavigationIds.NOTES_LIST.name,
                        ArrayList(
                            listOf()
                        )
                    )

                ){
                    NotesList(navController, email, notesList)
                }
                composable(route = CreateURL(
                    NavigationIds.NOTES_CREATE.name,
                    listOf()
                )
                ){
                    CreateNote(navController, notesList)
                }
            }
        }
    }
}

