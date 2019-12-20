package com.roomlivedata.ui.main

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.roomlivedata.R
import com.roomlivedata.databinding.ActivityMainBinding
import com.roomlivedata.databinding.DialogAddUserBinding
import com.roomlivedata.databinding.DialogShowDetailsBinding
import com.roomlivedata.data.model.User

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityMainBinding
    private val model by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setOnClicks()
        observeField()
        createAdapter()
    }

    private fun observeField() {

        model.usersList?.observe(this,
            Observer {

                adapter =
                    UserAdapter(this, it, object : AdapterClickListener {
                        override fun onViewClick(position: Int) {
                            val user = adapter.getItem(position)
                            val showInfoBinding = DataBindingUtil.inflate<DialogShowDetailsBinding>(
                                LayoutInflater.from(this@MainActivity),
                                R.layout.dialog_show_details, null, false
                            )
                            val showInfo = Dialog(this@MainActivity).apply {
                                setContentView(showInfoBinding.root)
                                window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                                setCanceledOnTouchOutside(false)
                            }
                            showInfoBinding.model = user

                            showInfoBinding.btnOk.setOnClickListener {
                                showInfo.dismiss()
                            }
                            showInfo.show()
                        }

                        override fun onEditClick(position: Int) {
                            val user = adapter.getItem(position)
                            Log.i("TAG", user.toString())
                            openDialog(false, user)
                        }

                        override fun onDeleteClick(position: Int) {
                            val user = adapter.getItem(position)
                            model.delete(user)
                        }
                    })

                binding.recyclerList.adapter = adapter

            })

        model.toastMsg.observe(this,
            Observer { showToast(it) })

        model.flagDialog.observe(this,
            Observer {
                if (it)
                    if (dialog != null)
                        if(dialog!!.isShowing)
                            dialog!!.dismiss()

            })
    }

    private fun setOnClicks() {

        binding.add.setOnClickListener {
            openDialog(true, null)
        }

        binding.deleteAll.setOnClickListener {
            model.deleteAll()
        }
    }

    private var dialog: Dialog? = null
    private fun openDialog(flag: Boolean, userUpdate: User?) {

        val dialogBinding = DataBindingUtil.inflate<DialogAddUserBinding>(LayoutInflater.from(this),
                                                    R.layout.dialog_add_user,null,false)

        dialog = Dialog(this).apply {
            setContentView(dialogBinding.root)
            window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            setCanceledOnTouchOutside(false)
            setCancelable(true)
        }

        dialogBinding.activityModel = model

        if (flag) {
            dialogBinding.headerText.text = "Add User"
            dialogBinding.btnAddUpdate.text = "Add"
            model.run {
                name.set("")
                age.set("")
                salary.set("")
            }
        } else {
            dialogBinding.headerText.text = "Update User"
            dialogBinding.btnAddUpdate.text = "Update"
            dialogBinding.edtName.isEnabled = false

            userUpdate!!.let {
                model.run {
                    name.set(userUpdate.name)
                    age.set(userUpdate.age.toString())
                    salary.set(userUpdate.salary.toString())
                }
            }
        }

        dialogBinding.btnAddUpdate.setOnClickListener {
            model.addUpdateUser(flag)
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }

        dialog!!.show()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun createAdapter() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerList.layoutManager = layoutManager
    }
}
