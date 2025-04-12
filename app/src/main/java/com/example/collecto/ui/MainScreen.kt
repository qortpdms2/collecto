package com.example.collecto.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.collecto.local.UrlManager
import com.example.collecto.model.Website

@Composable
fun MainScreen(onLogout:() -> Unit) {
    var inputUrl by remember { mutableStateOf("") }
    val websites = remember { mutableStateListOf<Website>() }
    val context = LocalContext.current
    val urlManager = remember { UrlManager(context) }

    LaunchedEffect(Unit) { websites.addAll(urlManager.loadUrls()) }
    Column(
        modifier = Modifier.fillMaxWidth().padding(28.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("\uD83D\uDCCC 저장한 웹사이트 목록", style =MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inputUrl,
            onValueChange = { inputUrl = it},
            label = { Text("URL 입력") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if(inputUrl.isNotBlank()){
                    val newSite = Website(inputUrl)
                    websites.add(newSite)
                    urlManager.saveUrls(websites)
                    inputUrl=""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("저장하기")
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn {
            items(websites) {
                site->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_MAIN, Uri.parse(site.url))
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                        }
                ) {
                    Text(
                        text = site.url,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

    Spacer(modifier = Modifier.height(24.dp))
    Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()){ Text("로그아웃") }
    }
}