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
    @Deprecated("status를 더이상 사용하지 않음")
    var status: TodoStatus = TodoStatus.TODO,
    var completedAt: LocalDateTime? = null,

    ) {
    companion object {
        fun create(title: String, description: String): Todo {
            val id = Random.nextLong(1, 1000)
            return Todo(id, title, description)
        }
    }

    fun updateCompletedAt(): Todo {
        this.completedAt = LocalDateTime.now()
        return this
    }
}

class TodoService(private val todoRepository: TodoRepository) {
    fun createTodo(title: String, description: String): Todo {
        val item = Todo.create(title, description)
        todoRepository.save(item)
        return item
    }

    fun getTodos(completed: Boolean? = null): List<Todo> = if (completed == null) {
        todoRepository.findAll()
    } else {
        todoRepository.findByComplete(completed)
    }

    fun completeTodo(item: Todo): Todo = item.updateCompletedAt()
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

    fun findByComplete(completed: Boolean): List<Todo> = if (completed) {
        todoList.filter { it.completedAt != null }.toList()
    } else {
        todoList.filter { it.completedAt == null }.toList()
    }

    fun clear() {
        todoList.clear()
    }
}