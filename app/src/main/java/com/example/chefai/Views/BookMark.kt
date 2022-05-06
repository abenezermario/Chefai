package com.example.chefai.Views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chefai.R
import com.example.chefai.dto.RecipeData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.fragment_book_mark.*


class BookMark : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dbRef: DatabaseReference
    private lateinit var recipeDataList: ArrayList<RecipeData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_mark, container, false)
        bookMarkData.layoutManager = LinearLayoutManager(activity)
        bookMarkData.setHasFixedSize(true)

        recipeDataList = arrayListOf<RecipeData>()
        getDataFromFirebase()

        // Inflate the layout for this fragment
        return view
    }

    private fun getDataFromFirebase() {
// Read from the database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Response")
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val rec_data = data.getValue(RecipeData::class.java)
                        recipeDataList.add(rec_data!!)
                    }

                    bookMarkData.adapter = MyAdapter(recipeDataList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Failed", "Failed to read value.", error.toException())
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = BookMark()
    }


}