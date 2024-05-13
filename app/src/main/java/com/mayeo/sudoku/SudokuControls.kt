package com.mayeo.sudoku

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@Composable
fun SudokuControls(
    numberInputType: NumberInputType,
    modifier: Modifier = Modifier,
    onNumberInputTypeChanged: (NumberInputType) -> Unit,
) {
    Row(
        modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Box(modifier = Modifier
            .wrapContentSize()
            .aspectRatio(1f)
            .clip(CutCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primary.copy(0.2f))) {
            ControlBox()
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .offset(16.dp)
        ) {
            val viewModel = LocalSudokuViewModel.current
            Button(
                onClick = {
                    viewModel.clear()
                },
                modifier = modifier.fillMaxWidth()
            ) {
                Text(text = "Deselect")
            }

            Spacer(modifier = Modifier.size(16.dp))

            val inputType = viewModel.numberInputType().collectAsState().value
            Button(
                onClick = {
                    viewModel.delete()
                },
                modifier = modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Delete,
                    contentDescription = "Delete")
            }

            Spacer(modifier = Modifier.size(16.dp))

            NumberInputTypeButton(
                inputType == NumberInputType.Actual,
                onSelection = {
                    viewModel.changeNumberType(NumberInputType.Actual)
                }
            ) {
                Text(
                    text = "9",
                    fontWeight = FontWeight(600),
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(18f, TextUnitType.Sp),
                    modifier = modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            NumberInputTypeButton(
                inputType == NumberInputType.Note,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onSelection = {
                    viewModel.changeNumberType(NumberInputType.Note)
                }
            ) {
                Text(
                    text = "9",
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(12f, TextUnitType.Sp))
            }

        }
    }
}

@Composable
fun NumberInputTypeButton(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onSelection: () -> Unit,
    content: @Composable () -> Unit,
) {
    val backgroundColor =
        animateColorAsState(
            targetValue = if(isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            else Color.Unspecified).value
    val borderColor = backgroundColor.copy(backgroundColor.alpha + 0.1f)

    Row(
        modifier
            .clip(RoundedCornerShape(5.dp))
            .clickable {
                onSelection()
            }
            .border(1.dp, borderColor)
            .background(backgroundColor)
            .size(32.dp)
            .padding(2.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
fun ControlBox() {
    val viewModel = LocalSudokuViewModel.current
    FixedGrid(columnCount = SUBGRID_COLS, Modifier.clickable {}) {
        repeat(MAX_VAL) {
            val number = it + 1
            NumberPadButton(number = number, viewModel.numberInputType().collectAsState().value) {
                viewModel.onNumberPressed(number)
            }
        }
    }
}

@Composable
fun NumberPadButton(
    number: Int,
    inputType: NumberInputType,
    onClick: (Int) -> Unit
) {
    Box(Modifier.padding(5.dp)) {
        Button(modifier = Modifier.aspectRatio(1f),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(2.dp),
            onClick = {onClick(number)}
        ) {
            when (inputType){
                NumberInputType.Actual -> {
                    Text(
                        text = number.toString(),
                        fontSize = TextUnit(12f, TextUnitType.Sp)
                    )
                }
                NumberInputType.Note -> {
                    Box{
                        Text(
                            text = number.toString(),
                            fontSize = TextUnit(12f, TextUnitType.Sp),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Preview(widthDp = 500, heightDp = 300)
@Composable
fun SudokuControlsPreview() {
    SudokuControls(NumberInputType.Actual){

    }
}