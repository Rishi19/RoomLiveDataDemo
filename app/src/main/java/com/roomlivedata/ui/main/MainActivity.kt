package com.roomlivedata.ui.main

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private var adapter: UserAdapter? = null
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
                            val user = adapter?.getItem(position)
                            val showInfoBinding = DataBindingUtil.inflate<DialogShowDetailsBinding>(
                                LayoutInflater.from(this@MainActivity),
                                R.layout.dialog_show_details, null, false
                            )
                            val showInfo = Dialog(this@MainActivity).apply {
                                setContentView(showInfoBinding.root)
                                window!!.setLayout(400, WindowManager.LayoutParams.WRAP_CONTENT)
                                setCanceledOnTouchOutside(false)
                            }
                            showInfoBinding.model = user

                            showInfoBinding.btnOk.setOnClickListener {
                                showInfo.dismiss()
                            }
                            showInfo.show()
                        }

                        override fun onEditClick(position: Int) {
                            val user = adapter?.getItem(position)
                            openDialog(false, user!!)
                        }

                        override fun onDeleteClick(position: Int) {
                            val user = adapter!!.getItem(position)
                            Observable.fromCallable { model?.delete(user) }
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    showToast("User deleted successfully")
                                }, { throwable ->
                                    showToast("Something went wrong...! ${throwable.message}")
                                })
                        }
                    })

                binding.recyclerList?.adapter = adapter
//                if (dialog != null)
//                    if(dialog!!.isShowing)
//                        dialog!!.dismiss()
            })
    }

    private fun setOnClicks() {

        binding.add?.setOnClickListener {
            openDialog(true, null)
        }

        binding.deleteAll.setOnClickListener {
            Observable.fromCallable { model.deleteAll() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showToast("Records deleted")
                },{ throwable ->
                    showToast("Something went wrong...! ${throwable.message}")
                })
        }
    }

    private var dialog: Dialog? = null
    private fun openDialog(flag: Boolean, userUpdate: User?) {

        val dialogBinding = DataBindingUtil.inflate<DialogAddUserBinding>(LayoutInflater.from(this),
                                                    R.layout.dialog_add_user,null,false)

        dialog = Dialog(this).apply {
            setContentView(dialogBinding.root)
            window!!.setLayout(400, WindowManager.LayoutParams.WRAP_CONTENT)
            setCanceledOnTouchOutside(false)
            setCancelable(true)
        }

        if (flag) {
            dialogBinding.headerText.text = "Add User"
            dialogBinding.btnAddUpdate.text = "Add"
        } else {
            dialogBinding.headerText.text = "Update User"
            dialogBinding.btnAddUpdate.text = "Update"
            dialogBinding.model = userUpdate
        }

        dialogBinding.btnAddUpdate.setOnClickListener {

            val edtName = dialogBinding.edtName.text.toString()
            val edtAge = dialogBinding.edtAge.text.toString()
            val edtSalary = dialogBinding.edtSalary.text.toString()

            when {
                edtName.isEmpty() -> showToast("Name is blank")
                edtAge.isEmpty() -> showToast("Age is blank")
                edtSalary.isEmpty() -> showToast("Salary is blank")
                else -> {
                    val user = User().apply {
                        name = edtName
                        age = Integer.parseInt(edtAge)
                        salary = Integer.parseInt(edtSalary)
                    }

                    if (flag) {
                        Observable.fromCallable { model.insertUser(user) }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                showToast("User added successfully")
                            },{
                                if (it.message!!.contains("UNIQUE constraint failed"))
                                    showToast("Record already found...")
                                else
                                    showToast("Something went wrong...! ${it.message}")
                            })
                    } else {

                        Observable.fromCallable { model.update(user) }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                showToast("User updated successfully")
                            },{
                                if (it.message!!.contains("UNIQUE constraint failed"))
                                    showToast("Record already found...")
                                else
                                    showToast("Something went wrong...! ${it.message}")
                            })
                    }
                }
            }
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
        binding.recyclerList?.layoutManager = layoutManager
    }
}
