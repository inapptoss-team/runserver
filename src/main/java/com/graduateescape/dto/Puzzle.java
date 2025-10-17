package com.graduateescape.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Puzzle {
  private String id;
  private String title;
  private String question;
  private PuzzleType type;
  private String answer;
  private String successMessage;
  private String nextScene;
  private boolean isLocked;
  private boolean isCompleted;
  private int order;

  // 기본 생성자
  public Puzzle() {
  }

  // 생성자
  public Puzzle(String id, String title, String question, PuzzleType type, String answer) {
    this.id = id;
    this.title = title;
    this.question = question;
    this.type = type;
    this.answer = answer;
    this.isLocked = false;
    this.isCompleted = false;
  }

  // Getter/Setter
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public PuzzleType getType() {
    return type;
  }

  public void setType(PuzzleType type) {
    this.type = type;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public String getSuccessMessage() {
    return successMessage;
  }

  public void setSuccessMessage(String successMessage) {
    this.successMessage = successMessage;
  }

  public String getNextScene() {
    return nextScene;
  }

  public void setNextScene(String nextScene) {
    this.nextScene = nextScene;
  }

  @JsonProperty("isLocked")
  public boolean isLocked() {
    return isLocked;
  }

  public void setLocked(boolean locked) {
    isLocked = locked;
  }

  @JsonProperty("isCompleted")
  public boolean isCompleted() {
    return isCompleted;
  }

  public void setCompleted(boolean completed) {
    isCompleted = completed;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  // 퍼즐 타입 열거형
  public enum PuzzleType {
    DRAG_DROP("drag-drop"),
    CABINET_LOCK("cabinet-lock"),
    MIRROR_CODE("mirror-code"),
    STORAGE_CLUE("storage-clue"),
    PAPER_CLUE("paper-clue"),
    TEXT_INPUT("text-input");

    private final String value;

    PuzzleType(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return value;
    }
  }
}
