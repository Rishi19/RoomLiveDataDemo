package com.roomlivedata.data.local

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.roomlivedata.data.model.User
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LocalRepository(context: Context) : RoomManager {

    private var dao = LocalDatabase.getDatabase(context)!!.userDao()
    lateinit var disposable: Disposable

    override fun getAll(): LiveData<List<User>> {
        return dao.all
    }

    override suspend fun insert(callback: RoomManager.CallbackManager, user: User) {

        disposable = Completable.fromCallable { dao.insertAll(user) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onSetMessage("User added successfully")
            }, {
                callback.onSetMessage(it.message!!)
                Log.e("TAG", "insert ex : ", it)
            })
    }

    override suspend fun update(callback: RoomManager.CallbackManager, user: User) {

        Log.i("TAG", "User : $user")

        disposable = Completable.fromCallable { dao.update(user) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onSetMessage("User updated successfully")
            }, {
                callback.onSetMessage(it.message!!)
                Log.e("TAG", "update ex : ", it)
            })
    }

    override suspend fun delete(callback: RoomManager.CallbackManager, user: User) {
        disposable = Completable.fromCallable { dao.delete(user) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onSetMessage("User deleted successfully")
            }, {
                callback.onSetMessage(it.message!!)
                Log.e("TAG", "delete ex : ", it)
            })
    }

    override fun deleteAll(callback: RoomManager.CallbackManager) {
        disposable = Completable.fromCallable { dao.deleteAll() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onSetMessage("Records deleted successfully")
            }, {
                callback.onSetMessage(it.message!!)
                Log.e("TAG", "deleteAll ex : ", it)
            })
    }
}
