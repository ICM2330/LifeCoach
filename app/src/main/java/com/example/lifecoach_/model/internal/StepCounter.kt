package com.example.lifecoach_.model.internal

data class StepCounter(var listener: (steps: Int) -> Unit, var prevStepsCount: Int? = null)