package matvey.springtodolist.controller;

import lombok.RequiredArgsConstructor;

import matvey.springtodolist.model.Board;
import matvey.springtodolist.model.Task;
import matvey.springtodolist.service.AuthService;
import matvey.springtodolist.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final AuthService authService;

    @PostMapping("/{boardId}/add_task")
    public ResponseEntity<Board> addTaskToBoard(@PathVariable String boardId, @RequestParam String title) throws IOException {
        return ResponseEntity.ok(boardService.addTaskToBoard(boardId, title));
    }

    @PostMapping("/add")
    public ResponseEntity<Board> addBoard(@RequestParam String title) {
        return ResponseEntity.ok(boardService.addBoard(title));
    }

    @GetMapping("/get_boards")
    public ResponseEntity<List<Board>> getCurrentUserBoards() {
        return ResponseEntity.ok(boardService.getBoardsByUserId(authService.getCurrentUserId()));
    }

    @GetMapping("/{boardId}/tasks")
    public ResponseEntity<List<Task>> getTasksByBoard(@PathVariable String boardId) {
        return  ResponseEntity.ok(boardService.getTasksFromBoard(boardId));
    }
}
