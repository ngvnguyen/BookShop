package com.ptit.feature.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ptit.feature.viewmodel.Action
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.SurfaceOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navigateBack:()->Unit
){
    Scaffold(
        containerColor = SurfaceDarker,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "About",
                        fontSize = FontSize.MEDIUM
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack
                    ) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "back",
                            tint = IconSecondary
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ){padding->
        LazyColumn(
            modifier = Modifier
                .background(SurfaceLighter)
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding()
                )
        ) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Image(
                        painter = painterResource(Resources.Image.AppLogo),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "BookShop",
                        fontSize = FontSize.MEDIUM,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Version 1.0.0",
                        fontSize = FontSize.SMALL,
                        color = Color.Gray
                    )
                    Text(
                        text = "Connecting readers with knowledge",
                        fontSize = FontSize.REGULAR,
                        fontWeight = FontWeight.W400
                    )
                }
            }
            item{
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .background(SurfaceOrange)
                        .padding(horizontal = 12.dp)
                ) {
                    Spacer(modifier = Modifier.height(0.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = SurfaceLighter
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Our Story",
                                fontSize = FontSize.EXTRA_REGULAR,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text="Founded in 2025, we started with a simple dream: making quality books accessible to everyone. From a small shop, we're grown into an online platform serving thousands of reader nationwide.",
                                fontSize = FontSize.REGULAR
                            )
                            Text(
                                text = "With a diverse collection spanning literature, business, self-help, and children's books, we're committed to delivering the best reading experience for you.",
                                fontSize = FontSize.REGULAR
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xffd97706))
                            .padding(vertical = 12.dp, horizontal = 24.dp)

                    ) {
                        Column() {
                            Text(
                                text = "2K+",
                                fontSize = FontSize.LARGE,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Customer",
                                fontSize = FontSize.REGULAR,
                                color = Color.White
                            )
                        }
                        Column() {
                            Text(
                                text = "10K+",
                                fontSize = FontSize.LARGE,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Books",
                                fontSize = FontSize.REGULAR,
                                color = Color.White
                            )
                        }
                        Column() {
                            Text(
                                text = "4.8/5",
                                fontSize = FontSize.LARGE,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Rating",
                                fontSize = FontSize.REGULAR,
                                color = Color.White
                            )
                        }
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = SurfaceLighter
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Contact Us",
                                fontSize = FontSize.EXTRA_REGULAR,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xfffef3c7)),
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        painter = painterResource(Resources.Icon.Mail),
                                        contentDescription = "Email",
                                        tint = Color(0xffea580c),

                                    )
                                }
                                Column {
                                    Text(
                                        text = "Email",
                                        fontSize = FontSize.SMALL,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "bookshop.dpdns.org@email.com",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = FontSize.REGULAR
                                    )
                                }
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xfffef3c7)),
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        painter = painterResource(Resources.Icon.Phone),
                                        contentDescription = "Hotline",
                                        tint = Color(0xffea580c),

                                        )
                                }
                                Column {
                                    Text(
                                        text = "Hotline",
                                        fontSize = FontSize.SMALL,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "0987678902",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = FontSize.REGULAR
                                    )
                                }
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xfffef3c7)),
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        painter = painterResource(Resources.Icon.Domain),
                                        contentDescription = "Address",
                                        tint = Color(0xffea580c),

                                    )
                                }
                                Column {
                                    Text(
                                        text = "Address",
                                        fontSize = FontSize.SMALL,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "96A Đ. Trần Phú, P. Mộ Lao, Hà Đông, Hà Nội, Việt Nam",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = FontSize.REGULAR
                                    )
                                }
                            }

                        }
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = SurfaceLighter
                        )
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Follow Us",
                                fontSize = FontSize.EXTRA_REGULAR,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable(onClick = {})
                                    .background(Color.Blue)
                                    .padding(8.dp)
                            ){
                                Text(
                                    text = "Facebook",
                                    color = Color.White
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(0.dp))
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xff1f2937))
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "© 2025 BookShop. All rights reserved.",
                        color = Color.White.copy(alpha=0.6f),
                        fontWeight = FontWeight.W100,
                        fontSize = FontSize.EXTRA_REGULAR
                    )
                    Text(
                        text = "Bringing knowledge to every home",
                        color = Color.Gray,
                        fontWeight = FontWeight.W100,
                        fontSize = FontSize.REGULAR
                    )
                }
            }
        }
    }
}