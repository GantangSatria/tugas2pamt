package com.gsatria.tugas2pamt

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import java.util.Stack

class CalculatorViewModel : ViewModel() {

    private val _display = mutableStateOf("0")
    val display: State<String> = _display

    private var expression = ""

    fun onNumberClick(number: String) {
        if (_display.value == "0") {
            _display.value = number
        } else {
            _display.value += number
        }
        expression += number
    }

    fun onOperationClick(op: String) {
        if (expression.isNotEmpty() && expression.last().toString() in listOf("+", "-", "×", "÷")) {
            expression = expression.dropLast(1) + op
            _display.value = _display.value.dropLast(1) + op
        } else {
            _display.value += op
            expression += op
        }
    }

    fun onClear() {
        _display.value = "0"
        expression = ""
    }

    fun onEqual() {
        if (expression.isEmpty()) return

        try {
            val result = evaluateExpression(expression)
            _display.value = result.toString().removeSuffix(".0")
            expression = result.toString().removeSuffix(".0") // supaya bisa lanjut hitungan
        } catch (e: Exception) {
            _display.value = "Error"
            expression = ""
        }
    }

    private fun evaluateExpression(expr: String): Double {
        val tokens = mutableListOf<String>()
        var currentNumber = ""

        for (char in expr) {
            if (char.isDigit() || char == '.') {
                currentNumber += char
            } else {
                if (currentNumber.isNotEmpty()) {
                    tokens.add(currentNumber)
                    currentNumber = ""
                }
                tokens.add(char.toString())
            }
        }
        if (currentNumber.isNotEmpty()) tokens.add(currentNumber)

        val values = Stack<Double>()
        val ops = Stack<String>()

        fun applyOp() {
            val b = values.pop()
            val a = values.pop()
            val op = ops.pop()
            val res = when (op) {
                "+" -> a + b
                "-" -> a - b
                "×" -> a * b
                "÷" -> a / b
                else -> 0.0
            }
            values.push(res)
        }

        val precedence = mapOf("+" to 1, "-" to 1, "×" to 2, "÷" to 2)

        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> values.push(token.toDouble())
                token in listOf("+", "-", "×", "÷") -> {
                    while (ops.isNotEmpty() && precedence[ops.peek()]!! >= precedence[token]!!) {
                        applyOp()
                    }
                    ops.push(token)
                }
            }
        }

        while (ops.isNotEmpty()) {
            applyOp()
        }

        return values.pop()
    }
}
