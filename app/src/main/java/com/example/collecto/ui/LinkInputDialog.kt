package com.example.collecto.ui

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.collecto.model.Website
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup


@Composable
fun LinkInputDialog(
    onDismiss:() -> Unit,
    onSave: (Website) -> Unit
) {
    var urlInput by remember { mutableStateOf("") }
    var folderInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("링크 저장") },
        text = {
            Column {
                OutlinedTextField(
                    value =urlInput,
                    onValueChange = {urlInput=it},
                    label = { Text("URL 입력") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = folderInput,
                    onValueChange = {folderInput=it},
                    label = { Text("폴더명 입력") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (urlInput.isNotBlank()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val metadata = extractSiteMetadata(urlInput.trim())
                        val newWebsite = Website(
                            url = urlInput.trim(),
                            folder = folderInput.trim().ifBlank { "기타" },
                            title =  metadata.first,
                            imageUrl =  metadata.second
                        )
                        onSave(newWebsite)
                    }
                    onDismiss()
                }
            }){
                Text("저장")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

suspend fun extractSiteMetadata(url:String):Pair<String,String> {
    return try{
        val doc=Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
            .get()

        val title=doc.select("meta[property=og:title]").attr("content")
            .ifBlank { doc.selectFirst("title")?.text() ?: "" }

        var image = doc.select("meta[property=og:image]").attr("content")
            .ifBlank {
                doc.select("link[rel=icon], link[rel*=icon]").firstOrNull()?.attr("href") ?: ""
            }

        if (image.isNotBlank() && !image.startsWith("http")) {
            val baseUri = Uri.parse(url)
            image = "${baseUri.scheme}://${baseUri.host}${if (image.startsWith("/")) image else "/$image"}"
        }

        Pair(
            title.ifBlank { "사이트제목없" },
            image
        )
    } catch (e:Exception) {
        e.printStackTrace()
        Pair("사이트 제목 없음","")
    }
}