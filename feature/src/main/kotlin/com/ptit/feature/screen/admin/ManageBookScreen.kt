package com.ptit.feature.screen.admin

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.ptit.data.DisplayResult
import com.ptit.data.RequestState
import com.ptit.feature.form.AuthorForm
import com.ptit.feature.form.BookForm
import com.ptit.feature.form.CategoryForm
import com.ptit.feature.form.PublisherForm
import com.ptit.feature.viewmodel.Action
import com.ptit.feature.viewmodel.AdminViewModel
import com.ptit.shared.Alpha
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.Orange
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.TextSecondary
import com.ptit.shared.component.BottomPageSelect
import com.ptit.shared.component.CustomSearchBar
import com.ptit.shared.component.CustomTextFieldWithLabel
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.LoadingCard
import com.ptit.shared.component.PrimaryButton
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageBookScreen(
    navigateBack:()->Unit
){
    val adminViewModel = koinViewModel<AdminViewModel>()
    var action = adminViewModel.action
    val snackbarHostState = remember { SnackbarHostState() }
    val books by adminViewModel.bookPaged.collectAsState()
    var searchBarOpened by remember { mutableStateOf(false) }

    val rolePermission by adminViewModel.rolePermission
    val managePermission by remember {
        derivedStateOf {
            rolePermission.firstOrNull{it.isBook()}
        }
    }
    if (managePermission == null) navigateBack()


    val getMethod by remember {
        derivedStateOf {
            managePermission?.data?.any { it.isGet() && it.status.isActive() } ?:false
        }
    }
    val postMethod by remember {
        derivedStateOf {
            managePermission?.data?.any { it.isPost() && it.status.isActive() } ?:false
        }
    }
    val putMethod by remember {
        derivedStateOf {
            managePermission?.data?.any { it.isPut() && it.status.isActive() } ?:false
        }
    }
    val deleteMethod by remember {
        derivedStateOf {
            managePermission?.data?.any { it.isDelete() && it.status.isActive() } ?:false
        }
    }
    val bookSearchQuery = adminViewModel.medialBookSearchQuery
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    AnimatedContent(
        targetState = searchBarOpened,
        transitionSpec = { EnterTransition.None togetherWith ExitTransition.None }
    ) {open->
        if (open){
            var enableAdvanceSearch by remember { mutableStateOf(false) }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .imePadding()
                    .systemBarsPadding()
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Search book",
                        fontSize = FontSize.EXTRA_MEDIUM,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {searchBarOpened=false}
                    ) {
                        Icon(
                            painter = painterResource(Resources.Icon.Close),
                            contentDescription = "close search book"
                        )
                    }
                }
                CustomSearchBar(
                    value = bookSearchQuery.name,
                    onValueChange = {adminViewModel.updateBookSearchParams(name = it)},
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                adminViewModel.updateBookSearchParams(name = "")
                            }
                        ) {
                            Icon(
                                painter = painterResource(Resources.Icon.Close),
                                contentDescription = "Close"
                            )
                        }
                    },
                    onSearch = {},
                    title = "Book Name"
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Advanced",
                        fontSize = FontSize.SMALL,
                        color = Color.Black.copy(alpha = Alpha.HALF)
                    )
                    Icon(
                        painter = if(enableAdvanceSearch) painterResource(Resources.Icon.ArrowDown)
                        else painterResource(Resources.Icon.ArrowUp),
                        contentDescription = "Expand",
                        modifier = Modifier.clickable(onClick = {enableAdvanceSearch = !enableAdvanceSearch})
                    )

                }
                AnimatedVisibility(visible = enableAdvanceSearch) {
                    Column {
                        CustomSearchBar(
                            value = bookSearchQuery.authorQuery,
                            onValueChange = {adminViewModel.updateBookSearchParams(authorQuery = it)},
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        adminViewModel.updateBookSearchParams(authorQuery = "")
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(Resources.Icon.Close),
                                        contentDescription = "Close"
                                    )
                                }
                            },
                            onSearch = {},
                            title = "Author"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CustomSearchBar(
                            value = bookSearchQuery.categoryQuery,
                            onValueChange = {adminViewModel.updateBookSearchParams(categoryQuery = it)},
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        adminViewModel.updateBookSearchParams(categoryQuery = "")
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(Resources.Icon.Close),
                                        contentDescription = "Close"
                                    )
                                }
                            },
                            onSearch = {},
                            title = "Category"
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))

                PrimaryButton(text = "Search") {
                    adminViewModel.submitSearchBookParams()
                    searchBarOpened = false
                }

            }
        }else{
            Scaffold(
                topBar = {
                    AnimatedContent(targetState = searchBarOpened) {visible->
                        if (!visible) {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "Manage Book",
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
                                },
                                actions = {
                                    IconButton(onClick = {searchBarOpened = true}) {
                                        Icon(
                                            painter = painterResource(Resources.Icon.Search),
                                            contentDescription = "Search"
                                        )
                                    }
                                }
                            )
                        }
                    }


                },
                floatingActionButton = {
                    if (postMethod)
                        FloatingActionButton(
                            onClick = {
                                adminViewModel.setAct(Action.CREATE)
                                showDialog = true
                            },
                            shape = RoundedCornerShape(50.dp),
                            containerColor = Color.White
                        ){
                            Icon(
                                painter = painterResource(Resources.Icon.Plus),
                                contentDescription = "Add",
                                tint = IconSecondary,
                            )
                        }
                },
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState
                    )
                }
            ) { padding ->
                books.DisplayResult(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = padding.calculateTopPadding(),
                            bottom = padding.calculateBottomPadding()
                        ),
                    onSuccess = {allBook->
                        Column(modifier = Modifier.fillMaxHeight()) {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(12.dp)
                            ) {
                                items(
                                    items = allBook,
                                    key = {it.id}
                                ) {book->
                                    BookItemCard(
                                        bookForm = book,
                                        onClick = {
                                            if (putMethod){
                                                adminViewModel.selectBookForm(book)
                                                adminViewModel.setAct(Action.UPDATE)
                                                showDialog = true
                                            }
                                        },
                                        canDelete = deleteMethod,
                                        onDelete = {},
                                        quantityVisible = true
                                    )
                                }
                            }

                            val page = allBook.firstOrNull()?.page?:1
                            val maxPage = allBook.firstOrNull()?.maxPage?:1

                            BottomPageSelect(
                                page = page,
                                maxPage = maxPage,
                                onRewindSelect = {
                                    adminViewModel.updateBookSearchParams(page=1)
                                },
                                onForwardSelect = {
                                    adminViewModel.updateBookSearchParams(page=maxPage)
                                },
                                onPageSelect = {p->
                                    adminViewModel.updateBookSearchParams(page = p)
                                }
                            )
                        }
                    },
                    onLoading = {
                        LoadingCard(modifier = Modifier.fillMaxSize())
                    },
                    onError = {e->
                        ErrorCard(message = e,modifier = Modifier.fillMaxSize())
                    }
                )
            }
        }
    }
    if (showDialog){
        BookAlertDialog(
            bookForm = adminViewModel.bookForm,
            onDismiss = {showDialog = false},
            onConfirm = {
                if (action == Action.UPDATE)
                    adminViewModel.updateBook(
                        onSuccess = {
                            showDialog = false
                            Toast.makeText(context,"Updated",Toast.LENGTH_SHORT).show()
                        },
                        onError = {e->
                            Toast.makeText(context,e,Toast.LENGTH_SHORT).show()
                        }
                    )
                else
                    adminViewModel.createBook(
                        onSuccess = {
                            showDialog = false
                            Toast.makeText(context,"Created",Toast.LENGTH_SHORT).show()
                        },
                        onError = {e->
                            Toast.makeText(context,e,Toast.LENGTH_SHORT).show()
                        }
                    )
            },
            action = adminViewModel.action,
            viewModel = adminViewModel
        )
    }

}

