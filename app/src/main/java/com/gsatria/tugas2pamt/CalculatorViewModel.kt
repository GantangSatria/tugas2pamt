package com.gsatria.tugas2pamt

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class CalculatorViewModel : ViewModel() {

    private val _display = mutableStateOf("0")
    val display: State<String> = _display

    private var firstOperand: Double? = null
    private var operation: String? = null
    private var isNewInput = false

    fun onNumberClick(number: String) {
        if (_display.value == "0" || isNewInput) {
            _display.value = number
            isNewInput = false
        } else {
            _display.value += number
        }
    }

    fun onOperationClick(op: String) {
        firstOperand = _display.value.toDoubleOrNull()
        operation = op
        isNewInput = true
    }

    fun onClear() {
        _display.value = "0"
        firstOperand = null
        operation = null
        isNewInput = false
    }

    fun onEqual() {
        val secondOperand = _display.value.toDoubleOrNull() ?: return
        val result = when (operation) {
            "+" -> (firstOperand ?: 0.0) + secondOperand
            "-" -> (firstOperand ?: 0.0) - secondOperand
            "×" -> (firstOperand ?: 0.0) * secondOperand
            "÷" -> if (secondOperand != 0.0) (firstOperand ?: 0.0) / secondOperand else Double.NaN
            else -> return
        }
        _display.value = result.toString().removeSuffix(".0")
        firstOperand = null
        operation = null
        isNewInput = true
    }
}