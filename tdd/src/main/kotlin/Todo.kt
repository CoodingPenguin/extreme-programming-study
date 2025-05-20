package org.example

import java.time.LocalDateTime
import kotlin.random.Random

enum class TodoStatus {
    TODO,
    DONE
}

data class Todo(
    val id: Long,
    val title: String,
    val description: String,
    var status: TodoStatus = TodoStatus.TODO,
    var doneAt: LocalDateTime? = null
) {
    companion object {
        fun create(title: String, description: String): Todo {
            val id = Random.nextLong(1, 1000)
            return Todo(id, title, description)
        }
    }

    fun updateStatus(status: TodoStatus) {
        this.status = status
        if (status == TodoStatus.DONE) {
            this.doneAt = LocalDateTime.now()
        }
    }
}

class TodoService(private val todoRepository: TodoRepository) {
    fun createTodo(title: String, description: String): Todo {
        val item = Todo.create(title, description)
        todoRepository.save(item)
        return item
    }

    fun getTodos(status: TodoStatus? = null): List<Todo> = if (status == null) {
        todoRepository.findAll()
    } else {
        todoRepository.findByStatus(status)
    }

    fun changeStatus(item: Todo, status: TodoStatus): Todo {
        item.updateStatus(status)
        return item
    }
}


class TodoRepository {
    private val todoList = mutableListOf<Todo>()

    fun save(todo: Todo): Todo {
        todoList.add(todo)
        return todo
    }

    fun findAll(): List<Todo> {
        return todoList.toList()
    }

    fun findByStatus(status: TodoStatus): List<Todo> {
        return todoList.filter { it.status == status }.toList()
    }

    fun clear() {
        todoList.clear()
    }
}