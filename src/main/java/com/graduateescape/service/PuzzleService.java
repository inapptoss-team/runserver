package com.graduateescape.service;

import com.graduateescape.dto.Puzzle;
import com.graduateescape.dto.PuzzleAnswer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PuzzleService {

  // 퍼즐 데이터 저장소
  private final Map<String, Puzzle> puzzles = new ConcurrentHashMap<>();

  // 퍼즐 순서 정의
  private final String[] puzzleOrder = {
      "chair-puzzle", "storage-clue", "cabinet-puzzle", "paper-clue", "mirror-puzzle"
  };

  @PostConstruct
  public void initializePuzzles() {
    // 의자 퍼즐
    Puzzle chairPuzzle = new Puzzle(
        "chair-puzzle",
        "의자 배치",
        "의자들을 클릭해서 원형 탁자에 배치하세요!",
        Puzzle.PuzzleType.DRAG_DROP,
        "10001000");
    chairPuzzle.setSuccessMessage("창고에서 무슨 소리가 난 것 같다.");
    chairPuzzle.setNextScene("storage-sound");
    chairPuzzle.setOrder(0);
    puzzles.put("chair-puzzle", chairPuzzle);

    // 창고 단서
    Puzzle storageClue = new Puzzle(
        "storage-clue",
        "창고",
        "의자 퍼즐을 완료한 후 창고에서 발견한 재료들을 확인해보세요.",
        Puzzle.PuzzleType.STORAGE_CLUE,
        "");
    storageClue.setOrder(1);
    puzzles.put("storage-clue", storageClue);

    // 캐비넷 퍼즐
    Puzzle cabinetPuzzle = new Puzzle(
        "cabinet-puzzle",
        "캐비넷",
        "올바른 원소를 선택하여 캐비넷을 열어주세요.",
        Puzzle.PuzzleType.CABINET_LOCK,
        "Ga");
    cabinetPuzzle.setSuccessMessage("거울 주변에 단서가 생긴 것 같다. \n 확인해보자.");
    cabinetPuzzle.setNextScene("show-paper");
    cabinetPuzzle.setOrder(2);
    puzzles.put("cabinet-puzzle", cabinetPuzzle);

    // 종이 단서
    Puzzle paperClue = new Puzzle(
        "paper-clue",
        "거꾸로 쓰인 종이",
        "거울에 비춰보면 뭔가 보일지도...",
        Puzzle.PuzzleType.PAPER_CLUE,
        "");
    paperClue.setOrder(3);
    puzzles.put("paper-clue", paperClue);

    // 거울 퍼즐
    Puzzle mirrorPuzzle = new Puzzle(
        "mirror-puzzle",
        "거울",
        "각 원소의 원자번호를 순서대로 입력하세요.",
        Puzzle.PuzzleType.MIRROR_CODE,
        "3214");
    mirrorPuzzle.setSuccessMessage("거울 속의 비밀을 풀었습니다!");
    mirrorPuzzle.setNextScene("mirror-unlocked");
    mirrorPuzzle.setOrder(4);
    puzzles.put("mirror-puzzle", mirrorPuzzle);
  }

  /**
   * 모든 퍼즐 조회
   */
  public List<Puzzle> getAllPuzzles() {
    return new ArrayList<>(puzzles.values());
  }

  /**
   * 특정 퍼즐 조회
   */
  public Puzzle getPuzzle(String puzzleId) {
    return puzzles.get(puzzleId);
  }

  /**
   * 플레이어별 퍼즐 상태 조회 (잠금 상태 포함)
   */
  public List<Puzzle> getPuzzlesForPlayer(String playerId, Map<String, Boolean> completedPuzzles) {
    List<Puzzle> playerPuzzles = new ArrayList<>();

    for (String puzzleId : puzzleOrder) {
      Puzzle puzzle = puzzles.get(puzzleId);
      if (puzzle != null) {
        // 복사본 생성하여 플레이어별 상태 설정
        Puzzle playerPuzzle = copyPuzzle(puzzle);

        // 완료 상태 설정
        boolean isCompleted = completedPuzzles.getOrDefault(puzzleId, false);
        playerPuzzle.setCompleted(isCompleted);

        // 잠금 상태 설정 (이전 퍼즐이 완료되지 않으면 잠김)
        boolean isLocked = isPuzzleLocked(puzzleId, completedPuzzles);
        playerPuzzle.setLocked(isLocked);

        playerPuzzles.add(playerPuzzle);
      }
    }

    return playerPuzzles;
  }

  /**
   * 퍼즐 정답 검증
   */
  public boolean validateAnswer(String puzzleId, String answer) {
    Puzzle puzzle = puzzles.get(puzzleId);
    if (puzzle == null) {
      return false;
    }

    // 단서 타입은 항상 통과
    if (puzzle.getType() == Puzzle.PuzzleType.STORAGE_CLUE ||
        puzzle.getType() == Puzzle.PuzzleType.PAPER_CLUE) {
      return true;
    }

    // 정답 비교 (대소문자 무시)
    return puzzle.getAnswer().equalsIgnoreCase(answer.trim());
  }

  /**
   * 퍼즐 잠금 상태 확인
   */
  private boolean isPuzzleLocked(String puzzleId, Map<String, Boolean> completedPuzzles) {
    int puzzleIndex = getPuzzleIndex(puzzleId);
    if (puzzleIndex <= 0)
      return false; // 첫 번째 퍼즐은 항상 열림

    // 이전 퍼즐이 완료되었는지 확인
    String previousPuzzleId = puzzleOrder[puzzleIndex - 1];
    return !completedPuzzles.getOrDefault(previousPuzzleId, false);
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
   * 퍼즐 복사본 생성
   */
  private Puzzle copyPuzzle(Puzzle original) {
    Puzzle copy = new Puzzle();
    copy.setId(original.getId());
    copy.setTitle(original.getTitle());
    copy.setQuestion(original.getQuestion());
    copy.setType(original.getType());
    copy.setSuccessMessage(original.getSuccessMessage());
    copy.setNextScene(original.getNextScene());
    copy.setOrder(original.getOrder());
    // answer는 보안상 복사하지 않음
    return copy;
  }

  /**
   * 퍼즐 순서 조회
   */
  public String[] getPuzzleOrder() {
    return puzzleOrder.clone();
  }
}
