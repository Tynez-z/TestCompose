package com.example.testCompose

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.testCompose.presentation.ui.compose.MovieApp
import com.example.testCompose.presentation.viewModel.LanguageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.yield
import kotlin.system.measureTimeMillis


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val settingsViewModel: LanguageViewModel by viewModels()

    fun checkStrongBoxFeature(): Boolean {
        return this.packageManager.hasSystemFeature(
            PackageManager.FEATURE_STRONGBOX_KEYSTORE
        )
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(
        ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class,
        ExperimentalFoundationApi::class, ExperimentalMaterialApi::class, FlowPreview::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MainActivity", "checkStrongBoxFeature:${checkStrongBoxFeature()} ")
        Log.i("MainActivity", "onCreate ")

        enableEdgeToEdge()
        settingsViewModel.onSettingsChanged.observe(this) {
        }

        setContent {
            val showSettingsDialog = remember { mutableStateOf(false) }
            MovieApp()
            task3()
//            lifecycleScope.launch {
////                testConcatMap()
////                testFlatMapMerged()
////                getOffers()
////                testFlatMapLatest()
//                testCoroutinesQuiz()
//            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "onStart ")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i("MainActivity", "onRestoreInstanceState ")
    }

    override fun onResume() {
        super.onResume()
        Log.i("MainActivity", "onResume ")
    }

    override fun onPause() {
        super.onPause()
        Log.i("MainActivity", "onPause ")
    }

    override fun onStop() {
        super.onStop()
        Log.i("MainActivity", "onStop ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("MainActivity", "onRestart ")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.i("MainActivity", "onSaveInstanceState ")
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MainActivity", "onDestroy ")
    }
}

suspend fun testCoroutinesQuiz() {
    val start = System.currentTimeMillis()


    fun log(who: String, msg: String) {
        val t = System.currentTimeMillis() - start
        println("${t}ms | $who | $msg")
    }

    coroutineScope {
        log("OUTER", "before delay 2000")
        delay(2000)
        log("OUTER", "after delay(2000)")
        println("D")
        log("OUTER", "before delay 1000")
        delay(1000)
        log("OUTER", "after delay(1000)")
//        log("OUTER", "before async")
//        val value1 = async {
//            log("CHILD", "started + before delay(1000)")
//            delay(1000)
//            log("CHILD", "after delay(1000) (child finishing)")
//            println("C")
//            log("CHILD", "before A")
//            "A"
//            log("CHILD", "after A")
//        }
        launch {
            log("CHILD", "started + before delay(2000)")
            delay(2000)
            log("CHILD", "after delay(2000) (child finishing)")
            println("B")
            log("CHILD", "started + before delay(2000)-2")
            delay(2000)
            log("CHILD", "after delay(2000) (child finishing)-2")
        }
        println("A")
//        log("OUTER", "before delay(2000)")
//        delay(2000)
//        log("OUTER", "after delay(2000)")

//        log("OUTER", "before await")
//        println(value1.await())
//        log("OUTER", "after await")
    }
    log("OUTER", "after coroutineScope (done)")
}

data class Category(val id: Int, val name: String)
data class Offer(val categoryId: Int, val title: String)

val categories = (1..10).map { Category(it, "Category $it") }

suspend fun fetchOffersForCategory(category: Category): List<Offer> {
    delay(2000)
    return List(5) { i ->
        Offer(categoryId = category.id, title = "Offer ${i + 1} for :${category.name}")
    }
}

val semaphore = Semaphore(5)

suspend fun getOffersAsync() {
    coroutineScope {
        categories.chunked(5).forEach { batch ->
            val batchResult = batch.map {
                async {
//                    semaphore.withPermit {
                    Log.i("Fetch Offer", " Fetching ${it}... ")
                    fetchOffersForCategory(it)
//                    }

                }
            }
            val result = batchResult.awaitAll().flatten()
            Log.i("Fetch Offer", "getOffers:${result.joinToString("\n")} ")
        }
//        val offers = categories.map { category ->
//            async {
//                semaphore.withPermit {
//                    Log.i("Fetch Offer", " Fetching ${category}... ")
//                    fetchOffersForCategory(category)
//                }
//
//            }
//        }

//        val result = offers.awaitAll().flatten()
//        Log.i("Fetch Offer", "getOffers:${result.joinToString("\n")} ")

    }
}

suspend fun getOffers() {
    flowOf(*categories.toTypedArray())
        .flatMapMerge(concurrency = 5) { category ->
            flow {
                Log.i("Fetch Offer", " Fetching ${category}... ")
                val offers = fetchOffersForCategory(category = category)
                offers.forEach { emit(it) }
            }
        }
//        .flatMapConcat { category ->
//            flow {
//                Log.i("Fetch Offer", " Fetching ${category.name}... ")
//                val offers = fetchOffersForCategory(category = category)
//                offers.forEach { emit(it) }
//            }
//        }
        .collect { offer ->
            Log.i("Fetch Offer", "getOffers:$offer ")
        }

//    channelFlow {
//
//    }.collect {  }
}

suspend fun testFlatMapLatest() {
    flowOf(1, 2, 3)
        .flatMapLatest {
            flow {
                emit("$it: A")
                delay(1000)
                emit("$it: B")
                delay(1000)
                emit("$it: C")
            }
        }
        .collect {
            Log.i("TestFlatMapLatest", "Collect-MapLatest:$it ")

        }
}

suspend fun testFlatMapMerged() {
    flowOf(1, 2, 3)
        .flatMapMerge {
            flow {
                emit("$it: A")
//                delay(1000)
                yield()
                emit("$it: B")
//                delay(1000)
                yield()
                emit("$it: C")
            }
        }
        .collect {
            Log.i("TestFlatMapMerged", "Collect-MapMerged:$it ")
        }
}

suspend fun testConcatMap() {

    flowOf(1, 2, 3)
        .flatMapConcat {
            flow {
                emit("$it: A")
                delay(1000)
                emit("$it: B")
                delay(1000)
                emit("$it: C")
            }
        }.collect {
            Log.i("TestConcatMap", "Collect-ConcatMap:$it ")
        }
}

suspend fun testFlow() {
    val time = measureTimeMillis {
        flow {
            for (i in 1..3) {
//                delay(100)
                emit(i)
                Log.i("TestFlow", "[NOT BUFFER] Emit:$i ")
            }
        }.collect {
//            delay(300)
            Log.i("TestFlow", "[NOT BUFFER] Collect:$it ")
        }
    }
    Log.i("TestFlow", "[NOT BUFFER] Completed in time:$time ms ")

    val timeBuffer = measureTimeMillis {
        flow {
            for (i in 1..3) {
                emit(i)
                Log.i("TestFlow", "[BUFFER] Emit:$i ")
            }
        }
            .collect {
                Log.i("TestFlow", "[BUFFER] Collect:$it ")
            }
    }
    Log.i("TestFlow", "[BUFFER] Completed in time:$timeBuffer ms ")

//    val coldFlow = flow {
//        Log.i("TestFlow", "testFlow:Flow started ")
//        repeat(3) {
//            emit(it + 1)
//        }
//    }
//
//    val myFlow = flow {
//        emit(1)
//        emit(2)
//        emit(3)
//    }
//        .map { it * 2 }
//        .filter { it > 2 }
//
//    myFlow.collect {
//        Log.i("TestFlow", "testFlow:Collector 1:$it ")
//    }

//    val hotFlow = MutableSharedFlow<Int>(replay = 3)
//    coroutineScope {
//        launch {
//            hotFlow.emit(1)
//            hotFlow.emit(2)
//            hotFlow.emit(3)
//        }
//
//        launch {
//            hotFlow.collect { Log.i("TestFlow", "testFlow:Collector 1:$it ") }
//        }
//
//        launch {
//            hotFlow.collect { Log.i("TestFlow", "testFlow:Collector 2:$it ") }
//        }
//    }
}

fun testCollections() {
    class Fruits(val fruit: String, val price: Double)
    class FruitsColor(val fruit: String, val color: String)

    // second way
    val anotherwayCollection = listOf(
        Fruits("Eggplant", 4.3),
        Fruits("Tomato", 2.1),
        Fruits("Pineapple", 7.0),
        Fruits("Eggplant", 4.3)
    )

    val result = anotherwayCollection
        .distinct() // eggplant -> tomato -> pineapple
        .zipWithNext { f1, f2 -> // [eggplant, tomato], [tomato, pineapple]
            listOf(
                f1,
                f2
            ).maxBy { it.price } // 1 pair - [eggplant 4.3] 2 pair - [pineapple - 7.0]
        } // result listOf - [eggplant - 4.3, pineapple -7.0]
        .sortedByDescending { it.price } // [pineapple, eggplant]

    val fruitColors = listOf(
        FruitsColor("Peach", "Orange"),
        FruitsColor("Carrot", "Orange"),
        FruitsColor("Lemon", "Yellow"),
        FruitsColor("Banana", "Yellow")
    ).associateWith { it.color }

    Log.i("TestCollection", "testCollections:${fruitColors} ")
}

suspend fun testCoroutines() {
    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("Error", "Caught!")
    }

    supervisorScope {
        val deferred = async {
            throw kotlin.RuntimeException("Error in async")
        }
        try {
            deferred.await()
        } catch (e: Exception) {
            Log.i("TestCoroutines", "Caught in try-catch:$e: ")
        }
    }
}

fun testSum() {
    /**
     * Easy
     *
     * Given an array of integers nums and an integer target,
     * return indices of the two numbers such that they add up to target.
     * You may assume that each input would have exactly one solution, and you may not use the same element twice.
     *
     * You can return the answer in any order.
     * Example 1:
     * Input: nums = [2,7,11,15], target = 9
     * Output: [0,1]
     * Output: Because nums[0] + nums[1] == 9, we return [0, 1].
     */

    val nums = intArrayOf(2, 7, 10, 15)
    val target = 25

    val indexMap = HashMap<Int, Int>()

    for (i in nums.indices) {
        val requiredNumber = target - nums[i]
        if (indexMap.containsKey(requiredNumber)) {
            val result = intArrayOf(indexMap.getValue(requiredNumber), i)
            Log.i("TwoSum", "Indices found: [${result[0]}, ${result[1]}]")
            return
        }

        indexMap[nums[i]] = i
    }
}

fun duplicateTask() {
    /**
     * Given an integer array nums,
     * return true if any value appears at least twice in the array,
     * and return false if every element is distinct.
     *
     * Example 1:
     * Input: nums = [1,2,3,1]
     * Output: true
     */

    val intArray = intArrayOf(1, 2, 3, 4, 4)
    val set: MutableSet<Int> = HashSet()

    for (n in intArray) {
        if (set.contains(n)) {
            return
        }
        set.add(n)
    }
}

fun bestTimeToBuyAndSellStock() {
    /**
     * You are given an array prices where prices[i] is the price of a given stock on the ith day.
     * You want to maximize your profit by choosing a single day to buy one stock and choosing
     * a different day in the future to sell that stock.
     *
     * Return the maximum profit you can achieve from this transaction.
     * If you cannot achieve any profit, return 0.
     *
     * Example 1:
     * Input: prices = [7,1,5,3,6,4]
     * Output: 5
     * Explanation: Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
     * Note that buying on day 2 and selling on day 1 is not allowed because you must buy before you sell.
     */

    /*
    input:
     - array [prices]
     output:
      - max profit
      - return 0 no profit

      calculate max profit:
      1. set minimum price - first price
      2. compare current price with minimum
      3. if current price > min -> maxProfit = current price - min price
      4. if current price < min -> return 0 as no profit
     */

    val prices = intArrayOf(2, 5, 8, 4, 10)
    var maxProfit = 0
    var min = prices[0]

    for (i in 1 until prices.size) {
        if (prices[i] > min) {
            maxProfit = maxProfit.coerceAtLeast(prices[i] - min)
        } else {
            min = prices[i]
            Log.i("Result-minProfir", "minProfit:$min ")
        }
        Log.i("Result", "maxProfit:$maxProfit ")
    }
    Log.i("Result", "Final maxProfit: $maxProfit")
}

fun productOfArrayExceptSelf() {
    /**
     * Given an integer array nums,
     * return an array answer such that answer[i] is equal to the product of all the elements of
     * nums except nums[i].
     * The product of any prefix or suffix of nums is guaranteed to fit in a 32-bit integer.
     *
     * You must write an algorithm that runs in O(n) time and without using the division operation.
     */

    /*
    input: array int
    output: answer - array int where each element = product of all other elements:
    result:
    prefix -> Product of all numbers BEFORE position
    suffix -> Product of all numbers AFTER position
    answer -> prefix * suffix for each position
     */

    val nums = intArrayOf(2, 3, 4, 5)
    val result = IntArray(nums.size)

    // 1: Fill with left products (numbers before each index)
    var leftProduct = 1
    for (i in nums.indices) {
        result[i] = leftProduct
        leftProduct = leftProduct * nums[i]
    }

    // 2: Multiply with right products (numbers after each index)
    var rightProduct = 1
    for (i in nums.size - 1 downTo 0) {
        result[i] = result[i] * rightProduct
        rightProduct = rightProduct * nums[i]
    }

    Log.i("Result", "Final Result:${result.contentToString()} ")
}

fun maxSubArray() {
    /**
     * Given an integer array nums,
     * find the contiguous (Elements must be next to each other) subarray (containing at least one number)
     * which has the largest sum and return its sum.
     *
     * A subarray is a contiguous part of an array.
     *  Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
     */

    val array = intArrayOf(-2, 1, -3, 4, -1, 2, 1, -5, 4)

    var currentSum = array[0]
    var maxSum = array[0]

    var currentSubArray = mutableListOf(array[0])

    val start = 0
    val end: Int = array.size

    val subArray = IntArray(end - start)

    for (i in subArray.indices) {
        subArray[i] = array[start + i]
        currentSubArray.add(array[i])
    }

    Log.i(
        "Result",
        "maxSubArray:${subArray.contentToString()} currentSubArray:$currentSubArray "
    )
}

fun task1() {
    /**
     * Description: Given an array of integers nums and an integer target,
     * return the indices of the two numbers that add up to target. Each input has exactly one
     * solution. You may not use the same element twice.
     *
     * Example:
     *
     *
     * 	Input:  nums = [2, 7, 11, 15], target = 9
     * 	Output: [0, 1]
     * 	Explanation: nums[0] + nums[1] = 2 + 7 = 9
     */

    val inputIntArray = intArrayOf(1, 2, 3, 4, 5)
    val targetInt = 9
    val seen = mutableMapOf<Int, Int>()

    inputIntArray.forEachIndexed { index, num ->
        val compliment = targetInt - num
        seen[compliment]?.let { complimentIndex ->
            Log.i("Task1", "result:${complimentIndex} :${index} ")
        }
        seen[num] = index
    }
}

fun task2() {
    /**
     * #2 — Longest Substring Without Repeating Characters
     *
     * Description: Given a string s, find the length of the longest substring that contains
     * no duplicate characters. A substring is a contiguous sequence of characters.
     *
     * Input:  s = "abcabcbb"
     * Output: 3
     * Explanation: "abc" is the longest substring without repeats
     */

    val string = "abcdfghhasdal"
    var start = 0
    var maxLength = 0
    val uniqueString = mutableSetOf<Char>()

    for (end in string.indices) {
        val currentChar = string[end]

        while (uniqueString.contains(currentChar)) {
            uniqueString.remove(string[start])
            start++
        }

        uniqueString.add(currentChar)
        maxLength = maxOf(maxLength, end - start + 1)
        Log.i("Task2", "Longest: ${uniqueString.toString()}")
    }
    Log.i("Task2", "Longest-Result: ${maxLength}")
}

fun task3() {
    /**
     * Valid Parentheses
     *
     *
     * Description: Given a string containing only '(', ')', '{', '}', '[', ']',
     * determine if the input string is valid. Every open bracket must be closed by the same type
     * in the correct order. An empty string is valid.
     *
     * Example:
     *
     *
     * 	Input:  s = "([{}])"
     * 	Output: true
     *
     * 	Input:  s = "(]"
     * 	Output: false
     */

    val inputString = "([{}])"
    val stack = ArrayDeque<Char>()

    for (ch in inputString) {
        when (ch) {
            '(', '[', '{' -> {
                stack.addLast(ch)
                Log.i("TAG", "task3-addLast: $stack")
            }
            ')', ']', '}' -> {
                if (stack.isEmpty()) Log.i("TAG", "task3: invalid")
                val open = stack.removeLast()
                Log.i("TAG", "task3-removeLast:$open ")
                val ok = (open == '(' && ch == ')') ||
                        (open == '[' && ch == ']') ||
                        (open == '{' && ch == '}')
                if (!ok) Log.i("TAG", "task3: invalid")
            }
            else -> Log.i("TAG", "task3: invalid")
        }
    }
     stack.isEmpty()
}