package com.te.pewala.main_files.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.te.pewala.R
import com.te.pewala.db.AuthViewModel
import com.te.pewala.db.DBViewModel
import com.te.pewala.main_files.adapters.PeopleAdapter
import com.te.pewala.main_files.items.PeopleItems
import com.te.pewala.main_files.slider_files.SliderAdapter
import com.google.firebase.firestore.DocumentSnapshot
import com.smarteist.autoimageslider.SliderView

class Home : Fragment() {

    private lateinit var imageUrl: ArrayList<String>
    private lateinit var sliderView: SliderView
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var send: LinearLayout
    private lateinit var add: LinearLayout
    private lateinit var receive: LinearLayout
    private lateinit var redeem: LinearLayout
    private lateinit var viewWallet: ImageButton
    private lateinit var peopleAdapter: PeopleAdapter
    private var peopleItemsArray = arrayListOf<PeopleItems>()
    private lateinit var recyclerview: RecyclerView
    private lateinit var authViewModel: AuthViewModel
    private lateinit var dbViewModel: DBViewModel
    private lateinit var peopleText: TextView
    private lateinit var peopleLayout: CardView
    private lateinit var shop: CardView
    private lateinit var userType: String
    private lateinit var userStatus: String
    private lateinit var mainLayout: ScrollView
    private lateinit var loader: LottieAnimationView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        dbViewModel = ViewModelProvider(this)[DBViewModel::class.java]

        viewWallet = view.findViewById(R.id.view_wallet)
        send = view.findViewById(R.id.send_money)
        add = view.findViewById(R.id.add_money)
        receive = view.findViewById(R.id.receive_money)
        redeem = view.findViewById(R.id.redeem_money)
        recyclerview = view.findViewById(R.id.people_recyclerview_home)
        peopleLayout = view.findViewById(R.id.peopleLayout_home)
        peopleText = view.findViewById(R.id.people_text_home)
        shop = view.findViewById(R.id.shop_home)
        mainLayout = view.findViewById(R.id.mainLayout_home)
        loader = view.findViewById(R.id.progressbar_home)

        loadData()

//        dbViewModel.addAccount("5100D0C7B6F0", "123456")
//        dbViewModel.addAccount("5100C8940409", "123456")
//        dbViewModel.addAccount("5100C7B9D5FA", "123456")
//        dbViewModel.addAccount("5100C7FCBCD6", "123456")
//        dbViewModel.addAccount("5100D15B06DD", "123456")

        peopleAdapter = PeopleAdapter(requireContext(), peopleItemsArray)
        recyclerview.layoutManager = GridLayoutManager(view.context, 3)
        recyclerview.setHasFixedSize(true)
        recyclerview.setItemViewCacheSize(20)
        recyclerview.adapter = peopleAdapter

        sliderView = view.findViewById(R.id.slider)

        imageUrl = ArrayList()
        imageUrl =
            (imageUrl + "https://firebasestorage.googleapis.com/v0/b/my-chat-app-98801.appspot.com/o/13416072_5243336.png?alt=media&token=5190cbdc-9b47-4b21-b443-c6905da38d96") as ArrayList<String>
        imageUrl =
            (imageUrl + "https://firebasestorage.googleapis.com/v0/b/my-chat-app-98801.appspot.com/o/20824349_6342757.png?alt=media&token=94e820f8-2cfa-4848-b543-8b1c512e1738") as ArrayList<String>
        imageUrl =
            (imageUrl + "https://firebasestorage.googleapis.com/v0/b/my-chat-app-98801.appspot.com/o/9457133_4137379.png?alt=media&token=e14c5339-d2a9-47c8-a04b-4c7222c01ffa") as ArrayList<String>
        imageUrl =
            (imageUrl + "https://firebasestorage.googleapis.com/v0/b/my-chat-app-98801.appspot.com/o/20827766_Hand%20holding%20phone%20with%20digital%20wallet%20service%20and%20sending%20money.png?alt=media&token=bb05263a-4cf6-4848-b946-e59db5983ea8") as ArrayList<String>

