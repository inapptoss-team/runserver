package com.graduateescape.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PuzzleAnswer {
  private String playerId;
  private String puzzleId;
  private String answer;
  private long timestamp;

  // 기본 생성자
  public PuzzleAnswer() {
    this.timestamp = System.currentTimeMillis();
  }

  // 생성자
  public PuzzleAnswer(String playerId, String puzzleId, String answer) {
    this();
    this.playerId = playerId;
    this.puzzleId = puzzleId;
    this.answer = answer;
  }

  // Getter/Setter
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public String getPuzzleId() {
    return puzzleId;
  }

  public void setPuzzleId(String puzzleId) {
    this.puzzleId = puzzleId;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
}
