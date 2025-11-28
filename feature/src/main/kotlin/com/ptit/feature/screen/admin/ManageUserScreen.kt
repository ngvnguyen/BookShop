package com.ptit.feature.screen.admin

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.ptit.data.DisplayResult
import com.ptit.data.RequestState
import com.ptit.shared.component.BottomPageSelect
import com.ptit.feature.domain.Gender
import com.ptit.feature.form.RoleForm
import com.ptit.feature.form.UserForm
import com.ptit.feature.viewmodel.Action
import com.ptit.feature.viewmodel.AdminViewModel
import com.ptit.feature.viewmodel.getStatus
import com.ptit.shared.Alpha
import com.ptit.shared.ButtonPrimary
import com.ptit.shared.FontSize
import com.ptit.shared.GrayDarker
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.TextPrimary
import com.ptit.shared.TextSecondary
import com.ptit.shared.component.CustomSearchBar
import com.ptit.shared.component.CustomTextFieldWithLabel
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.LoadingCard
import com.ptit.shared.component.PrimaryButton
import org.koin.compose.viewmodel.koinViewModel

/**
 * entry screen
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageUserScreen(
    navigateBack:()->Unit,
    navigateToCreateUser:()->Unit,
    adminViewModel: AdminViewModel
){
    var action = adminViewModel.action
    val snackbarHostState = remember { SnackbarHostState() }
    val users by adminViewModel.userPaged.collectAsState()
    val userSearchQuery by adminViewModel.userSearchQuery.collectAsState()
    var searchBarOpened by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }

    val rolePermission by adminViewModel.rolePermission
    val managePermission by remember {
        derivedStateOf {
            rolePermission.firstOrNull{it.isUser()}
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

    LaunchedEffect(searchBarOpened) {
        if (searchBarOpened) focusRequester.requestFocus()
    }

    Scaffold(
        containerColor = SurfaceDarker,
        topBar = {
            AnimatedContent(targetState = searchBarOpened) {visible->
                if (!visible) {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Manage User",
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
                }else{
                    SearchBar(
                        inputField = {
                            CustomSearchBar(
                                value = userSearchQuery,
                                onValueChange = adminViewModel::setUserSearchQuery,
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            searchBarOpened = false
                                            adminViewModel.setUserSearchQuery("")
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(Resources.Icon.Close),
                                            contentDescription = "Close"
                                        )
                                    }
                                },
                                focusRequester = focusRequester,
                                onSearch = {searchBarOpened = false}
                            )
                        },
                        expanded = false,
                        onExpandedChange = {},
                        content = {},
                        colors = SearchBarDefaults.colors(
                            containerColor = Color.Unspecified
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                }
            }


        },
        floatingActionButton = {
            if (postMethod)
                FloatingActionButton(
                    onClick = {
                        adminViewModel.resetUser()
                        navigateToCreateUser()
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
    ){padding->
        users.DisplayResult(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
            onError = {e->
                ErrorCard(
                    message = e,
                    modifier = Modifier.fillMaxSize()
                )
            },
            onLoading = {
                LoadingCard(modifier = Modifier.fillMaxSize())
            },
            onSuccess = {data->
                var selectedUserId:Int? by remember { mutableStateOf(null) }
                Column(modifier = Modifier.fillMaxHeight()){
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .padding(
                                vertical = 12.dp,
                                horizontal = 12.dp
                            ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = data,
                            key = {it.id}
                        ) {user->
                            UserItemCard(
                                user = user,
                                canDelete = deleteMethod,
                                viewModel = adminViewModel,
                                isSelect = user.id==selectedUserId,
                                onClick = {
                                    if (putMethod){
                                        selectedUserId = if (user.id==selectedUserId) null else user.id
                                        if (selectedUserId!=null) adminViewModel.selectUser(user)
                                    }
                                },
                                snackbarHostState = snackbarHostState,
                                onDismiss = {
                                    selectedUserId = null
                                }
                            )
                        }
                    }
                    val page = data.firstOrNull()?.page?:1
                    val maxPage = data.firstOrNull()?.maxPage?:1
                    BottomPageSelect(
                        page = page,
                        maxPage = maxPage,
                        onPageSelect = adminViewModel::updatePage,
                        onRewindSelect = {
                            adminViewModel.updatePage(1)
                        },
                        onForwardSelect = {
                            adminViewModel.updatePage(maxPage)
                        }
                    )

                }

            },
            transitionSpec = fadeIn() togetherWith fadeOut()
        )
    }
}


/**
 * Card to display user info in manage screen
 */


