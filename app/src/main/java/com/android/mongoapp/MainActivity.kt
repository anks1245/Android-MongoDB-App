package com.android.mongoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.mongoapp.databinding.ActivityMainBinding
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.mongodb.App
import io.realm.mongodb.Credentials
import io.realm.kotlin.where
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.User
import io.realm.mongodb.sync.Subscription
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.bson.types.ObjectId

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var app:App
    lateinit var bt:Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Realm.init(this)

        val appID : String = "application-0-wzpas"

        val credentials = Credentials.anonymous()
        app = App(AppConfiguration.Builder(appID).build())
        app.loginAsync(Credentials.emailPassword("anks1245@gmail.com","123456")){
            Log.e("SuccessLogin",app.currentUser().toString())
            if(it.isSuccess){

                if (it.isSuccess) {
                    val user = it.get()
                    // add an initial subscription to the sync configuration
                    CoroutineScope(Dispatchers.IO).launch {
                        val config = SyncConfiguration.Builder(app.currentUser())
                            .initialSubscriptions { realm, subscriptions ->
                                subscriptions.add(
                                    Subscription.create(
                                        "Student",
                                        realm.where(Student::class.java)
                                            .equalTo("name", "ankit")
                                    )
                                )
                            }
                            .build()
                        // instantiate a realm instance with the flexible sync configuration

                        bt = Realm.getInstance(config)
                    }
                } else {
                    Log.e(
                        "EXAMPLE",
                        "Failed to log in: " + it.error.errorMessage
                    )
                }
                Toast.makeText(this@MainActivity, "You can start now", Toast.LENGTH_SHORT).show()
            }
        }


        binding.submit.setOnClickListener {
            Toast.makeText(this@MainActivity, "Login you in as ${binding.name.text.toString()}", Toast.LENGTH_SHORT).show()

            CoroutineScope(Dispatchers.IO).launch {
                bt.executeTransaction{
                    val stu = it.createObject(Student::class.java,ObjectId())
                    stu.name = binding.name.text.toString()
                    stu.email = binding.email.text.toString()
                    bt.insert(stu)
                }
            }
        }
        binding.clear.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                app.currentUser()?.logOutAsync() {
                    if (it.isSuccess) {
                        Log.v("QUICKSTART", "Successfully logged out.")
                    } else {
                        Log.e("QUICKSTART", "Failed to log out, error: ${it.error}")
                    }
                }
            }
        }
    }
    fun showResults(realm:Realm){
        Log.v("SuccessLogin","gettingUsers")
        val users : RealmResults<Student> = realm.where<Student>().findAll()
        for(stu in users){
            Log.v("Student:",stu.name)
        }
    }
}