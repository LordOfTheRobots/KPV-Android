package com.kpv.lesson.navscreens.createnotes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kpv.lesson.entities.Note
import com.kpv.lesson.ui.theme.DesignConstants
import com.kpv.lesson.R
import java.util.LinkedList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNote(
    navController: NavController,
    notesList: MutableState<LinkedList<Note>>
) {
    val headerState = remember { mutableStateOf(TextFieldValue()) }
    val bodyState = remember { mutableStateOf(TextFieldValue()) }
    val headerError = remember { mutableStateOf("") }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Spacer(modifier = Modifier.padding(DesignConstants.PaddingForEdge))

                TextField(
                    value = headerState.value,
                    onValueChange = {
                        headerState.value = it
                        headerError.value = ""
                    },
                    placeholder = { Text(stringResource(R.string.enter_note_title)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = headerError.value.isNotEmpty(),
                    singleLine = true
                )

                if (headerError.value.isNotEmpty()) {
                    Text(
                        text = headerError.value,
                        fontSize = DesignConstants.FontSizeNotEmptyText(),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.padding(8.dp))

                TextField(
                    value = bodyState.value,
                    onValueChange = { bodyState.value = it },
                    placeholder = { Text(stringResource(R.string.enter_note_text)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    singleLine = false
                )
            }
            val titleCannotBeEmpty = stringResource(R.string.title_cannot_be_empty)
            Button(
                onClick = {
                    val header = headerState.value.text.trim()
                    val body = bodyState.value.text.trim()

                    if (header.isEmpty()) {
                        headerError.value = titleCannotBeEmpty
                    } else {
                        val newNote = Note(header, body)
                        notesList.value.add(newNote)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save_note))
            }
        }
    }
}