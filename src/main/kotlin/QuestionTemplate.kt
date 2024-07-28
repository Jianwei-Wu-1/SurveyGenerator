fun order(names: List<String>): String {
    return names.indices.joinToString(",\n") {
        """
            "${it + 1}""""}
}

fun choices(names: List<String>): String {
    return names.withIndex()
        .joinToString(",") { (index, name) ->
            """
            "${index + 1}":{
               "Display":"$name"
            }"""
        }
}

data class QuestionTest(val index: Int, val testBody: String, val names: List<String>) {

    val template = """{
    "SurveyID":"SV_a95XOKDdAB76HVX",
    "Element":"SQ",
    "PrimaryAttribute":"QID$index",
    "SecondaryAttribute":null,
    "TertiaryAttribute":null,
    "Payload":{
        "QuestionText":"Which name would you choose for the following test:<br/><br/>$testBody<br/><br/>",
        "DataExportTag":"Q$index",
        "QuestionType":"MC",
        "Selector":"SAVR",
        "SubSelector":"TX",
        "Configuration":{
            "QuestionDescriptionOption":"UseText"
        },
        "QuestionDescription": null,
        "Choices":{"""
        .plus(
            choices(names)
        )
        .plus(
            """
        },
        "ChoiceOrder":[
            """
                .plus(
                    order(names)
                )
                .plus(
                    """
        ],
        "Validation":{
            "Settings":{
                "ForceResponse":"OFF",
                "ForceResponseType":"ON",
                "Type":"None"
            }
        },
        "Language":[
        ],
        "NextChoiceId":${names.size + 1},
        "NextAnswerId":${1},
        "QuestionID":"QID$index"
    }
}"""
                )
        )
}

data class QuestionMethod(val index: Int, val testBody: String, val names: List<String>) {

    val template = """{
    "SurveyID":"SV_a95XOKDdAB76HVX",
    "Element":"SQ",
    "PrimaryAttribute":"QID$index",
    "SecondaryAttribute":null,
    "TertiaryAttribute":null,
    "Payload":{
        "QuestionText":"Which name would you choose for the following method:<br/><br/>$testBody<br/><br/>",
        "DataExportTag":"Q$index",
        "QuestionType":"MC",
        "Selector":"SAVR",
        "SubSelector":"TX",
        "Configuration":{
            "QuestionDescriptionOption":"UseText"
        },
        "QuestionDescription": null,
        "Choices":{"""
        .plus(
            choices(names)
        )
        .plus(
            """
        },
        "ChoiceOrder":[
            """
                .plus(
                    order(names)
                )
                .plus(
                    """
        ],
        "Validation":{
            "Settings":{
                "ForceResponse":"OFF",
                "ForceResponseType":"ON",
                "Type":"None"
            }
        },
        "Language":[
        ],
        "NextChoiceId":${names.size + 1},
        "NextAnswerId":${1},
        "QuestionID":"QID$index"
    }
}"""
                )
        )
}


data class QuestionTestUnique (val index: Int, val testBody: String, val generatedName: String) {

    val template = """{
    "SurveyID":"SV_a95XOKDdAB76HVX",
    "Element":"SQ",
    "PrimaryAttribute":"QID$index",
    "SecondaryAttribute":null,
    "TertiaryAttribute":null,
    "Payload":{
        "QuestionText":"Do you think the generated name is good? if good, at which category?:<br/><br/>$testBody<br/><br/>Generated Name:<br/><br/>$generatedName",
        "DataExportTag":"Q$index",
        "QuestionType":"MC",
        "Selector":"SAVR",
        "SubSelector":"TX",
        "Configuration":{
            "QuestionDescriptionOption":"UseText"
        },
        "QuestionDescription": null,
        "Choices":{"""
        .plus(
            "\"1\":{\"Display\":\"Not Good\"},\"2\":{\"Display\":\"Good and Complete\"},\"3\":{\"Display\":\"Good at being descriptive about action\"},\"4\":{\"Display\":\"Good at being descriptive about predicate\"},\"5\":{\"Display\":\"Good at being descriptive about scenario\"}"
        )
        .plus(
            """
        },
        "ChoiceOrder":[
            """
                .plus(
                    "1,2,3,4,5"
                )
                .plus(
                    """
        ],
        "Validation":{
            "Settings":{
                "ForceResponse":"OFF",
                "ForceResponseType":"ON",
                "Type":"None"
            }
        },
        "Language":[
        ],
        "NextChoiceId":${1},
        "NextAnswerId":${1},
        "QuestionID":"QID$index"
    }
}"""
                )
        )
}
