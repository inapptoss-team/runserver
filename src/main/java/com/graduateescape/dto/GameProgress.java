package com.graduateescape.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameProgress {
    private String playerId;
    private String[] completedPuzzles;
    private int currentStep;
    private long lastUpdated;
    private String sessionId;

    // 기본 생성자
    public GameProgress() {
        this.completedPuzzles = new String[0];
        this.currentStep = 0;
        this.lastUpdated = System.currentTimeMillis();
    }

    // 생성자
    public GameProgress(String playerId) {
        this();
        this.playerId = playerId;
        this.sessionId = generateSessionId();
    }

    private String generateSessionId() {
        return "session_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    // Getter/Setter
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    public String[] getCompletedPuzzles() { return completedPuzzles; }
    public void setCompletedPuzzles(String[] completedPuzzles) { this.completedPuzzles = completedPuzzles; }

    public int getCurrentStep() { return currentStep; }
    public void setCurrentStep(int currentStep) { this.currentStep = currentStep; }

    public long getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    // 헬퍼 메서드
    public void completePuzzle(String puzzleId) {
        if (!isPuzzleCompleted(puzzleId)) {
            String[] newCompleted = new String[completedPuzzles.length + 1];
            System.arraycopy(completedPuzzles, 0, newCompleted, 0, completedPuzzles.length);
            newCompleted[completedPuzzles.length] = puzzleId;
            completedPuzzles = newCompleted;
            lastUpdated = System.currentTimeMillis();
        }
    }

    public boolean isPuzzleCompleted(String puzzleId) {
        for (String completed : completedPuzzles) {
            if (completed.equals(puzzleId)) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        this.completedPuzzles = new String[0];
        this.currentStep = 0;
        this.lastUpdated = System.currentTimeMillis();
        this.sessionId = generateSessionId();
    }
}
