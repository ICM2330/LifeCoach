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

        val habitName = itemView!!.findViewById<TextView>(R.id.habitName)
        val imgCheck = itemView.findViewById<ImageView>(R.id.checkImage)
        val imgHabit = itemView.findViewById<ImageView>(R.id.imageHabit)
        val objective = itemView.findViewById<TextView>(R.id.objective)
        val objectiveTV = itemView.findViewById<TextView>(R.id.objectiveText)
        val notification = itemView.findViewById<TextView>(R.id.notificationTime)

        var objectiveTxt = ""
        var img = 0
        when (item){
            is StepsHabit -> {
                img = R.drawable.stepscreate
                objectiveTxt = "${item.objectiveSteps} pasos"
            }
            is RunningHabit -> {
                img = R.drawable.foot
                objectiveTxt = "${item.objectiveMins} minutos"
            }
            is TimeControlHabit -> {
                img = R.drawable.clock
                objectiveTxt = "${item.objectiveMins} minutos"
            }
            is StrengthHabit -> {
                img = R.drawable.muscle_logo
                objectiveTxt = item.muscularGroup
                objectiveTV.text = "Grupo Muscular"
            }
            is Habit -> {
                img = R.drawable.pencil
                if (item.doneToday()) objectiveTxt = "Completado!"
                else objectiveTxt = "Pendiente"
                objectiveTV.text = "Estado"
            }
        }

        // HABIT NAME
        habitName.text = item?.name
        // HABIT IMAGE
        imgHabit.setImageResource(img)
        // HABIT ACC
        if(item!!.doneToday()) imgCheck.setImageResource(R.drawable.checkpositive)
        else imgCheck.setImageResource(R.drawable.checknegative)
        // HABIT OB
        objective.text = objectiveTxt
        // HABIT NOTIFICATION
        notification.text = item.frequency.toString()

        return itemView
    }
}

