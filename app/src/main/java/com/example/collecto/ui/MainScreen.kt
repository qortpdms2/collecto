package com.example.collecto.ui

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.collecto.model.Website

@Composable
fun MainScreen(
    websites: List<Website>,
    folders: List<String>,
    selectedFolder: String,
    onSelectFolder: (String)-> Unit,
    onAddLinkClick: ()->Unit,
    onItemClick: (Website)->Unit,
    onItemDelete:(Website)->Unit,
    onProfileclick:()->Unit
) {
    val context= LocalContext.current
Box(modifier = Modifier.fillMaxSize()){
    Column(modifier = Modifier.fillMaxSize().padding(40.dp)) {
        Text(
            text = "\uD83D\uDCDA 내 콜레토",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            folders.forEach {
                folder ->
                FilterChip(
                    selected = folder == selectedFolder,
                    onClick = {onSelectFolder(folder)},
                    label = { Text(folder) },
                    modifier = Modifier
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(websites) {
                site->
                WebSiteCard(
                    site = site,
                    onClick = {onItemClick(site)},
                    onDelete = {onItemDelete(site)}
                )
            }
            item { AddLinkCard(onClick = onAddLinkClick) }
        }
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            HorizontalDivider(Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.LightGray)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                    horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { onProfileclick() }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "마이페이지"
                    )
            }

            }
        }
    }
}
}

@Composable
fun WebSiteCard(site:Website, onClick:()->Unit,onDelete:()->Unit){
    val displayTitle = site.title.ifBlank {
        try{
            Uri.parse(site.url).host ?: "사이트 제목 없음"
        }catch (e:Exception){
            "사이트 제목 없음"
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() }
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Box(
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ){
            val bgColor = if("naver" in site.url) Color(0xFFB0E200) else Color.LightGray
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = bgColor,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = getDomainInitial(site.url),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }

    Text(
        text = displayTitle,
        fontSize = 13.sp,
        color = Color.Gray,
        modifier = Modifier.padding(top = 4.dp),
        textAlign = TextAlign.Center
    )
    Text(
        text="❌",
        color = Color.Red,
        fontSize = 12.sp,
        modifier = Modifier
            .padding(top = 4.dp)
            .clickable { onDelete() },
        textAlign = TextAlign.Right
    )
}

@Composable
fun AddLinkCard(onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFF0F0F0),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text("+ 링크 저장", fontWeight = FontWeight.Medium, fontSize = 16.sp)
        }
    }
}

fun getDomainInitial(url:String):String {
    return try {
        Uri.parse(url).host?.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
    } catch (e:Exception) {
        "?"
    }
}

fun getDomainName(url: String):String{
    return when{
        "naver" in url -> "네이버"
        "google" in url -> "Google"
        "youtube" in url -> "YouTube"
        else -> Uri.parse(url).host ?: "사이트"
    }
}