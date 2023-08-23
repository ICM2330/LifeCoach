package com.example.lifecoach_.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.lifecoach_.R
import com.example.lifecoach_.model.habits.Habit
import com.example.lifecoach_.model.habits.RunningHabit
import com.example.lifecoach_.model.habits.StepsHabit
import com.example.lifecoach_.model.habits.StrengthHabit
import com.example.lifecoach_.model.habits.TimeControlHabit

class HabitListViewAdapter (context : Context, habits : MutableList<Habit>) :
    ArrayAdapter<Habit>(context, 0, habits) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        val item = getItem(position)

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.habit_dashboard_layout, parent, false)
        }

        when (item){
            is StepsHabit -> {
                val imgCheck = itemView!!.findViewById<ImageView>(R.id.checkImage)
                imgCheck.setImageResource(R.drawable.checkpositive)

                val imgHabit = itemView!!.findViewById<ImageView>(R.id.imageHabit)
                imgHabit.setImageResource(R.drawable.corriendo)

                val textProgress = itemView!!.findViewById<TextView>(R.id.progressHabitText)
                textProgress.setText("50 pasos")

                val goalProgress = itemView!!.findViewById<TextView>(R.id.goalHabitText)
                goalProgress.setText("100 pasos")
            }
            is RunningHabit -> {
                val imgCheck = itemView!!.findViewById<ImageView>(R.id.checkImage)
                imgCheck.setImageResource(R.drawable.checknegative)

                val imgHabit = itemView!!.findViewById<ImageView>(R.id.imageHabit)
                imgHabit.setImageResource(R.drawable.running)

                val textProgress = itemView!!.findViewById<TextView>(R.id.progressHabitText)
                textProgress.setText("1 hora")

                val goalProgress = itemView!!.findViewById<TextView>(R.id.goalHabitText)
                goalProgress.setText("30 minutos")
            }
            is TimeControlHabit -> {
                val imgCheck = itemView!!.findViewById<ImageView>(R.id.checkImage)
                imgCheck.setImageResource(R.drawable.checknegative)

                val imgHabit = itemView!!.findViewById<ImageView>(R.id.imageHabit)
                imgHabit.setImageResource(R.drawable.clock)

                val textProgress = itemView!!.findViewById<TextView>(R.id.progressHabitText)
                textProgress.setText("1 hora")

                val goalProgress = itemView!!.findViewById<TextView>(R.id.goalHabitText)
                goalProgress.setText("40 minutos")
            }
            is StrengthHabit -> {
                val imgCheck = itemView!!.findViewById<ImageView>(R.id.checkImage)
                imgCheck.setImageResource(R.drawable.checkpositive)

                val imgHabit = itemView!!.findViewById<ImageView>(R.id.imageHabit)
                imgHabit.setImageResource(R.drawable.healthydashboard)

                val textProgress = itemView!!.findViewById<TextView>(R.id.progressHabitText)
                textProgress.setText("Completado!")

                val goalProgress = itemView!!.findViewById<TextView>(R.id.goalHabitText)
                goalProgress.setText("Completado!")
            }
            is Habit -> {
                val imgCheck = itemView!!.findViewById<ImageView>(R.id.checkImage)
                imgCheck.setImageResource(R.drawable.checkpositive)

                val imgHabit = itemView!!.findViewById<ImageView>(R.id.imageHabit)
                imgHabit.setImageResource(R.drawable.pencildashboard)

                val textProgress = itemView!!.findViewById<TextView>(R.id.progressHabitText)
                textProgress.setText("Completado")

                val goalProgress = itemView!!.findViewById<TextView>(R.id.goalHabitText)
                goalProgress.setText("Completado")
            }
        }

        return itemView!!
    }
}

