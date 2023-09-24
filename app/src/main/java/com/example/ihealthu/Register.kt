package com.example.ihealthu


import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import com.example.ihealthu.databinding.LoginRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class Register : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    val db = Firebase.firestore
    private lateinit var binding: LoginRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.backBtn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        binding.register.setOnClickListener{
            val fullName = binding.registerFullName.text.toString()

            val email = binding.registerEmail.text.toString()

            val birthdayDate = binding.cvDateOfBirth.date
            val dateFormat = SimpleDateFormat("yyyy/MM/dd")
            val dateOfBirth = dateFormat.format(birthdayDate)

            val selectedRadioButton: RadioButton = binding.root.findViewById(binding.genderRadioGroup.checkedRadioButtonId)
            val gender = selectedRadioButton.text.toString()

            val pass = binding.registerPassword.text.toString()
            val confirmPass = binding.registerConfirmPassword.text.toString()

            if(fullName.isNotEmpty() && email.isNotEmpty() && dateOfBirth.isNotEmpty() && gender.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()){
                if(pass == confirmPass){
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        val data = hashMapOf(
                            "fullname" to fullName,
                            "email" to email,
                            "dateOfBirth" to dateOfBirth,
                            "gender" to gender,
                            "password" to pass
                        )
                        db.collection("user")
                            .add(data)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Successfull register an account", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, Login::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "failed, try again", Toast.LENGTH_SHORT).show()
                            }
                    }
                }else{
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Empty fields are not allow!!", Toast.LENGTH_SHORT).show()
            }

        }


    }
}

