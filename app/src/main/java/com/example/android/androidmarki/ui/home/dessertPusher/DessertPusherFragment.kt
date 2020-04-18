package com.example.android.androidmarki.ui.home.dessertPusher

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.*
import androidx.core.app.ShareCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentDessertPusherBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import timber.log.Timber

/** onSaveInstanceState Bundle Keys **/
const val KEY_REVENUE = "revenue_key"
const val KEY_DESSERT_SOLD = "dessert_sold_key"
const val KEY_TIMER_SECONDS = "timer_seconds_key"

class DessertPusherFragment : BaseFragment(), LifecycleObserver {

    private lateinit var dessertTimer: DessertTimer
    private val _amountSold = MutableLiveData(0)
    val amountSold: LiveData<Int> = _amountSold
    private val _revenue = MutableLiveData(0)
    val revenue: LiveData<Int> = _revenue

  lateinit var  timer : MutableLiveData<Int>

    // Contains all the views
    private lateinit var binding: FragmentDessertPusherBinding

    /** Dessert Data **/

    /**
     * Simple data class that represents a dessert. Includes the resource id integer associated with
     * the image, the price it's sold for, and the startProductionAmount, which determines when
     * the dessert starts to be produced.
     */

    // Create a list of all desserts, in order of when they start being produced
    private val allDesserts = listOf(
        Dessert(R.drawable.dessert_pusher_cupcake, 5, 0),
        Dessert(R.drawable.dessert_pusher_donut, 10, 5),
        Dessert(R.drawable.dessert_pusher_eclair, 15, 20),
        Dessert(R.drawable.dessert_pusher_froyo, 30, 50),
        Dessert(R.drawable.dessert_pusher_gingerbread, 50, 100),
        Dessert(R.drawable.dessert_pusher_honeycomb, 100, 200),
        Dessert(R.drawable.dessert_pusher_icecreamsandwich, 500, 500),
        Dessert(R.drawable.dessert_pusher_jellybean, 1000, 1000),
        Dessert(R.drawable.dessert_pusher_kitkat, 2000, 2000),
        Dessert(R.drawable.dessert_pusher_lollipop, 3000, 4000),
        Dessert(R.drawable.dessert_pusher_marshmallow, 4000, 8000),
        Dessert(R.drawable.dessert_pusher_nougat, 5000, 16000),
        Dessert(R.drawable.dessert_pusher_oreo, 6000, 20000)
    )

    private val _currentDessert = MutableLiveData(allDesserts[0])
    val currentDessert: LiveData<Dessert> = _currentDessert

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDessertPusherBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            desertPusher = this@DessertPusherFragment
        }
        setHasOptionsMenu(true)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dessertTimer = DessertTimer(this.lifecycle)
        timer = dessertTimer.secondsCount

        // If there is a savedInstanceState bundle, then you're "restarting" the activity
        // If there isn't a bundle, then it's a "fresh" start
        if (savedInstanceState != null) {
            // Get all the game state information from the bundle, set it
            _revenue.value = savedInstanceState.getInt(KEY_REVENUE, 0)
            _amountSold.value = savedInstanceState.getInt(KEY_DESSERT_SOLD, 0)
            dessertTimer.secondsCount.value = savedInstanceState.getInt(KEY_TIMER_SECONDS, 0)
            showCurrentDessert()
        }
    }


    /**
     * Updates the score when the dessert is clicked. Possibly shows a new dessert.
     */
    fun onDessertClicked() {
        // Update the score
        _revenue.value = _currentDessert.value?.price?.let { _revenue.value?.plus(it) }
        _amountSold.value = _amountSold.value?.plus(1)

        // Show the next dessert
        showCurrentDessert()
    }

    /**
     * Determine which dessert to show.
     */
    private fun showCurrentDessert() {
        var newDessert = allDesserts[0]
        for (dessert in allDesserts) {
            if (_amountSold.value!! >= dessert.startProductionAmount) {
                newDessert = dessert
            }
            // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
            // you'll start producing more expensive desserts as determined by startProductionAmount
            // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
            // than the amount sold.
            else break
        }

        // If the new dessert is actually different than the current dessert, update the image
        if (newDessert != _currentDessert.value) {
            _currentDessert.value = newDessert
        }
    }

    /**
     * Menu methods
     */
    private fun onShare() {
        val shareIntent = activity?.let {
            ShareCompat.IntentBuilder.from(it)
                .setText(getString(R.string.share_text, _amountSold.value, _revenue.value))
                .setType("text/plain")
                .intent
        }
        try {
            startActivity(shareIntent)
        } catch (ex: ActivityNotFoundException) {
            showSnackBar(R.string.sharing_not_available)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dessert_pusher_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shareMenuButton -> onShare()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Called when the user navigates away from the app but might come back
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_REVENUE, _revenue.value!!)
        outState.putInt(KEY_DESSERT_SOLD, _amountSold.value!!)
        outState.putInt(KEY_TIMER_SECONDS, dessertTimer.secondsCount.value!!)
        Timber.i("onSaveInstanceState Called")
        super.onSaveInstanceState(outState)
    }


}
