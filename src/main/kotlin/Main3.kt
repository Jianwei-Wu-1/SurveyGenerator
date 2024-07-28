import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.body.MethodDeclaration
import com.google.common.escape.Escaper
import com.google.common.escape.Escapers
import com.google.googlejavaformat.java.Formatter
import com.google.googlejavaformat.java.JavaFormatterOptions
import com.google.gson.Gson
import com.opencsv.CSVWriter
import java.io.File
import java.io.PrintWriter

//To test the survey generator
@Suppress("UnstableApiUsage")

fun main(args: Array<String>) {

    val escape: Escaper = Escapers.builder()
        .addEscape('\'', "")
        .addEscape('\"', "")
        .addEscape('\n', "<br/>")
        .addEscape(' ', "&nbsp")
        .addEscape(':', "")
        .addEscape('\\',"")
        .build()

    val inputMethod = "src/input/Input1.json"

    val inputTest = "src/input/Input2.json"

    val gson = Gson()

    val renamesMethod: List<Rename> = File(inputMethod).bufferedReader().use { reader ->
        gson.fromJson(reader, Array<Rename>::class.java).toList()
    }

    val renamesTest: List<Rename> = File(inputTest).bufferedReader().use { reader ->
        gson.fromJson(reader, Array<Rename>::class.java).toList()
    }

    val questionsForTests: List<QuestionTest> = renamesTest.mapIndexed { index, rename ->

        val (oldName, newName, body) = rename

        val result =  org.apache.commons.text.StringEscapeUtils.unescapeJava(body)

        val code: String = Formatter().formatSource("public class testClass { public void XXX() $result }")

        val lines = code.lines()

        val formattedCode = lines.slice(1 until lines.lastIndex - 1)
            .joinToString("\n")
            .trimIndent()

        val encodedBody = "<code>${escape.escape(formattedCode)}</code>"

        val choices = listOf(oldName, newName).shuffled()

        QuestionTest(index + 1, encodedBody, choices)
    }.toList()

    val questionsForMethods: List<QuestionMethod> = renamesMethod.mapIndexed { index, rename ->

        val (oldName, newName, body) = rename

        val code: String = Formatter().formatSource("public class testClass { $body }")

        val cu = StaticJavaParser.parse(code)

        cu.findAll(MethodDeclaration::class.java).stream()
            .forEach { method -> method.setName("XXX")
            }

        val lines = cu.toString().lines().toMutableList()

        lines.withIndex().forEach { (index, line) ->

            if (line.length > 120){

                lines[index] = line.substring(0, 121) + "\n\t\t\t" + line.substring(121, line.length)
            }
        }

        val formattedCode = lines.toList().slice(1 until lines.lastIndex - 1)
            .joinToString("\n")
            .trimIndent()

        val encodedBody = "<code>${escape.escape(formattedCode)}</code>"

        val choices = listOf(oldName, newName).shuffled()

        QuestionMethod(index + 51, encodedBody, choices)
    }.toList()


    PrintWriter(File("/Users/wujianwei/ideaProjects/survey-generator/answer-key-method.csv").bufferedWriter()).use { writer ->

        val csvWriter = CSVWriter(writer)

        csvWriter.writeNext("question,old,new".split(",".toRegex()).toTypedArray())

        renamesMethod.withIndex().forEach { (index, rename) ->
            val entries = "${index + 1},${rename.oldName},${rename.newName}"
                .split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            csvWriter.writeNext(entries)
        }
    }

    PrintWriter(File("/Users/wujianwei/ideaProjects/survey-generator/answer-key-test.csv").bufferedWriter()).use { writer ->

        val csvWriter = CSVWriter(writer)

        csvWriter.writeNext("question,old,new".split(",".toRegex()).toTypedArray())

        renamesTest.withIndex().forEach { (index, rename) ->
            val entries = "${index + 1},${rename.oldName},${rename.newName}"
                .split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            csvWriter.writeNext(entries)
        }
    }


    println(
        """
        {
  "SurveyEntry": {
    "SurveyID": "SV_0jobguuG3iWzrXn",
    "SurveyName": "Rename Survey 2",
    "SurveyDescription": null,
    "SurveyOwnerID": "UR_02izUEzZbIoFAgZ",
    "SurveyBrandID": "delaware",
    "DivisionID": null,
    "SurveyLanguage": "EN",
    "SurveyActiveResponseSet": "RS_aXmf0fM9FZLZNZz",
    "SurveyStatus": "Inactive",
    "SurveyStartDate": "0000-00-00 00:00:00",
    "SurveyExpirationDate": "0000-00-00 00:00:00",
    "SurveyCreationDate": "2019-10-18 13:53:03",
    "CreatorID": "UR_02izUEzZbIoFAgZ",
    "LastModified": "2019-10-18 13:53:48",
    "LastAccessed": "0000-00-00 00:00:00",
    "LastActivated": "0000-00-00 00:00:00",
    "Deleted": null
  },""")

  println("""
  "SurveyElements": [
    {
      "SurveyID": "SV_0jobguuG3iWzrXn",
      "Element": "BL",
      "PrimaryAttribute": "Survey Blocks",
      "SecondaryAttribute": null,
      "TertiaryAttribute": null,
      "Payload": [
        {
          "Type": "Default",
          "Description": "Test Block",
          "ID": "BL_6sPQTxTc1IRtCBv",
          "BlockElements": [
          """.trimIndent()
    )

    val indexBlock = questionsForTests
        .withIndex()
        .joinToString(",") { (index, _) ->
            """
            {
                "Type": "Question",
                "QuestionID": "QID${index + 1}"
            }"""
        }

    print(indexBlock)

    println(
        """],
            """)
    println("""
      "Options":{
      "BlockLocking":"false",
      "RandomizeQuestions":"Advanced",
      "Randomization":{
         "Advanced":{
            "FixedOrder":[
               "{~SubSet~}",
               "{~SubSet~}",
               "{~SubSet~}",
               "{~SubSet~}",
               "{~SubSet~}",
               "{~SubSet~}",
               "{~SubSet~}",
               "{~SubSet~}",
               "{~SubSet~}",
               "{~SubSet~}"
            ],
            "RandomizeAll":[

            ],
            "RandomSubSet":[
    """.trimIndent())

    val randomBlock = questionsForTests
        .withIndex()
        .joinToString(",\n") { (index, _) ->
            """
                "QID${index + 1}""""
        }

    print(randomBlock)

    println("""
              ],
                "Undisplayed":[

                ],
                "TotalRandSubset":10,
                "QuestionsPerPage":"1"
             },
             "EvenPresentation":true
          },
          "BlockVisibility":"Expanded"
       }
    },
    """)

    println("""
     {
      "Type":"Trash",
      "Description":"Trash \/ Unused Questions",
      "ID":"BL_8r0gRVlSqRH8dlX",
      "BlockElements":[

      ],
      "Options":{
        "BlockLocking":"false",
        "RandomizeQuestions":"false",
        "BlockVisibility":"Collapsed"
      }
     },
    """.trimIndent())

    //method
    println("""
    {
      "Type": "Standard",
      "Description": "Method Block",
      "ID": "BL_4N706Wtcw5Qk3MF",
      "BlockElements": [
          """.trimIndent()
    )

    val indexBlock2 = questionsForMethods
        .withIndex()
        .joinToString(",") { (index, _) ->
            """
            {
                "Type": "Question",
                "QuestionID": "QID${index + 51}"
            }"""
        }

    print(indexBlock2)

    println(
        """],
            """)
    println("""
      "Options":{
      "BlockLocking":"false",
      "RandomizeQuestions":"Advanced",
      "Randomization":{
         "Advanced":{
            "FixedOrder":[
               "{~SubSet~}",
               "{~SubSet~}",
               "{~SubSet~}",
               "{~SubSet~}",
               "{~SubSet~}",
               "{~SubSet~}",
               "{~SubSet~}",
               "{~SubSet~}",
               "{~SubSet~}",
               "{~SubSet~}"
            ],
            "RandomizeAll":[

            ],
            "RandomSubSet":[
    """.trimIndent())

    val randomBlock2 = questionsForMethods
        .withIndex()
        .joinToString(",\n") { (index, _) ->
            """
                "QID${index + 51}""""
        }

    print(randomBlock2)

    println("""
              ],
                "Undisplayed":[

                ],
                "TotalRandSubset":10,
                "QuestionsPerPage":"1"
             },
             "EvenPresentation":true
          },
          "BlockVisibility":"Expanded"
        }
        }
      ]
    },
    """)
    println("""
    {
      "SurveyID": "SV_0jobguuG3iWzrXn",
      "Element": "FL",
      "PrimaryAttribute": "Survey Flow",
      "SecondaryAttribute": null,
      "TertiaryAttribute": null,
      "Payload": {
        "Type":"Root",
        "FlowID":"FL_1",
        "Flow": [
          {
            "ID": "BL_6sPQTxTc1IRtCBv",
            "Type": "Block",
            "FlowID": "FL_2"
          },
          {
            "ID": "BL_4N706Wtcw5Qk3MF",
            "Type": "Standard",
            "FlowID": "FL_3"
          }
        ],
        "Properties": {
          "Count": 3,
          "RemovedFieldsets":[

          ]
        }
      }
    },
    {
      "SurveyID": "SV_0jobguuG3iWzrXn",
      "Element": "SO",
      "PrimaryAttribute": "Survey Options",
      "SecondaryAttribute": null,
      "TertiaryAttribute": null,
      "Payload": {
        "BackButton": "false",
        "SaveAndContinue": "true",
        "SurveyProtection": "PublicSurvey",
        "BallotBoxStuffingPrevention": "false",
        "NoIndex": "Yes",
        "SecureResponseFiles": "true",
        "SurveyExpiration": "None",
        "SurveyTermination": "DefaultMessage",
        "Header": "",
        "Footer": "",
        "ProgressBarDisplay": "None",
        "PartialData": "+1 week",
        "ValidationMessage": "",
        "PreviousButton": " \u2190 ",
        "NextButton": " \u2192 ",
        "SkinLibrary": "delaware",
        "SkinType": "MQ",
        "Skin": "ud1",
        "NewScoring": 1,
        "QuestionsPerPage": "1"
      }
    },
    {
      "SurveyID": "SV_0jobguuG3iWzrXn",
      "Element": "SCO",
      "PrimaryAttribute": "Scoring",
      "SecondaryAttribute": null,
      "TertiaryAttribute": null,
      "Payload": {
        "ScoringCategories": [
        ],
        "ScoringCategoryGroups": [
        ],
        "ScoringSummaryCategory": null,
        "ScoringSummaryAfterQuestions": 0,
        "ScoringSummaryAfterSurvey": 0,
        "DefaultScoringCategory": null,
        "AutoScoringCategory": null
      }
    },
    {
      "SurveyID": "SV_0jobguuG3iWzrXn",
      "Element": "PROJ",
      "PrimaryAttribute": "CORE",
      "SecondaryAttribute": null,
      "TertiaryAttribute": "1.1.0",
      "Payload": {
        "ProjectCategory": "CORE",
        "SchemaVersion": "1.1.0"
      }
    },
    {
      "SurveyID": "SV_0jobguuG3iWzrXn",
      "Element": "STAT",
      "PrimaryAttribute": "Survey Statistics",
      "SecondaryAttribute": null,
      "TertiaryAttribute": null,
      "Payload": {
        "MobileCompatible": true,
        "ID": "Survey Statistics"
      }
    },
    {
      "SurveyID": "SV_0jobguuG3iWzrXn",
      "Element": "QC",
      "PrimaryAttribute": "Survey Question Count",
      "SecondaryAttribute": "${questionsForMethods.size + questionsForTests.size}",
      "TertiaryAttribute": null,
      "Payload": null
    },
    {
      "SurveyID": "SV_0jobguuG3iWzrXn",
      "Element": "RS",
      "PrimaryAttribute": "RS_aXmf0fM9FZLZNZz",
      "SecondaryAttribute": "Default Response Set",
      "TertiaryAttribute": null,
      "Payload": null
    },
    """.trimIndent()
    )

    val questionBlockForTests = questionsForTests.joinToString(",") { it.template }

    print("$questionBlockForTests,")

    val questionBlockForMethods = questionsForMethods.joinToString(",") { it.template }

    print(questionBlockForMethods)

    println("""
    ]
}
    """)

}