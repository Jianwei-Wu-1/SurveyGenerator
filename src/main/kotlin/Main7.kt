import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileReader
import java.io.PrintWriter


fun main(args: Array<String>) {

    val order = HashMap<Int, String>()

    order[2] = "Our"
    order[3] = "Original"
    order[4] = "Code2vec"
    order[5] = "Host"

    val keyReader = CSVReader(FileReader("src/input/Input_random.csv"))

    val keyEntries = keyReader.readAll()

    val keys = HashMap<String, List<String>>()

    val randomized = HashMap<String, List<String>>()

    for (index in 1..(keyEntries.size - 1)){

        val entry = keyEntries[index].orEmpty().map { m -> m.orEmpty() }

        val originalOrder = HashMap<Int, String>()

        for (i in 2..5){
            originalOrder[i] = entry[i]
        }

        val rand = listOf(2, 3, 4, 5).shuffled()

        keys[entry[0]] = listOf(entry[1], order[rand[0]].orEmpty(), order[rand[1]].orEmpty(), order[rand[2]].orEmpty(), order[rand[3]].orEmpty())

        randomized[entry[0]] = listOf(entry[1], originalOrder[rand[0]].orEmpty(), originalOrder[rand[1]].orEmpty(), originalOrder[rand[2]].orEmpty(), originalOrder[rand[3]].orEmpty())
    }

    for (k in keyEntries){

        val first = k[0]

        if (first.isNotEmpty() && keys.containsKey(first)){

            println("$first: ${keys[first]!!.subList(1, keys[first]!!.size)}")
        }
    }
    println()
    for (k in keyEntries){

        val first = k[0]

        if (first.isNotEmpty() && randomized.containsKey(first)){

            println("$first: ${randomized[first]!!.subList(1, randomized[first]!!.size)}")
        }
    }

    PrintWriter(File("/Users/wujianwei/ideaProjects/survey-generator/src/output/randomized.csv").bufferedWriter()).use { writer ->

        val csvWriter = CSVWriter(writer)

        for (key in keyEntries){

            val randResult = randomized[key[0]]?.subList(1, randomized[key[0]]?.size!!).orEmpty().toTypedArray()

            csvWriter.writeNext(randResult)
        }
    }
}