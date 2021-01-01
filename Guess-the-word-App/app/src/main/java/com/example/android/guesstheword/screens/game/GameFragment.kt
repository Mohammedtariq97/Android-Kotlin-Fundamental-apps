package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

class GameFragment : Fragment() {

    private lateinit var binding: GameFragmentBinding
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )
        Log.i("GameFragment", "Called ViewModelProvider.get")
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        // Set the viewmodel for databinding - this allows the bound layout access
        // to all the data in the ViewModel
        binding.gameViewModel = viewModel
        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        // Replacing LiveData observer method for 'word'
        binding.lifecycleOwner = viewLifecycleOwner

        // Observer for the Game finished event
        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer<Boolean> { hasFinished ->
            if (hasFinished) gameFinished()
        })

        return binding.root
    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        Toast.makeText(activity, "Game has just finished", Toast.LENGTH_SHORT).show()
        val action = GameFragmentDirections.actionGameToScore()
        action.score = viewModel.score.value?:0
        NavHostFragment.findNavController(this).navigate(action)
        viewModel.onGameFinishComplete()
    }
}