@Composable
fun UserItemCard(
    modifier : Modifier = Modifier,
    user: UserForm,
    canDelete:Boolean,
    viewModel: AdminViewModel,
    isSelect:Boolean,
    onClick:()->Unit,
    snackbarHostState: SnackbarHostState,
    onDismiss: () -> Unit={}
){
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceLighter)
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = Alpha.TEN_PERCENT),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable(onClick = onClick)
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = Color.Black.copy(alpha = Alpha.TEN_PERCENT),
                    shape = RoundedCornerShape(12.dp)
                )
        ){
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterVertically)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = 1.dp,
                            color = Color.Black.copy(alpha = Alpha.TEN_PERCENT),
                            shape = RoundedCornerShape(12.dp)
                        )
                ){
                    SubcomposeAsyncImage(
                        model = user.avatarUrl?:"",
                        contentDescription = "avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        loading = {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = IconSecondary
                                )
                            }
                        },
                        error = {
                            Icon(
                                painter = painterResource(Resources.Icon.Person),
                                contentDescription = null
                            )
                        },
                    )

                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(
                            vertical = 4.dp,
                            horizontal = 12.dp
                        )
                ) {
                    Text(
                        text = user.name,
                        fontSize = FontSize.EXTRA_REGULAR,
                        fontWeight = FontWeight.W500
                    )
                    Text(
                        text = user.roleName,
                        fontSize = FontSize.SMALL,
                        color = Color.Black.copy(alpha = Alpha.HALF)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = user.email,
                            fontSize = FontSize.REGULAR,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = getStatus(user.isActive),
                            fontSize = FontSize.REGULAR,
                            color = if (user.isActive) Color.Green else Color.Red,
                            maxLines = 1,
                            modifier = Modifier.padding(start = 12.dp),
                            fontWeight = FontWeight.W500
                        )
                    }
                }
            }
            if (canDelete)
                IconButton(
                    onClick={

                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        painter = painterResource(Resources.Icon.Delete),
                        contentDescription = "delete user",
                    )
                }

        }
        AnimatedVisibility(visible = isSelect) {
            UserFormInterface(
                onDismiss = onDismiss,
                onConfirm = {
                    viewModel.updateUser(
                        onSuccess = {
                            onDismiss()
                            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
                        },
                        onError = {e->
                            snackbarHostState.showSnackbar(e)
                        }
                    )
                },
                action = Action.UPDATE,
                viewModel = viewModel,
                snackbarHostState = snackbarHostState
            )
        }

    }


}


