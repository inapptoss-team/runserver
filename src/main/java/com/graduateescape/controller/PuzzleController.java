package com.graduateescape.controller;

import com.graduateescape.dto.Puzzle;
import com.graduateescape.dto.PuzzleAnswer;
import com.graduateescape.service.GameService;
import com.graduateescape.service.PuzzleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/puzzle")
@CrossOrigin(origins = "*", allowCredentials = "false")
public class PuzzleController {

  @Autowired
  private PuzzleService puzzleService;

  @Autowired
  private GameService gameService;

  /**
   * 모든 퍼즐 정보 조회 (정답 제외)
   */
  @GetMapping("/all")
  public ResponseEntity<List<Puzzle>> getAllPuzzles() {
    List<Puzzle> puzzles = puzzleService.getAllPuzzles();
    return ResponseEntity.ok(puzzles);
  }

  /**
   * 특정 플레이어의 퍼즐 상태 조회
   */
  @GetMapping("/player/{playerId}")
  public ResponseEntity<List<Puzzle>> getPlayerPuzzles(@PathVariable String playerId) {
    // 플레이어의 완료된 퍼즐 정보 가져오기
    var completedPuzzles = gameService.getCompletedPuzzlesMap(playerId);

    // 퍼즐 상태 포함하여 반환
    List<Puzzle> playerPuzzles = puzzleService.getPuzzlesForPlayer(playerId, completedPuzzles);
    return ResponseEntity.ok(playerPuzzles);
  }

  /**
   * 특정 퍼즐 정보 조회 (정답 제외)
   */
  @GetMapping("/{puzzleId}")
  public ResponseEntity<Puzzle> getPuzzle(@PathVariable String puzzleId) {
    Puzzle puzzle = puzzleService.getPuzzle(puzzleId);
    if (puzzle == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(puzzle);
  }

  /**
   * 퍼즐 정답 제출 및 검증
   */
  @PostMapping("/submit")
  public ResponseEntity<Map<String, Object>> submitAnswer(@RequestBody PuzzleAnswer puzzleAnswer) {
    Map<String, Object> response = new HashMap<>();

    // 퍼즐 존재 여부 확인
    Puzzle puzzle = puzzleService.getPuzzle(puzzleAnswer.getPuzzleId());
    if (puzzle == null) {
      response.put("success", false);
      response.put("message", "퍼즐을 찾을 수 없습니다.");
      return ResponseEntity.badRequest().body(response);
    }

    // 퍼즐 잠금 상태 확인
    boolean isLocked = gameService.isPuzzleLocked(puzzleAnswer.getPlayerId(), puzzleAnswer.getPuzzleId());
    if (isLocked) {
      response.put("success", false);
      response.put("message", "이 퍼즐은 아직 잠겨있습니다.");
      response.put("isLocked", true);
      return ResponseEntity.ok(response);
    }

    // 퍼즐 완료 상태 확인
    boolean isCompleted = gameService.isPuzzleCompleted(puzzleAnswer.getPlayerId(), puzzleAnswer.getPuzzleId());
    if (isCompleted) {
      response.put("success", true);
      response.put("message", "이미 완료된 퍼즐입니다.");
      response.put("isCompleted", true);
      response.put("puzzle", puzzle);
      return ResponseEntity.ok(response);
    }

    // 정답 검증
    boolean isCorrect = puzzleService.validateAnswer(puzzleAnswer.getPuzzleId(), puzzleAnswer.getAnswer());

    if (isCorrect) {
      // 퍼즐 완료 처리
      gameService.completePuzzle(puzzleAnswer.getPlayerId(), puzzleAnswer.getPuzzleId());

      response.put("success", true);
      response.put("message", puzzle.getSuccessMessage() != null ? puzzle.getSuccessMessage() : "퍼즐을 완료했습니다!");
      response.put("nextScene", puzzle.getNextScene());
      response.put("puzzle", puzzle);

      // 업데이트된 진행 상태 포함
      response.put("progress", gameService.getProgress(puzzleAnswer.getPlayerId()));

    } else {
      response.put("success", false);
      response.put("message", "틀렸습니다. 다시 시도해보세요.");
    }

    return ResponseEntity.ok(response);
  }

  /**
   * 퍼즐 잠금 상태 확인
   */
  @GetMapping("/status/{playerId}/{puzzleId}")
  public ResponseEntity<Map<String, Object>> getPuzzleStatus(
      @PathVariable String playerId,
      @PathVariable String puzzleId) {

    Map<String, Object> response = new HashMap<>();

    Puzzle puzzle = puzzleService.getPuzzle(puzzleId);
    if (puzzle == null) {
      response.put("exists", false);
      return ResponseEntity.notFound().build();
    }

    response.put("exists", true);
    response.put("isLocked", gameService.isPuzzleLocked(playerId, puzzleId));
    response.put("isCompleted", gameService.isPuzzleCompleted(playerId, puzzleId));
    response.put("puzzle", puzzle);

    return ResponseEntity.ok(response);
  }

  /**
   * 퍼즐 순서 조회
   */
  @GetMapping("/order")
  public ResponseEntity<String[]> getPuzzleOrder() {
    String[] order = puzzleService.getPuzzleOrder();
    return ResponseEntity.ok(order);
  }
}
