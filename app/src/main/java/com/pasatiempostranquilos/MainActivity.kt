package com.pasatiempostranquilos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Cream = Color(0xFFFFF8E8)
private val Ink = Color(0xFF17324D)
private val Teal = Color(0xFF155B51)
private val Gold = Color(0xFFFFD166)
private val FoundGreen = Color(0xFFB7E4C7)
private val White = Color(0xFFFFFFFF)

private data class Cell(val row: Int, val column: Int)

private data class WordTarget(
    val word: String,
    val row: Int,
    val column: Int,
    val rowStep: Int,
    val columnStep: Int,
)

private val wordTargets = listOf(
    WordTarget("SOL", row = 0, column = 0, rowStep = 0, columnStep = 1),
    WordTarget("LUZ", row = 0, column = 4, rowStep = 1, columnStep = 0),
    WordTarget("PAZ", row = 4, column = 1, rowStep = 0, columnStep = 1),
)

private val puzzle = buildList {
    repeat(5) { row ->
        add(buildList {
            repeat(5) { column ->
                val target = wordTargets.firstOrNull { wordTarget ->
                    (0 until wordTarget.word.length).any { index ->
                        wordTarget.row + wordTarget.rowStep * index == row &&
                            wordTarget.column + wordTarget.columnStep * index == column
                    }
                }
                val letter = if (target == null) {
                    listOf('A', 'E', 'I', 'M', 'R', 'T', 'N', 'O')[(row * 3 + column * 5) % 8]
                } else {
                    val index = if (target.rowStep == 0) column - target.column else row - target.row
                    target.word[index]
                }
                add(letter)
            }
        })
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
                    WordSearchScreen()
                }
            }
        }
    }
}

@Composable
private fun WordSearchScreen() {
    var firstCell by remember { mutableStateOf<Cell?>(null) }
    var chosenCells by remember { mutableStateOf(emptyList<Cell>()) }
    var foundWords by remember { mutableStateOf(emptySet<String>()) }
    var message by remember { mutableStateOf("Toca la primera letra y luego la última.") }

    fun resetPuzzle() {
        firstCell = null
        chosenCells = emptyList()
        foundWords = emptySet()
        message = "Toca la primera letra y luego la última."
    }

    fun selectCell(cell: Cell) {
        val start = firstCell
        if (start == null) {
            firstCell = cell
            chosenCells = listOf(cell)
            message = "Ahora toca la última letra de la palabra."
            return
        }

        val dr = cell.row - start.row
        val dc = cell.column - start.column
        val isStraightLine = dr == 0 || dc == 0
        val steps = maxOf(kotlin.math.abs(dr), kotlin.math.abs(dc))
        val path = if (isStraightLine && steps > 0) {
            val rowStep = dr.compareTo(0)
            val columnStep = dc.compareTo(0)
            (0..steps).map { step -> Cell(start.row + rowStep * step, start.column + columnStep * step) }
        } else {
            listOf(start)
        }
        val selectedWord = path.joinToString(separator = "") { puzzle[it.row][it.column].toString() }
        val match = wordTargets.firstOrNull { target ->
            target.word !in foundWords && (selectedWord == target.word || selectedWord.reversed() == target.word)
        }

        if (match != null) {
            foundWords = foundWords + match.word
            chosenCells = path
            message = if (foundWords.size == wordTargets.size) {
                "¡Muy bien! Encontraste todas las palabras."
            } else {
                "¡Encontraste ${match.word}! Busca otra palabra."
            }
            firstCell = null
        } else {
            firstCell = cell
            chosenCells = listOf(cell)
            message = "Esa selección no forma una palabra. Elige otra letra inicial."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Pasatiempos Tranquilos",
            color = Ink,
            fontSize = 28.sp,
            lineHeight = 34.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(text = "Sopa de letras", color = Teal, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Encuentra estas palabras. Pueden estar en horizontal o vertical.",
            color = Ink,
            fontSize = 18.sp,
            lineHeight = 25.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(14.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
            wordTargets.forEach { target ->
                Text(
                    text = if (target.word in foundWords) "✓ ${target.word}" else target.word,
                    color = if (target.word in foundWords) Teal else Ink,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            puzzle.forEachIndexed { rowIndex, row ->
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    row.forEachIndexed { columnIndex, letter ->
                        val cell = Cell(rowIndex, columnIndex)
                        val selected = cell in chosenCells
                        val partOfFoundWord = wordTargets.any { target ->
                            target.word in foundWords && (0 until target.word.length).any { index ->
                                Cell(target.row + target.rowStep * index, target.column + target.columnStep * index) == cell
                            }
                        }
                        val cellColor = when {
                            partOfFoundWord -> FoundGreen
                            selected -> Gold
                            else -> White
                        }
                        Button(
                            onClick = { selectCell(cell) },
                            modifier = Modifier.size(48.dp),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = cellColor,
                                contentColor = Ink,
                            ),
                        ) {
                            Text(
                                text = letter.toString(),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.semantics {
                                    contentDescription = "Fila ${rowIndex + 1}, columna ${columnIndex + 1}, letra $letter"
                                    if (selected || partOfFoundWord) {
                                        stateDescription = if (partOfFoundWord) "Letra de una palabra encontrada" else "Letra seleccionada"
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = message,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEAF2F0), RoundedCornerShape(12.dp))
                .padding(14.dp)
                .semantics { liveRegion = LiveRegionMode.Polite },
            color = Ink,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(14.dp))
        Button(
            onClick = { resetPuzzle() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Teal, contentColor = White),
        ) {
            Text("Empezar de nuevo", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = "Prototipo sin anuncios ni conexión a internet",
                color = Ink,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp),
            )
        }
    }
}
