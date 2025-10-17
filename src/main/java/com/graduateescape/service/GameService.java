package com.graduateescape.service;

import com.graduateescape.dto.GameProgress;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

  // 메모리 기반 저장소 (실제 운영에서는 Redis 또는 데이터베이스 사용)
  private final Map<String, GameProgress> gameProgressMap = new ConcurrentHashMap<>();

  // 퍼즐 순서 정의
  private final String[] puzzleOrder = {
      "chair-puzzle", "storage-clue", "cabinet-puzzle", "paper-clue", "mirror-puzzle"
  };

  /**
   * 게임 진행 상태 조회
   */
  public GameProgress getProgress(String playerId) {
    return gameProgressMap.computeIfAbsent(playerId, GameProgress::new);
  }

  /**
   * 게임 진행 상태 저장
   */
  public GameProgress saveProgress(GameProgress progress) {
    progress.setLastUpdated(System.currentTimeMillis());
    gameProgressMap.put(progress.getPlayerId(), progress);
    return progress;
  }

  /**
   * 퍼즐 완료 처리
   */
  public GameProgress completePuzzle(String playerId, String puzzleId) {
    GameProgress progress = getProgress(playerId);
    progress.completePuzzle(puzzleId);

    // 현재 단계 업데이트
    int puzzleIndex = getPuzzleIndex(puzzleId);
    if (puzzleIndex >= 0) {
      progress.setCurrentStep(Math.max(progress.getCurrentStep(), puzzleIndex + 1));
    }

    return saveProgress(progress);
  }

  /**
   * 퍼즐 잠금 상태 확인
   */
  public boolean isPuzzleLocked(String playerId, String puzzleId) {
    GameProgress progress = getProgress(playerId);
    int puzzleIndex = getPuzzleIndex(puzzleId);

    if (puzzleIndex < 0)
      return false;

    return puzzleIndex > progress.getCurrentStep();
  }

  /**
   * 퍼즐 완료 상태 확인
   */
  public boolean isPuzzleCompleted(String playerId, String puzzleId) {
    GameProgress progress = getProgress(playerId);
    return progress.isPuzzleCompleted(puzzleId);
  }

  /**
   * 게임 진행 상태 리셋
   */
  public GameProgress resetProgress(String playerId) {
    GameProgress progress = getProgress(playerId);
    progress.reset();
    return saveProgress(progress);
  }

  /**
   * 모든 퍼즐 잠금 해제
   */
  public GameProgress unlockAll(String playerId) {
    GameProgress progress = getProgress(playerId);
    progress.setCompletedPuzzles(puzzleOrder.clone());
    progress.setCurrentStep(puzzleOrder.length);
    return saveProgress(progress);
  }

  /**
   * 퍼즐 인덱스 조회
   */
  private int getPuzzleIndex(String puzzleId) {
    for (int i = 0; i < puzzleOrder.length; i++) {
      if (puzzleOrder[i].equals(puzzleId)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * 모든 진행 상태 조회 (관리용)
   */
  public Map<String, GameProgress> getAllProgress() {
    return new HashMap<>(gameProgressMap);
  }

  /**
   * 완료된 퍼즐 맵 조회
   */
  public Map<String, Boolean> getCompletedPuzzlesMap(String playerId) {
    GameProgress progress = getProgress(playerId);
    Map<String, Boolean> completedMap = new HashMap<>();

    for (String puzzleId : puzzleOrder) {
      completedMap.put(puzzleId, progress.isPuzzleCompleted(puzzleId));
    }

    return completedMap;
  }

  /**
   * 오래된 세션 정리
   */
  public void cleanupOldSessions(long maxAge) {
    long currentTime = System.currentTimeMillis();
    gameProgressMap.entrySet().removeIf(entry -> currentTime - entry.getValue().getLastUpdated() > maxAge);
  }
}
