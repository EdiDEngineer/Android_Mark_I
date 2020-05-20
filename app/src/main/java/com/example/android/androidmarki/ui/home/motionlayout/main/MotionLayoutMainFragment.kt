package com.example.android.androidmarki.ui.home.motionlayout.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentMotionLayoutBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.home.HomeActivity
import kotlin.reflect.KClass

/**
 * A simple [Fragment] subclass.
 */
data class Step(
    val number: String,
    val name: String,
    val caption: String,
    val navDirections: NavDirections,
    val highlight: Boolean = false)

private val data = listOf(
    Step("Step 1",
        "Animations with Motion Layout",
        "Learn how to build a basic animation with Motion Layout. This will crash until you complete the step in the codelab.",
        MotionLayoutFragmentDirections.actionMotionLayoutFragmentToMotionLayoutStep1Fragment()
    ),

    Step("Steps 2-7",
        "Animating based on drag events, Modifying a path, Building complex paths, Changing attributes with motion, Changing custom attributes, OnSwipe with complex paths.",
        "Learn how to control animations with drag events. This will not display any animation until you complete the step in the codelab, Learn how to use KeyFrames to modify a path between start and end, Learn how to use KeyFrames to build complex paths through multiple KeyFrames, Learn how to resize and rotate views during animations, Learn how to change custom attributes during motion, Learn how to control motion through complex paths with OnSwipe.",
        MotionLayoutFragmentDirections.actionMotionLayoutFragmentToMotionLayoutStep27Fragment(),
        highlight = true
    ),

    Step("Step 8",
        "Implements running motion with code",
        "Changes applied from step 8",
        MotionLayoutFragmentDirections.actionMotionLayoutFragmentToMotionLayoutStep8Fragment(),
        highlight = true
    )
)

class MotionLayoutFragment : BaseFragment() {
private lateinit var binding: FragmentMotionLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as HomeActivity).supportActionBar?.show()

        binding = FragmentMotionLayoutBinding.inflate(inflater, container,false).apply {
        navController = findNavController()
            motionLayoutRecyclerView.adapter =  MainAdapter(data,navController)
        }
        return binding.root
    }


}

class MainAdapter(val data: List<Step>, val navController: NavController) : RecyclerView.Adapter<MainViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.motion_layout_list_item, parent, false)
        return MainViewHolder(view as CardView)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(data[position], navController )
    }

}

class MainViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView) {
    val header: TextView = cardView.findViewById(R.id.header)
    val description: TextView = cardView.findViewById(R.id.description)
    val caption: TextView = cardView.findViewById(R.id.caption)

    fun bind(step: Step, navController: NavController) {
        header.text = step.number
        description.text = step.name
        caption.text = step.caption
        val context = cardView.context
        cardView.setOnClickListener {
            navController.navigate(step.navDirections)
        }

        val color = if (step.highlight) {
            context.resources.getColor(R.color.secondaryLightColor)
        } else {
            context.resources.getColor(R.color.colorPrimary)
        }
        header.setTextColor(color)
        description.setTextColor(color)
    }

}
