package com.example.labmanager

val FIRST_UNIT_ARRAY = arrayListOf("µmol","umol","mcmol","µIU", "mIU", "IU", "kIU", "µM", "mcM", "uM", "mg", "µg", "mcg", "ug", "microg", "pg", "ng")
val SECOND_UNIT_ARRAY = arrayListOf("mL","dL","L")

val FIRST_UNIT_MOL = hashMapOf("µmol" to 1.0,
                                                    "umol" to 1.0,
                                                    "mcmol" to 1.0,
                                                    "µM" to 1.0,
                                                    "mcM" to 1.0,
                                                    "uM" to 1.0)


val FIRST_UNIT_IU = hashMapOf(  "µIU" to 1000000.0,
                                                    "mIU" to 1000.0,
                                                    "IU" to 1.0,
                                                    "kIU" to 0.001)

val FIRST_UNIT_GRAM = hashMapOf("mg" to 0.001,
                                                    "µg" to 1.0,
                                                    "mcg" to 1.0,
                                                    "ug" to 1.0,
                                                    "microg" to 1.0,
                                                    "pg" to 1000000.0,
                                                    "ng" to 1000.0)

val SECOND_UNIT_ML = hashMapOf("mL" to 1.0,
                                                    "dL" to 0.01,
                                                    "L" to 0.001)

const val RESULT_TYPE_NUMERIC = 0
const val RESULT_TYPE_POSITIVE_NEGATIVE = 1
const val RESULT_TYPE_DESC = 2

const val NEGATIVE_RESULT = 0f
const val POSITIVE_RESULT = 1f

const val PICK_IMAGE_REQUEST = 234

const val IS_FAVORITE = 1
const val NOT_FAVORITE = 0

const val BLOOD_TESTS_NODE = "BloodTests"
const val USERS_NODE = "Users"
const val TESTS_RESULTS_NODE = "TestResults"
const val TESTS_GROUPS_NODE = "TestGroups"

const val DATE = "dt"
const val BLOODTEST_NAME = "nm"
const val RESULT_TYPE = "tp"
const val RESULT = "val"
const val UNIT = "unit"
const val NOTES = "note"
const val FAVORITE = "fav"

const val TEST_GROUP_NAME = "Name"
const val TEST_IDs = "IDs"

const val USER_GENERATED = 1
const val AUTO_GENERATED = 2
const val AUTO_GENERATED_NO_CHART = 3

const val DATA_BASE_ERROR = "error"

const val GLOBAL_NODE = "Global"
const val HASHED_ID = "hId"
const val BLOOD_TEST_REF = "bt"

const val GLOBAL_ALLOWED = 1
const val GLOBAL_DECLINED = 0

const val USER_GLOBAL_PERMISSION = "Global"
const val MEDICAL_FILES_NODE = "MedFiles"

const val NAME = "Name"
const val VERIFY_EMAIL_ERROR_CODE = "1974"

const val USER_NAME = "Name"
const val USER_GENDER= "Gender"


const val SUCCESS_ACTIVITY_ENTRY_POINT = "entryPoint"
const val TEST_RESULT_ADITION = 1
const val MED_FILE_ADDITION = 2

const val PASSWOED_ENTRY_POINT = "entryPoint"
const val CHANGE_PASSWORD_ENTRY = "changePAssword"
const val RESET_PASSWORD_ENTRY = "resetPAssword"

const val HEX_CHARS = "0123456789ABCDEF"
const val SHA_256 = "SHA-256"

const val EMAIL_ERROR_TEXT = "Nieprawidowa wartość"
const val EMAIL_ERROR_TEXT_WRONG = "Wprowadź swój adres e-mail"
const val PASSWORD_ERROR_TEXT = "Hasło musi zawierać min. 8 znaków, w tym minimum jedną dużą i małą literę oraz liczbę"
const val NAME_ERROR_TEXT = "Nieprawidowa wartość"

const val ACTION_ADDING_FAV = 1
const val ACTION_DELETING = 2
const val ACTION_FILTERING = 3
const val ACTION_SHOWING_FAV = 4


const val DIALOG_EDITABLE = 1
const val DIALOG_NOT_EDITABLE = 2

