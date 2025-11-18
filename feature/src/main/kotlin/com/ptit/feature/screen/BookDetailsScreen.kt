package com.ptit.feature.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.ptit.data.DisplayResult
import com.ptit.feature.viewmodel.BookDetailsViewModel
import com.ptit.shared.Alpha
import com.ptit.shared.ButtonSecondary
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources
import com.ptit.shared.Surface
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.TextSecondary
import com.ptit.shared.bebasNeueFont
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.LoadingCard
import com.ptit.shared.component.PrimaryButton
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    modifier: Modifier = Modifier,
    navigateBack:()->Unit,
    navigateToCheckOut:()->Unit
){
    val viewModel = koinViewModel<BookDetailsViewModel>()
    val bookState = viewModel.book
    val context = LocalContext.current

    Scaffold(
        containerColor = SurfaceDarker,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (!bookState.isSuccess()) "Book Details" else bookState.getSuccessData().name,
                        fontSize = FontSize.MEDIUM,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back Icon"
                        )
                    }
                }
            )
        }
    ) { padding->
        bookState.DisplayResult(
            modifier = modifier.padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding()
            ),
            onError = {e->
                ErrorCard(message = e,modifier = Modifier.fillMaxSize())
            },
            onLoading = {
                LoadingCard(modifier = Modifier.fillMaxSize())
            },
            onSuccess = {book->
                Column(modifier = Modifier.fillMaxSize()){
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .background(SurfaceLighter)
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ){
                                SubcomposeAsyncImage(
                                    model = book.image?:"",
                                    loading = {
                                        LoadingCard(modifier = Modifier.fillMaxHeight())
                                    },
                                    error = {
                                        Image(
                                            painter = painterResource(Resources.Icon.Broken),
                                            contentDescription = "broken image"
                                        )
                                    },
                                    contentDescription = "book image",
                                    modifier = Modifier
                                        .fillMaxHeight()
                                )
                            }
                        }
                        item {
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 12.dp)
                            ) {
                                Text(
                                    text = book.name.uppercase(),
                                    fontSize = FontSize.EXTRA_REGULAR,
                                    fontWeight = FontWeight.W500
                                )
                                Text(
                                    text = "${book.price.toInt()} vnd(-${book.discount}%)",
                                    fontSize = FontSize.EXTRA_REGULAR,
                                    color = TextSecondary
                                )
                                book.category?.let {
                                    Text(
                                        text = it.name,
                                        fontSize = FontSize.REGULAR,
                                        fontStyle = FontStyle.Italic,
                                        fontWeight = FontWeight.W300
                                    )
                                }
                            }
                        }
                        item {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Description",
                                    fontSize = FontSize.EXTRA_REGULAR,
                                    fontWeight = FontWeight.W400
                                )
                                Text(
                                    text = book.description,
                                    fontWeight = FontWeight.W200,
                                )
                            }

                        }
                        item {
                            Column(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Spacer(modifier = Modifier.height(24.dp))

                                Row {
                                    Text(
                                        text = "Author: ",
                                        fontSize = FontSize.REGULAR
                                    )
                                    Text(
                                        text = book.author?.name?:"",
                                        fontSize = FontSize.REGULAR,
                                        fontWeight = FontWeight.W300
                                    )
                                }
                                Row {
                                    Text(
                                        text = "Publisher: ",
                                        fontSize = FontSize.REGULAR
                                    )
                                    Text(
                                        text = book.publisher?.name?:"",
                                        fontSize = FontSize.REGULAR,
                                        fontWeight = FontWeight.W300
                                    )
                                }
                                Row {
                                    Text(
                                        text = "Language: ",
                                        fontSize = FontSize.REGULAR
                                    )
                                    Text(
                                        text = book.language,
                                        fontSize = FontSize.REGULAR,
                                        fontWeight = FontWeight.W300
                                    )
                                }
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        PrimaryButton(
                            text = "Buy Now",
                            onClick = {},
                            modifier = Modifier.weight(1f)
                        )
                        PrimaryButton(
                            text = "Add to Cart",
                            icon = Resources.Image.ShoppingCart,
                            onClick = {viewModel.addToCart(
                                onSuccess = {
                                    Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                                },
                                onError = {e->
                                    Toast.makeText(context, e, Toast.LENGTH_SHORT).show()
                                }
                            )},
                            containerColor = IconSecondary.copy(alpha = Alpha.EIGHTY_PERCENT),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }


            }
        )
    }
}