@file:Suppress("UnstableApiUsage")

import com.google.common.escape.Escaper
import com.google.common.escape.Escapers
import com.google.googlejavaformat.java.Formatter
import com.google.gson.Gson
import java.io.File
import java.io.PrintWriter
import com.opencsv.CSVWriter

// Main generator

fun main(args: Array<String>) {

    val escape: Escaper = Escapers.builder()
        .addEscape('\'', "")
        .addEscape('\"', "")
        .addEscape('\n', "<br/>")
        .addEscape(' ', "&nbsp")
        .addEscape(':', "")
        .addEscape('\\',"")
        .build()

    val input = "src/input/Input_unique.json"

    val gson = Gson()

    val names: List<GeneratedName> = File(input).bufferedReader().use { reader ->
        gson.fromJson(reader, Array<GeneratedName>::class.java).toList()
    }

    val randNames = names.shuffled().subList(0, names.size)

    val questionTests: List<QuestionTest> = randNames.mapIndexed { index, name ->

        val (originalName, generatedName, body) = name

        println(generatedName)

        val code: String = Formatter().formatSourceAndFixImports("public class testClass { public void XXX() $body }")

        val lines = code.lines()

        val formattedCode = lines.slice(1 until lines.lastIndex - 1)
            .joinToString("\n")
            .trimIndent()

        val encodedBody = "<code>${escape.escape(formattedCode)}</code>"

        val choices = listOf(originalName, generatedName).shuffled()

        QuestionTest(index + 1, encodedBody, choices)
    }.toList()

    PrintWriter(File("/Users/wujianwei/ideaProjects/survey-generator/answer-key.csv").bufferedWriter()).use { writer ->

        val csvWriter = CSVWriter(writer)

        csvWriter.writeNext("Q#,original,generated".split(",".toRegex()).toTypedArray())

        randNames.withIndex().forEach { (index, name) ->
            val entries = "${index + 1},${name.originalName},${name.generatedName}"
                .split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            csvWriter.writeNext(entries)
        }
    }

    println(
        """
        {
  "SurveyEntry": {
    "SurveyID": "SV_0jobguuG3iWzrXn",
    "SurveyName": "Uniqueness Naming Survey - new",
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
  },
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
          "Description": "Default Question Block",
          "ID": "BL_6sPQTxTc1IRtCBv",
          "BlockElements": [
          """.trimIndent()
    )

    val indexBlock = questionTests
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
        """]
          },
        {
          "Type": "Trash",
          "Description": "Trash \/ Unused Questions",
          "ID": "BL_8r0gRVlSqRH8dlX"
        }
      ]
    },
    {
      "SurveyID": "SV_0jobguuG3iWzrXn",
      "Element": "FL",
      "PrimaryAttribute": "Survey Flow",
      "SecondaryAttribute": null,
      "TertiaryAttribute": null,
      "Payload": {
        "Flow": [
          {
            "ID": "BL_6sPQTxTc1IRtCBv",
            "Type": "Block",
            "FlowID": "FL_2"
          }
        ],
        "Properties": {
          "Count": 2
        },
        "FlowID": "FL_1",
        "Type": "Root"
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
      "SecondaryAttribute": "${questionTests.size}",
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

    val questionBlock = questionTests.joinToString(",") { it.template }

    print("$questionBlock")

    println(
        """
        ]
    }"""
            .trimIndent()
    )

}