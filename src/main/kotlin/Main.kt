package org.bonfire.lattuse

import java.util.Scanner

// interface
interface Identifiable {
    val id: Int
}


data class Task(
    override val id: Int,
    var title: String,
    var description: String,
    var priority: Int,    // 1 = Low, 2 = Medium, 3 = High
    var status: String    // TODO, IN_PROGRESS, DONE
) : Identifiable


class TaskManager {
    private val tasks = mutableListOf<Task>()
    private var nextId = 1

    // create
    fun addTask(title: String, description: String, priority: Int, status: String) {
        if (title.isBlank()) {
            println("Title cannot be empty.")
            return
        }

        val task = Task(nextId++, title.trim(), description.trim(), priority, status)
        tasks.add(task)
        println("Task added.")
    }

    // read
    fun listTasks() {
        if (tasks.isEmpty()) {
            println("No tasks available.")
            return
        }

        tasks.forEach {
            println("ID: ${it.id}, Title: ${it.title}, Priority: ${priorityName(it.priority)}, Status: ${it.status}")
        }
    }

    // update
    fun updateTask(id: Int, newTitle: String?, newStatus: String?) {
        val task = tasks.find { it.id == id }
        if (task == null) {
            println("Task not found.")
            return
        }

        newTitle?.let { task.title = it }
        newStatus?.let { task.status = it }

        println("Task updated.")
    }

    // delete
    fun deleteTask(id: Int) {
        val removed = tasks.removeIf { it.id == id }
        if (removed) println("Task deleted.")
        else println("Task not found.")
    }

    // lambdas / higher-order functions
    fun filterByStatus(status: String) {
        val result = tasks.filter { it.status.equals(status, ignoreCase = true) }
        if (result.isEmpty()) println("No tasks with status $status.")
        else result.forEach { println("${it.title} (${priorityName(it.priority)})") }
    }

    fun sortByPriority() {
        val sorted = tasks.sortedByDescending { it.priority }
        sorted.forEach {
            println("${it.title} - ${priorityName(it.priority)}")
        }
    }

    // helper
    private fun priorityName(p: Int): String {
        return when (p) {
            1 -> "Low"
            2 -> "Medium"
            3 -> "High"
            else -> "Unknown"
        }
    }
}


fun main() {
    val scanner = Scanner(System.`in`)
    val manager = TaskManager()
    var running = true

    while (running) {
        println("\n=== Personal Task Tracker ===")
        println("1. Add task")
        println("2. List tasks")
        println("3. Update task")
        println("4. Delete task")
        println("5. Filter tasks by status")
        println("6. Sort tasks by priority")
        println("0. Exit")
        print("Choose option: ")

        when (scanner.nextLine()) {
            "1" -> {
                print("Title: ")
                val title = scanner.nextLine()

                print("Description: ")
                val desc = scanner.nextLine()

                print("Priority (1-Low, 2-Medium, 3-High): ")
                val priority = scanner.nextLine().toIntOrNull()

                print("Status (TODO / IN_PROGRESS / DONE): ")
                val status = scanner.nextLine().uppercase()

                if (priority == null || priority !in 1..3) {
                    println("Invalid priority.")
                } else {
                    manager.addTask(title, desc, priority, status)
                }
            }

            "2" -> manager.listTasks()

            "3" -> {
                print("Task ID: ")
                val id = scanner.nextLine().toIntOrNull()

                print("New title (leave empty): ")
                val title = scanner.nextLine().trim().ifBlank { null }

                print("New status (leave empty): ")
                val status = scanner.nextLine().trim().ifBlank { null }

                id?.let { manager.updateTask(it, title, status) }
                    ?: println("Invalid ID.")
            }

            "4" -> {
                print("Task ID: ")
                scanner.nextLine().toIntOrNull()
                    ?.let { manager.deleteTask(it) }
                    ?: println("Invalid ID.")
            }

            "5" -> {
                print("Enter status: ")
                manager.filterByStatus(scanner.nextLine())
            }

            "6" -> manager.sortByPriority()

            "0" -> {
                println("Goodbye!")
                running = false
            }

            else -> println("Invalid option.")
        }
    }
}
