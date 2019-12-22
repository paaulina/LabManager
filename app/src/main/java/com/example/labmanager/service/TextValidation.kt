package com.example.labmanager.service

import java.util.regex.Pattern


class TextValidation{


    companion object{

        private val namePattern = Pattern.compile("/^[a-z0-9_-]{6,18}\\$/")
        private val emailPattern = android.util.Patterns.EMAIL_ADDRESS
        private val passwordPattern :Pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")

        private fun matchesPattern(text: String, pattern: Pattern): Boolean {
            return pattern.matcher(text).matches()
        }

        fun correctNameValue(nameText: String): Boolean {
            if(nameText.isEmpty()){
                return false
            }
            if(!matchesPattern(
                    nameText,
                    namePattern
                )
            ){
                return false
            }
            return true
        }

        fun correctEmailValue (emailText: String): Boolean {
            if(!matchesPattern(
                    emailText,
                    emailPattern
                )
            ){
                return false
            }
            return true
        }

        fun correctPasswordValue(passwordTest: String) : Boolean{
            if(!matchesPattern(
                    passwordTest,
                    passwordPattern
                ) || passwordTest.length < 8){
                return false
            }
            return true
        }
    }






}