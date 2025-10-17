package com.graduateescape.controller;

import com.graduateescape.dto.GameProgress;
import com.graduateescape.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/game")
@CrossOrigin(origins = "*", allowCredentials = "false")
public class GameController {

  @Autowired
  private GameService gameService;

  /**
   * 게임 진행 상태 조회
   */
  @GetMapping("/progress/{playerId}")
  public ResponseEntity<GameProgress> getProgress(@PathVariable String playerId) {
    GameProgress progress = gameService.getProgress(playerId);
    return ResponseEntity.ok(progress);
  }

  /**
   * 게임 진행 상태 저장
   */
  @PostMapping("/progress")
  public ResponseEntity<GameProgress> saveProgress(@RequestBody GameProgress progress) {
    GameProgress savedProgress = gameService.saveProgress(progress);
    return ResponseEntity.ok(savedProgress);
  }

  /**
   * 퍼즐 완료 처리
   */
  @PostMapping("/complete-puzzle")
  public ResponseEntity<Map<String, Object>> completePuzzle(
      @RequestParam String playerId,
      @RequestParam String puzzleId) {

    GameProgress progress = gameService.completePuzzle(playerId, puzzleId);

    Map<String, Object> response = new HashMap<>();
    response.put("success", true);
    response.put("progress", progress);
    response.put("message", "퍼즐이 완료되었습니다!");

    return ResponseEntity.ok(response);
  }

  /**
   * 게임 진행 상태 리셋
   */
  @PostMapping("/reset/{playerId}")
  public ResponseEntity<Map<String, Object>> resetProgress(@PathVariable String playerId) {
    GameProgress progress = gameService.resetProgress(playerId);

    Map<String, Object> response = new HashMap<>();
    response.put("success", true);
    response.put("progress", progress);
    response.put("message", "게임 진행 상태가 리셋되었습니다.");

    return ResponseEntity.ok(response);
  }

  /**
   * 모든 퍼즐 잠금 해제 (개발용)
   */
  @PostMapping("/unlock-all/{playerId}")
  public ResponseEntity<Map<String, Object>> unlockAll(@PathVariable String playerId) {
    GameProgress progress = gameService.unlockAll(playerId);

    Map<String, Object> response = new HashMap<>();
    response.put("success", true);
    response.put("progress", progress);
    response.put("message", "모든 퍼즐이 잠금 해제되었습니다.");

    return ResponseEntity.ok(response);
  }

  /**
   * 퍼즐 잠금 상태 확인
   */
  @GetMapping("/puzzle-status/{playerId}/{puzzleId}")
  public ResponseEntity<Map<String, Object>> getPuzzleStatus(
      @PathVariable String playerId,
      @PathVariable String puzzleId) {

    boolean isLocked = gameService.isPuzzleLocked(playerId, puzzleId);

    Map<String, Object> response = new HashMap<>();
    response.put("puzzleId", puzzleId);
    response.put("isLocked", isLocked);
    response.put("isCompleted", gameService.isPuzzleCompleted(playerId, puzzleId));

    return ResponseEntity.ok(response);
  }

  /**
   * 게임 정보 조회
   */
  @GetMapping("/info")
  public ResponseEntity<Map<String, Object>> getGameInfo() {
    Map<String, Object> info = new HashMap<>();
    info.put("gameName", "연구실 탈출하기");
    info.put("version", "1.0.0");
    info.put("totalPuzzles", 5);
    info.put("puzzleOrder", new String[] {
        "chair-puzzle", "storage-clue", "cabinet-puzzle", "paper-clue", "mirror-puzzle"
    });

    return ResponseEntity.ok(info);
  }
}
