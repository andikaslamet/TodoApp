package com.example.todoapp.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg todo: Todo)

    @Query("select * from todo WHERE is_done = 0 ORDER BY priority DESC")
    fun selectAllTodo(): List<Todo>

    @Query("select * from todo where uuid= :id")
    fun selectTodo(id: Int): Todo

    @Delete
    fun deleteTodo(todo: Todo)

    @Query("UPDATE todo SET title=:title, notes=:notes, priority=:priority WHERE uuid = :id")
    fun update(title: String, notes: String, priority: Int, id: Int)

    @Query("UPDATE todo SET is_done = 1 WHERE uuid = :id")
    fun done(id: Int)

    //cara dosen pendek update
    @Update
    fun updateTodo(todo: Todo)
    //
}

