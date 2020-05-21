package com.example.android.androidmarki.ui.home.coordinatedMotion

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentCurveMotionListBinding
import com.example.android.androidmarki.ui.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class CurveMotionListFragment : BaseFragment() {
    private lateinit var binding: FragmentCurveMotionListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCurveMotionListBinding.inflate(inflater, container, false).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                navController = findNavController()
                list.apply {
                    adapter = MessageAdapter(navController)
                    setHasFixedSize(true)
                }
            } else {
                layoutView = binding.root
                showSnackBar(R.string.coordinated_motion_app_name)
            }
        }
        return binding.root
    }

    internal class MessageAdapter(private val navController: NavController) :
        RecyclerView.Adapter<MessageHolder>() {
        private val COLORS = intArrayOf(
            -0x6a9977, -0x7f9876, -0x959878,
            -0xab997d, -0xc09a85, -0xc09a85
        )
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
            return MessageHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.coordinate_motion_message, parent, false)
            )
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onBindViewHolder(holder: MessageHolder, position: Int) {
            val color = COLORS[position % COLORS.size]
            holder.avatar.backgroundTintList = ColorStateList.valueOf(color)

            holder.itemView.setOnClickListener {
                val curve = (position % 2 ) == 0
                val extras = FragmentNavigatorExtras(
                    holder.avatar to holder.avatar.transitionName
                )
                navController.navigate(
                    CurveMotionListFragmentDirections.actionCurveMotionListFragmentToCurvedMotionDetailFragment(
                        color,
                        curve
                    ), extras
                )

            }
        }

        override fun getItemCount() = 32


    }

    internal class MessageHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var avatar: View = itemView.findViewById(R.id.avatar)
        var title: View = itemView.findViewById(R.id.title)
        var subtitle: View = itemView.findViewById(R.id.subtitle)
    }

}
