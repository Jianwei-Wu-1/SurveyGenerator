@file:Suppress("UnstableApiUsage")

import com.google.common.escape.Escaper
import com.google.common.escape.Escapers
import com.google.googlejavaformat.java.Formatter
import com.google.gson.Gson
import java.io.File
import java.io.PrintWriter
import com.opencsv.CSVWriter
import java.util.Collections.list

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

    val input = "src/input/Input_survey.json"

    val gson = Gson()

    val names: List<NameBody> = File(input).bufferedReader().use { reader ->
        gson.fromJson(reader, Array<NameBody>::class.java).toList()
    }

    println(names)

    val nbs = names.subList(0, names.size)

    val nameBodys: List<Pair<String, String>> = nbs.mapIndexed { index, name ->

        val (name, body) = name

        val code: String = Formatter().formatSourceAndFixImports("public class testClass { public void XXX() $body }")

        val lines = code.lines()

        val formattedCode = lines.slice(1 until lines.lastIndex - 1)
            .joinToString("\n")
            .trimIndent()

        val encodedBody = "<code>${escape.escape(formattedCode)}</code>"

        Pair(name, encodedBody)

    }.toList()

    for (p in nameBodys){
        println(p.first)
        println(p.second)
    }

    PrintWriter(File("/Users/wujianwei/ideaProjects/survey-generator/src/output/body.csv").bufferedWriter()).use { writer ->

        val csvWriter = CSVWriter(writer)

        for (key in nameBodys){

            val randResult = listOf(key.first, key.second).toTypedArray()

            csvWriter.writeNext(randResult)
        }
    }
}