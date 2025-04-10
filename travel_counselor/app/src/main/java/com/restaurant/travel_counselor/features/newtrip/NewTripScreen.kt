package com.restaurant.travel_counselor.features.newtrip

import RequiredTextField
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurant.travel_counselor.shared.components.MyDatePickerField
import com.restaurant.travel_counselor.shared.components.RequiredNumberField
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTripScreen(onNavigateTo: (String) -> Unit) {
    val viewModel: NewTripViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState()
    val ctx = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("New Trip") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RequiredTextField(
                label = "Destination",
                value = uiState.value.destination,
                onValueChange = viewModel::onDestinationChange
            )

            TripTypeSelector(
                selectedType = uiState.value.tripType,
                onTypeChange = viewModel::onTripTypeChange
            )

            val sdf = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
            val startDateMillis = remember(uiState.value.startDate) {
                try {
                    sdf.parse(uiState.value.startDate)?.time
                } catch (e: Exception) {
                    null
                }
            }

            MyDatePickerField(
                label = "Start Date",
                date = uiState.value.startDate,
                onDateChange = viewModel::onStartDateChange,
                minDate = System.currentTimeMillis()
            )

            MyDatePickerField(
                label = "End Date",
                date = uiState.value.endDate,
                onDateChange = viewModel::onEndDateChange,
                minDate = startDateMillis
            )

            RequiredNumberField(
                label = "Budget (R$)",
                value = uiState.value.budget,
                onValueChange = viewModel::onBudgetChange
            )

            Button(onClick = {
                if (viewModel.saveTrip()) {
                    Toast.makeText(ctx, "Saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(ctx, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
fun TripTypeSelector(selectedType: String, onTypeChange: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Trip Type:")
        RadioButtonWithLabel("Lazer", selectedType, onTypeChange)
        RadioButtonWithLabel("Trabalho", selectedType, onTypeChange)
    }
}

@Composable
fun RadioButtonWithLabel(label: String, selected: String, onSelect: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected == label,
            onClick = { onSelect(label) }
        )
        Text(text = label)
    }
}
