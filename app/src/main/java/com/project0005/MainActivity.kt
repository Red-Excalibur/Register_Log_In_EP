package com.project0005

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.project0005.ui.theme.Project0005Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    lateinit var auth :FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth= FirebaseAuth.getInstance()

        setContent {
            Project0005Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                   modifier = Modifier
                ) {
                    RegisterUserFull()

                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        checkLoggedInState()
    }

    private fun registerUser (){
        if(ToSave.email.isNotEmpty() && ToSave.pass.isNotEmpty() ) {
            Toast.makeText(this@MainActivity,"They are not empty",Toast.LENGTH_SHORT).show()
//            Log.e("ilyas","They are not empty")

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    //we wait for this action
                    auth.createUserWithEmailAndPassword(ToSave.email,ToSave.pass)
                        .await()
                    Log.e("ilyas","after await")
                    Log.e("ilyas","before check logged in state")

                    withContext(Dispatchers.Main){
                        checkLoggedInState()
                    }

                }catch (e:Exception){
                    Toast.makeText(this@MainActivity,"Error Happened",Toast.LENGTH_SHORT).show()

                }
            }
        }else{
            Toast.makeText(this@MainActivity,"Texts Are empty",Toast.LENGTH_SHORT).show()

        }
    }
    private fun checkLoggedInState(){
        if (auth.currentUser==null){
            Toast.makeText(this,"You are not logged in",Toast.LENGTH_LONG).show()
        }else {
            Toast.makeText(this,"You are logged in ",Toast.LENGTH_LONG).show()
        }
    }
    //Login
    private fun loginUser(){
        if(ToSave.email.isNotEmpty() && ToSave.pass.isNotEmpty() ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    //this is the only thing we need to change
                    auth.signInWithEmailAndPassword(ToSave.email,ToSave.pass)
                        .await()

                    withContext(Dispatchers.Main){
                        checkLoggedInState()
                    }

                }catch (e:Exception){
                    Toast.makeText(this@MainActivity,"Error Happened",Toast.LENGTH_SHORT).show()

                }
            }
        }else{
            Toast.makeText(this@MainActivity,"Texts Are empty",Toast.LENGTH_SHORT).show()

        }
    }
@Composable
fun RegisterUserFull(){

    var emailTF by remember { mutableStateOf("") }
    var passTF by remember { mutableStateOf("") }
    val context = LocalContext.current
Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
){
    TextField(
        value = emailTF,
        onValueChange = { newText ->
            emailTF = newText
        },
        label =   { Text("Enter Email")}

    )
    TextField(
        value = passTF,
        onValueChange = { newText ->
            passTF = newText
        },
        label =   { Text("Enter Password")}
    )
     Row(
         modifier = Modifier.fillMaxWidth(),
         verticalAlignment = Alignment.CenterVertically,
         horizontalArrangement =Arrangement.Center
     ){
         Button(onClick = {
             Toast.makeText(context,"Wait",Toast.LENGTH_SHORT).show()
             //taking the data from the user(txt field) and setting them on the storage)
             ToSave.email =emailTF
             ToSave.pass =passTF
             //then registering

             registerUser()
             Toast.makeText(context,"Registering",Toast.LENGTH_SHORT).show()

         }) {
             Text(text = "Register in ")
         }
         Spacer(modifier = Modifier.width(4.dp))
         Button(onClick = {
             Toast.makeText(context,"Wait",Toast.LENGTH_SHORT).show()
             //taking the data from the user(txt field) and setting them on the storage)
             ToSave.email =emailTF
             ToSave.pass =passTF
             //then logging in

             loginUser()
             Toast.makeText(context,"Logging In",Toast.LENGTH_SHORT).show()

         }) {
             Text(text = "Log In ")
         }
         Spacer(modifier = Modifier.width(4.dp))
         Button(onClick = {
             Toast.makeText(context,"Wait",Toast.LENGTH_SHORT).show()
             //taking the data from the user(txt field) and setting them on the storage)
             ToSave.email =emailTF
             ToSave.pass =passTF
             //then signing out

             auth.signOut()
             Toast.makeText(context,"Signing out",Toast.LENGTH_SHORT).show()
             checkLoggedInState()

         }) {
             Text(text = "Sign Out ")

         }
     }

}





}

}


