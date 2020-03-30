package com.roomlivedata.ui.main

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.roomlivedata.data.local.LocalRepository
import com.roomlivedata.data.local.RoomManager
import com.roomlivedata.data.model.User
import kotlinx.coroutines.launch
import org.jetbrains.annotations.NotNull

class MainViewModel(@NotNull aapContext: Application) : AndroidViewModel(aapContext) {

    private var roomRepository = LocalRepository(aapContext)

    var usersList: LiveData<List<User>>? = null
    var toastMsg = MutableLiveData<String>()
    var flagDialog = MutableLiveData<Boolean>()

    var name = ObservableField("")
    var age = ObservableField("")
    var salary = ObservableField("")

    val user = User()

    init {
        usersList = roomRepository.getAll()
    }

    fun addUpdateUser(flag: Boolean) {

        if (name.get()?.length == 0) {
            toastMsg.value = "First name is blank"
            return
        } else if (age.get()?.length == 0) {
            toastMsg.value = "Age is blank"
            return
        } else if (salary.get()?.length == 0) {
            toastMsg.value = "Salary is blank"
            return
        } else {

            user.name = name.get()!!
            user.age = age.get()!!.toInt()
            user.salary = salary.get()!!.toInt()

            if (flag) {
                viewModelScope.launch {
                    roomRepository.insert(object : RoomManager.CallbackManager {
                        override fun onSetMessage(msg: String) {
                            if (msg.contains("UNIQUE constraint failed")) toastMsg.value =
                                "Record already found..."
                            else {
                                toastMsg.value = msg
                                flagDialog.value = true
                            }
                        }
                    }, user)
                }
            } else {
                viewModelScope.launch {
                    roomRepository.update(object : RoomManager.CallbackManager {
                        override fun onSetMessage(msg: String) {
                            toastMsg.value = msg
                            flagDialog.value = true
                        }
                    }, user)
                }
            }
        }
    }

    fun delete(user: User) = viewModelScope.launch {
        roomRepository.delete(object : RoomManager.CallbackManager {
            override fun onSetMessage(msg: String) {
                toastMsg.value = msg
            }
        }, user)
    }

    fun deleteAll() = viewModelScope.launch {
        roomRepository.deleteAll(object : RoomManager.CallbackManager {
            override fun onSetMessage(msg: String) {
                toastMsg.value = msg
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        roomRepository.disposable.dispose()
    }

}