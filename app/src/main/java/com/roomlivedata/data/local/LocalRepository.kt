package com.roomlivedata.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.roomlivedata.data.model.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LocalRepository(context: Context) : RoomManager {

    private var dao = LocalDatabase.getDatabase(context)!!.userDao()

    override fun getAll(): LiveData<List<User>> {
        return dao.all
    }

    @SuppressLint("CheckResult")
    override suspend fun insert(callback: RoomManager.CallbackManager, user: User) {
        Observable.fromCallable { dao.insertAll(user) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onSetMessage("User added successfully")
            },{
                callback.onSetMessage(it.message!!)
                Log.i("TAG", "Long data : ${it.message}")
            })
    }

    @SuppressLint("CheckResult")
    override suspend fun update(callback: RoomManager.CallbackManager, user: User) {
        Observable.fromCallable { dao.update(user) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onSetMessage("User updated successfully")
            },{
                callback.onSetMessage(it.message!!)
                Log.i("TAG", "Long data : ${it.message}")
            })
    }

    @SuppressLint("CheckResult")
    override suspend fun delete(callback: RoomManager.CallbackManager, user: User) {
        Observable.fromCallable { dao.delete(user) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onSetMessage("User deleted successfully")
            }, { throwable ->
                callback.onSetMessage("Something went wrong...! ${throwable.message}")
            })
    }

    @SuppressLint("CheckResult")
    override fun deleteAll(callback: RoomManager.CallbackManager) {
        Observable.fromCallable { dao.deleteAll() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onSetMessage("Records deleted successfully")
            }, { throwable ->
                callback.onSetMessage("Something went wrong...! ${throwable.message}")
            })
    }
}