/**
 * Screen to create new user
 * action is CREATED
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCreateScreen(
    navigateBack: () -> Unit
){
    val adminViewModel = koinViewModel<AdminViewModel>()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create User",
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
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ){padding->
        UserFormInterface(
            modifier = Modifier
                .padding(
                    bottom = padding.calculateBottomPadding(),
                    top = padding.calculateTopPadding()
                )
                .fillMaxHeight(),
            onDismiss = navigateBack,
            onConfirm = {
                adminViewModel.createUser(
                    onSuccess = {
                        Toast.makeText(context, "Created", Toast.LENGTH_SHORT).show()
                        navigateBack()
                    },
                    onError = {e->
                        snackbarHostState.showSnackbar(e)
                    }
                )
            },
            action = Action.CREATE,
            viewModel = adminViewModel,
            snackbarHostState = snackbarHostState
        )
    }
}


/**
 * Form interface for create or update user
 * @param action if CREATED navigate to create screen, if UPDATE show update form
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserFormInterface(
    modifier : Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    action: Action,
    viewModel: AdminViewModel,
    snackbarHostState: SnackbarHostState
){
    val userForm = viewModel.userForm
    val context = LocalContext.current
    val allRole by viewModel.allRole.collectAsState()
    var datePickerOpened by remember { mutableStateOf(false) }
    var datePickerState = rememberDatePickerState()
    datePickerState.selectedDateMillis = viewModel.getLongFromDate(userForm.dateOfBirth)
    val pickImageForResult = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {uri->
        uri?.let {
            viewModel.updateUserAvatar(
                uri = it,
                context = context,
                onSuccess = {
                },
                onError = {e->
                    snackbarHostState.showSnackbar(e)
                    println(e)
                }
            )
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceLighter)
            .padding(bottom = 12.dp)
            .padding(horizontal = 12.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .then(
                    if (action == Action.CREATE) Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                    else Modifier
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(if(action == Action.CREATE) 160.dp else 100.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(6.dp),
                        color = Color.Black.copy(alpha = Alpha.TEN_PERCENT)
                    )
            ){
                val uploadState = viewModel.uploadState
                when{
                    uploadState.isSuccess() || uploadState.isIdle()->{
                        if (userForm.avatarUrl==null || userForm.avatarUrl.isEmpty() == true){
                            Image(
                                painter = painterResource(Resources.Icon.Add),
                                contentDescription = "image",
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .clickable(onClick = {
                                        pickImageForResult.launch("image/*")
                                    }),
                                contentScale = ContentScale.Crop
                            )
                        }else
                            SubcomposeAsyncImage(
                                model = userForm.avatarUrl,
                                contentDescription = "avatar",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable(onClick = {
                                        pickImageForResult.launch("image/*")
                                    }),
                                contentScale = ContentScale.Crop,
                                loading = {
                                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            color = IconSecondary
                                        )
                                    }
                                },
                                error = {
                                    Icon(
                                        painter = painterResource(Resources.Icon.Broken),
                                        contentDescription = null
                                    )
                                },
                            )
                    }
                    uploadState.isLoading()->{
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    uploadState.isError()->{
                        IconButton(onClick = {viewModel.resetUploadState()}) {
                            Icon(
                                painter = painterResource(Resources.Icon.Replay),
                                contentDescription = "Retry upload",
                            )
                        }
                    }
                }

            }
            CustomTextFieldWithLabel(
                value = userForm.name,
                onValueChange = {
                    viewModel.updateUserName(it)
                },
                label = "User name"
            )
            CustomTextFieldWithLabel(
                value = userForm.phone?:"",
                onValueChange = {
                    viewModel.updateUserPhone(it)
                },
                label = "Phone",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                )
            )
            CustomTextFieldWithLabel(
                value = userForm.email,
                onValueChange = {
                    viewModel.updateUserEmail(it)
                },
                label = "Email",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )
            if (action == Action.CREATE){
                CustomTextFieldWithLabel(
                    value = userForm.password,
                    onValueChange = {
                        viewModel.updateUserPassword(it)
                    },
                    label = "Password"
                )
            }
            CustomTextFieldWithLabel(
                value = userForm.dateOfBirth?.let {viewModel.getDateString(it)}?:"",
                onValueChange = {},
                enabled = false,
                trailingIcon = {
                    IconButton(onClick = {datePickerOpened = !datePickerOpened}) {
                        Icon(
                            painter = if (datePickerOpened) painterResource(Resources.Icon.ArrowUp)
                            else painterResource(Resources.Icon.ArrowDown),
                            contentDescription = "Expand date picker"
                        )
                    }
                },
                label = "Date of birth"
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                var expanded by remember { mutableStateOf(false) }
                Text(
                    text = "Gender"
                )

                Box(){
                    Button(
                        onClick = {expanded=true},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceDarker,
                            contentColor = TextPrimary
                        ),
                    ) {
                        Text(userForm.gender.name)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        Gender.entries.forEach { gender->
                            DropdownMenuItem(
                                text = {Text(gender.name)},
                                onClick = {
                                    viewModel.updateUserGender(gender)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            if (action==Action.UPDATE){
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        text = "Status"
                    )
                    Button(
                        onClick = {
                            viewModel.updateUserStatus(
                                isActive = !userForm.isActive
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (userForm.isActive) Color.Green else Color.Red,
                            contentColor = Color.White
                        ),
                    ) {
                        Text(
                            text = if (userForm.isActive) "ACTIVE" else "INACTIVE"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (action == Action.UPDATE)
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceDarker,
                        )
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonPrimary,
                        )
                    ) {
                        Text("Confirm")
                    }
                }

            if (datePickerOpened)
                DatePickerDialog(
                    onDismissRequest = {datePickerOpened = false},
                    confirmButton = {
                        Button(
                            onClick = {
                                datePickerState.selectedDateMillis?.let{
                                    viewModel.updateUserDateOfBirth(it)
                                }
                                datePickerOpened = false
                            }
                        ) {
                            Text("Select")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {datePickerOpened = false},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GrayDarker
                            )
                        ) {
                            Text("Cancel")
                        }
                    }
                ){
                    DatePicker(state = datePickerState)
                }

        }

        if (action == Action.CREATE)
            PrimaryButton(
                text = "Create",
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth()
            )
    }



}