@Composable
fun BookAlertDialog(
    bookForm: BookForm,
    onDismiss:()->Unit,
    onConfirm:()->Unit,
    action: Action,
    viewModel: AdminViewModel
){
    val context = LocalContext.current
    val pickImageForResult = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {uri->
        uri?.let {
            viewModel.updateBookImage(
                uri = it,
                context = context,
                onSuccess = {
                },
                onError = {e->
                    Toast.makeText(context,e,Toast.LENGTH_SHORT).show()
                    println(e)
                }
            )
        }
    }
    var price by remember { mutableStateOf(bookForm.price.toString()) }
    var discount by remember { mutableStateOf(bookForm.discount.toString()) }
    var quantity by remember { mutableStateOf(bookForm.quantity.toString()) }

    var showSelectAuthor by remember { mutableStateOf(false) }
    var showSelectCategory by remember { mutableStateOf(false) }
    var showSelectPublisher by remember { mutableStateOf(false) }
    var showSelectStatus by remember { mutableStateOf(false) }

    AlertDialog(
        shape = RoundedCornerShape(6.dp),
        onDismissRequest = onDismiss,
        confirmButton = {
            PrimaryButton(
                text = "Confirm",
                onClick = {
                    viewModel.updateBookForm(
                        price = price.toDoubleOrNull() ?: 0.0,
                        discount = discount.toDoubleOrNull() ?: 0.0,
                        quantity = quantity.toIntOrNull() ?: 0
                    )
                    onConfirm()
                }
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .height(180.dp)
                        .aspectRatio(0.66f)
                        .clip(RoundedCornerShape(6.dp))
                        .border(
                            width = 1.dp,
                            color = Color.Black.copy(alpha = Alpha.TEN_PERCENT),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable(onClick = {
                            pickImageForResult.launch("image/*")
                        })
                        .align(Alignment.CenterHorizontally)

                ){
                    SubcomposeAsyncImage(
                        model = bookForm.image,
                        contentDescription = null,
                        loading = {
                            CircularProgressIndicator()
                        },
                        error = {
                            Image(
                                painter = painterResource(Resources.Icon.Broken),
                                contentDescription = "error image"
                            )
                        },
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    item{
                        CustomTextFieldWithLabel(
                            value = bookForm.name,
                            onValueChange = {viewModel.updateBookForm(name = it)},
                            label = "Name"
                        )
                    }
                    item{
                        CustomTextFieldWithLabel(
                            value = bookForm.description,
                            onValueChange = {viewModel.updateBookForm(description = it)},
                            label = "Description"
                        )
                    }
                    item{
                        CustomTextFieldWithLabel(
                            value = price,
                            onValueChange = {
                                if( it.count{c->c=='.'}<=1 && it.all { c->c.isDigit() || c=='.' } ){
                                    price = it
                                }
                            },
                            label = "Price",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                    }
                    item{
                        CustomTextFieldWithLabel(
                            value = discount,
                            onValueChange = {
                                if( it.count{c->c=='.'}<=1 && it.all { c->c.isDigit() || c=='.' } ){
                                    discount = it
                                }
                            },
                            label = "Discount (%)",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                    }
                    item{
                        CustomTextFieldWithLabel(
                            value = quantity,
                            onValueChange = {
                                if(it.isDigitsOnly()){
                                    quantity = it
                                }
                            },
                            label = "Quantity",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                    }
                    item{
                        CustomTextFieldWithLabel(
                            value = bookForm.language,
                            onValueChange = {viewModel.updateBookForm(language=it)},
                            label = "Language"
                        )
                    }
                    item{
                        CustomTextFieldWithLabel(
                            value = bookForm.author?.name?:"",
                            onValueChange = {},
                            label = "Author",
                            enabled = false,
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(Resources.Icon.ArrowDown),
                                    contentDescription = null,
                                    modifier = Modifier.clickable(onClick = {showSelectAuthor = true})
                                )
                            }
                        )
                    }
                    item{
                        CustomTextFieldWithLabel(
                            value = bookForm.publisher?.name?:"",
                            onValueChange = {},
                            label = "Publisher",
                            enabled = false,
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(Resources.Icon.ArrowDown),
                                    contentDescription = null,
                                    modifier = Modifier.clickable(onClick = {showSelectPublisher=true})
                                )
                            }
                        )
                    }
                    item{
                        CustomTextFieldWithLabel(
                            value = bookForm.category?.name?:"",
                            onValueChange = {},
                            label = "Category",
                            enabled = false,
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(Resources.Icon.ArrowDown),
                                    contentDescription = null,
                                    modifier = Modifier.clickable(onClick = {showSelectCategory=true})
                                )
                            }
                        )
                    }
                    item{
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Status"
                            )
                            Button(
                                onClick = {
                                    showSelectStatus = true
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (bookForm.status == BookForm.Status.AVAILABLE) Color.Green
                                        else Color.Red
                                )
                            ) {
                                Text(
                                    text = bookForm.status.title
                                )
                            }
                        }
                    }
                }
            }
        },
        title = {
            Text(
                text = if (action==Action.UPDATE) "Update Book" else "Create Book",
                color = TextSecondary
            )
        },
        containerColor = SurfaceLighter
    )


    val authorState by viewModel.allAuthor.collectAsState()
    val publisherState by viewModel.allPublisher.collectAsState()
    val categoryState by viewModel.allCategory.collectAsState()
    AnimatedVisibility(visible = showSelectAuthor) {
        SelectAuthorSheet(
            onDismiss = {showSelectAuthor = false},
            onSelect = {id,name->
                viewModel.updateBookForm(author = Pair(id,name))
                showSelectAuthor = false
            },
            authorState = authorState
        )
    }
    AnimatedVisibility(visible = showSelectPublisher) {
        SelectPublisherSheet(
            onDismiss = {showSelectPublisher = false},
            onSelect = {id,name->
                viewModel.updateBookForm(publisher = Pair(id,name))
                showSelectPublisher = false
            },
            publisherState = publisherState
        )
    }
    AnimatedVisibility(visible = showSelectCategory) {
        SelectCategorySheet(
            onDismiss = {showSelectCategory = false},
            onSelect = {id,name->
                viewModel.updateBookForm(category = Pair(id,name))
                showSelectCategory = false
            },
            categoryState = categoryState
        )
    }
    AnimatedVisibility(visible = showSelectStatus) {
        SelectStatusSheet(
            onDismiss ={showSelectStatus = false},
            onSelect = {status->
                viewModel.updateBookForm(status = status)
                showSelectStatus = false
            }
        )
    }
}


