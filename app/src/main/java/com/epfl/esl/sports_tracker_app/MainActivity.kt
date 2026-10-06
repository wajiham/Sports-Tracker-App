package com.epfl.esl.sports_tracker_app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.epfl.esl.sports_tracker_app.ui.theme.SportsTrackerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SportsTrackerAppTheme {
                Sportstackerapp()

                }
            }
        }
    }

@Composable
fun Sportstackerapp() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var currentScreen by remember { mutableStateOf("home") }
    var isEditingMode by remember { mutableStateOf(true) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val resultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val uri = result.data?.data

            imageUri = uri
        }
    }


    if (currentScreen == "home") {

        HomeScreen(
            onContinueButtonClicked = {
                currentScreen = "account"
            }
        )

    } else {

        if (isEditingMode) {

            AccountScreen(
                username = username,
                password = password,
                imageUri = imageUri,

                onUsernameChanged = { newValue ->
                    username = newValue
                },

                onPasswordChanged = { newValue ->
                    password = newValue
                },

                onConfirmButtonClicked = {
                    isEditingMode = false
                },

                onSelectButtonClicked = {

                    val intent = Intent(Intent.ACTION_GET_CONTENT)

                    intent.type = "image/*"

                    resultLauncher.launch(intent)
                },
                modifier = Modifier
            )

        } else {

            UserAccountDisplay(
                username = username,
                password=password,
                imageUri = imageUri,

                onUpdateButtonClicked = {
                    isEditingMode = true
                },

                onLogoutButtonClicked = {

                    currentScreen="home"
                }
            )
        }
    }
}




@Composable
fun HomeScreen(
    onContinueButtonClicked: () -> Unit,
    modifier: Modifier= Modifier){
    Surface(
        modifier=modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) { }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Image(painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Sports Tracker Logo",
            modifier=modifier.padding(top = 100.dp))

        Text("Sports Tracker App!", fontSize = 24.sp, modifier=modifier.padding(top = 24.dp))

        Row() {
            Button(
                onClick = onContinueButtonClicked,
                modifier=modifier.padding(top = 50.dp)
            ){
                Text("Continue")
            }

        }

    }

}
@Composable
fun AccountScreen(
    username: String,
    password: String,
    imageUri: Uri?,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmButtonClicked: () -> Unit,
    onSelectButtonClicked: () -> Unit,
    modifier: Modifier= Modifier
){
    Surface(
        modifier=modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) { }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding()
    ) {
        Text(
            text = "User Account",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth().padding(top = 60.dp)
        )

        if(imageUri==null)
        {
            Image(
                painter = painterResource(R.drawable.user_image),
                contentDescription = stringResource(R.string.default_user_image),
                modifier=modifier.fillMaxWidth().height(300.dp)
            )
        }
        else {
            AsyncImage(
                model = imageUri,
                contentDescription = stringResource(R.string.picked_user_image),
                modifier=modifier.fillMaxWidth().height(300.dp)
            )
        }
        TextField(
            value = username,
            onValueChange = onUsernameChanged,
            label = { Text(stringResource(R.string.username)) },
            textStyle = TextStyle(fontSize = 24.sp),
            modifier = modifier.fillMaxWidth().padding(top = 30.dp)
        )
        TextField(
            value = password,
            onValueChange = onPasswordChanged,
            label = { Text(stringResource(R.string.password)) },
            textStyle = TextStyle(fontSize = 24.sp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = modifier.fillMaxWidth().padding(top = 30.dp)
        )
        Row(
            modifier = modifier.padding(top = 20.dp)

        ) {
            Button(
                onClick = onConfirmButtonClicked,
                modifier = modifier.weight(1f),
            ) {
                Text(text = stringResource(R.string.confirm_button))
            }
            Button(
                onClick = onSelectButtonClicked,
                        modifier = modifier.weight(1f),
            ) {
                Text(text = stringResource(R.string.pick_image_button))
            }

        }
    }
}

@Composable
fun UserAccountDisplay(
    username: String,
    password: String,
    imageUri: Uri?,
    onUpdateButtonClicked: () -> Unit,
    onLogoutButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,

)
{
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding()
    ) {
        Text(
            text = "User Account",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth().padding(top = 60.dp)
        )

        if(imageUri==null)
        {
            Image(
                painter = painterResource(R.drawable.user_image),
                contentDescription = stringResource(R.string.default_user_image),
                modifier=modifier.fillMaxWidth().height(300.dp)
            )
        }
        else {
            AsyncImage(
                model = imageUri,
                contentDescription = stringResource(R.string.picked_user_image),
                modifier=modifier.fillMaxWidth().height(300.dp)
            )
        }

        Text(
            text=username, fontSize = 24.sp, textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = modifier.fillMaxWidth().padding(top = 8.dp)
        )
       Text(
           text = "*".repeat(password.length),
           fontSize = 24.sp, textAlign = TextAlign.Center, maxLines = 1,
            modifier = modifier.fillMaxWidth()
        )
        Row(
            modifier = modifier.padding(top = 20.dp)

        ) {
            Button(
                onClick = onUpdateButtonClicked,
                modifier = modifier.weight(1f),
            ) {
                Text(text = stringResource(R.string.update_button))
            }
            Button(
                onClick = onLogoutButtonClicked,
                modifier = modifier.weight(1f),
            ) {
                Text(text = stringResource(R.string.logout_button))
            }

        }
    }
}




@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SportsTrackerAppTheme {
        Sportstackerapp()

    }
}