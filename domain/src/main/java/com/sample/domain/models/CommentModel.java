package com.sample.domain.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comments")
public class CommentModel {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "image_id")
    public String imageId;

    @ColumnInfo(name = "comment")
    public String comment;
}