@Composable
fun BookItemCard(
    modifier:Modifier = Modifier,
    bookForm: BookForm,
    onClick:()->Unit,
    canDelete:Boolean = false,
    onDelete:()->Unit={},
    quantityVisible:Boolean = false
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = Alpha.HALF),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(8.dp)
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .height(200.dp)
            ) {
                SubcomposeAsyncImage(
                    model = bookForm.image,
                    contentDescription = null,
                    loading = {
                        CircularProgressIndicator()
                    },
                    error = {
                        Image(
                            painter = painterResource(Resources.Icon.Broken),
                            contentDescription = "error image"
                        )
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(0.66f)
                        .clip(RoundedCornerShape(6.dp))
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(6.dp)
                        ),
                    contentScale = ContentScale.Crop
                )

                Column{
                    Text(
                        text = bookForm.name,
                        maxLines = 1,
                        fontSize = FontSize.EXTRA_REGULAR,
                        fontWeight = FontWeight.W500
                    )
                    Text(
                        text = "by ${bookForm.author?.name}",
                        maxLines = 1,
                        fontSize = FontSize.REGULAR,
                        fontWeight = FontWeight.Light,
                        fontStyle = FontStyle.Italic
                    )

                    HorizontalDivider(color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Price: ${bookForm.price}vnd (-${bookForm.discount.toInt()}%)",
                        maxLines = 1,
                        fontSize = FontSize.REGULAR,
                        fontStyle = FontStyle.Italic,
                        color = Orange
                    )
                    Text(
                        text = "Category: ${bookForm.category?.name}",
                        maxLines = 1,
                        fontSize = FontSize.REGULAR
                    )
                    Text(
                        text = "Description: ${bookForm.description}",
                        maxLines = 3,
                        fontSize = FontSize.EXTRA_SMALL,
                        fontWeight = FontWeight.Light,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Publisher: ${bookForm.publisher?.name}",
                        maxLines = 1,
                        fontSize = FontSize.REGULAR
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (quantityVisible) Text(
                    text = "Quantity: ${bookForm.quantity}",
                    fontSize = FontSize.REGULAR,
                    fontWeight = FontWeight.W400
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = bookForm.status.title,
                    color = when(bookForm.status){
                        BookForm.Status.OUT_OF_STOCK -> Color.Red
                        BookForm.Status.AVAILABLE-> Color.Green
                    },
                    fontWeight = FontWeight.W400
                )
            }
        }

        if (canDelete) Icon(
            painter = painterResource(Resources.Icon.Delete),
            contentDescription = "Delete",
            modifier = Modifier
                .clickable(onClick=onDelete)
                .align(Alignment.TopEnd)
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectStatusSheet(
    onDismiss: () -> Unit,
    onSelect:(BookForm.Status)->Unit
){

    val listStatus = BookForm.Status.entries

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SurfaceLighter

    ) {
        Column(
            modifier = Modifier
                .padding(horizontal =12.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Select Status",
                    fontSize = FontSize.MEDIUM,
                    color = TextSecondary
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listStatus) { status ->
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable(onClick = {
                                onSelect(status)
                            })
                            .padding(
                                vertical = 12.dp,
                                horizontal = 8.dp
                            )


                    ) {
                        Text(
                            text = status.name,
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontSize = FontSize.EXTRA_REGULAR
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAuthorSheet(
    onDismiss: () -> Unit,
    onSelect:(Int,String)->Unit,
    authorState: RequestState<List<AuthorForm>>
){
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var searchQuery by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight(),
        containerColor = SurfaceLighter
    ) {
        authorState.DisplayResult(
            onSuccess = {authors->
                Column(
                    modifier = Modifier
                        .padding(horizontal =12.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Select Author",
                            fontSize = FontSize.MEDIUM,
                            color = TextSecondary
                        )
                    }
                    CustomTextFieldWithLabel(
                        value = searchQuery,
                        onValueChange = {searchQuery = it},
                        label = "Search"
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = authors.filter {
                                if (searchQuery.isEmpty()) true
                                else it.name.lowercase().contains(searchQuery.lowercase())
                            },
                            key = {it.id}
                        ) {author->
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable(onClick = {
                                        onSelect(author.id,author.name)
                                    })
                                    .padding(
                                        vertical = 12.dp,
                                        horizontal = 8.dp
                                    )


                            ) {
                                Text(
                                    text = author.name,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    fontSize = FontSize.EXTRA_REGULAR
                                )
                            }
                        }
                    }

                }

            },
            onError = {
                ErrorCard(message = it)
            },
            onLoading = {
                LoadingCard()
            }
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCategorySheet(
    onDismiss: () -> Unit,
    onSelect:(Int,String)->Unit,
    categoryState: RequestState<List<CategoryForm>>
){
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var searchQuery by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight(),
        containerColor = SurfaceLighter

    ) {
        categoryState.DisplayResult(
            onSuccess = {categories->
                Column(
                    modifier = Modifier
                        .padding(horizontal =12.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Select Category",
                            fontSize = FontSize.MEDIUM,
                            color = TextSecondary
                        )
                    }
                    CustomTextFieldWithLabel(
                        value = searchQuery,
                        onValueChange = {searchQuery = it},
                        label = "Search"
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = categories.filter {
                                if (searchQuery.isEmpty()) true
                                else it.name.lowercase().contains(searchQuery.lowercase())
                            },
                            key = {it.id}
                        ) {category->
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable(onClick = {
                                        onSelect(category.id,category.name)
                                    })
                                    .padding(
                                        vertical = 12.dp,
                                        horizontal = 8.dp
                                    )


                            ) {
                                Text(
                                    text = category.name,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    fontSize = FontSize.EXTRA_REGULAR
                                )
                            }
                        }
                    }

                }

            },
            onError = {
                ErrorCard(message = it)
            },
            onLoading = {
                LoadingCard()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPublisherSheet(
    onDismiss: () -> Unit,
    onSelect:(Int,String)->Unit,
    publisherState: RequestState<List<PublisherForm>>
){
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var searchQuery by remember { mutableStateOf("") }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight(),
        containerColor = SurfaceLighter

    ) {
        publisherState.DisplayResult(
            onSuccess = {publishers->
                Column(
                    modifier = Modifier
                        .padding(horizontal =12.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Select Publisher",
                            fontSize = FontSize.MEDIUM,
                            color = TextSecondary
                        )
                    }
                    CustomTextFieldWithLabel(
                        value = searchQuery,
                        onValueChange = {searchQuery = it},
                        label = "Search"
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = publishers.filter {
                                if (searchQuery.isEmpty()) true
                                else it.name.lowercase().contains(searchQuery.lowercase())
                            },
                            key = {it.id}
                        ) {publisher->
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable(onClick = {
                                        onSelect(publisher.id,publisher.name)
                                    })
                                    .padding(
                                        vertical = 12.dp,
                                        horizontal = 8.dp
                                    )


                            ) {
                                Text(
                                    text = publisher.name,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    fontSize = FontSize.EXTRA_REGULAR
                                )
                            }
                        }
                    }

                }

            },
            onError = {
                ErrorCard(message = it)
            },
            onLoading = {
                LoadingCard()
            }
        )
    }
}