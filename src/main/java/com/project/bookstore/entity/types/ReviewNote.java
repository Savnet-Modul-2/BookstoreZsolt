package com.project.bookstore.entity.types;

public enum ReviewNote {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);
    final int note;

    ReviewNote(int note) {
        this.note = note;
    }

    public int getNote() {
        return note;
    }
}
