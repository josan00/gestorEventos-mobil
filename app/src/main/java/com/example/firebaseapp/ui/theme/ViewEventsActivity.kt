package com.example.firebaseapp.ui.theme

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebaseapp.R

class ViewEventsActivity : AppCompatActivity() {

    private lateinit var eventsListView: ListView
    private lateinit var eventAdapter: EventAdapter
    private val taskRepository = TaskRepository(ApiService.create())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_events)

        eventsListView = findViewById(R.id.taskListView)
        eventAdapter = EventAdapter(this, emptyList())
        eventsListView.adapter = eventAdapter

        loadEvents()
    }

    private fun loadEvents() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val events = taskRepository.getTasks()
                withContext(Dispatchers.Main) {
                    updateEventsList(events)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ViewEventsActivity,
                        "Error al cargar eventos: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun updateEventsList(events: List<Task>) {
        eventAdapter.clear()
        eventAdapter.addAll(events)
        eventAdapter.notifyDataSetChanged()
    }

}