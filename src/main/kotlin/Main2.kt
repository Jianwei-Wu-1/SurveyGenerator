import kotlin.collections.HashMap
import java.io.FileReader
import com.opencsv.CSVReader
import java.util.*

// Calculate stats

fun main(args: Array<String>) {

    val renameMap = HashMap<Int, List<String>>()

    val keyReader = CSVReader(FileReader("/Users/wujianwei/ideaProjects/survey-generator/answer-key.csv"))

    val keyEntries = keyReader.readAll()

    keyEntries.forEach { entry ->
        if (entry[0].toString() != "question" && entry[1] != "old" && entry[2] != "new") {
            renameMap[entry[0].toInt()] = listOf(entry[1], entry[2])
        }
    }
//    renameMap.forEach { line -> print(line.toString()) }

    val allEntries = LinkedList<String>()

    val eachResults = LinkedList<List<String>>()

    val reader = CSVReader(FileReader("//Users/wujianwei/ideaProjects/survey-generator/survey_result.csv"))

    val csvEntries = reader.readAll()

    csvEntries.forEach { entry ->
        entry.forEach {
                element -> allEntries.add((element))
        }
    }

    allEntries.withIndex().forEach { (index, element) ->
        if (element == "EN"){
            eachResults.add(allEntries.subList(index, index + 21))
        }
    }

    val eachRecipient =  IntArray(eachResults.size)

    eachResults.withIndex().forEach { (index1, result) ->

        print("Recipient$index1\n")

        result.subList(1, 21).withIndex().forEach {(index2, eachResult) ->

            if (renameMap[index2 + 1]!!.indexOf(eachResult) == -1){
                println("Recipient choice and key do not match!")
            }

            if (renameMap[index2 + 1]!!.indexOf(eachResult) == 0){
                eachRecipient[index1] += 1
                // == 0 is old name, == 1 is new name
            }


            print((index2+1).toString() + " - " + eachResult + "\n")
        }

        print("\n")
    }

    println()

    eachRecipient.withIndex().forEach {
            (index, recipient )->
        println("Recipient " + index + " Choose " + (recipient.toDouble()/20.toDouble())*100 + "% Old Names and "
                + ((20 - recipient).toDouble()/20.toDouble())*100 + "% New Names.")
    }

    println("Overall: "
            + ((20 * eachRecipient.size) - eachRecipient.sum()).toDouble()/(20 * eachRecipient.size).toDouble()*100
            + "% New Names are chosen.")

}