package com.kpv.lesson.navscreens.noteslist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kpv.lesson.utils.CreateURL
import com.kpv.lesson.entities.Note
import com.kpv.lesson.ui.theme.DesignConstants
import com.kpv.lesson.R
import com.kpv.lesson.navigation.NavigationIds
import java.util.LinkedList

@Composable
fun NotesList(
    navController: NavController,
    email: MutableState<String>,
    notesList: MutableState<LinkedList<Note>>
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            Spacer(Modifier.padding(DesignConstants.DistanceBetweenElementsNotes))

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Text(
                        text = stringResource(R.string.notes),
                        fontSize = DesignConstants.FontColumnName(),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                item {
                    EmailText(email)
                    Spacer(modifier = Modifier.padding(8.dp))
                }

                itemsIndexed(notesList.value) { index, note ->
                    Row(
                        modifier = Modifier
                            .background(
                                if (index % 2 == 0) Color(0xfff5f5f5) else Color.Transparent
                            )
                            .padding(12.dp)
                            .fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = note.header,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            if (note.body.isNotEmpty()) {
                                Text(
                                    text = note.body,
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        navController.navigate(CreateURL(
                            NavigationIds.NOTES_CREATE.name,
                            listOf()
                        ))
                    }
                ) {
                    Text(stringResource(R.string.create_note))
                }
            }
        }
    }
}

@Composable
private fun EmailText(
    email: MutableState<String>
) {
    Row {
        Text(
            text = email.value.ifEmpty { stringResource(R.string.not_found_email) },
            textAlign = TextAlign.Center
        )
    }
}