        sliderAdapter = SliderAdapter(imageUrl)
        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        sliderView.setSliderAdapter(sliderAdapter)
        sliderView.scrollTimeInSec = 3
        sliderView.isAutoCycle = true
        sliderView.startAutoCycle()

        val navBuilder = NavOptions.Builder()
        navBuilder.setEnterAnim(R.anim.fade_in).setExitAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.fade_in).setPopExitAnim(R.anim.fade_out)

        shop.setOnClickListener {
//            Toast.makeText(requireContext(), "This feature is not implemented yet", Toast.LENGTH_SHORT).show()
            val bundle = Bundle()
            bundle.putString("userType", userType)
            requireFragmentManager().popBackStack()
            if (userType == "Seller") {
                when (userStatus) {
                    "Not verified" -> Navigation.findNavController(view)
                        .navigate(R.id.nav_seller_doc, null, navBuilder.build())

                    "Checking" ->
                        Navigation.findNavController(view)
                            .navigate(R.id.nav_waiting, null, navBuilder.build())

                    else -> Navigation.findNavController(view)
                        .navigate(R.id.nav_shop_seller, bundle, navBuilder.build())
                }
            } else Navigation.findNavController(view)
                .navigate(R.id.nav_shop_buyer, bundle, navBuilder.build())
//            Navigation.findNavController(view)
//                .navigate(R.id.nav_address)
        }

        add.setOnClickListener {
            requireFragmentManager().popBackStack()
            Navigation.findNavController(view).navigate(R.id.nav_add, null, navBuilder.build())
        }

        send.setOnClickListener {
            requireFragmentManager().popBackStack()
            Navigation.findNavController(view).navigate(R.id.nav_send, null, navBuilder.build())
        }

        receive.setOnClickListener {
            requireFragmentManager().popBackStack()
            Navigation.findNavController(view).navigate(R.id.nav_receive, null, navBuilder.build())
        }

        redeem.setOnClickListener {
            requireFragmentManager().popBackStack()
            Navigation.findNavController(view).navigate(R.id.nav_redeem, null, navBuilder.build())
        }

        viewWallet.setOnClickListener {
            requireFragmentManager().popBackStack()
            Navigation.findNavController(view).navigate(R.id.nav_wallet, null, navBuilder.build())
        }

        return view
    }

    private fun fetchData(list: ArrayList<DocumentSnapshot>) {
        peopleText.visibility = View.GONE
        peopleLayout.visibility = View.GONE
        peopleItemsArray = arrayListOf()
        for (i in list) {
            if (i.exists()) {
                val peopleData = PeopleItems(
                    i.getString("Name"),
                    i.getString("Phone No"),
                    i.getString("Image Url"),
                    i.getString("Uid")
                )
                peopleItemsArray.add(peopleData)
                peopleText.visibility = View.VISIBLE
                peopleLayout.visibility = View.VISIBLE
            }
        }
        peopleAdapter.updatePeople(peopleItemsArray)
    }

    private fun loadData() {
        authViewModel.userdata.observe(viewLifecycleOwner) {
            if (it != null) {
                dbViewModel.updateTransactorDetails(it.uid)
                dbViewModel.fetchContacts(it.uid)
                dbViewModel.contactDetails.observe(viewLifecycleOwner) {
                    if (it.isNotEmpty()) {
                        fetchData(it)
                    } else {
                        peopleText.visibility = View.GONE
                        peopleLayout.visibility = View.GONE
                    }
                }
                dbViewModel.fetchAccountDetails(it.uid)
                dbViewModel.accDetails.observe(viewLifecycleOwner) { list ->
                    if (list.exists()) {
                        userType = list.getString("User").toString()
                        userStatus = list.getString("Status").toString()
                        mainLayout.visibility = View.VISIBLE
                        loader.visibility = View.GONE
                    }
                }
            }
        }
    }

}