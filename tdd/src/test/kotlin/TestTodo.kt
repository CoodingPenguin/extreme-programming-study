package todo.service

import io.kotest.core.annotation.Isolate
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.example.TodoRepository
import org.example.TodoService

@Isolate
class TestTodo : StringSpec({

    "제목과 설명이 주어지면 할일이 생성된다." {
        val todoRepository = TodoRepository()
        val todoService = TodoService(todoRepository)

        val title = "테스트코드 작성"
        val description = "테스트코드를 처음 작성해봅니다."
        val item = todoService.createTodo(title, description)
        item.title shouldBe title
        item.description shouldBe description
    }

    "할일 생성 시 고유한 ID가 부여된다." {
        val todoRepository = TodoRepository()
        val todoService = TodoService(todoRepository)

        val title = "테스트코드 작성"
        val description = "테스트코드를 처음 작성해봅니다."
        val item = todoService.createTodo(title, description)
        item.id.shouldBeInstanceOf<Long>()
    }

    "할일을 완료 상태로 변경할 수 있다." {
        val todoRepository = TodoRepository()
        val todoService = TodoService(todoRepository)

        val title = "테스트코드 작성"
        val description = "테스트코드를 처음 작성해봅니다."
        val item = todoService.createTodo(title, description)

        item.completedAt shouldBe null
        val completedItem = todoService.completeTodo(item)
        completedItem.completedAt shouldNotBe null
    }

    "생성된 할일 목록을 조회할 수 있다." {
        val todoRepository = TodoRepository()
        val todoService = TodoService(todoRepository)

        val title1 = "테스트1"
        val description1 = "설명1"
        val item1 = todoService.createTodo(title1, description1)

        val title2 = "테스트2"
        val description2 = "설명2"
        val item2 = todoService.createTodo(title2, description2)

        val items = todoService.getTodos()
        items.size shouldBe 2
        items[0].title shouldBe title1
        items[0].description shouldBe description1
        items[1].title shouldBe title2
        items[1].description shouldBe description2
    }

    "할일 목록을 상태로 필터링할 수 있다." {
        val todoRepository = TodoRepository()
        val todoService = TodoService(todoRepository)

        val title1 = "테스트1"
        val description1 = "설명1"
        val item1 = todoService.createTodo(title1, description1)

        val title2 = "테스트2"
        val description2 = "설명2"
        val item2 = todoService.createTodo(title2, description2)

        val title3 = "테스트3"
        val description3 = "설명3"
        val item3 = todoService.createTodo(title3, description3)
        todoService.completeTodo(item3)

        val todoStatusItems = todoService.getTodos(completed = false)
        todoStatusItems.size shouldBe 2

        val doneStatusItems = todoService.getTodos(completed = true)
        doneStatusItems.size shouldBe 1
    }

    "할일 상태가 완료로 변경되면 완료 시간이 기록된다." {
        val todoRepository = TodoRepository()
        val todoService = TodoService(todoRepository)

        val title = "테스트코드 작성"
        val description = "테스트코드를 처음 작성해봅니다."
        val item = todoService.createTodo(title, description)
        item.completedAt shouldBe null

        val completedItem = todoService.completeTodo(item)
        completedItem.completedAt shouldNotBe null
    }
})