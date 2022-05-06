package com.example.chefai.Views

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.chefai.R
import com.example.chefai.dto.RecipeData
import com.example.chefai.dto.UserData
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.recepie_fragment.*
import kotlinx.android.synthetic.main.recepie_fragment.view.*

class RecipeFragment : DialogFragment() {
    lateinit var mDatabase: FirebaseDatabase
    /** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.recepie_fragment, container, false)

        // Inflate the layout to use as dialog or embedded fragment
        val bundle = arguments

        if (bundle != null) {
            view.generatedValue.text = bundle.getString("RESPONSE")
            view.recipeTitle.title = bundle.getString("TITLE")

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeTitle.setNavigationOnClickListener { view ->
            // Navigate somewhere
            dismiss()
        }

        val bundle = arguments
        view.saveRecipe.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("Response")


            // Write a message to the database
            val recipeData = RecipeData(bundle?.getString("TITLE"),bundle?.getString("RESPONSE"))

            myRef.push().setValue(recipeData)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Posted Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            it.exception?.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            Toast.makeText(view.context, "Saved to Bookmark", Toast.LENGTH_SHORT).show()
            Log.d("saved", recipeData.recipeTitle.toString())
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar, menu)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }


}