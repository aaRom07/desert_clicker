package com.example.dessertclicker

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dessertclicker.data.DessertUiState
import com.example.dessertclicker.ui.theme.DessertClickerTheme
import com.example.dessertclicker.ui.DessertViewModel

// Tag for logging
private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate Called")

        setContent {
            DessertClickerTheme {
                DessertClickerApp()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart Called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume Called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart Called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy Called")
    }
}


private fun shareDessertSalesInfo(context: Context, totalDesserts: Int, totalRevenue: Int) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            context.getString(R.string.share_text, totalDesserts, totalRevenue)
        )
        type = "text/plain"
    }

    val chooserIntent = Intent.createChooser(shareIntent, null)

    try {
        startActivity(context, chooserIntent, null)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            context.getString(R.string.sharing_not_available),
            Toast.LENGTH_LONG
        ).show()
    }
}

@Composable
private fun DessertClickerApp(
    viewModel: DessertViewModel = viewModel()
) {
    val uiState by viewModel.dessertUiState.collectAsState()
    DessertClickerApp(
        uiState = uiState,
        onDessertTapped = viewModel::onDessertClicked
    )
}

@Composable
private fun DessertClickerApp(
    uiState: DessertUiState,
    onDessertTapped: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            val context = LocalContext.current
            AppBar(
                onShareButtonTapped = {
                    shareDessertSalesInfo(
                        context = context,
                        totalDesserts = uiState.dessertsSold,
                        totalRevenue = uiState.revenue
                    )
                }
            )
        }
    ) { contentPadding ->
        DessertScreenContent(
            totalRevenue = uiState.revenue,
            totalDesserts = uiState.dessertsSold,
            dessertImageId = uiState.currentDessertImageId,
            onDessertTapped = onDessertTapped,
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@Composable
private fun AppBar(
    onShareButtonTapped: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier.padding(start = 16.dp),
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h6,
        )
        IconButton(
            onClick = onShareButtonTapped,
            modifier = Modifier.padding(end = 16.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = stringResource(R.string.share),
                tint = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Composable
fun DessertScreenContent(
    totalRevenue: Int,
    totalDesserts: Int,
    @DrawableRes dessertImageId: Int,
    onDessertTapped: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.bakery_back),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(dessertImageId),
                    contentDescription = null,
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                        .align(Alignment.Center)
                        .clickable { onDessertTapped() },
                    contentScale = ContentScale.Crop,
                )
            }
            SalesInfo(totalRevenue = totalRevenue, totalDesserts = totalDesserts)
        }
    }
}

@Composable
private fun SalesInfo(
    totalRevenue: Int,
    totalDesserts: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White),
    ) {
        DessertsSoldInfo(totalDesserts)
        RevenueInfo(totalRevenue)
    }
}

@Composable
private fun RevenueInfo(totalRevenue: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.total_revenue),
            style = MaterialTheme.typography.h4
        )
        Text(
            text = "$${totalRevenue}",
            textAlign = TextAlign.Right,
            style = MaterialTheme.typography.h4
        )
    }
}

@Composable
private fun DessertsSoldInfo(totalDesserts: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.dessert_sold),
            style = MaterialTheme.typography.h6
        )
        Text(
            text = totalDesserts.toString(),
            style = MaterialTheme.typography.h6
        )
    }
}
